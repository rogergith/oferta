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
import com.weavx.web.model.PaymentGateways;
import com.weavx.web.model.ExternalPaymentType;
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

public class DonationDelegate {

	public String requestAccessToken(int option) throws Exception {
		DonationManager dm = new DonationManager();
		String result = dm.requestAccessToken(option);
		return result;
	}
	public ArrayList<Language> requestLanguage(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Language> result = dm.requestLanguage(accessTk);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<ListProp> requestListProperties(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<ListProp> result = dm.requestListProperties(accessTk);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<Property> requestCustomerProperties(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Property> result = dm.requestCustomerProperties(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}

	}
	public ArrayList<ListFund> requestListFund(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<ListFund> result = dm.requestListFund(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}		
	}

	// ajsb: List payment types
	public ArrayList<ExternalPaymentType> requestExternalPaymentTypes(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<ExternalPaymentType> result = dm.requestExternalPaymentTypes(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}		
	}

	public ArrayList<Fund> requestFund(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Fund> result = dm.requestFund(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}
	}
	public ArrayList<Amount> requestAmount(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Amount> result = dm.requestAmount(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}
	}
	public ArrayList<Language> requestLanguages(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Language> result = dm.requestLanguages(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}
	}
	public ArrayList<FundImage> requestFundImage(String accessTk) throws Exception  {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<FundImage> result = dm.requestFundImage(accessTk);
			return result;
		}catch (Exception ex){
			throw ex;
		}
	}
	public ArrayList<Country> requestCountry(String accessTk) throws Exception  {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Country> result = dm.requestCountry(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}		
	}
	public CustomerUser authenticateUser(String username, String password, HttpServletRequest ip, String au, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			CustomerUser result = dm.authenticateUser(username, password, ip, au, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	public boolean verifyUser(String email, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			boolean result = dm.verifyUser(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public HashMap<String, Object> verifyUserForRegister(String email, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			HashMap<String, Object> result = dm.verifyUserForRegister(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			CustomerNewUser result = dm.registerNewUser(newUser, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	public CustomerUser authenticateExternalUser(String aCCESS_TOKEN, AuthenticateExternalUser aUser) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			CustomerUser result = dm.authenticateExternalUser(aCCESS_TOKEN, aUser);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	public AuthorizationInfo creditCardPurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData,
			PaymentGwParameters paymentGwParameters, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			return dm.creditCardPurchase(aCCESS_TOKEN, txDetailList, txUserData, paymentGwParameters, hsr,  currU);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public HashMap<String, Object> freePurchase(String aCCESS_TOKEN, TxDetailList txDetailList, TxUserData txUserData, String source, String paramUA, 
			int langId, String coupon, HttpServletRequest hsr, CustomerUser currU, String comments,Long startDate,boolean checkStartDate) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			return dm.freePurchase(aCCESS_TOKEN, txDetailList, txUserData, source, paramUA, langId, coupon, hsr, currU, comments,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public AjaxResponseBody externalPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, String medium, String source, int lang, 
			TxExternalPayment txPaymentData, String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			return dm.externalPurchase(aCCESS_TOKEN, txDetail, txUserData, medium, source, lang, txPaymentData, coupon,  operatorId,  comments, hsr, currU, startDate, checkStartDate);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public AuthorizationInfo assistedCCPurchase(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, PaymentGwParameters paymentGwParameters, 
			int paymentGatewayId, String medium, String source, int lang, String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU,Long startDate,boolean checkStartDate) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			return dm.assistedCCPurchase(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, lang, coupon,  operatorId,  comments, hsr, currU,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public AuthorizationInfo assistedCCPurchaseSplitPayment(String aCCESS_TOKEN, TxDetail txDetail, TxUserData txUserData, PaymentGwParameters paymentGwParameters, 
			int paymentGatewayId, String medium, String source, int lang, String coupon, int operatorId, String comments, HttpServletRequest hsr, CustomerUser currU
			,Long startDate,boolean checkStartDate) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			return dm.assistedCCPurchaseSplitPayment(aCCESS_TOKEN, txDetail, txUserData, paymentGwParameters, paymentGatewayId, medium, source, 
					lang, coupon,  operatorId,  comments, hsr, currU,startDate,checkStartDate);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}


	public PaymentGateways requestPaymentGateway(String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			return dm.requestPaymentGateway(aCCESS_TOKEN);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	public ArrayList<PaymentTypeDescription> listAllPaymentTypeDescriptions(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<PaymentTypeDescription> result = dm.listAllPaymentTypeDescriptions(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<ReportDetail> reportDetail(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<ReportDetail> result = dm.reportDetail(accessTk, filter, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<ReportDetail> globalReportDetail(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<ReportDetail> result = dm.globalReportDetail(accessTk, filter, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<PaymentGatewaysDD> listAllPaymentGateways(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<PaymentGatewaysDD> result = dm.listAllPaymentGateways(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<TransactionStatus> listTxStatus(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<TransactionStatus> result = dm.listTxStatus(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<CustomerApplications> listCApp(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<CustomerApplications> result = dm.listCApp(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<TxSources> requestTxSources(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<TxSources> result = dm.listTxSources(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public ArrayList<TxCampaing> requestTxCampaing(String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<TxCampaing> result = dm.listTxCampaing(accessTk);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public ArrayList<HashMap<String, Object>> customerFinancesReport (String aCCESS_TOKEN, HashMap<String, Object> filter) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			return dm.customerFinancesReport(aCCESS_TOKEN,filter);
			//return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public HashMap<String,Object> getApplicationParameters (String accessTk) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			HashMap<String,Object> appParam = dm.getApplicationParameters(accessTk);
			return appParam;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response validateCoupon (String aCCESS_TOKEN, String code, int purchaseAmount, String applier, boolean activateCoupon, String userAgent, 
			String ipAddress,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response couponDisccount = dm.validateCoupon(aCCESS_TOKEN, code, purchaseAmount, applier, activateCoupon, userAgent, ipAddress, hsr, currU);
			return couponDisccount;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response registerCustomersList (FormMultiCustomerLoadDTO form, ArrayList<CsvFileModel> csvData, String ACCESS_TOKEN,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.registerCustomersList(form, csvData, ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public AjaxResponseBody registerRestrictEvent ( ArrayList<CsvModelRestrict> csvData, String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			AjaxResponseBody response = dm.registerRetrictEvent( csvData, ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<RestrictedUser> getRestrictedUsers( String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<RestrictedUser> response = dm.getRestrictedUsers( ACCESS_TOKEN, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response editRestrictedUser (  String ACCESS_TOKEN, RestrictedUser user, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.editRestrictedUser(  ACCESS_TOKEN, user, hsr, currU );
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response deleteRestrictedUser (  String ACCESS_TOKEN, Integer userId, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.deleteRestrictedUser(  ACCESS_TOKEN, userId, hsr, currU );
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response getEventSettings(String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.getEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response updateEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.updateEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response removeEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.removeEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response addEventSettings( String ACCESS_TOKEN, EventFundSettings eventFundSettings, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response response = dm.addEventSettings( ACCESS_TOKEN, eventFundSettings, hsr, currU);
			return response;
		} catch (Exception ex){
			throw ex;
		}
	}

	public ArrayList<CustomerUser> searchUsers(String accessTk, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<CustomerUser> result = dm.searchUsers(accessTk, filter, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response validateDevice(String  ACCESS_TOKEN, String email, String userName,  int DEVICE_TYPE_ID, int langId, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.validateDevice(ACCESS_TOKEN, email, userName, DEVICE_TYPE_ID, langId, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response validateCodeAndEdit(String  ACCESS_TOKEN, int userId, int validationId, String validationCode, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.validateCodeAndEdit(ACCESS_TOKEN, userId, validationId, validationCode, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response editWithNoConfirm(String ACCESS_TOKEN, int userId, String emailNew, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.editWithNoConfirm(ACCESS_TOKEN, userId, emailNew, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response editUserRegistered(String  ACCESS_TOKEN, CustomerUser userForEdit, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.editUserRegistered(ACCESS_TOKEN, userForEdit, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public AjaxResponseReport<CustomerTransaction> getUserTransactions(String  ACCESS_TOKEN, int userId, String ipAddress, String userAgent,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			AjaxResponseReport<CustomerTransaction> result = dm.getUserTransactions(ACCESS_TOKEN, userId,ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public AjaxResponseReport<CustomerTransaction> getAllUserTransactions(String  ACCESS_TOKEN, int userId, String ipAddress, String userAgent,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			AjaxResponseReport<CustomerTransaction> result = dm.getAllUserTransactions(ACCESS_TOKEN, userId,ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response resendUserReceiptTx(String ACCESS_TOKEN, int userId, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.resendUserReceiptTx(ACCESS_TOKEN, userId, txId, ipAddress, userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public Response sendMagicCustomerLink(String ACCESS_TOKEN, int userId, String email, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.sendMagicCustomerLink(ACCESS_TOKEN, userId, email, txId, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response sendTemporyPasswodReset(String ACCESS_TOKEN, int customerUserId, String email, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.sendTemporyPasswodReset(ACCESS_TOKEN, customerUserId, email, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response invalidateTx(String ACCESS_TOKEN, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.invalidateTx(ACCESS_TOKEN, txId, commentary, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response invalidateTx(String ACCESS_TOKEN, int customerUserId, int txId, String commentary,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.invalidateTx(ACCESS_TOKEN, customerUserId, txId, commentary, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response getComissionSettings(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getComissionSettings(ACCESS_TOKEN, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response getBlackList(String ACCESS_TOKEN, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getBlackList(ACCESS_TOKEN, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response unBlockFromBlackList(String ACCESS_TOKEN, int id, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.unBlockFromBlackList(ACCESS_TOKEN, id, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public AjaxResponseReport<UserAccess> getUserAccessList(String ACCESS_TOKEN, String email,  HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			AjaxResponseReport<UserAccess> result = dm.getUserAccessList(ACCESS_TOKEN, email, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response resendUserReceiptOp(String ACCESS_TOKEN, int userId, int txId, String adminEmail,HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.resendUserReceiptOp(ACCESS_TOKEN, userId, txId, adminEmail, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public ArrayList<Audit> getAppLogList(String ACCESS_TOKEN, HashMap<String, Object> filter, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			ArrayList<Audit> result = dm.getAppLogList(ACCESS_TOKEN, filter, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response getAllLanguages(String ACCESS_TOKEN) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getAllLanguages(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response getAllAssets(String ACCESS_TOKEN) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getAllAssets(ACCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}


	public Response getEventsAssetsByLang(String ACCESS_TOKEN, int lang_id) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getEventsAssetsByLang(ACCESS_TOKEN,  lang_id);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response addSetting(String ACCESS_TOKEN, ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.addSetting(ACCESS_TOKEN,  assets, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response updateSetting(String ACCESS_TOKEN,  ArrayList<Map<String, Object>> assets, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.updateSetting(ACCESS_TOKEN,  assets, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Response approveSignature(String ACCESS_TOKEN,  int customerId, long transaction, String comment, HttpServletRequest hsr,
			CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.approveSignature(ACCESS_TOKEN,  customerId, transaction, comment, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	public HashMap<String, Object> verifyUser2(String email, String aCCESS_TOKEN) {
		try{
			DonationManager dm = new DonationManager();
			HashMap<String, Object> result = dm.verifyUser2(email, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public AjaxResponseBodyHash2 verifyUserAddAttendee(String email, String aCCESS_TOKEN) {
		try{
			DonationManager dm = new DonationManager();
			return dm.verifyUserAddAttendee(email, aCCESS_TOKEN);
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response getOnlineUsers(String aCCESS_TOKEN) {
		try{
			DonationManager dm = new DonationManager();
			Response result = dm.getOnlineUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response addBlacklistItem(String data ,int dataTypeId, String aCCESS_TOKEN) {
		try{
			DonationManager dm = new DonationManager();
			Response result = dm.addBlacklistItem(data, dataTypeId, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response updateReportingTables(String aCCESS_TOKEN) {
		try{
			DonationManager dm = new DonationManager();
			Response result = dm.updateReportingTables(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response getEventDetailsregisteredUsers(String aCCESS_TOKEN) {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getEventDetailsregisteredUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}

	public Response getEventDetailsOnlineUsers(String aCCESS_TOKEN) {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.getEventDetailsOnlineUsers(aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	public Response reportUserWireFraud(int customerUserId, int txId, String aCCESS_TOKEN) {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.reportUserWireFraud(customerUserId, txId, aCCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public Response transferTransactionToAnotherUser(String emailOrigin, String emailToTransfer, String aCCESS_TOKEN) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.transferTransactionToAnotherUser(emailOrigin,emailToTransfer, aCCESS_TOKEN);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public CustomerNewUser registerNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			CustomerNewUser result = dm.registerNewUser(newUser, ipUser, userAgent, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public CustomerNewUser updateNewUser(RegisterNewUser newUser, String ipUser, String userAgent, String aCCESS_TOKEN) throws Exception {
		try{
			DonationManager dm = new DonationManager();
			CustomerNewUser result = dm.updateNewUser(newUser,  ipUser, userAgent, aCCESS_TOKEN);
			return result;
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Response sendMagicLinkOperator(String ACCESS_TOKEN, int userId, String email, String emailOp, int txId, String ipAddress, String userAgent, HttpServletRequest hsr, CustomerUser currU) throws Exception {
		try {
			DonationManager dm = new DonationManager();
			Response result = dm.sendMagicLinkOperator(ACCESS_TOKEN, userId, email, emailOp, txId, ipAddress,userAgent, hsr, currU);
			return result;
		} catch (Exception ex) {
			throw ex;
		}
	}

}
