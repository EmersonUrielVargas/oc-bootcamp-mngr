package com.onclass.bootcamp.domain.spi;

import com.onclass.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampPersistencePort {
    Mono<Bootcamp> upsert(Bootcamp bootcamp);
    Mono<Bootcamp> findByName(String nameBootcamp);
    Flux<Bootcamp> findPaginatedAndSortByName(String order, Integer size, Integer page);
    Flux<Bootcamp> findAllByIds(List<Long> bootcampsIds);
    Mono<Long> countBootcamps();
    Mono<Bootcamp> findById(Long bootcampId);
    Mono<Void> deleteBootcamp(Long bootcampId);
}
