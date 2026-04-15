package org.lnu.teaching.web.application.design.deanery.dto.academicgroup;

import lombok.Data;

@Data
public class BaseAcademicGroupDto {
    private String name;
    private Long curriculumId;
    private Integer enrollmentYear;
    private String info;
}
