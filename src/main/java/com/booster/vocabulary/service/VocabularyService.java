package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularySetEntity;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntityAlreadyExistsWithNameException;
import com.booster.vocabulary.exception.VocabularyEntityByIdNotFoundException;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import com.booster.vocabulary.repository.VocabularySetRepository;
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
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final UserRepository userRepository;
    private final VocabularySetRepository vocabularySetRepository;

    public List<VocabularyDto> findAllForUserId(Long userId) {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByUserId(userId);

        return vocabularyEntityList.stream()
                .map(vocabularyEntity ->
                        VocabularyDto.builder()
                                .id(vocabularyEntity.getId())
                                .name(vocabularyEntity.getName())
                                .createdOn(vocabularyEntity.getCreatedOn())
                                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                                .build()
                ).collect(toList());
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
                                .map(vocabularyEntryEntity -> VocabularyEntryDto.builder()
                                        .id(vocabularyEntryEntity.getId())
                                        .targetWord(vocabularyEntryEntity.getTargetWord().getWord())
                                        .build()
                                )
                                .collect(toList())
                )
                .build();
    }

    public Long create(VocabularyRequestDto vocabularyRequestDto) {
        UserEntity userEntity = userRepository.findById(vocabularyRequestDto.getUserId())
                .orElseThrow(() -> new UserEntityByIdNotFoundException(vocabularyRequestDto.getUserId()));

        if (vocabularyRepository.existsByUserIdAndName(vocabularyRequestDto.getUserId(), vocabularyRequestDto.getVocabularyName())) {
            throw new VocabularyEntityAlreadyExistsWithNameException(vocabularyRequestDto.getVocabularyName());
        }

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setName(vocabularyRequestDto.getVocabularyName());
        vocabularyEntity.setLanguageName(vocabularyRequestDto.getLanguageName());
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        VocabularySetEntity vocabularySetEntity = vocabularySetRepository.findByLanguageNameAndUserId(
                vocabularyRequestDto.getLanguageName(),
                vocabularyRequestDto.getUserId()
        ).orElseGet(() -> {
            var newVocabularySetEntity = new VocabularySetEntity();
            newVocabularySetEntity.setLanguageName(vocabularyRequestDto.getLanguageName());
            newVocabularySetEntity.setUser(userEntity);
            return newVocabularySetEntity;
        });
        vocabularySetEntity.getVocabularies().add(vocabularyEntity);
        vocabularySetRepository.save(vocabularySetEntity);

        return vocabularyEntity.getId();
    }

}