package test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import driverManager.DriverFactory;
import scrapper.BaseClass;
import scrapper.LFV_Elimination;
import scrapper.Testing;


public class TestClass extends BaseClass {
	DriverFactory driverFactory;
	BaseClass baseClass;
	LFV_Elimination elemination;
	Testing test;
	
	@BeforeClass
	public void initializationSetUp() {
		driverFactory=new DriverFactory();
		driverFactory.init_driver("chrome");
		baseClass=new BaseClass();
		baseClass.lunchBrowser();
	}
	@Test
	public void getLFVEleminationRecipe() {
		elemination=new LFV_Elimination(DriverFactory.getDriver());
		elemination.selectCategory();
		elemination.getCategory();
	}
	@Test
	public void testScrape() {
		//driverFactory=new DriverFactory();
		test=new Testing(DriverFactory.getDriver());
		test.trial2();
		
	}

}
