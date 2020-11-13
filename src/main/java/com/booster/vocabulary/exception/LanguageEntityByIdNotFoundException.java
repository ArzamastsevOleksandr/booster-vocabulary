package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.LanguageEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageEntityByIdNotFoundException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = LanguageEntity.class.getName() + " not found for id: %s";

    Long id;

    public LanguageEntityByIdNotFoundException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
