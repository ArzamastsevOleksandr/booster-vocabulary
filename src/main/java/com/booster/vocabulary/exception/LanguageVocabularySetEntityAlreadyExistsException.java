package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageVocabularySetEntityAlreadyExistsException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = LanguageVocabularySetEntity.class.getName() + " already exists with id: %s";

    Long id;

    public LanguageVocabularySetEntityAlreadyExistsException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
