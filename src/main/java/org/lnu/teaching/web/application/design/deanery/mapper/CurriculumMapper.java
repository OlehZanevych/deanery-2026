package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.curriculum.BaseCurriculumDto;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.CurriculumDto;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurriculumMapper {

    @Mapping(source = "speciality.id", target = "specialityId")
    CurriculumDto toDto(CurriculumEntity entity);

    @Mapping(target = "speciality", ignore = true)
    CurriculumEntity toEntity(BaseCurriculumDto dto);

    List<CurriculumDto> toDtoList(List<CurriculumEntity> entities);
}
