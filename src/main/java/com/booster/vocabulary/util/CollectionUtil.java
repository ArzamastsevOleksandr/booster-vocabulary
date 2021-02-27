package com.booster.vocabulary.util;

import java.util.ArrayList;
import java.util.List;

public interface CollectionUtil {

    static <E> ArrayList<E> arrayListOf(E...args) {
        return new ArrayList<>(List.of(args));
    }

}
