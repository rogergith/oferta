package com.weavx.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weavx.business.DonationDelegate;
import com.weavx.web.dto.FormMultiCustomerLoadDTO;
import com.weavx.web.dto.FormMultiCustomerRestrictDTO;
import com.weavx.web.model.*;
import com.weavx.web.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class AjaxController {
	Logger log = LoggerFactory.getLogger(AjaxController.class);

	@RequestMapping(value = "/search/api/selectkey")
	public AjaxResponseBody selectKey(@RequestBody String option, HttpSession session, HttpServletRequest request) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			session.invalidate();
			session = request.getSession();
			session.setMaxInactiveInterval(6*60*60); //6-horas
			session.setAttribute("option", Integer.parseInt(option));
			session.setAttribute("optionHistory", null);
			session.setAttribute("optionSelect", Integer.parseInt(option));
			session.setAttribute("optionSelectHistory", null);
			result.setCode("200");
			result.setMsg("login");
			return result;
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/selectkeyHistory")
	public AjaxResponseBody selectkeyHistory(@RequestBody String option, HttpSession session, HttpServletRequest request) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			session.invalidate();
			session = request.getSession();
			session.setMaxInactiveInterval(6*60*60); //6-horas
			session.setAttribute("optionHistory", Integer.parseInt(option));
			session.setAttribute("option", null);
			session.setAttribute("optionSelectHistory", Integer.parseInt(option));
			session.setAttribute("optionSelect", null);
			result.setCode("200");
			result.setMsg("login");
			return result;
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("");
			return result;
		}
	}

	@RequestMapping(value = "/search/api/selectkeyInside")
	public AjaxResponseBody selectKeyInside(@RequestBody String option, HttpSession session, HttpServletRequest request) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			if(session.getAttribute("optionSelect") != null) {
				session.invalidate();
				session = request.getSession();
				session.setAttribute("option", Integer.parseInt(option));
				session.setAttribute("optionSelect", Integer.parseInt(option));
			}else if(session.getAttribute("optionSelectHistory") != null){
				session.invalidate();
				session = request.getSession();
				session.setAttribute("optionHistory", Integer.parseInt(option));
				session.setAttribute("optionSelectHistory", Integer.parseInt(option));
			}
			session.setMaxInactiveInterval(6*60*60); //6-horas
			session.setAttribute("firstName", "");
			session.setAttribute("LastName", "");
			session.setAttribute("userToken", "");
			session.setAttribute("identityProviderId", "");
			session.setAttribute("accesstoken", "");
			result.setCode("200");
			result.setMsg("login");
			return result;
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("");
			return result;
		}
	}

	/*@RequestMapping(value = "/search/api/authenticate")
	public AjaxResponseBody authenticate(@RequestBody AuthenticateUser aUser, HttpServletRequest hsr, HttpSession session) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			CustomerUser user = null;
			if (isValidAuthenticateUser(aUser)) {
				DonationDelegate dd = new DonationDelegate();
				try {
					user = dd.authenticateUser(aUser.getUsername(), aUser.getPassword(), hsr,
							hsr.getHeader("user-agent"), ACCESS_TOKEN);
				} catch (ExpiredAccessTokenException ex) {
					log.error("ERROR", ex);
					refreshToken(session);
					ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
					user = dd.authenticateUser(aUser.getUsername(), aUser.getPassword(), hsr,
							hsr.getHeader("user-agent"), ACCESS_TOKEN);
				}
				if (user != null && user.getUserAccessToken().length() > 0) {
					result.setCode("200");
					result.setMsg("");
					result.setResult(user);
					session.setAttribute("firstName", user.getFirstName());
					session.setAttribute("LastName", user.getLastName());
					session.setAttribute("userToken", user.getUserAccessToken());
					session.setAttribute("identityProviderId", "0");
				} else {
					result.setCode("204");
					result.setMsg("No user!");
				}
			} else {
				result.setCode("400");
				result.setMsg("Debe colocar usuario y contraseña!");
			}
			return result;
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("401");
			result.setMsg(e.getMessage());
			return result;
		}
	}*/

	@RequestMapping(value = "/search/api/closeAuthenticate")
	public AjaxResponseBody closeAuthenticate(@RequestBody AuthenticateUser aUser, HttpSession session) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			session.invalidate();
			result.setCode("200");
			result.setMsg("");
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/verifyUser")
	public AjaxResponseBody verifyUser(@RequestBody String email, HttpSession session, HttpServletRequest request) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			boolean verify;
			try {
				verify = dd.verifyUser(email, ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				verify = dd.verifyUser(email, ACCESS_TOKEN);
			}
			if (verify) {
				result.setCode("204");
				result.setMsg("exist");
			} else {
				result.setCode("200");
				result.setMsg("not exist");
			}
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/verifyUser2")
	public AjaxResponseBodyHash2 verifyUser2(@RequestBody String email, HttpSession session, HttpServletRequest request) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			HashMap<String, Object> verify;	
			email = email.replace("\"", "");
			verify = dd.verifyUser2(email, ACCESS_TOKEN);
			if (verify==null) {
				result.setCode("401");
				result.setMsg("not exist");
				result.setResult(verify);
			} else {
				result.setCode("200");
				result.setMsg("exist");
				result.setResult(verify);				
			}
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/verifyUserForAddAttendee")
	public AjaxResponseBodyHash2 verifyUserAddAttendee(@RequestBody String email, HttpSession session, HttpServletRequest request) {
		AjaxResponseBodyHash2 result = new AjaxResponseBodyHash2();
		try {
			String ACCESS_TOKEN = null;
			String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
			
			if(accesstokenForLifeCycle != null) {
				ACCESS_TOKEN = accesstokenForLifeCycle;
			}else {
				refreshToken(session);
				ACCESS_TOKEN = validateAndRenewToken(session);
			}
			
			DonationDelegate dd = new DonationDelegate();
			email = email.replace("\"", "");
			result = dd.verifyUserAddAttendee(email.trim(), ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
			return result;
		}
	}

	@RequestMapping(value = "/search/api/authenticateExternal")
	public AjaxResponseBody authenticateExternal(@RequestBody AuthenticateExternalUser aUser, HttpSession session, HttpServletRequest request) {
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			CustomerUser user = null;
			if (isValidAuthenticateUser(aUser)) {
				DonationDelegate dd = new DonationDelegate();
				try {
					user = dd.authenticateExternalUser(ACCESS_TOKEN, aUser);
				} catch (ExpiredAccessTokenException ex) {
					log.error("ERROR", ex);
					refreshToken(session);
					ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
					user = dd.authenticateExternalUser(ACCESS_TOKEN, aUser);
				}
				if (user != null && user.getUserAccessToken().length() > 0) {
					result.setCode("200");
					result.setMsg("");
					result.setResult(user);
					session.setAttribute("firstName", user.getFirstName());
					session.setAttribute("LastName", user.getLastName());
					session.setAttribute("userToken", user.getUserAccessToken());
					session.setAttribute("identityProviderId", String.valueOf(aUser.getIdentityProviderId()));
				} else {
					result.setCode("204");
					result.setMsg("No user!");
				}
			} else {
				result.setCode("400");
				result.setMsg("Debe colocar usuario y contraseña!");
			}
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;

	}

	private boolean isValidAuthenticateUser(AuthenticateUser aUser) {

		boolean valid = true;
		if (aUser == null) {
			valid = false;
		}
		if ((StringUtils.isEmpty(aUser.getUsername())) || (StringUtils.isEmpty(aUser.getPassword()))) {
			valid = false;
		}
		return valid;
	}

	private boolean isValidAuthenticateUser(AuthenticateExternalUser aUser) {
		boolean valid = true;
		if (aUser == null) {
			valid = false;
		}
		if ((aUser.getIdentityProviderId() == -1) || (StringUtils.isEmpty(aUser.getIdentityProviderId()))
				|| (StringUtils.isEmpty(aUser.getAccessTokenExternal()))) {
			valid = false;
		}
		return valid;
	}

	@RequestMapping(value = "/search/api/creditCardPurchase", produces = "application/json; charset=UTF-8")
	private AjaxResponseBody creditCardPurchase(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray arr = jsonObj.getJSONArray("txDetail");
			ArrayList<TxDetail> txDetailtmp = new ArrayList<>();
			for (int i = 0; i < arr.length(); i++) {
				TxDetail txDetail = new TxDetail();
				txDetail.setFundId(arr.getJSONObject(i).getInt("typeDonation"));
				txDetail.setAmount(arr.getJSONObject(i).getInt("amount"));
				txDetailtmp.add(txDetail);
			}

			TxDetailList txDetailList = new TxDetailList();
			txDetailList.setTxDetails(txDetailtmp);
			JSONObject jsonUserData = jsonObj.getJSONObject("userData");
			TxUserData txUserData = new TxUserData();

			txUserData.setName(jsonUserData.getString("name"));
			txUserData.setLastname(jsonUserData.getString("lastname"));
			txUserData.setCountry(jsonUserData.getInt("country"));
			txUserData.setStateText(jsonUserData.getString("state"));
			txUserData.setCityText(jsonUserData.getString("city"));
			txUserData.setAddress(jsonUserData.getString("address"));
			txUserData.setPostcode(jsonUserData.getString("postcode"));
			txUserData.setEmail(jsonUserData.getString("email"));

			JSONObject jsonPaymentGwParameters = jsonObj.getJSONObject("paymentGwParameters");
			PaymentGwParameters paymentGwParameters = new PaymentGwParameters();

			if (jsonPaymentGwParameters.isNull("token")) {
				paymentGwParameters.setCreditCardNumber(jsonPaymentGwParameters.getString("creditcardnumber"));
				paymentGwParameters.setCreditCardExpiration(jsonPaymentGwParameters.getString("creditcardexpiration"));
				paymentGwParameters.setCreditCardCode(jsonPaymentGwParameters.getString("creditcardcode"));
				paymentGwParameters.setOrderDescription(jsonPaymentGwParameters.getString("orderdescription"));
			} else {
				paymentGwParameters.setToken(jsonPaymentGwParameters.getString("token"));
				paymentGwParameters.setOrderDescription(jsonPaymentGwParameters.getString("orderdescription"));
			}

			DonationDelegate dd = new DonationDelegate();
			AuthorizationInfo authorizationInfo;
			try {
				authorizationInfo = dd.creditCardPurchase(ACCESS_TOKEN, txDetailList, txUserData, paymentGwParameters,
						hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				authorizationInfo = dd.creditCardPurchase(ACCESS_TOKEN, txDetailList, txUserData, paymentGwParameters,
						hsr, currentUser);
			}

			if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("0")) {
				result.setCode("200");
				result.setMsg(authorizationInfo.toString());
			} else if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("-20")) {
				result.setCode("203");
				result.setMsg(authorizationInfo.getMessageResult());
			} else {
				result.setCode("204");
				result.setMsg(authorizationInfo.getMessageResult());
			}

		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/freePurchase", produces = "application/json; charset=UTF-8")
	private AjaxResponseBody freePurchase(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = null;
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		
		if(accesstokenForLifeCycle != null) {
			ACCESS_TOKEN = accesstokenForLifeCycle;
		}else {
			refreshToken(session);
			ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		}	
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		AjaxResponseBody result = new AjaxResponseBody();
		String ipAddress = getClientIp(hsr);
		if (!currentUser.userHasAccess(CustomerUser.COMPLIMENTARY)) {
			result.setCode("403");
			result.setMsg("Unauthorize action for this user: Complimentary");
		}
		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray arr = jsonObj.getJSONArray("txDetail");
			ArrayList<TxDetail> txDetailtmp = new ArrayList<>();

			if (!jsonObj.isNull("fundId")) {
				JSONObject amountByFund = (JSONObject) session.getAttribute("amountByFund");
				if (amountByFund.length() > 0) {
					TxDetail txDetail = new TxDetail();
					txDetail.setFundId(Integer.valueOf(jsonObj.getString("fundId")));
					txDetail.setAmount(amountByFund.getDouble(String.valueOf(txDetail.getFundId())));
					txDetailtmp.add(txDetail);
				}
			} else {
				for (int i = 0; i < arr.length(); i++) {
					TxDetail txDetail = new TxDetail();
					txDetail.setFundId(arr.getJSONObject(i).getInt("typeDonation"));
					JSONObject amountByFund = (JSONObject) session.getAttribute("amountByFund");
					if (amountByFund.length() > 0) {
						txDetail.setAmount(amountByFund.getDouble(String.valueOf(txDetail.getFundId())));
					} else {
						if ((int) session.getAttribute("amountValid") == arr.getJSONObject(i).getInt("amount")) {
							txDetail.setAmount(arr.getJSONObject(i).getInt("amount"));
						} else {
							txDetail.setAmount((int) session.getAttribute("amountValid"));
						}
					}
					txDetailtmp.add(txDetail);
				}
			}
			TxDetailList txDetailList = new TxDetailList();
			txDetailList.setTxDetails(txDetailtmp);
			JSONObject jsonUserData = jsonObj.getJSONObject("userData");
			TxUserData txUserData = new TxUserData();

			txUserData.setName(jsonUserData.getString("name"));
			txUserData.setLastname(jsonUserData.getString("lastname"));
			txUserData.setCountry(jsonObj.getInt("country"));
			txUserData.setSourceCode( jsonObj.isNull("source_code") ? 0 : jsonObj.getInt("source_code"));

			
			txUserData.setEmail(jsonUserData.getString("email"));
			txUserData.setPhone(jsonUserData.getString("phone"));

			String source = (String) jsonObj.getString("source") != null ? jsonObj.getString("source") : null;
			
			{//Inicio de verifycacion de usuario para saber si puede o no comprar desde el admin una clase.
				RequestWS requestWS = new RequestWS();
				HashMap<String, Object> verifyUser3 = requestWS.verifyUser3(txUserData.getEmail(), ipAddress, (String) session.getAttribute("accesstoken"));
				Response responseVerifyUser = verifyUser3 != null ? (Response) verifyUser3.get("response") : null; 
				if(responseVerifyUser != null && responseVerifyUser.getReturnCode() == -60){
					result.setCode("-60");
					result.setMsg(responseVerifyUser.getReturnMessage());
					return result;
				}
			}//fin verifyUser
			//Desde el modulo de registr de asistentes no se envia el UAT cuando la compra es gratis por cupon 100%
			String paramUA = "";//jsonObj.getString("paramUA") != null ? jsonObj.getString("paramUA") : null ;

			int langId = jsonUserData.getInt("langid");

			String coupon = jsonObj.getString("coupon");
			String comments = jsonObj.getString("comments");
			Long startDate = null;
			boolean checkStartDate = false;
			try {
				if(jsonObj.getBoolean("checkStartDate")) {
					startDate = jsonObj.getLong("startDate");
					checkStartDate = jsonObj.getBoolean("checkStartDate");
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			DonationDelegate dd = new DonationDelegate();
			HashMap<String, Object> authorizationInfo;
			try {
				authorizationInfo = dd.freePurchase(ACCESS_TOKEN, txDetailList, txUserData, source, paramUA, langId,
						coupon, hsr, currentUser,comments,startDate,checkStartDate);
				//session.setAttribute("accesstokenForLifeCycle", null);
			} catch (ExpiredAccessTokenException ex) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				authorizationInfo = dd.freePurchase(ACCESS_TOKEN, txDetailList, txUserData, source, paramUA, langId,
						coupon, hsr, currentUser, comments,startDate,checkStartDate);
			}

			if (authorizationInfo != null && (int) authorizationInfo.get("result") == 1) {
				result.setCode("200");
				result.setMsg((String) authorizationInfo.get("resultMessage"));
				HashMap<String, Object> txIdHashMap = (HashMap<String, Object>) authorizationInfo.get("authorizationInfo");
				result.setTxId( (String) txIdHashMap.get("tx_id"));
			} else if (authorizationInfo != null && ((String) authorizationInfo.get("returnCode")).equals("-20")) {
				result.setCode("203");
				result.setMsg((String) authorizationInfo.get("returnMessage"));
			} else if (authorizationInfo != null && ((String) authorizationInfo.get("returnCode")).equals("-34")) {
				result.setCode("206");
				result.setMsg("Ya posee una compra registrada para este evento, lo invitamos a autenticarse.");
			} else {
				result.setCode("204");
				result.setMsg((String) authorizationInfo.get("returnMessage"));
			}

		} catch (Exception e) {
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/externalPurchase", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	private AjaxResponseBody externalPurchase(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = null;
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		
		if(accesstokenForLifeCycle != null) {
			ACCESS_TOKEN = accesstokenForLifeCycle;
		}else {
			refreshToken(session);
			ACCESS_TOKEN = validateAndRenewToken(session);
		}
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			
			//Long langId = ((Number) params.get("lang_id")).longValue(); langId = (langId == null)?1:langId;
			
			String ipAddress = "10.0.0.1";//getClientIp(hsr);
			JSONObject jsonObj = new JSONObject(str);
			TxDetail txDetail = new TxDetail();
			txDetail.setFundId(Integer.parseInt(jsonObj.getString("fund")));
			txDetail.setAmount(Double.parseDouble(jsonObj.getString("amount")));

			TxUserData txUserData = new TxUserData();
			txUserData.setName(jsonObj.getString("name"));
			txUserData.setLastname(jsonObj.getString("lastname"));
			txUserData.setEmail(jsonObj.getString("email"));
			txUserData.setPhone(jsonObj.getString("phone"));
			txUserData.setCountry(jsonObj.getInt("country"));
			txUserData.setSourceCode( jsonObj.isNull("source_code") ? 0 : jsonObj.getInt("source_code"));
			//int langId = jsonObj.getInt("langid");
			
			int langId = jsonObj.isNull("langid") ? 1 : jsonObj.getInt("langid");
			
			if(txUserData.getSourceCode() == 1010 || txUserData.getSourceCode() == 1020){
				result.setCode("400");
				result.setMsg("You must select a sourceCode");
				return result;
			}
			
			{//Inicio de verifycacion de usuario para saber si puede o no comprar desde el admin una clase.
				RequestWS requestWS = new RequestWS();
				HashMap<String, Object> verifyUser3 = requestWS.verifyUser3(txUserData.getEmail(), ipAddress, ACCESS_TOKEN);
				Response responseVerifyUser = verifyUser3 != null ? (Response) verifyUser3.get("response") : null; 
				if(responseVerifyUser != null && responseVerifyUser.getReturnCode() == -60){
					result.setCode("-60");
					result.setMsg(responseVerifyUser.getReturnMessage());
					return result;
				}
			}//fin verifyUser
			
			TxExternalPayment txPaymentData = new TxExternalPayment(jsonObj.getInt("paymentType"),
					jsonObj.getString("concept"));

			String medium = jsonObj.getString("medium");
			medium = session.getAttribute("firstName") + " " + session.getAttribute("LastName");
			String source = jsonObj.getString("source");
			source = "SeminarioAdmin";
			String coupon = jsonObj.getString("coupon");
			String comments = jsonObj.getString("comments");
			Long startDate = null;
			boolean checkStartDate = false;
			try {
				if(jsonObj.getBoolean("checkStartDate")) {
					startDate = jsonObj.getLong("startDate");
					checkStartDate = jsonObj.getBoolean("checkStartDate");
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			int operatorId = currentUser.getId();

			//int lang = 1;

			DonationDelegate dd = new DonationDelegate();

			try {
				result = dd.externalPurchase(ACCESS_TOKEN, txDetail, txUserData, medium, source, langId, txPaymentData,
						coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
				//session.setAttribute("accesstokenForLifeCycle", null);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result = dd.externalPurchase(ACCESS_TOKEN, txDetail, txUserData, medium, source, langId, txPaymentData,
						coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@RequestMapping(value = "/search/api/createReport", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<ReportDetail> reportDetail(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<ReportDetail> result = new AjaxResponseReport<ReportDetail>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("operator_email")||keyM.toString().equals("person_email")||keyM.toString().equals("affiliate_email")) {
						filter.put(keyM, valueM.toString().trim());
					}else {
						filter.put(keyM, valueM);
					}
				}
			}
			DonationDelegate dd = new DonationDelegate();
			ArrayList<ReportDetail> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			try {
				reportDetailList = dd.reportDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = dd.reportDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/assistedCCPurchase", produces = "application/json; charset=UTF-8")
	private AjaxResponseBody assistedCCPurchase(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = null;
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		
		if(accesstokenForLifeCycle != null) {
			ACCESS_TOKEN = accesstokenForLifeCycle;
		}else {
			refreshToken(session);
			ACCESS_TOKEN = validateAndRenewToken(session);
		}
		
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			String ipAddress = "10.0.0.1";//getClientIp(hsr);
			JSONObject jsonObj = new JSONObject(str);
			TxDetail txDetail = new TxDetail();
			
			Long startDate = null;
			boolean checkStartDate = false;
			try {
				if(jsonObj.getBoolean("checkStartDate")) {
					startDate = jsonObj.getLong("startDate");
					checkStartDate = jsonObj.getBoolean("checkStartDate");
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			txDetail.setFundId(Integer.parseInt(jsonObj.getString("fund")));
			txDetail.setAmount(Double.parseDouble(jsonObj.getString("amount")));

			TxUserData txUserData = new TxUserData();
			txUserData.setName(jsonObj.getString("name"));
			txUserData.setLastname(jsonObj.getString("lastname"));
			txUserData.setEmail(jsonObj.getString("email"));
			txUserData.setPhone(jsonObj.getString("phone"));
			txUserData.setCountry(jsonObj.getInt("country"));
			txUserData.setSourceCode( jsonObj.isNull("source_code") ? 0 : jsonObj.getInt("source_code"));
			
			if(txUserData.getSourceCode() == 1010 || txUserData.getSourceCode() == 1020){
				result.setCode("400");
				result.setMsg("You must select a sourceCode");
				return result;
			}
			
			{//Inicio de verifycacion de usuario para saber si puede o no comprar desde el admin una clase.
				RequestWS requestWS = new RequestWS();
				HashMap<String, Object> verifyUser3 = requestWS.verifyUser3(txUserData.getEmail(), ipAddress, ACCESS_TOKEN);
				Response responseVerifyUser = verifyUser3 != null ? (Response) verifyUser3.get("response") : null; 
				if(responseVerifyUser != null && responseVerifyUser.getReturnCode() == -60){
					result.setCode("400");
					result.setMsg(responseVerifyUser.getReturnMessage());
					return result;
				}
			}//fin verifyUser
			
			
			PaymentGwParameters paymentGwParameters = new PaymentGwParameters();
			
			// por ahora desde el admin no se guardaran tarjetas.
			paymentGwParameters.setSavePaymentData(jsonObj.getBoolean("savePaymentData"));
			paymentGwParameters.setOrderDescription(jsonObj.getString("orderDescription"));
			//Si el token no viene vacio se agrega en paymentGwParameters
			if(jsonObj.getString("token").length() > 5){
				paymentGwParameters.setToken(jsonObj.getString("token"));
			//Se lo contrario se agrega el cardId, es decir, la compra se hara con una card previamente guardada.
			}else {
				paymentGwParameters.setCardId(jsonObj.getString("card"));
				
			}

			String medium = jsonObj.getString("medium");
			medium = (String) session.getAttribute("firstName") + " " + session.getAttribute("LastName");
			String source = jsonObj.getString("source");
			source = "SeminarioAdmin";
			String coupon = jsonObj.getString("coupon");
			String comments = jsonObj.getString("comments");

			int paymentGatewayId = jsonObj.getInt("paymentGatewayId");
			int lang = ((Number) jsonObj.getInt("langid")) != null ? ((Number) jsonObj.getInt("langid")).intValue() : 1;

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			int operatorId = 103;

			DonationDelegate dd = new DonationDelegate();
			AuthorizationInfo authorizationInfo;
			try {
				authorizationInfo = dd.assistedCCPurchase(ACCESS_TOKEN, txDetail, txUserData, paymentGwParameters,
						paymentGatewayId, medium, source, lang, coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
				//session.setAttribute("accesstokenForLifeCycle", null);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				authorizationInfo = dd.assistedCCPurchase(ACCESS_TOKEN, txDetail, txUserData, paymentGwParameters,
						paymentGatewayId, medium, source, lang, coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
			}

			if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("0")) {
				result.setCode("200");
				result.setMsg(authorizationInfo.toString());
				result.setTxId(authorizationInfo.getTxId());
			} else if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("-20")) {
				result.setCode("203");
				result.setMsg(authorizationInfo.getMessageResult());
			} else {
				result.setCode("204");
				result.setMsg(authorizationInfo.getMessageResult());
			}

		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/reportFinances")
	private AjaxResponseBodyHash reportFinances(@RequestBody String str, HttpSession session, HttpServletRequest hsr)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseBodyHash result = new AjaxResponseBodyHash();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					filter.put(keyM, valueM);
				}
			}
			DonationDelegate dd = new DonationDelegate();
			ArrayList<HashMap<String, Object>> reportDetailList = new ArrayList<>();

			try {
				reportDetailList = dd.customerFinancesReport(ACCESS_TOKEN, filter);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = dd.customerFinancesReport(ACCESS_TOKEN, filter);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	private void refreshToken(HttpSession session) {
		try {
			DonationDelegate dd = new DonationDelegate();
			int option = (int) session.getAttribute("option");
			String newAT = dd.requestAccessToken(option);
			session.setAttribute("accesstoken", newAT);
		} catch (Exception e) {
			log.error("ERROR", e);
		}
	}

	@RequestMapping(value = "/search/api/authenticateAdmin")
	public AjaxResponseBody authenticateAdmin(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {						
			CustomerUser user = null;
			AuthenticateUser aUser = new AuthenticateUser();
			JSONObject jsonObj = new JSONObject(str);
			session.setAttribute("option", jsonObj.getInt("option"));
			refreshToken(session);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			aUser.setUsername(jsonObj.getString("user"));
			aUser.setPassword(jsonObj.getString("passw"));
			aUser.setAccessToken(ACCESS_TOKEN);

			if (isValidAuthenticateUser(aUser)) {
				DonationDelegate dd = new DonationDelegate();
				try {
					user = dd.authenticateUser(aUser.getUsername(), aUser.getPassword(), hsr,
							hsr.getHeader("user-agent"), ACCESS_TOKEN);
				} catch (ExpiredAccessTokenException ex) {
					log.error("ERROR", ex);
					refreshToken(session);
					ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
					user = dd.authenticateUser(aUser.getUsername(), aUser.getPassword(), hsr,
							hsr.getHeader("user-agent"), ACCESS_TOKEN);
				} catch (WrongAuthenticationException ex) {
					log.error("ERROR", ex);
					result.setCode("201");
					result.setMsg("An error occurred, please try again later.");
					return result;
				}
				if (user != null && user.getFirstName().length() > 0 && user.userHasAccess(CustomerUser.LOGIN)) {
					result.setCode("200");
					result.setMsg("");
					result.setResult(user);
					session.setAttribute("firstName", user.getFirstName());
					session.setAttribute("LastName", user.getLastName());
					session.setAttribute("user-access-token", user.getUserAccessToken());
					session.setAttribute("userData", user);
					// session.setAttribute("userToken",user.getUserAccessToken());
					session.setAttribute("identityProviderId", "0");
				} else {
					
					if(!user.userHasAccess(CustomerUser.LOGIN)) {
						result.setCode("403");
						result.setMsg("You don't have permission to access to this admin panel!");
					} else {
						result.setCode("-4");
						result.setMsg("General error");
					}
					
				}
			} else {
				result.setCode("400");
				result.setMsg("Debe colocar usuario y contraseña!");
			}
			return result;
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("401");
			result.setMsg("An error occurred, please try again later.");
			return result;
		}
	}

	@RequestMapping(value = "/search/api/closeAuthenticateAdmin")
	public AjaxResponseBody closeAuthenticateAdmin(@RequestBody AuthenticateUser aUser, HttpSession session) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			session.invalidate();
			result.setCode("200");
			result.setMsg("");
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/dashboard")
	public AjaxResponseBody dashboard(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			refreshToken(session);
			JSONObject jsonObj = new JSONObject(str);
			if (jsonObj.has("time")) {
				String time = jsonObj.getString("time");
				String period = time.substring(time.length() - 1, time.length());
				String number = time.substring(0, time.length() - 1);
				int range = Integer.parseInt(number);
				DateFormat df = new SimpleDateFormat("MM/dd/yy");
				Calendar calrep = Calendar.getInstance();
				String showing = "Showing statistics from";

				switch (period) {
					case "T":
						df = new SimpleDateFormat("HH:mm");

						calrep.add(Calendar.DAY_OF_MONTH, 0);
						calrep.set(Calendar.HOUR_OF_DAY, 0);
						calrep.set(Calendar.MINUTE, 0);
						calrep.set(Calendar.SECOND, 0);
						calrep.set(Calendar.MILLISECOND, 0);
						break;

					case "H":
						calrep.add(Calendar.HOUR_OF_DAY, range * -1);
						break;

					case "D":
						calrep.add(Calendar.DAY_OF_MONTH, range * -1);
						break;

					case "M":
						calrep.add(Calendar.MONTH, range * -1);
						if (!time.startsWith("1")) {
							period = "3";
						}
						break;

					case "W":
						calrep.add(Calendar.DAY_OF_MONTH, range * -7);
						break;
				}

				long repDate = calrep.getTimeInMillis();
				Calendar calnow = Calendar.getInstance();
				calnow.setTime(new Date());
				long nowDate = calnow.getTimeInMillis();
				RequestWS rws = new RequestWS();
				JSONObject dataDashboard = rws.dashboard(8, 1, repDate, nowDate, period);
				dataDashboard.put("datefrom", df.format(calrep.getTime()));
				dataDashboard.put("dateto", df.format(calnow.getTime()));
				dataDashboard.put("showing", showing);

				result.setCode("200");
				result.setMsg(dataDashboard.toString());
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/validateCoupon")
	public Response validateCoupon(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		Response coupon = new Response();

		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.DISCOUNT)) {

			coupon.setReturnCode(403);
			coupon.setReturnMessage("Unauthorize action for this user: DISCOUNTS");
		}
		try {
			String ACCESS_TOKEN = null;
			String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
			
			if(accesstokenForLifeCycle != null) {
				ACCESS_TOKEN = accesstokenForLifeCycle;
			}else {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			DonationDelegate dd = new DonationDelegate();

			JSONObject jsonObj = new JSONObject(str);

			String code = jsonObj.getString("couponCode");
			int purchaseAmount = jsonObj.getInt("amount");
			String applier = jsonObj.getString("email");
			boolean activateCoupon = true;
			String userAgent = hsr.getHeader("User-Agent");
			String ipAddress = hsr.getRemoteAddr();

			coupon = dd.validateCoupon(ACCESS_TOKEN, code, purchaseAmount, applier, activateCoupon, userAgent,
					ipAddress, hsr, currentUser);

		} catch (Exception e) {

			log.error("ERROR", e);
		}

		return coupon;
	}

	@RequestMapping(value = "/search/api/registerCustomersList", method = RequestMethod.POST, consumes = "multipart/form-data")
	public Response registerCustomersList(@ModelAttribute FormMultiCustomerLoadDTO form, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		MultipartFile customersList = form.getCustomers();
		DonationDelegate dd = new DonationDelegate();
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");		
		if(!currentUser.userHasAccess(CustomerUser.ADD_ATTENDEEs)) {
			result.setReturnCode(403);
			result.setReturnMessage("Unauthorize action for this user: Massive register customers");
		}
		ArrayList<CsvFileModel> data = new ArrayList<>();
		try {
			data=CsvUtil.prepareCsvParsing(customersList);
			result = dd.registerCustomersList(form, data, (String) session.getAttribute("accesstoken"), hsr, (CustomerUser) session.getAttribute("userData"));
		} catch (Exception e1) {
			result.setReturnCode(-50);
			result.setReturnMessage(e1.getMessage());
			return result;
		}
		int resultCode = result.getReturnCode();
		if(resultCode == -22 || resultCode == -2 ) {
			refreshToken(session);
			try {			
				result = dd.registerCustomersList(form, data, (String) session.getAttribute("accesstoken"), hsr, (CustomerUser) session.getAttribute("userData"));
			} catch (Exception e) {
				log.error("ERROR: ", e);
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/search/api/addUsersByCsvFile", method = RequestMethod.POST, consumes = "multipart/form-data")
	public Response addUsersByCsvFileMethodImpl(@ModelAttribute FormMultiCustomerLoadDTO form, HttpServletRequest hsr, HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		MultipartFile customersList = form.getCustomers();
		DonationDelegate dd = new DonationDelegate();
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");		
		if(!currentUser.userHasAccess(CustomerUser.ADD_ATTENDEEs)) {
			result.setReturnCode(403);
			result.setReturnMessage("Unauthorize action for this user: Massive register customers");
		}
		ArrayList<CsvFileAddUsersModel> data = new ArrayList<>();
		try {
			data=CsvUtil.prepareCsvParsingAddsUsers(customersList);
			rws.addUsersByCsvFile(form, data, (String) session.getAttribute("accesstoken"), hsr, (CustomerUser) session.getAttribute("userData"));
			
		} catch (Exception e1) {
			result.setReturnCode(-50);
			result.setReturnMessage(e1.getMessage());
			return result;
		}
		int resultCode = result.getReturnCode();
		if(resultCode == -22 || resultCode == -2 ) {
			refreshToken(session);
			try {			
				result = rws.addUsersByCsvFile(form, data, (String) session.getAttribute("accesstoken"), hsr, (CustomerUser) session.getAttribute("userData"));
			} catch (Exception e) {
				log.error("ERROR: ", e);
			}
		}
		return result;
	}
	

	@RequestMapping(value = "/search/api/registerRestrictEvent", method = RequestMethod.POST, consumes = "multipart/form-data")
	public AjaxResponseBody registerRestrictEvent(@ModelAttribute FormMultiCustomerRestrictDTO form,
			HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		MultipartFile customersList = form.getCustomers();
		ArrayList<CsvModelRestrict> data = new ArrayList<>();
		DonationDelegate dd = new DonationDelegate();
		AjaxResponseBody result = new AjaxResponseBody();
		String ACCESS_TOKEN = validateAndRenewToken(session);

		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.RESTRICT_EVENT)) {
			result.setCode("403");
			result.setMsg("Unauthorize action for this user: Restrict Event");
		}

		try {
			data = CsvUtil.prepareCsvParsingRestrict(customersList);
			result = dd.registerRestrictEvent(data, ACCESS_TOKEN, hsr, currentUser);
		} catch (Exception e) {
			log.error("ERROR: ", e);
		}

		String resultCode = result.getCode();
		if (resultCode.equals("-22") || resultCode.equals("-2")) {

			refreshToken(session);
			ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				data = CsvUtil.prepareCsvParsingRestrict(customersList);
				result = dd.registerRestrictEvent(data, ACCESS_TOKEN, hsr, currentUser);
			} catch (Exception e) {
				log.error("ERROR: ", e);
			}

		}

		return result;
	}

	@RequestMapping(value = "/search/api/editRestrictedUser")
	private Response editRestrictedUser(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		try {
			String ACCESS_TOKEN = validateAndRenewToken(session);
			DonationDelegate dd = new DonationDelegate();
			JSONObject jsonObj = new JSONObject(str);
			RestrictedUser user = new RestrictedUser(jsonObj.getInt("id"), jsonObj.getString("name"),
					jsonObj.getString("lastName"), jsonObj.getString("email"), jsonObj.getBoolean("active"));
			Response result = dd.editRestrictedUser(ACCESS_TOKEN, user, hsr, currentUser);

			return result;

		} catch (Exception e) {

			refreshToken(session);

			try {

				String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				DonationDelegate dd = new DonationDelegate();
				JSONObject jsonObj = new JSONObject(str);
				RestrictedUser user = new RestrictedUser(jsonObj.getInt("id"), jsonObj.getString("name"),
						jsonObj.getString("lastName"), jsonObj.getString("email"));
				Response result = dd.editRestrictedUser(ACCESS_TOKEN, user, hsr, currentUser);
				return result;
			} catch (Exception error) {

				Response result = new Response();
				result.setReturnCode(500);
				result.setReturnMessage("An error occurred, please try again later.");
				return result;

			}

		}

	}

	@RequestMapping(value = "/search/api/deleteRestrictedUser")
	private Response deleteRestrictedUser(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		try {
			String ACCESS_TOKEN = validateAndRenewToken(session);
			DonationDelegate dd = new DonationDelegate();
			JSONObject jsonObj = new JSONObject(str);
			Integer userId = jsonObj.getInt("id");
			Response result = dd.deleteRestrictedUser(ACCESS_TOKEN, userId, hsr, currentUser);

			return result;

		} catch (Exception e) {

			refreshToken(session);

			try {
				String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				DonationDelegate dd = new DonationDelegate();
				JSONObject jsonObj = new JSONObject(str);
				Integer userId = jsonObj.getInt("id");
				Response result = dd.deleteRestrictedUser(ACCESS_TOKEN, userId, hsr, currentUser);

				return result;
			} catch (Exception error) {
				Response result = new Response();
				result.setReturnCode(500);
				result.setReturnMessage("An error occurred, please try again later.");
				return result;
			}

		}

	}

	@RequestMapping(value = "/search/api/getRestrictedUsers")
	private ResponseUsers getRestrictedUsers(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		DonationDelegate dd = new DonationDelegate();
		ResponseUsers resp = new ResponseUsers();
		try {
			ArrayList<RestrictedUser> response = dd.getRestrictedUsers(ACCESS_TOKEN, hsr, currentUser);
			resp.setResult(response);
			resp.setReturnCode(0);
			resp.setReturnMessage("Success");
		} catch (Exception e) {

			resp.setReturnCode(500);
			resp.setReturnMessage("An error occurred, please try again later.");
		}

		return resp;
	}

	@RequestMapping(value = "/search/api/getEventSettings")
	private ResponseEntity<Response> getEventSettings(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) {
		refreshToken(session);
		Response response = null;

		response = actionEventSettings(str, session, ActionEvent.GET, hsr);

		return validateResponse(response);
	}

	@RequestMapping(value = "/search/api/updateEventSettings")
	private Response updateEventSettings(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		Response response = null;

		response = actionEventSettings(str, session, ActionEvent.UPDATE, hsr);

		return response;
	}

	@RequestMapping(value = "/search/api/removeEventSettings")
	private Response removeEventSettings(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		refreshToken(session);
		Response response = null;

		response = actionEventSettings(str, session, ActionEvent.DELETE, hsr);

		return response;

	}

	@RequestMapping(value = "/search/api/addEventSettings")
	private ResponseEntity<Response> addEventSettings(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) {
		refreshToken(session);
		Response response = null;

		response = actionEventSettings(str, session, ActionEvent.ADD, hsr);

		return validateResponse(response);

	}

	private Response actionEventSettings(String requestBody, HttpSession session, ActionEvent actionEvent,
			HttpServletRequest hsr) {
		refreshToken(session);
		EventFundSettings eventFundSettings;
		Response response = null;

		String accesToken;
		accesToken = validateAndRenewToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		DonationDelegate dd = new DonationDelegate();

		ObjectMapper mapper = new ObjectMapper();

		try {
			eventFundSettings = mapper.readValue(requestBody, EventFundSettings.class);

			switch (actionEvent) {

				case GET:
					response = dd.getEventSettings(accesToken, eventFundSettings, hsr, currentUser);
					break;

				case ADD:
					response = dd.addEventSettings(accesToken, eventFundSettings, hsr, currentUser);
					break;

				case UPDATE:
					response = dd.updateEventSettings(accesToken, eventFundSettings, hsr, currentUser);
					break;

				case DELETE:
					response = dd.removeEventSettings(accesToken, eventFundSettings, hsr, currentUser);
					break;
			}

		} catch (Exception e) {
			log.error("ERROR: {}", e);
		}

		return response;

	}

	private ResponseEntity<Response> validateResponse(Response response) {

		switch (response.getReturnCode()) {
			case 0:
				return new ResponseEntity<>(response, HttpStatus.OK);
			case -4:
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			case -2:
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			case -22:
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
			case 49:
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			default:
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}

	}

	private String validateAndRenewToken(HttpSession session) {

		refreshToken(session);
		String accessToken = (String) session.getAttribute("accesstoken");

		return accessToken;

	}

	@RequestMapping(value = "/search/api/userSearch")
	private AjaxResponseReport<CustomerUser> userSearch(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<CustomerUser> result = new AjaxResponseReport<CustomerUser>();

		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.USERS_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}

		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("email")) {
						filter.put(keyM, valueM.toString().trim());
					}else {
						filter.put(keyM, valueM);
					}
				}
			}
			DonationDelegate dd = new DonationDelegate();
			ArrayList<CustomerUser> reportDetailList = new ArrayList<>();

			try {
				reportDetailList = dd.searchUsers(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = dd.searchUsers(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	
	@RequestMapping(value = "/search/api/userSearchAllEvent")
	private AjaxResponseReport<CustomerUser> userSearchAllEvent(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<CustomerUser> result = new AjaxResponseReport<CustomerUser>();
		RequestWS rws = new RequestWS();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		if (!currentUser.userHasAccess(CustomerUser.USERS_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}
		try {
			JSONObject jsonObj = new JSONObject(str);
			
			String email = jsonObj.isNull("email") ? null : jsonObj.getString("email").trim();
			String name = jsonObj.isNull("first_name") ? null : jsonObj.getString("first_name");
			String lastName = jsonObj.isNull("last_name") ? null : jsonObj.getString("last_name");
			String phoneNumber = jsonObj.isNull("phone_number") ? null : jsonObj.getString("phone_number");
			int userId = jsonObj.isNull("userId") ? 0 : jsonObj.getInt("userId");
			
			CustomerUser filter = new CustomerUser();
			
			filter.setFirstName(name);
			filter.setEmail(email);
			filter.setLastName(lastName);
			filter.setPhoneNumber(phoneNumber);
			filter.setUserId(userId);
			ArrayList<CustomerUser> reportDetailList = new ArrayList<>();

			try {
				reportDetailList = rws.userSearchAllEvent(ACCESS_TOKEN,filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.userSearchAllEvent(ACCESS_TOKEN,filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/validateDevice")
	private Response validateDevice(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.MODIFY_EMAIL)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			String email = jsonObj.getString("email");
			String userName = jsonObj.getString("fname") + " " + jsonObj.getString("lname");
			int DEVICE_TYPE_ID = 1;
			int langId = 1;

			DonationDelegate dd = new DonationDelegate();
			result = dd.validateDevice(ACCESS_TOKEN, email, userName, DEVICE_TYPE_ID, langId, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/validateCodeAndEdit")
	private Response validateCodeAndEdit(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.MODIFY_EMAIL)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			String validationCode = jsonObj.getString("validationCode");
			int validationId = jsonObj.getInt("validationId");
			int userId = jsonObj.getInt("userId");

			DonationDelegate dd = new DonationDelegate();
			result = dd.validateCodeAndEdit(ACCESS_TOKEN, userId, validationId, validationCode, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/editWithNoConfirm")
	private Response editWithNoConfirm(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (currentUser == null || !currentUser.userHasAccess(CustomerUser.MODIFY_EMAIL)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			String emailNew = jsonObj.getString("emailNew");
			int userId = jsonObj.getInt("userId");

			DonationDelegate dd = new DonationDelegate();
			result = dd.editWithNoConfirm(ACCESS_TOKEN, userId, emailNew, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/editUserRegistered", produces = "application/json; charset=UTF-8")
	private Response editUserRegistered(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if (!currentUser.userHasAccess(CustomerUser.MODIFY_USER_DATA)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			String fname = jsonObj.getString("fname");
			String lname = jsonObj.getString("lname");
			String bName = jsonObj.getString("nameBefore");
			String bLname = jsonObj.getString("lnameBefore");
			String email = jsonObj.getString("email");
			String phone = jsonObj.getString("phone");
			Integer country = jsonObj.has("countryId") ? jsonObj.getInt("countryId") : null;
			Integer bCountry = jsonObj.has("countryIdBefore") ? jsonObj.getInt("countryIdBefore"): null;
			String bphone = jsonObj.getString("phoneBefore");
			int userId = jsonObj.getInt("userId");
			String ipAddress = hsr.getRemoteAddr();
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();

			if (fname.equals(bName) && lname.equals(bLname) && phone.equals(bphone) && (country == null || country.equals(bCountry))){
				result.setReturnCode(204);
				result.setReturnMessage("The user already had this information.");
			} else {

				CustomerUser userForEdit = new CustomerUser();
				userForEdit.setEmail(email);
				userForEdit.setFirstName(fname);
				userForEdit.setLastName(lname);
				userForEdit.setUserId(userId);
				userForEdit.setIpAddress(ipAddress);
				userForEdit.setPhoneNumber(phone);
				userForEdit.setCustomerId(currentUser.getUserId());
				userForEdit.setNameBefore(bName);
				userForEdit.setLastNameBefore(bLname);
				userForEdit.setPhoneBefore(bphone);
				userForEdit.setCountryId(country != null ? country : 0);
				userForEdit.setCountryIdBefore(bCountry != null ? bCountry : 0);
				

				DonationDelegate dd = new DonationDelegate();
				result = dd.editUserRegistered(ACCESS_TOKEN, userForEdit, userAgent, hsr, currentUser);
			}

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/getUserTransactions")
	private AjaxResponseReport<CustomerTransaction> getUserTransactions(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<CustomerTransaction> result = new AjaxResponseReport<>();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.TX_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("id");
			String ipAddress = hsr.getRemoteAddr();
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			DonationDelegate dd = new DonationDelegate();
			result = dd.getUserTransactions(ACCESS_TOKEN, userId, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setCode("-4");
			result.setMsg("An error occurred, please try again later.");		
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/getAllUserTransactions")
	private AjaxResponseReport<CustomerTransaction> getAllUserTransactions(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<CustomerTransaction> result = new AjaxResponseReport<>();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.TX_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("id");
			String ipAddress = hsr.getRemoteAddr();
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			DonationDelegate dd = new DonationDelegate();
			result = dd.getAllUserTransactions(ACCESS_TOKEN, userId, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setCode("-4");
			result.setMsg("An error occurred, please try again later.");		
		}

		return result;
	}

	@RequestMapping(value = "/search/api/resendUserReceiptTx")
	private Response resendUserReceiptTx(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.SEND_PURCHASE_RECEIPT)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("userId");
			String ipAddress = hsr.getRemoteAddr();
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			int txId = jsonObj.getInt("txId");
			DonationDelegate dd = new DonationDelegate();
			result = dd.resendUserReceiptTx(ACCESS_TOKEN, userId, txId, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/sendMagicCustomerLink")
	private Response sendMagicCustomerLink(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.SEND_MAGIC_LINK)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("userId");
			String ipAddress = getClientIp(hsr);
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			String email = jsonObj.getString("email");
			int txId = jsonObj.isNull("txId") ? 0 : jsonObj.getInt("txId");
			DonationDelegate dd = new DonationDelegate();
			result = dd.sendMagicCustomerLink(ACCESS_TOKEN, userId, email,txId, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/sendTemporyPasswodReset")
	private Response sendTemporyPasswodReset(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.SEND_EMAIL_PASSWORD_RESET)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("customerUserId");
			String ipAddress = getClientIp(hsr);
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			String email = jsonObj.getString("email");
			DonationDelegate dd = new DonationDelegate();
			result = dd.sendTemporyPasswodReset(ACCESS_TOKEN, userId, email, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/invalidateTx")
	private Response invalidateTx(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.INVALIDATE_TRANSACTION)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int txId = jsonObj.getInt("txId");
			String commentary = jsonObj.getString("commentary");
			int customerUserId = jsonObj.getInt("customerUserId");
			DonationDelegate dd = new DonationDelegate();
			result = dd.invalidateTx(ACCESS_TOKEN, customerUserId, txId, commentary, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/cancelTx")
	private Response cancelTx(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		RequestWS requestWS = new RequestWS();
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.CANCEL_TX)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int txId = jsonObj.getInt("txId");
			String commentary = jsonObj.getString("commentary");
			int customerUserId = jsonObj.getInt("customerUserId");
			result = requestWS.cancelTx(ACCESS_TOKEN, customerUserId, txId, commentary, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later. Message:"+e.getMessage());
		}

		return result;
	}

	@RequestMapping(value = "/search/api/getComissionSettings")
	private Response getComissionSettings(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = null;
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		
		if(accesstokenForLifeCycle != null) {
			ACCESS_TOKEN = accesstokenForLifeCycle;
		}else {
			refreshToken(session);
			ACCESS_TOKEN = validateAndRenewToken(session);
		}
		
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		try {
			DonationDelegate dd = new DonationDelegate();
			result = dd.getComissionSettings(ACCESS_TOKEN, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/getBlackList")
	private Response getBlackList(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		try {
			DonationDelegate dd = new DonationDelegate();
			result = dd.getBlackList(ACCESS_TOKEN, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/unBlockFromBlackList")
	private Response unBlockFromBlackList(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		try {

			JSONObject jsonObj = new JSONObject(str);
			int unblockId = jsonObj.getInt("id");
			DonationDelegate dd = new DonationDelegate();
			result = dd.unBlockFromBlackList(ACCESS_TOKEN, unblockId, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/getUserAccessList")
	private AjaxResponseReport<UserAccess> getUserAccessList(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<UserAccess> result = new AjaxResponseReport<>();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.USERS_ACCESS_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			String email = jsonObj.getString("email");
			DonationDelegate dd = new DonationDelegate();
			result = dd.getUserAccessList(ACCESS_TOKEN, email, hsr, currentUser);

		} catch (Exception e) {
			result.setCode("-4");
			result.setMsg("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/resendUserReceiptOp")
	private Response resendUserReceiptOp(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.SEND_PURCHASE_RECEIPT)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("userId");
			int txId = jsonObj.getInt("txId");
			String adminEmail = currentUser.getEmail();
			DonationDelegate dd = new DonationDelegate();
			result = dd.resendUserReceiptOp(ACCESS_TOKEN, userId, txId, adminEmail, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}

	@RequestMapping(value = "/search/api/getAppLogList")
	private AjaxResponseReport<Audit> getAppLogList(@RequestBody String str, HttpServletRequest hsr,
			HttpSession session) throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		AjaxResponseReport<Audit> result = new AjaxResponseReport<Audit>();

		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.AUDIT_QUERY)) {
			result.setCode("403");
			result.setMsg("Your not authorized to execute this function!");
			return result;
		}

		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("what")||keyM.toString().equals("who")) {
						filter.put(keyM, valueM.toString().trim());
					}else {
						filter.put(keyM, valueM);
					}
				}
			}
			DonationDelegate dd = new DonationDelegate();
			ArrayList<Audit> reportDetailList = new ArrayList<>();

			try {
				reportDetailList = dd.getAppLogList(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = dd.getAppLogList(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/getAllLanguages")
	private Response getAllLanguages(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.USERS_ACCESS_QUERY)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			DonationDelegate dd = new DonationDelegate();
			result = dd.getAllLanguages(ACCESS_TOKEN);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/getAllAssets")
	private Response getAllAssets(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.USERS_ACCESS_QUERY)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			DonationDelegate dd = new DonationDelegate();
			result = dd.getAllAssets(ACCESS_TOKEN);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/getEventsAssetsByLang")
	private Response getEventsAssetsByLang(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.USERS_ACCESS_QUERY)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {
			JSONObject jsonObj = new JSONObject(str);
			int lang_id = jsonObj.getInt("lang_id");
			DonationDelegate dd = new DonationDelegate();
			result = dd.getEventsAssetsByLang(ACCESS_TOKEN, lang_id);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/addSetting")
	private Response addSetting(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if(currentUser == null) {
			result.setReturnCode(401);
			result.setReturnMessage("logout");
			return result;
		}

		if (!currentUser.userHasAccess(CustomerUser.EVENT_ASSETS)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray assetsConf = jsonObj.optJSONArray("eventAssets");			
			ArrayList<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>(); 
			if (assetsConf != null) { 
			   for (int i=0;i<assetsConf.length();i++){ 
				   Map<String, Object> elm =   assetsConf.getJSONObject(i).toMap();
					 int assetId = (int) elm.get("assetId"); 
					 
					if(assetId == 1) {
						String stringfySimLive = new JSONObject(elm.get("assetParams")).toString();
						elm.put("assetParams", stringfySimLive);
					}
					
					 listdata.add(elm);
			   } 
			} 
			DonationDelegate dd = new DonationDelegate();
			result = dd.addSetting(ACCESS_TOKEN, listdata, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/updateSetting")
	private Response updateSetting(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if(currentUser == null) {
			result.setReturnCode(401);
			result.setReturnMessage("logout");
			return result;
		}

		if (!currentUser.userHasAccess(CustomerUser.EVENT_ASSETS)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray assetsConf = jsonObj.optJSONArray("eventAssets");			
			ArrayList<Map<String, Object>> listdata = new ArrayList<Map<String, Object>>();
			boolean error = false;
			int big = 0;
			if (assetsConf != null) { 
			   for (int i=0;i<assetsConf.length();i++){ 
				 Map<String, Object> elm =   assetsConf.getJSONObject(i).toMap();
				 int assetId = (int) elm.get("assetId"); 
				if(assetId == 1) {
					String assetP = (String) elm.getOrDefault("assetParams", "");
					if(!assetP.equals("")) {
						JSONObject assetpJ = new JSONObject(assetP);
						Map<String, Object> stringfySimLive =  assetpJ.toMap();
						elm.put("assetParams", stringfySimLive);
					} 					
				}else if(assetId == 73) {
					
					  //Verificacion de la playList
			    	  String strA = (String) elm.get("assetValue");
			    	  JSONArray arr = new JSONArray(strA);
			    	  
			    	  //Recorremos la lista de videos
			    	  for (int k = 0 ; k < arr.length(); k++) {
			    		  //Obtenemos el objeto 
							JSONObject obj = arr.getJSONObject(k);	
							int time = 0;
							//Se verifica si no existe o es 0 el tiempo para visualizacion de videos.
							if (!obj.has("available_time") || "".equals(obj.getString("available_time")) || "0".equals(obj.getString("available_time"))) 		        	
							{
								time = 0;
							}else {
								//Se obtiene ese tiempo
								time = Integer.valueOf(obj.getString("available_time"));
							}
							
							//Aqui verificamos que el tiempo siempre sea de manera ascendente.
							if(time>=big) {
								big = time;
							}else {
								error = true;
							}
						}
			      }
				
				 listdata.add(elm);
			   } 
			} 
			
			if(error) {
				result.setReturnCode(-16);
				result.setReturnMessage("The set available time value should be from lowest to highest in ascending order.");
				return result;
			}
			DonationDelegate dd = new DonationDelegate();
			result = dd.updateSetting(ACCESS_TOKEN, listdata, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/approveSignature")
	private Response approveSignature(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if(currentUser == null) {
			result.setReturnCode(401);
			result.setReturnMessage("logout");
			return result;
		}

		if (!currentUser.userHasAccess(CustomerUser.REGISTER_DOCUMENT_SIGNATURE)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {
			JSONObject jsonObj = new JSONObject(str);
			int customerId = jsonObj.getInt("userId");
			long transaction = jsonObj.getLong("transactionId");
			String comment = jsonObj.getString("comments");
			DonationDelegate dd = new DonationDelegate();
			result = dd.approveSignature(ACCESS_TOKEN, customerId, transaction, comment, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	private static final String[] HEADERS_LIST = { 
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR" 
	};

	public static String getClientIp(HttpServletRequest request) {
		for (String header : HEADERS_LIST) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr(); 
	}

	@RequestMapping(value = "/search/api/createGlobalReport")
	private AjaxResponseReport<ReportDetail> globalReportDetail(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<ReportDetail> result = new AjaxResponseReport<ReportDetail>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("operator_email")||keyM.toString().equals("person_email")) {
						filter.put(keyM, valueM.toString().trim());
					}else {
						filter.put(keyM, valueM);
					}
				}
			}
			if (!filter.isEmpty()) {
				DonationDelegate dd = new DonationDelegate();
				ArrayList<ReportDetail> reportDetailList = new ArrayList<>();
				CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

				try {
					reportDetailList = dd.globalReportDetail(ACCESS_TOKEN, filter, hsr, currentUser);
				} catch (ExpiredAccessTokenException ex) {
					log.error("ERROR", ex);
					refreshToken(session);
					ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
					reportDetailList = dd.reportDetail(ACCESS_TOKEN, filter, hsr, currentUser);
				}
				if (reportDetailList != null) {
					result.setCode("200");
					result.setMsg("");
					result.setResult(reportDetailList);
				}
			}			
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/online", method = RequestMethod.GET)
	public Response getOnlineUsers1(HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			result = dd.getOnlineUsers(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/registeredUsers", method = RequestMethod.GET)
	public Response getEventDetailsregisteredUsers(HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			result = dd.getEventDetailsregisteredUsers(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/detailsOnlineUsers", method = RequestMethod.GET)
	public Response getEventDetailsOnlineUsers(HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			result = dd.getEventDetailsOnlineUsers(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	
	@RequestMapping(value = "/search/api/blacklist", method = RequestMethod.POST)
	public Response addBlacklistItem(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			String data = jsonObj.getString("data").trim();
			int dataTypeId = jsonObj.getInt("dataTypeId");
			if(dataTypeId == 1) {
				Pattern pat = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");   
			    Matcher patEmailOrigin = pat.matcher(data);
			    if(!patEmailOrigin.find()) {
			    	result.setReturnCode(-4);
					result.setReturnMessage("Invalid email");
					return result;
			    }
			}else {}
			result = dd.addBlacklistItem(data, dataTypeId, ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/addWhileListItem", method = RequestMethod.POST)
	public Response addWhileListItem(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		RequestWS rws = new RequestWS();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		int adminUserId = currentUser.getUserId();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			String data = jsonObj.getString("email");
			String reason = jsonObj.getString("reason");
			result = rws.addWhileListItem(data, adminUserId, reason, ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/listWhileListItem", method = RequestMethod.POST)
	public Response listWhileListItem(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		RequestWS rws = new RequestWS();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			result = rws.listWhileListItem(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}

	@RequestMapping(value = "/search/api/deleteWhiteListItem", method = RequestMethod.POST)
	public Response deleteWhiteListItem(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		RequestWS rws = new RequestWS();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		int adminUserId = currentUser.getUserId();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String data = String.valueOf(jsonObj.getInt("id"));
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			result = rws.deleteWhiteListItem(data, adminUserId,ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/deleteSourceCodeItem", method = RequestMethod.POST)
	public Response deleteSourceCodeItem(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		RequestWS rws = new RequestWS();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		int adminUserId = currentUser.getUserId();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String data = String.valueOf(jsonObj.getInt("id"));
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			result = rws.deleteSourceCodeItem(data, adminUserId,ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/updateReportingTables", method = RequestMethod.GET)
	public Response updateReportingTables(HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			result = dd.updateReportingTables(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/reportUserFraud", method = RequestMethod.POST)
	public Response reportUserWireFraud(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			int customerUserId = jsonObj.getInt("customerUserId");
			int txId = jsonObj.getInt("txId");
			result = dd.reportUserWireFraud(customerUserId,txId,ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/transferTransactionToAnotherUser", method = RequestMethod.POST)
	public Response transferTransactionToAnotherUser(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			String emailOrigin = jsonObj.getString("emailOrigin");
			String emailToTransfer = jsonObj.getString("emailToTransfer");
			try {
				result = dd.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result = dd.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, ACCESS_TOKEN);
			}
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/transferTransactionToAnotherUserUserLifeCycle", method = RequestMethod.POST)
	public Response transferTransactionToAnotherUserUserLifeCycle(@RequestBody String str ,HttpSession session) {
		Response result = new Response();
		refreshToken(session);
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			String emailOrigin = jsonObj.getString("emailOrigin");
			String emailToTransfer = jsonObj.getString("emailToTransfer");
			int customerId = jsonObj.getInt("customerId");
			long transactionId = jsonObj.getLong("transactionId");
			RequestWS rws = new RequestWS();
			try {
				result = rws.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, customerId, transactionId,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result = rws.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, customerId, transactionId,ACCESS_TOKEN);
			}
			return result;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
			return result;
		}
	}
	
	@RequestMapping(value = "/search/api/addPurchaseSource", method = RequestMethod.POST)
	public Response addPurchaseSource(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			String nameSourcePurchase = jsonObj.getString("name");
			String descriptionSourcePurchase = jsonObj.getString("description");
			String activeSourcePurchase = String.valueOf(jsonObj.getBoolean("active"));
			int langId = jsonObj.getInt("language");
			int adminUserId = currentUser.getUserId();
			try {
				response = rws.addPurchaseSource(nameSourcePurchase, descriptionSourcePurchase,activeSourcePurchase, adminUserId, langId,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.addPurchaseSource(nameSourcePurchase, descriptionSourcePurchase,activeSourcePurchase, adminUserId, langId,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/scan-qr", method = RequestMethod.POST)
	public Response scanQR(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			String qr = jsonObj.getString("qr");
			int adminUserId = currentUser.getUserId();
			try {
				response = rws.scarQR(qr, adminUserId,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.scarQR(qr, adminUserId,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/listPurchaseSource", method = RequestMethod.POST)
	public Response listPurchaseSource(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				response = rws.findSources(ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.findSources(ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/listAllCountries", method = RequestMethod.POST)
	public Response getListAllCountries(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				response = rws.getListAllCountries(ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.getListAllCountries(ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/createUser", produces = "application/json; charset=UTF-8")
	public AjaxResponseBody createUser(@RequestBody String str, HttpServletRequest hsr, HttpSession session) throws AlreadyExistsException {
		AjaxResponseBody result = new AjaxResponseBody();

		RegisterNewUser newUser = new RegisterNewUser();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();
			CustomerNewUser customerNewUser = null;
			HashMap<String, Object> verify = new HashMap<>();
			Boolean verifyUser = false;
			int idUser = -1;
			try {
				JSONObject jsonObj = new JSONObject(str);
				JSONObject newUserJO = jsonObj.getJSONObject("newUser");
				newUser.setEmail(newUserJO.getString("email").toLowerCase());
				newUser.setFirstName(newUserJO.getString("firstName"));
				newUser.setLastName(newUserJO.getString("lastName"));
				newUser.setPhoneNumber(newUserJO.getString("phoneNumber"));
				newUser.setCountryId(Integer.valueOf(newUserJO.getString("countryId")));
				newUser.setPassword(newUserJO.getString("password"));
				idUser = newUserJO.getInt("userId");
				
				if(newUserJO.isNull("transactionId")) {
					verify = dd.verifyUserForRegister(newUser.getEmail(), ACCESS_TOKEN);
				}else {
					RequestWS rws = new RequestWS();
					String transactionIdString = newUserJO.getString("transactionId");
					long transactionId = Long.parseLong(transactionIdString);
					verify = rws.verifyUserForRegister(newUser.getEmail(), transactionId, ACCESS_TOKEN);
				}
				if ( verify.get("returnCode") != null &&   ((int) verify.get("returnCode")) == -60){
					result.setCode("-60");
					result.setMsg((String) verify.get("returnMessage"));
					return result;
				}
				
				verifyUser = (Boolean) verify.get("existsUser");
				customerNewUser = dd.registerNewUser(newUser,getClientIp(hsr), hsr.getHeader("user-agent"),ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				if (idUser == -1){
					if (!verifyUser) {	
						customerNewUser = dd.registerNewUser(newUser,getClientIp(hsr), hsr.getHeader("user-agent"),ACCESS_TOKEN);
					} else {
						customerNewUser = new CustomerNewUser();
						customerNewUser.setEmail(newUser.getEmail());
						customerNewUser.setFirstName(newUser.getFirstName());
						customerNewUser.setLastName(newUser.getLastName());
						customerNewUser.setPassword((String) session.getAttribute("userAcessToken"));
					}
				} else {
					newUser.setGenderId(idUser);
					if (differentUser(session, newUser)) {
						customerNewUser = dd.updateNewUser(newUser,getClientIp(hsr), hsr.getHeader("user-agent"),ACCESS_TOKEN);
					} else {
						customerNewUser = new CustomerNewUser();
						customerNewUser.setEmail(newUser.getEmail());
						customerNewUser.setFirstName(newUser.getFirstName());
						customerNewUser.setLastName(newUser.getLastName());
						customerNewUser.setPassword((String) session.getAttribute("userAcessToken"));
					}

				}				
			} catch (AlreadyExistsException ex){
				String mensaje[] = ex.getMessage().split("-");
				idUser = Integer.valueOf(mensaje[0]);
				boolean payment = Boolean.valueOf(mensaje[1]);
				newUser.setGenderId(idUser);
				if (differentUser(session, newUser)) {
					customerNewUser = dd.updateNewUser(newUser, getClientIp(hsr), hsr.getHeader("user-agent"),ACCESS_TOKEN);
				} else {
					customerNewUser = new CustomerNewUser();
					customerNewUser.setEmail(newUser.getEmail());
					customerNewUser.setFirstName(newUser.getFirstName());
					customerNewUser.setLastName(newUser.getLastName());
					customerNewUser.setPassword((String) session.getAttribute("userAcessToken"));
				}
				customerNewUser.setPayment(payment);
			} catch (UnknownCustomerUserException ex){
				if (!verifyUser) {	
					customerNewUser = dd.registerNewUser(newUser, getClientIp(hsr), hsr.getHeader("user-agent"),ACCESS_TOKEN);
				} else {
					customerNewUser = new CustomerNewUser();
					customerNewUser.setEmail(newUser.getEmail());
					customerNewUser.setFirstName(newUser.getFirstName());
					customerNewUser.setLastName(newUser.getLastName());
					customerNewUser.setPassword((String) session.getAttribute("userAcessToken"));
				}				
			}	
			if (customerNewUser != null) {
				result.setCode("200");
				result.setMsg(customerNewUser.getPassword());
				HashMap<String, Object> tmp = new HashMap<>();
				tmp.put("payment", customerNewUser.isPayment());
				//tmp.put("customerNewUser", customerNewUser.toString());
				result.setResultHashMap(tmp);
			}
			else {
				result.setCode("403");
				result.setMsg("error");
			}
		} catch (Exception e) {
			result.setCode("400");
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	private boolean differentUser (HttpSession session, RegisterNewUser newUser) {
		boolean result = true;			
		if (newUser.getFirstName().equals((String) session.getAttribute("firstName")) &&
				newUser.getLastName().equals((String) session.getAttribute("LastName")) &&
				newUser.getEmail().equals((String) session.getAttribute("email"))) {
			result = false;
		}			
		return result;
	}
	
	@RequestMapping(value = "/search/api/updateTags", method = RequestMethod.POST)
	public Response updateTags(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		try {
			JSONObject jsonObj = new JSONObject(str);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			int adminUserId = currentUser.getUserId();
			try {
				response = rws.updateTags(jsonObj.toString(), adminUserId,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.scarQR(jsonObj.toString(), adminUserId,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/purchaseUserInformationByEventCategory", method = RequestMethod.POST)
	public Response purchaseUserInformationByEventCategory(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			int adminUserId = currentUser.getUserId();
			try {
				response = rws.purchaseUserInformationByEventCategory(str, adminUserId,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.purchaseUserInformationByEventCategory(str, adminUserId,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/addBusinessUserSupportHistory", method = RequestMethod.POST)
	public Response addBusinessUserSupportHistory(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			int adminUserId = currentUser.getUserId();
			try {
				response = rws.addBusinessUserSupportHistory(str, adminUserId, ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.addBusinessUserSupportHistory(str, adminUserId, ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/listUserCreditCards", method = RequestMethod.POST)
	public Response listUserCreditCards(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("listUserCard", rws.listUserCreditCards(str, ACCESS_TOKEN)) ;
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("listUserCard", rws.listUserCreditCards(str, ACCESS_TOKEN));
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/addUserCreditCard", method = RequestMethod.POST)
	public Response addUserCreditCard(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("listUserCard", rws.addUserCreditCard(str, ACCESS_TOKEN)) ;
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("listUserCard", rws.addUserCreditCard(str, ACCESS_TOKEN)) ;
			} catch (StripeUtilsException e) {
				log.error("ERROR", e);
				response.setReturnCode(-63);
				response.setReturnMessage(e.getLocalizedMessage());
				return response;
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/deleteUserCard", method = RequestMethod.POST)
	public Response deleteUserCard(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("listUserCard", rws.deleteUserCard(str, ACCESS_TOKEN)) ;
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("listUserCard", rws.deleteUserCard(str, ACCESS_TOKEN)) ;
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/getEventsAvailableForPurchase", method = RequestMethod.POST)
	public Response getEventsAvailableForPurchase(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("eventsAvailable", rws.getEventsAvailableForPurchase(str, ACCESS_TOKEN)) ;
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("listUserCard", rws.deleteUserCard(str, ACCESS_TOKEN)) ;
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/updateToDateUserAccess", method = RequestMethod.POST)
	public Response updateToDateUserAccess(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		
		if(currentUser == null) {
			response.setReturnCode(401);
			response.setReturnMessage("logout");
			return response;
		}

		if (!currentUser.userHasAccess(CustomerUser.RESTRICT_EVENT)) {
			response.setReturnCode(403);
			response.setReturnMessage("Your not authorized to execute this function!");
			return response;
		}

		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				response = rws.updateToDateUserAccess(currentUser,hsr,str,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.updateToDateUserAccess(currentUser,hsr,str,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/expireUserAccess", method = RequestMethod.POST)
	public Response expireUserAccess(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		
		if(currentUser == null) {
			response.setReturnCode(401);
			response.setReturnMessage("logout");
			return response;
		}

		if (!currentUser.userHasAccess(CustomerUser.EXPIRED_ACCESS)) {
			response.setReturnCode(403);
			response.setReturnMessage("Your not authorized to execute this function!");
			return response;
		}

		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				response = rws.expireUserAccess(currentUser,hsr,str,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				response = rws.expireUserAccess(currentUser,hsr,str,ACCESS_TOKEN);
			}
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/findLifeCycleByUser", method = RequestMethod.POST)
	public Response findLifeCycleByUser(@RequestBody String str, HttpServletRequest hsr, HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		Response response = new Response();
		List<HashMap<String, Object>> userLifecycle = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> result = new HashMap<>();
		JSONObject jsonObj = new JSONObject(str);

		Long userId = jsonObj.getLong("userId");

		if(currentUser == null) {
			response.setReturnCode(401);
			response.setReturnMessage("logout");
			return response;
		}
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			
			userLifecycle = rws.findLifecycle(userId.toString(), ACCESS_TOKEN);
			String[] lifeCycle = {"Seminario Creando Riqueza", "Seminario de Inversiones", "Planeación Estratégica", "Clase de Trading Live"};

			if(userLifecycle== null) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				userLifecycle = rws.findLifecycle(userId.toString(), ACCESS_TOKEN);
			}
				
			result.put("userLifecycle", userLifecycle);
			result.put("lifeCycle", lifeCycle);
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/sendMagicLinkOperator")
	private Response sendMagicLinkOperator(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (!currentUser.userHasAccess(CustomerUser.SEND_MAGIC_LINK)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {

			JSONObject jsonObj = new JSONObject(str);
			int userId = jsonObj.getInt("userId");
			String ipAddress = getClientIp(hsr);
			String userAgent = currentUser.getFirstName() + " " + currentUser.getLastName();
			String email = jsonObj.getString("email");
			String emailOp = currentUser.getEmail();
			int txId = jsonObj.isNull("txId") ? 0 : jsonObj.getInt("txId");
			DonationDelegate dd = new DonationDelegate();
			result = dd.sendMagicLinkOperator(ACCESS_TOKEN, userId, email, emailOp, txId, ipAddress, userAgent, hsr, currentUser);

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/assistedCCPurchaseSplitPayment", produces = "application/json; charset=UTF-8")
	private AjaxResponseBody assistedCCPurchaseSplitPayment(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		String ACCESS_TOKEN = null;
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		
		if(accesstokenForLifeCycle != null) {
			ACCESS_TOKEN = accesstokenForLifeCycle;
		}else {
			refreshToken(session);
			ACCESS_TOKEN = validateAndRenewToken(session);
		}
		
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			String ipAddress = "10.0.0.1";//getClientIp(hsr);
			JSONObject jsonObj = new JSONObject(str);
			TxDetail txDetail = new TxDetail();
			txDetail.setFundId(Integer.parseInt(jsonObj.getString("fund")));
			txDetail.setAmount(Double.parseDouble(jsonObj.getString("payableAmount")));
			txDetail.setSplitPayment(true);

			TxUserData txUserData = new TxUserData();
			txUserData.setName(jsonObj.getString("name"));
			txUserData.setLastname(jsonObj.getString("lastname"));
			txUserData.setEmail(jsonObj.getString("email"));
			txUserData.setPhone(jsonObj.getString("phone"));
			txUserData.setCountry(jsonObj.getInt("country"));
			txUserData.setSourceCode( jsonObj.isNull("source_code") ? 0 : jsonObj.getInt("source_code"));
			
			if(txUserData.getSourceCode() == 1010 || txUserData.getSourceCode() == 1020){
				result.setCode("400");
				result.setMsg("You must select a sourceCode");
				return result;
			}
			
			{//Inicio de verifycacion de usuario para saber si puede o no comprar desde el admin una clase.
				RequestWS requestWS = new RequestWS();
				HashMap<String, Object> verifyUser3 = requestWS.verifyUser3(txUserData.getEmail(), ipAddress, ACCESS_TOKEN);
				Response responseVerifyUser = verifyUser3 != null ? (Response) verifyUser3.get("response") : null; 
				if(responseVerifyUser != null && responseVerifyUser.getReturnCode() == -60){
					result.setCode("400");
					result.setMsg(responseVerifyUser.getReturnMessage());
					return result;
				}
			}//fin verifyUser
			
			
			PaymentGwParameters paymentGwParameters = new PaymentGwParameters();
			
			// por ahora desde el admin no se guardaran tarjetas.
			paymentGwParameters.setSavePaymentData(jsonObj.getBoolean("savePaymentData"));
			paymentGwParameters.setOrderDescription(jsonObj.getString("orderDescription"));
			//Si el token no viene vacio se agrega en paymentGwParameters
			if(jsonObj.getString("token").length() > 5){
				paymentGwParameters.setToken(jsonObj.getString("token"));
			//Se lo contrario se agrega el cardId, es decir, la compra se hara con una card previamente guardada.
			}else {
				paymentGwParameters.setCardId(jsonObj.getString("card"));
				
			}

			String medium = jsonObj.getString("medium");
			medium = (String) session.getAttribute("firstName") + " " + session.getAttribute("LastName");
			String source = jsonObj.getString("source");
			source = "SeminarioAdmin";
			String coupon = jsonObj.getString("coupon");
			String comments = jsonObj.getString("comments");

			int paymentGatewayId = jsonObj.getInt("paymentGatewayId");
			int lang = ((Number) jsonObj.getInt("langid")) != null ? ((Number) jsonObj.getInt("langid")).intValue() : 1;

			Long startDate = null;
			boolean checkStartDate = false;
			try {
				if(jsonObj.getBoolean("checkStartDate")) {
					startDate = jsonObj.getLong("startDate");
					checkStartDate = jsonObj.getBoolean("checkStartDate");
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			int operatorId = currentUser.getId();

			DonationDelegate dd = new DonationDelegate();
			AuthorizationInfo authorizationInfo;
			try {
				authorizationInfo = dd.assistedCCPurchaseSplitPayment(ACCESS_TOKEN, txDetail, txUserData, paymentGwParameters,
						paymentGatewayId, medium, source, lang, coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
				//session.setAttribute("accesstokenForLifeCycle", null);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				authorizationInfo = dd.assistedCCPurchaseSplitPayment(ACCESS_TOKEN, txDetail, txUserData, paymentGwParameters,
						paymentGatewayId, medium, source, lang, coupon, operatorId, comments, hsr, currentUser,startDate,checkStartDate);
			}

			if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("0")) {
				result.setCode("200");
				result.setMsg(authorizationInfo.toString());
				result.setTxId(authorizationInfo.getTxId());
			} else if (authorizationInfo != null && authorizationInfo.getResponseCode().equals("-20")) {
				result.setCode("203");
				result.setMsg(authorizationInfo.getMessageResult());
			} else {
				result.setCode("204");
				result.setMsg(authorizationInfo.getMessageResult());
			}

		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	@RequestMapping(value = "/search/api/findResponseDateOptionsReports", method = RequestMethod.POST)
	public Response getOptionsDateReport (HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("options", rws.getOptionsDateReport(ACCESS_TOKEN));
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("options", null);
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	@RequestMapping(value = "/search/api/createReportAffiliateMaster", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<AffiliateReport> createReportAffiliateMaster(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<AffiliateReport> result = new AjaxResponseReport<AffiliateReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			boolean exitReport = true;
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("person_email")) {
						filter.put("u2.email", valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
					if(keyM.toString().equals("date_months")) {
						exitReport =false;
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<AffiliateReport> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			if(exitReport) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
				return result;
			}
			try {
				reportDetailList = rws.listReportAffiliateMaster(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportAffiliateMaster(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	@RequestMapping(value = "/search/api/findListAllSelectReports", method = RequestMethod.POST)
	public Response findListAllSelectReports (HttpSession session) {
		RequestWS rws = new RequestWS();
		refreshToken(session);
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("allSelects", rws.findListAllSelectReports(ACCESS_TOKEN));
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("options", null);
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/createReportAffiliateDetail", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<AffiliateReport> createReportAffiliateDetail(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<AffiliateReport> result = new AjaxResponseReport<AffiliateReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			int affiliateId = jsonObj.getInt("affiliate_id");
			String dateMonths = jsonObj.getString("date_months");
			
			RequestWS rws = new RequestWS();
			ArrayList<AffiliateReport> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			filter.put("a.id", affiliateId);
			filter.put("date_months", dateMonths);
			try {
				reportDetailList = rws.listReportAffiliateDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportAffiliateDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	@RequestMapping(value = "/search/api/createReportAffiliateMasterDetail", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<AffiliateReport> createReportAffiliateMasterDetail(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<AffiliateReport> result = new AjaxResponseReport<AffiliateReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("u2.email")||keyM.toString().equals("u.email")) {
						filter.put(keyM, valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<AffiliateReport> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			try {
				reportDetailList = rws.listReportAffiliateDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportAffiliateDetail(ACCESS_TOKEN, filter, hsr, currentUser);
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}

	@RequestMapping(value = "/search/api/findResponseUpdatePaymentAffiliateReport", method = RequestMethod.POST)
	public Response findResponseUpdatePaymentAffiliateReport(@RequestBody TransactionUpdate transactionUpdate, HttpSession session) {
		RequestWS rws = new RequestWS();
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
//	 session.setAttribute("option", 1);

		refreshToken(session);  

		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("updateTransactions", rws.findResponseUpdatePaymentAffiliateReport(transactionUpdate,ACCESS_TOKEN));
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("updateTransactions", null);
			}
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			log.error("ERROR", ex);
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/updatePreferredLanguage", method = RequestMethod.POST)
	public Response updatePreferredLanguage(@RequestBody String str ,HttpSession session) {
		RequestWS rws = new RequestWS();
		HashMap<String, Object> result = new HashMap<>();
		Response response = new Response();
		JSONObject jsonObj = new JSONObject(str);
		try {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			try {
				result.put("listUserCard", rws.updatePreferredLanguage(ACCESS_TOKEN, jsonObj)) ;
			} catch (ExpiredAccessTokenException ex) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result.put("listUserCard", rws.updatePreferredLanguage(ACCESS_TOKEN, jsonObj)) ;
			}	
			response.setResult(result);
			return response;
		} catch (Exception ex) {
			response.setReturnCode(-4);
			response.setReturnMessage("An error occurred, please try again later.");
			return response;
		}
	}
	
	@RequestMapping(value = "/search/api/createReportSalesAdmin", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<SalesReport> createReportSalesAdmin(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<SalesReport> result = new AjaxResponseReport<SalesReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("u.email")) {
						filter.put(keyM, valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<SalesReport> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			if(currentUser == null) {
				result.setCode("401");
				result.setMsg("logout");
				return result;
			}

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN)) {
				result.setCode("403");
				result.setMsg("Your not authorized to execute this function!");
				return result;
			}
			try {
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportSalesAdmin");
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportSalesAdmin");
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	
	@RequestMapping(value = "/search/api/updateStatuSales")
	private Response updateStatuSales(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if(currentUser == null) {
			result.setReturnCode(401);
			result.setReturnMessage("logout");
			return result;
		}

		if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}
		
		try {
			RequestWS rws = new RequestWS();
			try {
				result = rws.updateStatuSales(currentUser,hsr,str,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result = rws.updateStatuSales(currentUser,hsr,str,ACCESS_TOKEN);
			}

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/createReportSalesAgent", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<SalesReport> createReportSalesAgent(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<SalesReport> result = new AjaxResponseReport<SalesReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("u.email")) {
						filter.put(keyM, valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<SalesReport> reportDetailList = new ArrayList<>();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			if(currentUser == null) {
				result.setCode("401");
				result.setMsg("logout");
				return result;
			}

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				result.setCode("403");
				result.setMsg("Your not authorized to execute this function!");
				return result;
			}
			try {
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportAgent");
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportAgent");
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	
	@RequestMapping(value = "/search/api/addBusinessListUsersSupportHistory")
	private Response addBusinessListUsersSupportHistory(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);
		Response result = new Response();
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		
		if(currentUser == null) {
			result.setReturnCode(401);
			result.setReturnMessage("logout");
			return result;
		}

		if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
			result.setReturnCode(403);
			result.setReturnMessage("Your not authorized to execute this function!");
			return result;
		}

		try {
			RequestWS rws = new RequestWS();
			try {
				result = rws.addBusinessListUsersSupportHistory(currentUser,hsr,str,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				result = rws.addBusinessListUsersSupportHistory(currentUser,hsr,str,ACCESS_TOKEN);
			}

		} catch (Exception e) {
			result.setReturnCode(-4);
			result.setReturnMessage("An error occurred, please try again later.");
		}

		return result;
	}
	
	@RequestMapping(value = "/search/api/createReportSalesLeadScr", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<SalesReport> createReportSalesLeadScr(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<SalesReport> result = new AjaxResponseReport<SalesReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			filter.put("lead_type", 1);  //Filtrar solo los SCR Lead
			filter.put("u_agent.id", currentUser.getUserId()); 
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("u.email")) {
						filter.put(keyM, valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<SalesReport> reportDetailList = new ArrayList<>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				result.setCode("403");
				result.setMsg("Your not authorized to execute this function!");
				return result;
			}
			try {
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportLeadScr");
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportLeadScr");
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	
	@RequestMapping(value = "/search/api/createReportSalesLeadClass", produces = "application/json; charset=UTF-8")
	private AjaxResponseReport<SalesReport> createReportSalesLeadClass(@RequestBody String str, HttpServletRequest hsr, HttpSession session)
			throws JsonProcessingException, IOException {
		refreshToken(session);
		String ACCESS_TOKEN = validateAndRenewToken(session);

		AjaxResponseReport<SalesReport> result = new AjaxResponseReport<SalesReport>();
		try {
			HashMap<String, Object> filter = new HashMap<>();
			JSONObject jsonObj = new JSONObject(str);
			Iterator<String> keys = jsonObj.keys();
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

			filter.put("lead_type", 3);  //Filtrar solo los SCR Lead
			filter.put("u_agent.id", currentUser.getUserId()); 
			while (keys.hasNext()) {
				String keyM = keys.next();
				Object valueM = jsonObj.get(keyM);
				if (valueM != null && valueM.toString().length() > 0) {
					if(keyM.toString().equals("u.email")) {
						filter.put(keyM, valueM.toString().trim());
					} else {
						filter.put(keyM, valueM);
					}
				}
			}
			RequestWS rws = new RequestWS();
			ArrayList<SalesReport> reportDetailList = new ArrayList<>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				result.setCode("403");
				result.setMsg("Your not authorized to execute this function!");
				return result;
			}
			try {
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportLeadClass");
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
				reportDetailList = rws.listReportSales(ACCESS_TOKEN, filter, hsr, currentUser,"reportLeadClass");
			}
			if (reportDetailList != null) {
				result.setCode("200");
				result.setMsg("");
				result.setResult(reportDetailList);
			}
		} catch (Exception e) {
			log.error("ERROR", e);
			result.setCode("400");
			result.setMsg("An error occurred, please try again later.");
		}
		return result;
	}
	
}

