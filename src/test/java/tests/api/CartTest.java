package tests.api;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import ai.GenAIClientAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.BaseAPITest;

public class CartTest extends BaseAPITest{
	
	  

	    @Test
	    public void addToCartWithGenAI() throws IOException {
	        // 🔥 Get random cart data from Gemini
	        Map<String, Object> cartPayload = GenAIClientAPI.generateCartData();
	        System.out.println("Generated Cart Data: " + cartPayload);
	        Gson gson = new Gson();
	        String jsonBody = gson.toJson(cartPayload);

	        Response response = requestSpec
	                .body(jsonBody)
	                .when()
	                .post("/carts");

	        response.then().statusCode(201);
	        Assert.assertTrue(response.asString().contains("products"),
	                "Cart creation failed!");
	    }

}
