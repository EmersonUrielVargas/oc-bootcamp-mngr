package com.onclass.bootcamp.domain.model.spi;

import java.util.List;

public record BootcampCapabilitiesComplete(
        Long id,
        List<Capacity> capabilities
    ) {
}
