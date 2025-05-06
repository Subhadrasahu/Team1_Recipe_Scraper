package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import driverManager.DriverFactory;
import utils.AdHandler;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeScraperTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();
        driver.get(utils.ConfigReader.get("baseUrl"));
    }

    @BeforeMethod
    public void handleAds() {
        AdHandler.handleAds(driver);  // Handle ads before each test
    }

    @Test(priority = 1)
    public void scrapeRecipes() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Set<String> allRecipeUrls = new HashSet<>();

        while (true) {
            // Wait for recipe links to be present
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div/h5[@class='mb-0 two-line-text']/a")));

            // Collect recipe links from the current page
            List<WebElement> recipeLinks = driver.findElements(By.xpath("//div/h5[@class='mb-0 two-line-text']/a"));
            for (WebElement link : recipeLinks) {
                String recipeURL = link.getAttribute("href");
                if (allRecipeUrls.add(recipeURL)) {
                    System.out.println(recipeURL); // Only print if new
                }
            }

            // Check and click "Next" if available
            List<WebElement> nextBtn = driver.findElements(By.xpath("//a[text()='Next']"));
            if (!nextBtn.isEmpty()) {
                WebElement nextButton = nextBtn.get(0);

                // Scroll and wait until clickable
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
                wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();

                // Wait for the next set of recipe links to become visible
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div/h5[@class='mb-0 two-line-text']/a")));
                
                // Sleep to allow the page to settle after navigation
                try {
                    Thread.sleep(2000);  // A small pause to ensure page loads completely
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break; // No more pages
            }
        }

        System.out.println("Total unique recipes collected: " + allRecipeUrls.size());
    }

    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
