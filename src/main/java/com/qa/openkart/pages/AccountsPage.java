package com.qa.openkart.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ElementUtil;

public class AccountsPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. By locators
	private By logoutLink = By.linkText("Logout");
	private By searchField = By.name("search");
	private By searchIcon = By.cssSelector("div#search button");
	private By accPageHeaders = By.cssSelector("div#content h2");

	// 2. constructor
	public AccountsPage(WebDriver dr) {
		System.out.println("AccountsPage constr called");
		this.driver = dr;
		eleUtil = new ElementUtil(driver);
		System.out.println("AccountsPage driver is "+driver);
	}

	// 3. Page actions:
	public String getAccountsPageTitle() {
		String title = eleUtil.waitForTitleToBe(AppConstants.SMALL_DEFAULT_TIMEOUT, AppConstants.ACCOUNTS_PAGE_TITLE);
		System.out.println("Login page title is " + title);
		return title;
	}

	public String getAccountsPageUrl() {
		String url = eleUtil.waitForUrl(AppConstants.SMALL_DEFAULT_TIMEOUT, AppConstants.ACCOUNTS_PAGE_URL_FRACTION);
		System.out.println("Login page url is " + url);
		return url;
	}

	public boolean isLogoutLinkExists() {
		return eleUtil.waitForElementToBeVisible(logoutLink, AppConstants.MEDIUM_DEFAULT_TIMEOUT).isDisplayed();
	}

	public WebElement isSearchFieldExists() {
		return eleUtil.waitForElementPresence(searchField, AppConstants.MEDIUM_DEFAULT_TIMEOUT);
	}

	public List<String> getAccountSectionsHeaderList() {
		return eleUtil.getElementsTextAsList(accPageHeaders, AppConstants.MEDIUM_DEFAULT_TIMEOUT);
	}
	
	//common page actions:
	public SearchResultsPage doSearch(String productName) {
		System.out.println("product name is "+productName);
		eleUtil.doSendKeysWithWait(searchField, AppConstants.SMALL_DEFAULT_TIMEOUT, productName);
		eleUtil.doClick(searchIcon);
		return new SearchResultsPage(driver);
	}
}
