package com.sempra.ivr.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.DBManager;
import com.scg.ivr.util.LoggerUtils;


public class SCGJDBCUtilsService extends BaseService {

	/**
	 * Comment for <code>JdbcUtilsinstance</code><br/>
	 * Description: Holds the only instance of the SCGJDBCUtilsService so this object need not be initialized again.<br/>
	 * This makes this class as a singleton class.
	 */
	private static SCGJDBCUtilsService JdbcUtilsinstance = null;

	protected SCGJDBCUtilsService(){
	}

	/**
	 * Description:  Returns the singleton instance of the SCGJDBCUtilsService Object.<br/>
	 * 
	 * @return JdbcUtilsinstance
	 * 
	 */
	public static SCGJDBCUtilsService getJdbcUtilsInstance() {
		JdbcUtilsinstance = new SCGJDBCUtilsService();	
		return JdbcUtilsinstance;
	}

	String xmlRequest = "";
	String xmlResponse = "";

	public JSONObject getSPReqResDataDetails(JSONObject requestJson) throws Exception {

		String procName = (String) requestJson.getJSONObject("Request").get("SCGIVRBRIDGE_TransName");
		LoggerUtils.debug(procName, SCGJDBCUtilsService.class, "getSPReqResDataDetails", "Inside getSPReqResDataDetails() with ProcName: " + procName);

		JSONObject responseJsonObj = new JSONObject();
		JSONObject mainResponseJsonObj = new JSONObject();

		try {
			WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

			String storedProcName = configParam.getName();			
			String user = configParam.getUser();
			String pwd = configParam.getPassword();
			String serverAddr = configParam.getDBServer();
			String port = configParam.getDBPort();
			String DBname = configParam.getDBName();
			Integer requestInput = configParam.getREQUESTINPUT();
			Integer requestOutput = configParam.getREQUESTOUTPUT();
			String waitTime = configParam.getWaitTime();
			String checkTiming = configParam.getCheckTiming();
			String DatabaseToPool = configParam.getDatabaseToPool();
			String SPReturnsCursor = configParam.getRETURNCURSOR();

			String requestValue;
			String pooling = configParam.getPooling();

			StringBuffer buf = new StringBuffer();

			buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			buf.append("<Transaction>");
			buf.append("<TransInfo Name=\"doStoredProcedure\">");

			if(pooling.equalsIgnoreCase("true") && DBname != null && DatabaseToPool != null && DatabaseToPool.contains(DBname.toLowerCase())){
				buf.append("<ProcedureInfo Name=\"" + storedProcName + "\" DBName=\"" + DBname + "\" SPReturnsCursor=\"" + SPReturnsCursor + "\" Pooling=\"" + pooling +  "\" CheckTiming=\"" + checkTiming +  "\" DatabaseToPool=\"" + DatabaseToPool + "\" WaitTime=\"" + waitTime + "\"/>");			
			}else{
				buf.append("<ProcedureInfo Name=\"" + storedProcName + "\" ServerAddr=\"" + serverAddr + "\" Port=\"" + port + "\" DBName=\"" + DBname + "\" SPReturnsCursor=\"" + SPReturnsCursor + "\" User=\"" + user + "\" Pwd=\"" + pwd + "\" Pooling=\"" + pooling +  "\" CheckTiming=\"" + checkTiming +  "\" DatabaseToPool=\"" + " " + "\" WaitTime=\"" + waitTime + "\"/>");
			}
			buf.append("<InputData Format=\"XML\" Data=\"TextElements\">");

			for(int i =1; i<=requestInput; i++){
				requestValue = lookUp (requestJson,"SCGIVRBRIDGE_Request" + i).trim();
				buf.append("<Input>" + requestValue.toUpperCase() + "</Input>");
			}

			for(int j = requestInput+1; j < requestInput+1+requestOutput; j++){
				requestValue = lookUp (requestJson,"SCGIVRBRIDGE_Request" + j).trim();
				buf.append("<Output>" + requestValue.toUpperCase() + "</Output>");
			} 

			buf.append("</InputData>");
			buf.append("<ReturnData Format=\"XML\" Data=\"TextElements\"/>");
			buf.append("</TransInfo>");
			buf.append("</Transaction>");		

			xmlRequest = buf.toString();
			LoggerUtils.debug(procName, SCGJDBCUtilsService.class, "getSPReqResDataDetails", "xmlRequestString: "+ xmlRequest);

			HashMap<String,ArrayList> hmValuesFromDBManagerClass = null;

			hmValuesFromDBManagerClass = DBManager.getdmMgrInstance().fetchValuesFromDB(xmlRequest,requestInput,requestOutput);

			xmlResponse = "";
			for ( String key : hmValuesFromDBManagerClass.keySet() ) {
				xmlResponse = key;
			}
			LoggerUtils.debug(procName, SCGJDBCUtilsService.class, "getSPReqResDataDetails", "xmlResponseString: "+ xmlResponse);

			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", xmlRequest);
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", xmlResponse);

			ArrayList valuesFromSP = null; 
			valuesFromSP = hmValuesFromDBManagerClass.get(xmlResponse);

			hmValuesFromDBManagerClass.clear();

			if(xmlResponse.contains("ERROR:")){
				SCGMainWSClientException ex = new SCGMainWSClientException(xmlResponse.toString());
				throw ex;
			}

			LoggerUtils.debug(procName, SCGJDBCUtilsService.class, "getSPReqResDataDetails", "xmlResponse: "+ xmlResponse);

			NodeList elemNodeList = null;
			DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;

			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));
			elemNodeList  = document.getElementsByTagName("ReturnCode");


			if(elemNodeList.item(0).getTextContent().equalsIgnoreCase("0")){
				responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("0"));				
				int cursorSize = valuesFromSP.size();  
				if(SPReturnsCursor.equalsIgnoreCase("no"))
				{					
					for(int k=0; k < cursorSize; k++){
						responseJsonObj.put("SCGIVRBRIDGE_Response"+(k+1), getResponseStringArray((String)valuesFromSP.get(k)));
					}
				} else {
					if(cursorSize != 0){        
						for(int l=0; l<requestOutput; l++){
							String[] response = new String[5];
							for(int k=0; k<5; k++){
								if(k < cursorSize){
									ArrayList valuesFromSPCursor = (ArrayList)valuesFromSP.get(k);
									response[k] = (String)valuesFromSPCursor.get(l);
								}else{
									response[k] = (String)null;
								}
							}
							responseJsonObj.put("SCGIVRBRIDGE_Response"+(l+1), response);
						}
					}
				}
			}else{
				responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
			}

			mainResponseJsonObj.put("Response", responseJsonObj);

		} catch (Exception ex) {
			mainResponseJsonObj = null;
			Exception e = new Exception(ex.getMessage());
			throw e;
		}

		return mainResponseJsonObj;
	}
}
