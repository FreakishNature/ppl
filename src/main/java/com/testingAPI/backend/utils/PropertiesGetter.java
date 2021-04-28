package com.testingAPI.backend.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesGetter {
    private Properties properties;
    Map<String,String> propertiesMap = new HashMap<>();
    String src;
    public PropertiesGetter(String src){ this.src = src; }

    public String getProperty(String property){
        if(properties == null){
            properties = new Properties();
            try{
                properties.load(new FileInputStream(System.getProperty("user.dir") + src));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Pattern p = Pattern.compile("\\$\\{a-zA-Z}{1,}");

        for(String prop: properties.stringPropertyNames()){
            propertiesMap.put(prop, properties.getProperty(prop));
        }

        for(String key: propertiesMap.keySet()){
            while (propertiesMap.get(key).contains("${")){
                Matcher m = p.matcher(propertiesMap.get(key));
                String result = propertiesMap.get(key);
                while (m.find()){
                    String variable = m.group().replaceAll("\\$\\{|}","");
                    result = result.replace("${" + variable + "}","");
                }
                propertiesMap.put(key,result);
            }
        }
        return propertiesMap.get(property);
    }
}
