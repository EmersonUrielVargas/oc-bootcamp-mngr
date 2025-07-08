package com.onclass.bootcamp.domain.usecase;

import com.onclass.bootcamp.domain.api.BootcampServicePort;
import com.onclass.bootcamp.domain.enums.ItemSortList;
import com.onclass.bootcamp.domain.enums.OrderList;
import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import com.onclass.bootcamp.domain.exceptions.BusinessException;
import com.onclass.bootcamp.domain.exceptions.EntityAlreadyExistException;
import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.domain.model.spi.BootcampCapabilities;
import com.onclass.bootcamp.domain.model.spi.BootcampList;
import com.onclass.bootcamp.domain.model.spi.CapacityItem;
import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.domain.spi.CapabilitiesGateway;
import com.onclass.bootcamp.domain.utilities.CustomPage;
import com.onclass.bootcamp.domain.validators.Validator;
import lombok.AllArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.*;

@AllArgsConstructor
public class BootcampUseCase implements BootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilitiesGateway capabilitiesGateway;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Bootcamp> registerBootcamp(Bootcamp bootcamp) {
        return transactionalOperator.transactional(
            Validator.validateBootcamp(bootcamp)
            .then( bootcampPersistencePort.findByName(bootcamp.name()))
            .flatMap(bootcampFound -> Mono.error(new EntityAlreadyExistException(TechnicalMessage.BOOTCAMP_ALREADY_EXISTS)))
            .switchIfEmpty(
                Mono.defer(()->
                    bootcampPersistencePort.upsert(bootcamp)
                    .flatMap(bootcampSaved ->
                        capabilitiesGateway.assignCapabilitiesToBootcamp(bootcampSaved.id(), bootcamp.capabilities())
                        .thenReturn(bootcampSaved)
                    )
                )
            ).cast(Bootcamp.class)
        );
    }

    @Override
    public Mono<CustomPage<BootcampList>> listBootcamps(OrderList order, ItemSortList item, Integer page, Integer size) {
        return switch (item){
            case CAPABILITIES -> listBootcampsSortByCapabilities(order, page, size);
            case NAME -> listBootcampsSortByName(order, page, size);
        };
    }

    @Override
    public Mono<Bootcamp> deleteBootcamp(Long bootcampId) {
        return transactionalOperator.transactional(
            Validator.validationCondition(bootcampId != null, TechnicalMessage.INVALID_PARAMETERS)
            .then(bootcampPersistencePort.findById(bootcampId)
                .flatMap( bootcampFound ->
                    bootcampPersistencePort.deleteBootcamp(bootcampFound.id())
                    .then(
                        capabilitiesGateway.deleteCapabilitiesByBootcampId(bootcampFound.id())
                        .thenReturn(bootcampFound)
                    )
                ).switchIfEmpty(
                    Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_NOT_FOUND))
                )
            )
        );
    }

    private Mono<CustomPage<BootcampList>> listBootcampsSortByCapabilities(OrderList order, Integer page, Integer size) {
        return capabilitiesGateway.getSortCapabilitiesByBootcamps(order.getMessage(), size, page)
            .flatMap(pageBootcamps -> {
                List<Long> listIds = pageBootcamps.getData().stream().map(BootcampCapabilities::id).toList();
                return bootcampPersistencePort.findAllByIds(listIds)
                    .map( bootcamp -> enrichBootcampInfo(bootcamp,pageBootcamps.getData()))
                    .collectSortedList(
                        verifyOrder(order, Comparator.comparing(item -> item.capabilities().size()))
                    )
                    .map( listBootcampsComplete ->
                        new CustomPage<BootcampList>(listBootcampsComplete, pageBootcamps)
                    );
            });
    }

    private Mono<CustomPage<BootcampList>> listBootcampsSortByName(OrderList order, Integer page, Integer size) {
        return bootcampPersistencePort.findPaginatedAndSortByName(order.getMessage(), size, page)
            .collectList()
            .flatMap( listBootcamps -> {
                if (listBootcamps.isEmpty()) {
                    return Mono.just(Collections.checkedList(List.of(),BootcampList.class));
                }
                List<Long> listIds = listBootcamps.stream().map(Bootcamp::id).toList();
                return capabilitiesGateway.getCapabilitiesByBootcampsIds(listIds)
                    .map( bootcampsCapabilities ->
                        listBootcamps.stream()
                        .map( bootcamp -> enrichBootcampInfo(bootcamp, bootcampsCapabilities))
                        .toList()
                    );
                }
            ).zipWith(bootcampPersistencePort.countBootcamps())
            .map(tuple ->
                CustomPage.buildCustomPage(tuple.getT1(), page, size, tuple.getT2())
            );
    }

    private BootcampList enrichBootcampInfo(Bootcamp bootcampBasic, List<BootcampCapabilities> bootcampCapabilities){
        List< CapacityItem > capabilitiesBootcamp =  bootcampCapabilities.stream()
                .filter(item -> Objects.equals(item.id(), bootcampBasic.id()))
                .findFirst()
                .map(BootcampCapabilities::capabilities)
                .orElse(Collections.checkedList(List.of(),CapacityItem.class));
        return new BootcampList(
                bootcampBasic.id(),
                bootcampBasic.name(),
                bootcampBasic.description(),
                bootcampBasic.startDate(),
                bootcampBasic.duration(),
                capabilitiesBootcamp);
    }

    private <T> Comparator<T> verifyOrder(OrderList order, Comparator<T> baseComparator){
        return order.equals(OrderList.ASCENDANT)? baseComparator: baseComparator.reversed();
    }
}
