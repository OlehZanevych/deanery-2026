package org.lnu.teaching.web.application.design.deanery.dto.course;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDto extends BaseCourseDto {
    private Long id;
}
