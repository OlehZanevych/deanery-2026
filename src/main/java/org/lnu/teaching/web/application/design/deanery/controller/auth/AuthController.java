package org.lnu.teaching.web.application.design.deanery.controller.auth;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.user.UserCredentials;
import org.lnu.teaching.web.application.design.deanery.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("sign-in")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signIn(@RequestBody @Valid UserCredentials userCredentials) {
        authService.signIn(userCredentials);
    }

    @Auth
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("refresh-token")
    public void refreshToken() {
    }
}
