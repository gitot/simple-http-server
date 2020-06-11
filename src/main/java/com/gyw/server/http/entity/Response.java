package com.gyw.server.http.entity;

import java.io.IOException;
import java.io.OutputStream;

public class Response {


    private OutputStream outputStream;


    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void write(String msg) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(msg);
        outputStream.write(sb.toString().getBytes());
    }
}
