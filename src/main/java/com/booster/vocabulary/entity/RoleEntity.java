package com.booster.vocabulary.entity;

import com.booster.vocabulary.entity.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "Role")
@Table(name = "role")
@Data
public class RoleEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}
