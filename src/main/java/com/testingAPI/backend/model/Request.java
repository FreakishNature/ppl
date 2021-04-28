package com.testingAPI.backend.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Request {
    static ObjectMapper mapper = new ObjectMapper();
    Map<String,String> headers = new HashMap<>();
    String body;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    HttpMethod method;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    private void setBody(String body){
        this.body = body;
    }

    public void setBody(Object jsonBody) {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            this.body = mapper.writeValueAsString(jsonBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void removeHeader(String key){
        headers.remove(key);
    }

    public void addHeader(String key,String value){
        headers.put(key,value);
    }

    public Request(String url, HttpMethod method){
        this.url = url;
        this.method = method;
    }

    public static class Builder {
        Request request;

        public Builder setBody(Object body) throws JsonProcessingException {
            request.setBody(body);
            return this;
        }

        public void setBody(String body){
            request.setBody(body);
        }

        public Builder addHeader(String key,String value){
            request.addHeader(key,value);
            return this;
        }

        public void remove(String key) {
            request.removeHeader(key);
        }

        public Request build(){
            return request;
        }
        public Builder(String url, HttpMethod method){
            request = new Request(url,method);
        }
    }
}
