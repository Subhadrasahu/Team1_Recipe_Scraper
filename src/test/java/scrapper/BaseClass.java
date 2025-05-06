package scrapper;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import driverManager.DriverFactory;
import utils.ConfigReader;

public class BaseClass {
	private WebDriver driver;
	ConfigReader config;

public void lunchBrowser() {
	config= new ConfigReader();
	driver=DriverFactory.getDriver();
	//driver.get(config.readDataFromConfig("baseUrl"));
	driver.get("https://m.tarladalal.com/");
	
}
}
