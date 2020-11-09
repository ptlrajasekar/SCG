package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E93515CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E93515CC.CustomerProfileResponse;

public class E93515CCService extends BaseService {

	public static JSONObject getupdateCustomerProfile(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E93515CCRequest E93515CCRequest = new E93515CCRequest();

		E93515CCRequest.setDb2CollName(configParam.getDBName());
		E93515CCRequest.setProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E93515CCRequest.setCisVersion(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E93515CCRequest.setAccountNumber(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E93515CCRequest.setWacID(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E93515CCRequest.setUserId(lookUp(requestJson, "SCGIVRBRIDGE_Request28"));
		E93515CCRequest.setCustTy(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		E93515CCRequest.setPhoneHmArea(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
		E93515CCRequest.setPhoneHmNo(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		E93515CCRequest.setPhoneWkArea(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		E93515CCRequest.setPhoneWkNo(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		E93515CCRequest.setPhoneWkExtn(lookUp(requestJson, "SCGIVRBRIDGE_Request11"));
		E93515CCRequest.setPhoneCellArea(lookUp(requestJson, "SCGIVRBRIDGE_Request12"));
		E93515CCRequest.setPhoneCellNo(lookUp(requestJson, "SCGIVRBRIDGE_Request13"));
		E93515CCRequest.setEmailAddrMyAcct(lookUp(requestJson, "SCGIVRBRIDGE_Request14"));
		E93515CCRequest.setEmailAddrCIS(lookUp(requestJson, "SCGIVRBRIDGE_Request15"));

		E93515CCRequest.setCustFirstNm(lookUp(requestJson, "SCGIVRBRIDGE_Request16"));
		E93515CCRequest.setCustMidInit((lookUp(requestJson, "SCGIVRBRIDGE_Request17")));
		E93515CCRequest.setCustLastNm((lookUp(requestJson, "SCGIVRBRIDGE_Request18")));
		E93515CCRequest.setCustBusnNm((lookUp(requestJson, "SCGIVRBRIDGE_Request19")));
		E93515CCRequest.setSpouseFirstNm((lookUp(requestJson, "SCGIVRBRIDGE_Request20")));
		E93515CCRequest.setSpouseMidInit((lookUp(requestJson, "SCGIVRBRIDGE_Request21")));
		E93515CCRequest.setSpouseLastNm((lookUp(requestJson, "SCGIVRBRIDGE_Request22")));
		E93515CCRequest.setNmChgRsn((lookUp(requestJson, "SCGIVRBRIDGE_Request23")));
		E93515CCRequest.setHearingImpairedSw((lookUp(requestJson, "SCGIVRBRIDGE_Request24")));
		E93515CCRequest.setVisuallyImpairedSw((lookUp(requestJson, "SCGIVRBRIDGE_Request25")));
		E93515CCRequest.setLargePrintBillSw((lookUp(requestJson, "SCGIVRBRIDGE_Request26")));
		E93515CCRequest.setBrailleBillSw((lookUp(requestJson, "SCGIVRBRIDGE_Request27")));
		// CEP3 - RAMP - July 2020 Release - Two new request field(ChannelId and Phone CellVerifSw)
		E93515CCRequest.setChannelId((lookUp(requestJson, "SCGIVRBRIDGE_Request28")));
		E93515CCRequest.setPhoneCellVerifSw((lookUp(requestJson, "SCGIVRBRIDGE_Request29")));

		CustomerProfileResponse customerProfileResponse = ServiceProvider.updateCustomerProfile(E93515CCRequest,configParam);

		if (customerProfileResponse != null) {
			responseJsonObj = parseE93515CCResponse(customerProfileResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE93515CCResponse(CustomerProfileResponse E93515Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(E93515Response.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(E93515Response.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(E93515Response.getProcessCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(E93515Response.getAccountNum()));

		return responseJsonObj;
	}
}
