package com.test.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.example.model.*;
import com.testingAPI.backend.baseTesting.BaseStepDefinitions;
import com.testingAPI.backend.model.HttpMethod;
import com.testingAPI.backend.model.Request;
import com.testingAPI.backend.model.Response;
import com.testingAPI.backend.utils.Authentication;
import com.testingAPI.backend.utils.PropertiesGetter;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;

public class StepDefinitionsExample extends BaseStepDefinitions {
    String serviceURL;
    {
        Authentication.addAuth("exampleAuth", (login,pass)->{
            try {
                return convertToClass(
                        execute(new Request.Builder(serviceURL + "/login",HttpMethod.POST)
                        .setBody(new LoginRequest(login,pass))
                        .build()).getBody(),
                        LoginResponse.class).getToken();
            } catch (IOException e) {
                e.printStackTrace();
                return "noAuth";
            }
        });
    }

    public StepDefinitionsExample() {
        super(  new PropertiesGetter("/src/test/resources/exampleService/example.properties"),
                Logger.getLogger(StepDefinitionsExample.class)
                );
        serviceURL = propertiesGetter.getProperty("URL");
    }


    @When("^Login with login:(.*) pass:(.*)$")
    public void visit(String login,String pass) throws IOException {
        responseActual = execute(new Request.Builder(serviceURL + "/login", HttpMethod.POST)
                .setBody(new LoginRequest(login,pass))
                .addHeader(HttpHeaders.CONTENT_TYPE,"application/json")
                .build()
        );
    }


    @When("^Create project name (.*), descr (.*), targetSum (.*)$")
    public void createProject(String projectName,String descr,Double targetSum) throws IOException {
        responseActual = execute(new Request.Builder(serviceURL + "/projects",HttpMethod.POST)
                .setBody(new Project(projectName,descr,targetSum))
                .addHeader(HttpHeaders.CONTENT_TYPE,"application/json")
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + Authentication.getToken("exampleAuth","login","pass"))
                .build()
        );
    }
    @When("Delete project with name: (.*)$")
    public void deleteProjectWithNameName(String name) throws IOException {
        responseActual = execute(new Request.Builder(serviceURL + "/projects/" + name,HttpMethod.DELETE)
                .addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + Authentication.getToken("exampleAuth","login","pass"))
                .build());
    }
    @When("^Get project with name: (.*)$")
    public void getProject(String name) throws IOException {
        responseActual = execute(new Request.Builder(serviceURL + "/projects/" + name, HttpMethod.GET)
                .addHeader(HttpHeaders.CONTENT_TYPE,"application/json")
                .addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + Authentication.getToken("exampleAuth","login","pass"))
                .build()
        );
    }

    @Then("^Expected to contain:(.*)$")
    public void testContent(String content) throws JsonProcessingException {
        Assert.assertTrue(responseActual.getBody().contains(content));
    }
    @Then("^Expected response code:(.*)$")
    public void testContent(int code) throws JsonProcessingException {
        Assert.assertEquals(code,responseActual.getStatusCode());
    }



}
