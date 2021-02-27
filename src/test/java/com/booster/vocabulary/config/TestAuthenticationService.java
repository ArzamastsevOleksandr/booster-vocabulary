package com.booster.vocabulary.config;

import com.booster.vocabulary.controller.TestController;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.booster.vocabulary.util.TestUtil.*;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestAuthenticationService {

    private final RestTemplate restTemplate;

    public HttpHeaders getAuthorizationBearerHttpHeaders(String host, String port) {
        String username = randomUsername();
        return getHttpHeaders(host, port, username);
    }

    public AuthenticationResponse getAuthenticationResponse(String host, String port) {
        String username = randomUsername();
        HttpHeaders httpHeaders = getHttpHeaders(host, port, username);
        return AuthenticationResponse.builder()
                .username(username)
                .httpHeaders(httpHeaders)
                .build();
    }

    private HttpHeaders getHttpHeaders(String host, String port, String username) {
        String password = randomPassword();
        var signupRequest = TestController.SignupRequest.builder()
                .email(randomEmail())
                .username(username)
                .password(password)
                .build();
        restTemplate.postForObject(
                "http://{host}:{port}/api/signup",
                signupRequest, TestController.MessageResponse.class,
                host, port
        );
        var loginRequest = TestController.LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        var jwtResponse = restTemplate.postForObject(
                "http://{host}:{port}/api/login",
                loginRequest, TestController.JwtResponse.class,
                host, port
        );
        var httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtResponse.getJwt());

        return httpHeaders;
    }

    @Data
    @Builder
    public static class AuthenticationResponse {
        private HttpHeaders httpHeaders;
        private String username;
    }

}
