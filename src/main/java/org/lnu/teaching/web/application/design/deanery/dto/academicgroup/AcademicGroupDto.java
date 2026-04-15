package org.lnu.teaching.web.application.design.deanery.dto.academicgroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AcademicGroupDto extends BaseAcademicGroupDto {
    private Long id;
}
