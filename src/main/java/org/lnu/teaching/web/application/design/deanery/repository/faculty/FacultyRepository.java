package org.lnu.teaching.web.application.design.deanery.repository.faculty;

import org.lnu.teaching.web.application.design.deanery.dto.faculty.FacultyPatch;
import org.lnu.teaching.web.application.design.deanery.dto.faculty.query.params.FacultyFilterOptions;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;

import java.util.List;

public interface FacultyRepository {
    FacultyEntity create(FacultyEntity facultyEntity);
    List<FacultyEntity> findAll(FacultyFilterOptions filterOptions, Integer limit, Integer offset);
    int count(FacultyFilterOptions filterOptions);
    FacultyEntity find(Long id);
    void update(FacultyEntity facultyEntity);
    void patch(Long id, FacultyPatch facultyPatch);
    void delete(Long id);
}