package org.lnu.teaching.web.application.design.deanery.dto.department;

import lombok.Data;

@Data
public class BaseDepartmentDto {
    private String name;
    private Long facultyId;
    private String email;
    private String phone;
    private String info;
}
