package com.onclass.bootcamp.infrastructure.adapters.persistenceadapter;

import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.domain.spi.BootcampPersistencePort;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.mapper.BootcampEntityMapper;
import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.repository.BootcampRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Flux<Bootcamp> findPaginatedAndSortByName(String order, Integer size, Integer page) {
        System.out.println("lega al repository "+ order + " size: "+size+" page: "+page);
        return bootcampRepository.findAndSortByName( page, size).map(bootcampEntityMapper::toModel);
    }

    @Override
    public Flux<Bootcamp> findAllByIds(List<Long> bootcampsIds) {
        return bootcampRepository.findAllById(bootcampsIds).map(bootcampEntityMapper::toModel);
    }

    @Override
    public Mono<Long> countBootcamps() {
        return bootcampRepository.count();
    }
}
