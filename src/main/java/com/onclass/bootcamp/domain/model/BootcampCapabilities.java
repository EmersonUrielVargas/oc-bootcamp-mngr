package com.onclass.bootcamp.domain.model;

import java.util.List;

public record BootcampCapabilities(
        Long id,
        List<CapacityItem> capabilities
    ) {
}
