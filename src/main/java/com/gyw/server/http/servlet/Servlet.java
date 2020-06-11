package com.gyw.server.http.servlet;

import com.gyw.server.http.entity.Request;
import com.gyw.server.http.entity.Response;

public abstract class Servlet {
    public void service(Request request, Response response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            doPost(request, response);
        } else {
            //do nothing
        }
    }

    public abstract void doGet(Request request, Response response) throws Exception;

    public abstract void doPost(Request request, Response response) throws Exception;
}
