package com.gyw.server.http;

import com.gyw.server.http.entity.Request;
import com.gyw.server.http.entity.Response;
import com.gyw.server.http.servlet.Servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


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


    public void start() {
        init();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server start on port :" + port);
            ExecutorService executorService = new ThreadPoolExecutor(
                    1,
                    1,
                    1000,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            while (true) {
                executorService.execute(() -> {
                    try (Socket socket = serverSocket.accept()) {
                        process(socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void process(Socket socket) throws Exception {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        Request request = new Request(in);
        Response response = new Response(out);

        String url = request.getUrl();
        Servlet servlet = urlMapping.get(url);


        if (urlMapping.containsKey(url)) {
            servlet.service(request, response);
        } else {
            response.write("404 Not Found");
        }

        out.flush();
        out.close();

        in.close();
        socket.close();
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start();
    }
}
