package com.onclass.bootcamp.domain.spi;

import com.onclass.bootcamp.domain.model.BootcampCapabilities;
import com.onclass.bootcamp.domain.model.CapacityItem;
import com.onclass.bootcamp.domain.utilities.CustomPage;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilitiesGateway {
    Mono<Void> assignCapabilitiesToBootcamp(Long bootcampId, List<Long> capabilitiesIds);
    Mono<List<BootcampCapabilities>> getCapabilitiesByBootcampsIds(List<Long> bootcampIds);
    Mono<CustomPage<BootcampCapabilities>> getSortCapabilitiesByBootcamps(String order, Integer size, Integer page);
}
