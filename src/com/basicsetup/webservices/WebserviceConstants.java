package com.basicsetup.webservices;

public class WebserviceConstants {

	public static final String EXTRA_WEBSERVICE_MODEL = "extra_webservice_model";
	public static final String EXTRA_FLAG = "extra_flag"; 

	public static final int METHODE_TYPE_GET = 100, METHODE_TYPE_POST = 101,
			METHODE_TYPE_PUT = 102;

	public static final int REQUEST_PRIORITY_HIGH = 1,
			REQUEST_PRIORITY_MEDIUM = 2, REQUEST_PRIORITY_LOW = 3;
 

	public static final int RESULT_FLAG_SUCCESS = 0, RESULT_FLAG_RETRY = 1,
			RESULT_FLAG_NO_ITERNET = 2, RESULT_FLAG_JSON_EXCEPTION = 3,
			RESULT_FLAG_UNKNOWN_ERROR = 4, RESULT_FLAG_DUPLICATE_RECORD = 1015;

	// amart url
	public static final String PROTOCOL_HLS = "&protocol=hls";
	public static final String PROTOCOL_RTSP = "&protocol=rtsp";
	public static final String SERVICE_ID = "?service_id=6";
	public static final String SMART_OPTION = "&play_url=yes&us=";
	public static final String PUBLIC_KEY = "ywVXaTzycwZ8agEs3ujx";

	/******************** Game server **********************/
	// base url
	private static final String SERVER = "";

	// specific urls
	 

	// params
	 

	// value
	public static final String GUEST = "guest";
	/******************** Game server **********************/

	/******************** Special symbols ***********************************/
	public static final String QUE = "?";
	public static final String EQU = "=";
	public static final String BOOL_TRUE = "true";
	public static final String BOOL_FALSE = "false";
	/******************** Special symbols ***********************************/

	/*********************** Facebook webservices *****************************/
	// base facebook api
	private static final String GRAPH_API = "https://graph.facebook.com/";

	// url params
	public static final String ACCESS_TOKEN = "access_token";

	// specific facebook api
	public static final String ME = GRAPH_API + "me";
	public static final String URL_FRIENDS = ME + "/friends";
	/*********************** Facebook webservices *****************************/

}
