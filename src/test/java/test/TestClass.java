package test;

import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scrapper.RecipeClass;
import utils.ExcelReader;

public class TestClass {
    private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

    public static void main(String[] args) throws InterruptedException {
        DriverFactory.init_driver("chrome");
        WebDriver driver = DriverFactory.getDriver();
        Map<String, List<String>> filterMap = ExcelReader.readExcelData();
        String[] ingredientsToAdd = filterMap.get(ExcelReader.LFV_Add).toArray(new String[0]);
        String[] ingredientsToEliminate = filterMap.get(ExcelReader.LFV_Eliminate).toArray(new String[0]);
        try {
            driver.get("https://www.tarladalal.com/recipes/");
            WebElement paginationElement = driver.findElement(By.xpath("//ul[@class=\"pagination justify-content-center align-items-center\"]/li[position() = last() - 1]/a"));
            int totalPages = Integer.parseInt(paginationElement.getText());
         logger.info("totalPages to: " + totalPages);
            ArrayList<RecipeClass> eliminatedRecipes = new ArrayList<>();
            ArrayList<RecipeClass> addedRecipes = new ArrayList<>();


            for (int o = 315; o <= totalPages; o++) {
                String url = "https://www.tarladalal.com/recipes/?page=" + o;

                driver.get(url);
                logger.info("url: " + url);
                List<WebElement> recipeElements = driver.findElements(By.xpath("(//div[contains(@class, 'recipe-list d-flex flex-wrap')])[1]/div"));
                System.out.println("recipeElements size: " + recipeElements.size());
                for (int i = 0; i < recipeElements.size(); i++) {
                    // Re-fetch the elements (important after navigation or click)
                    WebElement element = recipeElements.get(i).findElement(By.tagName("a")); // Assuming the clickable part is <a>
                    String recipeName = element.getText();
                    String href = element.getAttribute("href");
                    System.out.println("Navigating to: " + recipeName + " | " + href);
                    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", href);
                }
                // Switch to each tab(recipe) and scrape data
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
                String parentTab = driver.getWindowHandle();
                for (String tab : tabs) {
                    if (!tab.equals(parentTab)) {
                        driver.switchTo().window(tab);
                        try {
                            List<WebElement> ingredients = driver.findElements(By.xpath("//div[@id='ingredients']//p"));
                            WebElement recipeHeadingTitle = driver.findElement(By.xpath("//h4[@class='rec-heading']/span"));
                            WebElement preparationTime = driver.findElement(By.xpath("//div[@class='content'][.//h6[contains(text(),'Preparation Time')]]//p/strong"));


                            String recipeUrl = driver.getCurrentUrl();
                            logger.info(recipeUrl);
                            //Split the URL by hyphen and 'r' to get the parts
                            String[] parts = recipeUrl.split("-");
                            // The recipe ID is the last part before 'r'
                            String[] recipeArr = parts[parts.length - 1].split("r");
//                            System.out.println("*********************");
//                            System.out.println("Recipe Id : " + recipeArr[0]);
//                            System.out.println("Recipe Heading/Title:" + recipeHeadingTitle.getText().trim());
//
//                            System.out.println("Preparation Time:"+ preparationTime.getText().trim());


                            boolean isEliminated = false;
                            boolean isAdded = false;

                            StringBuffer buffer = new StringBuffer();
                            for (WebElement item : ingredients) {
                                String text = item.getText().trim();

                                if (!text.isEmpty()) {
                                    buffer.append(text).append(", ");

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
                            RecipeClass recipe = new RecipeClass();
                            recipe.setIngredients(buffer.toString());
                            recipe.setRecipeID(recipeArr[0]);
                            recipe.setRecipeURL(recipeUrl);
                            recipe.setRecipeName(recipeHeadingTitle.getText());
                            recipe.setPreparation_Time(preparationTime.getText());
                            logger.info("Ingredients:" + buffer.toString());
                            if (isEliminated) {
                                eliminatedRecipes.add(recipe);
                                logger.info(" This recipe is NOT allowed (contains eliminated items)");
                            } else {
//                                if (isAdded) {
                                    addedRecipes.add(recipe);
                                    logger.info(" This recipe is ALLOWED");
//                                }
                            }


                        } catch (Exception e) {
                            System.out.println("Error scraping data from tab: " + tab);
                        }
                        // Close the current tab
                    }

                }
                System.out.println("EliminatedRecipesSize" + eliminatedRecipes.size());
                System.out.println("AddedRecipesSize" + addedRecipes.size());

                System.out.println("EliminatedRecipes" + eliminatedRecipes);
                System.out.println("AddedRecipes" + addedRecipes);
            }


        } finally {

            driver.quit();


        }


    }

}
