package com.booster.vocabulary.entity;

import lombok.Data;

import javax.persistence.*;

// TODO: generate ids in constructor?
@Entity(name = "BaseLanguage")
@Table(name = "base_language")
@Data
public class BaseLanguageEntity {

    @Id
    @Column(length = 36)
    private String id;

    private String name;

}
