package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "LanguageToLearn")
@Table(name = "language_to_learn")
@Data
@EqualsAndHashCode(exclude = {
        "baseLanguage",
        "vocabularies",
        "user"
})
@ToString(exclude = {
        "baseLanguage",
        "vocabularies",
        "user"
})
public class LanguageToLearnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne(fetch = FetchType.LAZY)
    private BaseLanguageEntity baseLanguage;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntity> vocabularies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
