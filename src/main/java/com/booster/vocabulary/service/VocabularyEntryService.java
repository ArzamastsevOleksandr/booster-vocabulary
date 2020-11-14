package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.exception.UserEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntityByIdNotFoundException;
import com.booster.vocabulary.exception.VocabularyEntryEntityAlreadyExistsWithTargetWordException;
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
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyEntryService {

    private static final Function<VocabularyEntryEntity, VocabularyEntryDto> vocabularyEntryEntity2VocabularyEntryDto =
            vee -> VocabularyEntryDto.builder()
                    .id(vee.getId())
                    .targetWord(vee.getTargetWord().getName())
                    .build();

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        Long userId = vocabularyEntryRequestDto.getUserId();
        Long vocabularyId = vocabularyEntryRequestDto.getVocabularyId();
        String word = vocabularyEntryRequestDto.getWord();

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserEntityByIdNotFoundException(userId));

        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyEntityByIdNotFoundException(vocabularyId));

        if (vocabularyEntryRepository.existsByUserIdAndTargetWordName(userId, word)) {
            throw new VocabularyEntryEntityAlreadyExistsWithTargetWordException(word);
        }

        var vocabularyEntryEntity = new VocabularyEntryEntity();
        vocabularyEntryEntity.setUser(userEntity);

        WordEntity wordEntity = getWordEntityByNameOrCreateAndSave(word);
        vocabularyEntryEntity.setTargetWord(wordEntity);

        ofNullable(vocabularyEntryRequestDto.getAntonyms()).ifPresent(
                antonyms -> antonyms.forEach(
                        antonym -> {
                            WordEntity antonymEntity = getWordEntityByNameOrCreateAndSave(antonym);
                            vocabularyEntryEntity.getAntonyms().add(antonymEntity);
                        }
                )
        );
        ofNullable(vocabularyEntryRequestDto.getSynonyms()).ifPresent(
                synonyms -> synonyms.forEach(
                        synonym -> {
                            WordEntity synonymEntity = getWordEntityByNameOrCreateAndSave(synonym);
                            vocabularyEntryEntity.getSynonyms().add(synonymEntity);
                        }
                )
        );
        vocabularyEntryRepository.save(vocabularyEntryEntity);

        // todo: assign vocabulary
        vocabularyEntity.getVocabularyEntries().add(vocabularyEntryEntity);
        vocabularyRepository.save(vocabularyEntity);

        return vocabularyEntryEntity.getId();
    }

    private WordEntity getWordEntityByNameOrCreateAndSave(String word) {
        return wordRepository.findByName(word)
                .orElseGet(() -> {
                    var newWordEntity = new WordEntity();
                    newWordEntity.setName(word);
                    wordRepository.save(newWordEntity);
                    return newWordEntity;
                });
    }

    public VocabularyEntryDto findById(Long vocabularyEntryId) {
        VocabularyEntryEntity vocabularyEntryEntity = vocabularyEntryRepository.findById(vocabularyEntryId)
                .orElseThrow(() -> new VocabularyEntryEntityByIdNotFoundException(vocabularyEntryId));

        return VocabularyEntryDto.builder()
                .id(vocabularyEntryEntity.getId())
                .targetWord(vocabularyEntryEntity.getTargetWord().getName())
                .createdOn(vocabularyEntryEntity.getCreatedOn())
                .correctAnswersCount(vocabularyEntryEntity.getCorrectAnswersCount())
                .synonyms(getWordNames(vocabularyEntryEntity::getSynonyms))
                .antonyms(getWordNames(vocabularyEntryEntity::getAntonyms))
                .build();
    }

    private List<String> getWordNames(Supplier<List<WordEntity>> supplier) {
        return supplier.get()
                .stream()
                .map(WordEntity::getName)
                .collect(toList());
    }

    @Transactional
    public List<VocabularyEntryDto> findAllForUserId(Long userId) {
        return vocabularyEntryRepository.findAllByUserId(userId)
                .stream()
                .map(vocabularyEntryEntity2VocabularyEntryDto)
                .collect(toList());
    }

}
