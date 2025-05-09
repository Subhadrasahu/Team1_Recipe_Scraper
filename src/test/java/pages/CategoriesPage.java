package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoriesPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final int TIMEOUT = 15;

    public CategoriesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    // Locators
    private static final By CATEGORIES_LINK = By.linkText("Categories");
    private static final By VIEW_ALL_CATEGORY = By.linkText("View All Category");
    private static final By HEALTHY_BREAKFAST_VIEW_ALL = By.xpath(
        "//div[@class='recipe-block brd-radius-5 mb-3']//h5/a[text()='Healthy Indian Breakfast']" +
        "/ancestor::div[@class='details d-flex justify-content-between align-items-center brd-radius-5']" +
        "//a[text()='View All']"
    );
    private static final By RECIPE_LINKS = By.xpath("//div[contains(@class,'recipe-block')]//h5[contains(@class,'two-line-text')]/a");
    private static final By NEXT_BUTTON = By.linkText("Next");

    // Utility
    private void clickElementWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void categoriesDropdownLink() {
        wait.until(ExpectedConditions.elementToBeClickable(CATEGORIES_LINK)).click();
    }

    public void viewAllCategoryClick() {
        wait.until(ExpectedConditions.elementToBeClickable(VIEW_ALL_CATEGORY)).click();
    }

    public void healthyIndianBreakfastRcpClick() {
        try {
            System.out.println("Clicking 'Healthy Indian Breakfast' recipe...");
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(HEALTHY_BREAKFAST_VIEW_ALL));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                clickElementWithJS(element);
                System.out.println("Used JS click for intercepted element.");
            }
        } catch (TimeoutException e) {
            System.err.println("Element not clickable: " + HEALTHY_BREAKFAST_VIEW_ALL);
            throw e;
        }
    }

    public List<WebElement> getRecipeLinks() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
    }

    public boolean isNextPageAvailable() {
        try {
            WebElement nextBtn = wait.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON));
            return nextBtn.isDisplayed() && nextBtn.isEnabled();
        } catch (NoSuchElementException | TimeoutException e) {
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

    public void waitUntilRecipesLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
        } catch (TimeoutException te) {
            System.out.println("Timeout waiting for recipes on next page.");
        }
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
                          
                       //recipe  ingredients extraction code 
                            
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
                                System.out.println("Ingredients:");
                                for (WebElement item : ingredients) {
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
                                String currentUrl = driver.getCurrentUrl();
                                System.out.println("Recipe_URL: " + currentUrl);
                                String[] parts = currentUrl.split("-");
                                String[] recipeArr = parts[parts.length - 1].split("r");
                                System.out.println("Recipe ID: " + recipeArr[0]);
                                System.out.println("*******************************");
                            } catch (Exception e) {
                                System.out.println("Error scraping recipe: " + e.getMessage());
                            }
                            // Close tab and switch back
                            driver.close();
                            driver.switchTo().window(parentTab);
                            
                            //#############################################################                      
                            
                                     
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
    }
}



















































/*
 * package pages;
 * 
 * import java.time.Duration; import java.util.HashSet; import java.util.List;
 * import java.util.Set;
 * 
 * import org.openqa.selenium.*; import
 * org.openqa.selenium.support.ui.ExpectedConditions; import
 * org.openqa.selenium.support.ui.WebDriverWait;
 * 
 * public class CategoriesPage {
 * 
 * private WebDriver driver; private WebDriverWait wait; private static final
 * int TIMEOUT = 15;
 * 
 * public CategoriesPage(WebDriver driver) { this.driver = driver; this.wait =
 * new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT)); }
 * 
 * // Locators private static final By CATEGORIES_LINK =
 * By.linkText("Categories"); private static final By VIEW_ALL_CATEGORY =
 * By.linkText("View All Category"); private static final By
 * HEALTHY_BREAKFAST_VIEW_ALL = By.xpath(
 * "//div[@class='recipe-block brd-radius-5 mb-3']//h5/a[text()='Healthy Indian Breakfast']"
 * +
 * "/ancestor::div[@class='details d-flex justify-content-between align-items-center brd-radius-5']"
 * + "//a[text()='View All']" ); private static final By RECIPE_LINKS =
 * By.xpath(
 * "//div[contains(@class,'recipe-block')]//h5[contains(@class,'two-line-text')]/a"
 * ); private static final By NEXT_BUTTON = By.linkText("Next");
 * 
 * // Utility private void clickElementWithJS(WebElement element) {
 * ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
 * element); }
 * 
 * private void scrollToBottom() { ((JavascriptExecutor)
 * driver).executeScript("window.scrollTo(0, document.body.scrollHeight);"); }
 * 
 * public void categoriesDropdownLink() {
 * wait.until(ExpectedConditions.elementToBeClickable(CATEGORIES_LINK)).click();
 * }
 * 
 * public void viewAllCategoryClick() {
 * wait.until(ExpectedConditions.elementToBeClickable(VIEW_ALL_CATEGORY)).click(
 * ); }
 * 
 * public void healthyIndianBreakfastRcpClick() { try {
 * System.out.println("Clicking 'Healthy Indian Breakfast' recipe...");
 * WebElement element =
 * wait.until(ExpectedConditions.elementToBeClickable(HEALTHY_BREAKFAST_VIEW_ALL
 * )); ((JavascriptExecutor)
 * driver).executeScript("arguments[0].scrollIntoView(true);", element); try {
 * element.click(); } catch (ElementClickInterceptedException e) {
 * clickElementWithJS(element);
 * System.out.println("Used JS click for intercepted element."); } } catch
 * (TimeoutException e) { System.err.println("Element not clickable: " +
 * HEALTHY_BREAKFAST_VIEW_ALL); throw e; } }
 * 
 * public List<WebElement> getRecipeLinks() { return
 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
 * }
 * 
 * public boolean isNextPageAvailable() { try { WebElement nextBtn =
 * wait.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON)); return
 * nextBtn.isDisplayed() && nextBtn.isEnabled(); } catch (NoSuchElementException
 * | TimeoutException e) { return false; } }
 * 
 * public void navigateToNextPage() { try { WebElement nextBtn =
 * wait.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
 * nextBtn.click(); } catch (Exception e) {
 * System.out.println("Could not click Next: " + e.getMessage()); throw e; } }
 * 
 * public void waitUntilRecipesLoad() { try {
 * wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(RECIPE_LINKS));
 * } catch (TimeoutException te) {
 * System.out.println("Timeout waiting for recipes on next page."); } }
 * 
 * public void scrapeAllRecipesWithPagination() { boolean hasNextPage = true;
 * int pageCount = 1; int totalRecipes = 0; Set<String> uniqueRecipeUrls = new
 * HashSet<>();
 * 
 * while (hasNextPage) { System.out.println("Scraping Page: " + pageCount); try
 * { scrollToBottom(); Thread.sleep(1000); // Allow lazy-loaded content
 * 
 * List<WebElement> recipes = getRecipeLinks();
 * 
 * for (WebElement recipe : recipes) { try { String recipeName =
 * recipe.getText().trim(); String recipeUrl =
 * recipe.getAttribute("href").trim();
 * 
 * if (uniqueRecipeUrls.add(recipeUrl)) { System.out.println("Recipe: " +
 * recipeName + " | URL: " + recipeUrl); totalRecipes++; } else {
 * System.out.println("Duplicate skipped: " + recipeUrl); } } catch
 * (StaleElementReferenceException e) {
 * System.out.println("Stale element encountered. Skipping..."); } }
 * 
 * if (isNextPageAvailable()) { navigateToNextPage(); waitUntilRecipesLoad();
 * pageCount++; } else { hasNextPage = false;
 * System.out.println("Reached last page."); } } catch (Exception e) {
 * System.out.println("Unexpected error: " + e.getMessage());
 * e.printStackTrace(); hasNextPage = false; } }
 * 
 * System.out.println("Scraping completed. Total unique recipes scraped: " +
 * totalRecipes);
 * 
 * extractIngredientsFromRecipeLinks(uniqueRecipeUrls); }
 * 
 * 
 * public void extractIngredientsFromRecipeLinks(Set<String> recipeUrls) { By
 * ingredientsLocator =
 * By.xpath("//div[contains(@id,'rcpinglist') or contains(@class,'ingrelist')]"
 * );
 * 
 * int count = 1; for (String recipeUrl : recipeUrls) { try {
 * driver.get(recipeUrl); System.out.println("\n[" + count++ +
 * "] Navigating to: " + recipeUrl);
 * 
 * // Wait for ingredients section WebElement ingredientsSection =
 * wait.until(ExpectedConditions.presenceOfElementLocated(ingredientsLocator));
 * 
 * List<WebElement> ingredients =
 * ingredientsSection.findElements(By.tagName("li"));
 * 
 * System.out.println("Ingredients:"); for (WebElement ing : ingredients) {
 * String ingredient = ing.getText().trim(); if (!ingredient.isEmpty()) {
 * System.out.println(" - " + ingredient); } }
 * 
 * // Optional: short sleep for human-like delay Thread.sleep(500);
 * 
 * } catch (Exception e) { System.err.println("Failed to extract from: " +
 * recipeUrl); e.printStackTrace(); } } }
 * 
 * }
 */