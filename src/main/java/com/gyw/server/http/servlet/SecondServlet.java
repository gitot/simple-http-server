package com.gyw.server.http.servlet;

import com.gyw.server.http.entity.Request;
import com.gyw.server.http.entity.Response;

public class SecondServlet extends Servlet {
    public void doGet(Request request, Response response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(Request request, Response response) throws Exception {
        response.write("This is second servlet");
    }
}
