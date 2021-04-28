package com.testingAPI.backend.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authentication {
    public static interface Method{
        String authorize(String login,String pass);
    }

    static Map<String,Method> authMethods = new HashMap<>();

    static public String getToken(String authName, String login, String pass){
        return authMethods.get(authName).authorize(login,pass);
    }

    static public void addAuth(String name, Method method){
        authMethods.put(name,method);
    }
}
