package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92662CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92662CC.HighBillInvResponse;
import com.scg.ivr.ws.E92662CC.RESPONSE;



public class E92662CCService extends BaseService {
	public static JSONObject HighBillInv(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92662CCRequest E92662CCRequest = new E92662CCRequest();

		E92662CCRequest.setDatabaseName(configParam.getDBName());
		E92662CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92662CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92662CCRequest.setBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
				

		RESPONSE E92662CCResponse = ServiceProvider.getHighBillInv(E92662CCRequest, configParam);

		if (E92662CCResponse != null) {
			responseJsonObj = parseE92662CCResponse(E92662CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92662CCResponse(RESPONSE E92662CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		HighBillInvResponse hbillresponsedata = E92662CCResponse.getHighBillInvResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0",getResponseStringArray(hbillresponsedata.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response1",getResponseStringArray(hbillresponsedata.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2",getResponseStringArray(hbillresponsedata.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3",getResponseStringArray(hbillresponsedata.getApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4",getResponseStringArray(hbillresponsedata.getSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5",getResponseStringArray(hbillresponsedata.getRespBaId()+""));
		

		if(hbillresponsedata.getHighBillInvData() != null){

			responseJsonObj.put("SCGIVRBRIDGE_Response6",getResponseStringArray(hbillresponsedata.getHighBillInvData().getBaStatCd()));
			responseJsonObj.put("SCGIVRBRIDGE_Response7",getResponseStringArray(hbillresponsedata.getHighBillInvData().getHbiInvSw()));
			responseJsonObj.put("SCGIVRBRIDGE_Response8",getResponseStringArray(hbillresponsedata.getHighBillInvData().getLppSw()));
			responseJsonObj.put("SCGIVRBRIDGE_Response9",getResponseStringArray(hbillresponsedata.getHighBillInvData().getLppPaymentAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response10",getResponseStringArray(hbillresponsedata.getHighBillInvData().getLppPrevPaymentAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response11",getResponseStringArray(hbillresponsedata.getHighBillInvData().getLppAmortizationAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response12",getResponseStringArray(hbillresponsedata.getHighBillInvData().getLppAmortizationPmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response13",getResponseStringArray(hbillresponsedata.getHighBillInvData().getPrevBalAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response14",getResponseStringArray(hbillresponsedata.getHighBillInvData().getDepReimbAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response15",getResponseStringArray(hbillresponsedata.getHighBillInvData().getDepReimbInterest()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response16",getResponseStringArray(hbillresponsedata.getHighBillInvData().getDepReimbDt()));
			responseJsonObj.put("SCGIVRBRIDGE_Response17",getResponseStringArray(hbillresponsedata.getHighBillInvData().getDepAmt()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response18",getResponseStringArray(hbillresponsedata.getHighBillInvData().getDepDt()));
			responseJsonObj.put("SCGIVRBRIDGE_Response19",getResponseStringArray(hbillresponsedata.getHighBillInvData().getActualBillDays()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response20",getResponseStringArray(hbillresponsedata.getHighBillInvData().getNormalBillDays()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response21",getResponseStringArray(hbillresponsedata.getHighBillInvData().getEstimatedUsageSw()));
			responseJsonObj.put("SCGIVRBRIDGE_Response22",getResponseStringArray(hbillresponsedata.getHighBillInvData().getCurrentMonthUsage()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response23",getResponseStringArray(hbillresponsedata.getHighBillInvData().getPreviousMonthUsage()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response24",getResponseStringArray(hbillresponsedata.getHighBillInvData().getPreviousYearUsage()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response25",getResponseStringArray(hbillresponsedata.getHighBillInvData().getPoolSw()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response26",getResponseStringArray(hbillresponsedata.getHighBillInvData().getCurrColdWeatherDays()+""));
			responseJsonObj.put("SCGIVRBRIDGE_Response27",getResponseStringArray(hbillresponsedata.getHighBillInvData().getPrevColdWeatherDays()+""));
		}
		
		
		
		return responseJsonObj;
	}
}
