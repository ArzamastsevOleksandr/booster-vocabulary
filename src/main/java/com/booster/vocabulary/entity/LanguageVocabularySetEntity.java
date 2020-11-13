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
import java.util.ArrayList;
import java.util.List;

@Entity(name = "LanguageVocabularySet")
@Table(name = "language_vocabulary_set")
@Data
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    private LanguageEntity language;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VocabularyEntity> vocabularies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
