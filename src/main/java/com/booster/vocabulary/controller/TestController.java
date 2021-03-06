package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.config.security.jwt.JwtUtils;
import com.booster.vocabulary.entity.RoleEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.enums.RoleEnum;
import com.booster.vocabulary.repository.RoleRepository;
import com.booster.vocabulary.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder encoder;

    @GetMapping
    public List<RoleEntity> roles() {
        return roleRepository.findAll();
    }

    @GetMapping("/u")
    public List<UserEntity> users() {
        return userRepository.findAll();
    }

    @GetMapping("/s")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
    public List<UserEntity> users1() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.getJwtTokenBasedOnAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserEntity userEntity = new UserEntity();
        userEntity.setId(randomUuid());
        userEntity.setUsername(signUpRequest.getUsername());
         userEntity.setEmail(signUpRequest.getEmail());
                userEntity.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<RoleEntity> roleEntities = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRoleEntity = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roleEntities.add(userRoleEntity);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRoleEntity = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roleEntities.add(adminRoleEntity);

                        break;
                    default:
                        RoleEntity userRoleEntity = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roleEntities.add(userRoleEntity);
                }
            });
        }

        userEntity.setRoleEntities(roleEntities);
        userRepository.save(userEntity);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        String username;
        String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtResponse {
        String jwt;
        String id;
        String username;
        String email;
        List<String> roles;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequest {
        String username;
        String email;
        String password;
        Set<String> roles;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageResponse {
        String message;
    }

}
