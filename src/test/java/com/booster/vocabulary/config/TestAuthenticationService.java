package com.booster.vocabulary.config;

import com.booster.vocabulary.controller.TestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.booster.vocabulary.util.TestUtil.*;

@Profile("test")
@Service
public class TestAuthenticationService {

    @Autowired
    private RestTemplate restTemplate;

    public HttpHeaders getAuthorizationBearerHttpHeaders(String host, String port) {
        String username = randomUsername();
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

}
