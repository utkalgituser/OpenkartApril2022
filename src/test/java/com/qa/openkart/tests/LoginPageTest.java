package com.qa.openkart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.openkart.base.BaseTest;
import com.qa.openkart.constants.AppConstants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Epic - 100: Design the login page feature for opencart application")
@Story("US - 101: design login page feature with login, forgot pwd, title, url")
public class LoginPageTest extends BaseTest {

	@Test
	@Description("TC_01: login page title test")
	@Severity(SeverityLevel.NORMAL)
	public void loginPageTitleTest() {
		System.out.println("loginPage is loginPageTitleTest is "+loginPage);
		String actTitle = loginPage.getLoginPageTitle();
		System.out.println("actTitle is *********** "+actTitle);
		Assert.assertEquals(actTitle, AppConstants.LOGIN_PAGE_TITLE);
	}

	@Test
	@Description("TC_02: login page url test")
	@Severity(SeverityLevel.NORMAL)
	public void loginPageUrlTest() {
		String actUrl = loginPage.getLoginPageUrl();
		System.out.println("actUrl is ***********  "+actUrl);
		// Assert.assertEquals(actUrl.contains(AppConstants.LOGIN_PAGE_URL_FRACTION), true);
		Assert.assertEquals(actUrl.contains(AppConstants.LOGIN_PAGE_URL_FRACTION), false);
	}

	@Test
	@Description("TC_03: login page forgotPwdLinkExistsText test")
	@Severity(SeverityLevel.CRITICAL)
	public void forgotPwdLinkExistsText() {
		Assert.assertEquals(loginPage.isForgotPwdLinkExists(), true);
	}

	@Test
	@Description("TC_04: verify user is able to login with correct username and pwd")
	@Severity(SeverityLevel.BLOCKER)
	public void loginTest() {
		System.out.println("In loginTest ***********  ");
		accountPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		Assert.assertEquals(accountPage.getAccountsPageTitle(), AppConstants.ACCOUNTS_PAGE_TITLE);
	}
	
	@Test(enabled = false)
	public void loginPageFooterTest() {
		System.out.println("In loginPageFooterTest ***********  ");
	}
}
