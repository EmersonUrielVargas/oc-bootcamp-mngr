package com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter;

import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import com.onclass.bootcamp.domain.exceptions.BusinessException;
import com.onclass.bootcamp.domain.exceptions.TechnicalException;
import com.onclass.bootcamp.domain.model.BootcampCapabilities;
import com.onclass.bootcamp.domain.spi.CapabilitiesGateway;
import com.onclass.bootcamp.domain.utilities.CustomPage;
import com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.dto.CapabilityMngrProperties;
import com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.dto.request.CapabilityAssign;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.onclass.bootcamp.infrastructure.adapters.capabilitiesadapter.util.Constants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapabilityMngrAdapter implements CapabilitiesGateway {

    private final WebClient webClient;
    private final CapabilityMngrProperties capabilityMngrProperties;
    private final Retry retry;
    private final Bulkhead bulkhead;

    private Mono<Throwable> buildErrorResponse(ClientResponse response, TechnicalMessage technicalMessage) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty(NO_ADITIONAL_ERROR_DETAILS)
                .flatMap(errorBody -> {
                    log.error(STRING_ERROR_BODY_DATA, errorBody);
                    return Mono.error(
                            response.statusCode().is5xxServerError() ?
                                    new TechnicalException(technicalMessage):
                                    new BusinessException(technicalMessage));
                });
    }

    @Override
    @CircuitBreaker(name = "capabilityMngr", fallbackMethod = "fallback")
    public Mono<Void> assignCapabilitiesToBootcamp(Long bootcampId, List<Long> capabilitiesIds) {
        log.info(LOG_START_ASSIGN_CAPABILITIES, capabilitiesIds, bootcampId);
        CapabilityAssign requestBody = new CapabilityAssign(bootcampId, capabilitiesIds);
        return webClient.post()
            .uri(uriBuilder -> uriBuilder
                    .path(CAPABILITY_MNGR_PATH_ASSIGN)
                    .build())
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.CAPABILITIES_NOT_FOUND))
            .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.ERROR_ASSIGN_CAPABILITIES))
            .bodyToMono(Void.class)
            .doOnNext(response -> log.info("Received API response : {}", response))
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(mono -> Mono.defer(() -> bulkhead.executeSupplier(() -> mono)))
            .doOnTerminate(() -> log.info("Completed assign capabilities in bootcamp"))
            .doOnError(error -> log.error("Error occurred in capacity mngr: {}", error.getMessage()));
    }

    @Override
    public Mono<List<BootcampCapabilities>> getCapabilitiesByBootcampsIds(List<Long> bootcampIds) {
        log.info(LOG_START_GET_CAPABILITIES_BY_BOOTCAMPS_IDS, bootcampIds);
        return webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path(CAPABILITY_MNGR_PATH_GET_BY_BOOTCAMPS)
                .build())
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .bodyValue(bootcampIds)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.CAPABILITIES_NOT_FOUND))
            .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.ERROR_ASSIGN_CAPABILITIES))
            .bodyToMono(new ParameterizedTypeReference<List<BootcampCapabilities>>() {})
            .doOnNext(response -> log.info("Received API response : {}", response))
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(mono ->
                Mono.defer(() ->
                    bulkhead.executeSupplier(() -> mono)
                )
            )
            .doOnTerminate(() -> log.info("Completed getting capabilities in bootcamp"))
            .doOnError(error -> log.error("Error occurred in capacity mngr: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomPage<BootcampCapabilities>> getSortCapabilitiesByBootcamps(String order, Integer size, Integer page) {
        log.info(LOG_START_GET_CAPABILITIES_PAGINATION, order, page, size);
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam(QUERY_PARAM_ORDER_SORT, order)
                .queryParam(QUERY_PARAM_PAGE, page)
                .queryParam(QUERY_PARAM_SIZE, size)
                .build())
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.CAPABILITIES_NOT_FOUND))
            .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.ERROR_ASSIGN_CAPABILITIES))
            .bodyToMono(new ParameterizedTypeReference<CustomPage<BootcampCapabilities>>() {})
            .doOnNext(response -> log.info("Received API response : {}", response))
            .transformDeferred(RetryOperator.of(retry))
            .transformDeferred(mono ->
                Mono.defer(() ->
                    bulkhead.executeSupplier(() -> mono)
                )
            )
            .doOnTerminate(() -> log.info("Completed getting capabilities in bootcamp"))
            .doOnError(error -> log.error("Error occurred in capacity mngr: {}", error.getMessage()));
    }

    public Mono<Throwable> fallback(Throwable t) {
        return Mono.defer(() ->
                Mono.justOrEmpty(t instanceof TimeoutException
                                ? new TechnicalException(TechnicalMessage.INTERNAL_ERROR)
                                : t)
        );
    }
}
