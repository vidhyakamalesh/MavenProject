package com.maveric.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

public class WDHelper {
	static WebDriver driver;
	static WebDriverWait wait;
	public static void initialize(String browser) {
		try {
		switch(browser.toLowerCase())
		{
		case "ff":
			FirefoxOptions fo = new FirefoxOptions();
			fo.setAcceptInsecureCerts(true);
			System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
			driver = new FirefoxDriver(fo);
			break;
		case "gc": case "chrome":
			ChromeOptions co = new ChromeOptions();
			co.setAcceptInsecureCerts(true);
			System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
			driver = new ChromeDriver(co);
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			break;
		default:
			ChromeOptions nco = new ChromeOptions();
			nco.setAcceptInsecureCerts(true);
			System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
			driver = new ChromeDriver(nco);
			break;
		}
		ATUReports.setWebDriver(driver);
		wait = new WebDriverWait(driver,60);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		ATUReports.add(browser+" browser launch",LogAs.PASSED, new CaptureScreen(ScreenshotOf.DESKTOP));
	}
		catch(Exception e)
		{
			ATUReports.add(browser+" browser launch",LogAs.FAILED, new CaptureScreen(ScreenshotOf.DESKTOP));
		}
	}
	
	public static void navigate(String url) {
		if(!url.startsWith("http"))
		{
			url = "http:\\"+url;
		}
		ATUReports.add("URL Before Navigateion",driver.getCurrentUrl(), LogAs.INFO,null);
		ATUReports.add("Navigate",url,LogAs.INFO,null);
		driver.get(url);
		ATUReports.add("Navigation Successful",driver.getCurrentUrl(),LogAs.PASSED,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
	}
	
	public static WebElement frame(By loc) {
		WebElement e=null;
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		List<WebElement> frames = driver.findElements(By.tagName("frame"));
		if(frames.size()==0)
		{
			frames = driver.findElements(By.tagName("iframe"));
		}
		for(WebElement f:frames)
		{
			if(driver.switchTo().frame(f).findElements(loc).size()>0)
			{
				e = driver.findElement(loc);
				driver.switchTo().defaultContent();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				break;
			}
			driver.switchTo().defaultContent();
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return e;
	}
	
	public static WebElement findElement(By loc) {
		WebElement e=null;
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(loc));
			wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
			e = driver.findElement(loc);
			ATUReports.add("locate element "+loc.toString(), LogAs.PASSED, null);
		}
		catch(Exception ex)
		{
			try
			{
				e = frame(loc);
				ATUReports.add("locate element "+loc.toString()+ "on frame", LogAs.PASSED, null);
			}
			catch(Exception ex1) {
				ATUReports.add("search for element "+loc.toString(), LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			}
		}
		if(e==null)
		{
			ATUReports.add("Search for element "+loc.toString(), LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
		return e;
	}
	
	public static void click(By loc) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(findElement(loc)));
			findElement(loc).click();
			ATUReports.add("click element "+loc.toString(), LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
		/*catch(StaleElementReferenceException e)
		{
			driver.navigate().refresh();
			click(loc);
		}*/
		catch(Exception e)
		{
			try {
			JavascriptExecutor ex = (JavascriptExecutor) driver;
			ex.executeScript("argument[0].click()", findElement(loc));
			ATUReports.add("click element "+loc.toString()+" by javascript", LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			}
			catch(Exception ex)
			{
				ATUReports.add("click element "+loc.toString(), LogAs.FAILED, null);
			}
		}
	}
	
	public static void type(By loc, String value) {
		try {
			findElement(loc).clear();
			findElement(loc).sendKeys(value);
			ATUReports.add("type text into "+loc.toString(),value, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
		catch(Exception e) {
			ATUReports.add("type text into "+loc.toString(),value, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
	}
	
	public static void selectByValue(By loc, String value) {
		Select sel = new Select(findElement(loc));
		sel.selectByValue(value);
		ATUReports.add("select by value into "+loc.toString(),value, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
	}
	
	public static void selectByText(By loc, String value) {
		Select sel = new Select(findElement(loc));
		sel.selectByVisibleText(value);
		ATUReports.add("select by text into "+loc.toString(),value, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
	}
	
	public static void selectByIndex(By loc, int index) {
		Select sel = new Select(findElement(loc));
		sel.selectByIndex(index);
		ATUReports.add("select by index into "+loc.toString(),Integer.toString(index), LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
	}
	
	public static void assertText(By loc, String text) {
		try {
			wait.until(ExpectedConditions.textToBe(loc, text));
			ATUReports.add("Text Assertion", text, findElement(loc).getText(),LogAs.PASSED,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
		catch(Exception e) {
			ATUReports.add("Text Assertion", text, findElement(loc).getText(),LogAs.FAILED,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
	}
	public static void assertTitle(String text) {
		try {
			wait.until(ExpectedConditions.titleContains(text));
			ATUReports.add("Text Assertion", text, driver.getTitle(),LogAs.PASSED,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
		catch(Exception e) {
			ATUReports.add("Text Assertion", text, driver.getTitle(),LogAs.FAILED,new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		}
	}
	
	public static void quit() {
		driver.quit();
		ATUReports.add("Closed browser", LogAs.PASSED,new CaptureScreen(ScreenshotOf.DESKTOP));
	}
}
