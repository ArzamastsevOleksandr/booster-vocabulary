package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<LanguageDto> findAll() {
        return languageRepository.findAll()
                .stream()
                .map(languageEntity -> LanguageDto.builder()
                        .id(languageEntity.getId())
                        .name(languageEntity.getName())
                        .build()
                )
                .collect(toList());
    }

    public LanguageDto findById(Long languageId) {
        LanguageEntity languageEntity = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageEntityByIdNotFoundException(languageId));

        return LanguageDto.builder()
                .id(languageEntity.getId())
                .name(languageEntity.getName())
                .build();
    }

}
