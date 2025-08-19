package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LandingPage {

	WebDriver driver;

	public LandingPage(WebDriver driver) {
		
		this.driver = driver;
	}

	By loginHeading = By.id("nav-link-accountList-nav-line-1");

	public Boolean isProductDashboard() {

		String text = driver.findElement(loginHeading).getText();

		return text.contains("Hello, Harsh");
	}

}
