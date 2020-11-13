package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.VocabularyEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class VocabularyEntityAlreadyExistsWithNameException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = VocabularyEntity.class.getName() + " already exists with name: %s";

    String name;

    public VocabularyEntityAlreadyExistsWithNameException(String name) {
        super(format(ERROR_MESSAGE_TEMPLATE, name));
        this.name = name;
    }

}
