package com.onclass.bootcamp.infrastructure.adapters.persistenceadapter;

import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.mapper.BootcampEntityMapper;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.repository.BootcampRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {
    private final BootcampRepository bootcampRepository;
    private final BootcampEntityMapper bootcampEntityMapper;

    @Override
    public Mono<Bootcamp> upsert(Bootcamp bootcamp) {
        return bootcampRepository.save(bootcampEntityMapper.toEntity(bootcamp))
                .map(bootcampEntityMapper::toModel);
    }

    @Override
    public Mono<Bootcamp> findByName(String nameBootcamp) {
        return bootcampRepository.findByName(nameBootcamp)
                .map(bootcampEntityMapper::toModel);
    }
}
