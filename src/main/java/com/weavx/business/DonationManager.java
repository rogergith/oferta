package com.weavx.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.weavx.web.dto.FormMultiCustomerLoadDTO;
import com.weavx.web.model.AjaxResponseBody;
import com.weavx.web.model.AjaxResponseBodyHash2;
import com.weavx.web.model.AjaxResponseReport;
import com.weavx.web.model.Amount;
import com.weavx.web.model.Audit;
import com.weavx.web.model.AuthenticateExternalUser;
import com.weavx.web.model.AuthorizationInfo;
import com.weavx.web.model.Country;
import com.weavx.web.model.CsvFileModel;
import com.weavx.web.model.CsvModelRestrict;
import com.weavx.web.model.CustomerApplications;
import com.weavx.web.model.CustomerNewUser;
import com.weavx.web.model.CustomerTransaction;
import com.weavx.web.model.CustomerUser;
import com.weavx.web.model.Fund;
import com.weavx.web.model.FundImage;
import com.weavx.web.model.EventFundSettings;
import com.weavx.web.model.Language;
import com.weavx.web.model.ListFund;
import com.weavx.web.model.ListProp;
import com.weavx.web.model.ExternalPaymentType;
import com.weavx.web.model.PaymentGateways;
import com.weavx.web.model.PaymentGatewaysDD;
import com.weavx.web.model.PaymentGwParameters;
import com.weavx.web.model.PaymentTypeDescription;
import com.weavx.web.model.Property;
import com.weavx.web.model.RegisterNewUser;
import com.weavx.web.model.ReportDetail;
import com.weavx.web.model.Response;
import com.weavx.web.model.RestrictedUser;
import com.weavx.web.model.TransactionStatus;
import com.weavx.web.model.TxCampaing;
import com.weavx.web.model.TxDetail;
import com.weavx.web.model.TxDetailList;
import com.weavx.web.model.TxExternalPayment;
import com.weavx.web.model.TxSources;
import com.weavx.web.model.TxUserData;
import com.weavx.web.model.UserAccess;
import com.weavx.web.utils.CacheLoader;
import com.weavx.web.utils.RequestWS;

public class DonationManager {

	public String requestAccessToken(int option) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			String token = rws.getAccessToken(option);
			return token;	
		} catch (Throwable ex){
			throw ex;
		}
		
	}

	public ArrayList<Language> requestLanguage(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Language> aLanguage = cacheLoader.getLanguage(accessTk);
			return aLanguage;
		} catch (Exception ex){
			throw ex;
		}
		
		
	}

	public ArrayList<ListProp> requestListProperties(String accessTk) throws Exception {
		
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<ListProp> aListProp= cacheLoader.getListProp(accessTk);
			return aListProp;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<Property> requestCustomerProperties(String accessTk) throws Exception {
		
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Property> aProperty = cacheLoader.getProperty(accessTk);
			return aProperty;
		} catch (Exception ex){
			throw ex;
		}
	
		

	}

	public ArrayList<ListFund> requestListFund(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<ListFund> aListFund = cacheLoader.getListFund(accessTk);
			return aListFund;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<ExternalPaymentType> requestExternalPaymentTypes(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<ExternalPaymentType> paymentList = cacheLoader.getExternalPaymentList(accessTk);
			return paymentList;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<Fund> requestFund(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Fund> aFund = cacheLoader.getFund(accessTk);
			return aFund;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<Amount> requestAmount(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Amount> aAmount = cacheLoader.getAmount(accessTk);
			return aAmount;
		} catch (Exception ex){
			throw ex;
		}		
	}
	
	public ArrayList<Language> requestLanguages(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Language> aAmount = cacheLoader.getLanguages(accessTk);
			return aAmount;
		} catch (Exception ex){
			throw ex;
		}		
	}

	public ArrayList<FundImage> requestFundImage(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<FundImage> aFundImage = cacheLoader.getFundImage(accessTk);
			return aFundImage;
		} catch (Exception ex){
			throw ex;
		}		
	}
	
	public ArrayList<Country> requestCountry(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<Country> aCountry = cacheLoader.getCountry(accessTk);
			return aCountry;
		} catch (Exception ex){
			throw ex;
		}
	}

	public CustomerUser authenticateUser(String username, String password, HttpServletRequest ip, String au, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			CustomerUser result = rws.authenticateUser(username, password, ip, au, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					CustomerUser result = rws.authenticateUser(username, password, ip, au, aCCESS_TOKEN);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
			
		}
	}

	public boolean verifyUser(String email, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			boolean result = rws.verifyUser(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public HashMap<String, Object> verifyUserForRegister(String email, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			HashMap<String, Object> result = rws.verifyUserForRegister(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			CustomerNewUser result = rws.registerNewUser(newUser, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public CustomerUser authenticateExternalUser(String aCCESS_TOKEN, AuthenticateExternalUser aUser) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			CustomerUser result = rws.authenticateExternalUser(aCCESS_TOKEN, aUser);
			return result;
		} catch (Exception ex){
			throw ex;
		}

	}

	public AuthorizationInfo creditCardPurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData,
			PaymentGwParameters paymentGwParameters, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.creditCardPurchase(aCCESS_TOKEN, txDetailList, txUserData, paymentGwParameters, hsr, currU);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public HashMap<String, Object> freePurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData, String source, String paramUA, int langId, 
			String coupon, HttpServletRequest hsr, CustomerUser currU, String comments, Long startDate, boolean checkStartDate) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.freePurchase(aCCESS_TOKEN, txDetailList, txUserData, source, paramUA, langId, coupon, hsr, currU, comments, startDate, checkStartDate);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public AjaxResponseBody externalPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, String medium, String source, int lang, 
			TxExternalPayment txPaymentData, String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU, Long startDate,boolean checkStartDate) throws Exception {
		try{
			RequestWS rws = new RequestWS();
			return rws.externalPurchase(aCCESS_TOKEN, txDetail, txUserData, medium, source, lang,  txPaymentData, coupon,  operatorId,  comments, hsr, currU,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					AjaxResponseBody result = rws.externalPurchase(aCCESS_TOKEN, txDetail, txUserData, medium, source, lang,  txPaymentData, coupon,  operatorId,  comments, hsr, currU,startDate,checkStartDate);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}

	public AuthorizationInfo assistedCCPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, PaymentGwParameters paymentGwParameters, int paymentGatewayId, String medium, String source,
			int lang, String coupon, int operatorId, String comments,  HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.assistedCCPurchase(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, lang, coupon,
					  operatorId,  comments, hsr, currU,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					AuthorizationInfo result = rws.assistedCCPurchase(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, lang, coupon,
							  operatorId,  comments, hsr, currU,startDate,checkStartDate);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	public AuthorizationInfo assistedCCPurchaseSplitPayment(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, PaymentGwParameters paymentGwParameters, int paymentGatewayId, String medium, String source,
			int lang, String coupon, int operatorId, String comments,  HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.assistedCCPurchaseSplitPayment(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, lang, coupon,
					  operatorId,  comments, hsr, currU,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					AuthorizationInfo result = rws.assistedCCPurchaseSplitPayment(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, lang, coupon,
							  operatorId,  comments, hsr, currU,startDate,checkStartDate);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	
	public PaymentGateways requestPaymentGateway(String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.allPaymentMethodTDC(aCCESS_TOKEN);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<PaymentTypeDescription> listAllPaymentTypeDescriptions(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<PaymentTypeDescription> aPaymentTypeDescription = cacheLoader.getPaymentTypeDescription(accessTk);
			return aPaymentTypeDescription;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<ReportDetail> reportDetail(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.listReport(accessTk,filter, hsr, currU);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<ReportDetail> globalReportDetail(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.listGlobalReport(accessTk,filter, hsr, currU);			
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<PaymentGatewaysDD> listAllPaymentGateways(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<PaymentGatewaysDD> aPaymentGateways = cacheLoader.getListAllPaymentGateways(accessTk);
			return aPaymentGateways;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<TransactionStatus> listTxStatus(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<TransactionStatus> txStatus = cacheLoader.getListTxStatus(accessTk);
			return txStatus;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<CustomerApplications> listCApp(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<CustomerApplications> cApp = cacheLoader.getListCApp(accessTk);
			return cApp;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<TxSources> listTxSources(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<TxSources> txSources = cacheLoader.getListTxSources(accessTk);
			return txSources;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<TxCampaing> listTxCampaing(String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			ArrayList<TxCampaing> txCampaing = cacheLoader.getListTxCampaing(accessTk);
			return txCampaing;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<HashMap<String, Object>> customerFinancesReport (String aCCESS_TOKEN, HashMap<String, Object> filter) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.customerFinancesReport(aCCESS_TOKEN,filter);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public HashMap<String,Object> getApplicationParameters (String accessTk) throws Exception {
		try {
			CacheLoader cacheLoader = CacheLoader.getInstance();
			HashMap<String,Object> appParam = cacheLoader.getApplicationParameters(accessTk);
			return appParam;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response validateCoupon (String aCCESS_TOKEN,String code, int purchaseAmount, String applier, boolean activateCoupon, String userAgent, String ipAddress, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.validateCoupon(aCCESS_TOKEN, code, purchaseAmount, applier, activateCoupon, userAgent, ipAddress, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response registerCustomersList (FormMultiCustomerLoadDTO form, ArrayList<CsvFileModel> csvData, String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.registerCustomersList(form, csvData, ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public AjaxResponseBody registerRetrictEvent ( ArrayList<CsvModelRestrict> csvData, String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			AjaxResponseBody response = rws.registerRestrictEvent( csvData, ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<RestrictedUser> getRestrictedUsers( String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			ArrayList<RestrictedUser> response = rws.getRestrictedUsers( ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response editRestrictedUser (  String ACCESS_TOKEN, RestrictedUser user, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.editRestrictedUser(  ACCESS_TOKEN, user, hsr, currU );
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response deleteRestrictedUser (  String ACCESS_TOKEN, Integer userId, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.deleteRestrictedUser(  ACCESS_TOKEN, userId, hsr, currU );
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response getEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.getEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response updateEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.updateEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response removeEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.removeEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response addEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response response = rws.addEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<CustomerUser> searchUsers(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			return rws.searchUsers(accessTk,filter, hsr, currU);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response validateDevice(String  ACCESS_TOKEN, String email, String userName,  int DEVICE_TYPE_ID, int langId, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.validateDevice(ACCESS_TOKEN, email, userName, DEVICE_TYPE_ID, langId, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response validateCodeAndEdit(String  ACCESS_TOKEN, int userId, int validationId, String validationCode, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.validateCodeAndEdit(ACCESS_TOKEN, userId, validationId, validationCode, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public Response editWithNoConfirm(String ACCESS_TOKEN, int userId, String emailNew, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.editWithNoConfirm(ACCESS_TOKEN, userId, emailNew, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response editUserRegistered(String  ACCESS_TOKEN, CustomerUser userForEdit, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.editUserRegistered(ACCESS_TOKEN, userForEdit, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public AjaxResponseReport<CustomerTransaction> getUserTransactions(String  ACCESS_TOKEN, int userId, String ipAddress, String userAgent,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			AjaxResponseReport<CustomerTransaction> result = rws.getUserTransactions(ACCESS_TOKEN, userId,ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public AjaxResponseReport<CustomerTransaction> getAllUserTransactions(String  ACCESS_TOKEN, int userId, String ipAddress, String userAgent,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			AjaxResponseReport<CustomerTransaction> result = rws.getAllUserTransactions(ACCESS_TOKEN, userId,ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response resendUserReceiptTx(String ACCESS_TOKEN, int userId, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		RequestWS dm = new RequestWS();
		try {
			Response result = dm.resendUserReceiptTx(ACCESS_TOKEN, userId, txId, ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				try {
					Response result = dm.resendUserReceiptTx(ACCESS_TOKEN, userId, txId, ipAddress, userAgent, hsr, currU);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	public Response sendMagicCustomerLink(String ACCESS_TOKEN, int userId, String email, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.sendMagicCustomerLink(ACCESS_TOKEN, userId, email, txId, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					Response result = rws.sendMagicCustomerLink(ACCESS_TOKEN, userId, email, txId, ipAddress,userAgent, hsr, currU);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	public Response sendTemporyPasswodReset(String ACCESS_TOKEN, int customerUserId, String email, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.sendTemporyPasswodReset(ACCESS_TOKEN, customerUserId, email, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					Response result = rws.sendTemporyPasswodReset(ACCESS_TOKEN, customerUserId, email, ipAddress,userAgent, hsr, currU);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	public Response invalidateTx(String ACCESS_TOKEN, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.invalidateTx(ACCESS_TOKEN, txId, commentary,hsr , currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response invalidateTx(String ACCESS_TOKEN, int customerUserId, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.invalidateTx(ACCESS_TOKEN, customerUserId, txId, commentary,hsr , currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response getComissionSettings(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.getComissionSettings(ACCESS_TOKEN, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response getBlackList(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.getBlackList(ACCESS_TOKEN, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response unBlockFromBlackList(String ACCESS_TOKEN, int id, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.unBlockFromBlackList(ACCESS_TOKEN, id, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public AjaxResponseReport<UserAccess> getUserAccessList(String ACCESS_TOKEN, String email,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			AjaxResponseReport<UserAccess> result = dm.getUserAccessList(ACCESS_TOKEN, email, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response resendUserReceiptOp(String ACCESS_TOKEN, int userId, int txId, String adminEmail, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		RequestWS dm = new RequestWS();
		try {
			Response result = dm.resendUserReceiptOp(ACCESS_TOKEN, userId, txId, adminEmail, hsr, currU);
			return result;
		} catch (Exception ex) {
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				try {
					Response result = dm.resendUserReceiptOp(ACCESS_TOKEN, userId, txId, adminEmail, hsr, currU);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
	public ArrayList<Audit> getAppLogList(String ACCESS_TOKEN, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			ArrayList<Audit> result = dm.getAppLogList(ACCESS_TOKEN, filter, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response getAllLanguages(String ACCESS_TOKEN) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.getAllLanguages(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response getAllAssets(String ACCESS_TOKEN) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.getAllAssets(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response getEventsAssetsByLang(String ACCESS_TOKEN, int lang_id) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.getEventsAssetsByLang(ACCESS_TOKEN,  lang_id);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response addSetting(String ACCESS_TOKEN, ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.addSetting(ACCESS_TOKEN,  assets, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response updateSetting(String ACCESS_TOKEN, ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.updateSetting(ACCESS_TOKEN,  assets, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response approveSignature(String ACCESS_TOKEN,  int customerId, long transaction, String comment, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.approveSignature(ACCESS_TOKEN,  customerId, transaction, comment, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public HashMap<String, Object> verifyUser2(String email, String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			HashMap<String, Object> result = rws.verifyUser2(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public AjaxResponseBodyHash2 verifyUserAddAttendee(String email, String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			return rws.verifyUserAddAttendee(email, aCCESS_TOKEN);
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response getOnlineUsers(String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.getOnlineUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response getEventDetailsregisteredUsers(String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.getEventDetailsregisteredUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response getEventDetailsOnlineUsers(String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.getEventDetailsOnlineUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}	 
	
	public Response addBlacklistItem(String data ,int dataTypeId, String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.addBlacklistItem(data, dataTypeId, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response updateReportingTables(String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.updateReportingTables(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response reportUserWireFraud(int customerUserId, int txId, String aCCESS_TOKEN) {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.reportUserWireFraud(customerUserId, txId, aCCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response transferTransactionToAnotherUser(String emailOrigin, String emailToTransfer, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			Response result = rws.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, aCCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			CustomerNewUser result = rws.registerNewUser(newUser, ipUser, userAgent, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public CustomerNewUser updateNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String aCCESS_TOKEN) throws Exception {
		try {
			RequestWS rws = new RequestWS();
			CustomerNewUser result = rws.updateNewUser(newUser, ipUser, userAgent, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response sendMagicLinkOperator(String ACCESS_TOKEN, int userId, String email, String emailOp, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			RequestWS dm = new RequestWS();
			Response result = dm.sendMagicLinkOperator(ACCESS_TOKEN, userId, email, emailOp, txId, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			String msgExp = ex.getMessage();
			if(msgExp.equals("exceeded-users")) {
				
				try {
					RequestWS rws = new RequestWS();
					Response result = rws.sendMagicLinkOperator(ACCESS_TOKEN, userId, email, emailOp, txId, ipAddress,userAgent, hsr, currU);
					return result;
				} catch (Exception e) {
					throw ex;
				}
				
			} else {
				throw ex;
			}
		}
	}
	
}
