package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntryEntityAlreadyExistsByTargetWordException;
import com.booster.vocabulary.exception.VocabularyEntryEntityByIdNotFoundException;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import com.booster.vocabulary.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyEntryService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        UserEntity userEntity = userRepository.findById(vocabularyEntryRequestDto.getUserId())
                .orElseThrow(() -> new UserEntityByIdNotFoundException(vocabularyEntryRequestDto.getUserId()));

        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(vocabularyEntryRequestDto.getVocabularyId())
                .orElseThrow(() -> new VocabularyEntityByIdNotFoundException(vocabularyEntryRequestDto.getVocabularyId()));

        if (vocabularyEntryRepository.existsByUserIdAndTargetWordWord(
                vocabularyEntryRequestDto.getUserId(),
                vocabularyEntryRequestDto.getWord()
        )) {
            throw new VocabularyEntryEntityAlreadyExistsByTargetWordException(vocabularyEntryRequestDto.getWord());
        }

        var vocabularyEntryEntity = new VocabularyEntryEntity();
        vocabularyEntryEntity.setUser(userEntity);

        WordEntity wordEntity = wordRepository.findByWord(vocabularyEntryRequestDto.getWord())
                .orElseGet(() -> {
                    var newWordEntity = new WordEntity();
                    newWordEntity.setWord(vocabularyEntryRequestDto.getWord());
                    wordRepository.save(newWordEntity);
                    return newWordEntity;
                });
        vocabularyEntryEntity.setTargetWord(wordEntity);

        ofNullable(vocabularyEntryRequestDto.getAntonyms()).ifPresent(
                antonyms -> antonyms.forEach(
                        antonym -> {
                            WordEntity antonymEntity = wordRepository.findByWord(antonym)
                                    .orElseGet(() -> {
                                        var newWordEntity = new WordEntity();
                                        newWordEntity.setWord(antonym);
                                        wordRepository.save(newWordEntity);
                                        return newWordEntity;
                                    });
                            vocabularyEntryEntity.getAntonyms().add(antonymEntity);
                        }
                )
        );
        ofNullable(vocabularyEntryRequestDto.getSynonyms()).ifPresent(
                synonyms -> synonyms.forEach(
                        synonym -> {
                            WordEntity synonymEntity = wordRepository.findByWord(synonym)
                                    .orElseGet(() -> {
                                        var newWordEntity = new WordEntity();
                                        newWordEntity.setWord(synonym);
                                        wordRepository.save(newWordEntity);
                                        return newWordEntity;
                                    });
                            vocabularyEntryEntity.getSynonyms().add(synonymEntity);
                        }
                )
        );
        vocabularyEntryRepository.save(vocabularyEntryEntity);

        vocabularyEntity.getVocabularyEntries().add(vocabularyEntryEntity);
        vocabularyRepository.save(vocabularyEntity);

        return vocabularyEntryEntity.getId();
    }

    public VocabularyEntryDto findById(Long vocabularyEntryId) {
        VocabularyEntryEntity vocabularyEntryEntity = vocabularyEntryRepository.findById(vocabularyEntryId)
                .orElseThrow(() -> new VocabularyEntryEntityByIdNotFoundException(vocabularyEntryId));

        List<String> synonyms = vocabularyEntryEntity.getSynonyms().stream()
                .map(WordEntity::getWord)
                .collect(toList());
        List<String> antonyms = vocabularyEntryEntity.getAntonyms().stream()
                .map(WordEntity::getWord)
                .collect(toList());
        return VocabularyEntryDto.builder()
                .id(vocabularyEntryEntity.getId())
                .targetWord(vocabularyEntryEntity.getTargetWord().getWord())
                .createdOn(vocabularyEntryEntity.getCreatedOn())
                .correctAnswersCount(vocabularyEntryEntity.getCorrectAnswersCount())
                .synonyms(synonyms)
                .antonyms(antonyms)
                .build();
    }

    @Transactional
    public List<VocabularyEntryDto> findAllForUserId(Long userId) {
        return vocabularyEntryRepository.findAllByUserId(userId).stream()
                .map(vocabularyEntryEntity -> {
                    List<String> synonyms = vocabularyEntryEntity.getSynonyms().stream()
                            .map(WordEntity::getWord)
                            .collect(toList());
                    List<String> antonyms = vocabularyEntryEntity.getAntonyms().stream()
                            .map(WordEntity::getWord)
                            .collect(toList());

                    return VocabularyEntryDto.builder()
                            .id(vocabularyEntryEntity.getId())
                            .targetWord(vocabularyEntryEntity.getTargetWord().getWord())
                            .createdOn(vocabularyEntryEntity.getCreatedOn())
                            .correctAnswersCount(vocabularyEntryEntity.getCorrectAnswersCount())
                            .synonyms(synonyms)
                            .antonyms(antonyms)
                            .build();
                }).collect(toList());
    }

}
