package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.request.LanguageVocabularySetRequestDto;
import com.booster.vocabulary.dto.response.LanguageVocabularySetDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.exception.LanguageVocabularySetEntityAlreadyExistsException;
import com.booster.vocabulary.exception.LanguageVocabularySetEntityByIdNotFoundException;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.repository.LanguageRepository;
import com.booster.vocabulary.repository.LanguageVocabularySetRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageVocabularySetService {

    private static final Function<VocabularyEntity, VocabularyDto> vocabularyEntity2VocabularyDto = ve -> VocabularyDto.builder()
            .id(ve.getId())
            .name(ve.getName())
            .build();

    private static final Function<LanguageEntity, LanguageDto> languageEntity2LanguageDto = le -> LanguageDto.builder()
            .id(le.getId())
            .name(le.getName())
            .build();

    private final LanguageVocabularySetRepository languageVocabularySetRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;

    public Long create(LanguageVocabularySetRequestDto languageVocabularySetRequestDto) {
        Long languageId = languageVocabularySetRequestDto.getLanguageId();
        Long userId = languageVocabularySetRequestDto.getUserId();

        Optional<LanguageEntity> optionalLanguageEntity = languageRepository.findById(languageId);
        if (optionalLanguageEntity.isEmpty()) {
            throw new LanguageEntityByIdNotFoundException(languageId);
        }
        if (languageVocabularySetRepository.existsByUserIdAndLanguageId(userId, languageId)) {
            throw new LanguageVocabularySetEntityAlreadyExistsException(languageId);
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));

        LanguageEntity languageEntity = optionalLanguageEntity.get();

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setLanguage(languageEntity);
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        var languageVocabularySetEntity = new LanguageVocabularySetEntity();
        languageVocabularySetEntity.setLanguage(languageEntity);
        languageVocabularySetEntity.setUser(userEntity);
        languageVocabularySetEntity.getVocabularies().add(vocabularyEntity);
        languageVocabularySetRepository.save(languageVocabularySetEntity);

        return languageVocabularySetEntity.getId();
    }

    public LanguageVocabularySetDto findById(Long languageVocabularySetId) {
        LanguageVocabularySetEntity languageVocabularySetEntity = languageVocabularySetRepository.findById(languageVocabularySetId)
                .orElseThrow(() -> new LanguageVocabularySetEntityByIdNotFoundException(languageVocabularySetId));

        return LanguageVocabularySetDto.builder()
                .id(languageVocabularySetEntity.getId())
                .languageDto(languageEntity2LanguageDto.apply(languageVocabularySetEntity.getLanguage()))
                .vocabularyDtoList(languageVocabularySetEntity.getVocabularies()
                        .stream()
                        .map(vocabularyEntity2VocabularyDto)
                        .collect(toList())
                )
                .build();
    }

    public List<LanguageVocabularySetDto> findAllForUserId(Long userId) {
        return languageVocabularySetRepository.findAllByUserId(userId)
                .stream()
                .map(languageVocabularySetEntity -> LanguageVocabularySetDto.builder()
                        .id(languageVocabularySetEntity.getId())
                        .languageDto(languageEntity2LanguageDto.apply(languageVocabularySetEntity.getLanguage()))
                        .build()
                )
                .collect(toList());
    }

}
