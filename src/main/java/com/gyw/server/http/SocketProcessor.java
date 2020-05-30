package com.gyw.server.http;

import com.gyw.server.http.parser.HttpRequestParser;
import com.gyw.server.http.parser.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable {
    private Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }

    private Logger logger = LoggerFactory.getLogger(SocketProcessor.class);


    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            HttpRequestParser parser = new HttpRequestParser(inputStream);
            HttpRequest request = parser.parse();
            logger.info(request.getRemoteIp() + " visit");
        } catch (IOException e) {
            //return default error message
        }

    }
}
