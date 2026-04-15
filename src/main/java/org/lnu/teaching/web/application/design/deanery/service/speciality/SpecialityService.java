package org.lnu.teaching.web.application.design.deanery.service.speciality;

import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.BaseSpecialityDto;
import org.lnu.teaching.web.application.design.deanery.dto.speciality.SpecialityDto;

import java.util.List;

public interface SpecialityService {
    SpecialityDto create(BaseSpecialityDto dto);
    List<SpecialityDto> findAll();
    List<SpecialityDto> findByDepartmentId(Long departmentId);
    List<SpecialityDto> findByFacultyId(Long facultyId);
    List<SpecialityDto> search(String name);
    ValueDto<Long> countByDepartmentId(Long departmentId);
    SpecialityDto find(Long id);
    SpecialityDto update(Long id, BaseSpecialityDto dto);
    void delete(Long id);
}
