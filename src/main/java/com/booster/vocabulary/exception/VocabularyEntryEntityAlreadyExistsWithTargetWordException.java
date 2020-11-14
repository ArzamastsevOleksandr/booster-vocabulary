package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.VocabularyEntryEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class VocabularyEntryEntityAlreadyExistsWithTargetWordException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = VocabularyEntryEntity.class.getName() + " already exists by target word: %s";

    String word;

    public VocabularyEntryEntityAlreadyExistsWithTargetWordException(String word) {
        super(format(ERROR_MESSAGE_TEMPLATE, word));
        this.word = word;
    }

}
