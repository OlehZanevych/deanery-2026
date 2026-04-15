package org.lnu.teaching.web.application.design.deanery.entity.curriculum;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.lnu.teaching.web.application.design.deanery.entity.speciality.SpecialityEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "curricula")
@ToString(exclude = "speciality")
public class CurriculumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id", nullable = false)
    private SpecialityEntity speciality;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 32)
    private String degree;

    @Column(nullable = false)
    private Integer durationYears;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurriculumEntity that = (CurriculumEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
