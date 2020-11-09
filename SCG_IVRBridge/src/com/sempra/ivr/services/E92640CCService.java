package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92640CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92640CC.CARESERVICESRESPONSEDATA;
import com.scg.ivr.ws.E92640CC.RESPONSE;

public class E92640CCService extends BaseService {

	public static JSONObject CreatePaymentFunctions(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92640CCRequest E92640CCRequest = new E92640CCRequest();

		E92640CCRequest.setReqDatabaseName(configParam.getDBName());
		E92640CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92640CCRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92640CCRequest.setReqBaCfShrId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
		E92640CCRequest.setReqCareActionCd(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92640CCRequest.setReqMediCalSw(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		E92640CCRequest.setReqMediCalU65Sw(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		E92640CCRequest.setReqFoodStampsSw(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
		E92640CCRequest.setReqHlthyFamSw(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		E92640CCRequest.setReqTanfSw(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		E92640CCRequest.setReqWicSw(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		E92640CCRequest.setReqLiheapSw(lookUp(requestJson, "SCGIVRBRIDGE_Request11"));
		E92640CCRequest.setReqSsiSw(lookUp(requestJson, "SCGIVRBRIDGE_Request12"));
		E92640CCRequest.setReqNslSw(lookUp(requestJson, "SCGIVRBRIDGE_Request13"));
		E92640CCRequest.setReqTribaltanfSw(lookUp(requestJson, "SCGIVRBRIDGE_Request14"));
		E92640CCRequest.setReqBiaGaSw(lookUp(requestJson, "SCGIVRBRIDGE_Request15"));
		E92640CCRequest.setReqHstribalSw(lookUp(requestJson, "SCGIVRBRIDGE_Request16"));
		E92640CCRequest.setReqHouseholdNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request17"));
		E92640CCRequest.setReqHouseholdInc(lookUp(requestJson, "SCGIVRBRIDGE_Request18"));

	    // CEP-24 CARE Changes New Request Fields
		
		E92640CCRequest.setReqIncRngCd(lookUp(requestJson, "SCGIVRBRIDGE_Request19"));
		E92640CCRequest.setReqSrcSSSw(lookUp(requestJson, "SCGIVRBRIDGE_Request20"));
		E92640CCRequest.setReqSrcsspssdiSw(lookUp(requestJson, "SCGIVRBRIDGE_Request21"));
		E92640CCRequest.setReqSrcPensionsSw(lookUp(requestJson, "SCGIVRBRIDGE_Request22"));
		E92640CCRequest.setReqSrcIntDivSw(lookUp(requestJson, "SCGIVRBRIDGE_Request23"));
		E92640CCRequest.setReqSrcOtherSw(lookUp(requestJson, "SCGIVRBRIDGE_Request24"));
		E92640CCRequest.setReqChannelMethodType(lookUp(requestJson, "SCGIVRBRIDGE_Request25"));
		// CARE Language Code Updates - Jan 16, 2018
		E92640CCRequest.setReqLangCode(lookUp(requestJson, "SCGIVRBRIDGE_Request26"));
		
		RESPONSE response = ServiceProvider.CreateCareFunctions(E92640CCRequest,configParam);

		if (response != null) {
			responseJsonObj = parseE92640CCResponse(response);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92640CCResponse(RESPONSE E92640Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		CARESERVICESRESPONSEDATA careservicesresponsedata = E92640Response.getCareServicesResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(careservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(careservicesresponsedata.getRespCareServOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(careservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(careservicesresponsedata.getRespErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(careservicesresponsedata.getRespApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(careservicesresponsedata.getRespSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(careservicesresponsedata.getRespBaId()+""));
		
		// CEP-24 CARE Changes New Response Fields
		
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(careservicesresponsedata.getRespIncRngMax1()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(careservicesresponsedata.getRespIncRngMax2()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(careservicesresponsedata.getRespIncRngMax3()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(careservicesresponsedata.getRespIncRngMax4()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(careservicesresponsedata.getRespIncRngMax5()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(careservicesresponsedata.getRespNextStepCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(careservicesresponsedata.getRespCaaDocTyCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(careservicesresponsedata.getRespCaaDocStatCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(careservicesresponsedata.getRespCaaDocStatDt()+""));
				
		return responseJsonObj;
	}
}
