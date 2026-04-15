package org.lnu.teaching.web.application.design.deanery.entity.department;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.lnu.teaching.web.application.design.deanery.entity.faculty.FacultyEntity;

import java.util.Objects;

/**
 * Hibernate entity for the {@code departments} table.
 * Used both by the legacy JDBC-template-based {@code DepartmentRepository}
 * and as a JPA entity available for HQL / Spring Data JPA queries.
 *
 * <p><b>Lombok note:</b> {@code @Data} is intentionally avoided here.
 * Using {@code @Data} on JPA entities that have {@code @ManyToOne} relationships
 * is problematic because the generated {@code equals}, {@code hashCode}, and
 * {@code toString} methods may trigger Hibernate lazy-load proxies outside a
 * transaction, causing {@code LazyInitializationException}.
 * Instead, we use {@code @Getter}/{@code @Setter} and implement {@code equals}
 * and {@code hashCode} based solely on the entity PK — the standard JPA pattern.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "departments")
@ToString(exclude = "faculty")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyEntity faculty;

    @Column(nullable = false, length = 32)
    private String email;

    @Column(nullable = false, length = 32)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String info;

    public Long getFacultyId() {
        return faculty != null ? faculty.getId() : null;
    }

    public void setFacultyId(Long facultyId) {
        FacultyEntity ref = new FacultyEntity();
        ref.setId(facultyId);
        this.faculty = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
