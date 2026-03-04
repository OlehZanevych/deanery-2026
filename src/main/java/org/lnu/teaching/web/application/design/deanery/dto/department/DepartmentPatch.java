package org.lnu.teaching.web.application.design.deanery.dto.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class DepartmentPatch {
    private String name;
    private Long facultyId;
    private String email;
    private String phone;
    private String info;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean empty = true;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean nameUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean facultyIdUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean emailUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean phoneUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean infoUpdated;

    public void setName(String name) {
        empty = false;
        nameUpdated = true;

        this.name = name;
    }

    public void setFacultyId(Long facultyId) {
        empty = false;
        facultyIdUpdated = true;

        this.facultyId = facultyId;
    }

    public void setEmail(String email) {
        empty = false;
        emailUpdated = true;

        this.email = email;
    }

    public void setPhone(String phone) {
        empty = false;
        phoneUpdated = true;

        this.phone = phone;
    }

    public void setInfo(String info) {
        empty = false;
        infoUpdated = true;

        this.info = info;
    }
}
