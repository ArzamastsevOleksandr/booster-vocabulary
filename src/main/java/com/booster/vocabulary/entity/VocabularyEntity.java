package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Vocabulary")
@Table(name = "vocabulary")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "language",
        "vocabularyEntries",
        "user"
})
@ToString(exclude = {
        "language",
        "vocabularyEntries",
        "user"
})
public class VocabularyEntity {

    public static final String DEFAULT_VOCABULARY_NAME = "Default";

    // todo: entryCount

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = DEFAULT_VOCABULARY_NAME;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne
    private LanguageEntity language;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntryEntity> vocabularyEntries = new ArrayList<>();

}
