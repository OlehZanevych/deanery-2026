package org.lnu.teaching.web.application.design.deanery.service.auth.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lnu.teaching.web.application.design.deanery.annotation.Auth;
import org.lnu.teaching.web.application.design.deanery.dto.user.UserCredentials;
import org.lnu.teaching.web.application.design.deanery.entity.projection.UserAuthDataProjection;
import org.lnu.teaching.web.application.design.deanery.exception.UnauthenticatedException;
import org.lnu.teaching.web.application.design.deanery.exception.UnauthorizedException;
import org.lnu.teaching.web.application.design.deanery.repository.user.UserRepository;
import org.lnu.teaching.web.application.design.deanery.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String AUTH_TOKEN_HEADER = "Authorization";
    private static final String USER_DATA_CLAIMS = "userData";
    private static final String AUTH_ISSUER = "Deanery Service Auth";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String IS_ADMIN_PROPERTY = "isAdmin";
    private static final String USERNAME_PROPERTY = "username";

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final long authTokenLifetime;
    private final String jwtSigningKey;

    public AuthServiceImpl(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           @Value("${AUTH_TOKEN_LIFETIME}") long authTokenLifetime,
                           @Value("${JWT_SIGNING_KEY}") String jwtSigningKey) {

        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authTokenLifetime = 1000 * authTokenLifetime;
        this.jwtSigningKey = Base64.getEncoder().encodeToString(jwtSigningKey.getBytes());
    }

    record UserData(String username, boolean isAdmin) {
    }

    @Override
    public void signIn(UserCredentials userCredentials) {
        String authToken = generateToken(userCredentials);
        httpServletResponse.setHeader(AUTH_TOKEN_HEADER, authToken);
    }

    @Override
    public void verifyAuthority(Auth auth) {
        String authHeader = httpServletRequest.getHeader(AUTH_TOKEN_HEADER);
        if (authHeader == null) {
            throw new UnauthenticatedException("Missed Authorization header!");
        }

        String[] authHeaderParts = authHeader.trim().split("\\s+");
        if (authHeaderParts.length != 2 || !TOKEN_PREFIX.equals(authHeaderParts[0])) {
            throw new UnauthenticatedException("Invalid format of Auth header!");
        }

        String jwtToken = authHeaderParts[1];

        Map<String, Object> userData;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSigningKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            userData = claims.get(USER_DATA_CLAIMS, Map.class);

            refreshToken(new UserData((String) userData.get(USERNAME_PROPERTY) ,(Boolean) userData.get(IS_ADMIN_PROPERTY)));
        } catch (RuntimeException e) {
            throw new UnauthenticatedException("Invalid JWT token!");
        }

        if (auth.isAdmin()) {
            boolean isAdmin = (Boolean) userData.get(IS_ADMIN_PROPERTY);
            if (!isAdmin) {
                throw new UnauthorizedException("Customer is not authorized to call this API!");
            }
        }
    }


    private String generateToken(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();

        UserAuthDataProjection userAuthData = userRepository.findUserAuthDataByUsername(username);
        if (userAuthData == null) {
            throw new UnauthenticatedException("Invalid username!");
        }

        String password = userCredentials.getPassword();
        String passwordHash = userAuthData.getPasswordHash();
        if (!bCryptPasswordEncoder.matches(password, passwordHash)) {
            throw new UnauthenticatedException("Invalid password!");
        }

        UserData userData = new UserData(username, userAuthData.getIsAdmin());

        return generateToken(userData);
    }

    private String generateToken(UserData userData) {
        Claims claims = Jwts.claims()
                .add(USER_DATA_CLAIMS, userData)
                .build();

        long creationTime = System.currentTimeMillis();
        long expirationTime = creationTime + authTokenLifetime;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(AUTH_ISSUER)
                .setIssuedAt(new Date(creationTime))
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
                .compact();
    }

    private void refreshToken(UserData userData) {
        String refreshedToken = generateToken(userData);
        httpServletResponse.setHeader(AUTH_TOKEN_HEADER, refreshedToken);
    }
}
