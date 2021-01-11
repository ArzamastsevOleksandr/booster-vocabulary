package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class BaseLanguageEntityByIdNotFoundException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = BaseLanguageEntity.class.getName() + " not found for id: %s";

    Long id;

    public BaseLanguageEntityByIdNotFoundException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
