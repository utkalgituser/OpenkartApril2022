package com.qa.openkart.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.qa.openkart.factory.DriverFactory;
import com.qa.openkart.pages.AccountsPage;
import com.qa.openkart.pages.LoginPage;
import com.qa.openkart.pages.ProductInfoPage;
import com.qa.openkart.pages.RegisterPage;
import com.qa.openkart.pages.SearchResultsPage;

public class BaseTest {

	public WebDriver driver;
	public Properties prop;
	
	public DriverFactory df;
	
	// All page reference must be maintained in BaseTest
	public LoginPage loginPage;
	public AccountsPage accountPage;
	public SearchResultsPage searchResultsPage;
	public ProductInfoPage productInfoPage;
	public RegisterPage registerPage;
	
	public SoftAssert softAssert;

	@BeforeTest
	public void setup() {
		df = new DriverFactory();
		
		prop = df.initProp();
		// initDriver returns a copy of threadlocal driver
		// Used every where in the framework
		driver = df.initDriver(prop);
		System.out.println("driver is " + driver);
		loginPage = new LoginPage(driver);
		System.out.println("LoginPage in setup() is "+this.loginPage);
		softAssert = new SoftAssert();
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}
