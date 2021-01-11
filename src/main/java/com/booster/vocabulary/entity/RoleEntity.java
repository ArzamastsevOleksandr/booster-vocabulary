package com.booster.vocabulary.entity;

import com.booster.vocabulary.entity.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "Role")
@Table(name = "role")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}
