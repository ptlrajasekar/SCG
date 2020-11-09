package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92650CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92650CC.READSERVICESRESPONSEDATA;
import com.scg.ivr.ws.E92650CC.RESPONSE;

public class E92650CCService extends BaseService {

	public static JSONObject CreateCustReadFunctions(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));
		
		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92650CCRequest E92650CCRequest = new E92650CCRequest();

		E92650CCRequest.setReqDatabaseName(configParam.getDBName());
		E92650CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92650CCRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92650CCRequest.setReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
		E92650CCRequest.setReqReadDt(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92650CCRequest.setReqCustomerRead(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));

		RESPONSE response = ServiceProvider.CreateCustReadFunctions(E92650CCRequest,configParam);

		if (response != null) {
			responseJsonObj = parseE92650CCResponse(response);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92650CCResponse(RESPONSE E92650Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		READSERVICESRESPONSEDATA readservicesresponsedata = E92650Response.getReadServicesResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(readservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(readservicesresponsedata.getRespReadServOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(readservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(readservicesresponsedata.getRespErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(readservicesresponsedata.getRespApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(readservicesresponsedata.getRespSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(readservicesresponsedata.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(readservicesresponsedata.getRespReadProratedCcf()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(readservicesresponsedata.getRespReadProratedRead()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(readservicesresponsedata.getRespReadDailyQty()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(readservicesresponsedata.getRespReadDiffDays()+""));

		return responseJsonObj;
	}
}
