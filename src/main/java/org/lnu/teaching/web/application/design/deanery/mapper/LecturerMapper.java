package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.lecturer.BaseLecturerDto;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.LecturerDto;
import org.lnu.teaching.web.application.design.deanery.entity.lecturer.LecturerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper {

    @Mapping(source = "department.id", target = "departmentId")
    LecturerDto toDto(LecturerEntity entity);

    @Mapping(target = "department", ignore = true)
    LecturerEntity toEntity(BaseLecturerDto dto);

    List<LecturerDto> toDtoList(List<LecturerEntity> entities);
}
