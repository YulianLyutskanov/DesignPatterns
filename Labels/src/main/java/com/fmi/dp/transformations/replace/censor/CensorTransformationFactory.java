package com.fmi.dp.transformations.replace.censor;

import java.util.HashMap;
import java.util.Map;

//Flightweight
//java stringpool ?
public class CensorTransformationFactory {
    private static final Map<String, CensorTransformation> CACHE_MAP = new HashMap<>();

    private static final int CACHE_LENGTH = 4;

    public static CensorTransformation createCensorTransformation(String censorText) {
        if (censorText.length() <= CACHE_LENGTH) {
            return CACHE_MAP.putIfAbsent(censorText, new CensorTransformation(censorText));
        } else {
            return new CensorTransformation(censorText);
        }
    }
}
