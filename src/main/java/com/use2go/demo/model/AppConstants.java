package com.use2go.demo.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public interface AppConstants {

    String TOPIC_IN = "demo-in";
    String TOPIC_OUT = "demo-out";

    List<String> SOURCES = List.of("UGNB96", "2WN9J2", "Z76QVC", "P286ZN");
    int MAX_DATA_VALUE = 100;

    static String randomSource() {
        return SOURCES.get(RandomUtils.nextInt(0, SOURCES.size()));
    }

    static int randomValue() {
        return RandomUtils.nextInt(0, MAX_DATA_VALUE);
    }

}
