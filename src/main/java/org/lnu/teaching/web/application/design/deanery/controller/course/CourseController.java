package org.lnu.teaching.web.application.design.deanery.controller.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.common.ValueDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.BaseCourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CourseDto;
import org.lnu.teaching.web.application.design.deanery.dto.course.CoursePatch;
import org.lnu.teaching.web.application.design.deanery.entity.projection.cource.CourseSummaryProjection;
import org.lnu.teaching.web.application.design.deanery.service.course.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Auth
@RestController
@RequiredArgsConstructor
@RequestMapping("courses")
@Tag(name = "Courses", description = "Course management — comprehensive Spring Data JPA + Spring REST demo")
public class CourseController {

    private final CourseService courseService;

    @Auth(isAdmin = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course",
               description = "Demonstrates JpaRepository.save() triggering an INSERT via Hibernate.")
    public CourseDto create(@RequestBody BaseCourseDto dto) {
        return courseService.create(dto);
    }

    @GetMapping
    @Operation(summary = "List all courses",
               description = "Uses inherited JpaRepository.findAll().")
    public List<CourseDto> findAll() {
        return courseService.findAll();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a course by ID",
               description = "Uses JpaRepository.findById() with NotFoundException if absent.")
    public CourseDto find(@PathVariable Long id) {
        return courseService.find(id);
    }

    @GetMapping("search")
    @Operation(summary = "Search courses by name (case-insensitive)",
               description = "Uses HQL: SELECT c FROM CourseEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    public List<CourseDto> search(@RequestParam String name) {
        return courseService.search(name);
    }

    @GetMapping("by-curriculum/{curriculumId}")
    @Operation(summary = "Paginated courses for a curriculum",
               description = "Demonstrates Spring Data JPA Pageable. "
                           + "Add ?page=0&size=5&sort=semester,asc to the URL.")
    public Page<CourseDto> findByCurriculum(
            @PathVariable Long curriculumId,
            @PageableDefault(size = 10, sort = "semester") Pageable pageable) {
        return courseService.findByCurriculumId(curriculumId, pageable);
    }

    @GetMapping("by-curriculum/{curriculumId}/with-lecturer")
    @Operation(summary = "Courses for a curriculum with lecturers pre-fetched",
               description = "Uses HQL JOIN FETCH to load courses and their lecturers in a "
                           + "single SQL query, avoiding the N+1 select problem.")
    public List<CourseDto> findByCurriculumWithLecturer(@PathVariable Long curriculumId) {
        return courseService.findByCurriculumIdWithLecturer(curriculumId);
    }

    @GetMapping("by-curriculum/{curriculumId}/summary")
    @Operation(summary = "Lightweight summary of courses (interface-based projection)",
               description = "Returns only id/name/credits/semester — Spring Data creates a "
                           + "proxy implementing CourseSummaryProjection without loading full entities.")
    public List<CourseSummaryProjection> findSummaryByCurriculum(@PathVariable Long curriculumId) {
        return courseService.findSummaryByCurriculumId(curriculumId);
    }

    @GetMapping("by-lecturer/{lecturerId}")
    @Operation(summary = "Courses taught by a lecturer (derived query method)")
    public List<CourseDto> findByLecturer(@PathVariable Long lecturerId) {
        return courseService.findByLecturerId(lecturerId);
    }

    @GetMapping("by-semester/{semester}")
    @Operation(summary = "Courses in a specific semester (derived query method)")
    public List<CourseDto> findBySemester(@PathVariable Integer semester) {
        return courseService.findBySemester(semester);
    }

    @GetMapping("by-faculty/{facultyId}")
    @Operation(summary = "Courses in a faculty (HQL multi-level path: course→curriculum→speciality→department→faculty)")
    public List<CourseDto> findByFaculty(@PathVariable Long facultyId) {
        return courseService.findByFacultyId(facultyId);
    }

    @GetMapping("min-credits")
    @Operation(summary = "Courses with at least N credits (HQL comparison)",
               description = "HQL: SELECT c FROM CourseEntity c WHERE c.credits >= :minCredits ORDER BY c.credits DESC")
    public List<CourseDto> findByMinCredits(
            @Parameter(description = "Minimum credit load") @RequestParam Integer value) {
        return courseService.findByMinCredits(value);
    }

    @GetMapping("count/by-curriculum/{curriculumId}")
    @Operation(summary = "Count courses in a curriculum (derived count method)")
    public ValueDto<Long> countByCurriculum(@PathVariable Long curriculumId) {
        return courseService.countByCurriculumId(curriculumId);
    }

    @Auth(isAdmin = true)
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Replace a course (full update)",
               description = "Loads the entity, overwrites all fields, then calls save(). "
                           + "Hibernate's @DynamicUpdate emits only the changed columns in the SQL UPDATE.")
    public void update(@PathVariable Long id, @RequestBody BaseCourseDto dto) {
        courseService.update(id, dto);
    }

    @Auth(isAdmin = true)
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Partially update a course (PATCH)",
               description = "Only the fields present in the request body are modified. "
                           + "Each setter in CoursePatch flips a flag; the service applies only "
                           + "flagged fields to the entity before calling save().")
    public void patch(@PathVariable Long id, @RequestBody CoursePatch patch) {
        courseService.patch(id, patch);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a course by ID",
               description = "HQL: DELETE FROM CourseEntity c WHERE c.id = :id  — "
                           + "a @Modifying bulk-DML statement, bypassing entity lifecycle callbacks.")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @Auth(isAdmin = true)
    @DeleteMapping("by-curriculum/{curriculumId}")
    @Operation(summary = "Delete all courses in a curriculum (HQL bulk DELETE)",
               description = "HQL: DELETE FROM CourseEntity c WHERE c.curriculum.id = :curriculumId")
    public ValueDto<Integer> deleteByCurriculum(@PathVariable Long curriculumId) {
        return new ValueDto<>(courseService.deleteByCurriculumId(curriculumId));
    }

    @Auth(isAdmin = true)
    @DeleteMapping("by-lecturer/{lecturerId}")
    @Operation(summary = "Delete all courses by a lecturer (HQL bulk DELETE via association path)")
    public ValueDto<Integer> deleteByLecturer(@PathVariable Long lecturerId) {
        return new ValueDto<>(courseService.deleteByLecturerId(lecturerId));
    }

    @Auth(isAdmin = true)
    @PutMapping("reassign-lecturer")
    @Operation(summary = "Reassign all courses from one lecturer to another (HQL bulk UPDATE)",
               description = "HQL: UPDATE CourseEntity c SET c.lecturer.id = :newId WHERE c.lecturer.id = :oldId")
    public ValueDto<Integer> reassignLecturer(
            @RequestParam Long fromLecturerId,
            @RequestParam Long toLecturerId) {
        return new ValueDto<>(courseService.reassignLecturer(fromLecturerId, toLecturerId));
    }
}
