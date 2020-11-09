package com.sempra.ivr.services;


import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.iContactRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.OnDemandEmailService.CustomerContactResponseMessage;
import com.scg.ivr.ws.OnDemandEmailService.GetCustomerContactResponse;


public class CustomerContactService extends BaseService {
	public static JSONObject sendEmailOnDemand(JSONObject requestJson) throws SCGMainWSClientException, JSONException {
		
		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		
		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));
		
		iContactRequest iContReq = new iContactRequest();
		
			String amount = "";
			String dueDate = "";
			int length = 0;
			iContReq.setTransactionName(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));
			iContReq.setTransactionID(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
			iContReq.setRequestID(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
			iContReq.setEmailInfoto(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
			iContReq.setTemplateID(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
			
		if(lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString() != null 
				&& ( (lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("161"))
						||(lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("162"))
						||(lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("163"))
						||(lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("164"))
						) ){
			iContReq.setMasked_accNum(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
			iContReq.setConfNum(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
			iContReq.setServiceAddress(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
			iContReq.setCustomerName(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
			iContReq.setServiceDate(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		}else if ( lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString() != null && lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("165")){
			iContReq.setMasked_accNum(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
			iContReq.setConfNum(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
			iContReq.setQuickName(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
			iContReq.setCustomerName(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
			amount = lookUp(requestJson, "SCGIVRBRIDGE_Request9");
			dueDate = lookUp(requestJson, "SCGIVRBRIDGE_Request10");
			length = amount.split(",").length;
			if (length ==1 ){
				iContReq.setPaymentAmount1(amount.split(",")[0]);
				iContReq.setDueDate1(dueDate.split(",")[0]);
			}else if(length ==2){
				iContReq.setPaymentAmount1(amount.split(",")[0]);
				iContReq.setDueDate1(dueDate.split(",")[0]);
				iContReq.setPaymentAmount2(amount.split(",")[1]);
				iContReq.setDueDate2(dueDate.split(",")[1]);
			}else if(length ==3){
				iContReq.setPaymentAmount1(amount.split(",")[0]);
				iContReq.setDueDate1(dueDate.split(",")[0]);
				iContReq.setPaymentAmount2(amount.split(",")[1]);
				iContReq.setDueDate2(dueDate.split(",")[1]);
				iContReq.setPaymentAmount3(amount.split(",")[2]);
				iContReq.setDueDate3(dueDate.split(",")[2]);
				
			}
			
		}else if ( lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString() != null && lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("166")){
			iContReq.setMasked_accNum(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
			iContReq.setConfNum(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
			iContReq.setQuickName(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
			iContReq.setCustomerName(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		}else if ( lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString() != null && lookUp(requestJson, "SCGIVRBRIDGE_Request4").toString().equalsIgnoreCase("167")){
			iContReq.setMasked_accNum(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
			iContReq.setConfNum(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
			iContReq.setQuickName(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
			iContReq.setCustomerName(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
			iContReq.setAmount(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
			iContReq.setPayDate(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		}
		
		GetCustomerContactResponse getCustomerContactResponse = ServiceProvider.publishCustomerContactData(iContReq, configParam);
		
		if (getCustomerContactResponse != null) {
			
			responseJsonObj = parseSendEmailOnDemandResponse(getCustomerContactResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		
		return mainResponseJsonObj;
	}

	private static JSONObject parseSendEmailOnDemandResponse(GetCustomerContactResponse customerContactResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		CustomerContactResponseMessage customerContactResponseMessage = customerContactResponse.getCustomerContactResponseMessage();
		responseJsonObj.put("SCGIVRBRIDGE_Response0",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getCampaignId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getTemplateId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getEmailCode()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getEvent().getResult()));
		responseJsonObj.put("SCGIVRBRIDGE_Response4",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getEvent().getCreationTime()+""));
		
		if(customerContactResponseMessage.getMessagePayloadResponse().getEvent().getResult()!=null && !customerContactResponseMessage.getMessagePayloadResponse().getEvent().getResult().trim().equalsIgnoreCase("SUCCESS")){
			responseJsonObj.put("SCGIVRBRIDGE_Response5",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getEvent().getError().getErrorCode()));
			responseJsonObj.put("SCGIVRBRIDGE_Response6",getResponseStringArray(customerContactResponseMessage.getMessagePayloadResponse().getEvent().getError().getReason()));
		}
		
		
		return responseJsonObj;
	}
}

