package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "VocabularyEntry")
@Table(name = "vocabulary_entry")
@Data
@EqualsAndHashCode(exclude = {
        "antonyms",
        "synonyms"
})
@ToString(exclude = {
        "antonyms",
        "synonyms"
})
public class VocabularyEntryEntity {

    @Id
    @Column(length = 36)
    private String id;

    private Integer correctAnswersCount = 0;

    private Timestamp createdOn = new Timestamp(System.currentTimeMillis());

    // todo: consider allowing to add a translation
    // todo: rename to word
    @ManyToOne(fetch = FetchType.LAZY)
    private WordEntity targetWord;

    @OneToOne(fetch = FetchType.LAZY)
    private VocabularyEntity vocabulary;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<WordEntity> antonyms = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<WordEntity> synonyms = new ArrayList<>();

}
