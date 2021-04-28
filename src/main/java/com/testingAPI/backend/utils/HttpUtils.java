package com.testingAPI.backend.utils;

import com.testingAPI.backend.http.HttpExecutor;
import com.testingAPI.backend.model.Request;
import com.testingAPI.backend.model.Response;
import io.cucumber.java.bs.A;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpUtils {
    public static void executeRequestsAtTheSameTime(Map<String,Request> requests){
        Map<String, Response> responses = new ConcurrentHashMap<>();
        List<HttpThread> httpThreads = new ArrayList<>();
        for (Map.Entry<String,Request> requestEntry: requests.entrySet()){
            httpThreads.add(new HttpThread(responses,requestEntry));
        }
        httpThreads.forEach(HttpThread::start);
        httpWait();
    }

    private static void httpWait(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
