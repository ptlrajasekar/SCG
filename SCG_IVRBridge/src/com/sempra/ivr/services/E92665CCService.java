package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.E92665CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.E92665CC.RESPONSE;
import com.scg.ivr.ws.E92665CC.TailoredTreatmentsResponse;

public class E92665CCService extends BaseService {

	public static JSONObject createCustomerServiceOrder(JSONObject requestJson) throws SCGMainWSClientException, JSONException {

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = null;
		JSONObject mainResponseJsonObj = null;

		E92665CCRequest E92665CCRequest = new E92665CCRequest();

		E92665CCRequest.setDatabaseName(configParam.getDBName());
		E92665CCRequest.setReqOperationCd(Short.parseShort(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request1"))));
		E92665CCRequest.setChannelType(lookUp(requestJson, "SCGIVRBRIDGE_Request2"));
		E92665CCRequest.setReqBaId(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request3"))));

		RESPONSE E92665CCResponse = ServiceProvider.GetTailoredTreatmentsData(E92665CCRequest, configParam);

		if (E92665CCResponse != null) {
			responseJsonObj = parseE92665CCResponse(E92665CCResponse);
			mainResponseJsonObj = new JSONObject();
			mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
			mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
			mainResponseJsonObj.put("Response", responseJsonObj);
		}
		return mainResponseJsonObj;
	}

	private static JSONObject parseE92665CCResponse(RESPONSE E92665CCResponse) throws JSONException {

		JSONObject responseJsonObj = new JSONObject();

		TailoredTreatmentsResponse treatmentsResponse = E92665CCResponse.getTailoredTreatmentsResponse();

		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(treatmentsResponse.getReturnCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(treatmentsResponse.getRespOperationCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response2", getResponseStringArray(treatmentsResponse.getErrorMsg()));
		responseJsonObj.put("SCGIVRBRIDGE_Response3", getResponseStringArray(treatmentsResponse.getSystemErrorCd()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response4", getResponseStringArray(treatmentsResponse.getBillingInformation().getHasOverdueCharges()));
		responseJsonObj.put("SCGIVRBRIDGE_Response5",getResponseStringArray(treatmentsResponse.getRespBaId()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response6", getResponseStringArray(treatmentsResponse.getBillingInformation().getNumberOfPayments()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response7", getResponseStringArray(treatmentsResponse.getBillingInformation().getAccountOnMyAcct()));
		responseJsonObj.put("SCGIVRBRIDGE_Response8", getResponseStringArray(treatmentsResponse.getBillingInformation().getPaymentsMadeOnMyAcct()));
		responseJsonObj.put("SCGIVRBRIDGE_Response9", getResponseStringArray(treatmentsResponse.getBillingInformation().getPaymentsMadeAtApl()));
		responseJsonObj.put("SCGIVRBRIDGE_Response10", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfSeasonalOrders()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response11", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfPendingOrders()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response12", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfCurrInstallment()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response13", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfPePa6Months()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response14", getResponseStringArray(treatmentsResponse.getBillingInformation().getPendingPaymentAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response15", getResponseStringArray(treatmentsResponse.getBillingInformation().getAbeCurBalAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(treatmentsResponse.getBillingInformation().getCurDueDt()));

		
		if(treatmentsResponse.getServiceOrders().getSeasonalOrders() != null && treatmentsResponse.getServiceOrders().getSeasonalOrders().size() > 0){
			//			int size = treatmentsResponse.getServiceOrders().getSeasonalOrders().size();
			int size = 3;
			//			String[] response17 = new String[size];

			//set limit to size once tested
			for(int i= 0; i< size; i++) {
				String SeasonalTakenDt = treatmentsResponse.getServiceOrders().getSeasonalOrders().get(i).getSeasonalTakenDt();
				String strSeasonalTakenBy = treatmentsResponse.getServiceOrders().getSeasonalOrders().get(i).getSeasonalTakenBy();
				String strSeasonalOrdType = treatmentsResponse.getServiceOrders().getSeasonalOrders().get(i).getSeasonalOrdType();
				String strSeasonalOrdStatus = treatmentsResponse.getServiceOrders().getSeasonalOrders().get(i).getSeasonalOrdStatus();
				responseJsonObj.put("SCGIVRBRIDGE_Response"+(17+i), getResponseStringArray(SeasonalTakenDt));
				responseJsonObj.put("SCGIVRBRIDGE_Response"+(81+i), getResponseStringArray(strSeasonalTakenBy));
				responseJsonObj.put("SCGIVRBRIDGE_Response"+(84+i), getResponseStringArray(strSeasonalOrdType));
				responseJsonObj.put("SCGIVRBRIDGE_Response"+(87+i), getResponseStringArray(strSeasonalOrdStatus));
				//				response17[i] = getResponseValue(SeasonalTakenDt);
			}
			//			responseJsonObj.put("SCGIVRBRIDGE_Response17",response17);

		}

		if(treatmentsResponse.getBillingInformation().getPaymentTable() != null && treatmentsResponse.getBillingInformation().getPaymentTable().size() >0){
			int size = treatmentsResponse.getBillingInformation().getPaymentTable().size();
			String[] response20 = new String[size];
			String[] response90 = new String[size];
			String[] response91 = new String[size];
			for(int i= 0; i< size; i++) {
				String paymentSource = treatmentsResponse.getBillingInformation().getPaymentTable().get(i).getPaymentSource();
				String strPaymentDt = treatmentsResponse.getBillingInformation().getPaymentTable().get(i).getPaymentDt();
				String strPaymentAmt = treatmentsResponse.getBillingInformation().getPaymentTable().get(i).getPaymentAmt()+"";
				response20[i] = getResponseValue(paymentSource);
				response90[i] = getResponseValue(strPaymentDt);
				response91[i] = getResponseValue(strPaymentAmt);
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response20",response20);
			responseJsonObj.put("SCGIVRBRIDGE_Response90", response90);
			responseJsonObj.put("SCGIVRBRIDGE_Response91", response91);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response21", getResponseStringArray(treatmentsResponse.getBillingInformation().getCurDueAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response22", getResponseStringArray(treatmentsResponse.getBillingInformation().getArrears()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response23", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAccountOnMyAccount()));
		responseJsonObj.put("SCGIVRBRIDGE_Response24", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getPaperlessBill()));
		responseJsonObj.put("SCGIVRBRIDGE_Response25", getResponseStringArray(treatmentsResponse.getBillingInformation().getPayByPhone()));

		responseJsonObj.put("SCGIVRBRIDGE_Response26", getResponseStringArray(treatmentsResponse.getBillingInformation().getAbeBillEndDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response27", getResponseStringArray(treatmentsResponse.getBillingInformation().getAbeTotNetAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response28", getResponseStringArray(treatmentsResponse.getBillingInformation().getPrevAbeCurBalAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response29", getResponseStringArray(treatmentsResponse.getBillingInformation().getPrevAbeTotNetAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response30", getResponseStringArray(treatmentsResponse.getBillingInformation().getThirdPartyCharges()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response31", getResponseStringArray(treatmentsResponse.getBillingInformation().getDebitsCredits()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response32", getResponseStringArray(treatmentsResponse.getBillingInformation().getCurDueAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response33", getResponseStringArray(treatmentsResponse.getBillingInformation().getReturnCheckAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response34", getResponseStringArray(treatmentsResponse.getBillingInformation().getPendingPaymentDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response35", getResponseStringArray(treatmentsResponse.getBillingInformation().getTransferredAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response36", getResponseStringArray(treatmentsResponse.getBillingInformation().getTransferredToBillAcct()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response37", getResponseStringArray(treatmentsResponse.getBillingInformation().getAccountOnSimplePay()));
		responseJsonObj.put("SCGIVRBRIDGE_Response38", getResponseStringArray(treatmentsResponse.getBillingInformation().getPaymentSchedDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response39", getResponseStringArray(treatmentsResponse.getBillingInformation().getPaymentSchedAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response40", getResponseStringArray(treatmentsResponse.getBillingInformation().getBaStatCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response41", getResponseStringArray(treatmentsResponse.getBillingInformation().getBaOpenDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response42", getResponseStringArray(treatmentsResponse.getBillingInformation().getBaBilledSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response43", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getOnCare()));
		responseJsonObj.put("SCGIVRBRIDGE_Response44", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCareStage()));
		responseJsonObj.put("SCGIVRBRIDGE_Response45", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCareStatus()));
		responseJsonObj.put("SCGIVRBRIDGE_Response46", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCareStatusDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response47", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCareDocExpDt()));
		responseJsonObj.put("SCGIVRBRIDGE_Response48", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getLppSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response49", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAgencyPledgePending()));
		responseJsonObj.put("SCGIVRBRIDGE_Response50", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAgencyPledgeAmt()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response51", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAgency()));
		responseJsonObj.put("SCGIVRBRIDGE_Response52", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getMedBaseline()));
		responseJsonObj.put("SCGIVRBRIDGE_Response53", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getLifeSupport()));
		responseJsonObj.put("SCGIVRBRIDGE_Response54", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCustIsSeniorCitizen()));
		responseJsonObj.put("SCGIVRBRIDGE_Response55", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getRate()));
		responseJsonObj.put("SCGIVRBRIDGE_Response56", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getPrizmCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response57", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCustSegment()));
		responseJsonObj.put("SCGIVRBRIDGE_Response58", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAccountOnRecurPay()));
		responseJsonObj.put("SCGIVRBRIDGE_Response59", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getBouncedEmailSw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response60", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getAmInstallReadySw()));
		responseJsonObj.put("SCGIVRBRIDGE_Response61", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getCfTyCd()));
		responseJsonObj.put("SCGIVRBRIDGE_Response62", getResponseStringArray(treatmentsResponse.getServiceOrders().getPePaInLastYear()));
		responseJsonObj.put("SCGIVRBRIDGE_Response63", getResponseStringArray(treatmentsResponse.getServiceOrders().getPePaExpNotMet()));
		responseJsonObj.put("SCGIVRBRIDGE_Response64", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfPePa6Months()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response65", getResponseStringArray(treatmentsResponse.getServiceOrders().getActivePePaTakenBy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response66", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfCurrInstallment()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response67", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfIVRRequests()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response68", getResponseStringArray(treatmentsResponse.getServiceOrders().getNumberOfWEBRequests()+""));
		responseJsonObj.put("SCGIVRBRIDGE_Response69", getResponseStringArray(treatmentsResponse.getServiceOrders().getSeasonalOrders()+""));
		// CEP3 - RAMP - July 2020 Release - New response field(PhoneCellVerifiedTimeStamp)
		responseJsonObj.put("SCGIVRBRIDGE_Response92", getResponseStringArray(treatmentsResponse.getProgramsEnrolled().getPhoneCellVerifTs()));
		
		if(treatmentsResponse.getServiceOrders().getPendingServiceOrders() != null && treatmentsResponse.getServiceOrders().getPendingServiceOrders().size() >0){
			int size = treatmentsResponse.getServiceOrders().getPendingServiceOrders().size();
			String[] response70 = new String[size];
			String[] response71 = new String[size];
			String[] response72 = new String[size];
			String[] response73 = new String[size];

			for(int i= 0; i< size; i++) {
				String pendingOrderType = treatmentsResponse.getServiceOrders().getPendingServiceOrders().get(i).getPendingOrderType();
				String pendingOrderSchedDt = treatmentsResponse.getServiceOrders().getPendingServiceOrders().get(i).getPendingOrderSchedDt();
				String pendingOrderTakenBy = treatmentsResponse.getServiceOrders().getPendingServiceOrders().get(i).getPendingOrderTakenBy();
				String pendingOrderStatus = treatmentsResponse.getServiceOrders().getPendingServiceOrders().get(i).getPendingOrderStatus();

				response70[i] = getResponseValue(pendingOrderType);
				response71[i] = getResponseValue(pendingOrderSchedDt);
				response72[i] = getResponseValue(pendingOrderTakenBy);
				response73[i] = getResponseValue(pendingOrderStatus);
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response70",response70);
			responseJsonObj.put("SCGIVRBRIDGE_Response71", response71);
			responseJsonObj.put("SCGIVRBRIDGE_Response72", response72);
			responseJsonObj.put("SCGIVRBRIDGE_Response73", response73);
		}

		responseJsonObj.put("SCGIVRBRIDGE_Response74", getResponseStringArray(treatmentsResponse.getServiceOrders().getMostRecentServiceOrder().getMostRecentOrderType()));
		responseJsonObj.put("SCGIVRBRIDGE_Response75", getResponseStringArray(treatmentsResponse.getServiceOrders().getMostRecentServiceOrder().getMostRecentOrderStatus()));
		responseJsonObj.put("SCGIVRBRIDGE_Response76", getResponseStringArray(treatmentsResponse.getServiceOrders().getMostRecentServiceOrder().getMostRecentOrderTakenBy()));
		responseJsonObj.put("SCGIVRBRIDGE_Response77", getResponseStringArray(treatmentsResponse.getServiceOrders().getMostRecentServiceOrder().getMostRecentOrderSchedDt()));

		if(treatmentsResponse.getServiceOrders().getExistingExtSect() != null && treatmentsResponse.getServiceOrders().getExistingExtSect().size() >0){
			int size = treatmentsResponse.getServiceOrders().getExistingExtSect().size();
			String[] response78 = new String[size];
			String[] response79 = new String[size];
			String[] response80 = new String[size];

			for(int i= 0; i< size; i++) {
				String strPeCurrPesDt = treatmentsResponse.getServiceOrders().getExistingExtSect().get(i).getPeCurrPesDt();
				String strPeCurrPesAmt = treatmentsResponse.getServiceOrders().getExistingExtSect().get(i).getPeCurrPesAmt()+"";
				String strPeCurrPmtMthdCd = treatmentsResponse.getServiceOrders().getExistingExtSect().get(i).getPeCurrPmtMthdCd();

				response78[i] = getResponseValue(strPeCurrPesDt);
				response79[i] = getResponseValue(strPeCurrPesAmt);
				response80[i] = getResponseValue(strPeCurrPmtMthdCd);
			}
			responseJsonObj.put("SCGIVRBRIDGE_Response78",response78);
			responseJsonObj.put("SCGIVRBRIDGE_Response79", response79);
			responseJsonObj.put("SCGIVRBRIDGE_Response80", response80);
		}

		return responseJsonObj;
	}
}
