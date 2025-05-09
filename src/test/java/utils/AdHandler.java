package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdHandler {

    // Method to handle and close ads
    public static void handleAds(WebDriver driver) {
        try {
            // Handling pop-ups or overlays
            try {
                WebElement closeButton = driver.findElement(By.xpath("//button[contains(@class, 'close')]"));
                closeButton.click();
            } catch (NoSuchElementException e) {
                // Ignore if close button is not found
            }

            // Handling ads inside an iframe
            try {
                WebElement iframe = driver.findElement(By.cssSelector("iframe.ad-frame"));
                driver.switchTo().frame(iframe);
                WebElement closeButtonInIframe = driver.findElement(By.xpath("//button[@class='close']"));
                closeButtonInIframe.click();
                driver.switchTo().defaultContent();
            } catch (NoSuchElementException e) {
                // Ignore if iframe or ad is not found
            }

            // Handle dynamic ads or overlay popups
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement adCloseButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'close')]")));
                adCloseButton.click();
            } catch (TimeoutException e) {
                // Ignore if no ad appears within the timeout
            }

            // Handle new window popups (if they appear as separate windows)
            handlePopUpAds(driver);

        } catch (Exception e) {
            System.out.println("No ads were found.");
        }
    }

    // Method to handle new window popups (ads as new windows)
    private static void handlePopUpAds(WebDriver driver) {
        String mainWindow = driver.getWindowHandle();

        // Handle multiple windows
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindow)) {
                driver.switchTo().window(windowHandle);
                driver.close();
            }
        }

        // Switch back to the main window
        driver.switchTo().window(mainWindow);
    }
}
