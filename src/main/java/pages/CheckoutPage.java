package pages;

import java.io.IOException;
import java.time.Duration;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ai.GenAIClient;

public class CheckoutPage {
	
	WebDriver driver;
	
	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
	}
	
	static String Street = null;
	static String city1 = null;
	static String state = null;
	static String zip = null;
	
	By checkOutBtn = By.xpath("//input[@name='proceedToRetailCheckout' and @type='submit']");
	By checkOutHeading = By.xpath("//a[contains(text(),'Secure checkout')]");
	By delieveryAddress = By.xpath("//a[contains(text(),'Add a new delivery address')]");
	By fullName = By.id("address-ui-widgets-enterAddressFullName");
	By mobileNumber = By.id("address-ui-widgets-enterAddressPhoneNumber");
	By postalCode = By.id("address-ui-widgets-enterAddressPostalCode");
	By houseNumber = By.id("address-ui-widgets-enterAddressLine1");
	By streetNumber = By.id("address-ui-widgets-enterAddressLine2");
	By landmark = By.id("address-ui-widgets-landmark");
	By city = By.id("address-ui-widgets-enterAddressCity");
	By defaultAddressCheckbox = By.id("address-ui-widgets-use-as-my-default");
	
	public void doCheckOut() throws InterruptedException, IOException {
		
		driver.findElement(delieveryAddress).click();
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		
		
//		Enter Address Details
		GenAIClient genAI = new GenAIClient();
		String keyword = genAI.generateProductSearchKeyword();
		String customAddress = genAI.generateAddress();
	
		String cleanedJson = returnText(customAddress).replaceAll("(?s)```json", "")  
		                            .replaceAll("```", "")           
		                            .trim(); 
		System.out.println("After cleaned Json " +cleanedJson);
		extractAddress(cleanedJson);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 600);");
		
		driver.findElement(fullName).sendKeys("harsh Bobby");
		driver.findElement(mobileNumber).sendKeys("8765456789");
		driver.findElement(postalCode).sendKeys(zip);
		driver.findElement(houseNumber).sendKeys("147");
		driver.findElement(streetNumber).sendKeys(Street);
		driver.findElement(city).sendKeys(city1);
		driver.findElement(defaultAddressCheckbox).click();
		
		Thread.sleep(5000);
		
	}
	
	public Boolean goToCheckOutAndValidatePage() throws InterruptedException {
		
		driver.findElement(checkOutBtn).click();
		Thread.sleep(3000);
		return driver.findElement(checkOutHeading).getText().contains("Secure checkout");
	}
	
	public static void extractAddress(String jsonData) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonData);
		System.out.println("Yha kya arhha h "+jsonObject);
		Street = jsonObject.getString("street");
		city1 = jsonObject.getString("city");
		state = jsonObject.getString("state");
		zip = jsonObject.getString("zip");

		System.out.println("Street: " + Street);
		System.out.println("City: " + city1);
		System.out.println("State: " + state);
		System.out.println("ZIP: " + zip);
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

}
