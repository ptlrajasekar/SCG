package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E93560CCRequest;

import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E93560CC.LinkedAccountResponse;


public class E93560CCService extends BaseService {

	public static JSONObject getLinkedAcctInfo(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E93560CCRequest E93560CCRequest = new E93560CCRequest();

		E93560CCRequest.setDb2CollName(configParam.getDBName());
		E93560CCRequest.setProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E93560CCRequest.setCisVersion(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E93560CCRequest.setWacID(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		

		LinkedAccountResponse linkedAcctInfoResponse = ServiceProvider.GetLinkedAcctInfo(E93560CCRequest,configParam);

		if (linkedAcctInfoResponse != null) {
			responseJsonObj = parseE93560CCResponse(linkedAcctInfoResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE93560CCResponse(LinkedAccountResponse linkedAcctInfoResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(linkedAcctInfoResponse.getProcessCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(linkedAcctInfoResponse.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(linkedAcctInfoResponse.getWacID()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(linkedAcctInfoResponse.getUserID()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(linkedAcctInfoResponse.getMktngOption()));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(linkedAcctInfoResponse.getEmail()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(Integer.toString(linkedAcctInfoResponse.getLinkedAcctCnt())));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(Integer.toString(linkedAcctInfoResponse.getLinkedAcctDetailNum())));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(linkedAcctInfoResponse.getLinkedAcctDetail()+""));
		
		if(linkedAcctInfoResponse.getLinkedAcctDetail()!=null && linkedAcctInfoResponse.getLinkedAcctDetailNum() >=1 ){
			
			int size = linkedAcctInfoResponse.getLinkedAcctDetailNum();
			
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];
			String[] response12 = new String[size];
			
			for (int i=0;i<size;i++){
				response9[i] = getResponseValue(linkedAcctInfoResponse.getLinkedAcctDetail().get(i).getAcctNum());
				response10[i] = getResponseValue(linkedAcctInfoResponse.getLinkedAcctDetail().get(i).getNickName());
				response11[i] = getResponseValue(linkedAcctInfoResponse.getLinkedAcctDetail().get(i).getAddress1());
				response12[i] = getResponseValue(linkedAcctInfoResponse.getLinkedAcctDetail().get(i).getAddress2());
			}
			
			responseJsonObj.put("SCGIVRBRIDGE_Response9",response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10",response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11",response11);
			responseJsonObj.put("SCGIVRBRIDGE_Response12",response12);
			
		}
		

		return responseJsonObj;
	}
}
