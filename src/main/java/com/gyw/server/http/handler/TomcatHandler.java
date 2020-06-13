package com.gyw.server.http.handler;

import com.gyw.server.http.entity.Request;
import com.gyw.server.http.entity.Response;
import com.gyw.server.http.servlet.Servlet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashMap;
import java.util.Map;

public class TomcatHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Servlet> urlMapping = new HashMap<>();

    public TomcatHandler(Map<String, Servlet> urlMapping) {
        this.urlMapping = urlMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpMessage) {
            System.out.println("hello");
            HttpRequest req = (HttpRequest) msg;

            Request request = new Request(ctx, req);
            Response response = new Response(ctx, req);
            String url = request.getUrl();
            if (urlMapping.containsKey(url)) {
                urlMapping.get(url).service(request, response);
            } else {
                response.write("404 Not Found");
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
    }
}
