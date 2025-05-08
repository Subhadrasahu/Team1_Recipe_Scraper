package test;

import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import pages.HomePage;
import utils.AdHandler;
import java.time.Duration;
import java.util.List;

public class AllRecipesList {
    WebDriver driver;
    HomePage homePage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.initDriver();
        driver.get(utils.ConfigReader.get("baseUrl"));
    }

    @BeforeMethod
    public void handleAds() {
        AdHandler.handleAds(driver);  // Handle ads before each test
    }

    @Test(priority=1)
    public void scrapeRecipes() throws InterruptedException {
        homePage = new HomePage(driver);

        // Handling any ads or popups before interacting with the page
        handleAds();

          
        homePage.categoriesDropdownLink();
        homePage.viewAllCategoryClick();
        homePage.healthyIndianBreakfastRcpClick();
        homePage.getAllCategoryLinks();
        
    }
    
}
        
      /*  // Now scrape all recipes from the category
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<String> allRecipeUrls = new java.util.ArrayList<>();

        while (true) {
            // Wait for recipe cards to be present
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div/h5[@class='mb-0 two-line-text']/a")));

            // Get all recipe links on the current page
            List<WebElement> recipeLinks = driver.findElements(By.xpath("//div/h5[@class='mb-0 two-line-text']/a"));

            for (WebElement link : recipeLinks) {
                String recipeURL = link.getAttribute("href");
                allRecipeUrls.add(recipeURL);
                System.out.println(recipeURL);  // Or write to DB
            }

            // Try to find the "Next" button
            List<WebElement> nextBtn = driver.findElements(By.xpath("//a[text()='Next']"));

            if (!nextBtn.isEmpty() && nextBtn.get(0).isDisplayed()) {
                nextBtn.get(0).click();
                Thread.sleep(2000); // Better to use wait later
            } else {
                break;  // No more pages
            }
        }

        System.out.println("Total recipes collected: " + allRecipeUrls.size());
        
              
    }*/
    