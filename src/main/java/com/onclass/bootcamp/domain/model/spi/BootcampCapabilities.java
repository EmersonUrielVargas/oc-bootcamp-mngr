package com.onclass.bootcamp.domain.model.spi;

import java.util.List;

public record BootcampCapabilities(
        Long id,
        List<CapacityItem> capabilities
    ) {
}
