package test;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseClass;
import driverManager.DriverFactory;
import pages.CategoriesPage;

public class ListAllRecipes extends BaseClass {
	WebDriver driver;
	CategoriesPage cp;
	
	   @BeforeClass
	    public void setUp() {
	        driver = DriverFactory.initDriver();
	        driver.get(utils.ConfigReader.get("baseUrl"));
	    }

	
	 @Test(priority = 1)
	 public void scrapeRecipes() throws InterruptedException {
		 cp = new CategoriesPage(driver);
		 cp.CatagoriesDropdownLink();
		 cp.viewAllCategoryClick();
		 cp.HEALTHY_Heart_RecipesClick();
		 
		List<WebElement> recipes = cp.getRecipeLinks();
	    Assert.assertTrue(recipes.size() > 0, "No recipes found on first page.");
	    cp.scrapeAllRecipesWithPagination();
	  System.out.println(recipes.size());
	    System.out.println("Extracting all recipes under category with pagination...");
	 }

}
