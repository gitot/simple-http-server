package com.gyw.server.http.parser;

import java.io.InputStream;

/**
 * Parse binary InputStream to HttpMessage
 */
public class HttpRequestParser {

    private InputStream inputStream;

    public HttpRequestParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HttpRequest parse() {
        //TODO
        return new HttpRequest();
    }
}
