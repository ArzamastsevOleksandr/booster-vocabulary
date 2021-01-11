package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.LanguageToLearnEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageToLearnEntityByLanguageIdNotFoundException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = LanguageToLearnEntity.class.getName() + " not found by language id: %s";

    Long id;

    public LanguageToLearnEntityByLanguageIdNotFoundException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
