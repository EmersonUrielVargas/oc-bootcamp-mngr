package com.onclass.bootcamp.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilitiesGateway {
    Mono<Void> assignCapabilitiesToBootcamp(Long bootcampId, List<Long> capabilitiesIds);
}
