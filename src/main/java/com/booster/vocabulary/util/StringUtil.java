package com.booster.vocabulary.util;

import java.util.UUID;

public interface StringUtil {

    static String randomUuid() {
        return UUID.randomUUID().toString();
    }

}
