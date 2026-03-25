package org.lnu.teaching.web.application.design.deanery.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String info;
}
