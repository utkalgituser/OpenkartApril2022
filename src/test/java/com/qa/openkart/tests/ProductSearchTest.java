package com.qa.openkart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.openkart.base.BaseTest;
import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ExcelUtil;

public class ProductSearchTest extends BaseTest {

	@BeforeClass
	public void productSearchSetup() {
		accountPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@DataProvider
	public Object[][] getProductData() {
		return new Object[][] {
			{"Macbook","MacBook Pro" },
			{"Macbook","MacBook Air" },
			{"Samsung","Samsung Galaxy Tab 10.1" }
		};
	}
	
	/**
	 *
	 * 02-Aug-2022
	 * @author Utkal Barik
	 * 
	 */
	@Test(dataProvider = "getProductData")
	public void productSearchTest(String searchKey, String productName) {
		searchResultsPage = accountPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(productName);
		String actualProductHeaderName = productInfoPage.getProductHeaderValue();
		Assert.assertEquals(actualProductHeaderName, productName);
	}
	
	@DataProvider
	public Object[][] getProductInfoData() {
		return new Object[][] {
			{"Macbook","MacBook Pro", 4},
			{"Macbook","MacBook Air", 4},
			{"Samsung","Samsung Galaxy Tab 10.1", 7},
			{"iMac","iMac", 3 },
		};
	}
	
	@DataProvider
	public Object[][] getProductExcelData(){
		return ExcelUtil.getTestData(AppConstants.PRODUCT_SHEET_NAME);	
	}
	
	@Test(dataProvider = "getProductInfoData")
	public void productImagesCount(String searchKey, String productName, int imagesCount) {
		searchResultsPage = accountPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(productName);
		int actualImagesCount=productInfoPage.getProductImagesCount();
		Assert.assertEquals(actualImagesCount, imagesCount);
	}
	
	@Test
	public void productInfoTest() {
		searchResultsPage=accountPage.doSearch("Macbook");
		productInfoPage=searchResultsPage.selectProduct("MacBook Pro");
		
		Map<String, String> actualProductInfo = productInfoPage.getProductInfo();
		
		softAssert.assertEquals(actualProductInfo.get("Brand"), "Apple");
		softAssert.assertEquals(actualProductInfo.get("Product Code"), "Product 18");
		softAssert.assertEquals(actualProductInfo.get("Availability"), "In Stock");
		softAssert.assertEquals(actualProductInfo.get("productname"), "MacBook Pro");
		softAssert.assertEquals(actualProductInfo.get("productprice"), "$2,000.00");
		softAssert.assertEquals(actualProductInfo.get("productname"), "MacBook Pro");
		softAssert.assertAll();
	}
}
