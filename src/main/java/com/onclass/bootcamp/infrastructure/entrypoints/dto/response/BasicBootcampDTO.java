package com.onclass.bootcamp.infrastructure.entrypoints.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class BasicBootcampDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private Integer duration;
}
