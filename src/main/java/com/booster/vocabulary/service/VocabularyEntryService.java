package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.mapper.VocabularyEntryMapper;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.booster.vocabulary.util.StringUtil.randomUuid;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyEntryService {

    private static final String DEFAULT_VOCABULARY_NAME = "DEFAULT_VOCABULARY";

    private final WordService wordService;
    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;

    private final VocabularyEntryMapper vocabularyEntryMapper;

    @Transactional
    public VocabularyEntryDto create(VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        String userId = vocabularyEntryRequestDto.getUserId();
        String word = vocabularyEntryRequestDto.getWord();

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);

        if (vocabularyEntryRepository.existsByUserIdAndTargetWordName(userId, word)) {
            throw new RuntimeException();
        }
        VocabularyEntity vocabularyEntity = ofNullable(vocabularyEntryRequestDto.getVocabularyId())
                .map(vocabularyRepository::findById)
                .orElseGet(() -> vocabularyRepository.findByUserIdAndName(userId, DEFAULT_VOCABULARY_NAME))
                .orElseThrow(RuntimeException::new);

        var vocabularyEntryEntity = new VocabularyEntryEntity();
        vocabularyEntryEntity.setId(randomUuid());
        vocabularyEntryEntity.setUser(userEntity);

        WordEntity wordEntity = wordService.getWordEntityByNameOrCreateAndSave(word);
        vocabularyEntryEntity.setTargetWord(wordEntity);

        ofNullable(vocabularyEntryRequestDto.getAntonyms()).ifPresent(
                antonyms -> antonyms.forEach(
                        antonym -> {
                            WordEntity antonymWordEntity = wordService.getWordEntityByNameOrCreateAndSave(antonym);
                            vocabularyEntryEntity.getAntonyms().add(antonymWordEntity);
                        }
                )
        );
        ofNullable(vocabularyEntryRequestDto.getSynonyms()).ifPresent(
                synonyms -> synonyms.forEach(
                        synonym -> {
                            WordEntity synonymWordEntity = wordService.getWordEntityByNameOrCreateAndSave(synonym);
                            vocabularyEntryEntity.getSynonyms().add(synonymWordEntity);
                        }
                )
        );
        vocabularyEntryEntity.setVocabulary(vocabularyEntity);
        vocabularyEntryRepository.save(vocabularyEntryEntity);

        vocabularyEntity.getVocabularyEntries().add(vocabularyEntryEntity);
        vocabularyRepository.save(vocabularyEntity);

        return vocabularyEntryMapper.entity2Dto(vocabularyEntryEntity);
    }

    public VocabularyEntryDto findById(String id) {
        return vocabularyEntryRepository.findById(id)
                .map(vocabularyEntryMapper::entity2Dto)
                .orElseThrow(RuntimeException::new);
    }

    public List<VocabularyEntryDto> findAllByUserIdAndVocabularyId(String userId, String vocabularyId) {
        return vocabularyEntryRepository.findAllByUserIdAndVocabularyId(userId, vocabularyId)
                .stream()
                .map(vocabularyEntryMapper::entity2Dto)
                .collect(toList());
    }

    @Transactional
    public void deleteById(String id) {
        VocabularyEntryEntity vocabularyEntryEntity = vocabularyEntryRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        VocabularyEntity vocabularyEntity = vocabularyEntryEntity.getVocabulary();
        vocabularyEntity.getVocabularyEntries()
                .removeIf(e -> vocabularyEntryEntity.getId().equals(e.getId()));
        vocabularyRepository.save(vocabularyEntity);

        vocabularyEntryRepository.delete(vocabularyEntryEntity);
    }

}
