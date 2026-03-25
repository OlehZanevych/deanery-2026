package org.lnu.teaching.web.application.design.deanery.aspect;

import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.service.auth.AuthService;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@AllArgsConstructor
public class AuthAspect {

    private final AuthService authService;

    @Before("@annotation(auth) && within(@org.springframework.web.bind.annotation.RestController *)")
    public void verifyAuthoritySpecifiedForMethod(Auth auth) {
        authService.verifyAuthority(auth);
    }


    @Before("@within(auth) && !@annotation(org.lnu.teaching.web.application.design.deanery.annotation.Auth)" +
            "&& within(@org.springframework.web.bind.annotation.RestController *)")
    public void verifyAuthoritySpecifiedForControllerClass(Auth auth) {
        authService.verifyAuthority(auth);
    }
}
