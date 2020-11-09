package com.sempra.ivr.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.scg.ivr.beans.request.EW0100CCRequest;
import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.services.ServiceProvider;
import com.scg.ivr.ws.BillModelService.GetBillHighlightsResponse;
import com.scg.ivr.ws.CustomerInformationService.PublishCustomerBillDataResponse;
import com.scg.ivr.ws.EW0100CC.BillAccountRequest;
import com.scg.ivr.ws.EW0100CC.RESPONSE;

public class BillAnalyzerService extends BaseService {

	public static JSONObject getBillAnalyzerInfo(JSONObject requestJson, HttpServletRequest request) throws SCGMainWSClientException, JSONException{

		WSConfig configParam = MainServiceProvider.getWSProperties(lookUp(requestJson, "SCGIVRBRIDGE_TransName"));

		JSONObject responseJsonObj = new JSONObject();

		String transactionID = request.getSession().getId(); // Session ID of servlet.

		EW0100CCRequest ew0100ccRequest = new EW0100CCRequest();
		BillAccountRequest accountRequest = new BillAccountRequest();

		accountRequest.setReqDb2Collection(configParam.getDBName());
		accountRequest.setReqProcessCode(lookUp(requestJson, "SCGIVRBRIDGE_Request1"));
		accountRequest.setReqBillAccountID(Long.parseLong(setEmptyValue(lookUp(requestJson, "SCGIVRBRIDGE_Request2"))));		

		ew0100ccRequest.setBillAccountRequest(accountRequest);

		String sessionID = lookUp(requestJson, "SCGIVRBRIDGE_Request3");
		sessionID = sessionID.substring(1, sessionID.length()-1); // Genesys IVR session ID

		ArrayList<String> dateArray = new ArrayList<String>();
		responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));

		RESPONSE response  = ServiceProvider.billAccountResponse(ew0100ccRequest, configParam, transactionID, sessionID);
		if(response != null && response.getMessageHeaderResponse() != null){
			if(response.getMessageHeaderResponse().getResponseCode().equals("0")){
				int billArraySize = response.getBillAccountResponse().getUser().getBill().size();
				if(billArraySize >= 2){
					String strBlBillDate = "";
					for(int i= 0 ; i < billArraySize ; i++){
						strBlBillDate = response.getBillAccountResponse().getUser().getBill().get(i).getBlBillDate();
						dateArray.add(strBlBillDate);
					}
					Comparator<String> comparator = Collections.reverseOrder();
					Collections.sort(dateArray,comparator);//Descending sorting using sort method

					configParam = MainServiceProvider.getWSProperties("BILLANALYZER");
					accountRequest.setReqDb2Collection(configParam.getDBName());

					PublishCustomerBillDataResponse publishCustomerBillDataResponse = ServiceProvider.publishCustomerBillData(ew0100ccRequest, configParam, transactionID);
					if(publishCustomerBillDataResponse != null){
						if(publishCustomerBillDataResponse.getResponse().getResponseCode().equals(new BigInteger("0"))){

							GetBillHighlightsResponse getBillHighlightsResponse = ServiceProvider.getBillHighlightsResponse(ew0100ccRequest,configParam, publishCustomerBillDataResponse, dateArray);
							if (getBillHighlightsResponse != null) {
								responseJsonObj = parseBillAnalyzerResponse(getBillHighlightsResponse);

							}else{
								responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
								responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray("BillModelService Response data is null."));
							}
						}else {
							responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(publishCustomerBillDataResponse.getResponse().getResponseCode()+""));
							responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(publishCustomerBillDataResponse.getResponse().getResponseMessage()));

						}
					}else{
						responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
						responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray("CustomerInformationService Response data is null."));
					}
				}else{
					responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
					responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray("EW0100CC Response does not have at least 2 past bills."));
				}

			}else{
				responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray(response.getMessageHeaderResponse().getResponseCode()));
				responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(response.getMessageHeaderResponse().getResponseMessage()));
			}

		}else{
			responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
			responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray("EW0100CC Response data is null."));
		}

		JSONObject mainResponseJsonObj = new JSONObject();
		mainResponseJsonObj.put("SCGIVRBRIDGE_RequestXML", com.scg.ivr.util.LoggingHandler.getSOAPReqData());
		mainResponseJsonObj.put("SCGIVRBRIDGE_ResponseXML", com.scg.ivr.util.LoggingHandler.getSOAPRespData());
		mainResponseJsonObj.put("Response", responseJsonObj);

		return mainResponseJsonObj;
	}

	private static JSONObject parseBillAnalyzerResponse(GetBillHighlightsResponse parseBillAnalyzerResponse) throws JSONException{

		JSONObject responseJsonObj = new JSONObject();

		if(parseBillAnalyzerResponse.getGetBillHighlightsResult() != null && parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getStatus().toString().equalsIgnoreCase("success")){
			responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("0"));
			responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(""));

			int billHighlightSize = 0; 
			if(parseBillAnalyzerResponse.getGetBillHighlightsResult() != null && parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights() != null && parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight() != null){
				billHighlightSize = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().size();
			}

			ArrayList<Double> currencyArray = new ArrayList<Double>();
			for (int i=0; i< billHighlightSize;i++){
				currencyArray.add(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(i).getDetail().getValue().getImpact().getValue().getAmountCurrency());
			}

			Comparator<Double> comparator = Collections.reverseOrder();
			Collections.sort(currencyArray,comparator); //Descending sorting using sort method so that highest positive values come on top

			String [][] responseArray = new String[billHighlightSize][14];
			int addedResp = 0;

			//first adding all the positive values from highest to lowest
			for(int i =0; i < billHighlightSize ; i++ ){
				for (int j =0; j < billHighlightSize; j++){
					if(currencyArray.get(i) >= 0 && (currencyArray.get(i) == parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountCurrency())){
						responseArray[addedResp][0] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getBillHighlightType()+"";
						responseArray[addedResp][1] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getContext() + "";
						responseArray[addedResp][2] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getFuel() + "";
						responseArray[addedResp][3] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountCurrency() + "";
						responseArray[addedResp][4] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountPercent() + "";
						responseArray[addedResp][5] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasAmountCurrency() + "";
						responseArray[addedResp][6] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasAmountPercent() + "";
						responseArray[addedResp][7] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasMeterID() + "";
						responseArray[addedResp][8] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasRateClass() + "";
						responseArray[addedResp][9] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasReadingType() + "";
						responseArray[addedResp][10] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getImpactType() + "";
						if(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getMeterID().getValue() != null){
							responseArray[addedResp][11] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getMeterID().getValue();
						}else{
							responseArray[addedResp][11] = "";
						}
						if(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getRateClass().getValue()!=null){
							responseArray[addedResp][12] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getRateClass().getValue();
						}else{
							responseArray[addedResp][12] = "";
						}
						responseArray[addedResp][13] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getReadingType() + "";
						addedResp++;
					}
				}
			}

			Collections.sort(currencyArray); //Again sorting in opposite direction so that lowest negative values come on top

			//now adding all the negative values from lowest to highest. Bigger negative value will come first			
			for(int i =0; i < billHighlightSize ; i++ ){
				for (int j =0; j < billHighlightSize; j++){
					if(currencyArray.get(i) < 0 && (currencyArray.get(i) == parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountCurrency())){
						responseArray[addedResp][0] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getBillHighlightType()+"";
						responseArray[addedResp][1] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getContext() + "";
						responseArray[addedResp][2] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getFuel() + "";
						responseArray[addedResp][3] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountCurrency() + "";
						responseArray[addedResp][4] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getAmountPercent() + "";
						responseArray[addedResp][5] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasAmountCurrency() + "";
						responseArray[addedResp][6] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasAmountPercent() + "";
						responseArray[addedResp][7] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasMeterID() + "";
						responseArray[addedResp][8] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasRateClass() + "";
						responseArray[addedResp][9] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().isHasReadingType() + "";
						responseArray[addedResp][10] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getImpactType() + "";
						if(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getMeterID().getValue() != null){
							responseArray[addedResp][11] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getMeterID().getValue();
						}else{
							responseArray[addedResp][11] = "";
						}
						if(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getRateClass().getValue()!=null){
							responseArray[addedResp][12] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getRateClass().getValue();
						}else{
							responseArray[addedResp][12] = "";
						}
						responseArray[addedResp][13] = parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getBillHighlights().getValue().getBillHighlight().get(j).getDetail().getValue().getImpact().getValue().getReadingType() + "";
						addedResp++;
					}
				}
			}

			if(billHighlightSize > 0){			
				for(int l=0; l<14; l++){
					String[] response1 = new String[billHighlightSize];
					for(int k=0; k<billHighlightSize; k++){
						response1[k] = responseArray[k][l];
					}
					responseJsonObj.put("SCGIVRBRIDGE_Response"+(l+2), response1);
				}
			}

			responseJsonObj.put("SCGIVRBRIDGE_Response16", getResponseStringArray(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getSelectedBillDate().getValue()));
			responseJsonObj.put("SCGIVRBRIDGE_Response17", getResponseStringArray(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getComparedBillDate().getValue()));

		}else{
			responseJsonObj.put("SCGIVRBRIDGE_Response0", getResponseStringArray("1"));
			if(parseBillAnalyzerResponse.getGetBillHighlightsResult() != null && parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getStatusDetails().getValue().getStatusDetail().size() != 0){
				responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(parseBillAnalyzerResponse.getGetBillHighlightsResult().getValue().getStatusDetails().getValue().getStatusDetail().get(0).getMessage().getValue()));
			}else{
				responseJsonObj.put("SCGIVRBRIDGE_Response1", getResponseStringArray(""));
			}
		}

		return responseJsonObj;
	}	
}
