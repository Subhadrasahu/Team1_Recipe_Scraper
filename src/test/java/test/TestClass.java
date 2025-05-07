package test;
import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scrapper.BaseClass;
import scrapper.RecipeClass;
import utils.ExcelReader;

public  class  TestClass {

    public static void main(String[] args) throws InterruptedException {
        DriverFactory.init_driver("chrome");
        WebDriver driver = DriverFactory.getDriver();

        String[] ingredientsToAdd = {"Fish",
                "prawn",
                "poultry",
                "egg",
                "Onion",
                "Garlic",
                "turmeric",
                "Ginger",
                "Butter",
                "ghee",
                "hard cheese",
                "paneer",
                "cottage cheese",
                "sour cream",
                "greek yogurt",
                "hung curd",
                "almond",
                "pistachio",
                "brazil nut",
                "walnut",
                "pine nut",
                "hazelnut",
                "macadamia nut",
                "pecan",
                "hemp seed",
                "sunflower seed",
                "sesame seed",
                "chia seed",
                "flax seed",
                "Blueberry",
                "blackberry",
                "strawberry"};
        String[] ingredientsToEliminate = {
                "Ham", "sausage", "tinned fish", "tuna", "sardines",
                "yams",
                "beets",
                "parsnip",
                "turnip",
                "rutabagas",
                "carrot",
                "yuca",
                "kohlrabi",
                "celery root",
                "horseradish",
                "daikon",
                "jicama",
                "radish",
                "pumpkin",
                "squash",
                "Whole fat milk",
                "low fat milk",
                "fat free milk",
                "Evaporated milk",
                "condensed milk",
                "curd",
                "buttermilk",
                "ice cream",
                "flavored milk",
                "sweetened yogurt",
                "soft cheese",
                "grain",
                "Wheat",
                "oat",
                "barely",
                "rice",
                "millet",
                "jowar",
                "bajra",
                "corn",
                "dal",
                "lentil",
                "banana",
                "mango",
                "papaya",
                "plantain",
                "apple",
                "orange",
                "pineapple",
                "pear",
                "tangerine",
                "all melon varieties",
                "peach",
                "plum",
                "nectarine",
                "Avocado",
                "olive oil",
                "coconut oil",
                "soybean oil",
                "corn oil",
                "safflower oil",
                "sunflower oil",
                "rapeseed oil",
                "peanut oil",
                "cottonseed oil",
                "canola oil",
                "mustard oil",
                "sugar",
                "jaggery",
                "glucose",
                "fructose",
                "corn syrup",
                "cane sugar",
                "aspartame",
                "cane solids",
                "maltose",
                "dextrose",
                "sorbitol",
                "mannitol",
                "xylitol",
                "maltodextrin",
                "molasses",
                "brown rice syrup",
                "splenda",
                "nutra sweet",
                "stevia",
                "barley malt",
                "potato",
                "corn",
                "pea"
        };

        try {

            driver.get("https://www.tarladalal.com/recipes/");
            WebElement paginationElement = driver.findElement(By.xpath("//ul[@class=\"pagination justify-content-center align-items-center\"]/li[position() = last() - 1]/a"));
            int totalPages = Integer.parseInt(paginationElement.getText());
            System.out.println("totalPages to: " + totalPages);
            ArrayList<RecipeClass> eliminatedRecipes = new ArrayList<>();
            ArrayList<RecipeClass> addedRecipes = new ArrayList<>();


            for (int o = 315; o <= totalPages; o++) {
                String url = "https://www.tarladalal.com/recipes/?page=" + o;

                driver.get(url);
                System.out.println("url: " + url);

                List<WebElement> recipeElements = driver.findElements(By.xpath("(//div[contains(@class, 'recipe-list d-flex flex-wrap')])[1]/div"));
                // List<WebElement> recipeElements = driver.findElements(By.xpath("//main/section/div[2]/div/div[1]/div/div"));

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
                            System.out.println(recipeUrl);
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
                            System.out.println("Ingredients:" + buffer.toString());
                            if (isEliminated) {
                                eliminatedRecipes.add(recipe);
                                System.out.println(" This recipe is NOT allowed (contains eliminated items)");
                            } else {
                                if (isAdded) {
                                    addedRecipes.add(recipe);
                                    System.out.println(" This recipe is ALLOWED");
                                }
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
