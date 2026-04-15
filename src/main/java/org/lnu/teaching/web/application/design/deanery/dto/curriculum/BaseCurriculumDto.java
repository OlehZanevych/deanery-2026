package org.lnu.teaching.web.application.design.deanery.dto.curriculum;

import lombok.Data;

@Data
public class BaseCurriculumDto {
    private String name;
    private Long specialityId;
    private Integer year;
    private String degree;
    private Integer durationYears;
    private String info;
}
