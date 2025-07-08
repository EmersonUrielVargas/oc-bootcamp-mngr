package com.onclass.bootcamp.domain.model.spi;

import java.util.List;

public record CapacityItem(
        Long id,
        String name,
        List<TechnologyItem> technologies
    ) {
}
