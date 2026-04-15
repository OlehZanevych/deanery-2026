package org.lnu.teaching.web.application.design.deanery.dto.lecturer;

import lombok.Data;

@Data
public class BaseLecturerDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String title;
    private Long departmentId;
    private String info;
}
