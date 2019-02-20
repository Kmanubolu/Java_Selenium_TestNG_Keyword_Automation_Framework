package com.java.CommonMethods;

import com.aventstack.extentreports.ExtentTest;
import com.java.TestSuitDriver.TestSuitRun;


public class Keywords{
	TestSuitRun Run;
	LowLableKeywords Keywords;
	
	public Keywords(TestSuitRun Run) {
		
		this.Run = Run; 
		Keywords = new LowLableKeywords(Run);

	}
	
	public void KeywordSelection(String stepNo, String fieldName, String action, String object, String testDateVal, String workingDirectory, ExtentTest htmlReport){
		
		switch (action.toUpperCase()){
			case "LAUNCHAPP":
				Keywords.LaunchBrowser(testDateVal, workingDirectory, htmlReport);
				break;
			case "CLOSEAPP":
				Keywords.CloseBrowser();
				break;
			case "CLICK":
				Keywords.ClickElemnt(object, htmlReport);
				break;
			case "SENDTEXT":
				Keywords.SendText(object, testDateVal, htmlReport);
				break;
			case "CHECKASSIBILITY":
				Keywords.testAccessibility(fieldName, workingDirectory, htmlReport);
				break;
			case "WAITFORELEMENT":
				Keywords.WaitForElement(object, htmlReport);
				break;
			case "WAIT":
				Keywords.Wait(testDateVal);
				break;
			case "SCROLLUP":
				Keywords.ScrollTop();
				break;
			case "SELECT":
				Keywords.Select(object, testDateVal, htmlReport);
				break;
			case "DOBMONTH":
				Keywords.ClickElem(object, htmlReport);
				break;
		}
	}

	

}
