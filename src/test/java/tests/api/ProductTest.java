package tests.api;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import ai.GenAIClientAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.BaseAPITest;

public class ProductTest extends BaseAPITest {
	
	Response response;
	
	@Test(priority=1,description="Check whether Get Products API is working properly")
	public void searchProductWithGenAI() {
	    
	    // 1. Get all products from API
		 response = requestSpec
	                .when()
	                .get("/products");

	    response.then().statusCode(200);
	    System.out.println(response);

	   
	}
	
	@Test(priority=2,description="Check whether API product list contains GenAI generated product")
	public void isProductExist() throws IOException {
		
		// 1. Get a random product name from Gemini
	    String productName = GenAIClientAPI.generateProductName();
	    System.out.println("Generated Product Name: " + productName);

		 // 2. Search if Gemini product name exists in API response
	    boolean found = response.asString().toLowerCase().contains(productName.toLowerCase());

	    // 3. Assertion
	    Assert.assertTrue(found,
	            "Gemini generated product '" + productName + "' was not found in product list!");
	}


}
