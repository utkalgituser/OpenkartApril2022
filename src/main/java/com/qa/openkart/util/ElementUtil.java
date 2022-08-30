package com.qa.openkart.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.util.concurrent.Uninterruptibles;

public class ElementUtil {

	public static Logger log=Logger.getLogger(ElementUtil.class);

	private WebDriver driver;
	private Actions act;
	
	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		act=new Actions(driver);
	}

	public By getBy(String locatorType, String selector) {

		By locator = null;
		switch (locatorType.toLowerCase()) {

		case "id":
			locator = By.id(selector);
			break;

		case "name":
			locator = By.name(selector);
			break;

		case "classname":
			locator = By.className(selector);
			break;

		case "xpath":
			locator = By.xpath(selector);
			break;

		case "css":
			locator = By.cssSelector(selector);
			break;

		case "linktext":
			locator = By.linkText(selector);
			break;

		case "partiallinktext":
			locator = By.partialLinkText(selector);
			break;

		case "tagname":
			locator = By.tagName(selector);
			break;

		default:
			break;
		}
		return locator;
	}

	public void doSendKeys(String locatorType, String selector, String value) {
		By locator = getBy(locatorType, selector);
		getElement(locator).sendKeys(value);
	}

	public void doSendKeys(By locator, String value) {
		getElement(locator).clear();
		getElement(locator).sendKeys(value);
	}

	public void doClick(String locatorType, String selector) {
		By locator = getBy(locatorType, selector);
		getElement(locator).click();
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}
	
	public void doActionsSendKeys(By locator, String value) {
		act.sendKeys(getElement(locator), value).build().perform();
	}

	public void doActionsClick(By locator) {
		act.click(getElement(locator)).perform();
	}
	
	public String doElementGetText(By locator) {
		return getElement(locator).getText();
	}

	public String doGetAttributeValue(By locator, String attributeName) {
		return getElement(locator).getAttribute(attributeName);
	}

	public List<String> doGetAttributeList(By locator, String attributeName) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleAttrValList = new ArrayList<>();
		for (WebElement e : eleList) {
			eleAttrValList.add(e.getAttribute(attributeName));
		}
		return eleAttrValList;
	}

	public WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public int getPageLinksCount(By locator) {
		return getElements(locator).size();
	}
	
	/**
	 * Return a list of String from a given list and removing elements with no visible text
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @return
	 *
	 */
	public List<String> getElementsTextAsList(By locator, int timeToWait) {
		List<WebElement> eleList = waitForElementsToBeVisible(locator, timeToWait);
		List<String> eleTxtList = new ArrayList<>();
		for (WebElement e : eleList) {
			String text = e.getText();
			if (!text.isEmpty()) {
				eleTxtList.add(text);
			}
		}
		return eleTxtList;
	}
	
	/**
	 * Print all elements visible text by removing empty visible text elements   
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 *
	 */
	public void printAllElementsText(By locator, int timeOut) {
		List<WebElement> list = waitForElementsToBeVisible(locator, timeOut);
		list.stream()
			.map(WebElement::getText)
			.filter(s -> !s.isEmpty())
			.forEach(System.out::println);
	}
	
	public String getAlertText(int timeOut) {
		return waitForAlert(timeOut).getText();
	}

	public void acceptAlert(int timeOut) {
		waitForAlert(timeOut).accept();
	}

	public void rejectAlert(int timeOut) {
		waitForAlert(timeOut).dismiss();
	}

	public void alertSendKeys(int timeOut, String keysToSend) {
		waitForAlert(timeOut).sendKeys(keysToSend);
	}

	public void serach(By serachBox, String textToSearch, By searchListxpath, String searchListText) {
		doSendKeys(serachBox, textToSearch);
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(1));
		List<WebElement> suggList = driver.findElements(searchListxpath);
		for (WebElement e : suggList) {
			if (e.getText().contains(searchListText)) {
				e.click();
				Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(2));
				break;
			}
		}
	}

	// ************** Element display util **********************
	public boolean doIsDisplayed(By locator) {
		return getElement(locator).isDisplayed();
	}

	public int getElementCount(By locator) {
		return getElements(locator).size();
	}

	public boolean checkSingleElementExists(By locator) {
		return getElementCount(locator) == 1;
		
	}

	public boolean checMultipleElementExists(By locator) {
		return getElementCount(locator) > 1;
	}

	// *************** select tag based dropdown Utils *************************

	public void doSelectByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public void doSelectByVisibleText(By locator, String visibleText) {
		Select select = new Select(getElement(locator));
		select.selectByVisibleText(visibleText);
	}

	public void doSelectByValue(By locator, String value) {
		Select select = new Select(getElement(locator));
		select.selectByValue(value);
	}

	public int getDropDownCount(By locator) {
		Select select = new Select(getElement(locator));
		return select.getOptions().size();
	}

	public List<String> getDropDownValuesList(By locator) {
		Select select = new Select(getElement(locator));
		List<String> optionsValList = new ArrayList<>();
		List<WebElement> optionsEleList = select.getOptions();
		for (WebElement e : optionsEleList) {
			String text = e.getText();
			optionsValList.add(text);
		}
		return optionsValList;
	}

	public void doSelectValueUsingOptions(By locator, String value) {
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList = select.getOptions();
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

	public void doSelectValuefromDropDown(By locator, String value) {
		List<WebElement> optionsList = getElements(locator);
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}
	
	// *************** WAIT UTILS *************
	
	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not only
	 * displayed but also have a height and width that is greater than 0.
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 * @return
	 *
	 */
	public List<WebElement> waitForElementsToBeVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible.Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * Default polling time is 500ms. 
	 * 
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 * @param pollingInterval
	 * @return
	 *
	 */
	public WebElement waitForElementToBeVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public WebElement waitForElementToBeVisible(By locator, int timeOut, int pollingInterval) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut), Duration.ofSeconds(pollingInterval));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does not necessarily mean that the element is visible.
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 * @return
	 *
	 */
	
	public WebElement waitForElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	/**
	 * An expectation for checking an element is visible and enabled such that you can click it.
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 *
	 */
	public void clickWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	public void clickElementWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(getElement(locator))).click();
	}
	
	/**
	 * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in
	 * the 'until' condition, and immediately propagate all others.  You can add more to the ignore
	 * list by calling ignoring(exceptions to add).
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 * @param pollingTime
	 * @return
	 *
	 */
	public WebElement waitForElementVisiblityWithFluentWait(By locator, int timeOut, int pollingTime) {
		Wait<WebDriver> wait=new FluentWait<WebDriver>(driver)
					.withTimeout(Duration.ofSeconds(timeOut))
					.pollingEvery(Duration.ofSeconds(pollingTime))
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
					.withMessage("Element is not on the page.. sorry ");
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	/**
	 * Checks for Alert and switches to alert if condition is true.
	 *
	 * 19-Jul-2022
	 * @author Utkal Barik
	 * @param timeOut
	 * @return
	 *
	 */
	public Alert waitForAlert(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
	}
	
	/**
	 * Waits for title to appear and check part of title string  
	 *
	 * @author Utkal Barik
	 * @param timeOut
	 * @param titlePartValue
	 * @return
	 *
	 */
	public String waitForTitleContains(int timeOut, String titlePartValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if (Boolean.TRUE.equals(wait.until(ExpectedConditions.titleContains(titlePartValue)))) {
			return driver.getTitle();
		} else {
			System.out.println("Title not found...");
			return null;
		}
	}
	
	/**
	 * Waits for title to appear and check for exact title match 
	 *
	 * @author Utkal Barik
	 * @param timeOut
	 * @param expectedTitle
	 * @return
	 *
	 */
	public String waitForTitleToBe(int timeOut, String expectedTitle) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if (Boolean.TRUE.equals(wait.until(ExpectedConditions.titleIs(expectedTitle)))) {
			return driver.getTitle();
		} else {
			System.out.println("Title is not found...");
			return null;
		}
	}
	
	public String waitForUrl(int timeOut, String urlPart) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if (Boolean.TRUE.equals(wait.until(ExpectedConditions.urlContains(urlPart)))) {
			return driver.getCurrentUrl();
		} else {
			System.out.println("URL is not found...");
			return null;
		}
	}
	
	public String waitForUrlToBe(int timeOut, String expectedUrl) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		if (Boolean.TRUE.equals(wait.until(ExpectedConditions.urlToBe(expectedUrl)))) {
			return driver.getCurrentUrl();
		} else {
			System.out.println("URL is not found...");
			return null;
		}
	}
	
	/**
	 * An expectation for checking whether the given frame is available to switch to. 
	 * If the frame is available it switches the given driver to the specified frame.
	 *
	 * @author Utkal Barik
	 * @param frameLocator
	 * @param timeOut
	 *
	 */
	public void waitForFrameAndSwitchTo(By frameLocator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}
	
	public void waitForFrameAndSwitchTo(int frameIndex, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	
	public void waitForFrameAndSwitchTo(String frameName, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}
	
	/**
	 * Wait for presence of element check pass before sending char sequence. 
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 * @param value
	 *
	 */
	public void doSendKeysWithWait(By locator, int timeOut, String value) {
		WebElement ele = waitForElementPresence(locator, timeOut);
		ele.clear();
		ele.sendKeys(value);
	}
	
	/**
	 *  Wait for presence of element check pass before clicking on element.
	 *
	 * @author Utkal Barik
	 * @param locator
	 * @param timeOut
	 *
	 */
	public void doClickWithWait(By locator, int timeOut) {
		waitForElementPresence(locator, timeOut).click();
	}
	
	
	//**************************Custom Wait********************************//
		public static void shortWait() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		public static void mediumWait() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		public static void longWait() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		public static void waitFor(long timeOut) {
			try {
				Thread.sleep(timeOut * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		public void waitForPageLoad(int timeOut) {
			long endTime = System.currentTimeMillis() + timeOut;
			while (System.currentTimeMillis() < endTime) {
				String pageState = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
				System.out.println("page loading state: " + pageState);
				if (pageState.equals("complete")) {
					System.out.println("page is fully loaded with all scripts, images, css...");
					break;
				}
			}
		}

		public WebElement retryingElement(By locator, int timeOut) {
			WebElement element = null;
			int attempts = 0;
			while (attempts < timeOut) {
				try {
					element = getElement(locator);
					System.out.println("element is found....in attempt: " + (attempts + 1));
					break;
				} catch (NoSuchElementException e) {
					System.out.println("element is not found in attempt: " + attempts + " for locator: " + locator);
					try {
						Thread.sleep(500);// default interval time
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
				attempts++;
			}

			if (element == null) {
				try {
					throw new Exception("ELEMENTNOTFOUNDEXCEPTION");
				} catch (Exception e) {
					System.out.println("element is not found....tried for :" + timeOut + " secs "
							+ "with the interval of : " + 500 + " ms");
				}
			}

			return element;

		}

		public WebElement retryingElement(By locator, int timeOut, int intervalTime) {
			WebElement element = null;
			int attempts = 0;
			while (attempts < timeOut) {

				try {
					element = getElement(locator);
					System.out.println("element is found....in attempt: " + (attempts + 1));
					break;
				} catch (NoSuchElementException e) {
					System.out.println("element is not found in attempt: " + attempts + " for locator: " + locator);
					try {
						Thread.sleep(intervalTime);// interval time
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}

				attempts++;

			}

			if (element == null) {
				try {
					throw new Exception("ELEMENTNOTFOUNDEXCEPTION");
				} catch (Exception e) {
					System.out.println("element is not found....tried for :" + timeOut + " secs "
							+ "with the interval of : " + intervalTime + " ms");
				}
			}

			return element;

		}
}
