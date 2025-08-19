package tests.api;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import ai.GenAIClientAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.BaseAPITest;

public class AuthTests extends BaseAPITest{
	

    @Test
    public void loginWithGenAIUser() throws IOException {
        // Get user data from Gemini
        Map<String, String> userData = GenAIClientAPI.generateUserData();
        System.out.println("Generated User: " + userData);

        // Use static user for fakestore login test
        Response response = requestSpec
                .body("{ \"username\": \"mor_2314\", \"password\": \"83r5^_\" }")
                .when()
                .post("/auth/login");  

        response.then().statusCode(201);
        String token = response.jsonPath().getString("token");
        System.out.println("Login Token: " + token);

        Assert.assertNotNull(token, "Token should not be null");
    }

}
