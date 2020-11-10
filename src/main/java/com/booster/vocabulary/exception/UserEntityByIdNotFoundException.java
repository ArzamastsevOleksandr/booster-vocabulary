package com.booster.vocabulary.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.EntityNotFoundException;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserEntityByIdNotFoundException extends EntityNotFoundException {
    Long id;
}
