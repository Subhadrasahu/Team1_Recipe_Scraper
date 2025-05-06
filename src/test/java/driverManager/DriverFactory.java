package driverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

import java.time.Duration;

public class DriverFactory {

    private static WebDriver driver;

    public static WebDriver initDriver() {
        ConfigReader.loadConfig();
        String browser = ConfigReader.get("browser");

        if (browser != null && browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            
            options.addArguments("--headless");
            options.addArguments("--disable-notifications");
            options.addArguments("disable-infobars");
            options.addArguments("--disable-extensions");
            options.addArguments("--window-size=1920,1080");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

           driver = new ChromeDriver(options);
           //driver = new ChromeDriver();
        } else {
            throw new RuntimeException("Browser type is either null or unsupported: " + browser);
        }

        // Basic driver setup
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.get("implicitWait"))
        ));

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
