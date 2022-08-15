package com.qa.openkart.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.openkart.base.BaseTest;
import com.qa.openkart.constants.AppConstants;

import io.qameta.allure.Epic;
import io.qameta.allure.Story;

@Epic("Epic - 200: Design the Account page feature for opencart application")
@Story("US - 101: design login page feature with all details")
public class AccountPageTest extends BaseTest {

	@BeforeClass
	public void accSetup() {
		System.out.println("accSetup ----------- in AccountPageTest");
		System.out.println("loginPage is " + loginPage);
		System.out.println("username is "+prop.getProperty("username"));
		accountPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test
	public void isLogoutLinkExistsTest() {
		System.out.println("isLogoutLinkExistsTest -----------");
		Assert.assertEquals(accountPage.isLogoutLinkExists(), true);
	}
	
	@Test
	public void accountPageTitleTest() {
		System.out.println("accountPageTitleTest -----------");
		Assert.assertEquals(accountPage.getAccountsPageTitle(), AppConstants.ACCOUNTS_PAGE_TITLE);
	}
	
	
	@Test
	public void accountPageHeaderTest() {
		System.out.println("accountPageHeaderTest -----------");
		List<String> actualHeaderList = accountPage.getAccountSectionsHeaderList();
		System.out.println("================== actual headers list ==================");
		Assert.assertEquals(actualHeaderList, AppConstants.EXPECTED_ACCOUNTS_HEADER_LIST);
	}
		
}
