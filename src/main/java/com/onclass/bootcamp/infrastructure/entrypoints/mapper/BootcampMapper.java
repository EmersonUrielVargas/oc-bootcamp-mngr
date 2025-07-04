package com.onclass.bootcamp.infrastructure.entrypoints.mapper;

import com.onclass.bootcamp.domain.model.Bootcamp;
import com.onclass.bootcamp.infrastructure.entrypoints.dto.request.CreateBootcampDTO;
import com.onclass.bootcamp.infrastructure.entrypoints.dto.response.BasicBootcampDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BootcampMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "duration", target = "duration")
    BasicBootcampDTO toBasicBootcampDTO(Bootcamp bootcamp);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "capabilities", target = "capabilities")
    Bootcamp toBootcamp(CreateBootcampDTO createBootcampDTO);
}
