package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92300CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92300CC.RESPONSE;
import com.scg.ivr.ws.E92300CC.S300SOMFSENDDATA;

public class E92300CCService extends BaseService {

	public static JSONObject createCustomerServiceOrder(JSONObject requestJson) throws SCGMainWSClientException, JSONException{

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92300CCRequest E92300CCRequest = new E92300CCRequest();

		E92300CCRequest.setR300Db2Collname(configParam.getDBName());
		E92300CCRequest.setR300ChannelId(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E92300CCRequest.setR300OperationId(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92300CCRequest.setR300ProcessDate(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E92300CCRequest.setR300BaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request4"))));
		E92300CCRequest.setR300OrderNumber(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request5"))));
		E92300CCRequest.setR300OrderType(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		E92300CCRequest.setR300ChannelMthdTy(lookUp(requestJson, "SCGIVRBRIDGE_Request7"));
		E92300CCRequest.setR300CsrUserid(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));

		RESPONSE E92300CCResponse = ServiceProvider.ViewCancelOrder(E92300CCRequest, configParam);

		if (E92300CCResponse != null) {
			responseJsonObj = parseE92300CCResponse(E92300CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92300CCResponse(RESPONSE E92300CCResponse) throws JSONException{

		JSONObject responseJsonObj = new JSONObject();

		S300SOMFSENDDATA s300somfsenddata = E92300CCResponse.getS300SoMfSendData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(s300somfsenddata.getS300MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(s300somfsenddata.getS300OperationId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(s300somfsenddata.getS300MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(s300somfsenddata.getS300ErrorInfo().getS300SysErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(s300somfsenddata.getS300ErrorInfo().getS300AppErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(s300somfsenddata.getS300ErrorInfo().getS300ErrMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(s300somfsenddata.getS300ViewCsoCloseInfo().getS300NumberOfOrders()+""));

		if(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails() != null && s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().size() > 0){
			int size = s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().size();

			int servDetailsSize = 7;
			String [][] responseArray1 = new String[size][servDetailsSize];
			String [][] responseArray2 = new String[size][servDetailsSize];
			String [][] responseArray3 = new String[size][servDetailsSize];
			String [][] responseArray4 = new String[size][servDetailsSize];
			String [][] responseArray5 = new String[size][servDetailsSize];	

			String[] response7 = new String[size];
			String[] response8 = new String[size];
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];
			String[] response12 = new String[size];
			String[] response18 = new String[size];
			String[] response21 = new String[size];
			String[] response23 = new String[size];

			int accessArrgmtEntrySize = 6;
			String [][] responseArray6 = new String[size][accessArrgmtEntrySize];			

			for(int i= 0; i< size; i++) {
				response7[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300OrderNumber()+"");
				response8[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ScheduleDate());
				response9[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300OrderArrgmtTy());
				response10[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300OrderTmAppt());
				response11[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300OrderType());
				response12[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServCnt()+"");

				if(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails() != null && 
						s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().size() > 0){

					if(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().size() < servDetailsSize){
						servDetailsSize = s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().size();
					}

					for(int j= 0; j< servDetailsSize; j++) {
						responseArray1[i][j] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().get(j).getS300ApplAdjFlg());
						responseArray2[i][j] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().get(j).getS300ApplInopFlg());
						responseArray3[i][j] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().get(j).getS300ApplOtrApplTxt());
						responseArray4[i][j] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().get(j).getS300ReqServCatg());
						responseArray5[i][j] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300ReqServDetails().get(j).getS300ReqServCode());
					}
				}

				response18[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300StopServDate());

				response21[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtCnt()+"");

				if(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtEntry() != null && 
						s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtEntry().size() > 0){

					if(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtEntry().size() < accessArrgmtEntrySize){
						accessArrgmtEntrySize = s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtEntry().size();
					}

					for(int k= 0; k< accessArrgmtEntrySize; k++) {
						responseArray6[i][k] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AccessArrgmtEntry().get(k).getS300AccArrgmtCd());
					}
				}

				response23[i] = getResponseValue(s300somfsenddata.getS300ViewCsoCloseInfo().getS300OrderDetails().get(i).getS300AddtnlAccessTxt());
			}

			responseJsonObj.put("SCGIVRBRIDGE_Response7",response7);
			responseJsonObj.put("SCGIVRBRIDGE_Response8",response8);
			responseJsonObj.put("SCGIVRBRIDGE_Response9",response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10",response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11",response11);
			responseJsonObj.put("SCGIVRBRIDGE_Response12",response12);
			responseJsonObj.put("SCGIVRBRIDGE_Response13",responseArray1);
			responseJsonObj.put("SCGIVRBRIDGE_Response14",responseArray2);
			responseJsonObj.put("SCGIVRBRIDGE_Response15",responseArray3);
			responseJsonObj.put("SCGIVRBRIDGE_Response16",responseArray4);
			responseJsonObj.put("SCGIVRBRIDGE_Response17",responseArray5);
			responseJsonObj.put("SCGIVRBRIDGE_Response18",response18);
			responseJsonObj.put("SCGIVRBRIDGE_Response21",response21);
			responseJsonObj.put("SCGIVRBRIDGE_Response22",responseArray6);
			responseJsonObj.put("SCGIVRBRIDGE_Response23",response23);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response19", getResponseStringArray(s300somfsenddata.getS300CancelInfo().getS300CancelOrderNumber()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response20", getResponseStringArray(s300somfsenddata.getS300CancelInfo().getS300CancellationFlag()));

		responseJsonObj.put("SCGIVRBRIDGE_Response24", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonOrdStatCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response25", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonOrderNo()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response26", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonServStreetAddr1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonServStreetAddr2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response28", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonStartServDate()));
		responseJsonObj.put("SCGIVRBRIDGE_Response29", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonScheduleDate()));
		responseJsonObj.put("SCGIVRBRIDGE_Response30", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonOrderArrgmtTy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response31", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonOrderTmAppt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response32", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtCnt()+""));

		int tonAccessArrgmtEntrySize = 6;
		String[] response33 = new String[tonAccessArrgmtEntrySize];

		if(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtEntry() != null && s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtEntry().size() > 0){

			if(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtEntry().size() < tonAccessArrgmtEntrySize){
				tonAccessArrgmtEntrySize = s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtEntry().size();
			}

			for(int l= 0; l< tonAccessArrgmtEntrySize; l++) {
				response33[l] = getResponseValue(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAccessArrgmtEntry().get(l).getS300TonAccessArrgmtCd());
			}
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response33",response33);

		responseJsonObj.put("SCGIVRBRIDGE_Response34", getResponseStringArray(s300somfsenddata.getS300ViewTransferTonInfo().getS300TonAddtnlAccessTxt()));

		return responseJsonObj;
	}	
}
