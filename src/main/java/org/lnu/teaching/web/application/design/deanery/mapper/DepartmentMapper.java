package org.lnu.teaching.web.application.design.deanery.mapper;

import org.lnu.teaching.web.application.design.deanery.dto.department.BaseDepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentDto;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for {@link DepartmentEntity}.
 *
 * <h3>Why unmappedSourcePolicy = IGNORE?</h3>
 * After adding JPA support to {@code DepartmentEntity}, the plain {@code Long facultyId}
 * field was replaced by a {@code @ManyToOne FacultyEntity faculty} relationship plus the
 * compatibility helpers {@code getFacultyId()} / {@code setFacultyId(Long)}.
 *
 * <p>MapStruct now sees <em>two</em> source-side "properties" derived from the entity:
 * <ul>
 *   <li>{@code faculty}   — from {@code getFaculty()} returning a {@code FacultyEntity}</li>
 *   <li>{@code facultyId} — from the custom {@code getFacultyId()} returning a {@code Long}</li>
 * </ul>
 * {@code DepartmentDto} only has {@code facultyId} (a {@code Long}), so {@code faculty}
 * is intentionally unmapped.  Setting {@code unmappedSourcePolicy = IGNORE} suppresses the
 * MapStruct compile warning for that property and avoids the need for a spurious
 * {@code @Mapping(target = "faculty", ignore = true)} that would incorrectly reference a
 * non-existent target field on the DTO.
 *
 * <h3>Mapping logic</h3>
 * <ul>
 *   <li>{@code toDto} — {@code getFacultyId()} is auto-matched by name to {@code setFacultyId()}
 *       on the DTO; no explicit {@code @Mapping} needed.</li>
 *   <li>{@code toEntity} — {@code @Mapping(target = "faculty", ignore = true)} prevents
 *       MapStruct from trying to populate the JPA relationship from the DTO;
 *       {@code setFacultyId(Long)} is called instead via the matching source property.</li>
 * </ul>
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    /**
     * Maps DTO → entity.
     * <p>{@code setFacultyId(Long)} on the entity wraps the Long in a minimal
     * {@code FacultyEntity} stub — sufficient for the JDBC INSERT/UPDATE layer.
     */
    @Mapping(target = "faculty", ignore = true)
    DepartmentEntity toEntity(BaseDepartmentDto departmentDto);

    /**
     * Maps entity → DTO.
     * <p>{@code getFacultyId()} returns {@code faculty.getId()} safely without
     * triggering a Hibernate lazy-load; it is auto-matched to {@code setFacultyId()}
     * on the DTO by name.  The {@code getFaculty()} source property is silently
     * ignored (unmappedSourcePolicy = IGNORE).
     */
    DepartmentDto toDto(DepartmentEntity departmentEntity);

    List<DepartmentDto> toDtoList(List<DepartmentEntity> departmentEntities);
}
