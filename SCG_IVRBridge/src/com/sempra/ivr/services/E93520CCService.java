package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;
import com.scg.ivr.beans.request.E93520CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E93520CC.PaperlessPromoEligResponse;


public class E93520CCService extends BaseService {

	public static JSONObject getPaperlessPromoElig(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E93520CCRequest E93520CCRequest = new E93520CCRequest();

		E93520CCRequest.setDb2CollName(configParam.getDBName());
		E93520CCRequest.setProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E93520CCRequest.setCisVersion(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E93520CCRequest.setWacID(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		

		PaperlessPromoEligResponse paperlessPromoEligResponse = ServiceProvider.GetPaperlessPromoElig(E93520CCRequest,configParam);

		if (paperlessPromoEligResponse != null) {
			responseJsonObj = parseE93520CCResponse(paperlessPromoEligResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE93520CCResponse(PaperlessPromoEligResponse paperlessPromoEligResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(paperlessPromoEligResponse.getProcessCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(paperlessPromoEligResponse.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(paperlessPromoEligResponse.getWacID()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(paperlessPromoEligResponse.getAcctCnt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(paperlessPromoEligResponse.getAcctDetailNum()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(paperlessPromoEligResponse.getAcctDetail()+""));
		
		
		if(paperlessPromoEligResponse.getAcctDetail()!=null && paperlessPromoEligResponse.getAcctDetailNum() >=1 ){
			
			int size = paperlessPromoEligResponse.getAcctDetailNum();
			
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];
			
			
			for (int i=0;i<size;i++){
				response9[i] = getResponseValue(paperlessPromoEligResponse.getAcctDetail().get(i).getAcctNum());
				response10[i] = getResponseValue(paperlessPromoEligResponse.getAcctDetail().get(i).getEligReason());
				response11[i] = getResponseValue(paperlessPromoEligResponse.getAcctDetail().get(i).getEligSw());
			}
			
			responseJsonObj.put("SCGIVRBRIDGE_Response9",response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10",response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11",response11);
			
		}
		

		return responseJsonObj;
	}
}
