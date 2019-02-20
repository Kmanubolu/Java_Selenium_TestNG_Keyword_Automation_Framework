package com.java.CommonMethods;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.java.TestSuitDriver.TestSuitRun;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.*;


import java.io.File;
import java.util.ArrayList;



public class ReadingInputs {
	TestSuitRun Run ;
	
	
	public ReadingInputs(TestSuitRun Run){
		this.Run = Run;
	}

	
	/* Reading the configuration File*/
	public String ReadConfig(String workingDirectory){
		String BrowserType = null;
		String InputWorkBook = null;
		String TimeOut = null;
		try{
			File XmlFile = new File(workingDirectory + "/Configuration/Configuration.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);
			
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("testConfig");
			System.out.println("----------------------------");
			boolean runFlag = false;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if(eElement.getAttribute("run").toString().equalsIgnoreCase("true")){
						InputWorkBook = eElement.getElementsByTagName("TestInputSheet").item(0).getTextContent();
						BrowserType = eElement.getElementsByTagName("Browser").item(0).getTextContent();
						TimeOut = eElement.getElementsByTagName("TimeOut").item(0).getTextContent();
						runFlag = true;
					}	
				}
			}
			if (runFlag == false){
				System.out.println("**** No Test Configuration to run****");
				Run.stopFlag = true;
			}
			
		}catch(Exception e){
			System.out.println(e);
			Run.stopFlag = true;
		}
		return (InputWorkBook + "||" + BrowserType + "||" + TimeOut);
	}
	
	
	public Recordset SelectQuery(String workingDirectory, String InputWorkbook, String strQuery){
		Fillo fillo=new Fillo();
		Connection connection = null;
		Recordset recordset = null;
		try {
			connection = fillo.getConnection(workingDirectory + "/Input/" + InputWorkbook);
			recordset=connection.executeQuery(strQuery);
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Run.stopFlag = true;
		}
		return recordset;
		
	}
	
	public String[][] TestCaseDetails(String workingDirectory, String InputWorkbook, ArrayList<Integer> TestCases){
		String[][] TestCaseArray = null;
		int ci = 0;
		Fillo fillo=new Fillo();
		Connection connection = null;
		Recordset recordset = null;
		try {
			connection = fillo.getConnection(workingDirectory + "/Input/" + InputWorkbook);
			TestCaseArray = new String[TestCases.size()][2];
			for(int i: TestCases){
				String strQueryTestMaster="Select * from TestCaseMaster where TestCaseId='1'";
				
				recordset = connection.executeQuery(strQueryTestMaster);
				recordset.next();
				TestCaseArray [ci][0] = recordset.getField("TestCaseId");
				TestCaseArray [ci][1] = recordset.getField("TestCaseName");
				ci = ci++;
				recordset.close();
			}
			connection.close();
			return TestCaseArray;
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Run.stopFlag = true;
			return null;
		}
		
		
	}
}


