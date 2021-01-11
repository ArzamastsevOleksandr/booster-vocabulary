package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.LanguageToLearnEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageToLearnEntityAlreadyExistsException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = LanguageToLearnEntity.class.getName() + " already exists with id: %s";

    Long id;

    public LanguageToLearnEntityAlreadyExistsException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
