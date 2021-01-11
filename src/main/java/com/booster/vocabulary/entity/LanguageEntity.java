package com.booster.vocabulary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Language")
@Table(name = "language")
@Data
public class LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
