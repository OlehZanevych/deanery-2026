package org.lnu.teaching.web.application.design.deanery.repository.course;

import org.lnu.teaching.web.application.design.deanery.entity.course.CourseEntity;
import org.lnu.teaching.web.application.design.deanery.entity.projection.cource.CourseSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByName(String name);

    List<CourseEntity> findByCurriculumId(Long curriculumId);

    List<CourseEntity> findByLecturerId(Long lecturerId);

    List<CourseEntity> findBySemester(Integer semester);

    List<CourseEntity> findByCurriculumIdAndSemester(Long curriculumId, Integer semester);

    Page<CourseEntity> findByCurriculumId(Long curriculumId, Pageable pageable);

    boolean existsByName(String name);

    long countByCurriculumId(Long curriculumId);

    long countByLecturerId(Long lecturerId);

    @Query("SELECT c FROM CourseEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CourseEntity> searchByName(String name);

    @Query("SELECT c FROM CourseEntity c WHERE c.credits >= :minCredits ORDER BY c.credits DESC")
    List<CourseEntity> findByMinCredits(Integer minCredits);

    @Query("""
            SELECT c FROM CourseEntity c
            WHERE c.curriculum.speciality.department.faculty.id = :facultyId
            """)
    List<CourseEntity> findByFacultyId(Long facultyId);

    @Query("SELECT c FROM CourseEntity c WHERE c.curriculum.id = :curriculumId")
    List<CourseSummaryProjection> findSummaryByCurriculumId(Long curriculumId);

    @Query("""
            SELECT c.semester, SUM(c.credits)
            FROM CourseEntity c
            WHERE c.curriculum.id = :curriculumId
            GROUP BY c.semester
            ORDER BY c.semester ASC
            """)
    List<Object[]> sumCreditsBySemester(Long curriculumId);

    @Query("""
            SELECT c FROM CourseEntity c
            LEFT JOIN FETCH c.lecturer
            WHERE c.curriculum.id = :curriculumId
            """)
    List<CourseEntity> findByCurriculumIdWithLecturer(Long curriculumId);

    @Modifying
    @Query("DELETE FROM CourseEntity c WHERE c.id = :id")
    int removeById(Long id);

    @Modifying
    @Query("DELETE FROM CourseEntity c WHERE c.curriculum.id = :curriculumId")
    int removeByCurriculumId(Long curriculumId);

    @Modifying
    @Query("DELETE FROM CourseEntity c WHERE c.lecturer.id = :lecturerId")
    int removeByLecturerId(Long lecturerId);

    @Modifying
    @Query("UPDATE CourseEntity c SET c.lecturer.id = :newLecturerId WHERE c.lecturer.id = :oldLecturerId")
    int reassignLecturer(Long oldLecturerId, Long newLecturerId);
}
