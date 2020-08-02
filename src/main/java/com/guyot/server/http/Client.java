package com.guyot.server.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {

    private NioEventLoopGroup eventLoopGroup;

    public Client() {
        eventLoopGroup = new NioEventLoopGroup(1);
    }

    public void start() {
        //如何在启动时发送消息给服务器？
        Bootstrap client = new Bootstrap();
        client.group(eventLoopGroup);
        client.channel(NioSocketChannel.class);
        client.handler(new EchoClientChannelHandler());
        ChannelFuture future = null;
        try {
            future = client.connect(new InetSocketAddress("localhost", 7000)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}
