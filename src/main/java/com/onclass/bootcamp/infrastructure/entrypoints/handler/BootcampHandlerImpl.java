package com.onclass.bootcamp.infrastructure.entrypoints.handler;

import com.onclass.bootcamp.domain.api.BootcampServicePort;
import com.onclass.bootcamp.domain.enums.ItemSortList;
import com.onclass.bootcamp.domain.enums.OrderList;
import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import com.onclass.bootcamp.domain.exceptions.BusinessException;
import com.onclass.bootcamp.domain.exceptions.TechnicalException;
import com.onclass.bootcamp.infrastructure.entrypoints.dto.request.CreateBootcampDTO;
import com.onclass.bootcamp.infrastructure.entrypoints.mapper.BootcampMapper;
import com.onclass.bootcamp.infrastructure.entrypoints.util.Constants;
import com.onclass.bootcamp.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootcampHandlerImpl {

    private final BootcampServicePort bootcampServicePort;
    private final BootcampMapper bootcampMapper;

    public Mono<ServerResponse> createBootcamp(ServerRequest request) {
        return request.bodyToMono(CreateBootcampDTO.class)
                .flatMap(bootcampDTO -> bootcampServicePort.registerBootcamp(bootcampMapper.toBootcamp(bootcampDTO))
                        .doOnSuccess(savedTechnology -> log.info(Constants.BOOTCAMP_CREATED_RS_OK))
                )
                .flatMap(savedBootcamp -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(TechnicalMessage.BOOTCAMP_CREATED.getMessage()))
                .doOnError(ex -> log.error(Constants.BOOTCAMP_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getTechnicalMessage()))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getTechnicalMessage()))
                .onErrorResume(ex -> {
                    log.error(Constants.UNEXPECTED_ERROR, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR);
                });
    }

     public Mono<ServerResponse> getAllBootcamps(ServerRequest request) {
        String order = request.queryParam(Constants.QUERY_PARAM_ORDER_SORT).orElse(OrderList.ASCENDANT.getMessage());
        String itemToSort = request.queryParam(Constants.QUERY_PARAM_ITEM_SORT).orElse(ItemSortList.NAME.getMessage());
        Integer page = Integer.parseInt(request.queryParam(Constants.QUERY_PARAM_PAGE).orElse(Constants.DEFAULT_PAGE_PAGINATION));
        Integer size = Integer.parseInt(request.queryParam(Constants.QUERY_PARAM_SIZE).orElse(Constants.DEFAULT_SIZE_PAGINATION));

        return bootcampServicePort.listBootcamps(OrderList.fromString(order),ItemSortList.fromString(itemToSort),page,size)
                .flatMap(pageBootcamps -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(pageBootcamps))
                .doOnError(ex -> log.error(Constants.BOOTCAMP_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.CONFLICT,
                        ex.getTechnicalMessage()))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getTechnicalMessage()))
                .onErrorResume(ex -> {
                    log.error(Constants.UNEXPECTED_ERROR, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR);
                });
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, TechnicalMessage error) {
        return Mono.defer(() -> {
            ErrorDTO errorResponse = ErrorDTO.builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .build();
            return ServerResponse.status(httpStatus)
                    .bodyValue(errorResponse);
        });
    }
}
