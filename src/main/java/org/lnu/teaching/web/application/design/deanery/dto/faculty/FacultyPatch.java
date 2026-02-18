package org.lnu.teaching.web.application.design.deanery.dto.faculty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class FacultyPatch {
    private String name;
    private String website;
    private String email;
    private String phone;
    private String address;
    private String info;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean empty = true;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean nameUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean websiteUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean emailUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean phoneUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean addressUpdated;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private boolean infoUpdated;


    public void setName(String name) {
        empty = false;
        nameUpdated = true;

        this.name = name;
    }

    public void setWebsite(String website) {
        empty = false;
        websiteUpdated = true;

        this.website = website;
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

    public void setAddress(String address) {
        empty = false;
        addressUpdated = true;

        this.address = address;
    }

    public void setInfo(String info) {
        empty = false;
        infoUpdated = true;

        this.info = info;
    }
}
