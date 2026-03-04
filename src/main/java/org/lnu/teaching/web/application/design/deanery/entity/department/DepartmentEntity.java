package org.lnu.teaching.web.application.design.deanery.entity.department;

import lombok.Data;

@Data
public class DepartmentEntity {
    private Long id;
    private String name;
    private Long facultyId;
    private String email;
    private String phone;
    private String info;
}
