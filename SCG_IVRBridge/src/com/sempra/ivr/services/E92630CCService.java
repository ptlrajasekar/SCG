package com.sempra.ivr.services;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92630CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92630CC.PAYMENTSERVICESRESPONSEDATA;
import com.scg.ivr.ws.E92630CC.RESPONSE;

public class E92630CCService extends BaseService {

	public static JSONObject CreatePaymentFunctions(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92630CCRequest E92630CCRequest = new E92630CCRequest();

		E92630CCRequest.setReqDatabaseName(configParam.getDBName());
		E92630CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92630CCRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92630CCRequest.setReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
		E92630CCRequest.setReqSimplePayType(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92630CCRequest.setReqAmount(new BigDecimal(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request5"))));
		E92630CCRequest.setReqAgencyTyCd(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		E92630CCRequest.setReqCustBankAcctNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
		E92630CCRequest.setReqLanguageCd(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		E92630CCRequest.setReqSimplePayTermDt(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));

		RESPONSE response = ServiceProvider.CreatePaymentFunctions(E92630CCRequest,configParam);

		if (response != null) {
			responseJsonObj = parseE92630CCResponse(response);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92630CCResponse(RESPONSE E92630Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		PAYMENTSERVICESRESPONSEDATA paymentservicesresponsedata = E92630Response.getPaymentServicesResponseData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(paymentservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(paymentservicesresponsedata.getRespPymtServOperCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(paymentservicesresponsedata.getRespReturnCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(paymentservicesresponsedata.getRespErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(paymentservicesresponsedata.getRespApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(paymentservicesresponsedata.getRespSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(paymentservicesresponsedata.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(paymentservicesresponsedata.getSimplePayData().getRespSimplePayDebitAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(paymentservicesresponsedata.getSimplePayData().getRespSimplePayNxtSchedDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(paymentservicesresponsedata.getSimplePayData().getRespSimplePayStatus()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespVrfyBanAcctMatchSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayMaxPymtAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayMinColAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayCcInFldSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayConfirmationNbr().getRespDmdCnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayConfirmationNbr().getRespDmdCnJulianDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayConfirmationNbr().getRespDmdCnDollarRange()));
		responseJsonObj.put("SCGIVRBRIDGE_Response17", getResponseStringArray(paymentservicesresponsedata.getDemandPayData().getRespDmdPayConfirmationNbr().getRespDmdCnCkDigit()));

		return responseJsonObj;
	}
}
