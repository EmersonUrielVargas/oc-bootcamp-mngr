package com.onclass.bootcamp.domain.model;

import java.time.LocalDate;
import java.util.List;

public record Bootcamp(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        Integer duration,
        List<Long> capabilities
    ) {
}
