package com.qa.openkart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ElementUtil;

public class SearchResultsPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. By locators
	By productCount = By.cssSelector("div#content div.product-thumb");

	// 2. constructor
	public SearchResultsPage(WebDriver dr) {
		this.driver = dr;
		eleUtil = new ElementUtil(driver);
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

	public int getSerachProductCount() {
		return eleUtil.waitForElementsToBeVisible(productCount, AppConstants.MEDIUM_DEFAULT_TIMEOUT).size();
	}

	public ProductInfoPage selectProduct(String productName) {
		By product = By.linkText(productName);
		eleUtil.doClick(product);
		return new ProductInfoPage(driver);
	}
	
}
