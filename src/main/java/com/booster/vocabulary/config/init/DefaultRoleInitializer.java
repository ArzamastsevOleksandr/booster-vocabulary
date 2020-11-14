package com.booster.vocabulary.config.init;

import com.booster.vocabulary.entity.RoleEntity;
import com.booster.vocabulary.entity.RoleEnum;
import com.booster.vocabulary.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRoleInitializer {

    private final RoleRepository roleRepository;

    @EventListener
    public void mockUser(ContextRefreshedEvent event) {
        Stream.of(RoleEnum.values())
                .filter(roleEnum -> !roleRepository.existsByName(roleEnum))
                .map(r -> {
                    var roleEntity = new RoleEntity();
                    roleEntity.setName(r);
                    return roleEntity;
                }).forEach(roleRepository::save);
    }

}
