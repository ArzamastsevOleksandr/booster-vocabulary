package com.booster.vocabulary.exception;

import com.booster.vocabulary.entity.VocabularyEntity;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Value
@EqualsAndHashCode(callSuper = true)
public class VocabularyEntityByIdNotFoundException extends EntityNotFoundException {

    private static final String ERROR_MESSAGE_TEMPLATE = VocabularyEntity.class.getName() + " not found for id: %s";

    Long id;

    public VocabularyEntityByIdNotFoundException(Long id) {
        super(format(ERROR_MESSAGE_TEMPLATE, id));
        this.id = id;
    }

}
