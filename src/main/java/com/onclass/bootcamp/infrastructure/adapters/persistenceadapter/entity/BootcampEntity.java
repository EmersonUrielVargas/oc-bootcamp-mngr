package com.onclass.bootcamp.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "bootcamps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BootcampEntity {
    @Id
    private Long id;
    private String name;
    private String description;

    @Column("start_date")
    private LocalDate startDate;

    private Integer duration;
}
