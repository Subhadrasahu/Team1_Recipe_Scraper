package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecipeDetailPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public RecipeDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void extractRecipeDetails() {
        try {
            // Extract recipe title
            WebElement titleElement = driver.findElement(By.xpath("//h4[@class='rec-heading']/span"));
            String recipeTitle = titleElement.getText().trim();
            System.out.println("Recipe Title: " + recipeTitle);

            // Extract ingredients
            List<WebElement> ingredients = driver.findElements(By.xpath("//div[@id='ingredients']//p"));
            System.out.println("Ingredients:");
            for (WebElement ingredient : ingredients) {
                String text = ingredient.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("- " + text);
                }
            }

            // Extract preparation time
            List<WebElement> prepTimeElements = driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Preparation Time')]]//p/strong"));
            System.out.println("Preparation Time:");
            for (WebElement prepTime : prepTimeElements) {
                String text = prepTime.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("- " + text);
                }
            }

            // Extract cooking time
            List<WebElement> cookTimeElements = driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Cooking Time')]]//p/strong"));
            System.out.println("Cooking Time:");
            for (WebElement cookTime : cookTimeElements) {
                String text = cookTime.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("- " + text);
                }
            }

            // Extract tags
            List<WebElement> tagElements = driver.findElements(By.xpath("//div[@class='col-md-12']/ul[@class='tags-list']/li"));
            System.out.println("Tags:");
            for (WebElement tag : tagElements) {
                String text = tag.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("- " + text);
                }
            }

            // Extract nutrient values
            List<WebElement> nutrientElements = driver.findElements(By.xpath("//div[@id='rcpnuts']"));
            System.out.println("Nutrient Values:");
            for (WebElement nutrient : nutrientElements) {
                String text = nutrient.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("- " + text);
                }
            }

            // Extract recipe URL and ID
            String recipeUrl = driver.getCurrentUrl();
            System.out.println("Recipe URL: " + recipeUrl);
            String[] parts = recipeUrl.split("-");
            String[] recipeArr = parts[parts.length - 1].split("r");
            if (recipeArr.length > 0) {
                System.out.println("Recipe ID: " + recipeArr[0]);
            }

            System.out.println("*********************");

        } catch (Exception e) {
            System.out.println("Error extracting recipe details: " + e.getMessage());
        }
    }
}
