package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.ERole;
import com.booster.vocabulary.entity.Role;
import com.booster.vocabulary.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRoleInitializer {

    // todo: save to db if not exists

    private final RoleRepository roleRepository;

    @EventListener
    @Transactional
    public void mockUser(ContextRefreshedEvent event) {
        Stream.of(ERole.values())
                .map(r -> {
                    Role role = new Role();
                    role.setName(r);
                    return role;
                }).forEach(roleRepository::save);
    }

}
