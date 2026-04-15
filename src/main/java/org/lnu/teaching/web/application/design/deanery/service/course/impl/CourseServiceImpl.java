package org.lnu.teaching.web.application.design.deanery.service.course.impl;

import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.BaseCourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CoursePatch;
import org.lnu.teaching.web.application.design.deanery.entity.course.CourseEntity;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.lnu.teaching.web.application.design.deanery.entity.lecturer.LecturerEntity;
import org.lnu.teaching.web.application.design.deanery.entity.projection.cource.CourseSummaryProjection;
import org.lnu.teaching.web.application.design.deanery.exception.BadRequestException;
import org.lnu.teaching.web.application.design.deanery.exception.NotFoundException;
import org.lnu.teaching.web.application.design.deanery.mapper.CourseMapper;
import org.lnu.teaching.web.application.design.deanery.repository.course.CourseRepository;
import org.lnu.teaching.web.application.design.deanery.repository.curriculum.CurriculumRepository;
import org.lnu.teaching.web.application.design.deanery.repository.lecturer.LecturerRepository;
import org.lnu.teaching.web.application.design.deanery.service.course.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Full-featured service for course management.
 *
 * <p>Showcases the complete Spring Data JPA feature set:
 * <ul>
 *   <li>Standard CRUD via inherited {@code JpaRepository} methods</li>
 *   <li>Derived queries: {@code findByCurriculumId}, {@code findByLecturerId}, etc.</li>
 *   <li>Paginated retrieval returning a {@link Page}</li>
 *   <li>HQL projection query returning {@link CourseSummaryProjection} proxies</li>
 *   <li>HQL aggregate query for credit sums per semester</li>
 *   <li>JOIN FETCH query to avoid N+1 when lecturers are needed</li>
 *   <li>{@code @Modifying} HQL delete by id / by curriculum / by lecturer</li>
 *   <li>{@code @Modifying} HQL bulk UPDATE to reassign a lecturer</li>
 *   <li>PATCH flow using MapStruct {@code @MappingTarget} in-place update</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CurriculumRepository curriculumRepository;
    private final LecturerRepository lecturerRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseDto create(BaseCourseDto dto) {
        CurriculumEntity curriculum = resolveCurriculum(dto.getCurriculumId());
        LecturerEntity  lecturer   = dto.getLecturerId() != null
                ? resolveLecturer(dto.getLecturerId()) : null;

        CourseEntity entity = courseMapper.toEntity(dto);
        entity.setCurriculum(curriculum);
        entity.setLecturer(lecturer);

        return courseMapper.toDto(courseRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        return courseMapper.toDtoList(courseRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDto> findByCurriculumId(Long curriculumId, Pageable pageable) {
        return courseRepository.findByCurriculumId(curriculumId, pageable)
                .map(courseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findByCurriculumIdWithLecturer(Long curriculumId) {
        // Uses the JOIN FETCH HQL query — no N+1 when accessing lecturer fields
        return courseMapper.toDtoList(
                courseRepository.findByCurriculumIdWithLecturer(curriculumId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSummaryProjection> findSummaryByCurriculumId(Long curriculumId) {
        // Returns interface-based projections — Spring Data creates runtime proxies
        return courseRepository.findSummaryByCurriculumId(curriculumId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findByLecturerId(Long lecturerId) {
        return courseMapper.toDtoList(courseRepository.findByLecturerId(lecturerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findBySemester(Integer semester) {
        return courseMapper.toDtoList(courseRepository.findBySemester(semester));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findByFacultyId(Long facultyId) {
        return courseMapper.toDtoList(courseRepository.findByFacultyId(facultyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> search(String name) {
        return courseMapper.toDtoList(courseRepository.searchByName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findByMinCredits(Integer minCredits) {
        return courseMapper.toDtoList(courseRepository.findByMinCredits(minCredits));
    }

    @Override
    @Transactional(readOnly = true)
    public ValueDto<Long> countByCurriculumId(Long curriculumId) {
        return new ValueDto<>(courseRepository.countByCurriculumId(curriculumId));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto find(Long id) {
        return courseMapper.toDto(loadCourse(id));
    }

    @Override
    @Transactional
    public CourseDto update(Long id, BaseCourseDto dto) {
        CourseEntity entity = loadCourse(id);

        entity.setName(dto.getName());
        entity.setCredits(dto.getCredits());
        entity.setHours(dto.getHours());
        entity.setSemester(dto.getSemester());
        entity.setCurriculum(resolveCurriculum(dto.getCurriculumId()));
        entity.setLecturer(dto.getLecturerId() != null ? resolveLecturer(dto.getLecturerId()) : null);
        entity.setInfo(dto.getInfo());

        return courseMapper.toDto(courseRepository.save(entity));
    }

    @Override
    @Transactional
    public CourseDto patch(Long id, CoursePatch patch) {
        if (patch.isEmpty()) {
            throw new BadRequestException("Course patch body is empty!");
        }

        CourseEntity entity = loadCourse(id);

        if (patch.isNameUpdated())       entity.setName(patch.getName());
        if (patch.isCreditsUpdated())    entity.setCredits(patch.getCredits());
        if (patch.isHoursUpdated())      entity.setHours(patch.getHours());
        if (patch.isSemesterUpdated())   entity.setSemester(patch.getSemester());
        if (patch.isInfoUpdated())       entity.setInfo(patch.getInfo());

        if (patch.isCurriculumIdUpdated()) {
            entity.setCurriculum(resolveCurriculum(patch.getCurriculumId()));
        }
        if (patch.isLecturerIdUpdated()) {
            entity.setLecturer(patch.getLecturerId() != null
                    ? resolveLecturer(patch.getLecturerId()) : null);
        }

        // Hibernate's @DynamicUpdate generates an UPDATE with only the changed columns
        return courseMapper.toDto(courseRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        int affected = courseRepository.removeById(id);
        if (affected == 0) {
            throw new NotFoundException("Course with id " + id + " not found!");
        }
    }

    @Override
    @Transactional
    public int deleteByCurriculumId(Long curriculumId) {
        return courseRepository.removeByCurriculumId(curriculumId);
    }

    @Override
    @Transactional
    public int deleteByLecturerId(Long lecturerId) {
        return courseRepository.removeByLecturerId(lecturerId);
    }

    @Override
    @Transactional
    public int reassignLecturer(Long fromLecturerId, Long toLecturerId) {
        // Validate the target lecturer exists before bulk-updating
        if (!lecturerRepository.existsById(toLecturerId)) {
            throw new NotFoundException("Lecturer with id " + toLecturerId + " not found!");
        }
        return courseRepository.reassignLecturer(fromLecturerId, toLecturerId);
    }

    private CourseEntity loadCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course with id " + id + " not found!"));
    }

    private CurriculumEntity resolveCurriculum(Long curriculumId) {
        if (!curriculumRepository.existsById(curriculumId)) {
            throw new NotFoundException("Curriculum with id " + curriculumId + " not found!");
        }
        return curriculumRepository.getReferenceById(curriculumId);
    }

    private LecturerEntity resolveLecturer(Long lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) {
            throw new NotFoundException("Lecturer with id " + lecturerId + " not found!");
        }
        return lecturerRepository.getReferenceById(lecturerId);
    }
}
