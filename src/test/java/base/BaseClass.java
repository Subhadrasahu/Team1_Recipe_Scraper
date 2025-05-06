package base;

import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.PopupHandler;

public class BaseClass {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();

        // Optional: Handle JS-based popups manually
        PopupHandler.closeJavaScriptPopups(driver);
        PopupHandler.handleAlertPopups(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
