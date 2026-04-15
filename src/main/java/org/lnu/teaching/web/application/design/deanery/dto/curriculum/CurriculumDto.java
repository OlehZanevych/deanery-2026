package org.lnu.teaching.web.application.design.deanery.dto.curriculum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurriculumDto extends BaseCurriculumDto {
    private Long id;
}
