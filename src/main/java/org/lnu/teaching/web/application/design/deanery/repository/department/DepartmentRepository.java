package org.lnu.teaching.web.application.design.deanery.repository.department;

import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentPatch;
import org.lnu.teaching.web.application.design.deanery.dto.department.query.params.DepartmentFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;

import java.util.List;

public interface DepartmentRepository {
    DepartmentEntity create(DepartmentEntity departmentEntity);
    List<DepartmentEntity> findAll(DepartmentFilterOptions filterOptions, Integer limit, Integer offset);
    int count(DepartmentFilterOptions filterOptions);
    DepartmentEntity find(Long id);
    void update(DepartmentEntity departmentEntity);
    void patch(Long id, DepartmentPatch departmentPatch);
    void delete(Long id);
}
