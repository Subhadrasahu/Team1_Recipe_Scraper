package scrapper;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import driverManager.DriverFactory;
import utils.DatabaseUtil;
import utils.ExcelReaderCode;

public class RecipeInfoScrap extends DriverFactory {
	List<String> LFV_EliminateItemList = new ArrayList<String>();
	List<String> LFV_AddItemList = new ArrayList<String>();

	// constructor
	public RecipeInfoScrap(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public void scrapeRecipe(String url, String ingredients,String flag) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h5"))); // Recipe name
		String recipeId = url.replaceAll("\\D+", ""); // crude way to extract digits
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[@class='rec-heading']/span")));
		String recipeName = driver.findElement(By.xpath("//h4[@class='rec-heading']/span")).getText();

		List<WebElement> timeList = driver.findElements(By.xpath("//div[@class='content']/p/strong"));
		String prepTime = timeList.get(0).getText();
		String cookingTime = timeList.get(1).getText();
		String makesServings = timeList.get(2).getText();
		// fetching recipe tags
		List<WebElement> tagElements = driver.findElements(By.xpath("//ul[@class='tags-list']/li/a"));
		// Collect texts into a list
		List<String> tagTexts = new ArrayList<>();
		for (WebElement tag : tagElements) {
			tagTexts.add(tag.getText());
		}
		// Join the tag texts into one string separated by commas (or any delimiter)
		String tags = String.join(", ", tagTexts);
		// **************Recipe category**********************
		String rec_Category = "";
		if (tags.toLowerCase().contains("breakfast")) {
			rec_Category = "Breakfast";
		} else if (tags.toLowerCase().contains("dinner")) {
			rec_Category = "Dinner";
		} else if (tags.toLowerCase().contains("snack")) {
			rec_Category = "Snacks";
		} else if (tags.toLowerCase().contains("lunch")) {
			rec_Category = "Lunch";
		} else
			rec_Category = "Not mentioned";

		// retrive foodCategory******************
		String foodCategory = "";
		if (tags.toLowerCase().contains("vegan")) {
			foodCategory = "Vegan";
		} else if (tags.toLowerCase().contains("vegetarian")) {
			foodCategory = "vegetarian";
		} else if (tags.toLowerCase().contains("jain")) {
			foodCategory = "jain";
		} else if (tags.toLowerCase().contains("eggitarian")) {
			foodCategory = "eggitarian";
		} else if (tags.toLowerCase().contains("non-veg") || tags.toLowerCase().contains("non veg")) {
			foodCategory = "non-veg";
		} else
			foodCategory = "Not mentioned";
		// *****************CuisineCatagory***************
		List<String> cusineList = new ArrayList<String>();

		ExcelReaderCode reader = new ExcelReaderCode(
				"./src/test/resources/testData/Recipe-filters-ScrapperHackathon.xlsx");
		Boolean sheetCheck = reader.isSheetExist("Food Category");
		for (int i = 2; i <= 32; i++) {
			String testData = reader.getCellData("Food Category", 1, i);
			cusineList.add(testData.toLowerCase());

		}
		// fetching Cuisine category
		String cuisineCategory = "";
		for (String cuisine : cusineList) {
			if (tags.toLowerCase().contains(cuisine.toLowerCase())) {
				cuisineCategory = cuisine;
				break;

			} else {
				cuisineCategory = "Indian";
			}
		}

		// ***************METHOD***********************
		List<String> methodTextList = new ArrayList<>();
		String methodListString = String.join(",", methodTextList);
		List<WebElement> method = driver.findElements(By.xpath("//div[@id='methods']//ol/li"));
		for (WebElement methodText : method) {
			methodTextList.add(methodText.getText());
		}
		// **********NUTRIENT VALUES***************

		String nutrientEnergy = driver.findElement(By.xpath("//td[normalize-space()='Energy']")).getText();
		String nutrientEnergyValue = driver.findElement(By.xpath("//span[@itemprop='calories']")).getText();
		String nutrientValues = nutrientEnergy + nutrientEnergyValue;
		// *******RECIPE DESCRIPTION**********

		List<WebElement> descriptionEle = driver
				.findElements(By.xpath("//div[@class='recipesection']/ol[@class='rcpprocsteps']//p"));
		List<String> description = new ArrayList<>();
		for (WebElement desc : descriptionEle) {
			description.add(desc.getText());
		}
		String recipeDescription = String.join(", ", description);

		// ********************Finish*******************8
		System.out.println("ID: " + recipeId);
		System.out.println("Name: " + recipeName);
		System.out.println("RecipeCategory: " + rec_Category);
		System.out.println("FoodCategory: " + foodCategory);
		System.out.println("Ingredients: " + ingredients);
		System.out.println("Prep Time: " + prepTime);
		System.out.println("Cooking Time: " + cookingTime);
		System.out.println("Tag: " + tags);
		System.out.println("Servings: " + makesServings);
		System.out.println("CuisineCategory" + cuisineCategory);
		System.out.println("RecipeDescription: " + recipeDescription);
		System.out.println("PreparationMethod: " + methodListString);
		System.out.println("NutrientValues: " + nutrientValues);
		System.out.println("RecipeUrl: " + url);

		System.out.println("-----------------------------------------------------");
		if(flag.equals("Eliminate")) {
		DatabaseUtil.insertRecipe(recipeId,recipeName,rec_Category,foodCategory,ingredients,prepTime,cookingTime,tags,makesServings,cuisineCategory,recipeDescription,methodListString,nutrientValues,url);
		}
		else if(flag.equals("Add")) {
			DatabaseUtil.insertRecipeAdd(recipeId,recipeName,rec_Category,foodCategory,ingredients,prepTime,cookingTime,tags,makesServings,cuisineCategory,recipeDescription,methodListString,nutrientValues,url);
			
		}
		CheckAdvertisement();
	}
		
	public String getIngredientString(List<String> ingredientsList) {
		String ingredients = String.join(",", ingredientsList);
		return ingredients;
	}

	public List<String> getIngredients() {
		List<String> ingredientsList = new ArrayList<>();
		try {

			List<WebElement> ingredientElements = driver.findElements(By.xpath("//div[@id='ingredients']//p//span"));
			for (WebElement element : ingredientElements) {
				ingredientsList.add(element.getText().trim());
			}
		} catch (Exception e) {
			System.out.println("Error scraping ingredients: " + e.getMessage());
		}
		return ingredientsList;
	}

	public void read_LFV_Elimination_Excel() {
		ExcelReaderCode reader = new ExcelReaderCode(
				"./src/test/resources/testData/IngredientsAndComorbidities-ScrapperHackathon.xlsx");
		Boolean sheetCheck = reader.isSheetExist("Final list for LFV Elimination ");
		System.out.println("Is the Datasheet exist? -  " + sheetCheck);
		int eliminateRowCount = reader.getRowCount("Final list for LFV Elimination ") - 14; // as last 13 rows are empty
		System.out.println("Eliminate Row Count: " + eliminateRowCount);
		for (int i = 3; i <= eliminateRowCount; i++) {
			String testData = reader.getCellData("Final list for LFV Elimination ", 0, i);
			LFV_EliminateItemList.add(testData.toLowerCase());

		}
		System.out.println("Total eliminate items are: " + LFV_EliminateItemList.size());
	}

	public void read_LFV_AddItems_Excel() {
		ExcelReaderCode reader = new ExcelReaderCode(
				"./src/test/resources/testData/IngredientsAndComorbidities-ScrapperHackathon.xlsx");
		Boolean sheetCheck = reader.isSheetExist("Final list for LFV Elimination ");
		System.out.println("Is the Datasheet exist? -  " + sheetCheck);
		int addRowCount = reader.getRowCount("Final list for LFV Elimination ");
		System.out.println("Row Count of Add : " + addRowCount);

		for (int i = 3; i <= addRowCount; i++) {
			String testData = reader.getCellData("Final list for LFV Elimination ", 1, i);
			LFV_AddItemList.add(testData.toLowerCase());
		}
		System.out.println("Total Add items are: " + LFV_AddItemList.size());
	}

	public void LfvRecipe() {
		driver.get("https://www.tarladalal.com/recipe-category/");
		try {
			DatabaseUtil.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accordionExample")));
		List<WebElement> recipeCategory = driver.findElements(By.xpath("//a[contains(@href, 'recipes-for')]"));
		List<WebElement> recipesViewAll = driver.findElements(By.xpath("//a[text()='View All']"));
		List<String> categorylinks = new ArrayList<>();
		for (WebElement link : recipesViewAll) {
			categorylinks.add(link.getAttribute("href"));
		}
		System.out.println("******** Found " + recipeCategory.size() + " recipe categories.");
		// for (String categoryLink : categorylinks)
		for (int i = 5; i < 6; i++) {
			String categoryLink = categorylinks.get(i);
			CheckAdvertisement();
			System.out.println("**************** New category is started ***************");

			driver.get(categoryLink);

			CheckAdvertisement();

			WebDriverWait wait_1 = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait_1.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[@class='row recipe-list d-flex flex-wrap']")));
			int numberofPages;
			WebDriverWait wait_2 = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait_2.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//nav[@class='d-flex justify-content-center align-items-center mt-3 shadow-none']")));
			int totalsize = driver
					.findElements(By.xpath("//ul[@class='pagination justify-content-center align-items-center']/li"))
					.size();
			int pages = totalsize - 2;
			if (pages < 6) {
				numberofPages = totalsize - 2;
			} else {
				String element = driver
						.findElement(
								By.xpath("//ul[@class='pagination justify-content-center align-items-center']/li[7]/a"))
						.getText();
				numberofPages = Integer.parseInt(element);
			}
			System.out.println("Total pages are: " + numberofPages);

			for (int pg = 2; pg <= numberofPages; pg++) {

				CheckAdvertisement();
				WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait1.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//div[@class='row recipe-list d-flex flex-wrap']")));
				List<WebElement> insideCategoryrecipes = driver
						.findElements(By.xpath("//div[@class='col-md-12']//div[@class='overlay-content']//a"));
				System.out.println("*************** Inside this category " + insideCategoryrecipes.size()
						+ " recipes are present.****************");
				ArrayList<String> links = new ArrayList<>(25);
				for (WebElement insideCategoryRecipeLink : insideCategoryrecipes) {
					String recipeUrl = insideCategoryRecipeLink.getAttribute("href");
					// System.out.println(insideCategoryRecipeLink.getText() + " -> " + recipeUrl);
					links.add(recipeUrl);
				}
				for (String eachRecipe : links) {
					System.out.println(eachRecipe);
					((JavascriptExecutor) driver).executeScript("window.location.href='" + eachRecipe + "';");

					List<String> ingredientsList = getIngredients();
					CheckAdvertisement();

					String ingredients = getIngredientString(ingredientsList);
					// System.out.println(ingredients);
//			   if(ingredientsList.size()==0) {
//					continue;
//				}

					boolean validRecipe = true;
					for (String eliminateItem : LFV_EliminateItemList) {
						if (ingredients.toLowerCase().contains(eliminateItem.toLowerCase())) {

							validRecipe = false;
							break;
						}
					}
					boolean hasAdd = false;
					for (String a : LFV_AddItemList) {
						if (ingredients.contains(a)) {
							hasAdd = true;
							break;
						}
					}
					
					if (validRecipe) {
						System.out.println("************** Scrape this recipe. ******************");
						scrapeRecipe(eachRecipe, ingredients,"Eliminate");
					}
	        if (validRecipe && hasAdd) {
	        	
					System.out.println("************** Scrape this recipe. for ADD ******************");
					scrapeRecipe(eachRecipe, ingredients,"Add");
				}
					driver.navigate().back();
					CheckAdvertisement();
					WebDriverWait wait5 = new WebDriverWait(driver, Duration.ofSeconds(5));
					wait5.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//div[@class='row recipe-list d-flex flex-wrap']")));

				}

				// driver.navigate().back();
				CheckAdvertisement();

				try {

					WebElement page2Link = driver
							.findElement(By.xpath("//a[@class='page-link' and text()='" + pg + "']"));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", page2Link);
					System.out.println("********** Current page is: " + pg + "  *********");
					CheckAdvertisement();

				} // end of try
				catch (StaleElementReferenceException e) {
				}

			} // end of pagination
			CheckAdvertisement();

		}
	}

	public void CheckAdvertisement() {
		try {
			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement closeAd = wait2.until(ExpectedConditions
					.elementToBeClickable(By.cssSelector("div[role='dialog'] button, .close-button, .dismiss-button")));
			closeAd.click();
		} catch (TimeoutException e) {
			//System.out.println("No ad overlay found.");
		}
	}

}
