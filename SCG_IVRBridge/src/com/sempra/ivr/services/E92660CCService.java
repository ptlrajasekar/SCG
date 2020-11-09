package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92660CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92660CC.RESPONSE;
import com.scg.ivr.ws.E92660CC.SCREENPOPRESPONSEDATA;

public class E92660CCService extends BaseService {

	public static JSONObject getScreenPopDataDetails(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92660CCRequest E92660CCRequest = new E92660CCRequest();

		E92660CCRequest.setReqDatabaseName(configParam.getDBName());
		E92660CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92660CCRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92660CCRequest.setReqAccountId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
		E92660CCRequest.setReqCheckDigit(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92660CCRequest.setReqKeyType(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		E92660CCRequest.setReqLanguageCode(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));		

		RESPONSE E92660CCResponse = ServiceProvider.getCustomerProfileInformation(E92660CCRequest, configParam);

		if (E92660CCResponse != null) {
			responseJsonObj = parseE92660CCResponse(E92660CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92660CCResponse(RESPONSE E92660CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		SCREENPOPRESPONSEDATA screenpopresponsedata = E92660CCResponse.getScreenPopResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0",getResponseStringArray(screenpopresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1",getResponseStringArray(screenpopresponsedata.getRespScrnPopOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2",getResponseStringArray(screenpopresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3",getResponseStringArray(screenpopresponsedata.getRespErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4",getResponseStringArray(screenpopresponsedata.getRespApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5",getResponseStringArray(screenpopresponsedata.getRespSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6",getResponseStringArray(screenpopresponsedata.getRespAccountId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopHomePhone()));
		// Release: CEP3 -October 2019, Removing home phone extension
		//responseJsonObj.put("SCGIVRBRIDGE_Response8",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopHomePhoneExt()));
		
		responseJsonObj.put("SCGIVRBRIDGE_Response9",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCustomerName()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopSpouseName()));
		responseJsonObj.put("SCGIVRBRIDGE_Response11",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCustAddress()));
		responseJsonObj.put("SCGIVRBRIDGE_Response12",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCustId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response13",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCustTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response14",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCfId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response15",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopZipCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response16",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMailAddress()));
		responseJsonObj.put("SCGIVRBRIDGE_Response17",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMailCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response18",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMailState()));
		responseJsonObj.put("SCGIVRBRIDGE_Response19",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMailZip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response20",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMailZip4()));
		responseJsonObj.put("SCGIVRBRIDGE_Response21",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCheckDigit()));
		responseJsonObj.put("SCGIVRBRIDGE_Response22",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOnSimplePay()));
		responseJsonObj.put("SCGIVRBRIDGE_Response23",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOnDemandPay()));
		responseJsonObj.put("SCGIVRBRIDGE_Response24",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCurrBalDue()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response25",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopEligibleSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response26",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBase()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaTermDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response28",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaPaidDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response29",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaOpenDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response30",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopIcEstbDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response31",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOverdueCount()));
		responseJsonObj.put("SCGIVRBRIDGE_Response32",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPop48HrCount()));
		responseJsonObj.put("SCGIVRBRIDGE_Response33",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOverOneYr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response34",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOffGreater6()));
		responseJsonObj.put("SCGIVRBRIDGE_Response35",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopReadOkSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response36",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopNbrOfDials()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response37",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOfferAmortSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response38",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopYtdVariance()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response39",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopOfferRecertSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response40",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopLppSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response41",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCareSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response42",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopMedBaselineSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response43",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopThirdPartySw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response44",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaFrgnLngCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response45",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCustSegment()));
		responseJsonObj.put("SCGIVRBRIDGE_Response46",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCoreAggrBillCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response47",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopSbaCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response48",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response49",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCashOnlySw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response50",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaSpclLdgrSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response51",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopVcOrdrQty()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response52",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopNoVcOrdrQty()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response53",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopCfTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response54",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopAscBillCycId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response55",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaStatCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response56",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopBaClsDescCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response57",getResponseStringArray(screenpopresponsedata.getScreenPopData().getScreenPopEmailAddr()));
		
		// Release: CEP- Phase-II Paperless, May 25, 2017
		responseJsonObj.put("SCGIVRBRIDGE_Response58",getResponseStringArray(screenpopresponsedata.getScreenPopData().getFnpSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response59",getResponseStringArray(screenpopresponsedata.getScreenPopData().getAccountOnMyAccount()));
		responseJsonObj.put("SCGIVRBRIDGE_Response60",getResponseStringArray(screenpopresponsedata.getScreenPopData().getWacId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response61",getResponseStringArray(screenpopresponsedata.getScreenPopData().getUserId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response62",getResponseStringArray(screenpopresponsedata.getScreenPopData().getPaperlessBill()));
		responseJsonObj.put("SCGIVRBRIDGE_Response63",getResponseStringArray(screenpopresponsedata.getScreenPopData().getAmInstalledSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response64",getResponseStringArray(screenpopresponsedata.getScreenPopData().getPhoneCellArea()));
		responseJsonObj.put("SCGIVRBRIDGE_Response65",getResponseStringArray(screenpopresponsedata.getScreenPopData().getPhoneCellNo()));
		responseJsonObj.put("SCGIVRBRIDGE_Response66",getResponseStringArray(screenpopresponsedata.getScreenPopData().getServiceAddr1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response67",getResponseStringArray(screenpopresponsedata.getScreenPopData().getServiceAddr2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response68",getResponseStringArray(screenpopresponsedata.getScreenPopData().getServiceCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response69",getResponseStringArray(screenpopresponsedata.getScreenPopData().getServiceState()));

		// Release: CEP3 -October 2019, Adding new work phone fields
		responseJsonObj.put("SCGIVRBRIDGE_Response70",getResponseStringArray(screenpopresponsedata.getScreenPopData().getIcCustWorkPhoneNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response71",getResponseStringArray(screenpopresponsedata.getScreenPopData().getIcCustWorkPhoneExt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response72",getResponseStringArray(screenpopresponsedata.getScreenPopData().getBcCustBusPhoneNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response73",getResponseStringArray(screenpopresponsedata.getScreenPopData().getBcCustBusPhoneExt()));
		
		return responseJsonObj;
	}
}
