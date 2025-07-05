package com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.entity.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface BootcampRepository extends ReactiveCrudRepository<BootcampEntity, Long> {
    Mono<BootcampEntity> findByName(String name);

    @Query("SELECT * FROM bootcamps ORDER BY name ASC LIMIT :size OFFSET :page")
    Flux<BootcampEntity> findAndSortByName(int page, int size);
}
