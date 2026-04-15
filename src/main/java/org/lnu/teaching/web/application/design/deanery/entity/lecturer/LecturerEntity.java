package org.lnu.teaching.web.application.design.deanery.entity.lecturer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.lnu.teaching.web.application.design.deanery.entity.department.DepartmentEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "lecturers")
@ToString(exclude = "department")
public class LecturerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String middleName;

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(nullable = false, length = 64)
    private String email;

    @Column(nullable = false, length = 32)
    private String phone;

    @Column(nullable = false, length = 64)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LecturerEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
