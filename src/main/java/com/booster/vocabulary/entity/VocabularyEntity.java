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
        "languageToLearn",
        "vocabularyEntries",
        "user"
})
@ToString(exclude = {
        "baseLanguage",
        "languageToLearn",
        "vocabularyEntries",
        "user"
})
public class VocabularyEntity {

    // todo: entryCount

    @Id
    @Column(length = 36)
    private String id;

    private String name;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne
    private BaseLanguageEntity baseLanguage;

    @ManyToOne
    private LanguageToLearnEntity languageToLearn;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntryEntity> vocabularyEntries = new ArrayList<>();

}
