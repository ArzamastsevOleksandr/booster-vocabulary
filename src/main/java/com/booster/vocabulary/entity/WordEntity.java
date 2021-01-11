package com.booster.vocabulary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Word")
@Table(name = "word")
@Data
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
