package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92663CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92663CC.FnpAmountResponse;
import com.scg.ivr.ws.E92663CC.RESPONSE;

/*Release: CEP24 - FNP ; Sept 15,2017*/
/**
 * @author Pointel Inc
 *
 */
public class E92663CCService extends BaseService {

	/**
	 * @param requestJson
	 * @return
	 * @throws SCGMainWSClientException
	 * @throws JSONException
	 */
	public static JSONObject getReconnectAmount(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig e92663ccWSConfig = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92663CCRequest e92663ccRequest = new E92663CCRequest();

		e92663ccRequest.setReqDatabaseName(e92663ccWSConfig.getDBName());
		e92663ccRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		e92663ccRequest.setReqChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		e92663ccRequest.setReqAccountId(Long.parseLong(lookUp(requestJson, "SCGIVRBRIDGE_Request3")));

		RESPONSE E92663CCResponse = ServiceProvider.getReconnectAmount(e92663ccRequest, e92663ccWSConfig);

		if (E92663CCResponse != null) {
			responseJsonObj = parseE92663CCResponse(E92663CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	/**
	 * @param E92663CCResponse
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject parseE92663CCResponse(RESPONSE E92663CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		FnpAmountResponse fnpAmountResponse = E92663CCResponse.getFnpAmountResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(fnpAmountResponse.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(fnpAmountResponse.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(fnpAmountResponse.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(fnpAmountResponse.getApplErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(fnpAmountResponse.getSystemErrorCd()+""));		
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(fnpAmountResponse.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getDelinqAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getAddlDepositAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getPendPaymentAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getPaymentAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getTotalPaymentAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getReconnectFeeAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(fnpAmountResponse.getFnpAmountData().getTotAmountToReconnect()+""));

		return responseJsonObj;
	}
}
