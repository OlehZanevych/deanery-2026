package org.lnu.teaching.web.application.design.deanery.entity.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.lnu.teaching.web.application.design.deanery.entity.curriculum.CurriculumEntity;
import org.lnu.teaching.web.application.design.deanery.entity.lecturer.LecturerEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "courses")
@ToString(exclude = {"curriculum", "lecturer"})
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false)
    private Integer credits;

    @Column(nullable = false)
    private Integer hours;

    @Column(nullable = false)
    private Integer semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_id", nullable = false)
    private CurriculumEntity curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private LecturerEntity lecturer;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
