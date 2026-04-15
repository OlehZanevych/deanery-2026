package org.lnu.teaching.web.application.design.deanery.service.lecturer;

import org.lnu.teaching.web.application.design.deanery.dto.lecturer.BaseLecturerDto;
import org.lnu.teaching.web.application.design.deanery.dto.lecturer.LecturerDto;

import java.util.List;

public interface LecturerService {
    LecturerDto create(BaseLecturerDto dto);
    List<LecturerDto> findAll();
    List<LecturerDto> findByDepartmentId(Long departmentId);
    List<LecturerDto> findByFacultyId(Long facultyId);
    List<LecturerDto> search(String name);
    LecturerDto find(Long id);
    LecturerDto update(Long id, BaseLecturerDto dto);
    void delete(Long id);
}
