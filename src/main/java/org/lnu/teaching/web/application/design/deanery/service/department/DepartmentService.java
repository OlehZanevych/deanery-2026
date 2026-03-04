package org.lnu.teaching.web.application.design.deanery.service.department;

import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.BaseDepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentDto;
import org.lnu.teaching.web.application.design.deanery.dto.department.DepartmentPatch;
import org.lnu.teaching.web.application.design.deanery.dto.department.query.params.DepartmentFilterOptions;

import java.util.List;

public interface DepartmentService {
    DepartmentDto create(BaseDepartmentDto departmentDto);
    List<DepartmentDto> findAll(DepartmentFilterOptions filterOptions, Integer limit, Integer offset);
    ValueDto<Integer> count(DepartmentFilterOptions filterOptions);
    DepartmentDto find(Long id);
    void update(Long id, BaseDepartmentDto departmentDto);
    void patch(Long id, DepartmentPatch departmentPatch);
    void delete(Long id);
}
