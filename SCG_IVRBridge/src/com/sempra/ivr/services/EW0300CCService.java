package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.EW0300CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.EW0300CC.RESPONSE;

public class EW0300CCService extends BaseService {

	public static JSONObject updateSVOCDetails(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		EW0300CCRequest svocRequest = new EW0300CCRequest();

		svocRequest.setReqDb2Collection(configParam.getDBName());
		svocRequest.setReqProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));		
		svocRequest.setReqCompanyCode(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		svocRequest.setReqCustomerID(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		svocRequest.setReqCustomerTypeCode(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		svocRequest.setReqWACID(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		svocRequest.setReqCTCTransStartTS(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		svocRequest.setReqCTCSubjectAreaType(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
		svocRequest.setReqCTCReasonType(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		svocRequest.setReqCTCTransProcessType(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		svocRequest.setReqCTCChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		svocRequest.setReqCTCMethodType(lookUp(requestJson, "SCGIVRBRIDGE_Request11"));
		svocRequest.setReqLangType(lookUp(requestJson, "SCGIVRBRIDGE_Request12"));
		svocRequest.setReqCTCDirectionCode(lookUp(requestJson, "SCGIVRBRIDGE_Request13"));
		svocRequest.setReqOrigSystemName(lookUp(requestJson, "SCGIVRBRIDGE_Request14"));
		svocRequest.setReqCTCAnalyticSw(lookUp(requestJson, "SCGIVRBRIDGE_Request15"));
		svocRequest.setReqCTCComments(lookUp(requestJson, "SCGIVRBRIDGE_Request16"));

		svocRequest.setReqAddress1Desc(lookUp(requestJson, "SCGIVRBRIDGE_Request17"));
		svocRequest.setReqAddress2Desc(lookUp(requestJson, "SCGIVRBRIDGE_Request18"));
		svocRequest.setReqAddress3Desc(lookUp(requestJson, "SCGIVRBRIDGE_Request19"));
		svocRequest.setReqAgentID(lookUp(requestJson, "SCGIVRBRIDGE_Request20"));
		svocRequest.setReqBillAcctID(lookUp(requestJson, "SCGIVRBRIDGE_Request21"));
		svocRequest.setReqBusCustName(lookUp(requestJson, "SCGIVRBRIDGE_Request22"));		
		svocRequest.setReqCTCConfirmationNum(lookUp(requestJson, "SCGIVRBRIDGE_Request23"));
		svocRequest.setReqCTCContactID(lookUp(requestJson, "SCGIVRBRIDGE_Request24"));
		svocRequest.setReqCTCDestAddrUsed(lookUp(requestJson, "SCGIVRBRIDGE_Request25"));
		svocRequest.setReqCTCDestIDUsed(lookUp(requestJson, "SCGIVRBRIDGE_Request26"));		
		svocRequest.setReqCTCEmailAddr(lookUp(requestJson, "SCGIVRBRIDGE_Request27"));
		svocRequest.setReqCTCMsgUUID(lookUp(requestJson, "SCGIVRBRIDGE_Request28"));
		svocRequest.setReqCTCOutageStartTS(lookUp(requestJson, "SCGIVRBRIDGE_Request29"));		
		svocRequest.setReqCTCTransShrID(lookUp(requestJson, "SCGIVRBRIDGE_Request30"));		
		svocRequest.setReqCustFacilityID(lookUp(requestJson, "SCGIVRBRIDGE_Request31"));		
		svocRequest.setReqGNNID(lookUp(requestJson, "SCGIVRBRIDGE_Request32"));
		svocRequest.setReqIndCustFirstName(lookUp(requestJson, "SCGIVRBRIDGE_Request33"));
		svocRequest.setReqIndCustLastName(lookUp(requestJson, "SCGIVRBRIDGE_Request34"));
		svocRequest.setReqIndCustMiddleName(lookUp(requestJson, "SCGIVRBRIDGE_Request35"));		
		svocRequest.setReqOrigCTCSourceAddr(lookUp(requestJson, "SCGIVRBRIDGE_Request36"));
		svocRequest.setReqOrigCTCSourceID(lookUp(requestJson, "SCGIVRBRIDGE_Request37"));
		svocRequest.setReqOrigLocationName(lookUp(requestJson, "SCGIVRBRIDGE_Request38"));
		svocRequest.setReqThirdPartyName(lookUp(requestJson, "SCGIVRBRIDGE_Request39"));


		RESPONSE SVOCResponse = ServiceProvider.SVOCServiceResponse(svocRequest,configParam	);

		if (SVOCResponse != null) {
			responseJsonObj = parseEW0300CCResponse(SVOCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseEW0300CCResponse(RESPONSE EW0300Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(EW0300Response.getMessageHeaderResponse().getResponseCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(EW0300Response.getMessageHeaderResponse().getResponseMessage()));

		return responseJsonObj;
	}
}
