package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92200CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92200CC.R100APPLINFO;
import com.scg.ivr.ws.E92200CC.R100CONTACTPHN;
import com.scg.ivr.ws.E92200CC.R100ORDERINFO;
import com.scg.ivr.ws.E92200CC.RESPONSE;
import com.scg.ivr.ws.E92200CC.S100CSOMFSENDDATA;

public class E92200CCService extends BaseService {

	public static JSONObject createCustomerServiceOrder(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92200CCRequest E92200CCRequest = new E92200CCRequest();
		R100APPLINFO r100applinfo = new R100APPLINFO();
		R100ORDERINFO r100orderinfo = new R100ORDERINFO();
		R100CONTACTPHN r100contactphn = new R100CONTACTPHN();

		E92200CCRequest.setR100Db2Collname(configParam.getDBName());
		E92200CCRequest.setR100ChannelId(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E92200CCRequest.setR100OperationId(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92200CCRequest.setR100ProcessDt(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E92200CCRequest.setR100ProcessTm(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92200CCRequest.setR100BaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request5"))));
		E92200CCRequest.setR100ChannelMthdTy((lookUp(requestJson, "SCGIVRBRIDGE_Request43")));
		E92200CCRequest.setR100CsrUserid((lookUp(requestJson, "SCGIVRBRIDGE_Request44")));
		E92200CCRequest.setR100EmailId((lookUp(requestJson, "SCGIVRBRIDGE_Request45")));	

		r100applinfo.setR100ApplTy1(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		r100applinfo.setR100Appl1InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request7")));
		r100applinfo.setR100Appl1AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request8")));
		r100applinfo.setR100ApplTy2(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		r100applinfo.setR100Appl2InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request10")));
		r100applinfo.setR100Appl2AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request11")));
		r100applinfo.setR100ApplTy3(lookUp(requestJson, "SCGIVRBRIDGE_Request12"));
		r100applinfo.setR100Appl3InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request13")));
		r100applinfo.setR100Appl3AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request14")));
		r100applinfo.setR100ApplTy4(lookUp(requestJson, "SCGIVRBRIDGE_Request15"));
		r100applinfo.setR100Appl4InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request16")));
		r100applinfo.setR100Appl4AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request17")));
		r100applinfo.setR100ApplTy5(lookUp(requestJson, "SCGIVRBRIDGE_Request18"));
		r100applinfo.setR100Appl5InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request19")));
		r100applinfo.setR100Appl5AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request20")));
		r100applinfo.setR100ApplTy6(lookUp(requestJson, "SCGIVRBRIDGE_Request21"));
		r100applinfo.setR100Appl6InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request22")));
		r100applinfo.setR100Appl6AdjFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request23")));
		r100applinfo.setR100ApplTy7(lookUp(requestJson, "SCGIVRBRIDGE_Request24"));
		r100applinfo.setR100Appl7TxtEntered((lookUp(requestJson, "SCGIVRBRIDGE_Request25")));
		r100applinfo.setR100Appl7InopFlg((lookUp(requestJson, "SCGIVRBRIDGE_Request26")));
		r100applinfo.setR100Appl7AdjFlg(lookUp(requestJson, "SCGIVRBRIDGE_Request27"));
		r100applinfo.setR100LightPilotSw((lookUp(requestJson, "SCGIVRBRIDGE_Request28")));
		r100applinfo.setR100FdCommPrepdSw((lookUp(requestJson, "SCGIVRBRIDGE_Request29")));
		r100applinfo.setR100AddApplInfo((lookUp(requestJson, "SCGIVRBRIDGE_Request30")));

		r100orderinfo.setR100OrdrSchdDt((lookUp(requestJson, "SCGIVRBRIDGE_Request31")));
		r100orderinfo.setR100OrdrArrgmtTy((lookUp(requestJson, "SCGIVRBRIDGE_Request32")));
		r100orderinfo.setR100OrdrTmAppt((lookUp(requestJson, "SCGIVRBRIDGE_Request33")));
		r100orderinfo.setR100AccessArrgmt1((lookUp(requestJson, "SCGIVRBRIDGE_Request34")));
		r100orderinfo.setR100AccessArrgmt2((lookUp(requestJson, "SCGIVRBRIDGE_Request35")));
		r100orderinfo.setR100GateFlag((lookUp(requestJson, "SCGIVRBRIDGE_Request46")));
		r100orderinfo.setR100DogInfoFlag((lookUp(requestJson, "SCGIVRBRIDGE_Request47")));
		r100orderinfo.setR100EntryInstrctns((lookUp(requestJson, "SCGIVRBRIDGE_Request36")));
		r100orderinfo.setR100RequestorNm((lookUp(requestJson, "SCGIVRBRIDGE_Request37")));
		r100orderinfo.setR100ContactNm((lookUp(requestJson, "SCGIVRBRIDGE_Request38")));

		r100contactphn.setR100ContactAreaCd((lookUp(requestJson, "SCGIVRBRIDGE_Request39")));
		r100contactphn.setR100ContactPhnNbr((lookUp(requestJson, "SCGIVRBRIDGE_Request40")));

		r100orderinfo.setR100ContactPhn(r100contactphn);

		r100orderinfo.setR100ContactPhnExtn((lookUp(requestJson, "SCGIVRBRIDGE_Request41")));
		r100orderinfo.setR100CrossSt((lookUp(requestJson, "SCGIVRBRIDGE_Request42")));

		E92200CCRequest.setR100ApplInfo(r100applinfo);
		E92200CCRequest.setR100OrderInfo(r100orderinfo);

		RESPONSE E92200CCResponse = ServiceProvider.getCreateCustomerServiceOrder(E92200CCRequest, configParam);

		if (E92200CCResponse != null) {
			responseJsonObj = parseE92200CCResponse(E92200CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92200CCResponse(RESPONSE E92200CCResponse) throws JSONException{

		JSONObject responseJsonObj = new JSONObject();

		S100CSOMFSENDDATA s100csomfsenddata = E92200CCResponse.getS100CsoMfSendData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(s100csomfsenddata.getS100MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(s100csomfsenddata.getS100OperationId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(s100csomfsenddata.getS100MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(s100csomfsenddata.getS100BaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(s100csomfsenddata.getS100ErrorInfo().getS100SysErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(s100csomfsenddata.getS100ErrorInfo().getS100AppErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(s100csomfsenddata.getS100ErrorInfo().getS100ErrMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(s100csomfsenddata.getS100ValdtInfo().getS100FacilityTyCd()));

		if(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule() != null && s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().size() > 0){
			int size = s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().size();
			String[] response8 = new String[size];
			String[] response9 = new String[size];
			String[] response10 = new String[size];
			String[] response11 = new String[size];
			String[] response12 = new String[size];
			String[] response13 = new String[size];

			for(int i= 0; i< size; i++) {
				response8[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100SchdDayDt());
				response9[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100AvailAllDay());
				response10[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100AvailAm());
				response11[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100AvailPm());
				response12[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100AvailEveningWnd());
				response13[i] = getResponseValue(s100csomfsenddata.getS100SchdlInfo().getS100AvailableSchedule().get(i).getS100AvailTa());
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response8",response8);
			responseJsonObj.put("SCGIVRBRIDGE_Response9",response9);
			responseJsonObj.put("SCGIVRBRIDGE_Response10",response10);
			responseJsonObj.put("SCGIVRBRIDGE_Response11",response11);
			responseJsonObj.put("SCGIVRBRIDGE_Response12",response12);
			responseJsonObj.put("SCGIVRBRIDGE_Response13",response13);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(s100csomfsenddata.getS100SchdlInfo().getS100ApplPriority()));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(s100csomfsenddata.getS100ValdtInfo().getS100CustNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(s100csomfsenddata.getS100ValdtInfo().getS100ServAddrLn1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response17", getResponseStringArray(s100csomfsenddata.getS100ValdtInfo().getS100ServAddrLn2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response18", getResponseStringArray(s100csomfsenddata.getS100ValdtInfo().getS100CrossSt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response19", getResponseStringArray(s100csomfsenddata.getS100CreateInfo().getS100OrderConfirmNbr()+""));

		return responseJsonObj;
	}
}
