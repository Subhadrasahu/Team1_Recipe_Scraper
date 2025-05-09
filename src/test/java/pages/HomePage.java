package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.ConfigReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Initialize WebDriverWait
    }


    private By categoriesLink = By.linkText("Categories");
    public By viewAllCategory = By.linkText("View All Category");
    private By allCategories = By.xpath("//h5[@class='mb-0 two-line-text']/a");
   // private By healthyIndianBreakfastRcp = By.xpath("//div[@class='details d-flex justify-content-between align-items-center brd-radius-5'][.//h5/a[text()='Healthy Indian Breakfast']]//a[text()='View All']");
    
    // Click on categories dropdown
    public void categoriesDropdownLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(categoriesLink));
        link.click();
    }

    public void viewAllCategoryClick() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(viewAllCategory));
        link.click();
    }
    
    
    public void healthyIndianBreakfastRcpClick() {
        // XPath for the 'View All' link related to the 'Healthy Indian Breakfast' recipe
        By viewAllLink = By.xpath("//div[@class='recipe-block brd-radius-5 mb-3']//h5/a[text()='Healthy Indian Breakfast']/ancestor::div[@class='details d-flex justify-content-between align-items-center brd-radius-5']//a[text()='View All']");

        try {
            System.out.println("Clicking 'Healthy Indian Breakfast' recipe...");

            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(viewAllLink));
            
            // Scroll the element into view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            System.out.println("Element is now in view.");

            try {
                // Attempt to click the element
                element.click();
                System.out.println("'Healthy Indian Breakfast' recipe clicked.");
            } catch (ElementClickInterceptedException e) {
                // If normal click fails, use JavaScript to click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                System.out.println("Element click intercepted. Retrying with JavaScript.");
            }
        } catch (TimeoutException e) {
            // If the element is not clickable after the wait period
            System.err.println("Element not clickable: " + viewAllLink.toString());
            throw e;
        }
    
    }
    
    //all category links : 
    
    public List<String> getAllCategoryLinks() {
        String categoryUrl = ConfigReader.get("baseUrl") + "/RecipeCategories.aspx"; // or correct it to the real one if needed
        driver.get(categoryUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            wait.until(ExpectedConditions.titleContains("Recipe categories")); // adjust the title match if needed
            List<WebElement> categoryElements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//a[contains(@href,'recipes-for')]")));

            List<String> categoryLinks = new ArrayList<>();

            for (WebElement element : categoryElements) {
                String url = element.getAttribute("href");
                if (url != null && url.contains("recipes-for")) {
                    categoryLinks.add(url.trim());
                }
            }

            System.out.println("Total categories found: " + categoryLinks.size());
            return categoryLinks;

        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for the category page to load. Current title: " + driver.getTitle());
            Assert.fail("Category page did not load within the expected time. Title: " + driver.getTitle(), e);
            return Collections.emptyList(); // Will not be reached if Assert.fail throws
        }
    }

    
	/*
	 * public List<String> getAllCategoryLinks() { //String categoryUrl =
	 * ConfigReader.get("baseUrl") + "/RecipeCategories.aspx"; // Ensure baseUrl =
	 * https://m.tarladalal.com or https://www.tarladalal.com String categoryUrl =
	 * ConfigReader.get("baseUrl") + "/recipe-category/"; driver.get(categoryUrl);
	 * 
	 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	 * 
	 * try { wait.until(ExpectedConditions.titleContains("Recipe categories"));
	 * List<WebElement> categoryElements = wait.until(
	 * ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
	 * "//a[contains(@href,'recipes-for')]")));
	 * 
	 * List<String> categoryLinks = new ArrayList<>();
	 * 
	 * for (WebElement element : categoryElements) { String url =
	 * element.getAttribute("href"); if (url != null && url.contains("recipes-for"))
	 * { categoryLinks.add(url.trim()); } }
	 * 
	 * System.out.println("Total categories found: " + categoryLinks.size()); return
	 * categoryLinks;
	 * 
	 * } catch (TimeoutException e) { System.err.
	 * println("Timeout waiting for the category page to load. Current title: " +
	 * driver.getTitle()); return Collections.emptyList(); } }
	 */
	/*
	 * public List<String> getAllCategoryLinks() {
	 * driver.get("https://www.tarladalal.com/RecipeCategories.aspx");
	 * 
	 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	 * 
	 * // Wait for a stable element on the page to appear
	 * wait.until(ExpectedConditions.titleContains("Recipe categories"));
	 * 
	 * // Wait for category links to be visible
	 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
	 * "//div[@class='resp_left_content']//a")));
	 * 
	 * List<WebElement> categoryElements =
	 * driver.findElements(By.xpath("//div[@class='resp_left_content']//a"));
	 * List<String> categoryLinks = new ArrayList<>();
	 * 
	 * for (WebElement element : categoryElements) { String url =
	 * element.getAttribute("href"); if (url != null && !url.trim().isEmpty()) {
	 * categoryLinks.add(url); } }
	 * 
	 * // Retrieve all recipe links from each category List<String> allRecipeLinks =
	 * new ArrayList<>();
	 * 
	 * for (String categoryLink : categoryLinks) {
	 * allRecipeLinks.addAll(getAllRecipesFromCategory(categoryLink)); }
	 * 
	 * return allRecipeLinks; }
	 */

        private List<String> getAllRecipesFromCategory(String categoryLink) {
            List<String> recipeLinks = new ArrayList<>();
            
            // Visit the category page
            driver.get(categoryLink);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait for the page to load and for recipe links to be visible
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='m_img_div']//a")));
            
            // Extract all recipe links from the first page of the category
            List<WebElement> recipeElements = driver.findElements(By.xpath("//div[@class='m_img_div']//a"));
            
            for (WebElement recipe : recipeElements) {
                String recipeUrl = recipe.getAttribute("href");
                if (recipeUrl != null && !recipeUrl.trim().isEmpty()) {
                    recipeLinks.add(recipeUrl);
                }
            }

            // Now, check for pagination and keep navigating until we reach the last page
            boolean hasNextPage = true;
            while (hasNextPage) {
                // Check if there's a "Next" button for pagination
                WebElement nextPageButton = driver.findElement(By.xpath("//a[@class='next']"));
                
                if (nextPageButton != null && nextPageButton.isDisplayed()) {
                    nextPageButton.click();
                    wait.until(ExpectedConditions.stalenessOf(recipeElements.get(0))); // Wait for the page to reload
                    
                    // Extract new recipe links from the next page
                    recipeElements = driver.findElements(By.xpath("//div[@class='m_img_div']//a"));
                    for (WebElement recipe : recipeElements) {
                        String recipeUrl = recipe.getAttribute("href");
                        if (recipeUrl != null && !recipeUrl.trim().isEmpty()) {
                            recipeLinks.add(recipeUrl);
                        }
                    }
                } else {
                    hasNextPage = false; // No more pages, exit the loop
                }
            }

            return recipeLinks;
        }
        
    }
    
/*
 * public List<String> getAllCategoryLinks() {
 * driver.get("https://www.tarladalal.com/RecipeCategories.aspx");
 * 
 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
 * 
 * // Wait for a stable element on the page to appear
 * wait.until(ExpectedConditions.titleContains("Recipe categories"));
 * 
 * // Then wait for the actual category links
 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
 * "//div[@class='resp_left_content']//a")));
 * 
 * List<WebElement> categoryElements =
 * driver.findElements(By.xpath("//div[@class='resp_left_content']//a"));
 * List<String> categoryLinks = new ArrayList<>();
 * 
 * for (WebElement element : categoryElements) { String url =
 * element.getAttribute("href"); if (url != null && !url.trim().isEmpty()) {
 * categoryLinks.add(url); } }
 * 
 * return categoryLinks; }
 * 
 * // Method to scrape all recipes under a category (with pagination) public
 * List<String> scrapeRecipesUnderCategory(String categoryUrl) {
 * driver.get(categoryUrl); // Navigate to the category page
 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.
 * cssSelector("div#recipes_list .rcc_recipecard")));
 * 
 * List<String> recipeLinks = new ArrayList<>();
 * 
 * while (true) { List<WebElement> recipes = driver.findElements(By.
 * cssSelector("div#recipes_list .rcc_recipecard .rcc_recipename a")); for
 * (WebElement recipe : recipes) { String href = recipe.getAttribute("href"); if
 * (href != null && !href.isEmpty()) { recipeLinks.add(href); } }
 * 
 * try { WebElement nextButton =
 * driver.findElement(By.xpath("//a[text()='Next']")); if
 * (nextButton.isDisplayed() && nextButton.isEnabled()) { ((JavascriptExecutor)
 * driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
 * nextButton.click();
 * wait.until(ExpectedConditions.stalenessOf(recipes.get(0))); // Wait until the
 * page refreshes } else { break; } } catch (NoSuchElementException e) { break;
 * // No more pages or next button } }
 * 
 * return recipeLinks; } }
 * 
 */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
	/*
	 * public List<String> getAllCategoryLinks() {
	 * driver.get("https://www.tarladalal.com/RecipeCategories.aspx");
	 * 
	 * WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	 * 
	 * // Wait for a stable element on the page to appear
	 * wait.until(ExpectedConditions.titleContains("Recipe categories"));
	 * 
	 * // Then wait for the actual category links
	 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
	 * "//div[@class='resp_left_content']//a")));
	 * 
	 * List<WebElement> categoryElements =
	 * driver.findElements(By.xpath("//div[@class='resp_left_content']//a"));
	 * List<String> categoryLinks = new ArrayList<>();
	 * 
	 * for (WebElement element : categoryElements) { String url =
	 * element.getAttribute("href"); if (url != null && !url.trim().isEmpty()) {
	 * categoryLinks.add(url); } }
	 * 
	 * return categoryLinks; }
	 * 
	 * public List<String> scrapeAllRecipeLinksFromCurrentCategory() { List<String>
	 * recipeLinks = new ArrayList<>();
	 * 
	 * while (true) {
	 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.
	 * cssSelector("div#recipes_list .rcc_recipecard")));
	 * 
	 * List<WebElement> recipes = driver.findElements(By.
	 * cssSelector("div#recipes_list .rcc_recipecard .rcc_recipename a")); for
	 * (WebElement recipe : recipes) { String href = recipe.getAttribute("href"); if
	 * (href != null && !href.isEmpty()) { recipeLinks.add(href); } }
	 * 
	 * try { WebElement nextBtn =
	 * driver.findElement(By.xpath("//a[@class='respglink' and text()='Next']")); if
	 * (nextBtn.isDisplayed() && nextBtn.isEnabled()) { ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
	 * nextBtn.click(); wait.until(ExpectedConditions.stalenessOf(recipes.get(0)));
	 * } else { break; } } catch (NoSuchElementException e) { break; } }
	 * 
	 * return recipeLinks; }
	 * 
	 * //Get All Category links
	 * 
	 */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

	/*
	 * public void healthyIndianBreakfastRcpClick() { try {
	 * wait.until(ExpectedConditions.elementToBeClickable(healthyIndianBreakfastRcp)
	 * ).click(); } catch (org.openqa.selenium.StaleElementReferenceException e) {
	 * // Retry once if stale element error occurs
	 * wait.until(ExpectedConditions.refreshed(
	 * ExpectedConditions.elementToBeClickable(healthyIndianBreakfastRcp)
	 * )).click(); } }
	 * 
	 */
    
	/*
	 * public void healthyIndianBreakfastRcpClick() { // By viewAllLink = By.
	 * xpath("//a[text()='Healthy Indian Breakfast']/ancestor::div[contains(@class,'details')]//a[text()='View All']"
	 * ); By viewAllLink = By.
	 * xpath("//div[@class='recipe-block brd-radius-5 mb-3']//h5/a[text()='Healthy Indian Breakfast']/ancestor::div[@class='details d-flex justify-content-between align-items-center brd-radius-5']//a[text()='View All']"
	 * ); try { System.out.println("Clicking 'Healthy Indian Breakfast' recipe");
	 * WebElement element =
	 * wait.until(ExpectedConditions.elementToBeClickable(viewAllLink));
	 * ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView(true);", element);
	 * 
	 * try { element.click(); } catch (ElementClickInterceptedException e) {
	 * ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
	 * element); } } catch (TimeoutException e) {
	 * System.err.println("Element not clickable: " + viewAllLink.toString()); throw
	 * e; } }
	 */
	 

    

