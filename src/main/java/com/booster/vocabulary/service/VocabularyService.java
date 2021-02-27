package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.mapper.VocabularyMapper;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.booster.vocabulary.util.StringUtil.randomUuid;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final UserRepository userRepository;
    private final LanguageToLearnRepository languageToLearnRepository;

    private final VocabularyMapper vocabularyMapper;

    public VocabularyDto create(VocabularyRequestDto vocabularyRequestDto) {
        String userId = vocabularyRequestDto.getUserId();
        String languageToLearnId = vocabularyRequestDto.getLanguageToLearnId();
        String vocabularyName = vocabularyRequestDto.getVocabularyName();

        if (vocabularyRepository.existsByUserIdAndName(userId, vocabularyName)) {
            throw new RuntimeException();
        }
        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(languageToLearnId)
                .orElseThrow(RuntimeException::new);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);

        BaseLanguageEntity baseLanguageEntity = languageToLearnEntity.getBaseLanguage();

        var vocabularyEntity = new VocabularyEntity();
        vocabularyEntity.setId(randomUuid());
        vocabularyEntity.setName(vocabularyName);
        vocabularyEntity.setBaseLanguage(baseLanguageEntity);
        vocabularyEntity.setLanguageToLearn(languageToLearnEntity);
        vocabularyEntity.setUser(userEntity);
        vocabularyRepository.save(vocabularyEntity);

        languageToLearnEntity.getVocabularies().add(vocabularyEntity);
        languageToLearnRepository.save(languageToLearnEntity);

        return vocabularyMapper.entity2Dto(vocabularyEntity);
    }

    public VocabularyDto findById(String id) {
        return vocabularyRepository.findById(id)
                .map(vocabularyMapper::entity2Dto)
                .orElseThrow(RuntimeException::new);
    }

    public List<VocabularyDto> findAllByUserIdAndLanguageToLearnId(String userId, String languageToLearnId) {
        return vocabularyRepository.findByUserIdAndLanguageToLearnId(userId, languageToLearnId)
                .stream()
                .map(vocabularyMapper::entity2Dto)
                .collect(toList());
    }

    // todo: test that entries are deleted as well
    public void deleteById(String id) {
        vocabularyRepository.deleteById(id);
    }

}
