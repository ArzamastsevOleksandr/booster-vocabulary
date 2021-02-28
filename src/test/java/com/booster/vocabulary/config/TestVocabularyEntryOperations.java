package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import com.booster.vocabulary.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booster.vocabulary.util.CollectionUtil.arrayListOf;
import static com.booster.vocabulary.util.StringUtil.randomUuid;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestVocabularyEntryOperations {

    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final WordService wordService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VocabularyEntryEntity createVocabularyEntryEntity(UserEntity userEntity,
                                                             VocabularyEntity vocabularyEntity) {
        VocabularyEntryEntity vocabularyEntryEntity = new VocabularyEntryEntity();
        vocabularyEntryEntity.setId(randomUuid());
        vocabularyEntryEntity.setTargetWord(wordService.getWordEntityByNameOrCreateAndSave("one"));
        vocabularyEntryEntity.setSynonyms(arrayListOf(wordService.getWordEntityByNameOrCreateAndSave("one_synonym1")));
        vocabularyEntryEntity.setAntonyms(arrayListOf(wordService.getWordEntityByNameOrCreateAndSave("one_antonym1")));
        vocabularyEntryEntity.setUser(userEntity);
        vocabularyEntryEntity.setVocabulary(vocabularyEntity);
        VocabularyEntryEntity save = vocabularyEntryRepository.save(vocabularyEntryEntity);

        vocabularyEntity.getVocabularyEntries().add(save);
        vocabularyRepository.save(vocabularyEntity);

        return save;
    }

}
