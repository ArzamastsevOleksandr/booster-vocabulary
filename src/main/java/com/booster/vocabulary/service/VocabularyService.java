package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.exception.LanguageVocabularySetEntityByLanguageIdNotFoundException;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntityAlreadyExistsWithNameException;
import com.booster.vocabulary.exception.VocabularyEntityByIdNotFoundException;
import com.booster.vocabulary.repository.LanguageRepository;
import com.booster.vocabulary.repository.LanguageVocabularySetRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyService {

    private static final Function<VocabularyEntryEntity, VocabularyEntryDto> vocabularyEntryEntity2VocabularyEntryDto =
            vee -> VocabularyEntryDto.builder()
                    .id(vee.getId())
                    .targetWord(vee.getTargetWord().getName())
                    .build();

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final UserRepository userRepository;
    private final LanguageVocabularySetRepository languageVocabularySetRepository;
    private final LanguageRepository languageRepository;

    public Long create(VocabularyRequestDto vocabularyRequestDto) {
        Long userId = vocabularyRequestDto.getUserId();
        Long languageId = vocabularyRequestDto.getLanguageId();
        String vocabularyName = vocabularyRequestDto.getVocabularyName();

        LanguageVocabularySetEntity languageVocabularySetEntity = languageVocabularySetRepository.findByUserIdAndLanguageId(
                userId, languageId
        ).orElseThrow(
                () -> new LanguageVocabularySetEntityByLanguageIdNotFoundException(languageId)
        );
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));

        if (vocabularyRepository.existsByUserIdAndName(userId, vocabularyName)) {
            throw new VocabularyEntityAlreadyExistsWithNameException(vocabularyName);
        }

        LanguageEntity languageEntity = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageEntityByIdNotFoundException(languageId));

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setName(vocabularyName);
        vocabularyEntity.setLanguage(languageEntity);
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        languageVocabularySetEntity.getVocabularies().add(vocabularyEntity);
        languageVocabularySetRepository.save(languageVocabularySetEntity);

        return vocabularyEntity.getId();
    }

    public VocabularyDto findById(Long vocabularyId) {
        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyEntityByIdNotFoundException(vocabularyId));

        return VocabularyDto.builder()
                .id(vocabularyEntity.getId())
                .name(vocabularyEntity.getName())
                .createdOn(vocabularyEntity.getCreatedOn())
                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                .vocabularyEntries(
                        vocabularyEntity.getVocabularyEntries()
                                .stream()
                                .map(vocabularyEntryEntity2VocabularyEntryDto)
                                .collect(toList())
                )
                .build();
    }

    public List<VocabularyDto> findAllForUserId(Long userId) {
        return vocabularyRepository.findByUserId(userId).stream()
                .map(vocabularyEntity ->
                        VocabularyDto.builder()
                                .id(vocabularyEntity.getId())
                                .name(vocabularyEntity.getName())
                                .createdOn(vocabularyEntity.getCreatedOn())
                                // todo: bug
                                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                                .build()
                ).collect(toList());
    }

}
