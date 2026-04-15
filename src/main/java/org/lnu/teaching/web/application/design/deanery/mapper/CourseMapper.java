package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.course.BaseCourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CourseDto;
import org.lnu.teaching.web.application.design.deanery.entity.course.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "curriculum.id", target = "curriculumId")
    @Mapping(source = "lecturer.id",   target = "lecturerId")
    CourseDto toDto(CourseEntity entity);

    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "lecturer",   ignore = true)
    CourseEntity toEntity(BaseCourseDto dto);

    @Mapping(target = "id",         ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "lecturer",   ignore = true)
    void updateEntity(BaseCourseDto dto, @MappingTarget CourseEntity entity);

    List<CourseDto> toDtoList(List<CourseEntity> entities);
}
