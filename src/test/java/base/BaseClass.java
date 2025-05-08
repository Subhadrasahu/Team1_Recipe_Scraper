package base;

import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.PopupHandler;

public class BaseClass {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.initDriver();

        try {
            // Optional: Handle JavaScript or alert-based popups early
            PopupHandler.closeJavaScriptPopups(driver);
            PopupHandler.handleAlertPopups(driver);
        } catch (Exception e) {
            System.err.println("Popup handling failed during setup: " + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DriverFactory.quitDriver();
        } catch (Exception e) {
            System.err.println("Error during WebDriver cleanup: " + e.getMessage());
        }
    }
}
