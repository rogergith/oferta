package com.weavx.web.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.weavx.web.model.Amount;
import com.weavx.web.model.Country;
import com.weavx.web.model.CustomerApplications;
import com.weavx.web.model.Fund;
import com.weavx.web.model.FundImage;
import com.weavx.web.model.Language;
import com.weavx.web.model.ListFund;
import com.weavx.web.model.ListProp;
import com.weavx.web.model.PaymentGatewaysDD;
import com.weavx.web.model.ExternalPaymentType;
import com.weavx.web.model.PaymentTypeDescription;
import com.weavx.web.model.Property;
import com.weavx.web.model.TransactionStatus;
import com.weavx.web.model.TxCampaing;
import com.weavx.web.model.TxSources;

public class CacheLoader {

	private static ArrayList<Language> aLanguages;
	private static ArrayList<Language> aLanguage;
	private static ArrayList<ListProp> aListProp;
	private static ArrayList<Property>  aProperty;
	private static ArrayList<ListFund> aListFund;
	private static ArrayList<Fund> aFund;
	private static ArrayList<Amount> aAmount;
	private static ArrayList<FundImage> aFundImage;
	private static ArrayList<Country> aCountry;
	private static ArrayList<PaymentTypeDescription> paymentTypeDescription;
	private static ArrayList<PaymentGatewaysDD> paymentGatewaysDD;
	private static ArrayList<TransactionStatus> txStatus;
	private static ArrayList<CustomerApplications> cApp;
	private static ArrayList<TxSources> txSources;
	private static ArrayList<TxCampaing> txCampaing;
	private static HashMap<String, Object> appParam;
	private static ArrayList<ExternalPaymentType> paymentList;
	
	private static CacheLoader instance = null;
	private CacheLoader() {

	}
	public static CacheLoader getInstance() {
		return getFreshInstance();
	}

	public static CacheLoader getFreshInstance() {
		aLanguage = null;
		aListProp = null;
		aProperty = null;
		aListFund = null;
		aFund = null;
		aAmount = null;
		aFundImage = null;
		paymentTypeDescription = null;
		paymentGatewaysDD = null;
		txStatus = null;
		cApp = null;
		txSources = null;
		txCampaing = null;
		appParam = null;

		instance = new CacheLoader(); 
		return instance;
		
	
	}

	public ArrayList<Language> getLanguage(String accessTk) throws Exception {
		try {
			if (aLanguage == null){
				aLanguage = setLanguage(accessTk);
			}
			return aLanguage;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<Language> setLanguage(String accessTk) throws Exception {
		try {
			ArrayList<Language> lan;
			RequestWS rws = new RequestWS();
			lan = rws.getLanguages(accessTk);
			return lan;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<ListProp> getListProp(String accessTk) throws Exception {
		try {
			if (aListProp == null){
				aListProp = setListProp(accessTk);
			}
			return aListProp;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<ListProp> setListProp(String accessTk) throws Exception {
		try {
			ArrayList<ListProp> aListProp;
			RequestWS rws = new RequestWS();
			aListProp = rws.getListProperties(accessTk);
			return aListProp;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<Property> getProperty(String accessTk) throws Exception {
		try {
			if (aProperty == null){
				aProperty= setProperty(accessTk);
			}
			return aProperty;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<Property> setProperty(String accessTk) throws Exception {
		try {
			ArrayList<Property> aProperty;
			RequestWS rws = new RequestWS();
			aProperty = rws.getCustomerProperties(accessTk);
			return aProperty;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<ListFund> getListFund(String accessTk) throws Exception {
		try {
			if (aListFund == null){
				aListFund= setListFund(accessTk);
			}
			return aListFund;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<ListFund> setListFund(String accessTk) throws Exception {
		try {
			ArrayList<ListFund> aListFund;
			RequestWS rws = new RequestWS();
			aListFund = rws.getListFund(accessTk);
			return aListFund;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<ExternalPaymentType> getExternalPaymentList(String accessTk) throws Exception {
		try {
			if (paymentList == null){
				paymentList= setExternalPaymentList(accessTk);
			}
			return paymentList;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	private ArrayList<ExternalPaymentType> setExternalPaymentList(String accessTk) throws Exception {
		try {
			ArrayList<ExternalPaymentType> paymentList;
			RequestWS rws = new RequestWS();
			paymentList = rws.getExternalPaymentList(accessTk);
			return paymentList;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<Fund> getFund(String accessTk) throws Exception {
		try {
			if (aFund == null){
				aFund= setFund(accessTk);
			}
			return aFund;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<Fund> setFund(String accessTk) throws Exception {
		try {
			ArrayList<Fund> aFund;
			RequestWS rws = new RequestWS();
			aFund = rws.getFund(accessTk);
			return aFund;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<Amount> getAmount(String accessTk) throws Exception {
		try {
			if (aAmount == null){
				aAmount= setAmount(accessTk);
			}
			return aAmount;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	public ArrayList<Language> getLanguages(String accessTk) throws Exception {
		try {
			//if (aLanguages == null){
				aLanguages= setLanguages(accessTk);
			//}
			return aLanguages;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<Amount> setAmount(String accessTk) throws Exception {
		try {
			ArrayList<Amount> aAmount;
			RequestWS rws = new RequestWS();
			aAmount = rws.getAmount(accessTk);
			return aAmount;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	private ArrayList<Language> setLanguages(String accessTk) throws Exception {
		try {
			ArrayList<Language> aAmount;
			RequestWS rws = new RequestWS();
			aAmount = rws.getLanguagesByCustomer(accessTk);
			return aAmount;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<FundImage> getFundImage(String accessTk) throws Exception {
		try {
			if (aFundImage == null){
				aFundImage= setFundImage(accessTk);
			}
			return aFundImage;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<FundImage> setFundImage(String accessTk) throws Exception {
		try {
			ArrayList<FundImage> aFundImage;
			RequestWS rws = new RequestWS();
			aFundImage = rws.getFundImage(accessTk);
			return aFundImage;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public ArrayList<Country> getCountry(String accessTk) throws Exception {
		try {
			if (aCountry == null){
				aCountry = setCountry(accessTk);
			}
			return aCountry;
		}catch (Exception ex){
			throw ex;
		}
	}

	private ArrayList<Country> setCountry(String accessTk) throws Exception {
		try {
			ArrayList<Country> aCountry;
			RequestWS rws = new RequestWS();
			aCountry = rws.getListAllContries(accessTk);
			return aCountry;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<PaymentTypeDescription> getPaymentTypeDescription(String accessTk) throws Exception {
		try {
			if (paymentTypeDescription == null){
				paymentTypeDescription = setPaymentTypeDescription(accessTk);
			}
			return paymentTypeDescription;
		}catch (Exception ex){
			throw ex;
		}
	}
	private ArrayList<PaymentTypeDescription> setPaymentTypeDescription(String accessTk) throws Exception {
		try {
			ArrayList<PaymentTypeDescription> aPaymentTypeDescription;
			RequestWS rws = new RequestWS();
			aPaymentTypeDescription = rws.listAllPaymentTypeDescriptions(accessTk);
			return aPaymentTypeDescription;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<PaymentGatewaysDD> getListAllPaymentGateways(String accessTk) throws Exception {
		try {
			if (paymentGatewaysDD == null){
				paymentGatewaysDD = setListAllPaymentGateways(accessTk);
			}
			return paymentGatewaysDD;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	private ArrayList<PaymentGatewaysDD> setListAllPaymentGateways(String accessTk) throws Exception {
		try {
			ArrayList<PaymentGatewaysDD> aPaymentGatewaysDD;
			RequestWS rws = new RequestWS();
			aPaymentGatewaysDD = rws.listAllPaymentGateways(accessTk);
			return aPaymentGatewaysDD;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<TransactionStatus> getListTxStatus(String accessTk) throws Exception {
		try {
			if (txStatus == null){
				txStatus = setListTxStatus(accessTk);
			}
			return txStatus;
		}catch (Exception ex){
			throw ex;
		}
	}
	private ArrayList<TransactionStatus> setListTxStatus(String accessTk) throws Exception {
		try {
			ArrayList<TransactionStatus> aTxStatus;
			RequestWS rws = new RequestWS();
			aTxStatus = rws.listTxStatus(accessTk);
			return aTxStatus;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<CustomerApplications> getListCApp(String accessTk) throws Exception {
		try {
			if (cApp == null){
				cApp = setListCApp(accessTk);
			}
			return cApp;
		}catch (Exception ex){
			throw ex;
		}
	}
	private ArrayList<CustomerApplications> setListCApp(String accessTk) throws Exception {
		try {
			ArrayList<CustomerApplications> aCApp;
			RequestWS rws = new RequestWS();
			aCApp = rws.listCustomerApplications(accessTk);
			return aCApp;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<TxSources> getListTxSources(String accessTk) throws Exception {
		try {
			if (txSources == null){
				txSources = setListTxSources(accessTk);
			}
			return txSources;
		}catch (Exception ex){
			throw ex;
		}
	}
	private ArrayList<TxSources> setListTxSources(String accessTk) throws Exception {
		try {
			ArrayList<TxSources> atxSources;
			RequestWS rws = new RequestWS();
			atxSources = rws.listTxSource(accessTk);
			return atxSources;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	public ArrayList<TxCampaing> getListTxCampaing(String accessTk) throws Exception {
		try {
			if (txCampaing == null){
				txCampaing = setListTxCampaing(accessTk);
			}
			return txCampaing;
		}catch (Exception ex){
			throw ex;
		}
	}
	
	private ArrayList<TxCampaing> setListTxCampaing(String accessTk) throws Exception {
		try {
			ArrayList<TxCampaing> aTxCampaing;
			RequestWS rws = new RequestWS();
			aTxCampaing = rws.listTxCampaing(accessTk);
			return aTxCampaing;
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw ex;
		}
	}
	
	public HashMap<String, Object> getApplicationParameters(String accessTk) throws Exception {
		try {
			appParam = setApplicationParameters(accessTk);
			return appParam;
		}catch (Exception ex){
			throw ex;
		}
	}
	private HashMap<String, Object> setApplicationParameters(String accessTk) throws Exception {
		try {
			HashMap<String, Object> appParam;
			RequestWS rws = new RequestWS();
			appParam = rws.getApplicationParameters(accessTk);
			return appParam;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
