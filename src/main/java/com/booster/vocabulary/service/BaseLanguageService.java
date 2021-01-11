package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.BaseLanguageDto;
import com.booster.vocabulary.exception.BaseLanguageEntityByIdNotFoundException;
import com.booster.vocabulary.mapper.BaseLanguageMapper;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseLanguageService {

    private final BaseLanguageMapper baseLanguageMapper;
    private final BaseLanguageRepository baseLanguageRepository;

    public List<BaseLanguageDto> findAll() {
        return baseLanguageRepository.findAll()
                .stream()
                .map(baseLanguageMapper::entity2Dto)
                .collect(toList());
    }

    public BaseLanguageDto findById(Long id) {
        return baseLanguageRepository.findById(id)
                .map(baseLanguageMapper::entity2Dto)
                .orElseThrow(() -> new BaseLanguageEntityByIdNotFoundException(id));
    }

}
