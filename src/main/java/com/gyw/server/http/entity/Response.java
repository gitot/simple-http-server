package com.gyw.server.http.entity;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Response {


    private ChannelHandlerContext ctx;
    private HttpMessage req;

    private OutputStream outputStream;


    public Response(ChannelHandlerContext ctx, HttpMessage req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String out) throws IOException {
        try {

            if (out == null || out.length() <= 0) {
                return;
            }
            DefaultHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8"))
            );


            response.headers().set("Content-Type", "text/html");
            ctx.write(response);
        }finally {
            ctx.flush();
            ctx.close();
        }
    }
}
