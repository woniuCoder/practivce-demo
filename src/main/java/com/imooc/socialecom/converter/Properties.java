package com.imooc.socialecom.converter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Properties {

    private static Map<String, String> map = null;

    public Properties() {
        map = new HashMap<>();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }
}
