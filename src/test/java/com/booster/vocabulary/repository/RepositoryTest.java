package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "application-test.yml")
@ExtendWith(SpringExtension.class)
@Transactional
class RepositoryTest {

    @Autowired
    WordRepository wordRepository;
    @Autowired
    VocabularyEntryRepository vocabularyEntryRepository;

    @Test
    void vocabularyEntryRepository() {
        // given
        var wordEntity1 = new WordEntity();
        var wordEntity2 = new WordEntity();

        wordRepository.save(wordEntity1);
        wordRepository.save(wordEntity2);

        var vocabularyEntryEntity1 = new VocabularyEntryEntity();

        vocabularyEntryEntity1.setAntonyms(List.of(wordEntity1, wordEntity2));
        vocabularyEntryEntity1.setSynonyms(List.of(wordEntity1, wordEntity2));
        vocabularyEntryRepository.save(vocabularyEntryEntity1);
        // when
        List<VocabularyEntryEntity> vocabularyEntries = vocabularyEntryRepository.findAll();
        // then
        assertThat(vocabularyEntries).isNotEmpty();

        vocabularyEntries.forEach(vocabularyEntryEntity -> {
            assertThat(vocabularyEntryEntity.getCreatedOn()).isNotNull();
            assertThat(vocabularyEntryEntity.getAntonyms().containsAll(Set.of(wordEntity1, wordEntity2)));
        });
    }

}