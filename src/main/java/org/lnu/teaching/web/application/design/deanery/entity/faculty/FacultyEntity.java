package org.lnu.teaching.web.application.design.deanery.entity.faculty;

import lombok.Data;

@Data
public class FacultyEntity {
    private Long id;
    private String name;
    private String website;
    private String email;
    private String phone;
    private String address;
    private String info;
}
