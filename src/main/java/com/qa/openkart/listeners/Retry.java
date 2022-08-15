package com.qa.openkart.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
	private int count = 0;
	private static int maxTry = 3;

	@Override
	public boolean retry(ITestResult result) {
		if (!result.isSuccess()) {
			if (count < maxTry) {
				count++;
				result.setStatus(ITestResult.FAILURE); // if failed
				return true;
			} else {
				result.setStatus(ITestResult.FAILURE); // if failed
			}
		} else {
			result.setStatus(ITestResult.SUCCESS); // if passed
		}
		return false;
	}

}
