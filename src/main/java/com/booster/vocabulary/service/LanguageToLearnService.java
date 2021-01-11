package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.exception.LanguageEntityByIdNotFoundException;
import com.booster.vocabulary.exception.LanguageToLearnEntityAlreadyExistsException;
import com.booster.vocabulary.exception.LanguageToLearnEntityByIdNotFoundException;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.mapper.LanguageToLearnMapper;
import com.booster.vocabulary.repository.LanguageRepository;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageToLearnService {

    private final LanguageToLearnRepository languageToLearnRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;

    private final LanguageToLearnMapper languageToLearnMapper;

    public LanguageToLearnDto create(LanguageToLearnRequestDto languageToLearnRequestDto) {
        Long languageId = languageToLearnRequestDto.getLanguageId();
        Long userId = languageToLearnRequestDto.getUserId();

        Optional<LanguageEntity> optionalLanguageEntity = languageRepository.findById(languageId);
        if (optionalLanguageEntity.isEmpty()) {
            throw new LanguageEntityByIdNotFoundException(languageId);
        }
        if (languageToLearnRepository.existsByUserIdAndLanguageId(userId, languageId)) {
            throw new LanguageToLearnEntityAlreadyExistsException(languageId);
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));

        LanguageEntity languageEntity = optionalLanguageEntity.get();

        var defaultVocabularyEntity = new VocabularyEntity();
        defaultVocabularyEntity.setLanguage(languageEntity);
        defaultVocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(defaultVocabularyEntity);

        var languageToLearnEntity = new LanguageToLearnEntity();
        languageToLearnEntity.setLanguage(languageEntity);
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

}
