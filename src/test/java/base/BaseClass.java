package base;

import driverManager.DriverFactory;
import utils.AdHandler;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
//import utils.PopupHandler;

public class BaseClass {

    protected WebDriver driver;

//    @BeforeMethod
//    public void setUp() {
//        driver = DriverFactory.initDriver();
//
//        // Optional: Handle JS-based popups manually
//       // PopupHandler.closeJavaScriptPopups(driver);
//        //PopupHandler.handleAlertPopups(driver);
//    }
//    
    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();
        driver.get(utils.ConfigReader.get("baseUrl"));
    }

    @BeforeMethod
    public void handleAds() {
        AdHandler.handleAds(driver);  // Handle ads before each test
       // AdHandler.handlePopUpAds(driver);
        
    }
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
