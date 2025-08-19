package tests.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LoginPage;
import utils.BaseTest;

public class LoginTest extends BaseTest{
	
	@Test(priority=2,dependsOnMethods="makeInvalidLogin")
	public void makeValidLogin() throws InterruptedException {
		
		LoginPage login = new LoginPage(driver);
		login.goToLoginPage();
		Assert.assertTrue(login.isLoginBoard(), "You are not landing on Login Page");
		login.loginWithValidCredentials();
		Thread.sleep(5000);
		login.logout();
		Thread.sleep(5000);
	
	}
	
	@Test(priority=1)
	public void makeInvalidLogin() throws InterruptedException {
		
		LoginPage login = new LoginPage(driver);
		login.goToLoginPage();
		Assert.assertTrue(login.isLoginBoard(), "You are not landing on Login Page");
		login.loginWithInvalidCredentials();
		Thread.sleep(5000);
	}

}
