package com.testingAPI.backend.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    static ObjectMapper mapper = new ObjectMapper();

    public static boolean checkJsonCompatibility(String jsonStr, Class<?> valueType) throws JsonParseException, IOException {
        if(jsonStr == null){
            return false;
        }
        try {
            mapper.readValue(jsonStr, valueType);
            return true;
        } catch (JsonParseException | JsonMappingException e) {
            return false;
        }
    }
}
