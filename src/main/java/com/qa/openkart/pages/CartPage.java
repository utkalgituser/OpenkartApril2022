package com.qa.openkart.pages;

import org.openqa.selenium.By;

public class CartPage {
	
	private By cartBtn=By.id("cart");
	
	public boolean CartPage() {
		System.out.println("This is my cart page");
		return true;
	}
}
