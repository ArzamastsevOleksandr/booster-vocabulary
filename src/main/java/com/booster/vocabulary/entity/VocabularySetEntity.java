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
import java.util.HashSet;
import java.util.Set;

@Entity(name = "VocabularySet")
@Table(name = "vocabulary_set")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "vocabularies",
        "user"
})
@ToString(exclude = {
        "vocabularies",
        "user"
})
public class VocabularySetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LanguageEnum languageName;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<VocabularyEntity> vocabularies = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
