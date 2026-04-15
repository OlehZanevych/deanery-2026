package org.lnu.teaching.web.application.design.deanery.dto.speciality;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialityDto extends BaseSpecialityDto {
    private Long id;
}
