package tests.ui;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.LandingPage;
import pages.LoginPage;
import pages.ProductPage;
import utils.BaseTest;

public class ProductTest extends BaseTest{
	

	@Test
	public void searchProduct() throws IOException, InterruptedException {
		
		LoginPage login = new LoginPage(driver);
		LandingPage landing = new LandingPage(driver);
		ProductPage product = new ProductPage(driver);
		login.goToLoginPage();
		Assert.assertTrue(login.isLoginBoard(), "You are not landing on Login Page");
		login.loginWithValidCredentials();
		
		Assert.assertTrue(landing.isProductDashboard(),"You are not succesfully Logged In");
		
		
		product.searchProduct();
		Thread.sleep(5000);
	}
}
