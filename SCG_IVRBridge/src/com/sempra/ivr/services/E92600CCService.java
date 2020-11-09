package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92600CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92600CC.BILLINGSERVICESRESPONSEDATA;
import com.scg.ivr.ws.E92600CC.RESPONSE;

public class E92600CCService extends BaseService {

	public static JSONObject getCreateBillingFunctions(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92600CCRequest E92600CCRequest = new E92600CCRequest();		

		E92600CCRequest.setReqDatabaseName(configParam.getDBName());
		E92600CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92600CCRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92600CCRequest.setReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
		E92600CCRequest.setReqFldrHistData(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));		

		RESPONSE E92600CCResponse = ServiceProvider.getCreateBillingFunctions(E92600CCRequest, configParam);

		if (E92600CCResponse != null) {
			responseJsonObj = parseE92600CCResponse(E92600CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92600CCResponse(RESPONSE E92600CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		BILLINGSERVICESRESPONSEDATA billingservicesresponsedata = E92600CCResponse.getBillingServicesResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(billingservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(billingservicesresponsedata.getRespBillServOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(billingservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(billingservicesresponsedata.getRespErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(billingservicesresponsedata.getRespApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(billingservicesresponsedata.getRespSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(billingservicesresponsedata.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqCurrentBalDue()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqLppSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqPaymentDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqPaymentAmount()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqCheckDigit()));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqBillDueDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqPastDueSw()));		
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(billingservicesresponsedata.getAccountBalanceData().getRespInqZipCode()+""));		
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(billingservicesresponsedata.getLppAmortizationData().getRespLppNewPymtAmt()+""));

		return responseJsonObj;
	}
}
