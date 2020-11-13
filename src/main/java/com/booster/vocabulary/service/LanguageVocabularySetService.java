package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.request.LanguageVocabularySetRequestDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.exception.LanguageVocabularySetEntityAlreadyExistsException;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.repository.LanguageRepository;
import com.booster.vocabulary.repository.LanguageVocabularySetRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageVocabularySetService {

    private final LanguageVocabularySetRepository languageVocabularySetRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;

    public Long create(LanguageVocabularySetRequestDto languageVocabularySetRequestDto) {
        Optional<LanguageEntity> optionalLanguageEntity = languageRepository.findById(languageVocabularySetRequestDto.getLanguageId());
        if (optionalLanguageEntity.isEmpty()) {
            throw new LanguageEntityByIdNotFoundException(languageVocabularySetRequestDto.getLanguageId());
        }
        if (languageVocabularySetRepository.existsByUserIdAndLanguageId(
                languageVocabularySetRequestDto.getUserId(),
                languageVocabularySetRequestDto.getLanguageId()
        )) {
            throw new LanguageVocabularySetEntityAlreadyExistsException(languageVocabularySetRequestDto.getLanguageId());
        }
        UserEntity userEntity = userRepository.findById(languageVocabularySetRequestDto.getUserId())
                .orElseThrow(() -> new UserEntityByIdNotFoundException(languageVocabularySetRequestDto.getUserId()));

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setLanguage(optionalLanguageEntity.get());
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        var languageVocabularySetEntity = new LanguageVocabularySetEntity();
        languageVocabularySetEntity.setLanguage(optionalLanguageEntity.get());
        languageVocabularySetEntity.setUser(userEntity);
        languageVocabularySetEntity.getVocabularies().add(vocabularyEntity);
        languageVocabularySetRepository.save(languageVocabularySetEntity);

        return languageVocabularySetEntity.getId();
    }

}
