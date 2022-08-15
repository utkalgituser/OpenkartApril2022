package com.qa.openkart.tests;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.openkart.base.BaseTest;
import com.qa.openkart.constants.AppConstants;
import com.qa.openkart.util.ExcelUtil;

public class RegisterTest extends BaseTest {

	public String randonEmail() {
		Random random = new Random();
		String email = "automation" + random.nextInt(1000) + "@gmail.com";
		System.out.println("New email is " + email);
		return email;
	}

	@BeforeClass
	public void registrationSetup() {
		registerPage = loginPage.goToRegistrationPage();
	}

	@DataProvider
	public Object[][] getRegistratioExcelData() {
		return ExcelUtil.getTestData(AppConstants.REGISTER_SHEET_NAME);
	}

	@Test(dataProvider = "getRegistratioExcelData")
	public void userRegTest(String firstName, String lastname, String phone, String password, String subscribe) {
		boolean actualFlag = registerPage.userRegistration(firstName, lastname, randonEmail(), phone, password,
				subscribe);
		Assert.assertEquals(actualFlag, true);
	}
}
