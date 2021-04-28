package com.testingAPI.backend.baseTesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testingAPI.backend.http.HttpExecutor;
import com.testingAPI.backend.model.HttpMethod;
import com.testingAPI.backend.model.Request;
import com.testingAPI.backend.model.Response;
import com.testingAPI.backend.utils.JsonUtils;
import com.testingAPI.backend.utils.PropertiesGetter;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseStepDefinitions {
    HttpExecutor httpExecutor = new HttpExecutor();
    protected PropertiesGetter propertiesGetter;
    protected ObjectMapper mapper = new ObjectMapper();
    protected Logger logger;
    protected Map<String,Response> responses = new HashMap<String,Response>();
    protected Response responseActual;
    protected Response responseExpected;
    private final String separator = "<|>";

    protected BaseStepDefinitions(PropertiesGetter propertiesGetter,Logger logger){
        this.propertiesGetter = propertiesGetter;
        this.logger = logger;
    }

    protected Response getSavedResponse(HttpMethod method,String url){
        return responses.get(method + separator + url);
    }

    protected Response execute(Request request) throws IOException {
        if(!request.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)){
            request.addHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        }
        if(request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            logger.info("Auth : " + request.getHeaders().get(HttpHeaders.AUTHORIZATION));
        }

        logger.info("Requesting : " + request.getMethod().getStringValue() + " - " + request.getUrl());
        if (JsonUtils.checkJsonCompatibility(request.getBody(),Object.class)){
            if(!request.getBody().isEmpty())
                logger.info("Request body : \n" + convertToPrettyJson(request.getBody()));
        }

        Response response = httpExecutor.execute(request);
        responses.put(request.getMethod().getStringValue() + separator + request.getUrl(),response);
        logger.info("Response code : " + response.getStatusCode());
        if(!response.getBody().isEmpty())
            logger.info("Response body : " + convertToPrettyJson(response.getBody()));

        for(Map.Entry<String,String> entry : response.getHeaders().entrySet()){
            logger.info("Header : " + entry.getKey() + ", value : " + entry.getValue());
        }

        return response;
    }

    protected String convertToPrettyJson(String json) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString( mapper.readValue(json, Object.class));
    }

    protected <T> T convertToClass(String url,Class<T> cl) throws JsonProcessingException {
        return mapper.readValue(url,cl);
    }
}
