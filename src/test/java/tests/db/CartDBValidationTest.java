package tests.db;

import base.BaseDBTest;
import utils.DBUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.*;

import ai.GenAIClientAPI;
import com.google.gson.Gson;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class CartDBValidationTest extends BaseDBTest{
	
	 Connection conn;
	    String baseUrl = "https://fakestoreapi.com";

	    @BeforeClass
	    public void setupDB() throws Exception {
	        connect();
	        conn = connection;
	    }

	    @AfterClass
	    public void tearDownDB() throws Exception {
	        disconnect();
	    }
	    
	    @Test
	    public void validateCartDataWithDB() throws Exception {
	    	
//	        Generate Cart Data from GenAI
	    	
	        Map<String, Object> cartPayload = GenAIClientAPI.generateCartData();
	        Gson gson = new Gson();
	        String jsonBody = gson.toJson(cartPayload);
	        System.out.println("Cart Payload : "+ cartPayload);

	       
	        Response response = RestAssured.given()
	                .header("Content-Type", "application/json")
	                .body(jsonBody)
	                .when()
	                .post(baseUrl + "/carts");

	        response.then().statusCode(201);
	        System.out.println("Cart Response : "+ response);

	        int userId = ((Double) cartPayload.get("userId")).intValue();
	        String date = cartPayload.get("date").toString();
	        
	        int cartId = DBUtils.getCartId(conn, userId, date);
	        Assert.assertTrue(cartId != -1, "Cart not found in DB!");

	        System.out.println("Cart found in DB with ID: " + cartId);

//	        Validate Cart Products
	        List<String> dbProducts = DBUtils.getCartProducts(conn, cartId);
	        System.out.println("DB Products: " + dbProducts);

	        // Compare with API payload products
	        @SuppressWarnings("unchecked")
	        List<Map<String, Object>> apiProducts = (List<Map<String, Object>>) cartPayload.get("products");

	        for (Map<String, Object> prod : apiProducts) {
	            String expected = "ProductID=" + ((Double) prod.get("productId")).intValue()
	                    + ", Qty=" + ((Double) prod.get("quantity")).intValue();
	            Assert.assertTrue(dbProducts.contains(expected),
	                    "Product mismatch! Expected: " + expected);
	        }
	        
	        System.out.println("Cart API data validated with DB successfully!");
	    }

}
