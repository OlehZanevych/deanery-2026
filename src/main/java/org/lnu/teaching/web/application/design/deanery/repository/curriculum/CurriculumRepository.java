package org.lnu.teaching.web.application.design.deanery.repository.curriculum;

import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<CurriculumEntity, Long> {
    List<CurriculumEntity> findBySpecialityId(Long specialityId);

    Page<CurriculumEntity> findBySpecialityId(Long specialityId, Pageable pageable);

    List<CurriculumEntity> findByDegree(String degree);

    List<CurriculumEntity> findByYear(Integer year);

    boolean existsBySpecialityIdAndDegreeAndYear(Long specialityId, String degree, Integer year);

    @Query("SELECT c FROM CurriculumEntity c WHERE c.speciality.department.id = :departmentId")
    List<CurriculumEntity> findByDepartmentId(Long departmentId);

    @Query("SELECT c FROM CurriculumEntity c WHERE c.speciality.department.faculty.id = :facultyId")
    List<CurriculumEntity> findByFacultyId(Long facultyId);

    @Query("SELECT c FROM CurriculumEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CurriculumEntity> searchByName(String name);

    @Query("SELECT c.degree, COUNT(c) FROM CurriculumEntity c GROUP BY c.degree")
    List<Object[]> countByDegree();

    @Modifying
    @Query("DELETE FROM CurriculumEntity c WHERE c.id = :id")
    int removeById(Long id);

    @Modifying
    @Query("DELETE FROM CurriculumEntity c WHERE c.speciality.id = :specialityId")
    int removeBySpecialityId(Long specialityId);
}
