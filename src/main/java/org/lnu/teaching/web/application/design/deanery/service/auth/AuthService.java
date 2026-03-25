package org.lnu.teaching.web.application.design.deanery.service.auth;

import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.user.UserCredentials;

public interface AuthService {
    void signIn(UserCredentials userCredentials);
    void verifyAuthority(Auth auth);
}