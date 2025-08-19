package pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ai.GenAIClient;

public class ProductPage {

	WebDriver driver;

	public ProductPage(WebDriver driver) {
		this.driver = driver;
	}

	public static String returnText(String jsonString) {
		String jsonResponse = jsonString;
		String text = null;
		JsonObject root = JsonParser.parseString(jsonResponse).getAsJsonObject();
		JsonArray candidates = root.getAsJsonArray("candidates");

		if (candidates != null && candidates.size() > 0) {
			JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
			JsonObject content = firstCandidate.getAsJsonObject("content");
			JsonArray parts = content.getAsJsonArray("parts");

			if (parts != null && parts.size() > 0) {
				text = parts.get(0).getAsJsonObject().get("text").getAsString();
				System.out.println("Extracted Text: " + text);

			}
		}
		return text;

	}

	By searchBox = By.id("twotabsearchtextbox");
	By searchButton = By.id("nav-search-submit-button");
	By addToCartProductItem = By.xpath("(//div[@class='a-section a-spacing-base'])[1]//button");
	By addToCartBTn = By.id("nav-cart-count-container");
	By shoppingCartHeading = By.id("sc-active-items-header");
	
	public void searchProduct() throws IOException, InterruptedException {

		GenAIClient genAI = new GenAIClient();

		String keyword = genAI.generateProductSearchKeyword();
		System.out.println("Searching for: " + returnText(keyword));
		String searchValue = returnText(keyword);
		Thread.sleep(3000);
		driver.findElement(searchBox).sendKeys(searchValue);
		
		driver.findElement(searchButton).click();
	}
	
	public void addToCartProduct() throws InterruptedException {
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 600);");

		Thread.sleep(2000);
			
		driver.findElement(addToCartProductItem).click();
	}
	
	public Boolean goToCart() throws InterruptedException {
		
		driver.findElement(addToCartBTn).click();
		Thread.sleep(3000);
		return driver.findElement(shoppingCartHeading).getText().contains("Shopping Cart");
	}
}
