package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.VocabularySetEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntryEntityByIdNotFoundException;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import com.booster.vocabulary.repository.VocabularySetRepository;
import com.booster.vocabulary.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyEntryService {

    private final VocabularySetRepository vocabularySetRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long add(VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        UserEntity userEntity = userRepository.findById(vocabularyEntryRequestDto.getUserId())
                .orElseThrow(() -> new UserEntityByIdNotFoundException(vocabularyEntryRequestDto.getUserId()));

        var vocabularyEntryEntity = new VocabularyEntryEntity();
        vocabularyEntryEntity.setUser(userEntity);

        Optional<WordEntity> optionalWord = wordRepository.findByWord(vocabularyEntryRequestDto.getWord());

        if (optionalWord.isPresent()) {
            vocabularyEntryEntity.setTargetWord(optionalWord.get());
        } else {
            var wordEntity = new WordEntity();
            wordEntity.setWord(vocabularyEntryRequestDto.getWord());
            wordRepository.save(wordEntity);

            vocabularyEntryEntity.setTargetWord(wordEntity);
        }
        ofNullable(vocabularyEntryRequestDto.getAntonyms()).ifPresent(
                antonyms -> antonyms.forEach(
                        antonym -> {
                            Optional<WordEntity> optionalAntonym = wordRepository.findByWord(antonym);
                            if (optionalAntonym.isPresent()) {
                                vocabularyEntryEntity.getAntonyms().add(optionalAntonym.get());
                            } else {
                                var antonymEntity = new WordEntity();
                                antonymEntity.setWord(antonym);
                                wordRepository.save(antonymEntity);

                                vocabularyEntryEntity.getAntonyms().add(antonymEntity);
                            }
                        }
                )
        );
        ofNullable(vocabularyEntryRequestDto.getSynonyms()).ifPresent(
                synonyms -> synonyms.forEach(
                        synonym -> {
                            Optional<WordEntity> optionalSynonym = wordRepository.findByWord(synonym);
                            if (optionalSynonym.isPresent()) {
                                vocabularyEntryEntity.getSynonyms().add(optionalSynonym.get());
                            } else {
                                var synonymEntity = new WordEntity();
                                synonymEntity.setWord(synonym);
                                wordRepository.save(synonymEntity);

                                vocabularyEntryEntity.getSynonyms().add(synonymEntity);
                            }
                        }
                )
        );
//        vocabularyEntryRepository.save(vocabularyEntryEntity);

        var vocabularyName = ofNullable(vocabularyEntryRequestDto.getVocabularyName())
                .orElse("Default");

        VocabularyEntity vocabularyEntity = vocabularyRepository.findByUserIdAndName(vocabularyEntryRequestDto.getUserId(), vocabularyName)
                .orElseGet(() -> {
                    var newVocabularyEntity = new VocabularyEntity();
                    newVocabularyEntity.setName(vocabularyName);
                    newVocabularyEntity.setUser(userEntity);
                    return newVocabularyEntity;
                });
        vocabularyEntity.getVocabularyEntries().add(vocabularyEntryEntity);
        vocabularyRepository.save(vocabularyEntity);

        vocabularyEntryEntity.setVocabulary(vocabularyEntity);
        vocabularyEntryRepository.save(vocabularyEntryEntity);

        Optional<VocabularySetEntity> optionalVocabularySetEntity = vocabularySetRepository.findByLanguageNameAndUserId(
                vocabularyEntryRequestDto.getLanguageName(), userEntity.getId()
        );
        if (optionalVocabularySetEntity.isEmpty()) {
            var vocabularySetEntity = new VocabularySetEntity();
            vocabularySetEntity.getVocabularies().add(vocabularyEntity);
            vocabularySetEntity.setLanguageName(vocabularyEntryRequestDto.getLanguageName());
            vocabularySetEntity.setUser(userEntity);
            vocabularySetRepository.save(vocabularySetEntity);
        }
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
