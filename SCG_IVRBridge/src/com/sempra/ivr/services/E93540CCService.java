package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;
import com.scg.ivr.beans.request.E93540CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E93540CC.PaperlessPromoEnrollResponse;



public class E93540CCService extends BaseService {

	public static JSONObject updatePaperlessPromoEnroll(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E93540CCRequest E93540CCRequest = new E93540CCRequest();

		E93540CCRequest.setDb2CollName(configParam.getDBName());
		E93540CCRequest.setProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E93540CCRequest.setCisVersion(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E93540CCRequest.setAccountNumber(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E93540CCRequest.setUserId(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E93540CCRequest.setUserTy(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		
		

		PaperlessPromoEnrollResponse paperlessPromoEnrollResponse = ServiceProvider.GetPaperlessPromoEnroll(E93540CCRequest,configParam);

		if (paperlessPromoEnrollResponse != null) {
			responseJsonObj = parseE93540CCResponse(paperlessPromoEnrollResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE93540CCResponse(PaperlessPromoEnrollResponse paperlessPromoEnrollResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(paperlessPromoEnrollResponse.getProcessCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(paperlessPromoEnrollResponse.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(paperlessPromoEnrollResponse.getAcctNum()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(paperlessPromoEnrollResponse.getSuccessSw()));
		

		return responseJsonObj;
	}
}
