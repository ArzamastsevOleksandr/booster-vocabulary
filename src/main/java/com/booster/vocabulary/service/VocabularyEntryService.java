package com.booster.vocabulary.service;

import com.booster.vocabulary.controller.VocabularyEntryController;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import com.booster.vocabulary.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyEntryService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long add(VocabularyEntryController.VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        UserEntity userEntity = userRepository.findById(vocabularyEntryRequestDto.getUserId())
                .orElseThrow(() -> new UserEntityByIdNotFoundException(vocabularyEntryRequestDto.getUserId()));

        VocabularyEntryEntity vocabularyEntryEntity = new VocabularyEntryEntity();
        Optional<WordEntity> optionalWord = wordRepository.findByWord(vocabularyEntryRequestDto.getWord());

        if (optionalWord.isPresent()) {
            vocabularyEntryEntity.setTargetWord(optionalWord.get());
        } else {
            WordEntity wordEntity = new WordEntity();
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
                                WordEntity antonymEntity = new WordEntity();
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
                                WordEntity synonymEntity = new WordEntity();
                                synonymEntity.setWord(synonym);
                                wordRepository.save(synonymEntity);

                                vocabularyEntryEntity.getSynonyms().add(synonymEntity);
                            }
                        }
                )
        );
        vocabularyEntryRepository.save(vocabularyEntryEntity);

        String vocabularyName = ofNullable(vocabularyEntryRequestDto.getVocabularyName())
                .orElse("Default");

        VocabularyEntity vocabularyEntity = vocabularyRepository.findByUserIdAndName(vocabularyEntryRequestDto.getUserId(), vocabularyName)
                .orElseGet(() -> {
                    VocabularyEntity newVocabularyEntity = new VocabularyEntity();
                    newVocabularyEntity.setName(vocabularyName);
                    newVocabularyEntity.setUser(userEntity);
                    return newVocabularyEntity;
                });
        vocabularyEntity.getVocabularyEntries().add(vocabularyEntryEntity);
        vocabularyRepository.save(vocabularyEntity);

        return vocabularyEntryEntity.getId();
    }

}
