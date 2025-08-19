package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	WebDriver driver;

	public LoginPage(WebDriver driver) {

		this.driver = driver;
	}

	By loginLink = By.id("nav-link-accountList-nav-line-1");
	By loginBoardHeading = By.xpath("//h1[@class='a-size-medium-plus a-spacing-small']");
	By emailField = By.xpath("//input[@name='email' and @id='ap_email_login']");
	By passwordField = By.xpath("//input[@id='ap_password' and @type='password']");
	By continueBtn = By.xpath("//input[@type='submit']");
	By signInButton = By.id("signInSubmit");

	public void goToLoginPage() {

		driver.findElement(loginLink).click();
	}

	public Boolean isLoginBoard() {
		return driver.findElement(loginBoardHeading).isDisplayed();
	}

	public void loginWithInvalidCredentials() {
		WebElement emailFieldElement = driver.findElement(emailField);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value = 'hbsaini1610@yahoo.com'", emailFieldElement);

		driver.findElement(continueBtn).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement passwordFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

		js.executeScript("arguments[0].value = 'Dummyamazon@1234'", passwordFieldElement);

		driver.findElement(signInButton).click();
	}

	public void loginWithValidCredentials() throws InterruptedException {

		WebElement emailFieldElement = driver.findElement(emailField);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value = 'hbsaini1610@yahoo.com'", emailFieldElement);

		driver.findElement(continueBtn).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement passwordFieldElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

		js.executeScript("arguments[0].value = 'Dummyamazon@5024'", passwordFieldElement);

		driver.findElement(signInButton).click();

	}
	
	public void logout() {
		
		WebElement accountsLists = driver.findElement(By.id("nav-link-accountList"));
        Actions actions = new Actions(driver);
        actions.moveToElement(accountsLists).perform(); // Hover to show popup

        // Step 2: Wait for the Sign Out link to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement signOutLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Sign Out']"))
        );

        // Step 3: Click Sign Out using JSExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", signOutLink);
	}

}
