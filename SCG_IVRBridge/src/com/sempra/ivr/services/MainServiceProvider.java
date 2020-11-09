package com.sempra.ivr.services;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.scg.ivr.parser.WSConfig;
import com.scg.ivr.parser.WSPropertiesParser;
import com.scg.ivr.util.LoggerUtils;

/**
 * Service class to call the appropriate Web Service method based on the
 * transaction name
 */
public class MainServiceProvider  extends BaseService {

	private static Logger logger = LogManager.getLogger(MainServiceProvider.class);
	public static HashMap<String, WSConfig> hmWSProperties;

	static {
		try {
			hmWSProperties = WSPropertiesParser.parseWSProperties();
		} catch (Exception e) {
			logger.error("Error in parsing WS Config File:" + e);
		}

	}
	/*Release: CEP24 - FNP Sept 15,2017  
	 *Description: Added E92663CC transaction*/
	private static enum transactions {
		E92200CC,E92300CC,E92400CC,E92600CC,E92630CC,E92640CC,E92650CC,E92660CC,E92665CC,
		E92670CC,E92680CC,E92685CC,E92800CC,E93515CC,E93570CC,EW0100CC,EW0300CC,E92661CC,
		E92662CC,E93560CC,CustomerContactService,E93520CC,E93540CC,E92663CC;
	}

	/**
	 * Method to make the appropriate webservice call based on the transaction
	 * name
	 * 
	 * @param localPageContext
	 * @throws Exception
	 */
	/*Release: CEP24 - FNP Sept 15,2017 
	 *Description: Added switch case for E92663CC transaction*/
	public static JSONObject callService(JSONObject requestJson, HttpServletRequest request)
			throws Exception {

		String transactionName = (String) requestJson.getJSONObject("Request").get("SCGIVRBRIDGE_TransName");
		JSONObject responseJsonObj = null;

		try {
			LoggerUtils.debug(transactionName, MainServiceProvider.class, "callService", "callService() TransactionName:" + transactionName);

			switch (transactions.valueOf(transactionName)) {
			case E92200CC:
				responseJsonObj = E92200CCService.createCustomerServiceOrder(requestJson);
				break;
			case E92300CC:
				responseJsonObj = E92300CCService.createCustomerServiceOrder(requestJson);
				break;
			case E92400CC:
				responseJsonObj = E92400CCService.CreateCloseOrder(requestJson);
				break;
			case E92600CC:
				responseJsonObj = E92600CCService.getCreateBillingFunctions(requestJson);
				break;
			case E92630CC:
				responseJsonObj = E92630CCService.CreatePaymentFunctions(requestJson);
				break;
			case E92640CC:
				responseJsonObj = E92640CCService.CreatePaymentFunctions(requestJson);
				break;
			case E92650CC:
				responseJsonObj = E92650CCService.CreateCustReadFunctions(requestJson);
				break;
			case E92660CC:
				responseJsonObj = E92660CCService.getScreenPopDataDetails(requestJson);
				break;
			case E92665CC:
				responseJsonObj = E92665CCService.createCustomerServiceOrder(requestJson);
				break;
			case E92670CC:
				responseJsonObj = E92670CCService.getPrioritiesData(requestJson);
				break;
			case E92680CC:
				responseJsonObj = E92680CCService.createMemoPostData(requestJson);
				break;
			case E92685CC:
				responseJsonObj = E92685CCService.getMemoPostData(requestJson);
				break;
			case E92800CC:
				responseJsonObj = E92800CCService.CreatePaymentExtension(requestJson);
				break;
			case E93515CC:
				responseJsonObj = E93515CCService.getupdateCustomerProfile(requestJson);
				break;
			case E93570CC:
				responseJsonObj = E93570CCService.getBillAcctInfo(requestJson);
				break;
			case EW0100CC:
				responseJsonObj = BillAnalyzerService.getBillAnalyzerInfo(requestJson, request);
				break;
			case EW0300CC:
				responseJsonObj = EW0300CCService.updateSVOCDetails(requestJson);
				break;
			case E92661CC:
				responseJsonObj = E92661CCService.AuthenticateANI(requestJson);
				break;
			case E92662CC:
				responseJsonObj = E92662CCService.HighBillInv(requestJson);
				break;
			case E93560CC:
				responseJsonObj = E93560CCService.getLinkedAcctInfo(requestJson);
				break;
			case CustomerContactService:
				responseJsonObj = CustomerContactService.sendEmailOnDemand(requestJson);
				break;
			case E93520CC:
				responseJsonObj = E93520CCService.getPaperlessPromoElig(requestJson);
				break;
			case E93540CC:
					responseJsonObj = E93540CCService.updatePaperlessPromoEnroll(requestJson);
					break;
			case E92663CC:
				responseJsonObj = E92663CCService.getReconnectAmount(requestJson);
				break;
			default:
				LoggerUtils.error(transactionName, MainServiceProvider.class, "callService", "Unknown Transaction:" + transactionName);
				throw new Exception("Unknown Transaction: " + transactionName);
			}

		} catch (Exception e) {
			LoggerUtils.error(transactionName, MainServiceProvider.class, "callService", "Exception in Webservice: "+ e.getMessage());
			throw e;
		}

		LoggerUtils.debug(transactionName, MainServiceProvider.class, "callService", "End of callService()");
		return responseJsonObj;
	}

	public static WSConfig getWSProperties(String transactionName) {
		LoggerUtils.debug(transactionName, MainServiceProvider.class, "getWSProperties", "transactionName: " + transactionName);
		if (transactionName == null || transactionName.trim().length() == 0) {
			return null;
		}

		LoggerUtils.debug(transactionName, MainServiceProvider.class, "getWSProperties", hmWSProperties.get(transactionName).toString());
		return hmWSProperties.get(transactionName);
	}

	/**
	 * @param localPageContext
	 * @throws Exception
	 */
	public static JSONObject callJDBCService(JSONObject requestJson)
			throws Exception{

		String procedureName = (String) requestJson.getJSONObject("Request").get("SCGIVRBRIDGE_TransName");
		JSONObject responseJsonObj = null;

		try{

			LoggerUtils.debug(procedureName, MainServiceProvider.class, "callJDBCService", "callJDBCService() procedureName:" + procedureName);
			responseJsonObj = SCGJDBCUtilsService.getJdbcUtilsInstance().getSPReqResDataDetails(requestJson);

		}catch (Exception e) {
			LoggerUtils.error(procedureName, MainServiceProvider.class, "callJDBCService", "Exception in StoredProcedure: " + procedureName + " :Error Msg: " + e.getMessage());
			throw e;
		}

		LoggerUtils.debug(procedureName, MainServiceProvider.class, "callJDBCService", "End of callJDBCService()");
		return responseJsonObj;
	}

}
