package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92400CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92400CC.R400ACCESSARRNGMNTINFO;
import com.scg.ivr.ws.E92400CC.R400CLOSEORDEROPER2INFO;
import com.scg.ivr.ws.E92400CC.R400CLOSEORDEROPER3INFO;
import com.scg.ivr.ws.E92400CC.R400CLOSEORDEROPER4INFO;
import com.scg.ivr.ws.E92400CC.R400CLSORDERINFO;
import com.scg.ivr.ws.E92400CC.R400DOMESTICADDR;
import com.scg.ivr.ws.E92400CC.R400DOMESTICADDRSPLIT;
import com.scg.ivr.ws.E92400CC.R400FOREIGNADDR;
import com.scg.ivr.ws.E92400CC.R400OWNERINFO;
import com.scg.ivr.ws.E92400CC.R400REQUESTORINFO;
import com.scg.ivr.ws.E92400CC.RESPONSE;
import com.scg.ivr.ws.E92400CC.S400CLSMFSENDDATA;

public class E92400CCService extends BaseService {

	public static JSONObject CreateCloseOrder(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92400CCRequest E92400CCRequest = new E92400CCRequest();
		R400CLOSEORDEROPER2INFO r400closeorderoper2info = new R400CLOSEORDEROPER2INFO();
		R400CLOSEORDEROPER3INFO r400closeorderoper3info = new R400CLOSEORDEROPER3INFO();
		R400CLOSEORDEROPER4INFO r400closeorderoper4info = new R400CLOSEORDEROPER4INFO();
		R400REQUESTORINFO r400requestorinfo =  new R400REQUESTORINFO();
		R400DOMESTICADDR r400domesticaddr = new R400DOMESTICADDR();
		R400DOMESTICADDRSPLIT r400domesticaddrsplit = new R400DOMESTICADDRSPLIT();
		R400FOREIGNADDR r400foreignaddr = new R400FOREIGNADDR();
		R400OWNERINFO r400ownerinfo = new R400OWNERINFO();
		R400ACCESSARRNGMNTINFO r400accessarrngmntinfo = new R400ACCESSARRNGMNTINFO();
		R400CLSORDERINFO r400clsorderinfo = new R400CLSORDERINFO();

		E92400CCRequest.setR400Db2Collname(configParam.getDBName());
		E92400CCRequest.setR400ChannelId(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		E92400CCRequest.setR400OperationId(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92400CCRequest.setR400AltPathId(lookUp(requestJson, "SCGIVRBRIDGE_Request3"));
		E92400CCRequest.setR400ProcessDate(lookUp(requestJson, "SCGIVRBRIDGE_Request4"));
		E92400CCRequest.setR400ProcessTime(lookUp(requestJson, "SCGIVRBRIDGE_Request5"));
		E92400CCRequest.setR400CsrUserid(lookUp(requestJson, "SCGIVRBRIDGE_Request6"));
		E92400CCRequest.setR400ChannelMthdTy(lookUp(requestJson, "SCGIVRBRIDGE_Request35"));
		E92400CCRequest.setR400BaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request7"))));
		E92400CCRequest.setR400EmailId(lookUp(requestJson, "SCGIVRBRIDGE_Request39"));
		E92400CCRequest.setR400CsaFlag(lookUp(requestJson, "SCGIVRBRIDGE_Request8"));
		E92400CCRequest.setR400PendingTonFlag(lookUp(requestJson, "SCGIVRBRIDGE_Request9"));
		E92400CCRequest.setR400PendingTonSchdDt(lookUp(requestJson, "SCGIVRBRIDGE_Request10"));
		E92400CCRequest.setR400PendingTonBaoDt(lookUp(requestJson, "SCGIVRBRIDGE_Request11"));

		r400closeorderoper2info.setR400SelectedBaoDt(lookUp(requestJson, "SCGIVRBRIDGE_Request12"));
		E92400CCRequest.setR400CloseOrderOper2Info(r400closeorderoper2info);

		r400requestorinfo.setR400RequestorNm(lookUp(requestJson, "SCGIVRBRIDGE_Request38"));
		r400requestorinfo.setR400RequestorPhnNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request40"));
		r400requestorinfo.setR400RequestorPhnExtn(lookUp(requestJson, "SCGIVRBRIDGE_Request41"));
		r400closeorderoper3info.setR400RequestorInfo(r400requestorinfo);

		r400closeorderoper3info.setR400FwdNm(lookUp(requestJson, "SCGIVRBRIDGE_Request37"));
		r400closeorderoper3info.setR400FwdAddrFlag(lookUp(requestJson, "SCGIVRBRIDGE_Request13"));
		r400closeorderoper3info.setR400FwdAddrType(lookUp(requestJson, "SCGIVRBRIDGE_Request14"));

		r400domesticaddr.setR400DomesticAddrLn1(lookUp(requestJson, "SCGIVRBRIDGE_Request15"));
		r400domesticaddr.setR400DomesticCity(lookUp(requestJson, "SCGIVRBRIDGE_Request16"));
		r400domesticaddr.setR400DomesticSt(lookUp(requestJson, "SCGIVRBRIDGE_Request17"));
		r400domesticaddr.setR400DomesticZip(lookUp(requestJson, "SCGIVRBRIDGE_Request18"));
		r400closeorderoper3info.setR400DomesticAddr(r400domesticaddr);

		r400domesticaddrsplit.setR400AddrDaNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request19"));
		r400domesticaddrsplit.setR400AddrDaFracNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request20"));
		r400domesticaddrsplit.setR400AddrPreDrctCd(lookUp(requestJson, "SCGIVRBRIDGE_Request21"));
		r400domesticaddrsplit.setR400AddrStreetNm(lookUp(requestJson, "SCGIVRBRIDGE_Request22"));
		r400domesticaddrsplit.setR400AddrStreetTyCd(lookUp(requestJson, "SCGIVRBRIDGE_Request23"));
		r400domesticaddrsplit.setR400AddrPostDrctCd(lookUp(requestJson, "SCGIVRBRIDGE_Request24"));
		r400domesticaddrsplit.setR400AddrDestTyCd(lookUp(requestJson, "SCGIVRBRIDGE_Request25"));
		r400domesticaddrsplit.setR400AddrDestTxt(lookUp(requestJson, "SCGIVRBRIDGE_Request26"));
		r400domesticaddrsplit.setR400AddrUpobDesc(lookUp(requestJson, "SCGIVRBRIDGE_Request27"));
		r400domesticaddrsplit.setR400AddrCity(lookUp(requestJson, "SCGIVRBRIDGE_Request28"));
		r400domesticaddrsplit.setR400AddrState(lookUp(requestJson, "SCGIVRBRIDGE_Request29"));
		r400domesticaddrsplit.setR400AddrZip(lookUp(requestJson, "SCGIVRBRIDGE_Request30"));
		r400domesticaddrsplit.setR400BypasValidationFlag(lookUp(requestJson, "SCGIVRBRIDGE_Request31"));
		r400closeorderoper3info.setR400DomesticAddrSplit(r400domesticaddrsplit);

		r400foreignaddr.setR400ForeignAddr1(lookUp(requestJson, "SCGIVRBRIDGE_Request42"));
		r400foreignaddr.setR400ForeignAddr2(lookUp(requestJson, "SCGIVRBRIDGE_Request43"));
		r400foreignaddr.setR400ForeignAddr3(lookUp(requestJson, "SCGIVRBRIDGE_Request44"));
		r400closeorderoper3info.setR400ForeignAddr(r400foreignaddr);

		r400ownerinfo.setR400OwnerName(lookUp(requestJson, "SCGIVRBRIDGE_Request45"));
		r400ownerinfo.setR400OwnerAddr(lookUp(requestJson, "SCGIVRBRIDGE_Request46"));
		r400ownerinfo.setR400OwnerCity(lookUp(requestJson, "SCGIVRBRIDGE_Request47"));
		r400ownerinfo.setR400OwnerSt(lookUp(requestJson, "SCGIVRBRIDGE_Request48"));
		r400ownerinfo.setR400OwnerZip(lookUp(requestJson, "SCGIVRBRIDGE_Request49"));
		r400ownerinfo.setR400OwnerPhnNbr(lookUp(requestJson, "SCGIVRBRIDGE_Request50"));
		r400ownerinfo.setR400OwnerPhnExtn(lookUp(requestJson, "SCGIVRBRIDGE_Request51"));
		r400closeorderoper3info.setR400OwnerInfo(r400ownerinfo);

		E92400CCRequest.setR400CloseOrderOper3Info(r400closeorderoper3info);

		r400closeorderoper4info.setR400CrossSt(lookUp(requestJson, "SCGIVRBRIDGE_Request36"));
		r400closeorderoper4info.setR400SelectedOcsDt(lookUp(requestJson, "SCGIVRBRIDGE_Request32"));

		r400accessarrngmntinfo.setR400GateFlag(lookUp(requestJson, "SCGIVRBRIDGE_Request33"));
		r400accessarrngmntinfo.setR400DogInfo(lookUp(requestJson, "SCGIVRBRIDGE_Request34"));
		r400clsorderinfo.setR400AccessArrngmntInfo(r400accessarrngmntinfo);

		r400clsorderinfo.setR400AddtnlAccessTxt(lookUp(requestJson, "SCGIVRBRIDGE_Request52"));
		r400closeorderoper4info.setR400ClsOrderInfo(r400clsorderinfo);

		E92400CCRequest.setR400CloseOrderOper4Info(r400closeorderoper4info);

		RESPONSE response = ServiceProvider.CreateCloseOrder(E92400CCRequest, configParam);

		if (response != null) {
			responseJsonObj = parseE92400CCResponse(response);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92400CCResponse(RESPONSE E92400Response) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		S400CLSMFSENDDATA s400clsmfsenddata = E92400Response.getS400ClsMfSendData();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(s400clsmfsenddata.getS400MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(s400clsmfsenddata.getS400OperationId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(s400clsmfsenddata.getS400MessageId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(s400clsmfsenddata.getS400ErrorInfo().getS400SysErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(s400clsmfsenddata.getS400ErrorInfo().getS400AppErrCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response5", getResponseStringArray(s400clsmfsenddata.getS400ErrorInfo().getS400ErrMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(s400clsmfsenddata.getS400AltPathId()));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServDaNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CsaFlag()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400PendingTonFlag()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400PendingTonSchdDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400PendingTonBaoDt()));

		if(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OffCalendar() != null && s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OffCalendar().size() > 0){
			int size = s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OffCalendar().size();
			String[] response12 = new String[size];
			String[] response13 = new String[size];

			for(int i= 0; i< size; i++) {
				response12[i] = getResponseValue(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OffCalendar().get(i).getS400OffCalendarDt());
				response13[i] = getResponseValue(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OffCalendar().get(i).getS400OpenCloseFlag());
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response12",response12);
			responseJsonObj.put("SCGIVRBRIDGE_Response13",response13);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400OwnerInfoFlag()));

		if(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400OcsCalendar() != null && s400clsmfsenddata.getS400CloseOrderOper2Info().getS400OcsCalendar().size() > 0){
			int size = s400clsmfsenddata.getS400CloseOrderOper2Info().getS400OcsCalendar().size();
			String[] response15 = new String[size];
			String[] response16 = new String[size];

			for(int i= 0; i< size; i++) {
				response15[i] = getResponseValue(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400OcsCalendar().get(i).getS400OcsCalendarDt());
				response16[i] = getResponseValue(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400OcsCalendar().get(i).getS400AllDayWndWndw());
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response15",response15);
			responseJsonObj.put("SCGIVRBRIDGE_Response16",response16);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response17", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1Status()));
		responseJsonObj.put("SCGIVRBRIDGE_Response18", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1DaNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response19", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1DaFracNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response20", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1PreDrctCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response21", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1StreetNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response22", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1StreetTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response23", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1PostDrctCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response24", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1DestTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response25", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1DestTxt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response26", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1UpobDesc()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1City()));
		responseJsonObj.put("SCGIVRBRIDGE_Response28", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1State()));
		responseJsonObj.put("SCGIVRBRIDGE_Response29", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400Grp1Zip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response30", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper3Info().getS400Grp1RecomndAddr().getS400BypasValidationFlag()));
		responseJsonObj.put("SCGIVRBRIDGE_Response31", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper4Info().getS400ConfirmationNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response32", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400CrossSt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response33", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400SelectedBaoDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response34", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400CustNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response35", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServDaNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response36", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServDaFracNbr()));
		responseJsonObj.put("SCGIVRBRIDGE_Response37", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServPreDrctCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response38", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServStreetNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response39", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServStreetTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response40", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServPostDrctCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response41", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServDestTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response42", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServDestTxt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response43", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400DomesticAddr().getS400DomesticAddrLn1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response44", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400DomesticAddr().getS400DomesticCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response45", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400DomesticAddr().getS400DomesticSt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response46", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400DomesticAddr().getS400DomesticZip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response47", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400ForeignAddr().getS400ForeignAddrLn1()));
		responseJsonObj.put("SCGIVRBRIDGE_Response48", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400ForeignAddr().getS400ForeignAddrLn2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response49", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400ForeignAddr().getS400ForeignAddrLn3()));
		responseJsonObj.put("SCGIVRBRIDGE_Response50", getResponseStringArray(s400clsmfsenddata.getS400BaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response51", getResponseStringArray(s400clsmfsenddata.getS400InfoCd().getS400InfoCd2()));
		responseJsonObj.put("SCGIVRBRIDGE_Response52", getResponseStringArray(s400clsmfsenddata.getS400InfoCd().getS400InfoCd4()));
		responseJsonObj.put("SCGIVRBRIDGE_Response53", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServCity()));
		responseJsonObj.put("SCGIVRBRIDGE_Response54", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServState()));
		responseJsonObj.put("SCGIVRBRIDGE_Response55", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper1Info().getS400CustInfo().getS400ServAddrZip()));
		responseJsonObj.put("SCGIVRBRIDGE_Response56", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400SelectedOcsDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response57", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400FwdNm()));
		responseJsonObj.put("SCGIVRBRIDGE_Response58", getResponseStringArray(s400clsmfsenddata.getS400CloseOrderOper2Info().getS400FwdAddrType()));

		return responseJsonObj;
	}
}
