package com.weavx.web.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.weavx.web.model.PaymentGwParameters;
import com.weavx.web.model.RegisterNewUser;
import com.weavx.web.model.TxDetail;
import com.weavx.web.model.TxDetailList;
import com.weavx.web.model.TxExternalPayment;
import com.weavx.web.model.TxUserData;

public class JSONRequestFactory {

	private static UtilPropertiesLoader props = UtilPropertiesLoader.getInstance();
	public final static String D_KEY = "aN28Ez5a4WdmjQECqWS2d7GYS16zXEzV7Qmr9PJhOk0Ik2Ils1WRRFmdMtliDBfnfNY1Rx9vz6fZi6AwVn104cYAD4tonu44ZsifBL078EGjjFJa0HTspGbomrZxZR9N";
	public final static String D_SECRET = "A5kGHxZ3Dha1DKb49I1Q0gWOzvHsJVLdxFpvrNbgmKIq1AbURygBxbednMV7QDOvb3Vo1N6epFQtKanz3Gdgyp1pW2iJwqt1sF8Ct7T7PQkzvCuVyVyLoCAoeGmv11l4";
	public final static int BUSINESS_ADM = Integer.parseInt(props.getProp("business"));

	public static HashMap<Integer, HashMap<String, String[]>> eventosGl = new HashMap<Integer, HashMap<String, String[]>>();

	public static HashMap<String, String> getKeyScret(int option){
		HashMap<String, String> result = new HashMap<>();
		HashMap<Integer, HashMap<String, String[]>> eventos;
				
		if(!eventosGl.isEmpty()) {
			eventos = eventosGl;
		}else {
			eventos = getEventos();
		}
		HashMap<String, String[]> evetn = eventos.get(option);
		String[] keys = evetn.get("keys");
		result.put("A_KEY",keys[0]);
		result.put("A_SECRET",keys[1]);
		return result;	
	}
	
	public static HashMap<String, String> getKeyScret(int option,String accessToken){
		HashMap<String, String> result = new HashMap<>();
		HashMap<Integer, HashMap<String, String[]>> eventos = getEventos(accessToken);
		HashMap<String, String[]> evetn = eventos.get(option);
		String[] keys = evetn.get("keys");
		result.put("A_KEY",keys[0]);
		result.put("A_SECRET",keys[1]);
		return result;	
	}

	public static JSONObject getJSONStandard(String methodName, String accessToken){
		JSONObject json = new JSONObject();

		json.put("methodName", methodName);
		json.put("accessToken", accessToken);

		return json;
	}
	
	public static JSONObject getJSONDashboard(int customerId, int applicationId, long from, long to){
		JSONObject json = new JSONObject();
		
		json.put("customerId", customerId);
		json.put("applicationId", applicationId);
		json.put("from", from);
		json.put("to", to);
		
		return json;
	}

	public static JSONObject getJSONAuthenticate(String methodName, String accessToken, String user, String passwd,String ipUser, String userAgent){
		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONObject userCredential = new JSONObject();

		userCredential.put("email", user);
		userCredential.put("password", passwd);
		parm.put("ipAddress", ipUser);
		parm.put("userAgent", userAgent);

		parm.put("user_credentials", userCredential);


		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;
	}

	public static JSONObject getJSONVerifyUser(String methodName, String accessToken, String email) {

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();

		parm.put("email", email);
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;
	}

	public static JSONObject getJSONRegisterNewUser(String methodName, String accessToken, RegisterNewUser newUser){

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONObject user = new JSONObject();

		user.put("email", newUser.getEmail());
		user.put("firstName", newUser.getFirstName());
		user.put("lastName", newUser.getLastName());
		user.put("password", newUser.getPassword());
		user.put("genderId", newUser.getGenderId());
		user.put("birthDate", newUser.getBirthDate());
		user.put("profileImage", newUser.getProfileimage());
		user.put("countryId", newUser.getCountryId());
		user.put("stateText", newUser.getStateText());
		user.put("cityText", newUser.getCitytext());

		parm.put("user", user);
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;
	}


	public static JSONObject getJSONAuthenticateExternal(String methodName, String accessToken, int identityProviderId, String accessTokenExternal){

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONObject externalUser = new JSONObject();

		externalUser.put("identityProviderId", identityProviderId);
		externalUser.put("accessToken", accessTokenExternal);

		parm.put("external_user_profile", externalUser);


		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;
	}

	public static JSONObject getJSONcreditCardPurchase(String methodName, String accessToken, TxDetailList txDetailList, TxUserData txUserData, PaymentGwParameters paymentGwParameters) {

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONArray txDetails = new JSONArray();
		JSONObject paymentGwInfo = new JSONObject();
		JSONObject jsonpaymentGwParameters = new JSONObject();
		JSONObject tx_user_data = new JSONObject();
		int idGateway = 1;// PENDIENTE
		for (TxDetail txDetail : txDetailList.getTxDetails()) {
			JSONObject detail = new JSONObject();
			detail.put("fundId", txDetail.getFundId());
			detail.put("amount", txDetail.getAmount());
			txDetails.put(detail);
		}

		tx_user_data.put("name", txUserData.getName());
		tx_user_data.put("lastname", txUserData.getLastname());
		tx_user_data.put("country", txUserData.getCountry());
		tx_user_data.put("stateText", txUserData.getStateText());
		tx_user_data.put("cityText", txUserData.getCityText());
		tx_user_data.put("address", txUserData.getAddress());
		tx_user_data.put("postcode", txUserData.getPostcode());
		tx_user_data.put("email", txUserData.getEmail());
		tx_user_data.put("phoneNumber", txUserData.getEmail());

		if (paymentGwParameters.getToken() == null) {
			jsonpaymentGwParameters.put("creditCardNumber", paymentGwParameters.getCreditCardNumber());
			jsonpaymentGwParameters.put("creditCardExpiration",  paymentGwParameters.getCreditCardExpiration());
			jsonpaymentGwParameters.put("creditCardCode",  paymentGwParameters.getCreditCardCode());
			jsonpaymentGwParameters.put("orderDescription",  paymentGwParameters.getOrderDescription());
		} else {
			jsonpaymentGwParameters.put("token", paymentGwParameters.getToken());
			jsonpaymentGwParameters.put("orderDescription",  paymentGwParameters.getOrderDescription());
			idGateway = 2; // PENDIENTE
		}

		paymentGwInfo.put("paymentGatewayId", idGateway);
		paymentGwInfo.put("paymentGwParameters", jsonpaymentGwParameters);

		parm.put("tx_details", txDetails);
		parm.put("payment_gw_info", paymentGwInfo);
		parm.put("tx_user_data",tx_user_data);
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;	

	}

	public static JSONObject getJSONexternalPurchase(String methodName, String accessToken, TxDetail txDetail, TxUserData txUserData, String medium, String source, int lang, TxExternalPayment txPaymentData, String coupon, int operatorId, String comments) {

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONArray txDetails = new JSONArray();
		JSONObject tx_user_data = new JSONObject();
		JSONObject tx_external_payment = new JSONObject();
		

		JSONObject detail = new JSONObject();
		detail.put("fundId", txDetail.getFundId());
		detail.put("amount", txDetail.getAmount());
		txDetails.put(detail);
		
		tx_user_data.put("name", txUserData.getName());
		tx_user_data.put("lastname", txUserData.getLastname());
		tx_user_data.put("email", txUserData.getEmail());
		tx_user_data.put("phoneNumber", txUserData.getPhone());
		
		tx_external_payment.put("externalPaymentDataId", txPaymentData.getExternalPaymentDataId());
		tx_external_payment.put("externalPaymentDataTxt", txPaymentData.getExternalPaymentDataTxt());

		parm.put("tx_details", txDetails);
		parm.put("tx_user_data",tx_user_data);
		parm.put("transaction_medium_key", medium);
		parm.put("transaction_source_key", source);
		parm.put("lang_id", lang);
		parm.put("external_payment_data", tx_external_payment);
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		
		if(!coupon.equals("")) {
			parm.put("coupon", coupon);
		}
		if(!comments.equals("")) {
			parm.put("comments", comments);
		}
		
		parm.put("operator_id", operatorId);
		json.put("parameters", parm);

		return json;	

	}
	
	
	
	public static JSONObject getJSONassistedCCPurchase(String methodName, String accessToken, TxDetail txDetail, TxUserData txUserData, 
			PaymentGwParameters paymentGwParameters, int paymentGatewayId, String medium, String source, int lang, String coupon, 
			int operatorId, String comments, Long startDate, boolean checkStartDate) {

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONArray txDetails = new JSONArray();
		JSONObject paymentGwInfo = new JSONObject();
		JSONObject jsonpaymentGwParameters = new JSONObject();
		JSONObject tx_user_data = new JSONObject();
		int idGateway = paymentGatewayId;// PENDIENTE
		/*for (TxDetail txDetail : txDetailList.getTxDetails()) {*/
			JSONObject detail = new JSONObject();
			detail.put("fundId", txDetail.getFundId());
			detail.put("amount", txDetail.getAmount());
			detail.put("splitPayment", txDetail.isSplitPayment() ? true:false);
			txDetails.put(detail);
		//}

		tx_user_data.put("name", txUserData.getName());
		tx_user_data.put("lastname", txUserData.getLastname());		
		tx_user_data.put("email", txUserData.getEmail());
		tx_user_data.put("phoneNumber", txUserData.getPhone());
		tx_user_data.put("countryId", txUserData.getCountry());
		tx_user_data.put("source_code", txUserData.getSourceCode());
		

		jsonpaymentGwParameters.put("orderDescription",  paymentGwParameters.getOrderDescription());
		jsonpaymentGwParameters.put("savePaymentData", paymentGwParameters.isSavePaymentData());
		if (paymentGwParameters.getToken() == null) {
			jsonpaymentGwParameters.put("cardId", paymentGwParameters.getCardId());
		} else {
			jsonpaymentGwParameters.put("token", paymentGwParameters.getToken());			
		}

		paymentGwInfo.put("paymentGatewayId", idGateway);
		paymentGwInfo.put("paymentGwParameters", jsonpaymentGwParameters);

		parm.put("tx_details", txDetails);
		parm.put("payment_gw_info", paymentGwInfo);
		parm.put("tx_user_data",tx_user_data);
		
		
		parm.put("transaction_medium_key", medium);
		parm.put("transaction_source_key", source);
		parm.put("lang_id", lang);
		parm.put("isModifyStartDate", checkStartDate);
		parm.put("startDate", startDate);
		
		if(!coupon.equals("")) {
			parm.put("coupon", coupon);
		}
		
		if(!comments.equals("")) {
			parm.put("comments", comments);
		}
		
		//parm.put("operator_id", operatorId);
		
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);
		
		
	
			
		return json;	

	}
	
	
	
	public static JSONObject getJSONReport(String methodName, String accessToken, HashMap<String, Object> filter){

		JSONObject json = new JSONObject();
		JSONObject parm = new JSONObject();
		JSONArray filters = new JSONArray();

		if (filter != null){
			for(Map.Entry<String, Object> entry : filter.entrySet()) {
				JSONObject filterJSON = new JSONObject();
				filterJSON.put("fieldName", entry.getKey());
				filterJSON.put("fieldValue", entry.getValue());
				filters.put(filterJSON);	
			}
		}

		parm.put("filters", filters);
		json.put("methodName", methodName);
		json.put("accessToken", accessToken);
		json.put("parameters", parm);

		return json;
	}
	
	private static HashMap<Integer, HashMap<String, String[]>> getEventos() {
		ArrayList<HashMap<String, String[]>> keysEvent = new ArrayList<>();
		HashMap<Integer, HashMap<String, String[]>> eventos = new HashMap<>();

		String[][] keyss = new String[][] {			
			{//Suscripcion clases en vivo
				"AIJ3rFmMhWOUjowDVyr3WSDUeYIc76274I4XyzkTbMds0cWKNj2qsYmSB27xW37amtmDe0b0HpMat4t9W0WsvxzxyFBlhJQY7I7JkUUmFGMTfsleNKFOizqkAqCcl7At",
				"7jnHUmNMlWOlRvIfl3kRWBmNavLxgarp8XiEE5lEBnKkzOZhvsPmGlhfhB5HmP4XUQFapkshnBxmCr7pry3qchtW3UI3BLPk60opEWMjvDf50djufKnBkoifUnxcAVAk"
			},	
		};

		for (int i = 0; i < keyss.length; i++) {
			HashMap<String, String[]> keysEventMap = new HashMap<String, String[]>();
			keysEventMap.put("keys", new String[] {keyss[i][0].toString(),keyss[i][1].toString()});
			keysEvent.add(keysEventMap);
		}

		int i = 1;
		for (HashMap<String, String[]> keys : keysEvent) {
			eventos.put(i, keys);
			i++;
		}
		return eventos;
	}
	
	private static HashMap<Integer, HashMap<String, String[]>> getEventos(String accessToken) {
		ArrayList<HashMap<String, String[]>> keysEvent = new ArrayList<>();
		HashMap<Integer, HashMap<String, String[]>> eventos = new HashMap<>();

		ArrayList<ArrayList<String>> keyss = new ArrayList<ArrayList<String>>();
		
		RequestWS rws = new RequestWS();
		List<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
		
		String ACCESS_TOKEN = accessToken;
		try {
			try {
				response = rws.getAllEvents(BUSINESS_ADM,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				eventosGl = new HashMap<Integer, HashMap<String, String[]>>();
				ACCESS_TOKEN =  rws.getAccessToken(1);
				response = rws.getAllEvents(BUSINESS_ADM,ACCESS_TOKEN);
			}
		} catch (Exception ex) {

		}
		
		for(int index=0; index<response.size();index++) {
			HashMap<String, Object> instance = response.get(index);
			ArrayList<String> arrayString = new ArrayList<String>();
			arrayString.add((String)instance.get("key"));
			arrayString.add((String)instance.get("key_private"));
			keyss.add(index, arrayString);
		}
		
		for (int i = 0; i < keyss.size(); i++) {
			HashMap<String, String[]> keysEventMap = new HashMap<String, String[]>();
			keysEventMap.put("keys", new String[] {(String)keyss.get(i).get(0),(String)keyss.get(i).get(1)});
			keysEvent.add(keysEventMap);
		}

		int i = 1;
		for (HashMap<String, String[]> keys : keysEvent) {
			eventos.put(i, keys);
			i++;
		}
		eventosGl = eventos;
		return eventos;
	}

}
