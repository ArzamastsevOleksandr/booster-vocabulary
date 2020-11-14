package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.RoleEntity;
import com.booster.vocabulary.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleEnum name);

    boolean existsByName(RoleEnum name);

}
