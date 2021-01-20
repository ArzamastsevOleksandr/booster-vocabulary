package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.entity.*;
import com.booster.vocabulary.exception.BaseLanguageEntityByIdNotFoundException;
import com.booster.vocabulary.exception.LanguageToLearnEntityAlreadyExistsException;
import com.booster.vocabulary.exception.LanguageToLearnEntityByIdNotFoundException;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.mapper.LanguageToLearnMapper;
import com.booster.vocabulary.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageToLearnService {

    private final LanguageToLearnRepository languageToLearnRepository;
    private final BaseLanguageRepository baseLanguageRepository;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;

    private final LanguageToLearnMapper languageToLearnMapper;

    public LanguageToLearnDto create(LanguageToLearnRequestDto languageToLearnRequestDto) {
        Long baseLanguageId = languageToLearnRequestDto.getBaseLanguageId();
        Long userId = languageToLearnRequestDto.getUserId();

        if (languageToLearnRepository.existsByUserIdAndBaseLanguageId(userId, baseLanguageId)) {
            throw new LanguageToLearnEntityAlreadyExistsException();
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));
        BaseLanguageEntity baseLanguageEntity = baseLanguageRepository.findById(baseLanguageId)
                .orElseThrow(() -> new BaseLanguageEntityByIdNotFoundException(baseLanguageId));

        var defaultVocabularyEntity = new VocabularyEntity();
        defaultVocabularyEntity.setBaseLanguage(baseLanguageEntity);
        defaultVocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(defaultVocabularyEntity);

        var languageToLearnEntity = new LanguageToLearnEntity();
        languageToLearnEntity.setBaseLanguage(baseLanguageEntity);
        languageToLearnEntity.setUser(userEntity);
        languageToLearnEntity.getVocabularies().add(defaultVocabularyEntity);
        languageToLearnRepository.save(languageToLearnEntity);

        return languageToLearnMapper.entity2Dto(languageToLearnEntity);
    }

    public LanguageToLearnDto findById(Long id) {
        return languageToLearnRepository.findById(id)
                .map(languageToLearnMapper::entity2Dto)
                .orElseThrow(() -> new LanguageToLearnEntityByIdNotFoundException(id));
    }

    public List<LanguageToLearnDto> findAllByUserId(Long userId) {
        return languageToLearnRepository.findAllByUserId(userId)
                .stream()
                .map(languageToLearnMapper::entity2Dto)
                .collect(toList());
    }

    @Transactional
    public void deleteById(Long id) {
        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(id)
                .orElseThrow(() -> new LanguageToLearnEntityByIdNotFoundException(id));

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
