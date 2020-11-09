package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92685CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92685CC.GetMemoPostResponse;
import com.scg.ivr.ws.E92685CC.RESPONSE;

public class E92685CCService extends BaseService {

	public static JSONObject getMemoPostData(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92685CCRequest e92685CCRequest = new E92685CCRequest();

		e92685CCRequest.setDatabaseName(configParam.getDBName());
		e92685CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		e92685CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		e92685CCRequest.setReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));

		RESPONSE E92685CCResponse = ServiceProvider.GetPaymentMemoPost(e92685CCRequest, configParam);

		if (E92685CCResponse != null) {
			responseJsonObj = parseE92685CCResponse(E92685CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92685CCResponse(RESPONSE E92685CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		GetMemoPostResponse getMemoPostResponse = E92685CCResponse.getGetMemoPostResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(getMemoPostResponse.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(getMemoPostResponse.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(getMemoPostResponse.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(getMemoPostResponse.getApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(getMemoPostResponse.getSystemErrorCd()+""));		
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(getMemoPostResponse.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(getMemoPostResponse.getTotalPendingPaymentAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(getMemoPostResponse.getNumberOfPendingPayments()+""));

		if(getMemoPostResponse.getPendingPaymentTable() != null && getMemoPostResponse.getPendingPaymentTable().size() > 0){
			int size = getMemoPostResponse.getPendingPaymentTable().size();
			String[] response8 = new String[size];
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];
			for(int i= 0; i< size; i++) {
				response8[i] = getResponseValue(getMemoPostResponse.getPendingPaymentTable().get(i).getPendingPaymentAmt()+"");
				response9[i] = getResponseValue(getMemoPostResponse.getPendingPaymentTable().get(i).getPendingPaymentDt());
				response10[i] = getResponseValue(getMemoPostResponse.getPendingPaymentTable().get(i).getPendingPaymentTm());
				response11[i] = getResponseValue(getMemoPostResponse.getPendingPaymentTable().get(i).getPendingPaymentChannel());
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response8",response8);
			responseJsonObj.put("SCGIVRBRIDGE_Response9", response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10", response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11", response11);
		}

		return responseJsonObj;
	}
}
