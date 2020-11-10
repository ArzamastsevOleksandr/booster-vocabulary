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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "VocabularyEntry")
@Table(name = "vocabulary_entry")
@Data
@NoArgsConstructor
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

    @ManyToOne
    private WordEntity targetWord;

    private Integer correctAnswersCount = 0;

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<WordEntity> antonyms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<WordEntity> synonyms = new HashSet<>();

}
