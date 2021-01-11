package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Vocabulary")
@Table(name = "vocabulary")
@Data
@EqualsAndHashCode(exclude = {
        "baseLanguage",
        "vocabularyEntries",
        "user"
})
@ToString(exclude = {
        "baseLanguage",
        "vocabularyEntries",
        "user"
})
public class VocabularyEntity {

    public static final String DEFAULT_VOCABULARY_NAME = "DEFAULT";

    // todo: entryCount

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = DEFAULT_VOCABULARY_NAME;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne
    private BaseLanguageEntity baseLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntryEntity> vocabularyEntries = new ArrayList<>();

}
