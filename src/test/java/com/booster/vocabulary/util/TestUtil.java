package com.booster.vocabulary.util;

import java.util.UUID;

public interface TestUtil {

    static String randomPassword() {
        return UUID.randomUUID().toString();
    }

    static String randomUsername() {
        return UUID.randomUUID().toString();
    }

    static String randomEmail() {
        return UUID.randomUUID().toString() + "@gmail.com";
    }

}
