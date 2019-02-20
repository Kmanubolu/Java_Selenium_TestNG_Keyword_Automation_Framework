package com.java.TestSuitDriver;

import java.util.Collections;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.java.CommonMethods.*;

import com.codoid.products.fillo.Recordset;



public class TestSuitRun {
	
	public ExtentReports extent;
	public ExtentTest htmlReport;
	public boolean stopFlag;
	public int gTimeOut;
	public static ThreadLocal<RemoteWebDriver> threadDriver = new ThreadLocal<RemoteWebDriver>();
	public String workingDirectory = System.getProperty("user.dir");
	public SoftAssert sAssert = new SoftAssert();
	public boolean assertFlag = false;
	public String printable = "";
	public String InputWorkbook;
	public String testngTestName = "";
	ArrayList<Integer> TestCases = new ArrayList<Integer>();
	
	ReadingInputs Read = new ReadingInputs(this);
	

	@BeforeTest
	@Parameters({"Browser", "TestCaseID", "InputSheet", "WaitTime"})
	public void Setup(String Browser, String TestCaseID, String InputSheet, String WaitTime, ITestContext ctx) throws MalformedURLException{
		RemoteWebDriver driver = null;
		DesiredCapabilities capability = null;
		
		testngTestName = ctx.getCurrentXmlTest().getName();

        /**Initiating HTML report*/
        extent = Report.GetExtent();
        
        /**TestData*/
        if (TestCaseID.contains("-")){
			String[] nTest = TestCaseID.split("-");
			int LowerLimit = Integer.parseInt(nTest[0]);
			int UpperLimit = Integer.parseInt(nTest[1]);
			for(int i=0; i <= UpperLimit-LowerLimit; i++){
				TestCases.add(LowerLimit+i);
			}
		}else if (TestCaseID.contains(",")){
			String[] nTest = TestCaseID.split(",");
			for(int i = 0;i < nTest.length;i++)
			{
				TestCases.add(Integer.parseInt(nTest[i]));
			}
		}else{
			int newTest = Integer.parseInt(TestCaseID);
			TestCases.add(newTest);
		}
        

        InputWorkbook = InputSheet;
        
		switch (Browser.toLowerCase()){
		case "firefox":
			capability = new DesiredCapabilities().firefox();
	        capability.setBrowserName("firefox");
			break;
		case "chrome":
			//capability = new DesiredCapabilities().chrome();
	        //capability.setBrowserName("chrome");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("no-sandbox");
			options.addArguments("start-maximized");
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.setExperimentalOption("useAutomationExtension", false);
			capability = DesiredCapabilities.chrome();
			capability.setCapability(ChromeOptions.CAPABILITY, options);
			capability.setBrowserName("chrome");
			break;
		case "ie":
			capability = new DesiredCapabilities().internetExplorer();
	        capability.setBrowserName("ie");
			break;
		}
		capability.setPlatform(Platform.WINDOWS);
        driver = new RemoteWebDriver(new URL("http://localhost:8888/wd/hub"), capability);
        setWebDriver(driver);
        

		gTimeOut = Integer.parseInt(WaitTime);
	}
	
	
	 public WebDriver getDriver() {
	        return threadDriver.get();
	 }
	 
	 public void setWebDriver(RemoteWebDriver driver) {
		 threadDriver.set(driver);
	 }
	 
	
	 @DataProvider(name = "createdata")
	 public Object[][] ValidDataProvider(){
		 try{
			 Object[][] TestDetails = Read.TestCaseDetails(workingDirectory, InputWorkbook, TestCases);
			 return TestDetails;
		 }catch(Exception e){
			 return null;
		 } 
	 }
	 
	 
	@Test(dataProvider = "createdata")
	public void ScriptExecution(String testcaseID, String testCaseName){
		LowLableKeywords asrt = new LowLableKeywords(this);
		try{
			System.out.println("********** Test Case Id: " + testcaseID + " **** Test Case Name: " + testCaseName + "********** \n");
			
			stopFlag = false;
			//Writting Report test case name
			htmlReport = extent.createTest(testCaseName + "_" + testngTestName, "Test Case Id: " + testcaseID);
			
			//Reading Test Case Flow to execute the script
			String strQueryTestFlow = "Select * from TestCaseFlow where TestCaseId = '" + testcaseID + "' and ToBeExecuted = 'Yes'";
			Recordset recordsetTestFlow = Read.SelectQuery(workingDirectory, InputWorkbook, strQueryTestFlow);
			while(recordsetTestFlow.next()){
				if (!stopFlag){                      
					String StepNo = recordsetTestFlow.getField("StepNo");
					String FieldName = recordsetTestFlow.getField("Screen_FieldName");
					String Action = recordsetTestFlow.getField("Action");
					String Object = recordsetTestFlow.getField("Object");
					String TestDateVal = recordsetTestFlow.getField("Data");
					
					//Reading Object Sheet
					if(Object.contains("Object_")){
						String[] TestObject = Object.split("_");
						String strQueryObject = "Select * from ObjectDetails where ObjectName = '" + TestObject[1] + "'";
						Recordset recordsetObject = Read.SelectQuery(workingDirectory, InputWorkbook, strQueryObject);
						recordsetObject.next();
						Object = recordsetObject.getField("ObjectProperty");
						recordsetObject.close();
					}
					
					//Reading InputData Sheet
					if(TestDateVal.contains("Input_")){
						String[] TestDate = TestDateVal.split("_");
						String strQueryObject = "Select * from InputDataDetails where FieldName = '" + TestDate[1] + "'";
						Recordset recordsetData = Read.SelectQuery(workingDirectory, InputWorkbook, strQueryObject);
						recordsetData.next();
						TestDateVal = recordsetData.getField("FieldValue");
						recordsetData.close();
					}
					
					System.out.println("---------- Step No: " + StepNo + " ---- Page/Field Name: " + FieldName + " ----------");
					System.out.println("---------- Action: " + Action + " ----------");
					System.out.println("Object: " + Object);
					System.out.println("TestDateVal: " + TestDateVal + "\n");
					//Script Flow Execution
					Keywords KeywordSelection = new Keywords(this);
					KeywordSelection.KeywordSelection(StepNo, FieldName, Action, Object, TestDateVal, workingDirectory, htmlReport);
				}
					
			}
			recordsetTestFlow.close();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		if(assertFlag){
			sAssert.assertTrue(false, printable);
			sAssert.assertAll();
		}
		if(stopFlag){
			sAssert.assertTrue(false, "Failed");
			sAssert.assertAll();
		}

	}
	
	@AfterMethod
    public void closeBrowser() {
        getDriver().quit();
        threadDriver.set(null);
    }
	
	@AfterClass
	public void closereport(){
		extent.flush();
	}
}
