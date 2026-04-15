package org.lnu.teaching.web.application.design.deanery.repository.speciality;

import org.lnu.teaching.web.application.design.deanery.entity.speciality.SpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<SpecialityEntity, Long> {

    Optional<SpecialityEntity> findByCode(String code);

    List<SpecialityEntity> findByNameContaining(String name);

    List<SpecialityEntity> findByDepartmentId(Long departmentId);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    @Query("SELECT s FROM SpecialityEntity s WHERE s.department.faculty.id = :facultyId")
    List<SpecialityEntity> findByFacultyId(Long facultyId);

    @Query("SELECT s FROM SpecialityEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SpecialityEntity> searchByName(String name);

    @Query("SELECT COUNT(s) FROM SpecialityEntity s WHERE s.department.id = :departmentId")
    long countByDepartmentId(Long departmentId);

    @Modifying
    @Query("DELETE FROM SpecialityEntity s WHERE s.id = :id")
    int removeById(Long id);
}
