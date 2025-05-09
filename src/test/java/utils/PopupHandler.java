package utils;


	import org.openqa.selenium.Alert;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.NoAlertPresentException;

	public class PopupHandler {

	    // Method to close pop-ups using JavaScript (e.g., close modal dialogs or unwanted pop-ups)
	    public static void closePopupsWithJavaScript(WebDriver driver) {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.onbeforeunload = function() {};"); // Disable any confirmation pop-ups
	        js.executeScript("window.close();"); // Close any opened popup window (if it's a new tab)
	    }

	    // Method to handle alert pop-ups (OK/Cancel pop-ups)
	    public static void handleAlertPopups(WebDriver driver) {
	        try {
	            Alert alert = driver.switchTo().alert();
	            alert.accept();  // Accept the alert (click OK)
	        } catch (NoAlertPresentException e) {
	            // No alert is present, nothing to handle
	        }
	    }

	    // Method to block pop-ups before navigation (set this in browser options)
	    public static void blockPopupsInBrowser(WebDriver driver) {
	        // Example for Chrome, but you can also use this for other browsers like Firefox
	        if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
	            // Add any pop-up blocking logic here
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("window.onbeforeunload = function() {};"); // Disable prompt before unload
	        }
	        
	        
	    }
	    
	    public static void closeJavaScriptPopups(WebDriver driver) {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.onbeforeunload = function(){};");
	    }
	}



