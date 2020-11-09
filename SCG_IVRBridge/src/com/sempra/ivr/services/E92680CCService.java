package com.sempra.ivr.services;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92680CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92680CC.MemoPostResponse;
import com.scg.ivr.ws.E92680CC.RESPONSE;

public class E92680CCService extends BaseService {

	public static JSONObject createMemoPostData(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92680CCRequest e92680CCRequest = new E92680CCRequest();

		e92680CCRequest.setDatabaseName(configParam.getDBName());
		e92680CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		e92680CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		e92680CCRequest.setBillAccountId(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		e92680CCRequest.setPymtAmt(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request4"))));
		e92680CCRequest.setPymtDayTime(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		e92680CCRequest.setExternalId(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		e92680CCRequest.setTransmissionId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request7"))));
		e92680CCRequest.setTenderType(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		e92680CCRequest.setTransType(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		e92680CCRequest.setPymtType(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		e92680CCRequest.setConfirmNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request11"));

		RESPONSE E92680CCResponse = ServiceProvider.CreatePaymentMemoPost(e92680CCRequest, configParam);

		if (E92680CCResponse != null) {
			responseJsonObj = parseE92680CCResponse(E92680CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92680CCResponse(RESPONSE E92680CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		MemoPostResponse memoPostResponse = E92680CCResponse.getMemoPostResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(memoPostResponse.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(memoPostResponse.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(memoPostResponse.getReasonCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(memoPostResponse.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(memoPostResponse.getRespBillAccountId()+""));		
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(memoPostResponse.getRespExternalId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(memoPostResponse.getRespTransmissionId()+""));

		return responseJsonObj;
	}
}
