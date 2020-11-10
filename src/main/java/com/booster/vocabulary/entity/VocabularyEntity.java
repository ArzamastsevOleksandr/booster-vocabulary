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
import java.util.HashSet;
import java.util.Set;

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

    private Timestamp createdOn = Timestamp.from(Instant.now());

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<VocabularyEntryEntity> vocabularyEntries = new HashSet<>();

}
