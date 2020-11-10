package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.entity.WordEntryEntity;
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
@TestPropertySource(properties = "application.yml")
@ExtendWith(SpringExtension.class)
@Transactional
class RepositoryTest {

    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordEntryRepository wordEntryRepository;

    @Test
    void wordRepository() {
        // given
        var wordEntryEntity1 = new WordEntryEntity();
        var wordEntryEntity2 = new WordEntryEntity();

        wordEntryRepository.save(wordEntryEntity1);
        wordEntryRepository.save(wordEntryEntity2);

        var wordEntity1 = new WordEntity();
        wordEntity1.setAntonyms(Set.of(wordEntryEntity1, wordEntryEntity2));
        wordEntity1.setSynonyms(Set.of(wordEntryEntity1, wordEntryEntity2));
        wordRepository.save(wordEntity1);
        // when
        List<WordEntity> wordEntities = wordRepository.findAll();
        // then
        assertThat(wordEntities).isNotEmpty();

        wordEntities.forEach(word -> {
            assertThat(word.getCreatedOn()).isNotNull();
            assertThat(word.getAntonyms().containsAll(Set.of(wordEntryEntity1, wordEntryEntity2)));
        });
    }

}