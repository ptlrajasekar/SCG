package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92661CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92661CC.AuthenticateANIResponse;
import com.scg.ivr.ws.E92661CC.RESPONSE;


public class E92661CCService extends BaseService {
	public static JSONObject AuthenticateANI(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92661CCRequest E92661CCRequest = new E92661CCRequest();

		E92661CCRequest.setDatabaseName(configParam.getDBName());
		E92661CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92661CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92661CCRequest.setReqPhoneNo(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));
				

		RESPONSE E92661CCResponse = ServiceProvider.getAuthenticateANI(E92661CCRequest, configParam);

		if (E92661CCResponse != null) {
			responseJsonObj = parseE92661CCResponse(E92661CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92661CCResponse(RESPONSE E92660CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		AuthenticateANIResponse authaniresponsedata = E92660CCResponse.getAuthenticateANIResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0",getResponseStringArray(authaniresponsedata.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1",getResponseStringArray(authaniresponsedata.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2",getResponseStringArray(authaniresponsedata.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3",getResponseStringArray(authaniresponsedata.getErrorMsg()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4",getResponseStringArray(authaniresponsedata.getApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5",getResponseStringArray(authaniresponsedata.getSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6",getResponseStringArray(authaniresponsedata.getRespPhoneNo()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7",getResponseStringArray(authaniresponsedata.getAuthenticateANIData().getNumberOfAddressReturned()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8",getResponseStringArray(authaniresponsedata.getAuthenticateANIData().getAddressTable()+""));
		

		if(authaniresponsedata.getAuthenticateANIData().getAddressTable() != null && authaniresponsedata.getAuthenticateANIData().getAddressTable().size() > 0){
			int size = authaniresponsedata.getAuthenticateANIData().getNumberOfAddressReturned();
			String[] response8 = new String[size];
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];


			for(int i= 0; i< 10; i++) {
				if(getResponseValue(authaniresponsedata.getAuthenticateANIData().getAddressTable().get(i).getPhoneNo()+"").equalsIgnoreCase(authaniresponsedata.getRespPhoneNo()+"".trim())){
					response8[i] = getResponseValue(authaniresponsedata.getAuthenticateANIData().getAddressTable().get(i).getPhoneNo()+"");
					response9[i] = getResponseValue(authaniresponsedata.getAuthenticateANIData().getAddressTable().get(i).getAccountNumber()+"");
					response10[i] = getResponseValue(authaniresponsedata.getAuthenticateANIData().getAddressTable().get(i).getBaStatCd());
					response11[i] = getResponseValue(authaniresponsedata.getAuthenticateANIData().getAddressTable().get(i).getServDaNbr());	
				}
				
				
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response8",response8);
			responseJsonObj.put("SCGIVRBRIDGE_Response9",response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10",response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11",response11);
		
		}
		
		
		
		return responseJsonObj;
	}
}
