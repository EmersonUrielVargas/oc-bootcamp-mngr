package com.onclass.bootcamp.domain.model.spi;

import java.time.LocalDate;
import java.util.List;

public record BootcampList(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        Integer duration,
        List<CapacityItem> capabilities
    ) {
}
