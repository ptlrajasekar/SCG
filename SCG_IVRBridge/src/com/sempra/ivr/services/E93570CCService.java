package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E93570CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E93570CC.BillAcctInfoResponse;

public class E93570CCService extends BaseService {

	public static JSONObject getBillAcctInfo(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E93570CCRequest E93570CCRequest = new E93570CCRequest();

		E93570CCRequest.setDb2CollName(configParam.getDBName());
		E93570CCRequest.setProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E93570CCRequest.setCisVersion(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E93570CCRequest.setAccountNumber(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E93570CCRequest.setUserID(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));

		BillAcctInfoResponse billAcctInfoResponse = ServiceProvider.GetBillAcctInfo(E93570CCRequest,configParam);

		if (billAcctInfoResponse != null) {
			responseJsonObj = parseE93570CCResponse(billAcctInfoResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE93570CCResponse(BillAcctInfoResponse E93570Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(E93570Response.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(E93570Response.getReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(E93570Response.getWacID()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(E93570Response.getUserID()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(E93570Response.getCustTy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(E93570Response.getPhoneHmArea()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(E93570Response.getPhoneHmNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(E93570Response.getPhoneWkArea()));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(E93570Response.getPhoneWkNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(E93570Response.getPhoneWkExtn()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(E93570Response.getPhoneCellArea()));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(E93570Response.getPhoneCellNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(E93570Response.getMailAddr1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(E93570Response.getMailCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(E93570Response.getMailState()));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(E93570Response.getMailZip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(E93570Response.getMailDaNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response17", getResponseStringArray(E93570Response.getMailFracNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response18", getResponseStringArray(E93570Response.getMailPreDrct()));
		responseJsonObj.put("SCGIVRBRIDGE_Response19", getResponseStringArray(E93570Response.getMailStreetNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response20", getResponseStringArray(E93570Response.getMailStreetTy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response21", getResponseStringArray(E93570Response.getMailPostDrct()));
		responseJsonObj.put("SCGIVRBRIDGE_Response22", getResponseStringArray(E93570Response.getMailDestTy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response23", getResponseStringArray(E93570Response.getMailDestTxt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response24", getResponseStringArray(E93570Response.getMailUPOBDesc()));
		responseJsonObj.put("SCGIVRBRIDGE_Response25", getResponseStringArray(E93570Response.getEmailAddr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response26", getResponseStringArray(E93570Response.getEmailAddrCIS()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27", getResponseStringArray(E93570Response.getAddrTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response28", getResponseStringArray(E93570Response.getCareOfNm()));

		responseJsonObj.put("SCGIVRBRIDGE_Response29", getResponseStringArray(E93570Response.getStatusDesc()));
		responseJsonObj.put("SCGIVRBRIDGE_Response30", getResponseStringArray(E93570Response.getOpenDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response31", getResponseStringArray(E93570Response.getCustNmPfx()));
		responseJsonObj.put("SCGIVRBRIDGE_Response32", getResponseStringArray(E93570Response.getCustFirstNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response33", getResponseStringArray(E93570Response.getCustMidInit()));
		responseJsonObj.put("SCGIVRBRIDGE_Response34", getResponseStringArray(E93570Response.getCustLastNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response35", getResponseStringArray(E93570Response.getCustNmSfx()));
		responseJsonObj.put("SCGIVRBRIDGE_Response36", getResponseStringArray(E93570Response.getCustBusnNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response37", getResponseStringArray(E93570Response.getPhoneHmExtn()));
		responseJsonObj.put("SCGIVRBRIDGE_Response38", getResponseStringArray(E93570Response.getServiceAddr1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response39", getResponseStringArray(E93570Response.getServiceAddr2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response40", getResponseStringArray(E93570Response.getServiceCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response41", getResponseStringArray(E93570Response.getServiceState()));
		responseJsonObj.put("SCGIVRBRIDGE_Response42", getResponseStringArray(E93570Response.getServiceZip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response43", getResponseStringArray(E93570Response.getFacilityTy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response44", getResponseStringArray(E93570Response.getCrossStreet()));
		responseJsonObj.put("SCGIVRBRIDGE_Response45", getResponseStringArray(E93570Response.getMailAddr2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response46", getResponseStringArray(E93570Response.getMailAddr3()));
		responseJsonObj.put("SCGIVRBRIDGE_Response47", getResponseStringArray(E93570Response.getSpouseNmPfx()));
		responseJsonObj.put("SCGIVRBRIDGE_Response48", getResponseStringArray(E93570Response.getSpouseFirstNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response49", getResponseStringArray(E93570Response.getSpouseMidInit()));
		responseJsonObj.put("SCGIVRBRIDGE_Response50", getResponseStringArray(E93570Response.getSpouseLastNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response51", getResponseStringArray(E93570Response.getCurDueAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response52", getResponseStringArray(E93570Response.getCurBalAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response53", getResponseStringArray(E93570Response.getNextReadDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response54", getResponseStringArray(E93570Response.getBillDueDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response55", getResponseStringArray(E93570Response.getDepDueDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response56", getResponseStringArray(E93570Response.getDepDueAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response57", getResponseStringArray(E93570Response.getDepPaidAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response58", getResponseStringArray(E93570Response.getCashOnlySw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response59", getResponseStringArray(E93570Response.getLppOnSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response60", getResponseStringArray(E93570Response.getLppOnAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response61", getResponseStringArray(E93570Response.getLppPromoAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response62", getResponseStringArray(E93570Response.getLppEligRsn()));
		responseJsonObj.put("SCGIVRBRIDGE_Response63", getResponseStringArray(E93570Response.getDirDbtStatus()));
		responseJsonObj.put("SCGIVRBRIDGE_Response64", getResponseStringArray(E93570Response.getDirDbtRoutNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response65", getResponseStringArray(E93570Response.getDirDbtAcctNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response66", getResponseStringArray(E93570Response.getDirDbtChkNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response67", getResponseStringArray(E93570Response.getDirDbtDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response68", getResponseStringArray(E93570Response.getDirDbtAmt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response69", getResponseStringArray(E93570Response.getHearingImpairedSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response70", getResponseStringArray(E93570Response.getVisuallyImpairedSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response71", getResponseStringArray(E93570Response.getLargePrintBillSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response72", getResponseStringArray(E93570Response.getBrailleBillSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response73", getResponseStringArray(E93570Response.getAccountNum()));
		responseJsonObj.put("SCGIVRBRIDGE_Response74", getResponseStringArray(E93570Response.getSpouseNmSfx()));
		responseJsonObj.put("SCGIVRBRIDGE_Response75", getResponseStringArray(E93570Response.getLppEligSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response76", getResponseStringArray(E93570Response.getCareSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response77", getResponseStringArray(E93570Response.getBaTyCd()));
		return responseJsonObj;
	}
}
