package com.testingAPI.backend.model;

public enum HttpMethod {
    POST("POST"), GET("GET"), PUT("PUT"), DELETE("DELETE"), PATCH("PATCH");

    private String method;
    private HttpMethod(String method){
        this.method = method;
    }
    public String getStringValue(){ return method; }
}
