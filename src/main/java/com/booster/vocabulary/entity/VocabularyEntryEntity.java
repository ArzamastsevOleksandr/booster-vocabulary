package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer correctAnswersCount = 0;

    private Timestamp createdOn = Timestamp.from(Instant.now());

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
