package com.study.systems.microservices;

import java.util.*;
import java.util.List;
import java.util.Optional;

public class HttpRequest {

    private final String method;  // GET, POST, PUT, DELETE, ...
    private final String path;    // e.g. "/orders/123"
    private final String body;

    public HttpRequest(String method, String path, String body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getBody() { return body; }
}
