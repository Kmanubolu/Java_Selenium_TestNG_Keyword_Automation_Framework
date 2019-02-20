package com.java.CommonMethods;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Report {
	public static ExtentReports extent;
	public static ExtentTest test;
	public static ExtentHtmlReporter htmlReporter;
	public static String filePath = "";
	
	public static ExtentReports GetExtent(){
		//if (extent != null)
                    //return extent; //avoid creating new instance of html file
        extent = new ExtentReports();		
		extent.attachReporter(getHtmlReporter());
		return extent;
	}
	
	public static ExtentHtmlReporter getHtmlReporter() {
		
		String workingDirectory = System.getProperty("user.dir");
		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmmss");
		Date date = new Date();
		filePath = workingDirectory + "\\Output\\AutomationReport" + dateFormat.format(date) + ".html";
		
        htmlReporter = new ExtentHtmlReporter(filePath);
		
        // make the charts visible on report open
        htmlReporter.config().setChartVisibilityOnOpen(true);
		
        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName("Regression cycle");
        return htmlReporter;
	}
	
	public static ExtentTest createTest(String name, String description){
		test = extent.createTest(name, description);
		return test;
	}
	
	/*public static ExtentReports Instance(){
		ExtentReports extent;
		
		System.out.println(Path);
		extent = new ExtentReports(Path);
		extent.config()
		           .documentTitle("Automation Report")
		           .reportName("Accessibility");
		
		return extent;
	}*/
	
	public static String CaptureScreen(WebDriver driver){
		String workingDirectory = System.getProperty("user.dir");
		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmmss");
		Date date = new Date();
		String ImagesPath = workingDirectory + "\\Output\\Screenshots\\Screenshot" + dateFormat.format(date) + ".jpg";
		TakesScreenshot oScn = (TakesScreenshot) driver;
	    File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
	    File oDest = new File(ImagesPath);
	    try {
	    	FileUtils.copyFile(oScnShot, oDest);
	    } catch (IOException e) {System.out.println(e.getMessage());}
	    return ImagesPath;
	}
}
