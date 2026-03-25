package org.lnu.teaching.web.application.design.deanery.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String password;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String info;
}
