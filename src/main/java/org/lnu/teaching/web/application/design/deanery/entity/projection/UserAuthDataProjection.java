package org.lnu.teaching.web.application.design.deanery.entity.projection;

public interface UserAuthDataProjection {
    String getUsername();
    String getPasswordHash();
    boolean getIsAdmin();
}
