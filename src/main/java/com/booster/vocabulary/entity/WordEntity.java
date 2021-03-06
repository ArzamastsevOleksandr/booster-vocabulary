package com.booster.vocabulary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Word")
@Table(name = "word")
@Data
public class WordEntity {

    @Id
    @Column(length = 36)
    private String id;

    private String name;

}
