package driverManager;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	public static WebDriver driver;
	//public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	public void init_driver(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options= new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("--disable-popup-blocking");      

            options.addArguments("-–disable-notifications");

            options.addArguments("-–disable-extensions");

            options.addArguments("--blink-settings=imagesEnabled=false");
			WebDriverManager.chromedriver().setup();
			//tlDriver.set(new ChromeDriver(options));
			driver= new ChromeDriver(options);
		}
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		//return getDriver();
	}

	public static WebDriver getDriver() {
		//return tlDriver.get();
		return driver;
	}


}
