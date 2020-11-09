package com.sempra.ivr.services;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseService {

	protected static String lookUp(JSONObject json, String str) throws JSONException {
		String strVal = "";
		if (str != null && !str.equals("") && json.getJSONObject("Request").has(str)){
			strVal = (String) json.getJSONObject("Request").get(str);
			if (strVal == null)
				strVal = "";
		}
		return strVal;
	}

	protected static String[] getResponseStringArray(String value) {
		return new String[] { getResponseValue(value) };
	}

	protected static String getResponseValue(String value) {
		return value == null ? "" : value.trim();
	}

	protected static String setEmptyValue(String value) {
		if(value == null || value.trim().equals("")){
			value = "0";
		}else{
			value = value.trim();
		}
		return value;
	}
}
