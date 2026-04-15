package org.lnu.teaching.web.application.design.deanery.service.course;

import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.BaseCourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CoursePatch;
import org.lnu.teaching.web.application.design.deanery.entity.projection.cource.CourseSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    CourseDto create(BaseCourseDto dto);
    List<CourseDto> findAll();
    Page<CourseDto> findByCurriculumId(Long curriculumId, Pageable pageable);
    List<CourseDto> findByCurriculumIdWithLecturer(Long curriculumId);
    List<CourseSummaryProjection> findSummaryByCurriculumId(Long curriculumId);
    List<CourseDto> findByLecturerId(Long lecturerId);
    List<CourseDto> findBySemester(Integer semester);
    List<CourseDto> findByFacultyId(Long facultyId);
    List<CourseDto> search(String name);
    List<CourseDto> findByMinCredits(Integer minCredits);
    ValueDto<Long> countByCurriculumId(Long curriculumId);
    CourseDto find(Long id);
    CourseDto update(Long id, BaseCourseDto dto);
    CourseDto patch(Long id, CoursePatch patch);
    void delete(Long id);
    int deleteByCurriculumId(Long curriculumId);
    int deleteByLecturerId(Long lecturerId);
    int reassignLecturer(Long fromLecturerId, Long toLecturerId);
}
