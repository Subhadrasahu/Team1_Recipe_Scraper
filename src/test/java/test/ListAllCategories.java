package test;

import driverManager.DriverFactory;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.CategoriesPage;
import pages.HomePage;
import utils.AdHandler;


public class ListAllCategories {
    WebDriver driver;
    HomePage homePage;
    CategoriesPage cp;

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
    public void scrapeRecipes() throws InterruptedException {
        cp = new CategoriesPage(driver);
        homePage = new HomePage(driver);

        homePage.categoriesDropdownLink();
        homePage.viewAllCategoryClick();
        cp.healthyIndianBreakfastRcpClick();

        List<WebElement> recipes = cp.getRecipeLinks();
        Assert.assertTrue(recipes.size() > 0, "No recipes found on first page.");
        
        // Start scraping recipes with pagination and ingredients
        cp.scrapeAllRecipesWithPagination();
        System.out.println("Extracting all recipes under category with pagination...");
                
		/*
		 * cp.clickEachRecipeAndExtractDetails();
		 * System.out.println("Click Recipe link");
		 */
    }
}

		