package com.gyw.server.http.parser;

import java.util.*;

public class MultiValueMap {
    private Map<String, List<String>> headers = new HashMap<>();

    public void addHeader(String key, String value) {
        List<String> objects = headers.get(key);
        if (Objects.nonNull(objects)) {
            objects.add(value);
            return;
        }
        objects = Collections.emptyList();
        objects.add(value);
        headers.put(key, objects);
    }

    public void removeHeader() {

    }

    public List<String> getHeader(String key) {
        return headers.get(key);
    }

    public boolean hasHeader() {
        return false;
    }
}
