package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelReader;
import test.RecipeClass;
public class CategoriesPage {
	private static final Logger logger = LoggerFactory.getLogger(CategoriesPage.class);
	private WebDriver driver;
	private WebDriverWait wait;
	private static final int TIMEOUT =15;
	 Map<String, List<String>> filterMap = ExcelReader.readExcelData();
     String[] ingredientsToAdd = filterMap.get(ExcelReader.LFV_Add).toArray(new String[0]);
     String[] ingredientsToEliminate = filterMap.get(ExcelReader.LFV_Eliminate).toArray(new String[0]);
     ArrayList<RecipeClass> eliminatedRecipes = new ArrayList<>();
     ArrayList<RecipeClass> addedRecipes = new ArrayList<>();
	//Constructor
	public CategoriesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

	//Locators
	private static By CATEGORIES_LINK = By.xpath("//ul[@class='navbar-nav d-flex flex-row mob-hide']//a[@role='button'][normalize-space()='Categories']");
	private static  By VIEW_ALL_CATEGORY = By.xpath("//ul[@class='dropdown-menu show']//a[text()='View All Category']");
	private static  By HEALTHY_Heart_Recipes=By.xpath("//*[@id=\"collapse476\"]/div/div/div[12]/div/div[2]/div/div[2]/a");
	private static  By RECIPE_LINKS = By.xpath("//div[contains(@class,'recipe-block')]//h5[contains(@class,'two-line-text')]/a");
	private static  By NEXT_BUTTON = By.linkText("Next");
	 
	// Utility
	    private void clickElementWithJS(WebElement element) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	    }

	    private void scrollToBottom() {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    }
	    
	    
	    public void CatagoriesDropdownLink() {
	    	wait.until(ExpectedConditions.elementToBeClickable(CATEGORIES_LINK)).click();
	    	System.out.println("All categories");
	    }

	    public void viewAllCategoryClick() {
	        wait.until(ExpectedConditions.elementToBeClickable(VIEW_ALL_CATEGORY)).click();
	        System.out.println("Clicking view all' recipe...");
	    }
	    
	    public void HEALTHY_Heart_RecipesClick() {
	        wait.until(ExpectedConditions.elementToBeClickable(HEALTHY_Heart_Recipes)).click();
	        System.out.println("Clicking 'Healthy Heart ' recipe...");
	    }
	    
	    public List<WebElement> getRecipeLinks() {
	        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
	    }
	    
	    
	    //For window handles
//	    public void ClickingEachRecipeAndScrapingAllDetails() {
//	    	  // Switch to each recipe and scrape data
//            ArrayList<String> newRecipetabs = new ArrayList<>(driver.getWindowHandles());
//            String parentTab = driver.getWindowHandle();
//            for (String tab : newRecipetabs) {
//                if (!tab.equals(parentTab)) {
//                    driver.switchTo().window(tab);
//                    try {
//                        List<WebElement> ingredients = driver.findElements(By.xpath("//div[@id='ingredients']//p"));
//                        List<WebElement> recipeHeadingTitle= driver.findElements(By.xpath("//h4[@class='rec-heading']/span\n"));
//                        List<WebElement> preparationTime= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Preparation Time')]]//p/strong"));
//                        List<WebElement> cookingTime= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Cooking Time')]]//p/strong"));
//                        List<WebElement> tagLists= driver.findElements(By.xpath("//div[@class='col-md-12']/ul[@class='tags-list']/li"));
//                        List<WebElement> NutrientValues= driver.findElements(By.xpath("//div[@id='rcpnuts']"));
//                        List<WebElement> Preparation_method= driver.findElements(By.xpath("//div[@class='methods']"));
//                        List<WebElement> Recipe_Description= driver.findElements(By.xpath("//div[@id='aboutrecipe']"));
//                    
//                        System.out.println("Recipe Name/Title:");
//                        for (WebElement heading : recipeHeadingTitle) {
//                            String recipeHeading = heading.getText().trim();
//                            if (!recipeHeading.isEmpty()) {
//                                System.out.println("- " + recipeHeading);
//                            }
//                        }
//
//                        System.out.println("Preparation Time:");
//                        for (WebElement time : preparationTime) {
//                            String prepTime = time.getText().trim();
//                            if (!prepTime.isEmpty()) {
//                                System.out.println("- " + prepTime);
//                            }
//                        }
//                        
//                     System.out.println("Cooking Time:");
//                     for (WebElement time : cookingTime) {
//                         String cookTime = time.getText().trim();
//                         if (!cookTime.isEmpty()) {
//                             System.out.println("- " + cookTime);
//                         }
//                     }
//                     
//                     System.out.println("Tags are:");
//                        for (WebElement tag : tagLists) {
//                            String text = tag.getText().trim();
//                            if (!text.isEmpty()) {
//                                System.out.println("- " + text);
//                            }
//                        }
//                        
//                     System.out.println("Nutrient values are:");
//                     for (WebElement nutrient : NutrientValues) {
//                         String text = nutrient.getText().trim();
//                         if (!text.isEmpty()) {
//                             System.out.println("- " + text);
//                         }
//                     }
//                     
//                     System.out.println("Preparation methods are:");
//                     for (WebElement method : Preparation_method) {
//                         String text = method.getText().trim();
//                         if (!text.isEmpty()) {
//                             System.out.println("- " + text);
//                         }
//                     }
//                     
//                     System.out.println("Recipe descriptions are:");
//                     for (WebElement description : Recipe_Description) {
//                         String text = description.getText().trim();
//                         if (!text.isEmpty()) {
//                             System.out.println("- " + text);
//                         }
//                     }
//                     System.out.println("Recipe_URL:");
//                        String recipeUrl = driver.getCurrentUrl();
//                        System.out.println(recipeUrl);
//                        //Split the URL by hyphen and 'r' to get the parts
//                        String[] parts = recipeUrl.split("-");
//                        // The recipe ID is the last part before 'r'
//                        String[]  recipeArr = parts[parts.length - 1].split("r");
//                        System.out.println("*********************");
//                        System.out.println("Recipe Id : " + recipeArr[0]);
//                        
//                        //elimination/ADD code for filtering from excel
//                        boolean isEliminated = false;
//                        boolean isAdded = false;
//                        
//                        StringBuffer buffer = new StringBuffer();
//                        System.out.println("Ingredients:");
//                        for (WebElement item : ingredients) {
//                            String text = item.getText().trim();
//                            if (!text.isEmpty()) {
//                              //  System.out.println("- " + text);
//                            	 buffer.append(text).append(", ");
//
//                                 for (String eliminated : ingredientsToEliminate) {
//                                     if (text.toLowerCase().contains(eliminated.toLowerCase())) {
//                                         isEliminated = true;
//                                         break;
//                                     }
//                                 }
//                                 for (String added : ingredientsToAdd) {
//                                     if (text.toLowerCase().contains(added.toLowerCase())) {
//                                         isAdded = true;
//                                         break;
//                                     }
//                                 }
//                            }
//                        }
//                        
//                        RecipeClass recipe = new RecipeClass();
//                        recipe.setIngredients(buffer.toString());
//                        recipe.setRecipeID(recipeArr[0]);
//                        recipe.setRecipeURL(recipeUrl);
//                        recipe.setRecipeName(recipeHeadingTitle.getText());
//                        recipe.setPreparation_Time(preparationTime.getText());
//                        logger.info("Ingredients:" + buffer.toString());
//                        if (isEliminated) {
//                            eliminatedRecipes.add(recipe);
//                            logger.info(" This recipe is NOT allowed (contains eliminated items)");
//                        } else {
////                            if (isAdded) {
//                                addedRecipes.add(recipe);
//                                logger.info(" This recipe is ALLOWED");
////                            }
//                        }
//                        
//                    } catch (Exception e) {
//                        System.out.println("Error scraping data from tab: " + tab);
//                    }
//                    driver.close(); // Close the current tab
//                
//                }
//                // Switch back to the main listing tab
//                driver.switchTo().window(parentTab);
//            }
//	    }
//	    
//	    
	    
	    public boolean isNextPageAvailable() throws TimeoutException {
	        try {
	           // WebElement nextBtn = wait.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON));
	        	 WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	             WebElement nextBtn = shortWait.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON));
	           // return nextBtn.isDisplayed() && nextBtn.isEnabled();
	             
	             if (nextBtn.isDisplayed()&& nextBtn.isEnabled()) {
	                 return true;  // true if clickable, false if disabled
	             } else {
	                 return false;
	             }
	        } catch (NoSuchElementException e) {
	            return false;
	        }
	    }
	    
	    public void navigateToNextPage() {
	        try {
	            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
	            nextBtn.click();
	        } catch (Exception e) {
	            System.out.println("Could not click Next: " + e.getMessage());
	            throw e;
	        }
	    }
	    public void waitUntilRecipesLoad() throws TimeoutException {
	        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
	    }
	    
	    public void scrapeAllRecipesWithPagination() {
	        boolean hasNextPage = true;
	        int pageCount = 1;
	        int totalRecipes = 0;
	        Set<String> uniqueRecipeUrls = new HashSet<>();

	        while (hasNextPage) {
	            System.out.println("Scraping Page: " + pageCount);
	            try {
	                scrollToBottom();
	                Thread.sleep(1000); // Allow lazy-loaded content

	                List<WebElement> recipes = getRecipeLinks();

	                for (WebElement recipe : recipes) {
	                    try {
	                        String recipeName = recipe.getText().trim();
	                        String recipeUrl = recipe.getAttribute("href").trim();

	                        if (uniqueRecipeUrls.add(recipeUrl)) {
	                            System.out.println("Recipe: " + recipeName + " | URL: " + recipeUrl);
	                           
	                          //To scrape the ingredients of each recipe
	                            System.out.println(".####################o scrape the ingredients of each recipe###..");
	                            //#######################################################################
	                            
	                         // Open recipe in new tab
	                            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", recipeUrl);

	                            // Switch to the new tab
	                            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
	                            String parentTab = driver.getWindowHandle();
	                            driver.switchTo().window(tabs.get(tabs.size() - 1));

	                            try {
	                                // SCRAPE RECIPE DETAILS HERE
	                                List<WebElement> ingredients = driver.findElements(By.xpath("//div[@id='ingredients']//p"));
	                                List<WebElement> recipeHeadingTitle= driver.findElements(By.xpath("//h4[@class='rec-heading']/span"));
	                                List<WebElement> preparationTime= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Preparation Time')]]//p/strong"));
	                                List<WebElement> cookingTime= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Cooking Time')]]//p/strong"));
	                                List<WebElement> tagLists= driver.findElements(By.xpath("//div[@class='col-md-12']/ul[@class='tags-list']/li"));
	                                List<WebElement> NutrientValues= driver.findElements(By.xpath("//div[@id='rcpnuts']"));
	                                List<WebElement> Preparation_method= driver.findElements(By.xpath("//div[@class='methods']"));
	                                List<WebElement> Recipe_Description= driver.findElements(By.xpath("//div[@id='aboutrecipe']"));
	                                List<WebElement> No_Of_servings= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Makes')]]//p/strong"));
	                                List<WebElement> recipecategory= driver.findElements(By.xpath("//div[@class='col-md-9 content-body']/p/span[4]/a"));
	                                List<WebElement> cusinecategory= driver.findElements(By.xpath("//div[@class='col-md-9 content-body']/p/span[3]/a"));
	                                
	                                
	                                System.out.println("cusinecategory:");
	                                for (WebElement item : cusinecategory) {
	                                    String text = item.getText().trim();
	                                    if (!text.isEmpty()) System.out.println("- " + text);
	                                }
	                                
	                                
	                                System.out.println("recipecategory:");
	                                for (WebElement item : recipecategory) {
	                                    String text = item.getText().trim();
	                                    if (!text.isEmpty()) System.out.println("- " + text);
	                                }
	                             

	                                System.out.println("Recipe Name/Title:");
	                                for (WebElement heading : recipeHeadingTitle) {
	                                    String recipeHeading = heading.getText().trim();
	                                    if (!recipeHeading.isEmpty()) System.out.println("- " + recipeHeading);
	                                }

	                                System.out.println("Preparation Time:");
	                                for (WebElement time : preparationTime) {
	                                    String prepTime = time.getText().trim();
	                                    if (!prepTime.isEmpty()) System.out.println("- " + prepTime);
	                                }

	                                System.out.println("Cooking Time:");
	                                for (WebElement time : cookingTime) {
	                                    String cookTime = time.getText().trim();
	                                    if (!cookTime.isEmpty()) System.out.println("- " + cookTime);
	                                }

	                                System.out.println("Tags:");
	                                for (WebElement tag : tagLists) {
	                                    String text = tag.getText().trim();
	                                    if (!text.isEmpty()) System.out.println("- " + text);
	                                }

	                                System.out.println("Nutrient values:");
	                                for (WebElement nutrient : NutrientValues) {
	                                    String text = nutrient.getText().trim();
	                                    if (!text.isEmpty()) System.out.println("- " + text);
	                                }
	                                
	                                System.out.println("Preparation methods are:");
	                                for (WebElement method : Preparation_method) {
	                                    String text = method.getText().trim();
	                                    if (!text.isEmpty()) {
	                                        System.out.println("- " + text);
	                                    }
	                                }
	                                
	                                System.out.println("#####Recipe descriptions are:#####");
	                                for (WebElement description : Recipe_Description) {
	                                    String text = description.getText().trim();
	                                    if (!text.isEmpty()) {
	                                        System.out.println("- " + text);
	                                    }
	                                }
	                                
	                                System.out.println("##### No of servings are:########");
	                                for (WebElement srving : No_Of_servings) {
	                                    String text = srving.getText().trim();
	                                    if (!text.isEmpty()) {
	                                        System.out.println("- " + text);
	                                    }
	                                }


	                                String currentUrl = driver.getCurrentUrl();
	                                System.out.println("Recipe_URL: " + currentUrl);
	                                logger.info(recipeUrl);
	                                String[] parts = currentUrl.split("-");
	                                String[] recipeArr = parts[parts.length - 1].split("r");
//	                                System.out.println("Recipe ID: " + recipeArr[0]);
//	                                System.out.println("*******************************");
	                                
	                                boolean isEliminated = false;
	                                boolean isAdded = false;
	                                
	                                StringBuffer buffer = new StringBuffer();
	                                
	                                
	                                System.out.println("Ingredients:");
	                                for (WebElement item : ingredients) {
	                                    String text = item.getText().trim();
	                                    if (!text.isEmpty())
	                                    	{
	                                    	buffer.append(text).append(", ");
	                                    	//System.out.println("- " + text);
	                                    	 for (String eliminated : ingredientsToEliminate) {
	                                             if (text.toLowerCase().contains(eliminated.toLowerCase())) {
	                                                 isEliminated = true;
	                                                 break;
	                                             }
	                                         }
	                                         for (String added : ingredientsToAdd) {
	                                             if (text.toLowerCase().contains(added.toLowerCase())) {
	                                                 isAdded = true;
	                                                 break;
	                                             }
	                                         }
	                                    	
	                                    }
	                                }
	                                RecipeClass recipe1 = new RecipeClass();
	                                recipe1.setIngredients(buffer.toString());
	                                recipe1.setRecipeID(recipeArr[0]);
	                                recipe1.setRecipeURL(recipeUrl);
	                               // recipe1.setRecipeName(recipeHeadingTitle.getText());
	                               // recipe1.setPreparation_Time(preparationTime.getText());
	                                logger.info("Ingredients:" + buffer.toString());
	                                if (isEliminated) {
	                                    eliminatedRecipes.add(recipe1);
	                                    logger.info(" This recipe is NOT allowed (contains eliminated items)");
	                                } else {
//	                                    if (isAdded) {
	                                        addedRecipes.add(recipe1);
	                                        logger.info(" This recipe is ALLOWED");
//	                                    }
	                                }

	                                
	                            } catch (Exception e) {
	                                System.out.println("Error scraping recipe: " + e.getMessage());
	                            }

	                            // Close tab and switch back
	                            driver.close();
	                            driver.switchTo().window(parentTab);

	                            
	                            //####################################################################
	                            
	                            totalRecipes++;
	                          
	                            
	                        } else {
	                            System.out.println("Duplicate skipped: " + recipeUrl);
	                        }
	                    } catch (StaleElementReferenceException e) {
	                        System.out.println("Stale element encountered. Skipping...");
	                    }
	                }

	                if (isNextPageAvailable()) {
	                    navigateToNextPage();
	                    waitUntilRecipesLoad();
	                    pageCount++;
	                } else {
	                    hasNextPage = false;
	                    System.out.println("Reached last page.");
	                }
	            } catch (Exception e) {
	                System.out.println("Unexpected error: " + e.getMessage());
	                e.printStackTrace();
	                hasNextPage = false;
	            }
	        }

	        System.out.println("Scraping completed. Total unique recipes scraped: " + totalRecipes);
	        System.out.println("EliminatedRecipesSize" + eliminatedRecipes.size());
            System.out.println("AddedRecipesSize" + addedRecipes.size());

            System.out.println("EliminatedRecipes" + eliminatedRecipes);
            System.out.println("AddedRecipes" + addedRecipes);
	    }
}
