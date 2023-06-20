package com.weavx.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weavx.web.dto.FormMultiCustomerLoadDTO;
import com.weavx.web.model.AdminRole;
import com.weavx.web.model.AffiliateReport;
import com.weavx.web.model.AjaxResponseBody;
import com.weavx.web.model.AjaxResponseBodyHash2;
import com.weavx.web.model.AjaxResponseReport;
import com.weavx.web.model.Amount;
import com.weavx.web.model.Audit;
import com.weavx.web.model.AuthenticateExternalUser;
import com.weavx.web.model.AuthorizationInfo;
import com.weavx.web.model.Country;
import com.weavx.web.model.CouponParameters;
import com.weavx.web.model.CsvFileAddUsersModel;
import com.weavx.web.model.CsvFileModel;
import com.weavx.web.model.CsvModelRestrict;
import com.weavx.web.model.CustomerApplications;
import com.weavx.web.model.CustomerNewUser;
import com.weavx.web.model.CustomerPaymentsSupported;
import com.weavx.web.model.CustomerTransaction;
import com.weavx.web.model.CustomerUser;
import com.weavx.web.model.Data;
import com.weavx.web.model.DataItem;
import com.weavx.web.model.DefaultPaymentGateways;
import com.weavx.web.model.EventFundSettings;
import com.weavx.web.model.ExternalPaymentType;
import com.weavx.web.model.Fund;
import com.weavx.web.model.FundDash;
import com.weavx.web.model.FundImage;
import com.weavx.web.model.Item;
import com.weavx.web.model.Language;
import com.weavx.web.model.ListFund;
import com.weavx.web.model.ListProp;
import com.weavx.web.model.PaymentGateways;
import com.weavx.web.model.PaymentGatewaysDD;
import com.weavx.web.model.PaymentGwParameters;
import com.weavx.web.model.PaymentType;
import com.weavx.web.model.PaymentTypeDescription;
import com.weavx.web.model.Property;
import com.weavx.web.model.RegisterNewUser;
import com.weavx.web.model.ReportDetail;
import com.weavx.web.model.Request;
import com.weavx.web.model.RequestCouponValidation;
import com.weavx.web.model.Response;
import com.weavx.web.model.RestrictedUser;
import com.weavx.web.model.SalesReport;
import com.weavx.web.model.TransactionStatus;
import com.weavx.web.model.TransactionUpdate;
import com.weavx.web.model.TxCampaing;
import com.weavx.web.model.TxDetail;
import com.weavx.web.model.TxDetailList;
import com.weavx.web.model.TxExternalPayment;
import com.weavx.web.model.TxSources;
import com.weavx.web.model.TxUserData;
import com.weavx.web.model.UserAccess;
import com.weavx.web.model.UserInfo;

public class RequestWS {
	Logger logger = LoggerFactory.getLogger(RequestWS.class);

	private UtilPropertiesLoader props = UtilPropertiesLoader.getInstance();
	private String URL_REQUEST = props.getProp("url.request");
	private String URL_SPRING_REQUEST = props.getProp("url.spring.request");
	private final String SOURCE = "CALLCENTER";
	//private final String URL_SSE = "https://ssedev.harvestful.org/SSE/";
	private final String URL_SSE = props.getProp("url.sse");
	//private final String URL_SSE = "https://sse.harvestful.org/SSE/";
	//private final String URL_SSE = "http://localhost:9494/SSE/";


	public String getAccessToken(int option) throws Exception {

		HashMap<String, String> result = JSONRequestFactory.getKeyScret(option);

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getAccessToken");
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> devKeyInfo = new HashMap<>();
		devKeyInfo.put("key", JSONRequestFactory.D_KEY);
		devKeyInfo.put("secret", JSONRequestFactory.D_SECRET);
		HashMap<String, Object> accessKeyInfo = new HashMap<>();
		accessKeyInfo.put("key", result.get("A_KEY"));
		accessKeyInfo.put("secret", result.get("A_SECRET"));		
		parameters.put("accessKey",accessKeyInfo);
		parameters.put("developerKey", devKeyInfo);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {
			return (String) resp.getResult().get("accessToken");
		} else {
			throw new Exception(resp.getReturnMessage());
		}		
	}
	

	public String getAccessToken() throws Exception {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getAccessToken");
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> devKeyInfo = new HashMap<>();
		devKeyInfo.put("key", JSONRequestFactory.D_KEY);
		devKeyInfo.put("secret", JSONRequestFactory.D_SECRET);
		HashMap<String, Object> accessKeyInfo = new HashMap<>();
		accessKeyInfo.put("key", "KML2rkqTP063mN0hHWls3EsZ8eePYt7YmWkarMDeKIZCYHoJ01vJOuKtQ0aMftX80972bOafCD37uDwxPJutQxhuRCCyQwA3T2RwuL4eil05tMwCMA9lpHsd011uGJBA");
		accessKeyInfo.put("secret", "onF9fNt3wLEWf3hnhrigacXYw2g6jvOcDrEgcmVOH7ny3XdtQcvwdeuBE9kCwxaRVDz9dlBUVtdIJ6GCRFnoSIoJUardpg22HiCyLHKabWWbsaNCsac8ofmY5eCdllzz");		
		parameters.put("accessKey",accessKeyInfo);
		parameters.put("developerKey", devKeyInfo);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {
			return (String) resp.getResult().get("accessToken");
		} else {
			throw new Exception(resp.getReturnMessage());
		}		
	}
	
	
	public String getAccessTokenAll(int option,String token) throws Exception {
		HashMap<String, String> result = JSONRequestFactory.getKeyScret(option,token);
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getAccessToken");
		HashMap<String, Object>parameters = new HashMap<>();
		HashMap<String, Object>devKeyInfo = new HashMap<>();
		devKeyInfo.put("key", JSONRequestFactory.D_KEY);
		devKeyInfo.put("secret", JSONRequestFactory.D_SECRET);
		HashMap<String, Object> accessKeyInfo = new HashMap<>();
		accessKeyInfo.put("key", result.get("A_KEY"));
		accessKeyInfo.put("secret", result.get("A_SECRET"));		
		parameters.put("accessKey",accessKeyInfo);
		parameters.put("developerKey", devKeyInfo);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {
			return  (String) resp.getResult().get("accessToken");
		}else {
			throw new Exception(resp.getReturnMessage());
		}
	}

	public String getAccessToken(String publicKey, String privateKey) throws Exception {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getAccessToken");
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> devKeyInfo = new HashMap<>();
		devKeyInfo.put("key", JSONRequestFactory.D_KEY);
		devKeyInfo.put("secret", JSONRequestFactory.D_SECRET);
		HashMap<String, Object> accessKeyInfo = new HashMap<>();
		accessKeyInfo.put("key", publicKey);
		accessKeyInfo.put("secret", privateKey);		
		parameters.put("accessKey",accessKeyInfo);
		parameters.put("developerKey", devKeyInfo);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {
			return (String) resp.getResult().get("accessToken");
		} else {
			throw new Exception(resp.getReturnMessage());
		}		
	}

	public ArrayList<Language> getLanguages(String accessToken) throws IOException, ExpiredAccessTokenException {

		String getJSON = JSONRequestFactory.getJSONStandard("listAllLanguages", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		JSONObject result = totalResult.getJSONObject("result");

		JSONArray arr = result.getJSONArray("languages");

		ArrayList<Language> arrayListLang = new ArrayList<>();

		for (int i = 0; i < arr.length(); i++) {
			boolean isDefault = false;
			if (arr.getJSONObject(i).getString("isDefault").equals("1"))
				isDefault = true;
			Language lan = new Language(arr.getJSONObject(i).getInt("id"), isDefault,
					arr.getJSONObject(i).getString("locale"), arr.getJSONObject(i).getString("description"));
			arrayListLang.add(lan);

		}
		httpClient.close();
		return arrayListLang;

	}

	public ArrayList<ListProp> getListProperties(String accessToken) throws IOException, ExpiredAccessTokenException {

		String getJSON = JSONRequestFactory.getJSONStandard("listAllProperties", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONArray arr = result.getJSONArray("properties");

		ArrayList<ListProp> listProperties = new ArrayList<ListProp>();

		for (int i = 0; i < arr.length(); i++) {
			ListProp lp = new ListProp(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("name"));
			listProperties.add(lp);
		}
		httpClient.close();
		return listProperties;
	}

	public ArrayList<Property> getCustomerProperties(String accessToken)
			throws IOException, ExpiredAccessTokenException {

		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerProperties", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		JSONObject result = totalResult.getJSONObject("result");

		JSONArray arr = result.getJSONArray("customerProperties");

		ArrayList<Property> arrayListProp = new ArrayList<>();

		for (int i = 0; i < arr.length(); i++) {
			Property prop = new Property(arr.getJSONObject(i).getInt("propertyId"),
					arr.getJSONObject(i).getInt("langId"), arr.getJSONObject(i).getInt("customerId"),
					arr.getJSONObject(i).getString("propertyValue"));
			arrayListProp.add(prop);
		}
		httpClient.close();
		return arrayListProp;

	}

	public ArrayList<ListFund> getListFund(String accessToken) throws IOException, ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getCustomerFunds");
		request.setAccessToken(accessToken);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		if (resp.getReturnCode() == 0) {
			ArrayList<ListFund> listFund = new ArrayList<ListFund>();
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> fundTmp = (List<HashMap<String, Object>>) resp.getResult().get("funds");
			long dateNow = System.currentTimeMillis();
			for (HashMap<String, Object> funds : fundTmp) {
				ListFund fund = new ListFund((int) funds.get("id"), (String) funds.get("businessCode"),
						(Boolean) funds.get("default"), (Long) funds.get("validFrom"), (Long) funds.get("validTo"),
						(String) funds.get("name"));
				if (dateNow >= fund.getValidFrom() && dateNow <= fund.getValidTo()) {
					listFund.add(fund);
				}
			}
			return listFund;
		} else if (resp.getReturnCode() == -2) {
			throw new ExpiredAccessTokenException();
		} else {
			return null;
		}
	}

	public ArrayList<ExternalPaymentType> getExternalPaymentList(String accessToken)
			throws IOException, ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("listAllExternalPaymentTypes");
		request.setAccessToken(accessToken);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		RestTemplate templateSupported = new RestTemplate();
		Request requestSupportedPayments = new Request();
		requestSupportedPayments.setMethodName("listCustomerExternalPaymentTypes");
		requestSupportedPayments.setAccessToken(accessToken);
		Response respSupportedP = templateSupported.postForObject(URL_REQUEST, requestSupportedPayments, Response.class,
				new HashMap<>());

		if (resp.getReturnCode() == 0 && respSupportedP.getReturnCode() == 0) {
			ArrayList<ExternalPaymentType> paymentList = new ArrayList<ExternalPaymentType>();
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> paymentTmp = (List<HashMap<String, Object>>) resp.getResult()
					.get("externalPaymentTypes");
			for (HashMap<String, Object> payment : paymentTmp) {
				ExternalPaymentType payments = new ExternalPaymentType((int) payment.get("id"),
						(String) payment.get("name"), (String) payment.get("iconUrl"));
				paymentList.add(payments);
			}

			ArrayList<CustomerPaymentsSupported> supportedPayments = new ArrayList<CustomerPaymentsSupported>();

			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> supportedTmp = (List<HashMap<String, Object>>) respSupportedP.getResult()
					.get("externalPaymentTypes");
			for (HashMap<String, Object> supported : supportedTmp) {
				CustomerPaymentsSupported supportedP = new CustomerPaymentsSupported((int) supported.get("customerId"),
						(int) supported.get("externalPTId"), (boolean) supported.get("enabled"));
				supportedPayments.add(supportedP);
			}

			ArrayList<ExternalPaymentType> filteredPaymentTypes = new ArrayList<ExternalPaymentType>();

			for (ExternalPaymentType element : paymentList) {

				for (CustomerPaymentsSupported elm : supportedPayments) {

					if (elm.getExternalPaymentId() == element.getId() && elm.getEnabled()) {

						filteredPaymentTypes.add(element);
					}
				}
			}
			/*
			 * ArrayList<ExternalPaymentType> filteredPaymentTypes =
			 * paymentList.stream().filter( element -> {
			 * 
			 * boolean existSupport = false; for ( CustomerPaymentsSupported elm :
			 * supportedPayments ) {
			 * 
			 * if( elm.getId() == element.getId() && elm.getEnabled() ) {
			 * 
			 * existSupport = true; } }
			 * 
			 * return existSupport;
			 * 
			 * } ).collect(Collectors.toCollection(ArrayList<ExternalPaymentType>::new));
			 */

			return filteredPaymentTypes;

		} else if (resp.getReturnCode() == -2) {
			throw new ExpiredAccessTokenException();
		} else {
			return null;
		}
	}

	public ArrayList<Fund> getFund(String accessToken) throws IOException, ExpiredAccessTokenException {
		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerFundsDescription", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONArray arr = result.getJSONArray("fundDescriptions");

		ArrayList<Fund> aListFund = new ArrayList<Fund>();

		for (int i = 0; i < arr.length(); i++) {
			Fund fund = new Fund(arr.getJSONObject(i).getInt("fundId"), arr.getJSONObject(i).getInt("langId"),
					arr.getJSONObject(i).getString("fundLabel"), arr.getJSONObject(i).getString("fundDescription"));
			aListFund.add(fund);
		}
		httpClient.close();
		return aListFund;
	}

	public ArrayList<Amount> getAmount(String accessToken) throws IOException, ExpiredAccessTokenException {
		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerDonationAmounts", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONArray arr = result.getJSONArray("donationAmounts");

		ArrayList<Amount> aListAmount = new ArrayList<Amount>();

		for (int i = 0; i < arr.length(); i++) {
			Amount amount = new Amount(arr.getJSONObject(i).getInt("amount"), arr.getJSONObject(i).getInt("id"));
			aListAmount.add(amount);
		}
		httpClient.close();
		return aListAmount;
	}
	
	public ArrayList<Language> getLanguagesByCustomer(String accessToken) throws IOException, ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getCustomerLang");
		request.setAccessToken(accessToken);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		
		switch (resp.getReturnCode()) {
		case 0: 
			HashMap<String, Object> result =  resp.getResult();
			ArrayList<Language> languajes = (ArrayList<Language>) result.get("languages");
			return languajes;					
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();
		default:
			return null;
		}
	}

	public ArrayList<FundImage> getFundImage(String accessToken) throws IOException, ExpiredAccessTokenException {
		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerFundsImages", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONArray arr = result.getJSONArray("fundsImages");

		ArrayList<FundImage> aListFundImage = new ArrayList<FundImage>();

		for (int i = 0; i < arr.length(); i++) {
			FundImage fundImage = new FundImage(arr.getJSONObject(i).getInt("fundId"),
					arr.getJSONObject(i).getInt("imageId"), arr.getJSONObject(i).getString("imageUrl"));
			aListFundImage.add(fundImage);
		}
		httpClient.close();
		return aListFundImage;
	}

	public ArrayList<Country> getListAllContries(String accessToken) throws IOException, ExpiredAccessTokenException {
		String getJSON = JSONRequestFactory.getJSONStandard("listAllCountries", accessToken).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONArray arr = result.getJSONArray("countries");

		ArrayList<Country> aListCountries = new ArrayList<Country>();

		for (int i = 0; i < arr.length(); i++) {
			Country country = new Country(arr.getJSONObject(i).getString("shortName"),
					arr.getJSONObject(i).getString("name"), arr.getJSONObject(i).getInt("id"));
			aListCountries.add(country);
		}
		httpClient.close();
		return aListCountries;
	}

	public CustomerUser authenticateUser(String user, String passwd, HttpServletRequest hsr, String au,
			String accessToken) throws IOException, ExpiredAccessTokenException, WrongAuthenticationException, ExceedUsersSessionException {

		String getJSON = JSONRequestFactory
				.getJSONAuthenticate("authenticateUser", accessToken, user, passwd, getClientIp(hsr), au).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		addAuditRecord(accessToken, user, "AUTHENTICATE USER", hsr.getHeader("user-agent"), getClientIp(hsr), 0,
				"success");

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		
		JSONObject totalResult = new JSONObject(builder.toString());
		

		
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		
				
		if (totalResult.getInt("returnCode") == -14 || totalResult.getInt("returnCode") == -32) {
			try {
				JSONObject result = totalResult.getJSONObject("result");
				JSONObject userSessionToken = result.getJSONObject("user_access_token");
				String userAccessToken = userSessionToken.getString("token");
				int codeResult = invalidateAllUserAccessToken(accessToken, userAccessToken);
				throw new ExceedUsersSessionException("exceeded-users");
			} catch(Exception e) {
				String messageResult = totalResult.getString("returnMessage");
				throw new WrongAuthenticationException(messageResult);
			}
			
			
		}
		
		if(totalResult.getInt("returnCode") != 0) {
			String messageResult = totalResult.getString("returnMessage");
			throw new WrongAuthenticationException(messageResult);
		}
		
		JSONObject result = totalResult.getJSONObject("result");
		JSONObject customer = result.getJSONObject("customer_user");
		JSONObject customerResult = customer.getJSONObject("custUser");

		JSONObject userSessionToken = result.getJSONObject("user_access_token");
		String userAccessToken = userSessionToken.getString("token");
		

		try {
			CustomerUser customerUser = new CustomerUser(customerResult.getInt("userId"),
					customerResult.getString("firstName"), customerResult.getString("lastName"),
					customerResult.getInt("genderId"),
					(customerResult.isNull("birthDate")) ? "" : customerResult.getString("birthDate"),
					(customerResult.isNull("profileImage")) ? "" : customerResult.getString("profileImage"),
					customerResult.getInt("statedId"), customerResult.getInt("cityId"),
					customerResult.getInt("customerId"),
					(customerResult.isNull("stateText")) ? "" : customerResult.getString("stateText"),
					(customerResult.isNull("cityText")) ? "" : customerResult.getString("cityText"),
					(customerResult.isNull("password")) ? "" : customerResult.getString("password"),
					customerResult.getInt("countryId"), customerResult.getInt("id"),
					(customerResult.isNull("password")) ? "" : customerResult.getString("password"));

			customerUser.setUserAccessToken(userAccessToken);
			customerUser.setEmail(customer.getString("email"));

			ArrayList<AdminRole> functionsAdmin = getUserAdminRoleFunctions(accessToken, userAccessToken);
			customerUser.setRoleFunctions(functionsAdmin);

			httpClient.close();

			return customerUser;
		} catch (Exception e) {
			httpClient.close();
			logger.error("ERROR", e);
			return null;
		}
	

	}

	public ArrayList<AdminRole> getUserAdminRoleFunctions(String accessToken, String userAccessToken) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(accessToken);
		request.setMethodName("findCustomerUserAdminProfile");

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("userAccessToken", userAccessToken);
		request.setParameters(parameters);
		ArrayList<AdminRole> adminRoles = new ArrayList<>();

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {

			HashMap<String, Object> dataResult = (HashMap<String, Object>) resp.getResult();
			HashMap<String, Object> customerUserAdminProfile = (HashMap<String, Object>) dataResult
					.get("customerUserAdminProfile");
			List<HashMap<String, Object>> adminRoleFunctions = (List<HashMap<String, Object>>) customerUserAdminProfile
					.get("adminRoleFunctions");

			for (HashMap<String, Object> adminRole : adminRoleFunctions) {

				List<HashMap<String, Object>> adminFunctions = (List<HashMap<String, Object>>) adminRole
						.get("adminFunctions");

				for (HashMap<String, Object> adminFunction : adminFunctions) {
					AdminRole role = new AdminRole((int) adminFunction.get("id"), (String) adminFunction.get("name"),
							(String) adminFunction.get("description"));
					adminRoles.add(role);
				}
			}

		}

		return adminRoles;
	}

	public Boolean verifyUser(String email, String accessToken) throws IOException, ExpiredAccessTokenException {

		String getJSON = JSONRequestFactory.getJSONVerifyUser("existsUser", accessToken, email).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		JSONObject result = totalResult.getJSONObject("result");
		try {
			httpClient.close();
			return result.getBoolean("existsUser");
		} catch (Exception e) {
			logger.error("ERROR", e);
			httpClient.close();
			return null;
		}

	}
	
	public HashMap<String, Object> verifyUserForRegister(String email, String accessToken) throws ExpiredAccessTokenException {		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("existsUser");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("email", email);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> verifyUser = new HashMap<>();
			verifyUser.put("existsUser", (Boolean) resp.getResult().get("existsUser"));
			if ((Boolean) resp.getResult().get("existsUser")){
				verifyUser.put("customerUserId", ((Integer) resp.getResult().get("customerUserId")) != null ? (int) resp.getResult().get("customerUserId") : null );
				verifyUser.put("password", (Boolean) resp.getResult().get("password"));
				verifyUser.put("user", resp.getResult().get("user"));
			}
			return verifyUser;
		case 45:
			HashMap<String, Object> blockUser = new HashMap<>();
			blockUser.put("message", (String) resp.getReturnMessage());			
			return blockUser;
		case -46:
			HashMap<String, Object> result = new HashMap<>();
			result.put("invalid", (String) resp.getReturnMessage());			
			return result;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
	
	public HashMap<String, Object> verifyUserForRegister(String email, long transactionId, String accessToken) throws ExpiredAccessTokenException {		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("existsUser");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("email", email);
		parm.put("transactionId", transactionId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> verifyUser = new HashMap<>();
			verifyUser.put("existsUser", (Boolean) resp.getResult().get("existsUser"));
			if ((Boolean) resp.getResult().get("existsUser")){
				verifyUser.put("customerUserId", ((Integer) resp.getResult().get("customerUserId")) != null ? (int) resp.getResult().get("customerUserId") : null );
				verifyUser.put("password", (Boolean) resp.getResult().get("password"));
				verifyUser.put("user", resp.getResult().get("user"));
			}
			return verifyUser;
		case 45:
			HashMap<String, Object> blockUser = new HashMap<>();
			blockUser.put("message", (String) resp.getReturnMessage());			
			return blockUser;
		case -46:
			HashMap<String, Object> result = new HashMap<>();
			result.put("invalid", (String) resp.getReturnMessage());			
			return result;
		case -60:
			HashMap<String, Object> result1 = new HashMap<>();
			result1.put("returnCode", (int) resp.getReturnCode());
			result1.put("returnMessage", (String) resp.getReturnMessage());
			return result1;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}

	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String accessToken)
			throws IOException, ExpiredAccessTokenException, AlreadyExistsException {

		String getJSON = JSONRequestFactory.getJSONRegisterNewUser("registerNewUser", accessToken, newUser).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		if (totalResult.getInt("returnCode") == -9) {
			throw new AlreadyExistsException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONObject customerResult = result.getJSONObject("customer_user");

		try {

			CustomerNewUser customerUser = new CustomerNewUser(customerResult.getInt("userId"),
					customerResult.getString("firstName"), customerResult.getString("lastName"),
					customerResult.getInt("genderId"),
					(customerResult.isNull("birthDate")) ? "" : customerResult.getString("birthDate"),
					customerResult.getString("profileImage"), customerResult.getInt("statedId"),
					customerResult.getInt("cityId"), customerResult.getInt("customerId"),
					customerResult.getString("stateText"), customerResult.getString("cityText"), newUser.getPassword(),
					customerResult.getInt("countryId"), customerResult.getInt("id"), newUser.getEmail());
			httpClient.close();
			return customerUser;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			httpClient.close();
			return null;
		}

	}

	public CustomerUser authenticateExternalUser(String accessToken, AuthenticateExternalUser authExternal)
			throws IOException, ExpiredAccessTokenException, WrongAuthenticationException {

		String getJSON = JSONRequestFactory.getJSONAuthenticateExternal("authenticateExternalUser", accessToken,
				authExternal.getIdentityProviderId(), authExternal.getAccessTokenExternal()).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		if (totalResult.getInt("returnCode") == -14) {
			throw new WrongAuthenticationException();
		}

		JSONObject result = totalResult.getJSONObject("result");
		JSONObject customerResult = result.getJSONObject("customer_user");
		try {
			CustomerUser customerUser = new CustomerUser(customerResult.getInt("userId"),
					customerResult.getString("firstName"), customerResult.getString("lastName"),
					customerResult.getInt("genderId"),
					(customerResult.isNull("birthDate")) ? "" : customerResult.getString("birthDate"),
					customerResult.getString("profileImage"), customerResult.getInt("statedId"),
					customerResult.getInt("cityId"), customerResult.getInt("customerId"),
					(customerResult.isNull("stateText")) ? "" : customerResult.getString("stateText"),
					(customerResult.isNull("cityText")) ? "" : customerResult.getString("cityText"),
					customerResult.getString("password"), customerResult.getInt("countryId"),
					customerResult.getInt("id"), result.getString("user_access_token"));
			httpClient.close();
			return customerUser;
		} catch (Exception e) {
			httpClient.close();
			logger.error("ERROR", e);
			return null;
		}

	}

	public AuthorizationInfo creditCardPurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData,
			PaymentGwParameters paymentGwParameters, HttpServletRequest hsr, CustomerUser currU)
			throws IOException, ExpiredAccessTokenException {

		String getJSON = JSONRequestFactory.getJSONcreditCardPurchase("creditCardPurchase", aCCESS_TOKEN, txDetailList,
				txUserData, paymentGwParameters).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");

			JSONObject authorization = paymentResult.getJSONObject("authorizationInfo");
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setAccountId(authorization.getString("accountId"));
			authorizationInfo.setAuthCode(authorization.getString("authCode"));
			authorizationInfo.setTransId(authorization.getString("transId"));
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "CREDIT CARD PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "SUCCESS");

			httpClient.close();
			return authorizationInfo;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "CREDIT CARD PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, totalResult.getString("returnMessage"));
			httpClient.close();
			return authorizationInfo;
		}

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> freePurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData,
			String source, String paramUA, int langId, String coupon, HttpServletRequest hsr, CustomerUser currU, String comments, Long startDate, boolean checkStartDate)
			throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("freePurchase");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		ArrayList<HashMap<String, Object>> txDetails = new ArrayList<>();
		HashMap<String, Object> tx_user_data = new HashMap<>();
		/*
		 * HashMap<String, Object> jsonpaymentGwParameters = new HashMap<>();
		 * HashMap<String, Object> paymentGwInfo = new HashMap<>();
		 * 
		 * int idGateway = 1;// PENDIENTE
		 */ for (TxDetail txDetail : txDetailList.getTxDetails()) {
			HashMap<String, Object> detail = new HashMap<>();
			detail.put("fundId", txDetail.getFundId());
			detail.put("amount", txDetail.getAmount());
			txDetails.add(detail);
		}
		tx_user_data.put("name", txUserData.getName());
		tx_user_data.put("lastname", txUserData.getLastname());
		tx_user_data.put("country", txUserData.getCountry());
		tx_user_data.put("stateText", txUserData.getStateText());
		tx_user_data.put("cityText", txUserData.getCityText());
		tx_user_data.put("address", txUserData.getAddress());
		tx_user_data.put("postcode", txUserData.getPostcode());
		tx_user_data.put("email", txUserData.getEmail());
		tx_user_data.put("source_code", txUserData.getSourceCode());
		tx_user_data.put("countryId", txUserData.getCountry());
		
		
		
		if (paramUA.length() > 0)
			tx_user_data.put("userAccessToken", paramUA);
		/*
		 * if (paymentGwParameters.getToken() == null) {
		 * jsonpaymentGwParameters.put("creditCardNumber",
		 * paymentGwParameters.getCreditCardNumber());
		 * jsonpaymentGwParameters.put("creditCardExpiration",
		 * paymentGwParameters.getCreditCardExpiration());
		 * jsonpaymentGwParameters.put("creditCardCode",
		 * paymentGwParameters.getCreditCardCode());
		 * jsonpaymentGwParameters.put("orderDescription",
		 * paymentGwParameters.getOrderDescription()); } else {
		 * jsonpaymentGwParameters.put("token", paymentGwParameters.getToken());
		 * jsonpaymentGwParameters.put("orderDescription",
		 * paymentGwParameters.getOrderDescription()); idGateway = 2; // PENDIENTE }
		 * paymentGwInfo.put("paymentGatewayId", idGateway);
		 * paymentGwInfo.put("paymentGwParameters", jsonpaymentGwParameters);
		 * parm.put("payment_gw_info", paymentGwInfo);
		 */
		parm.put("tx_details", txDetails);
		parm.put("comments",comments);
		parm.put("tx_user_data", tx_user_data);
		parm.put("isModifyStartDate", checkStartDate);
		parm.put("startDate", startDate);
		if (source != null)
			parm.put("transaction_source_key", source);
		parm.put("lang_id", langId);
		if (coupon.trim().length() > 0)
			parm.put("coupon", coupon);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		switch (resp.getReturnCode()) {
			case 0:
				HashMap<String, Object> paymentResult = (HashMap<String, Object>) resp.getResult().get("paymentResult");
				addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "FREE PURCHASE", hsr.getHeader("user-agent"),
						getClientIp(hsr), 0, "SUCCESS");
				return paymentResult;
			case -2:
				throw new ExpiredAccessTokenException();
			default:
				HashMap<String, Object> authorizationInfo2 = new HashMap<String, Object>();
				authorizationInfo2.put("returnCode", String.valueOf(resp.getReturnCode()));
				authorizationInfo2.put("returnMessage", resp.getReturnMessage());
				addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "FREE PURCHASE", hsr.getHeader("user-agent"),
						getClientIp(hsr), 2, resp.getReturnMessage());
				return authorizationInfo2;
		}
	}

	@SuppressWarnings("restriction")
	public AjaxResponseBody externalPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData,
			String medium, String source, int lang, TxExternalPayment txPaymentData, String coupon, int operatorId,
			String comments, HttpServletRequest hsr, CustomerUser currU, Long startDate,boolean checkStartDate)
			throws IOException, ExpiredAccessTokenException, ExceedUsersSessionException {
		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> txDetails = new HashMap<>();
		HashMap<String, Object> tx_user_data = new HashMap<>();
		HashMap<String, Object> tx_external_payment = new HashMap<>();
		ArrayList<HashMap<String, Object>> txDetailsList = new ArrayList<>();
		txDetails.put("fundId", txDetail.getFundId());
		txDetails.put("amount", txDetail.getAmount());
		txDetailsList.add(txDetails);
		tx_user_data.put("name", txUserData.getName());
		tx_user_data.put("lastname", txUserData.getLastname());
		tx_user_data.put("email", txUserData.getEmail());
		tx_user_data.put("phoneNumber", txUserData.getPhone());
		tx_user_data.put("countryId", txUserData.getCountry());
		tx_user_data.put("source_code", txUserData.getSourceCode());
		
		tx_external_payment.put("externalPaymentDataId", txPaymentData.getExternalPaymentDataId());
		tx_external_payment.put("externalPaymentDataTxt", txPaymentData.getExternalPaymentDataTxt());
		parameters.put("external_payment_data", tx_external_payment);
		parameters.put("tx_details", txDetailsList);
		parameters.put("tx_user_data", tx_user_data);
		request.setMethodName("externalPurchase");
		request.setAccessToken(aCCESS_TOKEN);

		if(!coupon.equals("")) {
			parameters.put("coupon", coupon);
		}
		if(!comments.equals("")) {
			parameters.put("comments", comments);
		}
		parameters.put("operator_id", operatorId);
		parameters.put("isModifyStartDate", checkStartDate);
		parameters.put("startDate", startDate);
		parameters.put("lang_id", lang);
		request.setParameters(parameters);
		
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		
		if (resp.getReturnCode() == -2) {
			throw new ExpiredAccessTokenException();
		}
		
		if (resp.getReturnCode() == -34) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-34");
			authorizationInfo.setMsg(resp.getReturnMessage());
			return authorizationInfo;
		}
		
		if (resp.getReturnCode() == -4) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-4");
			authorizationInfo.setMsg(resp.getReturnMessage());	
			return authorizationInfo;
		}
		
		if (resp.getReturnCode() == -46) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-46");
			authorizationInfo.setMsg(resp.getReturnMessage());	
			return authorizationInfo;
		}
		
		if (resp.getReturnCode() == -45) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-45");
			//authorizationInfo.setMsg(resp.getReturnMessage());
			authorizationInfo.setMsg("El correo indicado se encuentra en listas negras, por favor contacte a un supervisor para que realice su desbloqueo y luego vuelva a intentar la venta");
			return authorizationInfo;
		}

		try {
			HashMap<String, Object> result = resp.getResult();
			HashMap<String, Object> paymentResult =  (HashMap<String, Object>) result.get("paymentResult");
			HashMap<String, Object> authorizationInfomap = (HashMap<String, Object>) paymentResult.get("authorizationInfo");
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode(String.valueOf(resp.getReturnCode()));
			authorizationInfo.setTxId((String)authorizationInfomap.get("tx_id"));
			authorizationInfo.setMsg((String)paymentResult.get("resultMessage"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "EXTERNAL PURCHASE", hsr.getHeader("user-agent"), getClientIp(hsr), 0, "SUCCESS");
			return authorizationInfo;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("1255");
			authorizationInfo.setMsg(e.getMessage());
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "EXTERNAL PURCHASE", hsr.getHeader("user-agent"), getClientIp(hsr), 2, e.getMessage());
			return authorizationInfo;
		}

	}

	public AuthorizationInfo assistedCCPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData,
			PaymentGwParameters paymentGwParameters, int paymentGatewayId, String medium, String source, int lang,
			String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate)
			throws IOException, ExpiredAccessTokenException, ExceedUsersSessionException {

		String getJSON = JSONRequestFactory
				.getJSONassistedCCPurchase("assistedCCPurchase", aCCESS_TOKEN, txDetail, txUserData,
						paymentGwParameters, paymentGatewayId, medium, source, lang, coupon, operatorId, comments,startDate,checkStartDate)
				.toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON, "utf-8");
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		
		if (totalResult.getInt("returnCode") == -32) {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray userSession  = result.getJSONArray("userSessions");
			JSONObject userIndex = userSession.getJSONObject(0);
			int currentCustomerId = userIndex.getInt("customerUserId");
			String token = userIndex.getString("token");
			int codeResult = invalidateAllUserAccessToken(aCCESS_TOKEN, token);
			int currentCustomerUserId = currU.getCustomerId();
			closeSSE(currentCustomerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		}
		
		if (totalResult.getInt("returnCode") == -45) {
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult("El correo indicado se encuentra en listas negras, por favor contacte a un supervisor para que realice su desbloqueo y luego vuelva a intentar la venta");
			return authorizationInfo;
		}	

		if (totalResult.getInt("returnCode") == -34) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			authorizationInfo.setCode("-34");
			authorizationInfo.setMsg(paymentResult.getString("resultMessage"));
			
		}
		
		if (totalResult.getInt("returnCode") == -53) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			authorizationInfo.setCode("-53");
			authorizationInfo.setMsg(paymentResult.getString("resultMessage"));
			
		}
		
		if (totalResult.getInt("returnCode") == -4) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-4");
			authorizationInfo.setMsg(totalResult.getString("returnMessage"));			
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			JSONObject authorization = paymentResult.getJSONObject("authorizationInfo");
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			authorizationInfo.setTxId(authorization.getString("tx_id"));
			addAuditRecord(aCCESS_TOKEN, "barberam@gmail.com", "ASSISTED CC PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
			httpClient.close();
			return authorizationInfo;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "ASSISTED CC PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return authorizationInfo;
		}

	}

	public ArrayList<PaymentType> listAllPaymentTypes(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<PaymentType> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listAllPaymentTypes", aCCESS_TOKEN).toString();

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("paymentTypes");
			ArrayList<PaymentType> arrayListPaymentType = new ArrayList<>();

			for (int i = 0; i < arr.length(); i++) {
				PaymentType paymentType = new PaymentType();
				paymentType.setId(arr.getJSONObject(i).getInt("id"));
				paymentType.setName(arr.getJSONObject(i).getString("name"));
				paymentType.setUrlLogo(arr.getJSONObject(i).getString("urlLogo"));
				paymentType.setEnable(arr.getJSONObject(i).getBoolean("enabled"));
				if (paymentType.isEnable()) {
					arrayListPaymentType.add(paymentType);
				}
			}
			for (PaymentType paymentTmp : arrayListPaymentType) {
				if (paymentTmp.isEnable()) {
					finalResult.add(paymentTmp);
				}
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			PaymentType paymentTmp = new PaymentType();
			paymentTmp.setId(-1);
			paymentTmp.setName(e.getMessage());
			finalResult.add(paymentTmp);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<DefaultPaymentGateways> getCustomerDefaultPaymentGateways(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<DefaultPaymentGateways> finalResult = new ArrayList<>();

		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerDefaultPaymentGateways", aCCESS_TOKEN)
				.toString();

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {

			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("customerDefaultPaymentGateways");

			for (int i = 0; i < arr.length(); i++) {
				DefaultPaymentGateways defaultPaymentGateways = new DefaultPaymentGateways();
				defaultPaymentGateways.setPaymentTypeId(arr.getJSONObject(i).getInt("paymentTypeId"));
				defaultPaymentGateways.setPaymentGatewayId(arr.getJSONObject(i).getInt("paymentGatewayId"));
				finalResult.add(defaultPaymentGateways);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			httpClient.close();
			DefaultPaymentGateways defaultPaymentGateways = new DefaultPaymentGateways();
			defaultPaymentGateways.setPaymentTypeId(-1);
			finalResult.add(defaultPaymentGateways);
			return finalResult;
		}
	}

	public PaymentGateways allPaymentMethodTDC(String aCCESS_TOKEN) throws IOException, ExpiredAccessTokenException {
		PaymentGateways finalResult = new PaymentGateways();

		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerPaymentGatewayInfo", aCCESS_TOKEN).toString();

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON);

		input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			ArrayList<PaymentType> arrayListPaymentType = listAllPaymentTypes(aCCESS_TOKEN);// ENABLED
			ArrayList<DefaultPaymentGateways> arrayListDefaultPaymentGateways = getCustomerDefaultPaymentGateways(
					aCCESS_TOKEN);
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("customerPaymentGateways");
			ArrayList<PaymentGateways> arrayListPaymentGateways = new ArrayList<>();
			for (int i = 0; i < arr.length(); i++) {
				PaymentGateways paymentGateways = new PaymentGateways();
				paymentGateways.setPaymentGatewayId(arr.getJSONObject(i).getInt("paymentGatewayId"));
				paymentGateways.setAuthKey1(arr.getJSONObject(i).getString("authKey1"));
				paymentGateways.setAuthKey2(arr.getJSONObject(i).getString("authKey2"));
				paymentGateways.setAuthKey3(arr.getJSONObject(i).getString("authKey3"));
				paymentGateways.setAuthKey4(arr.getJSONObject(i).getString("authKey4"));
				paymentGateways.setPublicAuthKey1(arr.getJSONObject(i).getBoolean("publicAuthKey1"));
				paymentGateways.setPublicAuthKey2(arr.getJSONObject(i).getBoolean("publicAuthKey2"));
				paymentGateways.setPublicAuthKey3(arr.getJSONObject(i).getBoolean("publicAuthKey3"));
				paymentGateways.setPublicAuthKey4(arr.getJSONObject(i).getBoolean("publicAuthKey4"));
				paymentGateways.setPaymentModeId(arr.getJSONObject(i).getInt("paymentModeId"));
				arrayListPaymentGateways.add(paymentGateways);
			}
			int paymentTypesId = 0;
			int paymentGatewayId = 0;
			for (PaymentType paymentTmp : arrayListPaymentType) {
				if (paymentTmp.getName().equals("CREDIT_CARD") && paymentTmp.isEnable()) {
					paymentTypesId = paymentTmp.getId();
				}
			}
			for (DefaultPaymentGateways defaultPaymentTmp : arrayListDefaultPaymentGateways) {
				if (defaultPaymentTmp.getPaymentTypeId() == paymentTypesId) {
					paymentGatewayId = defaultPaymentTmp.getPaymentGatewayId();
				}
			}
			for (PaymentGateways paymentGatewaysTmp : arrayListPaymentGateways) {
				if (paymentGatewaysTmp.getPaymentGatewayId() == paymentGatewayId) {
					finalResult = paymentGatewaysTmp;
				}
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			finalResult.setPaymentGatewayId(-1);
			finalResult.setAuthKey1(e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<PaymentTypeDescription> listAllPaymentTypeDescriptions(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<PaymentTypeDescription> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listAllPaymentTypeDescriptions", aCCESS_TOKEN).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("paymentTypeDescriptions");
			ArrayList<PaymentType> arrayListPaymentType = listAllPaymentTypes(aCCESS_TOKEN);// ENABLED

			for (int i = 0; i < arr.length(); i++) {
				PaymentTypeDescription paymentTypeDescription = new PaymentTypeDescription();
				paymentTypeDescription.setPaymentTypeId(arr.getJSONObject(i).getInt("paymentTypeId"));
				paymentTypeDescription.setLangId(arr.getJSONObject(i).getInt("langId"));
				paymentTypeDescription.setLabel(arr.getJSONObject(i).getString("label"));
				for (PaymentType paymentTmp : arrayListPaymentType) {
					if ((paymentTypeDescription.getPaymentTypeId() == paymentTmp.getId()) && (paymentTmp.isEnable())) {
						finalResult.add(paymentTypeDescription);
					}
				}
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			PaymentTypeDescription paymentTypeDescription = new PaymentTypeDescription();
			paymentTypeDescription.setPaymentTypeId(-1);
			paymentTypeDescription.setLabel(e.getMessage());
			finalResult.add(paymentTypeDescription);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<ReportDetail> listReport(String aCCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU) throws IOException, ExpiredAccessTokenException {

		ArrayList<ReportDetail> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONReport("customerTransactionsReport", aCCESS_TOKEN, filter)
				.toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT", hsr.getHeader("user-agent"),
				getClientIp(hsr), 0, "SUCCESS");
		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				ReportDetail reportDetail = new ReportDetail();
				reportDetail.setPerson_firstname(RequestWS.convertStringUtf8ToLatin1(arr.getJSONObject(i).getString("person_firstname")));
				reportDetail.setPerson_lastname(RequestWS.convertStringUtf8ToLatin1(arr.getJSONObject(i).getString("person_lastname")));
				reportDetail.setPerson_addressline1(arr.getJSONObject(i).isNull("person_addressline1") ? "": arr.getJSONObject(i).getString("person_addressline1"));
				reportDetail.setPerson_addresscity(arr.getJSONObject(i).isNull("person_addresscity") ? "" : arr.getJSONObject(i).getString("person_addresscity"));
				reportDetail.setPerson_addressstate(arr.getJSONObject(i).isNull("person_addressstate") ? "" : arr.getJSONObject(i).getString("person_addressstate"));
				reportDetail.setPerson_addresszip(arr.getJSONObject(i).isNull("person_addresszip") ? "" : arr.getJSONObject(i).getString("person_addresszip"));
				reportDetail.setPerson_country(arr.getJSONObject(i).isNull("person_country") ? "" : arr.getJSONObject(i).getString("person_country"));
				reportDetail.setPerson_phone(arr.getJSONObject(i).isNull("person_phone") ? "" : arr.getJSONObject(i).getString("person_phone"));
				reportDetail.setContribution_date(arr.getJSONObject(i).getLong("contribution_date"));
				reportDetail.setSourceCode(arr.getJSONObject(i).isNull("source_code") ? "" : arr.getJSONObject(i).getString("source_code"));
				reportDetail.setAffiliateEmail(arr.getJSONObject(i).isNull("affiliateemail") ? "" : arr.getJSONObject(i).getString("affiliateemail"));
				reportDetail.setAffiliateId(arr.getJSONObject(i).isNull("affiliateid") ? "" : arr.getJSONObject(i).getString("affiliateid"));
				try {
					long date = (long) reportDetail.getContribution_date();
					Date dateObj = new Date(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat df2 = new SimpleDateFormat("HH:mm");
					reportDetail.setDateFormat(df.format(dateObj));
					reportDetail.setTimeFormat(df2.format(dateObj));

				} catch (Exception e) {
					logger.error("ERROR", e);
					reportDetail.setDateFormat(e.getMessage());
				}
				reportDetail.setContribution_amount(arr.getJSONObject(i).getInt("contribution_amount"));
				if (arr.getJSONObject(i).isNull("contribution_discount")) {
					reportDetail.setContribution_discount(0);
				} else {
					reportDetail.setContribution_discount(arr.getJSONObject(i).getDouble("contribution_discount"));
				}
				if (arr.getJSONObject(i).isNull("comments")) {
					reportDetail.setComments("");
				} else {
					reportDetail.setComments(arr.getJSONObject(i).getString("comments"));
				}

				if (arr.getJSONObject(i).isNull("contribution_onl_transactionid")) {
					reportDetail.setContribution_onl_transactionid("");
				} else {
					reportDetail.setContribution_onl_transactionid(
							arr.getJSONObject(i).getString("contribution_onl_transactionid"));
				}
				reportDetail.setContribution_paymentinfo(arr.getJSONObject(i).isNull("contribution_paymentinfo") ? "N/A"
						: arr.getJSONObject(i).getString("contribution_paymentinfo"));
				reportDetail.setContribution_fund(arr.getJSONObject(i).getString("contribution_fund"));
				reportDetail.setContribution_containerid(arr.getJSONObject(i).getString("contribution_containerid"));
				reportDetail.setPerson_email(arr.getJSONObject(i).getString("person_email"));
				reportDetail.setPerson_authorizenetprofileid(
						arr.getJSONObject(i).getString("person_authorizenetprofileid"));
				if (arr.getJSONObject(i).isNull("contribution_onl_approvalnumber")) {
					reportDetail.setContribution_onl_approvalnumber("");
				} else {
					reportDetail.setContribution_onl_approvalnumber(
							arr.getJSONObject(i).getString("contribution_onl_approvalnumber"));
				}
				reportDetail.setContribution_form(arr.getJSONObject(i).getString("contribution_form"));
				reportDetail.setTransactioninternalid(arr.getJSONObject(i).getString("transactioninternalid"));
				reportDetail.setApplication_name(arr.getJSONObject(i).getString("application_name"));
				reportDetail.setTransaction_status(arr.getJSONObject(i).getString("transaction_status"));
				reportDetail.setPayment_type(arr.getJSONObject(i).getString("payment_type"));
				reportDetail.setPayment_gw(arr.getJSONObject(i).isNull("payment_gw") ? "N/A"
						: arr.getJSONObject(i).getString("payment_gw"));
				reportDetail.setDonation_source(arr.getJSONObject(i).getString("donation_source"));
				reportDetail.setDonation_campaing(arr.getJSONObject(i).getString("donation_campaing"));
				reportDetail.setTransaction_medium(arr.getJSONObject(i).getString("transaction_medium"));
				reportDetail.setTransaction_content(arr.getJSONObject(i).getString("transaction_content"));
				reportDetail.setTransaction_term(arr.getJSONObject(i).getString("transaction_term"));
				reportDetail.setIs_settled(arr.getJSONObject(i).getBoolean("is_settled"));
				if (arr.getJSONObject(i).isNull("operator_email")) {
					reportDetail.setOperatorEmail("N/A");
				} else {
					reportDetail.setOperatorEmail(arr.getJSONObject(i).getString("operator_email"));
				}
				reportDetail.setIp_address(arr.getJSONObject(i).isNull("ip_address") ? "N/A"
						: arr.getJSONObject(i).getString("ip_address"));
				reportDetail.setUser_agent(arr.getJSONObject(i).isNull("user_agent") ? "N/A"
						: arr.getJSONObject(i).getString("user_agent"));
				finalResult.add(reportDetail);
			}

			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			ReportDetail reportDetail = new ReportDetail();
			reportDetail.setContribution_amount(-1);
			reportDetail.setPerson_firstname(e.getMessage());
			finalResult.add(reportDetail);
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<ReportDetail> listGlobalReport(String aCCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU) throws IOException, ExpiredAccessTokenException {

		ArrayList<ReportDetail> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONReport("globalTransactionReport", aCCESS_TOKEN, filter)
				.toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST GLOBAL TRANSACTION REPORT", hsr.getHeader("user-agent"),
				getClientIp(hsr), 0, "SUCCESS");
		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				ReportDetail reportDetail = new ReportDetail();
				reportDetail.setPerson_firstname(RequestWS.convertStringUtf8ToLatin1(arr.getJSONObject(i).getString("person_firstname")));
				reportDetail.setPerson_lastname(RequestWS.convertStringUtf8ToLatin1(arr.getJSONObject(i).getString("person_lastname")));
				reportDetail.setPerson_addressline1(arr.getJSONObject(i).isNull("person_addressline1") ? "": arr.getJSONObject(i).getString("person_addressline1"));
				reportDetail.setPerson_addresscity(arr.getJSONObject(i).isNull("person_addresscity") ? "": arr.getJSONObject(i).getString("person_addresscity"));
				reportDetail.setPerson_addressstate(arr.getJSONObject(i).isNull("person_addressstate") ? "": arr.getJSONObject(i).getString("person_addressstate"));
				reportDetail.setPerson_addresszip(arr.getJSONObject(i).isNull("person_addresszip") ? "" : arr.getJSONObject(i).getString("person_addresszip"));
				reportDetail.setPerson_country(arr.getJSONObject(i).isNull("person_country") ? "" : arr.getJSONObject(i).getString("person_country"));
				reportDetail.setPerson_phone(arr.getJSONObject(i).isNull("person_phone") ? "" : arr.getJSONObject(i).getString("person_phone"));
				reportDetail.setContribution_date(arr.getJSONObject(i).getLong("contribution_date"));
				reportDetail.setSourceCode(arr.getJSONObject(i).isNull("source_code") ? "" : arr.getJSONObject(i).getString("source_code"));
				reportDetail.setAffiliateEmail(arr.getJSONObject(i).isNull("affiliateemail") ? "" : arr.getJSONObject(i).getString("affiliateemail"));
				reportDetail.setAffiliateId(arr.getJSONObject(i).isNull("affiliateid") ? "" : arr.getJSONObject(i).getString("affiliateid"));

				try {
					long date = (long) reportDetail.getContribution_date();
					Date dateObj = new Date(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat df2 = new SimpleDateFormat("HH:mm");
					reportDetail.setDateFormat(df.format(dateObj));
					reportDetail.setTimeFormat(df2.format(dateObj));

				} catch (Exception e) {
					logger.error("ERROR", e);
					reportDetail.setDateFormat(e.getMessage());
				}
				reportDetail.setContribution_amount(arr.getJSONObject(i).getInt("contribution_amount"));
				if (arr.getJSONObject(i).isNull("contribution_discount")) {
					reportDetail.setContribution_discount(0);
				} else {
					reportDetail.setContribution_discount(arr.getJSONObject(i).getDouble("contribution_discount"));
				}
				if (arr.getJSONObject(i).isNull("comments")) {
					reportDetail.setComments("");
				} else {
					reportDetail.setComments(arr.getJSONObject(i).getString("comments"));
				}

				if (arr.getJSONObject(i).isNull("contribution_onl_transactionid")) {
					reportDetail.setContribution_onl_transactionid("");
				} else {
					reportDetail.setContribution_onl_transactionid(
							arr.getJSONObject(i).getString("contribution_onl_transactionid"));
				}
				reportDetail.setContribution_paymentinfo(arr.getJSONObject(i).isNull("contribution_paymentinfo") ? "N/A"
						: arr.getJSONObject(i).getString("contribution_paymentinfo"));
				reportDetail.setContribution_fund(arr.getJSONObject(i).getString("contribution_fund"));
				reportDetail.setContribution_containerid(arr.getJSONObject(i).getString("contribution_containerid"));
				reportDetail.setPerson_email(arr.getJSONObject(i).getString("person_email"));
				reportDetail.setPerson_authorizenetprofileid(
						arr.getJSONObject(i).getString("person_authorizenetprofileid"));
				if (arr.getJSONObject(i).isNull("contribution_onl_approvalnumber")) {
					reportDetail.setContribution_onl_approvalnumber("");
				} else {
					reportDetail.setContribution_onl_approvalnumber(
							arr.getJSONObject(i).getString("contribution_onl_approvalnumber"));
				}
				reportDetail.setContribution_form(arr.getJSONObject(i).getString("contribution_form"));
				reportDetail.setTransactioninternalid(arr.getJSONObject(i).getString("transactioninternalid"));
				reportDetail.setApplication_name(arr.getJSONObject(i).getString("application_name"));
				reportDetail.setTransaction_status(arr.getJSONObject(i).getString("transaction_status"));
				reportDetail.setPayment_type(arr.getJSONObject(i).getString("payment_type"));
				reportDetail.setPayment_gw(arr.getJSONObject(i).isNull("payment_gw") ? "N/A"
						: arr.getJSONObject(i).getString("payment_gw"));
				reportDetail.setDonation_source(arr.getJSONObject(i).getString("donation_source"));
				reportDetail.setDonation_campaing(arr.getJSONObject(i).getString("donation_campaing"));
				reportDetail.setTransaction_medium(arr.getJSONObject(i).getString("transaction_medium"));
				reportDetail.setTransaction_content(arr.getJSONObject(i).getString("transaction_content"));
				reportDetail.setTransaction_term(arr.getJSONObject(i).getString("transaction_term"));
				reportDetail.setIs_settled(arr.getJSONObject(i).getBoolean("is_settled"));
				if (arr.getJSONObject(i).isNull("operator_email")) {
					reportDetail.setOperatorEmail("N/A");
				} else {
					reportDetail.setOperatorEmail(arr.getJSONObject(i).getString("operator_email"));
				}
				reportDetail.setIp_address(arr.getJSONObject(i).isNull("ip_address") ? "N/A"
						: arr.getJSONObject(i).getString("ip_address"));
				reportDetail.setUser_agent(arr.getJSONObject(i).isNull("user_agent") ? "N/A"
						: arr.getJSONObject(i).getString("user_agent"));
				finalResult.add(reportDetail);
			}

			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			ReportDetail reportDetail = new ReportDetail();
			reportDetail.setContribution_amount(-1);
			reportDetail.setPerson_firstname(e.getMessage());
			finalResult.add(reportDetail);
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST GLOBAL TRANSACTION REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}
	
	public ArrayList<ReportDetail> listReportFinances(String aCCESS_TOKEN, HashMap<String, Object> filter)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<ReportDetail> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONReport("customerFinancesReport", aCCESS_TOKEN, filter).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				ReportDetail reportDetail = new ReportDetail();
				reportDetail.setPerson_firstname(arr.getJSONObject(i).getString("person_firstname"));
				reportDetail.setPerson_lastname(arr.getJSONObject(i).getString("person_lastname"));
				reportDetail.setPerson_addressline1(arr.getJSONObject(i).isNull("person_addressline1") ? ""
						: arr.getJSONObject(i).getString("person_addressline1"));
				reportDetail.setPerson_addresscity(arr.getJSONObject(i).isNull("person_addresscity") ? ""
						: arr.getJSONObject(i).getString("person_addresscity"));
				reportDetail.setPerson_addressstate(arr.getJSONObject(i).isNull("person_addressstate") ? ""
						: arr.getJSONObject(i).getString("person_addressstate"));
				reportDetail.setPerson_addresszip(arr.getJSONObject(i).isNull("person_addresszip") ? ""
						: arr.getJSONObject(i).getString("person_addresszip"));
				reportDetail.setPerson_country(arr.getJSONObject(i).isNull("person_country") ? ""
						: arr.getJSONObject(i).getString("person_country"));
				reportDetail.setContribution_date(arr.getJSONObject(i).getLong("contribution_date"));
				try {
					long date = (long) reportDetail.getContribution_date();
					Date dateObj = new Date(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat df2 = new SimpleDateFormat("HH:mm");
					reportDetail.setDateFormat(df.format(dateObj));
					reportDetail.setTimeFormat(df2.format(dateObj));

				} catch (Exception e) {
					logger.error("ERROR", e);
					reportDetail.setDateFormat(e.getMessage());
				}
				reportDetail.setContribution_amount(arr.getJSONObject(i).getInt("contribution_amount"));
				if (arr.getJSONObject(i).isNull("contribution_onl_transactionid")) {
					reportDetail.setContribution_onl_transactionid("");
				} else {
					reportDetail.setContribution_onl_transactionid(
							arr.getJSONObject(i).getString("contribution_onl_transactionid"));
				}
				reportDetail.setContribution_paymentinfo(arr.getJSONObject(i).getString("contribution_paymentinfo"));
				reportDetail.setContribution_fund(arr.getJSONObject(i).getString("contribution_fund"));
				reportDetail.setContribution_containerid(arr.getJSONObject(i).getString("contribution_containerid"));
				reportDetail.setPerson_email(arr.getJSONObject(i).getString("person_email"));
				reportDetail.setPerson_authorizenetprofileid(
						arr.getJSONObject(i).getString("person_authorizenetprofileid"));
				if (arr.getJSONObject(i).isNull("contribution_onl_approvalnumber")) {
					reportDetail.setContribution_onl_approvalnumber("");
				} else {
					reportDetail.setContribution_onl_approvalnumber(
							arr.getJSONObject(i).getString("contribution_onl_approvalnumber"));
				}
				reportDetail.setContribution_form(arr.getJSONObject(i).getString("contribution_form"));
				reportDetail.setTransactioninternalid(arr.getJSONObject(i).getString("transactioninternalid"));
				reportDetail.setApplication_name(arr.getJSONObject(i).getString("application_name"));
				reportDetail.setTransaction_status(arr.getJSONObject(i).getString("transaction_status"));
				reportDetail.setPayment_type(arr.getJSONObject(i).getString("payment_type"));
				reportDetail.setPayment_gw(arr.getJSONObject(i).getString("payment_gw"));
				reportDetail.setDonation_source(arr.getJSONObject(i).getString("donation_source"));
				reportDetail.setDonation_campaing(arr.getJSONObject(i).getString("donation_campaing"));
				reportDetail.setIs_settled(arr.getJSONObject(i).getBoolean("is_settled"));
				reportDetail.setIp_address(arr.getJSONObject(i).isNull("ip_address") ? "N/A"
						: arr.getJSONObject(i).getString("ip_address"));
				reportDetail.setUser_agent(arr.getJSONObject(i).isNull("user_agent") ? "N/A"
						: arr.getJSONObject(i).getString("user_agent"));
				finalResult.add(reportDetail);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			ReportDetail reportDetail = new ReportDetail();
			reportDetail.setContribution_amount(-1);
			reportDetail.setPerson_firstname(e.getMessage());
			finalResult.add(reportDetail);
			httpClient.close();
			return finalResult;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, Object>> customerFinancesReport(String aCCESS_TOKEN,
			HashMap<String, Object> filter) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> parm = new HashMap<>();
		request.setMethodName("customerFinancesReport");
		request.setAccessToken(aCCESS_TOKEN);
		ArrayList<HashMap<String, Object>> filters = new ArrayList<>();
		if (filter != null) {
			for (Map.Entry<String, Object> entry : filter.entrySet()) {
				HashMap<String, Object> filterJSON = new HashMap<>();
				filterJSON.put("fieldName", entry.getKey());
				filterJSON.put("fieldValue", entry.getValue());
				filters.add(filterJSON);
			}
		}
		parm.put("filters", filters);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		switch (resp.getReturnCode()) {
			case 0:
				ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) resp.getResult()
						.get("report");
				return list;
			case -2:
			case -22:
				throw new ExpiredAccessTokenException();
			default:
				return null;
		}
	}

	public ArrayList<PaymentGatewaysDD> listAllPaymentGateways(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<PaymentGatewaysDD> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listAllPaymentGateways", aCCESS_TOKEN).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("paymentGateways");
			for (int i = 0; i < arr.length(); i++) {
				PaymentGatewaysDD paymentGateways = new PaymentGatewaysDD();
				paymentGateways.setId(arr.getJSONObject(i).getInt("id"));
				paymentGateways.setName(arr.getJSONObject(i).getString("name"));
				paymentGateways.setEnabled(arr.getJSONObject(i).getBoolean("enabled"));
				finalResult.add(paymentGateways);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			PaymentGatewaysDD paymentGateways = new PaymentGatewaysDD();
			paymentGateways.setId(-1);
			paymentGateways.setName(e.getMessage());
			finalResult.add(paymentGateways);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<TransactionStatus> listTxStatus(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<TransactionStatus> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listAllTransactionStatus", aCCESS_TOKEN).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("transactionStatus");
			for (int i = 0; i < arr.length(); i++) {
				TransactionStatus txStatus = new TransactionStatus();
				txStatus.setId(arr.getJSONObject(i).getInt("id"));
				txStatus.setName(arr.getJSONObject(i).getString("name"));
				finalResult.add(txStatus);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			TransactionStatus txStatus = new TransactionStatus();
			txStatus.setId(-1);
			txStatus.setName(e.getMessage());
			finalResult.add(txStatus);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<CustomerApplications> listCustomerApplications(String aCCESS_TOKEN)
			throws IOException, ExpiredAccessTokenException {

		ArrayList<CustomerApplications> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("getCustomerApplications", aCCESS_TOKEN).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("customerApplications");
			for (int i = 0; i < arr.length(); i++) {
				CustomerApplications cApp = new CustomerApplications();
				cApp.setId(arr.getJSONObject(i).getInt("id"));
				cApp.setName(arr.getJSONObject(i).getString("name"));
				if (cApp.getId() != 2)// quitar
					finalResult.add(cApp);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			CustomerApplications cApp = new CustomerApplications();
			cApp.setId(-1);
			cApp.setName(e.getMessage());
			finalResult.add(cApp);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<TxSources> listTxSource(String aCCESS_TOKEN) throws IOException, ExpiredAccessTokenException {

		ArrayList<TxSources> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listCustomerTransactionSources", aCCESS_TOKEN).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("transactionSources");
			for (int i = 0; i < arr.length(); i++) {
				TxSources txSources = new TxSources();
				txSources.setId(arr.getJSONObject(i).getInt("id"));
				txSources.setName(arr.getJSONObject(i).getString("key"));
				txSources.setName(arr.getJSONObject(i).getString("name"));
				finalResult.add(txSources);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			TxSources txSources = new TxSources();
			txSources.setId(-1);
			txSources.setName(e.getMessage());
			finalResult.add(txSources);
			httpClient.close();
			return finalResult;
		}
	}

	public ArrayList<TxCampaing> listTxCampaing(String aCCESS_TOKEN) throws IOException, ExpiredAccessTokenException {

		ArrayList<TxCampaing> finalResult = new ArrayList<>();
		String getJSON = JSONRequestFactory.getJSONStandard("listCustomerTransactionCampaings", aCCESS_TOKEN)
				.toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("transactionCampaings");
			for (int i = 0; i < arr.length(); i++) {
				TxCampaing txCampaing = new TxCampaing();
				txCampaing.setId(arr.getJSONObject(i).getInt("id"));
				txCampaing.setKey(arr.getJSONObject(i).getString("key"));
				txCampaing.setName(arr.getJSONObject(i).getString("name"));
				finalResult.add(txCampaing);
			}
			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			TxCampaing txCampaing = new TxCampaing();
			txCampaing.setId(-1);
			txCampaing.setName(e.getMessage());
			finalResult.add(txCampaing);
			httpClient.close();
			return finalResult;
		}
	}

	@SuppressWarnings("serial")
	public JSONObject dashboard(int customerId, int applicationId, long from, long to, String period)
			throws IOException, ExpiredAccessTokenException {
		JSONObject finalResult;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			String getJSON = JSONRequestFactory.getJSONDashboard(customerId, applicationId, from, to).toString();
			HttpPost postRequest = new HttpPost(URL_SPRING_REQUEST);
			StringEntity input = new StringEntity(getJSON);
			input.setContentType("application/json");
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "ISO-8859-1"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}

			byte[] jsonData = builder.toString().getBytes();
			ObjectMapper objectMapper = new ObjectMapper();
			Data obj = objectMapper.readValue(jsonData, Data.class);
			List<Item> list = obj.getData();
			List<Item> data = list.stream().filter(item -> item.getDate() >= from && item.getDate() <= to)
					.collect(Collectors.toList());

			long QUANTITY = data.size();
			double TOTAL = data.stream().mapToDouble(Item::getAmount).sum();
			List<FundDash> funds = data.stream().map(x -> x.getFunds()).flatMap(x -> x.stream())
					.collect(Collectors.toList());
			long FUNDS_QUANTITY = funds.size();
			double FUNDS_TOTAL = funds.stream().mapToDouble(FundDash::getAmount).sum();
			Map<Object, DataItem> fundsGroup = funds.stream()
					.collect(Collectors.groupingBy(FundDash::getFundDescription,
							Collectors.collectingAndThen(Collectors.summarizingDouble(FundDash::getAmount),
									set -> new DataItem(null, set.getCount(), set.getSum(),
											set.getCount() / (double) FUNDS_QUANTITY * 100,
											set.getSum() / (double) FUNDS_TOTAL * 100))));
			fundsGroup.forEach((label, item) -> {
				item.setLabel(label.toString());
			});
			Map<String, Map<Object, DataItem>> summary = new HashMap<String, Map<Object, DataItem>>() {
				{
					put("funds", fundsGroup);
				};
			};
			Map<String, Function<Item, Object>> dateGroups = new HashMap<String, Function<Item, Object>>() {
				{
					put("T", Item::getDateAsHours);
					put("H", Item::getDateAsHours);
					put("D", Item::getDateAsDate);
					put("W", Item::getDateAsDate);
					put("M", Item::getDateAsDate);
					put("3", Item::getDateAsWeek);
				};
			};
			Function<Item, Object> detailFunction = dateGroups.get(period);
			Map<String, Function<Item, Object>> groups = new HashMap<String, Function<Item, Object>>() {
				{
					put("TOTALS", Item::getScheduled);
					put("sources", Item::getSource);
					// put("sources_type", Item::getSource_type);
					put("continents", Item::getContinentName);
					put("countries", Item::getCountryName);
					put("details", detailFunction);
				};
			};
			groups.forEach((group, clasiffier) -> {
				Map<Object, DataItem> result = data.stream()
						.collect(Collectors.groupingBy(clasiffier,
								Collectors.collectingAndThen(Collectors.summarizingDouble(Item::getAmount),
										set -> new DataItem(null, set.getCount(), set.getSum(),
												set.getCount() / (double) QUANTITY * 100,
												set.getSum() / (double) TOTAL * 100))));

				result.forEach((label, item) -> {
					item.setLabel(label.toString());
				});
				summary.put(group, sortByComparator(result));
			});
			JSONObject dataDashboard = new JSONObject(summary);
			finalResult = dataDashboard;
		} catch (Exception e) {
			logger.error("ERROR", e);
			finalResult = null;
		}
		httpClient.close();
		return finalResult;
	}

	private static Map<Object, DataItem> sortByComparator(Map<Object, DataItem> unsortMap) {

		class MyComparator implements Comparator<Map.Entry<Object, DataItem>> {
			public int compare(Map.Entry<Object, DataItem> o1, Map.Entry<Object, DataItem> o2) {
				if (((DataItem) o1.getValue()).getQuantity() >= ((DataItem) o2.getValue()).getQuantity()) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		List<Map.Entry<Object, DataItem>> list = new LinkedList<Map.Entry<Object, DataItem>>(unsortMap.entrySet());

		Collections.sort(list, new MyComparator());

		Map<Object, DataItem> sortedMap = new LinkedHashMap<Object, DataItem>();
		for (Map.Entry<Object, DataItem> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getApplicationParameters(String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> finalResult = new HashMap<String, Object>();
		Request request = new Request();
		request.setMethodName("getApplicationParameters");
		request.setAccessToken(aCCESS_TOKEN);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		switch (resp.getReturnCode()) {
			case 0:
				HashMap<String, Object> app = (HashMap<String, Object>) resp.getResult().get("applicationParameters");
				Iterator<String> keys = app.keySet().iterator();
				while (keys.hasNext()) {
					String keyM = keys.next();
					Object valueM = app.get(keyM);
					if (valueM != null && valueM.toString().length() > 0) {
						finalResult.put(keyM, valueM);
					}
				}
				return finalResult;
			case -2:
				throw new ExpiredAccessTokenException();
			default:
				finalResult.put("error", resp.getReturnMessage());
				return finalResult;
		}
	}

	public Response validateCoupon(String aCCESS_TOKEN, String code, int purchaseAmount, String applier,
			boolean activateCoupon, String userAgent, String ipAddress, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> finalResult = new HashMap<String, Object>();

		CouponParameters parameters = new CouponParameters(code, purchaseAmount, applier, activateCoupon, userAgent,
				getClientIp(hsr));

		RequestCouponValidation request = new RequestCouponValidation();
		request.setMethodName("validateCoupon");
		request.setAccessToken(aCCESS_TOKEN);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "VALIDATE COUPON", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "VALIDATE COUPON", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		/*
		 * switch (resp.getReturnCode()) { case 0: Integer app = (Integer)
		 * resp.getResult().get("cuponDiscount"); while(keys.hasNext()){ String keyM =
		 * keys.next(); Object valueM = app.get(keyM); if (valueM!=null &&
		 * valueM.toString().length()>0){ finalResult.put(keyM, valueM); } } return
		 * finalResult; case -2: throw new ExpiredAccessTokenException(); default:
		 * finalResult.put("error", resp.getReturnMessage()); return finalResult; }
		 */

		return resp;
	}
	
	public Response registerCustomersList(FormMultiCustomerLoadDTO form, ArrayList<CsvFileModel> csvData,
			String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> parameters = new HashMap<>();

		ArrayList<HashMap<String, Object>> txDetails = new ArrayList<>();

		HashMap<String, Object> detail = new HashMap<>();

		detail.put("fundId", form.getFunds());
		detail.put("amount", form.getAmount());
		txDetails.add(detail);

		//Response ajax = new Response();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("addAttendees");
		if (form.getSource().length() > 0)
			parameters.put("transaction_source_key", form.getSource());
		if (form.getMedium().length() > 0)
			parameters.put("transaction_medium_key", form.getMedium());
		parameters.put("tx_details", txDetails);
		parameters.put("attendees", csvData);
		parameters.put("lang_id", 1);
		parameters.put("emailCustomerUser", currU.getEmail());
		request.setParameters(parameters);
	
		new Thread(() -> {
			try {
				template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();	
		
		
		Response resp = new Response();
		resp.setReturnCode(0);
		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD ATTENDEEES FOR AN EVENT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD ATTENDEEES FOR AN EVENT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		switch (resp.getReturnCode()) {
		case 0:
			return resp;
		case -2:
			throw new ExpiredAccessTokenException();
		default:
			return resp;
		}
	}

	public Response addUsersByCsvFile(FormMultiCustomerLoadDTO form, ArrayList<CsvFileAddUsersModel> csvData,
			String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> parameters = new HashMap<>();

		ArrayList<HashMap<String, Object>> txDetails = new ArrayList<>();

		HashMap<String, Object> detail = new HashMap<>();

		detail.put("fundId", form.getFunds());
		detail.put("amount", form.getAmount());
		txDetails.add(detail);

		//Response ajax = new Response();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("addUsersByCsvFileMethodImpl");
		if (form.getSource().length() > 0)
			parameters.put("transaction_source_key", form.getSource());
		if (form.getMedium().length() > 0)
			parameters.put("transaction_medium_key", form.getMedium());
		parameters.put("tx_details", txDetails);
		parameters.put("attendees", csvData);
		parameters.put("lang_id", 1);
		parameters.put("emailCustomerUser", currU.getEmail());
		request.setParameters(parameters);
	
		new Thread(() -> {
			try {
				template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();	
		
		
		Response resp = new Response();
		resp.setReturnCode(0);
		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD USERS BY CSV FILE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD USERS BY CSV FILE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		switch (resp.getReturnCode()) {
		case 0:
			return resp;
		case -2:
			throw new ExpiredAccessTokenException();
		default:
			return resp;
		}
	}
	
	public AjaxResponseBody registerRestrictEvent(ArrayList<CsvModelRestrict> csvData, String ACCESS_TOKEN,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> parameters = new HashMap<>();

		AjaxResponseBody ajax = new AjaxResponseBody();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("addRestrictedAttendees");
		parameters.put("restrictedAttendees", csvData);
		parameters.put("lang_id", 1);
		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD RESTRICTED ATTENDEES", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD RESTRICTED ATTENDEES", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}
		switch (resp.getReturnCode()) {
			case 0:
				if (resp.getResult() != null) {
					ajax.setMsg(resp.getResult().toString());
				}
				ajax.setCode(String.valueOf(resp.getReturnCode()));
				return ajax;
			case -2:
				throw new ExpiredAccessTokenException();
			default:
				if (resp.getResult() != null) {
					ajax.setMsg(resp.getResult().toString());
				}
				ajax.setCode(String.valueOf(resp.getReturnCode()));
				return ajax;
		}
	}

	public ArrayList<RestrictedUser> getRestrictedUsers(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		AjaxResponseBody ajax = new AjaxResponseBody();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("listRestrictedEventAttendees");

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "LIST RESTRICTED EVENT ATTENDEES",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "LIST RESTRICTED EVENT ATTENDEES",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, resp.getReturnMessage());
		}

		switch (resp.getReturnCode()) {
			case 0:

				ArrayList<RestrictedUser> restrictedList = new ArrayList<>();
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> usersTmp = (List<HashMap<String, Object>>) resp.getResult()
						.get("restrictedEventAttendees");
				for (HashMap<String, Object> userRestricted : usersTmp) {
					RestrictedUser user = new RestrictedUser((int) userRestricted.get("id"),
							(int) userRestricted.get("customerId"), (int) userRestricted.get("applicationId"),
							(String) userRestricted.get("name"), (String) userRestricted.get("lastName"),
							(String) userRestricted.get("email"), (boolean) userRestricted.get("active"));
					restrictedList.add(user);
				}

				return restrictedList;
			case -2:
				throw new ExpiredAccessTokenException();
			default:

				return new ArrayList<>();
		}
	}

	public Response editRestrictedUser(String ACCESS_TOKEN, RestrictedUser user, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("updateRestrictedAttendee");

		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> attendee = new HashMap<>();

		attendee.put("name", user.getName());
		attendee.put("lastName", user.getLastName());
		attendee.put("email", user.getEmail());
		attendee.put("id", new Integer(user.getId()));
		attendee.put("active", user.isActive());
		
		parameters.put("attendee", attendee);

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE RESTRICTED ATTENDEE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE RESTRICTED ATTENDEE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		switch (resp.getReturnCode()) {
			case 0:

				/*
				 * ArrayList<RestrictedUser> restrictedList = new ArrayList<>();
				 * 
				 * @SuppressWarnings("unchecked") List<HashMap<String, Object>> usersTmp =
				 * (List<HashMap<String, Object>>)
				 * resp.getResult().get("restrictedEventAttendees"); for (HashMap<String,
				 * Object> userRestricted : usersTmp) { RestrictedUser user = new
				 * RestrictedUser((int)userRestricted.get("id"),(int)userRestricted.get(
				 * "customerId"),(int)userRestricted.get("applicationId"),
				 * (String)userRestricted.get("name"), (String)userRestricted.get("lastName"),
				 * (String)userRestricted.get("email"), (boolean)userRestricted.get("active"));
				 * restrictedList.add(user); }
				 */

				return resp;
			case -2:
				throw new ExpiredAccessTokenException();
			default:

				return resp;
		}
	}

	public Response deleteRestrictedUser(String ACCESS_TOKEN, Integer userId, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("removeRestrictedAttendee");

		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put("attendeeId", userId);

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "REMOVE RESTRICTED ATTENDEE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "REMOVE RESTRICTED ATTENDEE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		switch (resp.getReturnCode()) {
			case 0:

				/*
				 * ArrayList<RestrictedUser> restrictedList = new ArrayList<>();
				 * 
				 * @SuppressWarnings("unchecked") List<HashMap<String, Object>> usersTmp =
				 * (List<HashMap<String, Object>>)
				 * resp.getResult().get("restrictedEventAttendees"); for (HashMap<String,
				 * Object> userRestricted : usersTmp) { RestrictedUser user = new
				 * RestrictedUser((int)userRestricted.get("id"),(int)userRestricted.get(
				 * "customerId"),(int)userRestricted.get("applicationId"),
				 * (String)userRestricted.get("name"), (String)userRestricted.get("lastName"),
				 * (String)userRestricted.get("email"), (boolean)userRestricted.get("active"));
				 * restrictedList.add(user); }
				 */

				return resp;
			case -2:
				throw new ExpiredAccessTokenException();
			default:

				return resp;
		}
	}

	public Response getEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("findEventFundSettings");

		HashMap<String, Object> parameters = new HashMap<>();

		parameters.put("fundId", eventFundSettings.getFundId());

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		return responseStandard(resp);

	}

	public Response updateEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("updateEventFundSettings");

		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> fundMap = new HashMap<>();

		fundMap.put("id", eventFundSettings.getId());
		fundMap.put("fund_id", eventFundSettings.getFundId());
		fundMap.put("start_date", eventFundSettings.getStartDate());
		fundMap.put("end_date", eventFundSettings.getEndDate());
		fundMap.put("allowed_days_to_access", eventFundSettings.getAllowDays());
		fundMap.put("signature_required", eventFundSettings.isSignatureRequired());
		if(eventFundSettings.isSignatureRequired()) {
			fundMap.put("signature_document_id", eventFundSettings.getSignatureDocumentId());
		}

		parameters.put("event_fund_settings", fundMap);

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		return responseStandard(resp);
	}

	public Response removeEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("removeEventFundSettings");

		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> fundMap = new HashMap<>();

		// parameters.put("id", eventFundSettings.getId());
		// parameters.put("fund_id", eventFundSettings.getFundId());

		fundMap.put("id", eventFundSettings.getId());
		fundMap.put("fund_id", eventFundSettings.getFundId());

		parameters.put("event_fund_settings", fundMap);

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "REMOVE EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "REMOVE EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		return responseStandard(resp);
	}

	public Response addEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("addEventFundSettings");

		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, Object> fundMap = new HashMap<>();

		fundMap.put("fund_id", eventFundSettings.getFundId());
		fundMap.put("start_date", eventFundSettings.getStartDate());
		fundMap.put("end_date", eventFundSettings.getEndDate());
		fundMap.put("allowed_days_to_access", eventFundSettings.getAllowDays());
		fundMap.put("signature_required", eventFundSettings.isSignatureRequired());
		if(eventFundSettings.isSignatureRequired()) {
			fundMap.put("signature_document_id", eventFundSettings.getSignatureDocumentId());
		}

		parameters.put("event_fund_settings", fundMap);

		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD EVENT FUND SETTINGS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		return responseStandard(resp);
	}

	private Response responseStandard(Response resp) throws ExpiredAccessTokenException {
		switch (resp.getReturnCode()) {
			case 0:
				return resp;
			case -2:
				throw new ExpiredAccessTokenException();
			case -22:
				throw new ExpiredAccessTokenException();

			default:
				return resp;
		}
	}

	public ArrayList<CustomerUser> searchUsers(String ACCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();

		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("customerUserReport");
		HashMap<String, Object> parameters = new HashMap<>();
		ArrayList<HashMap<String, Object>> filter$ = new ArrayList<>();
		if (filter != null) {
			for (Map.Entry<String, Object> entry : filter.entrySet()) {
				HashMap<String, Object> item = new HashMap<>();
				item.put("fieldName",  entry.getKey().equals("userId") ? "u.id" : entry.getKey());
				item.put("fieldValue", entry.getKey().equals("userId") ? Integer.valueOf((String) entry.getValue()) : entry.getValue());
				filter$.add(item);
			}
		}

		parameters.put("filters", filter$);
		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		ArrayList<CustomerUser> foundUsers = new ArrayList<>();

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CUSTOMER USER REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CUSTOMER USER REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> usersTmps = (List<HashMap<String, Object>>) resp.getResult().get("users");

		for (HashMap<String, Object> userT : usersTmps) {
			CustomerUser user = new CustomerUser();
			user.setId((int) userT.get("id"));
			user.setUserId((int) userT.get("userid"));
			if(userT.get("first_name") != null)
				user.setFirstName(RequestWS.convertStringUtf8ToLatin1((String) userT.get("first_name")));
			else
				user.setFirstName("");
			if(userT.get("last_name") != null)
				user.setLastName(RequestWS.convertStringUtf8ToLatin1((String) userT.get("last_name")));
			else 
				user.setLastName("");
			user.setEmail((String) userT.get("email"));
			if(userT.get("phone_number")!= null)
				user.setPhoneNumber((String) userT.get("phone_number"));
			else
				user.setPhoneNumber("");
			user.setSigned( (boolean) userT.get("signature_status"));
			if (userT.get("signed_at") != null) {
				user.setSignedAt((long) userT.get("signed_at"));
				user.setSignatureUrl((String) userT.get("signature_url"));
			}
				
			if (userT.get("created_at") != null)
				user.setCreatedAt((long) userT.get("created_at"));
			
			if (userT.get("country_id") != null)
				user.setCountryId((int) userT.get("country_id"));
			
			if (userT.get("preferred_language") != null)
				user.setPreferredLanguage(userT.get("preferred_language") != null ? (int) userT.get("preferred_language"):0);
			
			
			foundUsers.add(user);
		}
		return foundUsers;

	}
	
	public ArrayList<CustomerUser> userSearchAllEvent(String ACCESS_TOKEN, CustomerUser filter, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("userUserReportAllEventMethodImpl");
		HashMap<String, Object> parameters = new HashMap<>();
		ArrayList<CustomerUser> foundUsers = new ArrayList<>();
		parameters.put("filters", filter);
		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "USER REPORT ALL EVENT", hsr.getHeader("user-agent"),getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "USER REPORT ALL EVENT", hsr.getHeader("user-agent"), getClientIp(hsr), 2, resp.getReturnMessage());
		}

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> usersTmps = (List<HashMap<String, Object>>) resp.getResult().get("user");
		
		for (HashMap<String, Object> userT : usersTmps) {
			CustomerUser user = new CustomerUser();
			user.setId((int) userT.get("id"));
			user.setUserId((int) userT.get("id"));
			if( userT.get("firstName") != null)
				user.setFirstName(RequestWS.convertStringUtf8ToLatin1((String) userT.get("firstName")));
			else 
				user.setFirstName("");
			if(userT.get("lastName")!= null)
				user.setLastName(RequestWS.convertStringUtf8ToLatin1((String) userT.get("lastName")));
			else 
				user.setLastName("");
			
			user.setEmail((String) userT.get("email"));
			if(userT.get("phoneNumber") != null)
				user.setPhoneNumber((String) userT.get("phoneNumber"));
			else 
				user.setPhoneNumber("");
			
			if (userT.get("createdAt") != null)
				user.setCreatedAt((long) userT.get("createdAt"));
			
			if (userT.get("preferredLanguage") != null)
				user.setPreferredLanguage(userT.get("preferredLanguage") != null ? (int) userT.get("preferredLanguage"):0);
			
			foundUsers.add(user);
		}
		return foundUsers;

	}

	public Response validateDevice(String ACCESS_TOKEN, String email, String userName, int DEVICE_TYPE_ID, int langId,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template2 = new RestTemplate();
		Request requestExist = new Request();

		requestExist.setAccessToken(ACCESS_TOKEN);
		requestExist.setMethodName("existsUser");

		HashMap<String, Object> params = new HashMap<>();
		params.put("email", email);

		requestExist.setParameters(params);

		Response confirmUser = template2.postForObject(URL_REQUEST, requestExist, Response.class, new HashMap<>());

		if (confirmUser.getReturnCode() == 0) {
			Boolean existUser = (Boolean) confirmUser.getResult().get("existsUser");
			if (!existUser) {
				RestTemplate template = new RestTemplate();
				Request request = new Request();

				request.setAccessToken(ACCESS_TOKEN);
				request.setMethodName("validateDevice");

				HashMap<String, Object> parameters = new HashMap<>();
				parameters.put("device", email);
				parameters.put("deviceTypeId", DEVICE_TYPE_ID);
				parameters.put("userName", userName);
				parameters.put("langId", langId);

				request.setParameters(parameters);

				Response result = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

				addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "EXISTS USER", hsr.getHeader("user-agent"),
						getClientIp(hsr), 0, "");

				return result;
			} else {
				Response userExist = new Response();
				userExist.setReturnCode(203);
				userExist.setReturnMessage("The email already exist in system, please try other email.");
				return userExist;
			}
		} else {
			return confirmUser;
		}

	}

	public Response validateCodeAndEdit(String ACCESS_TOKEN, int userId, int validationId, String validationCode,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template2 = new RestTemplate();
		Request requestEdition = new Request();
		requestEdition.setAccessToken(ACCESS_TOKEN);
		requestEdition.setMethodName("updateCustomerUserEmail");

		HashMap<String, Object> params = new HashMap<>();
		params.put("validationCodeId", validationId);
		params.put("deviceCode", validationCode);
		params.put("customerUserId", userId);

		requestEdition.setParameters(params);
		Response confirmEdition = template2.postForObject(URL_REQUEST, requestEdition, Response.class, new HashMap<>());

		if (confirmEdition.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER EMAIL", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER EMAIL", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, confirmEdition.getReturnMessage());
		}

		return confirmEdition;

	}

	public Response editWithNoConfirm(String ACCESS_TOKEN, int userId, String emailNew, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template2 = new RestTemplate();
		Request requestEdition = new Request();
		requestEdition.setAccessToken(ACCESS_TOKEN);
		requestEdition.setMethodName("updateCustomerUserEmail");

		HashMap<String, Object> params = new HashMap<>();
		params.put("newEmail", emailNew);
		params.put("customerUserId", userId);

		requestEdition.setParameters(params);
		Response confirmEdition = template2.postForObject(URL_REQUEST, requestEdition, Response.class, new HashMap<>());

		if (confirmEdition.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER EMAIL WITH VALIDATE A DEVICE OTP CODE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER EMAIL WITH VALIDATE A DEVICE OTP CODE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, confirmEdition.getReturnMessage());
		}

		return confirmEdition;

	}

	public Response editUserRegistered(String ACCESS_TOKEN, CustomerUser userForEdit, String userAgent,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request requestEdition = new Request();
		requestEdition.setAccessToken(ACCESS_TOKEN);
		requestEdition.setMethodName("updateCustomerUser");

		HashMap<String, Object> params = new HashMap<>();
		HashMap<String, Object> userD = new HashMap<>();
		userD.put("id", userForEdit.getUserId());
		userD.put("firstName", userForEdit.getFirstName());
		userD.put("lastName", userForEdit.getLastName());
		userD.put("phoneNumber", userForEdit.getPhoneNumber());
		userD.put("phoneBefore", userForEdit.getPhoneBefore());
		userD.put("nameBefore", userForEdit.getNameBefore());
		userD.put("lastNameBefore", userForEdit.getLastNameBefore());
		userD.put("countryId", userForEdit.getCountryId());
		userD.put("countryIdBefore", userForEdit.getCountryIdBefore());
		

		params.put("user", userD);
		params.put("ipAddress", userForEdit.getIpAddress());
		params.put("userAgent", userAgent);
		params.put("operatorId", userForEdit.getCustomerId());

		requestEdition.setParameters(params);
		Response confirmEdition = template.postForObject(URL_REQUEST, requestEdition, Response.class, new HashMap<>());

		if (confirmEdition.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE CUSTOMER USER", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, confirmEdition.getReturnMessage());
		}

		return confirmEdition;

	}

	public AjaxResponseReport<CustomerTransaction> getUserTransactions(String ACCESS_TOKEN, int userId,
			String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request requestTransactions = new Request();
		requestTransactions.setAccessToken(ACCESS_TOKEN);
		requestTransactions.setMethodName("findAdminCustomerUserTransactions");
		HashMap<String, Object> params = new HashMap<>();
		params.put("customerUserId", userId);
		params.put("ipAddress", ipAddress);
		params.put("userAgent", userAgent);
		requestTransactions.setParameters(params);
		AjaxResponseReport<CustomerTransaction> response = new AjaxResponseReport<>();

		@SuppressWarnings("unchecked")
		Response result = template.postForObject(URL_REQUEST, requestTransactions, Response.class, new HashMap<>());

		if (result.getReturnCode() == 0) {
			Integer codeReturned = result.getReturnCode();
			String messageReturned = result.getReturnMessage();
			ArrayList<CustomerTransaction> transactions = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> rawTransactions = (List<HashMap<String, Object>>) result.getResult()
					.get("transactions");

			for (HashMap<String, Object> transaction : rawTransactions) {
				CustomerTransaction tx = new CustomerTransaction();
				tx.setId((int) transaction.get("txId"));
				tx.setTxDate((long) transaction.get("txDate"));
				tx.setFund((String) transaction.get("fund"));
				tx.setCardMasked((String) transaction.get("cardMasked"));
				tx.setCardBrand((String) transaction.get("cardBrand"));
				tx.setAmount((Number) transaction.get("amount"));
				tx.setStatus((String) transaction.get("status"));
				tx.setIsScheduled((Boolean) transaction.get("is_scheduled"));
				tx.setInternationalId((String) transaction.get("txInternalId"));
				tx.setPaymentInfo((String) transaction.get("paymentInfo"));
				tx.setActiveEvent((String) transaction.get("activeEvent"));
				tx.setSplitPayment((Boolean)transaction.get("splitPayment"));
				tx.setCurrentEvent((int) transaction.get("currentEvent"));
				tx.setAccessDateTxt((String)transaction.get("accessDateTxt"));
				tx.setDiscount((Number) transaction.get("discount"));
				tx.setAccessDateFrom(transaction.get("accessDateFrom") != null ? (Long)transaction.get("accessDateFrom") : null);
				tx.setAccessDateTo(transaction.get("accessDateTo") != null ? (Long)transaction.get("accessDateTo") : null);
				tx.setAccessDays(transaction.get("accessDays") != null ? (int)transaction.get("accessDays") : 0);
				tx.setSubscription((Boolean) transaction.get("subscription"));
				tx.setSubscriptionStatusId((Integer)transaction.get("subscriptionStatusId"));
				if(tx.getSubscription()) {
					if(PaymentStatuses.SUCCESS_TX.getStatus().equals((String) transaction.get("status")) && tx.getSubscriptionStatusId() == 3 ) {
						tx.setStatus("SUBSCRIPTION_CANCEL");
					}
				}

				transactions.add(tx);
			}

			response.setCode(codeReturned.toString());
			response.setMsg(messageReturned);
			response.setResult(transactions);
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND ADMIN CUSTOMER USER TRANSACTIONS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
			return response;

		} else {

			Integer codeReturned = result.getReturnCode();
			String messageReturned = result.getReturnMessage();
			response.setCode(codeReturned.toString());
			response.setMsg(messageReturned);
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND ADMIN CUSTOMER USER TRANSACTIONS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, result.getReturnMessage());
			return response;
		}

	}

	public AjaxResponseReport<CustomerTransaction> getAllUserTransactions(String ACCESS_TOKEN, int userId,
			String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request requestTransactions = new Request();
		requestTransactions.setAccessToken(ACCESS_TOKEN);
		requestTransactions.setMethodName("findCustomerUserAdminAllTransactionsMethodImpl");
		HashMap<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("ipAddress", ipAddress);
		params.put("userAgent", userAgent);
		requestTransactions.setParameters(params);
		AjaxResponseReport<CustomerTransaction> response = new AjaxResponseReport<>();

		@SuppressWarnings("unchecked")
		Response result = template.postForObject(URL_REQUEST, requestTransactions, Response.class, new HashMap<>());

		if (result.getReturnCode() == 0) {
			Integer codeReturned = result.getReturnCode();
			String messageReturned = result.getReturnMessage();
			ArrayList<CustomerTransaction> transactions = new ArrayList<>();
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> rawTransactions = (List<HashMap<String, Object>>) result.getResult()
					.get("transactions");

			for (HashMap<String, Object> transaction : rawTransactions) {
				CustomerTransaction tx = new CustomerTransaction();
				tx.setId((int) transaction.get("txId"));
				tx.setTxDate((long) transaction.get("txDate"));
				tx.setFund((String) transaction.get("fund"));
				tx.setCardMasked((String) transaction.get("cardMasked"));
				tx.setCardBrand((String) transaction.get("cardBrand"));
				tx.setAmount((Number) transaction.get("amount"));
				tx.setStatus((String) transaction.get("status"));
				tx.setIsScheduled((Boolean) transaction.get("is_scheduled"));
				tx.setInternationalId((String) transaction.get("txInternalId"));
				tx.setPaymentInfo((String) transaction.get("paymentInfo"));
				tx.setActiveEvent((String) transaction.get("activeEvent"));
				tx.setSplitPayment((Boolean)transaction.get("splitPayment"));
				tx.setCurrentEvent((int) transaction.get("currentEvent"));
				tx.setAccessDateTxt((String)transaction.get("accessDateTxt"));
				tx.setDiscount((Number) transaction.get("discount"));
				tx.setAccessDateFrom(transaction.get("accessDateFrom") != null ? (Long)transaction.get("accessDateFrom") : null);
				tx.setAccessDateTo(transaction.get("accessDateTo") != null ? (Long)transaction.get("accessDateTo") : null);
				tx.setAccessDays(transaction.get("accessDays") != null ? (int)transaction.get("accessDays") : 0);
				tx.setSubscription((Boolean) transaction.get("subscription"));
				tx.setSubscriptionStatusId((Integer)transaction.get("subscriptionStatusId"));
				if(tx.getSubscription()) {
					if(PaymentStatuses.SUCCESS_TX.getStatus().equals((String) transaction.get("status")) && tx.getSubscriptionStatusId() != null && tx.getSubscriptionStatusId() == 3 ) {
						tx.setStatus("SUBSCRIPTION_CANCEL");
					}
				}
				
				transactions.add(tx);
			}

			response.setCode(codeReturned.toString());
			response.setMsg(messageReturned);
			response.setResult(transactions);
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND CUSTOMER USER ADMIN ALL TRANSACTIONS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
			return response;

		} else {

			Integer codeReturned = result.getReturnCode();
			String messageReturned = result.getReturnMessage();
			response.setCode(codeReturned.toString());
			response.setMsg(messageReturned);
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND CUSTOMER USER ADMIN ALL TRANSACTIONS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, result.getReturnMessage());
			return response;
		}

	}
	
	public Response resendUserReceiptTx(String ACCESS_TOKEN, int userId, int txId, String ipAddress, String userAgent,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException,ExceedUsersSessionException {

		RestTemplate template = new RestTemplate();
		Request sendReceipt = new Request();
		sendReceipt.setAccessToken(ACCESS_TOKEN);
		sendReceipt.setMethodName("resendAdminPurchaseReceipt");

		HashMap<String, Object> params = new HashMap<>();

		params.put("customerUserId", userId);
		params.put("transactionId", txId);
		// params.put("ipAddress", userForEdit.getIpAddress());
		// params.put("userAgent", userAgent);

		sendReceipt.setParameters(params);
		Response confirmSendReceipt = template.postForObject(URL_REQUEST, sendReceipt, Response.class, new HashMap<>());

		if (confirmSendReceipt.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "RESEND ADMIN PURCHASE RECEIPT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
		} else if(confirmSendReceipt.getReturnCode() == -32) {
			HashMap<String, Object> result = confirmSendReceipt.getResult();
			ArrayList<HashMap<String, Object>> userSession = (ArrayList<HashMap<String, Object>>) result.get("userSessions");
			HashMap<String, Object> userIndex = userSession.get(0);
			String token = (String) userIndex.get("token");
			long customerId = ((Integer) result.get("customerId")).intValue();
			int currentCustomerUserId = currU.getCustomerId();
			int resultInvalidate = invalidateAllUserAccessToken(ACCESS_TOKEN, txId, currU.getEmail(),token);
			closeSSE((int)customerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		 }else {
			 addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "RESEND ADMIN PURCHASE RECEIPT", hsr.getHeader("user-agent"),
						getClientIp(hsr), 2, confirmSendReceipt.getReturnMessage());
		}

		return confirmSendReceipt;

	}

	public Response sendMagicCustomerLink(String ACCESS_TOKEN, int userId, String email, int txId, String ipAddress,
			String userAgent, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException, ExceedUsersSessionException {

		RestTemplate template = new RestTemplate();
		Request sendLink = new Request();
		sendLink.setAccessToken(ACCESS_TOKEN);
		sendLink.setMethodName("authenticateCustomerUserByEmailV2");

		HashMap<String, Object> params = new HashMap<>();

		params.put("email", email);
		params.put("ipAddress", ipAddress);
		params.put("userAgent", userAgent);
		if(txId != 0)
			params.put("txId", txId);
		else
			params.put("txId", null);
		
		sendLink.setParameters(params);
		Response linkSent = template.postForObject(URL_REQUEST, sendLink, Response.class, new HashMap<>());

		if (linkSent.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUTHENTICATE CUSTOMER USER BY MAGIC LINK",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
		} else if(linkSent.getReturnCode() == -32) {
			HashMap<String, Object> result = linkSent.getResult();
			ArrayList<HashMap<String, Object>> userSession = (ArrayList<HashMap<String, Object>>) result.get("userSessions");
			HashMap<String, Object> userIndex = userSession.get(0);
			String token = (String) userIndex.get("token");
			long customerId = ((Integer) result.get("customerId")).intValue();
			int currentCustomerUserId = currU.getCustomerId();
			int resultInvalidate = invalidateAllUserAccessToken(ACCESS_TOKEN, txId, email,token);
			closeSSE((int)customerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		 }else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUTHENTICATE CUSTOMER USER BY MAGIC LINK",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, linkSent.getReturnMessage());
		}

		return linkSent;

	}

	
	
	public Response sendTemporyPasswodReset(String ACCESS_TOKEN, int customerUserId, String email, String ipAddress,
			String userAgent, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException, ExceedUsersSessionException {

		RestTemplate template = new RestTemplate();
		Request sendLink = new Request();
		sendLink.setAccessToken(ACCESS_TOKEN);
		sendLink.setMethodName("chagePasswordLink");

		HashMap<String, Object> params = new HashMap<>();

		params.put("email", email);
		params.put("ipAddress", ipAddress);
		params.put("userAgent", userAgent);

		sendLink.setParameters(params);
		Response linkSent = template.postForObject(URL_REQUEST, sendLink, Response.class, new HashMap<>());

		if (linkSent.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "Reset password",hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
		} else if(linkSent.getReturnCode() == -32) {
			HashMap<String, Object> result = linkSent.getResult();
			ArrayList<HashMap<String, Object>> userSession = (ArrayList<HashMap<String, Object>>) result.get("userSessions");
			HashMap<String, Object> userIndex = userSession.get(0);
			String token = (String) userIndex.get("token");
			int currentCustomerUserId = currU.getCustomerId();
			int resultInvalidate = invalidateAllUserAccessToken(ACCESS_TOKEN, token);
			closeSSE(currentCustomerUserId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		 }else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CHANGE PASSWORD LINK",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, linkSent.getReturnMessage());
		}

		return linkSent;

	}
	
	public Response invalidateTx(String ACCESS_TOKEN, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("invalidateTransaction");

		HashMap<String, Object> params = new HashMap<>();

		params.put("transactionId", txId);
		params.put("commentary", commentary);
		request.setParameters(params);
		Response invalidateConfirm = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (invalidateConfirm.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "INVALIDATE TRANSACTION", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "INVALIDATE TRANSACTION", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, invalidateConfirm.getReturnMessage());
		}

		return invalidateConfirm;

	}

	public Response invalidateTx(String ACCESS_TOKEN, int customerUserId, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("invalidateTransaction");

		HashMap<String, Object> params = new HashMap<>();

		params.put("transactionId", txId);
		params.put("commentary", commentary);
		params.put("customerUserId", customerUserId);
		request.setParameters(params);
		Response invalidateConfirm = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (invalidateConfirm.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "INVALIDATE TRANSACTION", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "INVALIDATE TRANSACTION", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, invalidateConfirm.getReturnMessage());
		}

		return invalidateConfirm;

	}
	
	public Response cancelTx(String ACCESS_TOKEN, int customerUserId, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("cancelTransactionMethodImpl");

		HashMap<String, Object> params = new HashMap<>();

		params.put("transactionId", txId);
		params.put("commentary", commentary);
		params.put("customerUserId", customerUserId);
		params.put("adminEmail", currU.getEmail());
		request.setParameters(params);
		Response invalidateConfirm = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (invalidateConfirm.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CANCEL TRANSACTION WITH REFUND", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CANCEL TRANSACTION WITH REFUND", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, invalidateConfirm.getReturnMessage());
		}

		return invalidateConfirm;

	}
	
	public Response getComissionSettings(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("findEventCommissionSettings");

		Response comissionSettings = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (comissionSettings.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND EVENT COMMISSION SETTINGS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND EVENT COMMISSION SETTINGS",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, comissionSettings.getReturnMessage());
		}

		return comissionSettings;

	}

	public Response getBlackList(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("listAllBlacklistItems");

		Response comissionSettings = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (comissionSettings.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "LIST ALL BLACKLIST ITEMS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "LIST ALL BLACKLIST ITEMS", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, comissionSettings.getReturnMessage());
		}

		return comissionSettings;

	}

	public Response unBlockFromBlackList(String ACCESS_TOKEN, int id, HttpServletRequest hsr, CustomerUser currU)
			throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("cleanBlacklistItem");
		HashMap<String, Object> params = new HashMap<>();
		params.put("blackListDataItemId", id);
		request.setParameters(params);

		Response isUnblocked = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (isUnblocked.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CLEAN BLACK LIST ITEM", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "CLEAN BLACK LIST ITEM", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, isUnblocked.getReturnMessage());
		}

		return isUnblocked;

	}

	public AjaxResponseReport<UserAccess> getUserAccessList(String ACCESS_TOKEN, String email, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {

		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("findUserAccessTokenInfoByEvent");
		HashMap<String, Object> params = new HashMap<>();
		params.put("userEmail", email);
		request.setParameters(params);

		Response userAccessList = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());

		if (userAccessList.getReturnCode() == 0) {
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> rawList = (List<HashMap<String, Object>>) userAccessList.getResult()
					.get("userAccessTokenInfo");
			ArrayList<UserAccess> listFormatted = new ArrayList<>();
			for (HashMap<String, Object> row : rawList) {
				UserAccess listRow = new UserAccess();
				listRow.setId((int) row.get("id"));
				listRow.setEmail((String) row.get("userEmail"));
				listRow.setCreatedAt((Number) row.get("createdAt"));
				listRow.setCustomerName((String) row.get("customerName"));
				listRow.setApplicationName((String) row.get("applicationName"));
				listRow.setCustomerId((int) row.get("customerId"));
				listRow.setApplicationId((int) row.get("applicationId"));
				listRow.setIpAddress((String) row.get("ipAddress"));
				listRow.setUserAgent((String) row.get("userAgent"));
				listRow.setTokenStatusId((int) row.get("tokenStatusId"));
				listRow.setTokenStatusName((String) row.get("tokenStatusName"));

				listFormatted.add(listRow);
			}

			AjaxResponseReport<UserAccess> response = new AjaxResponseReport<>();
			response.setCode("0");
			response.setMsg(userAccessList.getReturnMessage());
			response.setResult(listFormatted);

			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND USER ACCESS TOKEN INFO BY EVENT",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "");

			return response;

		} else {
			AjaxResponseReport<UserAccess> response = new AjaxResponseReport<>();
			Integer codeResp = (Integer) userAccessList.getReturnCode();
			String code = codeResp.toString();
			response.setCode(code);
			response.setMsg(userAccessList.getReturnMessage());
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "FIND USER ACCESS TOKEN INFO BY EVENT",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, userAccessList.getReturnMessage());
			return response;
		}

	}

	public Response resendUserReceiptOp(String ACCESS_TOKEN, int userId, int txId, String adminEmail,
			HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException, ExceedUsersSessionException {

		RestTemplate template = new RestTemplate();
		Request sendReceipt = new Request();
		sendReceipt.setAccessToken(ACCESS_TOKEN);
		sendReceipt.setMethodName("resendAdminPurchaseReceipt");

		HashMap<String, Object> params = new HashMap<>();
		params.put("customerUserId", userId);
		params.put("transactionId", txId);
		params.put("adminEmail", adminEmail);

		sendReceipt.setParameters(params);
		Response confirmSendReceipt = template.postForObject(URL_REQUEST, sendReceipt, Response.class, new HashMap<>());
		
		if (confirmSendReceipt.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "RESEND ADMIN PURCHASE RECEIPT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, confirmSendReceipt.getReturnMessage());
		} else if(confirmSendReceipt.getReturnCode() == -32) {
			HashMap<String, Object> result = confirmSendReceipt.getResult();
			ArrayList<HashMap<String, Object>> userSession = (ArrayList<HashMap<String, Object>>) result.get("userSessions");
			HashMap<String, Object> userIndex = userSession.get(0);
			String token = (String) userIndex.get("token");
			long customerId = ((Integer) result.get("customerId")).intValue();
			int currentCustomerUserId = currU.getCustomerId();
			int resultInvalidate = invalidateAllUserAccessToken(ACCESS_TOKEN, txId, adminEmail,token);
			closeSSE((int)customerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		 }else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(),  "RESEND ADMIN PURCHASE RECEIPT",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, confirmSendReceipt.getReturnMessage());
		}
		

		return confirmSendReceipt;

	}

	public ArrayList<Audit> getAppLogList(String ACCESS_TOKEN, HashMap<String, Object> filter, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setAccessToken(ACCESS_TOKEN);
		request.setMethodName("auditReport");
		HashMap<String, Object> parameters = new HashMap<>();
		ArrayList<HashMap<String, Object>> filter$ = new ArrayList<>();
		if (filter != null) {
			for (Map.Entry<String, Object> entry : filter.entrySet()) {
				HashMap<String, Object> item = new HashMap<>();
				item.put("fieldName", entry.getKey());
				item.put("fieldValue", entry.getValue());
				filter$.add(item);
			}
		}

		parameters.put("filters", filter$);
		parameters.put("audit_level_id", 0);
		request.setParameters(parameters);

		Response resp = template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		ArrayList<Audit> records = new ArrayList<>();

		if (resp.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUDIT REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "Consulting app log");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUDIT REPORT", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, resp.getReturnMessage());
		}

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> recordsTmps = (List<HashMap<String, Object>>) resp.getResult()
				.get("auditRecords");

		for (HashMap<String, Object> recordT : recordsTmps) {
			Audit auditRecord = new Audit();
			auditRecord.setId((int) recordT.get("id"));
			auditRecord.setCustomerId((int) recordT.get("customer_id"));
			auditRecord.setApplicationId((int) recordT.get("application_id"));
			auditRecord.setWho((String) recordT.get("who"));
			auditRecord.setWhat((String) recordT.get("what"));
			auditRecord.setCreatedAt((long) recordT.get("created_at"));
			auditRecord.setSource((String) recordT.get("source"));
			auditRecord.setIpAddress((String) recordT.get("ip_address"));
			auditRecord.setUserAgent((String) recordT.get("user_agent"));
			auditRecord.setAuditLevel((String) recordT.get("auditlevel"));
			auditRecord.setData((String) recordT.get("data"));

			records.add(auditRecord);
		}

		return records;
	}

	public void addAuditRecord(String accessToken, String who, String what, String userAgent, String ipAddress,
			int levelId, String data) throws ExpiredAccessTokenException {
		try {
			RestTemplate template = new RestTemplate();
			Request request = new Request();
			request.setMethodName("addAuditRecord");
			request.setAccessToken(accessToken);
			HashMap<String, Object> parm = new HashMap<>();
			HashMap<String, Object> auditRecord = new HashMap<>();
			auditRecord.put("who", who);
			auditRecord.put("what", what.toUpperCase());
			auditRecord.put("ip_address", ipAddress);
			auditRecord.put("user_agent", userAgent);
			auditRecord.put("source", SOURCE);
			auditRecord.put("audit_level_id", levelId);
			auditRecord.put("data", data);
			parm.put("audit_record", auditRecord);
			request.setParameters(parm);
			template.postForObject(URL_REQUEST, request, Response.class, new HashMap<>());
		} catch (Exception e) {
			addAuditRecord(accessToken, who, what.toUpperCase(), userAgent, ipAddress, 2, " e: " + e.getMessage());
		}
	}

	private static final String[] HEADERS_LIST = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static String getClientIp(HttpServletRequest request) {
	    for (String header : HEADERS_LIST) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}
	public Response getAllLanguages(String ACCESS_TOKEN) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request rq = new Request();
		rq.setAccessToken(ACCESS_TOKEN);
		rq.setMethodName("listAllLanguages");

		Response getLanguages = template.postForObject(URL_REQUEST, rq, Response.class, new HashMap<>());

		return getLanguages;
	}

	public Response getAllAssets(String ACCESS_TOKEN) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request rq = new Request();
		rq.setAccessToken(ACCESS_TOKEN);
		rq.setMethodName("listAllAssets");

		Response assetsList = template.postForObject(URL_REQUEST, rq, Response.class, new HashMap<>());

		return assetsList;
	}
	
	public Response getEventsAssetsByLang(String ACCESS_TOKEN, int lang_id) throws ExpiredAccessTokenException {
		try {
			RestTemplate template = new RestTemplate();
			Request rq = new Request();
			rq.setAccessToken(ACCESS_TOKEN);
			rq.setMethodName("findEventAssetsByLang");
			HashMap<String, Object> params = new HashMap<>();
			params.put("langId", lang_id);
			rq.setParameters(params);
			Response assetsByLang = template.postForObject(URL_REQUEST, rq, Response.class, new HashMap<>());
			return assetsByLang;
		} catch (Exception e) {
			Response assetsByLang = new Response();
			assetsByLang.setReturnCode(-4);
			assetsByLang.setReturnMessage(e.getMessage());
			return assetsByLang;
		}
				
	}
	
	public Response addSetting(String ACCESS_TOKEN, ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request rq = new Request();
		rq.setAccessToken(ACCESS_TOKEN);
		rq.setMethodName("addEventAssets");
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("eventAssets", assets);
		
		rq.setParameters(params);

		Response result = template.postForObject(URL_REQUEST, rq, Response.class, new HashMap<>());
		
		if (result.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD EVENT ASSETS FROM ADMIN", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "ADD EVENT ASSETS FROM ADMIN", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, result.getReturnMessage());
		}

		return result;
	}
	
	public Response updateSetting(String ACCESS_TOKEN, ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request rq = new Request();
		rq.setAccessToken(ACCESS_TOKEN);
		rq.setMethodName("updateEventAssets");
		HashMap<String, Object> params = new HashMap<>();
		params.put("eventAssets", assets);		
		rq.setParameters(params);
		Response result = template.postForObject(URL_REQUEST, rq, Response.class, new HashMap<>());
		
		if (result.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE EVENT ASSETS FROM ADMIN", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "");
		} else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "UPDATE EVENT ASSETS FROM ADMIN", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, result.getReturnMessage());
		}

		return result;
	}
	
	public int invalidateUserAccessToken(String accessToken, JSONArray userAccessToken) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("invalidateUserAccessToken");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("invalidUserAccessTokens", userAccessToken);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		int finalresult = 0;
		switch (resp.getReturnCode()) {
		case 0:
			finalresult = (int) resp.getResult().get("invalidatedUserAccessTokensCount");	
			return finalresult;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return finalresult;
		}
	}
	
	public int invalidateAllUserAccessToken(String accessToken, String userAccessToken) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("invalidateUserSessions");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("invalidUserAccessToken", userAccessToken);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		int finalresult = 0;
		switch (resp.getReturnCode()) {
		case 0:
			finalresult = 1;	
			return finalresult;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return finalresult;
		}
	}
	
	public int invalidateAllUserAccessToken(String accessToken, int txId, String email, String userAccessToken) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("invalidateUserSessions");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("invalidUserAccessToken", userAccessToken);
		parm.put("email", email);
		parm.put("txId", txId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		int finalresult = 0;
		switch (resp.getReturnCode()) {
		case 0:
			finalresult = 1;	
			return finalresult;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return finalresult;
		}
	}
	
	public Response approveSignature(String ACCESS_TOKEN,  int customerId, long transaction, String comment, HttpServletRequest hsr,
			CustomerUser currU) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("registerNewAdminDocumentSignature");
		request.setAccessToken(ACCESS_TOKEN);
		HashMap<String, Object> params = new HashMap<>();
		params.put("customerUserId", customerId);
		params.put("adminComments", comment);
		params.put("adminEmail", currU.getEmail());		
		params.put("ipAddress", getClientIp(hsr));
		params.put("transactionId", transaction);
		request.setParameters(params);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		
		return resp;
		
		
	}
	
	public boolean closeSSE(int userId, String userAccessToken) {		
		try {
			String eventURL = URL_SSE+"logout/"+userId+"@"+userAccessToken;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> resp = restTemplate.getForEntity(eventURL, String.class);
			return (resp.getStatusCode().is2xxSuccessful());
		} catch (Exception e) {
			return false;
		}
	}

	public HashMap<String, Object> verifyUser2(String email, String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findLastCustomerUserDataByEmail");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("userEmail", email);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:			
			return resp.getResult();
		default:
			return null;
		}
	}
	
	public AjaxResponseBodyHash2 verifyUserAddAttendee(String email, String aCCESS_TOKEN) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findLastCustomerUserDataByEmail");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("userEmail", email);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			result.setCode("200");
			result.setMsg("exist");
			result.setResult(resp.getResult());
			return result;
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return result;
		default:
			//return null;
		}
		return null;
	}
	
	public Response getOnlineUsers(String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findUsersEventInfo");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}
	
	public Response getEventDetailsregisteredUsers(String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getEventDetailsUsersRegistered");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;		
	}		
	
	public Response getEventDetailsOnlineUsers (String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("getEventDetailsOnlineUsers");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;		
	}		
	
	public Response addBlacklistItem(String data ,int dataTypeId, String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("addBlacklistItem");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("data", data);
		parm.put("dataTypeId", dataTypeId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}
	
	public Response addWhileListItem(String data, int adminUserId, String reason,String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("addWhiteListItem");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("email", data);
		parm.put("adminUserId", adminUserId);
		parm.put("reason", reason);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}
	
	public Response listWhileListItem(String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("listAllWhiteListItems");
		request.setAccessToken(aCCESS_TOKEN);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}
	
	public Response deleteWhiteListItem(String data, int adminUserId, String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("deleteWhiteListItem");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("whiteListDataItemId", Integer.parseInt(data));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}

	public Response deleteSourceCodeItem(String data, int adminUserId, String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("deleteSourceCode");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("sourceCodeItemId", Integer.parseInt(data));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}
	
	public Response updateReportingTables(String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("refreshReportTables");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		return resp;
		
	}

	public Response reportUserWireFraud(int customerUserId, int txId, String aCCESS_TOKEN) {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("reportUserWireFraud");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("customerUserId", customerUserId);
		parm.put("txId", txId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		HashMap<String, Object> result = resp.getResult();
		ArrayList<HashMap<String, Object>> resultTokens = (ArrayList<HashMap<String, Object>>) result.get("userAccessTokens");
		for (HashMap<String, Object> map : resultTokens) {
			int customerId = (int)map.get("customerId");
			String token = (String) map.get("token");
			fraudSSE(customerId, token);
		}
		
		
		return resp;
	}
	
	public Response transferTransactionToAnotherUser(String emailOrigin, String emailToTransfer, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("transferSuccessfulTransactionToAnotherUser");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("emailOrigin", emailOrigin);
		parm.put("emailToTransfer", emailToTransfer);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if(resp.getReturnCode() == -22 ) {
			throw new ExpiredAccessTokenException();
		}
		return resp;
	}
	
	public Response transferTransactionToAnotherUser(String emailOrigin, String emailToTransfer, int customerId, long transactionId, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("transferSuccessfulTransactionToAnotherUser");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("emailOrigin", emailOrigin);
		parm.put("emailToTransfer", emailToTransfer);
		parm.put("customerId", customerId);
		parm.put("transactionId", transactionId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if(resp.getReturnCode() == -22 ) {
			throw new ExpiredAccessTokenException();
		}
		return resp;
	}
	
	public boolean fraudSSE(int customerId, String userAccessToken) {		
		try {
			String eventURL = URL_SSE+"sendMessage/"+customerId+"@"+userAccessToken+"@fraud";
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> resp = restTemplate.getForEntity(eventURL, String.class);
			return (resp.getStatusCode().is2xxSuccessful());
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean sendMessageSSE(int customerId, String userAccessToken, String message) {		
		try {
			String eventURL = URL_SSE+"sendMessage/"+customerId+"@"+userAccessToken+"@"+message;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> resp = restTemplate.getForEntity(eventURL, String.class);
			return (resp.getStatusCode().is2xxSuccessful());
		} catch (Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String accessToken) throws ExpiredAccessTokenException, AlreadyExistsException, IOException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("registerNewUser");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		HashMap<String, Object> user = new HashMap<>();
		user.put("email", newUser.getEmail());
		user.put("firstName", newUser.getFirstName());
		user.put("lastName", newUser.getLastName());
		user.put("password", newUser.getPassword());
		user.put("genderId", newUser.getGenderId());
		user.put("birthDate", newUser.getBirthDate());
		user.put("profileImage", newUser.getProfileimage());
		user.put("countryId", newUser.getCountryId());
		//user.put("stateText", newUser.getStateText());
		user.put("cityText", newUser.getCitytext());
		user.put("phoneNumber",newUser.getPhoneNumber());
		//user.put("stateText",getCountryNameByIP(ipUser));
		parm.put("user", user);
		parm.put("ipAddress", ipUser);
		parm.put("userAgent", userAgent);		
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:		
			addAuditRecord(accessToken,newUser.getEmail(),"registerNewUser",userAgent,ipUser,0,"");			
			HashMap<String, Object> customerResult = (HashMap<String, Object>) resp.getResult().get("customer_user");
			HashMap<String, Object> userAccessToken = (HashMap<String, Object>) resp.getResult().get("userAccessToken");
			CustomerNewUser customerUser = new CustomerNewUser((int)customerResult.get("userId"),
					(String)customerResult.get("firstName"), 
					(String)customerResult.get("lastName"), 
					(int)customerResult.get("genderId"), 
					((String)customerResult.get("birthDate")==null) ? "" :
						(String)customerResult.get("birthDate"), 
						((String)customerResult.get("profileImage")==null) ? "" :
							(String)customerResult.get("profileImage"),
							(int)customerResult.get("statedId"),
							(int)customerResult.get("cityId"),
							(int)customerResult.get("customerId"),
							((String)customerResult.get("stateText")==null) ? "" :
								(String)customerResult.get("stateText"), 
								((String)customerResult.get("cityText")==null) ? "" :
									(String)customerResult.get("cityText"), 
									(String)userAccessToken.get("token"),
									(int)customerResult.get("countryId"),
									(int)customerResult.get("id"),
									newUser.getEmail());
			customerUser.setPayment(((Boolean) resp.getResult().get("payment"))!=null ? (Boolean) resp.getResult().get("payment"): false);
			return customerUser;
		case -2:
		case -22:
			addAuditRecord(accessToken,newUser.getEmail(),"REGISTER NEW USER",userAgent,ipUser,2,"ExpiredAccessTokenException");	
			throw new ExpiredAccessTokenException();	
		case -9:			
			addAuditRecord(accessToken,newUser.getEmail(),"REGISTER NEW USER",userAgent,ipUser,2,"AlreadyExistsException");
			int idUser = (int) resp.getResult().get("customerUserId");
			boolean payment = ((Boolean) resp.getResult().get("payment"))!=null ? (Boolean) resp.getResult().get("payment"): false;
			throw new AlreadyExistsException(String.valueOf(idUser).concat("-").concat(String.valueOf(payment)));
		default:
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public CustomerNewUser updateNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String accessToken) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("updateCustomerUser");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		HashMap<String, Object> user = new HashMap<>();

		user.put("id", newUser.getGenderId());
		user.put("firstName", newUser.getFirstName());
		user.put("lastName", newUser.getLastName());
		user.put("countryId", newUser.getCountryId());
		user.put("phoneNumber", newUser.getPhoneNumber());
		
		parm.put("user", user);
		parm.put("ipAddress", ipUser);
		parm.put("userAgent", userAgent);		
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:		
			addAuditRecord(accessToken,newUser.getEmail(),"UPDATE CUSTOMER USER",userAgent,ipUser,0,"");
			HashMap<String, Object> customerResult = (HashMap<String, Object>) resp.getResult().get("customer_user");
			HashMap<String, Object> userAccessToken = (HashMap<String, Object>) resp.getResult().get("userAccessToken");
			CustomerNewUser customerUser = new CustomerNewUser((int)customerResult.get("userId"),
					(String)customerResult.get("firstName"), 
					(String)customerResult.get("lastName"), 
					(int)customerResult.get("genderId"), 
					((String)customerResult.get("birthDate")==null) ? "" :
						(String)customerResult.get("birthDate"), 
						((String)customerResult.get("profileImage")==null) ? "" :
							(String)customerResult.get("profileImage"),
							(int)customerResult.get("statedId"),
							(int)customerResult.get("cityId"),
							(int)customerResult.get("customerId"),
							((String)customerResult.get("stateText")==null) ? "" :
								(String)customerResult.get("stateText"), 
								((String)customerResult.get("cityText")==null) ? "" :
									(String)customerResult.get("cityText"), 
									(String)userAccessToken.get("token"),
									(int)customerResult.get("countryId"),
									(int)customerResult.get("id"),
									newUser.getEmail());

			return customerUser;
		case -2:
		case -22:
			addAuditRecord(accessToken,newUser.getEmail(),"UPDATE CUSTOMER USER",userAgent,ipUser,2,"ExpiredAccessTokenException");
			throw new ExpiredAccessTokenException();	
		case -25:		
			addAuditRecord(accessToken,newUser.getEmail(),"UPDATE CUSTOMER USER",userAgent,ipUser,2,"UnknownCustomerUserException");
			throw new UnknownCustomerUserException();
		default:
			return null;
		}
	}

	public Response addPurchaseSource(String nameSourcePurchase, String descriptionSourcePurchase, String activeSourcePurchase, int adminUserId, int langId, String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("addSourceCodeMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("nameSourcePurchase", nameSourcePurchase);
		parm.put("descriptionSourcePurchase", descriptionSourcePurchase);
		parm.put("activeSourcePurchase", activeSourcePurchase);
		parm.put("adminUserId", adminUserId);
		parm.put("langId", langId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		case -25:		
			throw new UnknownCustomerUserException();
		default:
			return null;
		}
	}
	
	public Response scarQR(String qr, int adminUserId,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("checkinEventTicketMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("qr", qr);
		parm.put("adminUserId", adminUserId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
		case -62: //NOT_PRESENTIAL_EVENT_CODE
		case -49: //UNKNOWN_OBJECT_CODE  no se encuentra el ticket
		case -4: //General Error
		case -57://INVALID_ADMIN_USER
		case -61://ALREADY_USED_TICKET
		case -29://NOT_SUCCESS_TRANSACTIONS_IN_APP
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		case -25:		
			throw new UnknownCustomerUserException();
		default:
			return null;
		}
	}
	
	public Response updateTags(String tags, int adminUserId,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("updateTagsByUserIdMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("tags", tags);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		case -25:		
			throw new UnknownCustomerUserException();
		default:
			return null;
		}
	}
	
	public Response purchaseUserInformationByEventCategory(String jsonObjS, int adminUserId,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("findSuccessfulShoppingByUserAndCategoryMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		JSONObject jsonObj = new JSONObject(jsonObjS);
		int userId = jsonObj.getInt("userId");
		int categoyEventId = jsonObj.getInt("categoyEventId");
		parm.put("userId", userId);
		parm.put("eventCategory", categoyEventId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		case -25:		
			throw new UnknownCustomerUserException();
		default:
			return null;
		}
	}
	
	public Response findSources(String accessToken) throws ExpiredAccessTokenException {
		RestTemplate template = new RestTemplate();
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("listAllBusinessSourceCodes");
		request.setAccessToken(accessToken);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0: 
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;			
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();
		default:
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		}							
	}
	
	public Response getListAllCountries(String accessToken) throws IOException, ExpiredAccessTokenException {		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		HashMap<String, Object> result = new HashMap<>();
		request.setMethodName("listAllCountries");
		request.setAccessToken(accessToken);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {			
			ArrayList<Country> aListCountries = new ArrayList<Country>();
			@SuppressWarnings("unchecked")
			List<HashMap<String, Object>> countriesTmp = (List<HashMap<String, Object>>) resp.getResult().get("countries");		
			for (HashMap<String, Object> countryTmp : countriesTmp) {		
				Country country = new Country((String)countryTmp.get("shortName"),
						(String)countryTmp.get("name"), (int)countryTmp.get("id"));
				aListCountries.add(country);
			}
			Response response = new Response();
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			
			//Algoritmo para poner de primero en la lista de paises: EEUU, CANADA, UK
			ArrayList<Country> countryOrder = new ArrayList<>();
			{
				Country eeuu = new Country("", "", 231);  //EEUU
				Country canada = new Country("", "", 38); //CANADA
				Country uk = new Country("", "", 230);    //UK
				Country tmp1  = aListCountries.get(aListCountries.indexOf(eeuu));
				Country tmp2  = aListCountries.get(aListCountries.indexOf(canada));
				Country tmp3  = aListCountries.get(aListCountries.indexOf(uk));
				
				ArrayList<Country> tmpCountry = new ArrayList<>(aListCountries);
				
				countryOrder.add(tmp1);
				countryOrder.add(tmp2);
				countryOrder.add(tmp3);
				
				tmpCountry.removeAll(countryOrder);
				
				Collections.sort(tmpCountry, new Country.CustomComparator());
				
				countryOrder.addAll(tmpCountry);
			}//fin del algoritmo
			
			result.put("ListCountries", countryOrder);
			response.setResult(result);
			
			
			return response;
		} else if (resp.getReturnCode() == -2) {
			throw new ExpiredAccessTokenException();
		} else {
			return null;
		}		
	}
	
	public HashMap<String, Object> verifyUser3(String email, String ip,String accessToken) throws ExpiredAccessTokenException {		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("existsUser");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("email", email);
		parm.put("ip", ip);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> verifyUser = new HashMap<>();
			verifyUser.put("existsUser", (Boolean) resp.getResult().get("existsUser"));
			if ((Boolean) resp.getResult().get("existsUser")){
				verifyUser.put("customerUserId", ((Integer) resp.getResult().get("customerUserId")) != null ? (int) resp.getResult().get("customerUserId") : null );
				verifyUser.put("password", (Boolean) resp.getResult().get("password"));
				verifyUser.put("user", resp.getResult().get("user"));
			}
			return verifyUser;
		case -45:
			HashMap<String, Object> blockUser = new HashMap<>();
			blockUser.put("message", (String) resp.getReturnMessage());			
			return blockUser;
		case -46:
			HashMap<String, Object> result = new HashMap<>();
			result.put("invalid", (String) resp.getReturnMessage());			
			return result;
		case -2:
		case -60:
			HashMap<String, Object> r = new HashMap<>();
			r.put("response", resp);			
			return r;
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
	
	public static String convertStringUtf8ToLatin1(String stringUtf8) {
		StringBuffer stringUtf8Buffer = new StringBuffer();
		String [] stringUtf8tmp = stringUtf8.split(" ");
		for (int i = 0; i < stringUtf8tmp.length; i++) {
			try {
				if(stringUtf8tmp[i].contains("ÃÂ¡")) {
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ¡","á"));
				}else if(stringUtf8tmp[i].contains("ÃÂ")) {
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ","Á"));
				}else if(stringUtf8tmp[i].contains("Ã£Â¡")) {
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ã£Â¡","á"));
				}else if(stringUtf8tmp[i].contains("Ã£Â©")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ã£Â©", "é"));
				}else if(stringUtf8tmp[i].contains("ÃÂ©")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ©", "é"));
				}else if(stringUtf8tmp[i].contains("Ã‘")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ã‘", "Ñ"));
				}else if(stringUtf8tmp[i].contains("ÃÂ±")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ±", "ñ"));
				}else if(stringUtf8tmp[i].contains("Ãš")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ãš", "Ú"));
				}else if(stringUtf8tmp[i].contains("Ã‰")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ã‰", "É"));
				}else if(stringUtf8tmp[i].contains("Ã£Â­")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("Ã£Â­", "í"));
				}else if(stringUtf8tmp[i].contains("ÃÂ³")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ³", "ó"));
				}else if(stringUtf8tmp[i].contains("ÃÂ­")){
					stringUtf8Buffer.append(stringUtf8tmp[i].replace("ÃÂ­", "í"));
				}else{
					stringUtf8Buffer.append(new String(stringUtf8tmp[i].getBytes()));
				}
				stringUtf8Buffer.append(" ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringUtf8Buffer.toString();
	}
	
	public UserInfo findUserInfo(String userId, String aCCESS_TOKEN) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findUserInfoMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("userId", userId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			result.setCode("200");
			result.setMsg("exist");
			HashMap<String, Object> userMap = (HashMap<String, Object>) resp.getResult().get("user");
		 	UserInfo userInfo = new UserInfo(); 
		 	userInfo.setId((Integer) userMap.get("id"));
			userInfo.setEmail( (String) userMap.get("email"));
			userInfo.setFirstName((String) userMap.get("firstName"));
			userInfo.setLastName((String) userMap.get("lastName"));
			userInfo.setCountry(userMap.get("country") != null ? userMap.get("country").toString() : "");
			userInfo.setTotalAmount(String.valueOf((Double) resp.getResult().get("totalAmount")));
			userInfo.setTags((String) userMap.get("tags") != null ? (String) userMap.get("tags") : "{}");
			userInfo.setActivitiesList((List<Map<String, Object>>)resp.getResult().get("listUserSupport"));
			userInfo.setPhoneNumber(userMap.get("phoneNumber") != null ? userMap.get("phoneNumber").toString() : "");
			userInfo.setSourceCode(userMap.get("sourceCode") != null ? userMap.get("sourceCode").toString() : "");
			userInfo.setCountryId(userMap.get("countryId") != null ? (Integer)userMap.get("countryId"):0);
			userInfo.setPreferredLanguage(userMap.get("preferredLanguage") != null ? (Integer)userMap.get("preferredLanguage"):0);
			userInfo.setStatusSales(userMap.get("statusSales") != null ? (Integer)userMap.get("statusSales"):null);

		 	return userInfo;
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> findLifecycle(String userId, String aCCESS_TOKEN) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findLifeCycleByUserMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("userId", userId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("lifeCycle");
		 	return resultMap;
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public HashMap<String, Object> findBitacoraUserById(String userId, String aCCESS_TOKEN) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findLifeCycleByUserMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("userId", userId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("lifeCycle");
		 	return resultMap.get(0);
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> findBusinessUserSupportHistory(String userId, String aCCESS_TOKEN) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findBusinessUserSupportHistoryMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		HashMap<String, Object> user = new HashMap<>();
		user.put("userId",userId);
		parm.put("user", user);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("businessUserSupportHistory");
		 	for (int i = 0; i < resultMap.size(); i++) {
				resultMap.get(i).put("commentdate", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(resultMap.get(i).get("commentdate")));
			}
			return resultMap;
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public Response addBusinessUserSupportHistory(String str, int adminUserId,  String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("addBusinessUserSupportHistoryMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		JSONObject jsonObj = new JSONObject(str);
	
		HashMap<String, Object> businessUserSupportHistory = new HashMap<>();
	    businessUserSupportHistory.put("userId", String.valueOf(jsonObj.getInt("userId"))  );
		businessUserSupportHistory.put("operatorId", String.valueOf(adminUserId) );
		businessUserSupportHistory.put("comment", jsonObj.getString("comment"));
	
		parm.put("businessUserSupportHistory", businessUserSupportHistory);
		
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			return resp;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public HashMap<String, Object> getRecentLoginByEmail(String userId, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("getRecentLoginByEmailMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("userId", userId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> resultMap = (HashMap<String, Object>) resp.getResult().get("userRecentLogin");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> listUserCreditCards(String str, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		JSONObject jsonObj = new JSONObject(str);
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("listUserCreditCardsMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("userId", String.valueOf(jsonObj.getInt("userId")));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("customerUserCreditCards");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> addUserCreditCard(String str, String aCCESS_TOKEN) throws ExpiredAccessTokenException, StripeUtilsException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		JSONObject jsonObj = new JSONObject(str);
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("addUserCreditCardMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("userId", jsonObj.getInt("userId"));
		parm.put("token", jsonObj.getString("token"));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("customerUserCreditCards");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		case -63:
			throw new StripeUtilsException(resp.getReturnMessage());
		default:
			//return null;
		}
		return null;
	}
	
	public HashMap<String, Object> getRecentPurchaseByEmail(String userId, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("getRecentPurchaseByEmailMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("userId", userId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> resultMap = (HashMap<String, Object>) resp.getResult().get("userRecentPurchase");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}

	public List<HashMap<String, Object>> deleteUserCard(String str, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		JSONObject jsonObj = new JSONObject(str);
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("removeUserCreditCardMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("userId", jsonObj.getInt("userId"));
		parm.put("token", jsonObj.getString("token"));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("customerUserCreditCards");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public List<HashMap<String, Object>> getEventsAvailableForPurchase(String str, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		JSONObject jsonObj = new JSONObject(str);
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("getEventsAvailableForPurchase");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("email", jsonObj.getString("email"));
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("eventsAvailable");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			((HashMap<String, Object>) result.getResult().get("user")).put("firstName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("firstName")));
			((HashMap<String, Object>) result.getResult().get("user")).put("lastName", RequestWS.convertStringUtf8ToLatin1((String)((HashMap<String, Object>) result.getResult().get("user")).get("lastName")));
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public Response updateToDateUserAccess(CustomerUser currU, HttpServletRequest hsr, String str,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		JSONObject jsonObj = new JSONObject(str);
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("updateUserGrantedAccessMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("txId", jsonObj.getLong("txId"));
		parm.put("userId", jsonObj.getLong("userId"));
		parm.put("timeZone", jsonObj.getString("timeZone"));
		parm.put("update", jsonObj.getString("update"));
		
		if("from".equalsIgnoreCase(jsonObj.getString("update"))) {
			parm.put("accessDateFrom", jsonObj.getLong("accessDateFrom"));
		}else {
			parm.put("accessDateToUpdate", jsonObj.getLong("accessDateToUpdate"));
		}

		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			//Se guarda en auditoria si se realizo el proceso exitosamente.
			String content = "UPDATE FIELD 'DATE TO' IN USER GRANTED ACCESS";
			if("from".equalsIgnoreCase(jsonObj.getString("update"))) {
				content = "UPDATE FIELD 'DATE FROM' IN USER GRANTED ACCESS";
			}
			
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), content,
					hsr.getHeader("user-agent"), getClientIp(hsr), resp.getReturnCode(), resp.getReturnMessage());
		case 4: // The error general
		case -64: //The access field 'date to' is required.
		case -65: //There is currently a transaction of the corresponding event in success status(5).
		case -66: //The access field 'date to' must be greater than the access field 'date from'
		case -67://The minimum of days for access to an event is ONE (1)
		case -68://Currently the transaction is already in expired status(10).
		case -69: //The access field 'date from' is required.
		case -71: //There is currently a transaction of the corresponding event in success progress(14).
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
	
	public Response expireUserAccess(CustomerUser currU, HttpServletRequest hsr, String str,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		JSONObject jsonObj = new JSONObject(str);
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("expireUserAccessMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();
		parm.put("txId", jsonObj.getLong("txId"));
		parm.put("userId", jsonObj.getLong("userId"));
		parm.put("timeZone", jsonObj.getString("timeZone"));

		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
		case 4: // The error general
		case -29: //Not approved transaction.
			//Se guarda 
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "EXPIRE USER ACCESS TO AN EVENT",
					hsr.getHeader("user-agent"), getClientIp(hsr), resp.getReturnCode(), resp.getReturnMessage());
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
	
	public Response sendMagicLinkOperator(String ACCESS_TOKEN, int userId, String email, String emailOp, int txId, String ipAddress,
			String userAgent, HttpServletRequest hsr, CustomerUser currU) throws ExpiredAccessTokenException, ExceedUsersSessionException {

		RestTemplate template = new RestTemplate();
		Request sendLink = new Request();
		sendLink.setAccessToken(ACCESS_TOKEN);
		sendLink.setMethodName("authenticateCustomerUserByEmailV2");

		HashMap<String, Object> params = new HashMap<>();

		params.put("email", email);
		params.put("emailOp", emailOp);
		params.put("ipAddress", ipAddress);
		params.put("userAgent", userAgent);
		if(txId != 0)
			params.put("txId", txId);
		else
			params.put("txId", null);
		
		sendLink.setParameters(params);
		Response linkSent = template.postForObject(URL_REQUEST, sendLink, Response.class, new HashMap<>());

		if (linkSent.getReturnCode() == 0) {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUTHENTICATE CUSTOMER USER BY MAGIC LINK TO OPERATOR",
					hsr.getHeader("user-agent"), getClientIp(hsr), 0, "success");
		} else if(linkSent.getReturnCode() == -32) {
			HashMap<String, Object> result = linkSent.getResult();
			ArrayList<HashMap<String, Object>> userSession = (ArrayList<HashMap<String, Object>>) result.get("userSessions");
			HashMap<String, Object> userIndex = userSession.get(0);
			String token = (String) userIndex.get("token");
			long customerId = ((Integer) result.get("customerId")).intValue();
			int currentCustomerUserId = currU.getCustomerId();
			int resultInvalidate = invalidateAllUserAccessToken(ACCESS_TOKEN, txId, email,token);
			closeSSE((int)customerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		 }else {
			addAuditRecord(ACCESS_TOKEN, currU.getEmail(), "AUTHENTICATE CUSTOMER USER BY MAGIC LINK TO OPERATOR",
					hsr.getHeader("user-agent"), getClientIp(hsr), 2, linkSent.getReturnMessage());
		}

		return linkSent;

	}
	
	public AuthorizationInfo assistedCCPurchaseSplitPayment(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData,
			PaymentGwParameters paymentGwParameters, int paymentGatewayId, String medium, String source, int lang,
			String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate)
			throws IOException, ExpiredAccessTokenException, ExceedUsersSessionException {

		String getJSON = JSONRequestFactory
				.getJSONassistedCCPurchase("assistedCCPurchase", aCCESS_TOKEN, txDetail, txUserData,
						paymentGwParameters, paymentGatewayId, medium, source, lang, coupon, operatorId, comments,startDate,checkStartDate)
				.toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);

		StringEntity input = new StringEntity(getJSON, "utf-8");
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2) {
			throw new ExpiredAccessTokenException();
		}
		
		if (totalResult.getInt("returnCode") == -32) {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray userSession  = result.getJSONArray("userSessions");
			JSONObject userIndex = userSession.getJSONObject(0);
			int currentCustomerId = userIndex.getInt("customerUserId");
			String token = userIndex.getString("token");
			int codeResult = invalidateAllUserAccessToken(aCCESS_TOKEN, token);
			int currentCustomerUserId = currU.getCustomerId();
			closeSSE(currentCustomerId, token);
			throw new ExceedUsersSessionException("exceeded-users");
		}
		
		if (totalResult.getInt("returnCode") == -45) {
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult("El correo indicado se encuentra en listas negras, por favor contacte a un supervisor para que realice su desbloqueo y luego vuelva a intentar la venta");
			return authorizationInfo;
		}	

		if (totalResult.getInt("returnCode") == -34) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			authorizationInfo.setCode("-34");
			authorizationInfo.setMsg(paymentResult.getString("resultMessage"));
			
		}
		
		if (totalResult.getInt("returnCode") == -53) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			authorizationInfo.setCode("-53");
			authorizationInfo.setMsg(paymentResult.getString("resultMessage"));
			
		}
		
		if (totalResult.getInt("returnCode") == -4) {
			AjaxResponseBody authorizationInfo = new AjaxResponseBody();
			authorizationInfo.setCode("-4");
			authorizationInfo.setMsg(totalResult.getString("returnMessage"));			
		}

		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONObject paymentResult = result.getJSONObject("paymentResult");
			JSONObject authorization = paymentResult.getJSONObject("authorizationInfo");
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			authorizationInfo.setTxId(authorization.getString("tx_id"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "ASSISTED CC PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 0, "success");
			httpClient.close();
			return authorizationInfo;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AuthorizationInfo authorizationInfo = new AuthorizationInfo();
			authorizationInfo.setResponseCode(new Integer(totalResult.getInt("returnCode")).toString());
			authorizationInfo.setMessageResult(totalResult.getString("returnMessage"));
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "ASSISTED CC PURCHASE", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return authorizationInfo;
		}

	}
	
	public List<HashMap<String, Object>> getAllEvents(int businessId, String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parm = new HashMap<>();
		Request request = new Request();
		request.setMethodName("getAllEventsMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		parm.put("businessId", businessId);
		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("allEvents");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	public List<HashMap<String, Object>> getOptionsDateReport(String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("findResponseOptionsDateReports");
		request.setAccessToken(aCCESS_TOKEN);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			List<HashMap<String, Object>> resultMap = (List<HashMap<String, Object>>) resp.getResult().get("options");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public HashMap<String, Object> findListAllSelectReports(String aCCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("listAllSelectReportsMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> resultMap = (HashMap<String, Object>) resp.getResult();
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			return null;///result;
		default:
			//return null;
		}
		return null;
	}
	
	public ArrayList<AffiliateReport> listReportAffiliateMaster(String aCCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU) throws IOException, ExpiredAccessTokenException {

		ArrayList<AffiliateReport> finalResult = new ArrayList<>();
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
		parm.put("typeReport", "affiliateMaster");

		json.put("methodName", "customerTransactionsReport");
		json.put("accessToken", aCCESS_TOKEN);
		json.put("parameters", parm);

		String getJSON = json.toString();
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT AFFILIATE MASTER", hsr.getHeader("user-agent"),
				getClientIp(hsr), 0, "SUCCESS");
		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				AffiliateReport reportDetail = new AffiliateReport();
				reportDetail.setAffiliate(arr.getJSONObject(i).isNull("affiliate_id") ? 0: arr.getJSONObject(i).getInt("affiliate_id"));
				reportDetail.setAffiliateName(arr.getJSONObject(i).isNull("name_affiliate") ? "N/A" : arr.getJSONObject(i).getString("name_affiliate"));
				reportDetail.setAffiliateEmail(arr.getJSONObject(i).isNull("affiliate_email") ? "N/A" : arr.getJSONObject(i).getString("affiliate_email"));
				reportDetail.setPaymentTypeAffiliate(arr.getJSONObject(i).isNull("payment_type_affiliate") ? "N/A" : arr.getJSONObject(i).getString("payment_type_affiliate"));
				reportDetail.setCountAffiliationConvertion(arr.getJSONObject(i).isNull("count_affiliation_convertion") ? 0: arr.getJSONObject(i).getDouble("count_affiliation_convertion"));
				reportDetail.setSumAffiliationBalance(arr.getJSONObject(i).isNull("sum_affiliation_balance") ? 0: arr.getJSONObject(i).getDouble("sum_affiliation_balance"));
				reportDetail.setPaymentReference(arr.getJSONObject(i).isNull("payment_reference") ? "N/A" : arr.getJSONObject(i).getString("payment_reference"));
				reportDetail.setDataPayment(arr.getJSONObject(i).isNull("data_payment") ? "N/A" : arr.getJSONObject(i).getString("data_payment"));
				reportDetail.setAffiliationPayed((arr.getJSONObject(i).isNull("affiliation_payed") ? false: arr.getJSONObject(i).getBoolean("affiliation_payed"))? "Paid":"Pending");


				finalResult.add(reportDetail);
			}

			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AffiliateReport reportDetail = new AffiliateReport();
			finalResult.add(reportDetail);
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT AFFILIATE MASTER", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}
	
	public ArrayList<AffiliateReport> listReportAffiliateDetail(String aCCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU) throws IOException, ExpiredAccessTokenException {

		ArrayList<AffiliateReport> finalResult = new ArrayList<>();
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
		parm.put("typeReport", "affiliateDetail");

		json.put("methodName", "customerTransactionsReport");
		json.put("accessToken", aCCESS_TOKEN);
		json.put("parameters", parm);

		String getJSON = json.toString();
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON);
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT AFFILIATE DETAIL", hsr.getHeader("user-agent"),
				getClientIp(hsr), 0, "SUCCESS");
		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				AffiliateReport reportDetail = new AffiliateReport();
				reportDetail.setAffiliate(arr.getJSONObject(i).isNull("affiliate_id") ? 0: arr.getJSONObject(i).getInt("affiliate_id"));
				reportDetail.setAffiliateName(arr.getJSONObject(i).isNull("name_affiliate") ? "N/A" : arr.getJSONObject(i).getString("name_affiliate"));
				reportDetail.setAffiliateEmail(arr.getJSONObject(i).isNull("affiliate_email") ? "N/A" : arr.getJSONObject(i).getString("affiliate_email"));
				reportDetail.setPaymentTypeAffiliate(arr.getJSONObject(i).isNull("payment_type_affiliate") ? "N/A" : arr.getJSONObject(i).getString("payment_type_affiliate"));
				reportDetail.setStripTrxId(arr.getJSONObject(i).isNull("strip_trx_id") ? "N/A" : arr.getJSONObject(i).getString("strip_trx_id"));
				
				reportDetail.setNameReferred(arr.getJSONObject(i).isNull("name_referred") ? "N/A" : arr.getJSONObject(i).getString("name_referred"));
				reportDetail.setUserEmail(arr.getJSONObject(i).isNull("email_referred") ? "N/A" : arr.getJSONObject(i).getString("email_referred"));
			
				reportDetail.setPaymentReferred(arr.getJSONObject(i).isNull("payment_referred") ? "N/A" : arr.getJSONObject(i).getString("payment_referred"));
				if(reportDetail.getStripTrxId().contains("SPLIT")) {
					reportDetail.setPaymentReferred("STRIPE");
				}
				reportDetail.setTypePaymentReferred(arr.getJSONObject(i).isNull("type_payment_referred") ? "N/A" : arr.getJSONObject(i).getString("type_payment_referred"));
				reportDetail.setEventName(arr.getJSONObject(i).isNull("event_name") ? "N/A" : arr.getJSONObject(i).getString("event_name"));
				reportDetail.setAmount(arr.getJSONObject(i).isNull("amount") ? 0: arr.getJSONObject(i).getDouble("amount"));
				reportDetail.setComission(arr.getJSONObject(i).isNull("comission") ? 0: arr.getJSONObject(i).getDouble("comission"));

				reportDetail.setDataPayment(arr.getJSONObject(i).isNull("data_payment") ? "N/A" : arr.getJSONObject(i).getString("data_payment"));
				reportDetail.setAffiliationPayed((arr.getJSONObject(i).isNull("affiliation_payed") ? false: arr.getJSONObject(i).getBoolean("affiliation_payed"))? "Paid":"Pending");
				reportDetail.setTransactionId(arr.getJSONObject(i).isNull("transaction_id") ? "N/A" : arr.getJSONObject(i).getString("transaction_id"));

				reportDetail.setDate(arr.getJSONObject(i).isNull("tx_date") ? null: arr.getJSONObject(i).getLong("tx_date"));
				
				reportDetail.setPaymentReference(arr.getJSONObject(i).isNull("payment_reference") ? "N/A" : arr.getJSONObject(i).getString("payment_reference"));


				try {
					long date = (long) reportDetail.getDate();
					Date dateObj = new Date(date);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat df2 = new SimpleDateFormat("HH:mm");
					reportDetail.setDateFormat(df.format(dateObj));
					reportDetail.setTimeFormat(df2.format(dateObj));

				} catch (Exception e) {
					logger.error("ERROR", e);
					reportDetail.setDateFormat(e.getMessage());
				}
				finalResult.add(reportDetail);
			}

			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			AffiliateReport reportDetail = new AffiliateReport();
			finalResult.add(reportDetail);
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT AFFILIATE DETAIL", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}
	public HashMap<String, Object> findResponseUpdatePaymentAffiliateReport(TransactionUpdate transactionUpdate,String ACCESS_TOKEN) throws ExpiredAccessTokenException {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		RestTemplate template = new RestTemplate();
		HashMap<String, Object> parameters = new HashMap<>();
		Request request = new Request();
		request.setMethodName("findResponseUpdatePaymentAffiliateReport");
		request.setAccessToken(ACCESS_TOKEN);
		parameters.put("transactionUpdate", transactionUpdate);
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
			HashMap<String, Object> resultMap = (HashMap<String, Object>) resp.getResult().get("updateTransactions");
			return resultMap;
		case -22:
			throw new ExpiredAccessTokenException();
		case -25:			
			result.setCode("401");
			result.setMsg(resp.getReturnMessage());
			result.setResult(resp.getResult());
			return null;///result;
		default:
			//return null;
		}
		return null;
	}

	public Response updatePreferredLanguage(String accessToken, JSONObject jsonObj) throws ExpiredAccessTokenException {		
		RestTemplate template = new RestTemplate();
		Request request = new Request();
		request.setMethodName("updatePreferredLanguage");
		request.setAccessToken(accessToken);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("requestBody", jsonObj.toString());
		request.setParameters(parameters);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		if (resp.getReturnCode() == 0) {			
			return resp;
		} else if (resp.getReturnCode() == -2) {
			throw new ExpiredAccessTokenException();
		} else {
			return null;
		}				
	}
	
	public ArrayList<SalesReport> listReportSales(String aCCESS_TOKEN, HashMap<String, Object> filter,
			HttpServletRequest hsr, CustomerUser currU,String typeReport) throws IOException, ExpiredAccessTokenException {

		ArrayList<SalesReport> finalResult = new ArrayList<>();
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
		parm.put("typeReport", typeReport);
		parm.put("agentId",currU.getUserId());


		json.put("methodName", "salesReport");
		json.put("accessToken", aCCESS_TOKEN);
		json.put("parameters", parm);

		String getJSON = json.toString();
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(URL_REQUEST);
		StringEntity input = new StringEntity(getJSON, "utf-8");
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		JSONObject totalResult = new JSONObject(builder.toString());
		if (totalResult.getInt("returnCode") == -2 || totalResult.getInt("returnCode") == -22) {
			throw new ExpiredAccessTokenException();
		}
		addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT AFFILIATE DETAIL", hsr.getHeader("user-agent"),
				getClientIp(hsr), 0, "SUCCESS");
		try {
			JSONObject result = totalResult.getJSONObject("result");
			JSONArray arr = result.getJSONArray("report");

			for (int i = 0; i < arr.length(); i++) {
				SalesReport reportDetail = new SalesReport();
				reportDetail.setUserId(arr.getJSONObject(i).isNull("id_user") ? 0: arr.getJSONObject(i).getInt("id_user"));
				reportDetail.setUserAgentId(arr.getJSONObject(i).isNull("id_user_agent") ? 0: arr.getJSONObject(i).getInt("id_user_agent"));

				reportDetail.setAgentEmail(arr.getJSONObject(i).isNull("email_agent") ? "" : arr.getJSONObject(i).getString("email_agent"));
				reportDetail.setUserName(arr.getJSONObject(i).isNull("first_name") ? "" : arr.getJSONObject(i).getString("first_name"));
				reportDetail.setUserLastname(arr.getJSONObject(i).isNull("last_name") ? "" : arr.getJSONObject(i).getString("last_name"));
				reportDetail.setUserEmail(arr.getJSONObject(i).isNull("email") ? "N/A" : arr.getJSONObject(i).getString("email"));
				
				reportDetail.setCountryName(arr.getJSONObject(i).isNull("country_name") ? "" : arr.getJSONObject(i).getString("country_name"));
			
				reportDetail.setCountryCode(arr.getJSONObject(i).isNull("country_code") ? "" : arr.getJSONObject(i).getString("country_code"));

				reportDetail.setPhoneNumber(arr.getJSONObject(i).isNull("phone_number") ? "" : arr.getJSONObject(i).getString("phone_number"));
				reportDetail.setPreferredLanguage(arr.getJSONObject(i).isNull("preferred_language") ? "" : String.valueOf(arr.getJSONObject(i).getInt("preferred_language")));
				reportDetail.setDaysAssigned(arr.getJSONObject(i).isNull("days_assigned") ? 0: arr.getJSONObject(i).getInt("days_assigned"));
			
				reportDetail.setAgentAssigned(arr.getJSONObject(i).isNull("agent_assigned") ? "" : arr.getJSONObject(i).getString("agent_assigned"));
				reportDetail.setSales(arr.getJSONObject(i).isNull("sales") ? "" : arr.getJSONObject(i).getString("sales"));
				
				reportDetail.setSaleStatus(arr.getJSONObject(i).isNull("sales_status") ? "" : arr.getJSONObject(i).getString("sales_status"));
				
				reportDetail.setPreferredLanguage(reportDetail.getPreferredLanguage() != "" ? reportDetail.getPreferredLanguage().equals("1") ? "Spanish":"English":"");

				reportDetail.setLeadTypeId(arr.getJSONObject(i).isNull("lead_type_id") ? 0: arr.getJSONObject(i).getInt("lead_type_id"));
				
				reportDetail.setLeadType(arr.getJSONObject(i).isNull("lead_type") ? "":arr.getJSONObject(i).getString("lead_type"));


				if(!arr.getJSONObject(i).isNull("last_call")) {
					try {
						reportDetail.setDate(arr.getJSONObject(i).getLong("last_call"));
						long date = (long) reportDetail.getDate();
						Date dateObj = new Date(date);
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						DateFormat df2 = new SimpleDateFormat("HH:mm");
						reportDetail.setDateFormat(df.format(dateObj));
						reportDetail.setTimeFormat(df2.format(dateObj));

					} catch (Exception e) {
						logger.error("ERROR", e);
						reportDetail.setDateFormat(e.getMessage());
					}
					
					if("ASSIGNED".equals(reportDetail.getSaleStatus())) {
						reportDetail.setSales("PENDING");
					}
				}
				
				finalResult.add(reportDetail);
			}

			httpClient.close();
			return finalResult;
		} catch (Exception e) {
			logger.error("ERROR", e);
			SalesReport reportDetail = new SalesReport();
			finalResult.add(reportDetail);
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "LIST REPORT SALES", hsr.getHeader("user-agent"),
					getClientIp(hsr), 2, e.getMessage());
			httpClient.close();
			return finalResult;
		}
	}
	
	public Response updateStatuSales(CustomerUser currU, HttpServletRequest hsr, String str,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		JSONObject jsonObj = new JSONObject(str);
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("updateStatuSalesMethodImpl");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();

		parm.put("statusId", jsonObj.has("statusId") && !jsonObj.isNull("statusId") ?jsonObj.getInt("statusId"):-1);
		parm.put("agentId", jsonObj.has("agentId")  && !jsonObj.isNull("agentId") ? jsonObj.getLong("agentId"):0);
		parm.put("operatorId",currU.getUserId());
		parm.put("leadType", jsonObj.has("leadTypeId")  && !jsonObj.isNull("leadTypeId") ? jsonObj.getLong("leadTypeId"):0);
		
		ArrayList<UserAccess> listUsers = new ArrayList<>();
		JSONArray arr = jsonObj.getJSONArray("listUsers");

		for (int i = 0; i < arr.length(); i++) {
			UserAccess user = new UserAccess();
			user.setId(arr.getJSONObject(i).getInt("userId"));
			user.setEmail(arr.getJSONObject(i).getString("userEmail"));
			listUsers.add(user);
		}
		parm.put("listUsers", listUsers);

		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
		case -4: // The error general
		case -49: //Warning
			//Se guarda 
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "UPDATE STATUS SALES",
					hsr.getHeader("user-agent"), getClientIp(hsr), resp.getReturnCode(), resp.getReturnMessage());
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
	
	public Response addBusinessListUsersSupportHistory(CustomerUser currU, HttpServletRequest hsr, String str,String aCCESS_TOKEN) throws ExpiredAccessTokenException, UnknownCustomerUserException {
		RestTemplate template = new RestTemplate();
		JSONObject jsonObj = new JSONObject(str);
		Response response = new Response();
		Request request = new Request();
		request.setMethodName("addBusinessListUsersSupportHistory");
		request.setAccessToken(aCCESS_TOKEN);
		HashMap<String, Object> parm = new HashMap<>();

		parm.put("comment", jsonObj.has("comment")  && !jsonObj.isNull("comment") ? jsonObj.getString("comment"):"");
		parm.put("agentId",currU.getUserId());
		parm.put("leadType", jsonObj.has("leadTypeId")  && !jsonObj.isNull("leadTypeId") ? jsonObj.getLong("leadTypeId"):0);

		ArrayList<UserAccess> listUsers = new ArrayList<>();
		JSONArray arr = jsonObj.getJSONArray("listUsers");

		for (int i = 0; i < arr.length(); i++) {
			UserAccess user = new UserAccess();
			user.setId(arr.getJSONObject(i).getInt("userId"));
			user.setEmail(arr.getJSONObject(i).getString("userEmail"));
			listUsers.add(user);
		}
		parm.put("listUsers", listUsers);

		request.setParameters(parm);
		Response resp = template.postForObject(URL_REQUEST,request,Response.class,new HashMap<>());
		switch (resp.getReturnCode()) {
		case 0:
		case -4: // The error general
		case -49: //Warning
			//Se guarda 
			addAuditRecord(aCCESS_TOKEN, currU.getEmail(), "ADD BUSINESS LIST USERS SUPPORT HISTORY",
					hsr.getHeader("user-agent"), getClientIp(hsr), resp.getReturnCode(), resp.getReturnMessage());
			response.setReturnCode(resp.getReturnCode());
			response.setReturnMessage(resp.getReturnMessage());
			response.setResult(resp.getResult());
			return response;
		case -2:
		case -22:
			throw new ExpiredAccessTokenException();	
		default:
			return null;
		}
	}
}
