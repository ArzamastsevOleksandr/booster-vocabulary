package com.booster.vocabulary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "BaseLanguage")
@Table(name = "base_language")
@Data
public class BaseLanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
