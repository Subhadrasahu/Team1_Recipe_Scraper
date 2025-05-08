package pages;

import java.time.Duration;
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
