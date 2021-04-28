package com.testingAPI.backend.utils;

import com.testingAPI.backend.http.HttpExecutor;
import com.testingAPI.backend.model.Request;
import com.testingAPI.backend.model.Response;

import java.io.IOException;
import java.util.Map;

public class HttpThread extends Thread{
    Map<String, Response> responses;
    Map.Entry<String, Request> requestEntry;
    HttpThread(Map<String,Response> responses,Map.Entry<String,Request> requestEntry){
        this.responses = responses;
        this.requestEntry = requestEntry;
    }

    @Override
    public void run() {
        HttpExecutor executor = new HttpExecutor();
        try {
            responses.put(requestEntry.getKey(), executor.execute(requestEntry.getValue()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}