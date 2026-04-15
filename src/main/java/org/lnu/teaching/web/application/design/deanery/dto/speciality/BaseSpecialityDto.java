package org.lnu.teaching.web.application.design.deanery.dto.speciality;

import lombok.Data;

@Data
public class BaseSpecialityDto {
    private String name;
    private String code;
    private Long departmentId;
    private String info;
}
