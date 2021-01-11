package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "LanguageVocabularySet")
@Table(name = "language_vocabulary_set")
@Data
@EqualsAndHashCode(exclude = {
        "language",
        "vocabularies",
        "user"
})
@ToString(exclude = {
        "language",
        "vocabularies",
        "user"
})
public class LanguageVocabularySetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageEntity language;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntity> vocabularies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
