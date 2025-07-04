package com.onclass.bootcamp.infrastructure.entrypoints.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class CreateBootcampDTO {
    private String name;
    private String description;
    private LocalDate startDate;
    private Integer duration;
    private List<Long> capabilities;
}
