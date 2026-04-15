package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.speciality.BaseSpecialityDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.SpecialityDto;
import org.lnu.teaching.web.application.design.deanery.entity.speciality.SpecialityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for {@link SpecialityEntity}.
 *
 * <p>The {@code @Mapping} annotations handle the impedance mismatch between the
 * flat DTO (which carries {@code departmentId}) and the entity (which carries a
 * full {@code DepartmentEntity} reference):
 * <ul>
 *   <li>{@code toDto} — reads {@code entity.department.id} and writes it to {@code dto.departmentId}</li>
 *   <li>{@code toEntity} — ignores the {@code department} field; the service layer
 *       loads and sets it via the repository</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    /** Maps entity → DTO, flattening the {@code department} association to its PK. */
    @Mapping(source = "department.id", target = "departmentId")
    SpecialityDto toDto(SpecialityEntity entity);

    /** Maps DTO → entity, leaving the {@code department} field to be set by the service. */
    @Mapping(target = "department", ignore = true)
    SpecialityEntity toEntity(BaseSpecialityDto dto);

    List<SpecialityDto> toDtoList(List<SpecialityEntity> entities);
}
