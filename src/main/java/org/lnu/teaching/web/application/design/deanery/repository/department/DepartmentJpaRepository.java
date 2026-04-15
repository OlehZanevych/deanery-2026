package org.lnu.teaching.web.application.design.deanery.repository.department;

import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link DepartmentEntity}.
 *
 * <p>This interface co-exists with the legacy JDBC-based {@link DepartmentRepository}
 * and its implementation {@code DepartmentRepositoryImpl}.  Both repositories target
 * the same {@code departments} table — the JDBC layer handles write operations for
 * the existing endpoints, while this JPA repository is used by the newer services
 * (speciality, lecturer, etc.) to resolve {@code DepartmentEntity} references.
 *
 * <p>Key usage pattern: {@code getReferenceById(id)} returns a Hibernate proxy that
 * holds only the PK — no SELECT is issued until a non-id field is actually accessed.
 * This is perfect for setting FK associations on new entities before saving them.
 */
public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, Long> {
}
