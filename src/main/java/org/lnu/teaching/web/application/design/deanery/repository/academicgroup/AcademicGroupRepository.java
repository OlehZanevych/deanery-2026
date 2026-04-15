package org.lnu.teaching.web.application.design.deanery.repository.academicgroup;

import org.lnu.teaching.web.application.design.deanery.entity.academicgroup.AcademicGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademicGroupRepository extends JpaRepository<AcademicGroupEntity, Long> {
    Optional<AcademicGroupEntity> findByName(String name);

    List<AcademicGroupEntity> findByCurriculumId(Long curriculumId);

    List<AcademicGroupEntity> findByEnrollmentYear(Integer enrollmentYear);

    List<AcademicGroupEntity> findByCurriculumIdAndEnrollmentYear(Long curriculumId, Integer enrollmentYear);

    boolean existsByName(String name);

    long countByCurriculumId(Long curriculumId);

    @Query("SELECT g FROM AcademicGroupEntity g WHERE g.curriculum.speciality.id = :specialityId")
    List<AcademicGroupEntity> findBySpecialityId(Long specialityId);

    @Query("SELECT g FROM AcademicGroupEntity g WHERE g.enrollmentYear >= :fromYear ORDER BY g.enrollmentYear ASC")
    List<AcademicGroupEntity> findEnrolledFromYear(Integer fromYear);

    @Modifying
    @Query("DELETE FROM AcademicGroupEntity g WHERE g.id = :id")
    int removeById(Long id);

    @Modifying
    @Query("DELETE FROM AcademicGroupEntity g WHERE g.curriculum.id = :curriculumId")
    int removeByCurriculumId(Long curriculumId);
}
