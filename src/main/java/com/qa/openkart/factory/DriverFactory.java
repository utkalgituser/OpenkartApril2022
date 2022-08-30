package com.qa.openkart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.qa.openkart.constants.AppConstants;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * this class is used to webdriver instance.
 * 
 * @author Utkal Barik
 */
public class DriverFactory {

	public Properties prop;
	public OptionsManager optionsManager;

	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
	
	public static Logger log=Logger.getLogger(DriverFactory.class);
	
	/**
	 * This method used to initialize the driver on the basis of given browserName.
	 *
	 * @author Utkal Barik
	 * @param prop2
	 * @return returns driver instance
	 */
	public WebDriver initDriver(Properties prop2) {
		String browserName = prop2.getProperty("browser");
		log.info("Browser name is " + browserName);
		// System.out.println("Browser name is %%%%%%% "+System.getenv("browserName")+" %%%%%%%%%%%%%%%%%%%");
		
		// If the browserName is set at run time then use it
		/*
		 * if(Objects.nonNull(System.getenv("browserName")) ) {
		 * browserName=System.getenv("browserName"); }
		 */
		
		optionsManager = new OptionsManager(prop2);
		if (browserName.equalsIgnoreCase("chrome")) {
			log.info("Running test on chrome");
			if (Boolean.parseBoolean(prop2.getProperty("remote"))) {
				init_remoteWebDriver("chrome");
			} else {
				log.info("Running test oin local env.");
				WebDriverManager.chromedriver().setup();
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}

		} else if (browserName.equalsIgnoreCase("edge")) {
			if (Boolean.parseBoolean(prop2.getProperty("remote"))) {
				init_remoteWebDriver("edge");
			} else {
				WebDriverManager.edgedriver().setup();
				tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			}

		} else if (browserName.equalsIgnoreCase("firefox")) {
			if (Boolean.parseBoolean(prop2.getProperty("remote"))) {
				init_remoteWebDriver("firefox");
			} else {
				WebDriverManager.firefoxdriver().setup();
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}
		} else {
			log.error("please pass correct driver name");
			System.out.println("please pass correct driver name");
		}
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(AppConstants.MEDIUM_DEFAULT_TIMEOUT));
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop2.getProperty("url"));
		System.out.println("returning getDriver " + getDriver());
		return getDriver();
	}
	
	/**
	 * init remote webdriver on the basis of browser name 
	 *
	 * @author Utkal Barik
	 * @param string
	 *
	 */
	private void init_remoteWebDriver(String browserName) {
		System.out.println("Running on GRID ++++++++++++++++++");
		if (browserName.equalsIgnoreCase("chrome")) {
			try {
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("firefox")) {
			try {
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("edge")) {
			try {
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	public static synchronized WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * this return the properties reference with all config properties
	 * 
	 * @author Utkal Barik
	 * @return this return the properties reference
	 */
	public Properties initProp() {
		prop = new Properties();
		FileInputStream fis = null;

		// command line argument
		// mvn clean install -Denv="qa" -Dbrowser="chrome"
		// mvn clean install

		String envName = System.getProperty("env");
		System.out.println("Running the TCs on environment " + envName);

		if (Objects.isNull(envName)) {
			System.out.println("No environment is providied, so running in QA env");
			try {
				fis = new FileInputStream("./src/test/resources/config/qa.config.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				switch (envName.toLowerCase()) {
				case "qa":
					fis = new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
				case "stage":
					fis = new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
				case "dev":
					fis = new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
				case "prod":
					fis = new FileInputStream("./src/test/resources/config/prod.config.properties");
					break;
				case "uat":
					fis = new FileInputStream("./src/test/resources/config/uat.config.properties");
					break;
				default:
					System.out.println("Please pass the right environment name " + envName);
					break;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		/*
		 * try { fis = new
		 * FileInputStream("./src/test/resources/config/config.properties");
		 * prop.load(fis); } catch (IOException e) { e.printStackTrace(); } finally { if
		 * (Objects.nonNull(fis)) { try { fis.close(); } catch (IOException e) {
		 * e.printStackTrace(); } }
		 * 
		 * }
		 */
		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(fis)) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("prop is " + prop);
		return prop;
	}

	/**
	 * 
	 * @author Utkal Barik
	 * @return path of the screenshot file
	 *
	 */
	public static String getScreenShot() {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		File destination = new File(path);
		try {
			FileUtils.copyFile(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
}
