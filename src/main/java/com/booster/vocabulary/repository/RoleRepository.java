package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.RoleEntity;
import com.booster.vocabulary.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByName(RoleEnum name);

    boolean existsByName(RoleEnum name);

}
