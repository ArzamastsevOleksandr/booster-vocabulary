package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.mapper.LanguageMapper;
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

    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;

    public List<LanguageDto> findAll() {
        return languageRepository.findAll()
                .stream()
                .map(languageMapper::entity2Dto)
                .collect(toList());
    }

    public LanguageDto findById(Long id) {
        return languageRepository.findById(id)
                .map(languageMapper::entity2Dto)
                .orElseThrow(() -> new LanguageEntityByIdNotFoundException(id));
    }

}
