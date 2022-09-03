package com.qa.openkart.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class OptionsManager {

	public static Logger log=Logger.getLogger(OptionsManager.class);

	private Properties prop;
	// private ChromeOptions co;
	// private FirefoxOptions fo;
	// private EdgeOptions eo;

	public OptionsManager(Properties prop2) {
		this.prop = prop2;
	}

	public ChromeOptions getChromeOptions() {
		ChromeOptions co = new ChromeOptions();
		if (Boolean.parseBoolean(prop.getProperty("headless"))) {
			co.addArguments("--headless");
			// co.setHeadless(true);
		}
		if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
			co.addArguments("--incognito");
			// co.setHeadless(true);
		}
		if (Boolean.parseBoolean(prop.getProperty("remote"))) {
			co.setCapability("enableVNC", true);
			co.setBrowserVersion(prop.getProperty("browserversion"));
			/* // Selenium grid properties disabled for Selenoid 
			 * co.setCapability("se:screenResolution", "1920x1080");
			 * co.setPlatformName("linux");
			 */
		}
		return co;
	}

	public FirefoxOptions getFirefoxOptions() {

		FirefoxOptions fo = new FirefoxOptions();
		if (Boolean.parseBoolean(prop.getProperty("headless")))
			fo.setHeadless(true);

		if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
			fo.addArguments("--incognito");
		}
		if (Boolean.parseBoolean(prop.getProperty("remote"))) {
			fo.setCapability("enableVNC", true);
			fo.setBrowserVersion(prop.getProperty("browserversion"));
			Map<String, Object> selenoidOption=new HashMap<>();
			/* // Selenium grid  properties disabled for Selenoid 
			 * fo.setCapability("se:screenResolution", "1920x1080");
			 * fo.setPlatformName("linux");
			 */
		}
		return fo;
	}

	public EdgeOptions getEdgeOptions() {

		EdgeOptions eo = new EdgeOptions();
		if (Boolean.parseBoolean(prop.getProperty("headless")))
			eo.setHeadless(true);

		if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
			eo.addArguments("--incognito");
		}
		if (Boolean.parseBoolean(prop.getProperty("remote"))) {
			eo.setCapability("enableVNC", true);
			// Selenoid not supported for Edge browser
			eo.setCapability("se:screenResolution", "1920x1080");
			eo.setPlatformName("linux");
		}
		return eo;
	}
}
