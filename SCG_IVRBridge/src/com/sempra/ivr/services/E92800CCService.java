package com.sempra.ivr.services;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92800CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92800CC.PYMTEXTRESPONSEDATA;
import com.scg.ivr.ws.E92800CC.RESPONSE;
import com.scg.ivr.ws.E92800CC.T800PECREATEEXTSECTION;
import com.scg.ivr.ws.E92800CC.T800PYMTARR;
import com.scg.ivr.ws.E92800CC.X800EXISTINGEXTSECT;

public class E92800CCService extends BaseService {

	public static JSONObject CreatePaymentExtension(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92800CCRequest E92800CCRequest = new E92800CCRequest();
		T800PECREATEEXTSECTION t800pecreateextsection = new T800PECREATEEXTSECTION();

		E92800CCRequest.setT800DatabaseName(configParam.getDBName());
		E92800CCRequest.setT800OperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92800CCRequest.setT800ChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92800CCRequest.setT800ReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));

		t800pecreateextsection.setT800SuLogonId(lookUp(requestJson, "SCGIVRBRIDGE_Request23"));
		t800pecreateextsection.setT800PymtExtAmt(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request8"))));
		t800pecreateextsection.setT800PymtExtDt(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		//t800pecreateextsection.setT800PymtMthdCd(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		t800pecreateextsection.setT800NumberOfInstallments(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request11"))));

		T800PYMTARR t800pymtarr = new T800PYMTARR();
		t800pymtarr.setT800PymtArrAmt(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request12"))));
		t800pymtarr.setT800PymtArrDt(lookUp(requestJson, "SCGIVRBRIDGE_Request13"));
		t800pymtarr.setT800PymtArrMthdCd(lookUp(requestJson, "SCGIVRBRIDGE_Request14"));
		t800pecreateextsection.getT800PymtArr().add(t800pymtarr);

		t800pymtarr = new T800PYMTARR();
		t800pymtarr.setT800PymtArrAmt(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request15"))));
		t800pymtarr.setT800PymtArrDt(lookUp(requestJson, "SCGIVRBRIDGE_Request16"));
		t800pymtarr.setT800PymtArrMthdCd(lookUp(requestJson, "SCGIVRBRIDGE_Request17"));
		t800pecreateextsection.getT800PymtArr().add(t800pymtarr);

		t800pymtarr = new T800PYMTARR();
		t800pymtarr.setT800PymtArrAmt(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request18"))));
		t800pymtarr.setT800PymtArrDt(lookUp(requestJson, "SCGIVRBRIDGE_Request19"));
		t800pymtarr.setT800PymtArrMthdCd(lookUp(requestJson, "SCGIVRBRIDGE_Request20"));
		t800pecreateextsection.getT800PymtArr().add(t800pymtarr);

		E92800CCRequest.setT800PeCreateExtSection(t800pecreateextsection);

		RESPONSE response = ServiceProvider.CreatePaymentExtension(E92800CCRequest,configParam);
		if (response != null) {
			responseJsonObj = parseE92800CCResponse(response);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92800CCResponse(RESPONSE E92800Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		PYMTEXTRESPONSEDATA pymtextresponsedata = E92800Response.getPymtExtResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(pymtextresponsedata.getX800ReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(pymtextresponsedata.getX800PymtExtnOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(pymtextresponsedata.getX800ReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(pymtextresponsedata.getX800ErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(pymtextresponsedata.getX800ApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(pymtextresponsedata.getX800SystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(pymtextresponsedata.getX800BaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800CustPhoneNumber().getX800AreaCode()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800CustPhoneNumber().getX800PhoneNumber()+""));		
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800AbeCurBalAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800AbeBillEndDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800Arrears()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800AbeTotNetAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800DebitsCredits()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800RetCheckAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800ThirdPartyCharges()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800TotalExtendableChrgs()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response55", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800CheckDigit()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response56", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800OnCareSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response57", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800CustCellNumber().getX800CellAreaCode()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response58", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800CustCellNumber().getX800CellPhoneNumber()+""));

		if(pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtSect()!=null && pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtSect().size()>0){
			int size = pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtSect().size();
			String[] response17 = new String[size];
			String[] response18 = new String[size];
			String[] response19 = new String[size];
			
			for(int i= 0; i< size; i++) {
				
				X800EXISTINGEXTSECT x800existingextsect = pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtSect().get(i);
				response17[i] = x800existingextsect.getX800PeCurPesDt();
				response18[i] = x800existingextsect.getX800PeCurPesAmt()+"";
				response19[i] = x800existingextsect.getX800PeCurPmtMthdCd();
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response17",response17);
			responseJsonObj.put("SCGIVRBRIDGE_Response18",response18);
			responseJsonObj.put("SCGIVRBRIDGE_Response19",response19);
		}

//		responseJsonObj.put("SCGIVRBRIDGE_Response20", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PymtExtAmt()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response21", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PymtExtDt()));

//		if(pymtextresponsedata.getPymtExtScheduleData().getPymtExtDueMthdDts() !=null && pymtextresponsedata.getPymtExtScheduleData().getPymtExtDueMthdDts().size() > 0){
//			int size = pymtextresponsedata.getPymtExtScheduleData().getPymtExtDueMthdDts().size();
//			String[] response22 = new String[size];
//			String[] response23 = new String[size];
//
//			for(int i= 0; i< size; i++) {
//				response22[i] = getResponseValue(pymtextresponsedata.getPymtExtScheduleData().getPymtExtDueMthdDts().get(i).getX800MethodCode());
//				response23[i] = getResponseValue(pymtextresponsedata.getPymtExtScheduleData().getPymtExtDueMthdDts().get(i).getX800MethodCodeDt());
//			}
//			responseJsonObj.put("SCGIVRBRIDGE_Response22",response22);
//			responseJsonObj.put("SCGIVRBRIDGE_Response23",response23);
//		}
		

//		responseJsonObj.put("SCGIVRBRIDGE_Response24", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800VerifyAmountCd()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response25", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800VerifyDateCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response26", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800MyaccountLinkedSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800CustomerName()));
		responseJsonObj.put("SCGIVRBRIDGE_Response28", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800AddressLine1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response29", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800AddressLine2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response30", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800BillAccountNum()));
		responseJsonObj.put("SCGIVRBRIDGE_Response31", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800Grant48HrNtcSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response32", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800PeId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response59", getResponseStringArray(pymtextresponsedata.getPymtExtCreateData().getX800ServiceZipCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response33", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800RemainingBalance()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response34", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800NumberOfCurrentInst()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response35", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800CustInputAccepted()));
		
		//re-write
		responseJsonObj.put("SCGIVRBRIDGE_Response36", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption1().getX800Opt1ArrAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response37", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption1().getX800Opt1ArrDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response38", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption1().getX800Opt1ArrMthdCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response39", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrAmt1()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response40", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrDt1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response41", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrMthdCd1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response42", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrAmt2()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response43", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrDt2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response44", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800PaymentArrOption2().getX800Opt2ArrMthdCd2()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response45", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrAmt1()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response46", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrDt1()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response47", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrMthdCd1()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response48", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrAmt2()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response49", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrDt2()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response50", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrMthdCd2()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response51", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrAmt3()+""));
//		responseJsonObj.put("SCGIVRBRIDGE_Response52", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrDt3()));
//		responseJsonObj.put("SCGIVRBRIDGE_Response53", getResponseStringArray(pymtextresponsedata.getPymtExtScheduleData().getX800PaymentArrOption3().getX800Opt3ArrMthdCd3()));
		responseJsonObj.put("SCGIVRBRIDGE_Response54", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800NumberOfNewInst()+""));
		
		responseJsonObj.put("SCGIVRBRIDGE_Response60", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtUpdCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response61", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800ExistingExtPeId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response62", getResponseStringArray(pymtextresponsedata.getPymtExtValidationData().getX800AvoidShutoffAmt()+""));

		return responseJsonObj;
		
	}
}
