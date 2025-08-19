package utils;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseAPITest {

	protected RequestSpecification requestSpec;

    @BeforeClass
    public void setupAPI() {
        // Global base URL for all API tests
        RestAssured.baseURI = "https://fakestoreapi.com";

        // Common request spec (can be reused in all API tests)
        requestSpec = RestAssured.given()
                .contentType("application/json")
                .log().all();
    }
}
