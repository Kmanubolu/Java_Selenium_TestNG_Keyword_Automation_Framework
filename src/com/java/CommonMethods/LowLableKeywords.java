package com.java.CommonMethods;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;


import org.json.JSONArray;
import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.java.TestSuitDriver.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

//import com.relevantcodes.extentreports.LogStatus;


public class LowLableKeywords extends TestSuitRun {
	//public static WebDriver driver;
		
	TestSuitRun Run;

	public LowLableKeywords(TestSuitRun Run){
		this.Run= Run;
	}
	
	/**Launching Browser
	 * Developed By: Santanu Ghatak
	 * @throws IOException 
	*/
	public void LaunchBrowser(String URL, String workingDirectory, ExtentTest htmlReport){
		try{
			getDriver().get(URL);
			getDriver().manage().window().maximize();
			String imagePath = Report.CaptureScreen(Run.getDriver());
			Run.htmlReport.pass("Application Launched: " + URL);
		}catch(Exception e){
			System.out.println(e.toString());
			String imagePath = Report.CaptureScreen(Run.getDriver());
			try{
				Run.htmlReport.fail("Unable to launch application : " + URL, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
			Run.stopFlag = true;
		}
	}
	
	/**Close Browser
	 * Developed By: Santanu Ghatak
	*/
	public void CloseBrowser(){
		try{
			Run.getDriver().quit();
		}catch(Exception e){
			Run.stopFlag = true;
		}
	}
	
	
	/**Creating WebElement
	 * Developed By: Santanu Ghatak
	*/
	public WebElement CreateElement(String Object, ExtentTest htmlReport){
		WebElement el = null;
		WebDriverWait wait;
		try{
			String[] Part = Object.split("\\|\\|");
			String type = Part[0];
			String property = Part[1];
			
			wait = new WebDriverWait(Run.getDriver(), Run.gTimeOut);
			
			switch (type.toLowerCase()){
			case "id":
				if(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(property))) != null){
					el = Run.getDriver().findElement(By.id(property));
				}
				break;					
			case "name":
				if(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(property))) != null){
					el = Run.getDriver().findElement(By.name(property));
				}
				break;
			case "css":
				if(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(property))) != null){
					el = Run.getDriver().findElement(By.name(property));
				}
				break;
			case "xpath":
				if(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(property))) != null){
					el = Run.getDriver().findElement(By.xpath(property));
				}
				break;
			case "class":
				if(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(property))) != null){
					el = Run.getDriver().findElement(By.className(property));
				}
				break;
			}
		}catch(Exception e){
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(Run.getDriver());
			try{
				Run.htmlReport.fail("Object dosen't Exist in the application: " + Object, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}
		return el;		
	}
	
	
	/**Checking WbElement Exist or not
	 * Developed By: Santanu Ghatak
	 */
	public void WaitForElement(String Object, ExtentTest htmlReport){
		WebDriverWait wait;
		try{
			String[] Part = Object.split("\\|\\|");
			String type = Part[0];
			String property = Part[1];
			
			wait = new WebDriverWait(Run.getDriver(), Run.gTimeOut);
			switch (type.toLowerCase()){
			case "id":
				if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(property))) != null){
				}
				break;					
			case "name":
				if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(property))) != null){
				}
				break;
			case "css":
				if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(property))) != null){
				}
				break;
			case "xpath":
				if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(property))) != null){
				}
				break;
			case "class":
				if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(property))) != null){
				}
				break;
			}
		}catch(Exception e){
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(Run.getDriver());
			try{
				Run.htmlReport.fail("Object dosen't Exist in the application: " + Object, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}		
	}
	
	public void Wait(String Value){
		try {
			Thread.sleep(Integer.parseInt(Value));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**Clicking on WebElement
	 * Developed By: Santanu Ghatak
	 */
	public void ClickElemnt(String Object, ExtentTest htmlReport){
		try{
			
			WebElement ClickObject = CreateElement(Object, htmlReport);
			if(Run.stopFlag){
				return;
			}
			ClickObject.click();
			Run.htmlReport.pass("Clicked: "+ Object);
			
		}catch(Exception e){
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(getDriver());
			try{
				Run.htmlReport.fail("Unable to Clicked: " + Object, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}
	}
	
	
	public void ClickElem(String Object, ExtentTest htmlReport){
		try{
			WebElement el = null;
			String[] Part = Object.split("\\|\\|");
			String type = Part[0];
			String property = Part[1];
			WebElement ClickObject = CreateElement(Object, htmlReport);
			el = getDriver().findElement(By.xpath(property));
			
			el.click();
			Run.htmlReport.pass("Clicked: "+ Object);
			
		}catch(Exception e){
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(getDriver());
			try{
				Run.htmlReport.fail("Unable to Clicked: " + Object, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}
	}
	
	
	/**Enter Text
	 * Developed By: Santanu Ghatak
	 */
	public void SendText(String Object, String Value, ExtentTest htmlReport){
		try{
			
			WebElement Obj = CreateElement(Object, htmlReport);
			if(Run.stopFlag){
				return;
			}
			Obj.clear();
			Obj.sendKeys(Value);
			Run.htmlReport.pass("Send Text '" + Value + "' for the field :" + Object);
			
		}catch(Exception e){
			System.out.println(e.toString());
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(getDriver());
			//Run.htmlReport.fail("Unable to Send Text '" + Value + "' for the field :" + Object);
			try{
				Run.htmlReport.fail("Unable to Send Text '" + Value + "' for the field :" + Object, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}
	}
	
	
	/**Scroll Top of the page
	 * Developed By: Santanu Ghatak
	 */
	public void ScrollTop(){
		try{
			JavascriptExecutor js = ((JavascriptExecutor) getDriver());
			js.executeScript("window.scrollTo(0, 0)");
		}catch(Exception e){
			
		}
		
	}
	
	
	/**Select dropdown value
	 * Developed By: Santanu Ghatak
	 */
	public void Select(String Object, String Value, ExtentTest htmlReport){
		try{
			WebElement Obj = CreateElement(Object, htmlReport);
			if(Run.stopFlag){
				return;
			}
			Select select = new Select(Obj);
			select.deselectAll();
			select.selectByVisibleText(Value);
		}catch(Exception e){
			Run.stopFlag = true;
			String imagePath = Report.CaptureScreen(getDriver());
			//Run.htmlReport.fail("Unable to Select dropdown: "+ Object + " Value: " + Value);
			try{
				Run.htmlReport.fail("Unable to Select dropdown: "+ Object + " Value: " + Value, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}catch(Exception e1){}
		}
	}
	
	
	
	/**Basic Accessibility Test
	 * Developed By: Santanu Ghatak
	 */	
	public void testAccessibility(String Page, String workingDirectory, ExtentTest htmlReport) {
		try{
			String path = workingDirectory.replaceAll("\\\\", "//");
			URL scriptUrl = Paths.get(path + "/FrameworkSupportedJars/axe.min.js").toUri().toURL();
			JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl).analyze();
	
			JSONArray violations = responseJSON.getJSONArray("violations");
	
			if (violations.length() == 0) {
				//Assert.assertTrue(true, "No violations found");
				Run.sAssert.assertTrue(true, "No violations found");
			} else {
				AXE.writeResults(Page, responseJSON);
				Run.printable = Run.printable + "\n\n******** " + Page + ": " + AXE.report(violations);
				Run.assertFlag = true;
				String imagePath = Report.CaptureScreen(getDriver());
				Run.htmlReport.fail(Page + ": Accessibility Test failed ", MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
