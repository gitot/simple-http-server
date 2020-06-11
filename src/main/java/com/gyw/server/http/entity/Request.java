package com.gyw.server.http.entity;


import java.io.IOException;
import java.io.InputStream;

public class Request {
    private String method;
    private String url;


    public Request(InputStream in) {
        String content = "";

        byte[] buf = new byte[1024];
        int len = 0;
        try {
            if ((len = in.read(buf)) > 0) {
                content = new String(buf, 0, len);
            }

            String line = content.split("\\n")[0];

            method = line.split("\\s")[0];
            url = line.split("\\s")[1].split("\\?")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
