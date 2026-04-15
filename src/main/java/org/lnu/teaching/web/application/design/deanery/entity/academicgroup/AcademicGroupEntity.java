package org.lnu.teaching.web.application.design.deanery.entity.academicgroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "academic_groups")
@ToString(exclude = "curriculum")
public class AcademicGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private CurriculumEntity curriculum;

    @Column(nullable = false)
    private Integer enrollmentYear;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcademicGroupEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
