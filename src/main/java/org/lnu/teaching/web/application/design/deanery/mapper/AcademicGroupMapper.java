package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.AcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.BaseAcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.entity.academicgroup.AcademicGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AcademicGroupMapper {

    @Mapping(source = "curriculum.id", target = "curriculumId")
    AcademicGroupDto toDto(AcademicGroupEntity entity);

    @Mapping(target = "curriculum", ignore = true)
    AcademicGroupEntity toEntity(BaseAcademicGroupDto dto);

    List<AcademicGroupDto> toDtoList(List<AcademicGroupEntity> entities);
}
