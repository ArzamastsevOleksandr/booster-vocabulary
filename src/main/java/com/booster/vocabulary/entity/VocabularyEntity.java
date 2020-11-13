package com.booster.vocabulary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        "vocabularyEntries",
        "user"
})
@ToString(exclude = {
        "vocabularyEntries",
        "user"
})
public class VocabularyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "Default";

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LanguageEnum languageName;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntryEntity> vocabularyEntries = new ArrayList<>();

}
