package com.sempra.ivr.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.scg.ivr.exception.SCGMainWSClientException;
import com.scg.ivr.util.LoggerUtils;

public class SCG_IVRBridge extends HttpServlet  {	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(SCG_IVRBridge.class);

	public SCG_IVRBridge()
	{
		super();       
	}

	public void init() throws ServletException
	{
		super.init();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

		try {			    
			ObjectInputStream objIn = new ObjectInputStream(request.getInputStream());
			JSONObject requestJson = new JSONObject((String)objIn.readObject());
			JSONObject responseJson = null;

			String transName = (String)requestJson.getJSONObject("Request").get("SCGIVRBRIDGE_TransName");

			if(transName != null && !transName.trim().equals("")){
				LoggerUtils.debug(transName, SCG_IVRBridge.class, "doPost", "Sending request for transaction name: " + transName);

				if ( transName.startsWith("E9") || transName.startsWith("EW") || transName.startsWith("Customer")) {   
					responseJson = com.sempra.ivr.services.MainServiceProvider.callService(requestJson, request);

				}else{
					responseJson = com.sempra.ivr.services.MainServiceProvider.callJDBCService(requestJson);
				}

				LoggerUtils.debug(transName, SCG_IVRBridge.class, "doPost", "Response recieved for transaction name: " + transName);

				ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
				response.setContentType("application/json");
				out.writeObject(responseJson.toString());

			} else {
				SCGMainWSClientException ex = new SCGMainWSClientException("Transcation Name is empty or null");
				throw ex;
			}

		} catch (Exception e) {
			logger.error("doPost | Exception in SCG IVR Bridge: "+ e.getMessage());
		}
	}

	public void destroy()
	{
		super.destroy();
	}
}