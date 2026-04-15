package org.lnu.teaching.web.application.design.deanery.repository.lecturer;

import org.lnu.teaching.web.application.design.deanery.entity.lecturer.LecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<LecturerEntity, Long> {
    Optional<LecturerEntity> findByEmail(String email);

    List<LecturerEntity> findByDepartmentIdOrderByLastNameAsc(Long departmentId);

    List<LecturerEntity> findByTitle(String title);

    boolean existsByEmail(String email);

    long countByDepartmentId(Long departmentId);

    @Query("""
            SELECT l FROM LecturerEntity l
            WHERE LOWER(CONCAT(l.firstName, ' ', l.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<LecturerEntity> searchByFullName(String name);

    @Query("SELECT l FROM LecturerEntity l WHERE l.department.faculty.id = :facultyId")
    List<LecturerEntity> findByFacultyId(Long facultyId);

    @Query("SELECT l FROM LecturerEntity l JOIN FETCH l.department WHERE l.department.id = :departmentId")
    List<LecturerEntity> findByDepartmentIdWithDepartment(Long departmentId);

    @Modifying
    @Query("DELETE FROM LecturerEntity l WHERE l.id = :id")
    int removeById(Long id);

    @Modifying
    @Query("DELETE FROM LecturerEntity l WHERE l.department.id = :departmentId")
    int removeByDepartmentId(Long departmentId);
}
