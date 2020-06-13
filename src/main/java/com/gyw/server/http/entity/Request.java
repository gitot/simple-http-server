package com.gyw.server.http.entity;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Request {
    private String method;
    private String url;

    private ChannelHandlerContext ctx;
    private HttpRequest req;

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

    public Request(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public String getMethod() {
        return req.method().name();
    }

    public String getUrl() {
        return req.getUri();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        return decoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        List<String> params = parameters.get(name);
        if (null == params) {
            return null;
        } else {
            return params.get(0);
        }
    }
}
