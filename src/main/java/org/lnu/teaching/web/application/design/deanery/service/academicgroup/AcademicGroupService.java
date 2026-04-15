package org.lnu.teaching.web.application.design.deanery.service.academicgroup;

import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.AcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.academicgroup.BaseAcademicGroupDto;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;

import java.util.List;

public interface AcademicGroupService {
    AcademicGroupDto create(BaseAcademicGroupDto dto);
    List<AcademicGroupDto> findAll();
    List<AcademicGroupDto> findByCurriculumId(Long curriculumId);
    List<AcademicGroupDto> findByEnrollmentYear(Integer year);
    List<AcademicGroupDto> findEnrolledFromYear(Integer fromYear);
    ValueDto<Long> countByCurriculumId(Long curriculumId);
    AcademicGroupDto find(Long id);
    AcademicGroupDto update(Long id, BaseAcademicGroupDto dto);
    void delete(Long id);
}
