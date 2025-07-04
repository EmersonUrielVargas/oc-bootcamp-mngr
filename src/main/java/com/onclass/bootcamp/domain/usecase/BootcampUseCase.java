package com.onclass.bootcamp.domain.usecase;

import com.onclass.bootcamp.domain.api.BootcampServicePort;
import com.onclass.bootcamp.domain.enums.TechnicalMessage;
import com.onclass.bootcamp.domain.exceptions.EntityAlreadyExistException;
import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.domain.spi.CapabilitiesGateway;
import com.onclass.bootcamp.domain.validators.Validator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class BootcampUseCase implements BootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilitiesGateway capabilitiesGateway;

    @Override
    public Mono<Bootcamp> registerBootcamp(Bootcamp bootcamp) {
        return Validator.validateBootcamp(bootcamp)
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
            ).cast(Bootcamp.class);
    }
}
