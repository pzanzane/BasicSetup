package com.basicsetup.webservices.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.basicsetup.webservices.WebserviceConstants;


public abstract class  Parser<T> implements IParser<T> {

	protected int code=WebserviceConstants.RESULT_FLAG_JSON_EXCEPTION;
	protected JSONObject jObj;
	@Override
	public T parse(String str) { 
		 
		try {
			jObj = new JSONObject(str);
			//If response does not contain "code" then respose is not from
			//apps server.It cannot have meta information in response.
			//Such a reponse gives exception then code is -1;
			if(jObj.has("meta"))
				code = jObj.getJSONObject("meta").getInt("code"); 
			else
				code = WebserviceConstants.RESULT_FLAG_SUCCESS;
		} catch (JSONException e) { 
			e.printStackTrace();
		}

		return null;
	}
 

}
