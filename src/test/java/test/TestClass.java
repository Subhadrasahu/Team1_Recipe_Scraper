package test;
import driverManager.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;
import scrapper.BaseClass;

public  class  TestClass {

    public static void main(String[] args) throws InterruptedException {
       DriverFactory.init_driver("chrome");
       WebDriver driver= DriverFactory.getDriver();
        BaseClass baseClass;

        try{
        driver.get("https://www.tarladalal.com/recipes/category/Healthy-Low-Calorie-Weight-Loss/");
        // Find all clickable elements under the XPath
        List<WebElement> recipeElements = driver.findElements(By.xpath("//main/section/div[2]/div/div[1]/div/div"));
            System.out.println("recipeElements size: " + recipeElements.size());
        for (int i = 22; i < recipeElements.size(); i++) {
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
                        List<WebElement> recipeHeadingTitle= driver.findElements(By.xpath("//h4[@class='rec-heading']/span\n"));
                        List<WebElement> preparationTime= driver.findElements(By.xpath("//div[@class='content'][.//h6[contains(text(),'Preparation Time')]]//p/strong"));

                        System.out.println("Ingredients:");
                        for (WebElement item : ingredients) {
                            String text = item.getText().trim();
                            if (!text.isEmpty()) {
                                System.out.println("- " + text);
                            }
                        }
                        System.out.println("Recipe Heading/Title:");
                        for (WebElement heading : recipeHeadingTitle) {
                            String recipeHeading = heading.getText().trim();
                            if (!recipeHeading.isEmpty()) {
                                System.out.println("- " + recipeHeading);
                            }
                        }

                        System.out.println("Preparation Time:");
                        for (WebElement time : preparationTime) {
                            String prepTime = time.getText().trim();
                            if (!prepTime.isEmpty()) {
                                System.out.println("- " + prepTime);
                            }
                        }
                        String recipeUrl = driver.getCurrentUrl();
                        System.out.println(recipeUrl);
                        //Split the URL by hyphen and 'r' to get the parts
                        String[] parts = recipeUrl.split("-");
                        // The recipe ID is the last part before 'r'
                        String[]  recipeArr = parts[parts.length - 1].split("r");
                        System.out.println("*********************");
                        System.out.println("Recipe Id : " + recipeArr[0]);
                    } catch (Exception e) {
                        System.out.println("Error scraping data from tab: " + tab);
                    }
                    driver.close(); // Close the current tab
                }
            }
        }
        finally{

            driver.quit();

        }

    }

}
