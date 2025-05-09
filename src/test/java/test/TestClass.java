package test;

import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import driverManager.DriverFactory;
import scrapper.BaseClass;
import scrapper.LFV_Elimination;
import scrapper.RecipeInfoScrap;


public class TestClass extends BaseClass {
	DriverFactory driverFactory;
	BaseClass baseClass;
	LFV_Elimination elemination;
	

	@BeforeClass
	public void initializationSetUp() {
		driverFactory = new DriverFactory();
		driverFactory.init_driver("chrome");
		baseClass = new BaseClass();
		baseClass.lunchBrowser();
	}

	@Test
	public void testFinal() {
		RecipeInfoScrap scrapinfo = new RecipeInfoScrap(DriverFactory.getDriver());
		scrapinfo.read_LFV_Elimination_Excel();
		scrapinfo.read_LFV_AddItems_Excel();
		scrapinfo.LfvRecipe();
		
	}

}
