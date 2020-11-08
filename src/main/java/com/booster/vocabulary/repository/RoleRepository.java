package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.ERole;
import com.booster.vocabulary.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}
