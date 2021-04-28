package com.testingAPI.backend.model;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {
    int statusCode;
    byte[] body;
    Map<String,String> headers;

    public String getBody() { return new String(body); }
    public  byte[] getBodyAsBytes() { return Arrays.copyOf(body,body.length); }

    public int getStatusCode() {
        return statusCode;
    }

    public Response(int statusCode, byte[] body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
