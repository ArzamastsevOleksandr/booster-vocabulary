package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.entity.*;
import com.booster.vocabulary.exception.EntityByIdNotFoundException;
import com.booster.vocabulary.mapper.LanguageToLearnMapper;
import com.booster.vocabulary.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.booster.vocabulary.util.StringUtil.randomUuid;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageToLearnService {

    private static final String DEFAULT_VOCABULARY_NAME = "DEFAULT_VOCABULARY";

    private final LanguageToLearnRepository languageToLearnRepository;
    private final BaseLanguageRepository baseLanguageRepository;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;

    private final LanguageToLearnMapper languageToLearnMapper;

    public LanguageToLearnDto create(LanguageToLearnRequestDto languageToLearnRequestDto) {
        String baseLanguageId = languageToLearnRequestDto.getBaseLanguageId();
        String userId = languageToLearnRequestDto.getUserId();

        if (languageToLearnRepository.existsByUserIdAndBaseLanguageId(userId, baseLanguageId)) {
            throw new RuntimeException();
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityByIdNotFoundException(userId, UserEntity.class.getSimpleName()));
        BaseLanguageEntity baseLanguageEntity = baseLanguageRepository.findById(baseLanguageId)
                .orElseThrow(() -> new EntityByIdNotFoundException(baseLanguageId, BaseLanguageEntity.class.getSimpleName()));

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setId(randomUuid());
        vocabularyEntity.setName(DEFAULT_VOCABULARY_NAME);
        vocabularyEntity.setBaseLanguage(baseLanguageEntity);
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        var languageToLearnEntity = new LanguageToLearnEntity();
        languageToLearnEntity.setId(randomUuid());
        languageToLearnEntity.setBaseLanguage(baseLanguageEntity);
        languageToLearnEntity.setUser(userEntity);
        languageToLearnEntity.getVocabularies().add(vocabularyEntity);
        languageToLearnRepository.save(languageToLearnEntity);

        return languageToLearnMapper.entity2Dto(languageToLearnEntity);
    }

    public LanguageToLearnDto findById(String id) {
        return languageToLearnRepository.findById(id)
                .map(languageToLearnMapper::entity2Dto)
                .orElseThrow(RuntimeException::new);
    }

    public List<LanguageToLearnDto> findAllByUserId(String userId) {
        return languageToLearnRepository.findAllByUserId(userId)
                .stream()
                .map(languageToLearnMapper::entity2Dto)
                .collect(toList());
    }

    @Transactional
    public void deleteById(String id) {
        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        List<VocabularyEntity> vocabularyEntities = languageToLearnEntity.getVocabularies();
        languageToLearnEntity.setVocabularies(null);

        vocabularyEntities.forEach(ve -> {
            List<VocabularyEntryEntity> vocabularyEntryEntities = ve.getVocabularyEntries();
            ve.setVocabularyEntries(null);
            ve.setLanguageToLearn(null);
            vocabularyEntryEntities.forEach(vee -> {
                vee.setVocabulary(null);
                vocabularyEntryRepository.delete(vee);
            });
            vocabularyRepository.delete(ve);
        });
        languageToLearnRepository.delete(languageToLearnEntity);
    }

}
