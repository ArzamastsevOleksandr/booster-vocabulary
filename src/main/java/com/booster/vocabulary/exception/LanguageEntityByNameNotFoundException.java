package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.LanguageEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class LanguageEntityByNameNotFoundException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = LanguageEntity.class.getName() + " not found by name: %s";

    String name;

    public LanguageEntityByNameNotFoundException(String name) {
        super(format(ERROR_MESSAGE_TEMPLATE, name));
        this.name = name;
    }

}
