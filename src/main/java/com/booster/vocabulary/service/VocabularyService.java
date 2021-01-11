package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.exception.*;
import com.booster.vocabulary.mapper.VocabularyMapper;
import com.booster.vocabulary.repository.LanguageRepository;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final UserRepository userRepository;
    private final LanguageToLearnRepository languageToLearnRepository;

    private final VocabularyMapper vocabularyMapper;

    public VocabularyDto create(VocabularyRequestDto vocabularyRequestDto) {
        Long userId = vocabularyRequestDto.getUserId();
        Long languageToLearnId = vocabularyRequestDto.getLanguageToLearnId();
        String vocabularyName = vocabularyRequestDto.getVocabularyName();

        if (vocabularyRepository.existsByUserIdAndName(userId, vocabularyName)) {
            throw new VocabularyEntityAlreadyExistsWithNameException(vocabularyName);
        }
        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(languageToLearnId)
                .orElseThrow(() -> new LanguageToLearnEntityByIdNotFoundException(languageToLearnId));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));

        LanguageEntity languageEntity = languageToLearnEntity.getLanguage();

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setName(vocabularyName);
        vocabularyEntity.setLanguage(languageEntity);
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        languageToLearnEntity.getVocabularies().add(vocabularyEntity);
        languageToLearnRepository.save(languageToLearnEntity);

        return vocabularyMapper.entity2Dto(vocabularyEntity);
    }

    public VocabularyDto findById(Long id) {
        return vocabularyRepository.findById(id)
                .map(vocabularyMapper::entity2Dto)
                .orElseThrow(() -> new VocabularyEntityByIdNotFoundException(id));
    }

    public List<VocabularyDto> findAllByUserId(Long userId) {
        return vocabularyRepository.findByUserId(userId)
                .stream()
                .map(vocabularyMapper::entity2Dto)
                .collect(toList());
    }

}
