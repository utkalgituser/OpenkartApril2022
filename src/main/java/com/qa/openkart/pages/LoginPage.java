package com.qa.openkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. By locator - OR
	private By emailId = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By registerLink = By.linkText("Register");

	// 2. Page constructor
	public LoginPage(WebDriver dr) {
		this.driver = dr;
		eleUtil = new ElementUtil(driver);
		System.out.println("LoginPage driver is "+driver);
	}

	// 3. Page actions:
	@Step("getting login page title")
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleToBe(AppConstants.SMALL_DEFAULT_TIMEOUT, AppConstants.LOGIN_PAGE_TITLE);
		System.out.println("Login page title is " + title);
		return title;
	}
	
	@Step("getting login page url")
	public String getLoginPageUrl() {
		String url = eleUtil.waitForUrl(AppConstants.SMALL_DEFAULT_TIMEOUT, AppConstants.LOGIN_PAGE_URL_FRACTION);
		System.out.println("Login page url is ########## " + url);
		return url;
	}

	@Step("is Forgot Pwd Link Exists")
	public boolean isForgotPwdLinkExists() {
		return eleUtil.waitForElementPresence(forgotPwdLink, AppConstants.SMALL_DEFAULT_TIMEOUT).isDisplayed();
	}

	@Step("login with username: {0} and password: {1}")
	public AccountsPage doLogin(String uname, String pwd) {
		System.out.println("app credentials are  ########## " + uname + " , " + pwd);
		eleUtil.doSendKeysWithWait(emailId, AppConstants.MEDIUM_DEFAULT_TIMEOUT, uname);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		return new AccountsPage(driver);
		// return eleUtil.waitForTitleToBe(AppConstants.MEDIUM_DEFAULT_TIMEOUT,
		// AppConstants.ACCOUNTS_PAGE_TITLE);
	}

	@Step("perform search for the product: {0}")
	public SearchResultsPage performSearch(String name) {
		AccountsPage accountPage = new AccountsPage(driver);
		return accountPage.doSearch(name);
	}
	
	@Step("redirect to registration page")
	public RegisterPage goToRegistrationPage() {
		eleUtil.doClick(registerLink);
		return new RegisterPage(driver);
	}
}
