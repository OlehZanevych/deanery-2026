package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.faculty.BaseFacultyDto;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyDto;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyEntity toEntity(BaseFacultyDto facultyDto);
    FacultyDto toDto(FacultyEntity facultyEntity);
    List<FacultyDto> toDtoList(List<FacultyEntity> facultyEntities);
}
