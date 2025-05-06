package scrapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import driverManager.DriverFactory;

public class LFV_Elimination extends DriverFactory {
	//private WebDriver driver;
	WebDriverWait wait;
	

	public LFV_Elimination(WebDriver driver) {
		PageFactory.initElements(driver,this);
	}

	public void selectCategory() {
		//driver = DriverFactory.getDriver();
		/*driver.findElement(By.xpath("//ul[@class='navbar-nav d-flex flex-row mob-hide']//a[@role='button'][normalize-space()='Categories'")).click();
		System.out.println("****Category option available*************");
		CheckAdvertisement();
		driver.findElement(By.xpath("//ul[@class='dropdown-menu show']//a[text()='View All Category']")).click();
		System.out.println("Showing view all catagory********");*/
		
		driver.findElement(By.xpath("//ul[@class='navbar-nav d-flex flex-row mob-hide']//a[@role='button'][normalize-space()='Categories']")).click();
		System.out.println("Category clicked...");
		CheckAdvertisement();
		driver.findElement(By.xpath("//ul[@class='dropdown-menu show']//a[text()='View All Category']")).click();
		System.out.println("view all category is clicked...");
		CheckAdvertisement();
	}

	public void getCategory() {
		//CheckAdvertisement();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordionExample")));
		List<WebElement> recipeCategory = driver.findElements(By.xpath("//a[contains(@href, 'recipes-for')]"));
		List<WebElement> recipesViewAll = driver.findElements(By.xpath("//a[text()='View All']"));
		List<String> categorylinks = new ArrayList<>();
		for (WebElement link : recipesViewAll) {
			
			categorylinks.add(link.getAttribute("href"));
		}
		System.out.println("******** Found " + recipeCategory.size() + " recipe categories.");
		//for (String categoryLink : categorylinks) {
		for (int i = 4; i < 8; i++) {
		    String categoryLink = categorylinks.get(i);
			CheckAdvertisement();
			System.out.println("**************** New category is started ***************");
			
			 driver.get(categoryLink);
			
			CheckAdvertisement();
			//WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[@class='row recipe-list d-flex flex-wrap']")));
			List<WebElement> insideCategoryrecipes = driver
					.findElements(By.xpath("//div[@class='col-md-12']//div[@class='overlay-content']//a"));
			System.out.println("******* Inside this category " + insideCategoryrecipes.size() + " recipes are present.");
			for (WebElement insideCategoryRecipeLink : insideCategoryrecipes) {
				String recipeUrl = insideCategoryRecipeLink.getAttribute("href");
				System.out.println(insideCategoryRecipeLink.getText() + " -> " + recipeUrl);
				scrapeRecipe(insideCategoryRecipeLink.getText(), recipeUrl);
		}
			CheckAdvertisement();
			driver.navigate().back();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordionExample")));
			CheckAdvertisement();
		}
	}
	public void scrapeRecipe(String recipe_name, String url) {
		driver.get(url);
		CheckAdvertisement();
		//WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h5"))); // Recipe name
		String recipeId = url.replaceAll("\\D+", "");
		System.out.println("ID: " + recipeId);
		String recipeName = recipe_name;
		 System.out.println("Name: " + recipeName);
		 driver.navigate().back();
			CheckAdvertisement();
			WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait1.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[@class='row recipe-list d-flex flex-wrap']")));
			CheckAdvertisement();
	}
	
	
	public void CheckAdvertisement() {
		try {
		    
		    WebElement closeAd = wait.until(ExpectedConditions.elementToBeClickable(
		        By.cssSelector("div[role='dialog'] button, .close-button, .dismiss-button")
		    ));
		    closeAd.click();
		} catch (Exception e) {
		    System.out.println("No ad overlay found.");
		}

}
}
