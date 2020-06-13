package com.gyw.server.http;

import com.gyw.server.http.entity.Request;
import com.gyw.server.http.entity.Response;
import com.gyw.server.http.handler.TomcatHandler;
import com.gyw.server.http.servlet.Servlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Put logic in corresponding method ,state in field instead of everything in a
 * single "main" method.This can "divide and conquer" the complex problem into
 * some smaller problem,causing the codes readable.
 */
public class Bootstrap {


    private int port = 8080;
    private ServerSocket serverSocket;
    private Map<String, Servlet> urlMapping = new HashMap<>();

    private Properties properties = new Properties();


    public void init() {
        String path = this.getClass().getResource("/").getPath();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path + "web.properties");
            properties.load(fileInputStream);

            for (Object key : properties.keySet()) {
                String name = (String) key;
                if (name.endsWith("url")) {
                    String servletName = name.replaceAll("\\.url", "");
                    String url = properties.getProperty(name);
                    Class<?> clz = Class.forName(properties.getProperty(servletName + ".className"));
                    Servlet servlet = (Servlet) clz.getDeclaredConstructor().newInstance();
                    urlMapping.put(url, servlet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void start()  {
        init();

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {

                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new HttpResponseEncoder())
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new TomcatHandler(urlMapping));
                        }
                    })
                    .childOption(ChannelOption.SO_BACKLOG, 128);


            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("netty started on port: " + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }


    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start();
    }
}
