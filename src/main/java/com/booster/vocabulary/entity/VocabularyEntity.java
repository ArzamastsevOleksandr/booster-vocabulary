package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
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

    // todo: unique constraint
    private String name;

    private Timestamp createdOn = new Timestamp(System.currentTimeMillis());

    @ManyToOne(fetch = FetchType.LAZY)
    private BaseLanguageEntity baseLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageToLearnEntity languageToLearn;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntryEntity> vocabularyEntries = new ArrayList<>();

}
