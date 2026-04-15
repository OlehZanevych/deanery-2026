package org.lnu.teaching.web.application.design.deanery.service.curriculum;

import org.lnu.teaching.web.application.design.deanery.dto.curriculum.BaseCurriculumDto;
import org.lnu.teaching.web.application.design.deanery.dto.curriculum.CurriculumDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurriculumService {
    CurriculumDto create(BaseCurriculumDto dto);
    List<CurriculumDto> findAll();
    Page<CurriculumDto> findBySpecialityId(Long specialityId, Pageable pageable);
    List<CurriculumDto> findByDegree(String degree);
    List<CurriculumDto> findByFacultyId(Long facultyId);
    List<CurriculumDto> search(String name);
    CurriculumDto find(Long id);
    CurriculumDto update(Long id, BaseCurriculumDto dto);
    void delete(Long id);
}
