package org.lnu.teaching.web.application.design.deanery.dto.lecturer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LecturerDto extends BaseLecturerDto {
    private Long id;
}
