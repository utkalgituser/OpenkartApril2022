package com.qa.openkart.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ElementUtil;

public class ProductInfoPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. By locators
	private By productCount = By.cssSelector("div#content div.product-thumb");
	private By productHeader = By.cssSelector("div#content h1");
	private By productImages = By.cssSelector("ul.thumbnails img");
	private By productMetaData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[1]/li");
	private By productPriceData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[2]/li");

	private Map<String, String> productMap;

	// 2. constructor
	public ProductInfoPage(WebDriver dr) {
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

	public String getProductHeaderValue() {
		String productHeaderVal = eleUtil.doElementGetText(productHeader);
		System.out.println("product header is: " + productHeaderVal);
		return productHeaderVal;
	}

	public int getProductImagesCount() {
		int imagesCount = eleUtil.waitForElementsToBeVisible(productImages, AppConstants.MEDIUM_DEFAULT_TIMEOUT).size();
		System.out.println("Images count is " + imagesCount);
		return imagesCount;
	}

	public Map<String, String> getProductInfo() {
		// No ordering
		productMap = new HashMap<>();
		// Maintains a key based map 
		// productMap = new LinkedHashMap<>();
		// Maintains a key based sorted order
		// productMap = new TreeMap<>();
		
		// Add product name:
		productMap.put("productname", getProductHeaderValue());

		getProductMetaData();
		getProductPriceData();
		System.out.println("======== actual product info ==============");
		productMap.forEach((k,v) -> System.out.println(k +" , "+v));
		return productMap;
	}

	private void getProductMetaData() {
		// product meta data
		// Product Code: Product 6
		// Availability: In Stock
		List<WebElement> metaDataList = eleUtil.getElements(productMetaData);
		metaDataList.stream()
			.map(e -> e.getText())
			.map(e -> productMap.put(e.split(":")[0].trim(), e.split(":")[1].trim()))
			.forEach(e -> System.out.println(e));
		
		/*
		 * for (WebElement e : metaDataList) { String text = e.getText(); String[]
		 * metaArray = text.split(":"); String key = metaArray[0].trim(); String value =
		 * metaArray[1].trim(); productMap.put(key, value); }
		 */
	}

	private void getProductPriceData() {
		// price data
		// $242.00
		// Ex Tax: $200.00
		List<WebElement> metaPrceList = eleUtil.getElements(productPriceData);
		String productPrice = metaPrceList.get(0).getText().trim();
		String productExTaxPrice = metaPrceList.get(1).getText().trim();

		productMap.put("productprice", productPrice);
		productMap.put("ExTaxPrice", productExTaxPrice);
	}
}
