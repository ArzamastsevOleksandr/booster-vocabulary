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
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Vocabulary")
@Table(name = "vocabulary")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "words",
        "user"
})
@ToString(exclude = {
        "words",
        "user"
})
public class VocabularyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<WordEntity> words = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
