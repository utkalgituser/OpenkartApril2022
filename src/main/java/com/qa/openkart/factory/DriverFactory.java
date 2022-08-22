package com.qa.openkart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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

	/**
	 * This method used to initialize the driver on the basis of given browserName.
	 *
	 * @author Utkal Barik
	 * @param prop2
	 * @return returns driver instance
	 */
	public WebDriver initDriver(Properties prop2) {
		String browserName = prop2.getProperty("browser");
		System.out.println("Browser name is " + browserName);
		
		// If the browserName is set at run time then use it
		if(Objects.nonNull(System.getenv("browserName")) ) {
			browserName=System.getenv("browserName");
		}
		optionsManager = new OptionsManager(prop);
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			// driver = new ChromeDriver(optionsManager.getChromeOptions());
			tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
		} else if (browserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			// driver = new EdgeDriver();
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
		} else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
		} else {
			System.out.println("please pass correct driver name");
		}
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(AppConstants.MEDIUM_DEFAULT_TIMEOUT));
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop2.getProperty("url"));
		System.out.println("returning getDriver " + getDriver());
		return getDriver();
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
