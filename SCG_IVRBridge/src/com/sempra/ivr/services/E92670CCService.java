package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92670CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92670CC.RESPONSE;
import com.scg.ivr.ws.E92670CC.TailoredTreatmentsResponse;

public class E92670CCService extends BaseService {

	public static JSONObject getPrioritiesData(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92670CCRequest E92670CCRequest = new E92670CCRequest();

		E92670CCRequest.setDatabaseName(configParam.getDBName());
		E92670CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92670CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));

		RESPONSE E92670CCResponse = ServiceProvider.getPrioritiesData(E92670CCRequest, configParam);

		if (E92670CCResponse != null) {
			responseJsonObj = parseE92670CCResponse(E92670CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92670CCResponse(RESPONSE E92670CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		TailoredTreatmentsResponse tailoredTreatmentsResponse = E92670CCResponse.getTailoredTreatmentsResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(tailoredTreatmentsResponse.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(tailoredTreatmentsResponse.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(tailoredTreatmentsResponse.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(tailoredTreatmentsResponse.getApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(tailoredTreatmentsResponse.getSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(tailoredTreatmentsResponse.getPriorityInformation().getTatrPriorityOne()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(tailoredTreatmentsResponse.getPriorityInformation().getTatrPriorityTwo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(tailoredTreatmentsResponse.getPriorityInformation().getTatrPriorityThree()));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(tailoredTreatmentsResponse.getPriorityInformation().getTatrPriorityFour()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(tailoredTreatmentsResponse.getPriorityInformation().getTatrPriorityFive()));

		return responseJsonObj;
	}
}
