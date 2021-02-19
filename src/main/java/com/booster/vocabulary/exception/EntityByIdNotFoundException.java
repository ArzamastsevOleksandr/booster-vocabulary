package com.booster.vocabulary.exception;

import lombok.Getter;

@Getter
public class EntityByIdNotFoundException extends RuntimeException {

    private final String id;
    private final String entityName;

    public EntityByIdNotFoundException(String id, String entityName) {
        super(String.format("id: %s, entityName: %s", id, entityName));
        this.id = id;
        this.entityName = entityName;
    }

}
