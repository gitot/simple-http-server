package com.gyw.server.http;

import com.gyw.server.http.exceutor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Bootstrap {

    private static Exception exception;

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private static Executor executor = Executor.INSTANCE;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(80);
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(() -> {
                    while (true) {
                        InputStream inputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String s;
                        try {
                            while (((s = reader.readLine()) != null)) {
                                System.out.println(s);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        } catch (IOException e) {
            exception = e;
        }
        logger.error("error msg: ", exception);
    }
}
