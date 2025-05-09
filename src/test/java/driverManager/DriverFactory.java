package driverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver initDriver() {
        ConfigReader.loadConfig();
        String browser = ConfigReader.get("browser");

        if (browser != null && browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Enable headless if configured in properties
            if (ConfigReader.get("headless").equalsIgnoreCase("true")) {
                options.addArguments("--headless=new"); // new mode is more stable
            }

            options.addArguments("--disable-notifications");
            options.addArguments("disable-infobars");
            options.addArguments("--disable-extensions");
            options.addArguments("--window-size=1920,1080");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);

            tlDriver.set(new ChromeDriver(options));

        } else {
            throw new RuntimeException("Browser type is either null or unsupported: " + browser);
        }

        // Standard driver config
        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.get("implicitWait"))
        ));

        return driver;
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
            tlDriver.remove();
        }
    }
}
