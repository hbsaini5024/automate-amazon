package tests.ui;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.CheckoutPage;
import pages.LandingPage;
import pages.LoginPage;
import pages.ProductPage;
import utils.BaseTest;

public class CheckOutTest extends BaseTest {
	
	@Test
	public void checkOutTheProduct() throws InterruptedException, IOException {
		
		LoginPage login = new LoginPage(driver);
		LandingPage landing = new LandingPage(driver);
		ProductPage product = new ProductPage(driver);
		CheckoutPage checkout = new CheckoutPage(driver);
		login.goToLoginPage();
		Assert.assertTrue(login.isLoginBoard(), "You are not landing on Login Page");
		login.loginWithValidCredentials();
		
		Assert.assertTrue(landing.isProductDashboard(),"You are not succesfully Logged In");
		
		
		product.searchProduct();
		Thread.sleep(5000);
		
		product.addToCartProduct();
		Assert.assertTrue(product.goToCart(), "We are not succesfully land on Add To Cart Page");
		
		Thread.sleep(3000);
		Assert.assertTrue(checkout.goToCheckOutAndValidatePage(), "Error Generated");
		checkout.doCheckOut();
		Thread.sleep(3000);
	}

}

