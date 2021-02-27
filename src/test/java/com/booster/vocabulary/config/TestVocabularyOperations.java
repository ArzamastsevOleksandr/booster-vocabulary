package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestVocabularyOperations {

    private final VocabularyRepository vocabularyRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VocabularyEntity createVocabularyEntity(UserEntity userEntity,
                                                   LanguageToLearnEntity languageToLearnEntity,
                                                   String name) {
        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setId(randomUuid());
        vocabularyEntity.setName(name);
        vocabularyEntity.setUser(userEntity);
        vocabularyEntity.setLanguageToLearn(languageToLearnEntity);
        return vocabularyRepository.save(vocabularyEntity);
    }

}
