package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.department.BaseDepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentDto;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentEntity toEntity(BaseDepartmentDto departmentDto);
    DepartmentDto toDto(DepartmentEntity departmentEntity);
    List<DepartmentDto> toDtoList(List<DepartmentEntity> departmentEntities);
}
