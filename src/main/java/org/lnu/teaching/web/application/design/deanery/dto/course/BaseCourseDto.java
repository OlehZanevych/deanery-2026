package org.lnu.teaching.web.application.design.deanery.dto.course;

import lombok.Data;

@Data
public class BaseCourseDto {
    private String name;
    private Integer credits;
    private Integer hours;
    private Integer semester;
    private Long curriculumId;
    private Long lecturerId;
    private String info;
}
