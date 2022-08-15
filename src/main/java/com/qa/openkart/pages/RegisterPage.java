package com.qa.openkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ElementUtil;

public class RegisterPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	private By firstName = By.id("input-firstname");
	private By lastName = By.id("input-lastname");
	private By email = By.id("input-email");
	private By telephone = By.id("input-telephone");
	private By password = By.id("input-password");
	private By confirmpassword = By.id("input-confirm");

	private By subscribeYes = By.xpath("(//label[@class='radio-inline'])[position()=1]/input");
	private By subscribeNo = By.xpath("(//label[@class='radio-inline'])[position()=2]/input");

	private By agreeCheckBox = By.name("agree");
	private By continueButton = By.xpath("//input[@type='submit' and @value='Continue']");
	private By sucessMessg = By.cssSelector("div#content h1");

	private By logoutLink = By.linkText("Logout");
	private By registerLink = By.linkText("Register");

	public RegisterPage(WebDriver dr) {
		this.driver = dr;
		eleUtil = new ElementUtil(driver);
	}

	public boolean userRegistration(String firstName, String lastname, String email, String phone, String password,
			String subscribe) {

		eleUtil.doSendKeysWithWait(this.firstName, AppConstants.MEDIUM_DEFAULT_TIMEOUT, firstName);
		eleUtil.doSendKeys(this.lastName, lastname);
		eleUtil.doSendKeys(this.email, email);
		eleUtil.doSendKeys(this.telephone, phone);
		eleUtil.doSendKeys(this.password, password);
		eleUtil.doSendKeys(this.confirmpassword, password);

		if (subscribe.equalsIgnoreCase("yes")) {
			eleUtil.doActionsClick(this.subscribeYes);
		} else {
			eleUtil.doActionsClick(this.subscribeNo);
		}

		eleUtil.doClick(this.agreeCheckBox);
		eleUtil.doActionsClick(this.continueButton);

		System.out.println("User registration success message =================");
		String actualSuccessText = eleUtil
				.waitForElementToBeVisible(this.sucessMessg, AppConstants.MEDIUM_DEFAULT_TIMEOUT).getText();
		eleUtil.doClickWithWait(this.logoutLink, AppConstants.MEDIUM_DEFAULT_TIMEOUT);
		eleUtil.doClick(this.registerLink);
		return actualSuccessText.contains(AppConstants.REGISTER_SUCCCESS_MESSAGE);
	}

}
