package com.weavx.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weavx.business.DonationDelegate;
import com.weavx.web.model.Amount;
import com.weavx.web.model.Country;
import com.weavx.web.model.CustomerApplications;
import com.weavx.web.model.CustomerUser;
import com.weavx.web.model.Fund;
import com.weavx.web.model.FundImage;
import com.weavx.web.model.Language;
import com.weavx.web.model.ListFund;
import com.weavx.web.model.ListProp;
import com.weavx.web.model.PaymentGateways;
import com.weavx.web.model.PaymentGatewaysDD;
import com.weavx.web.model.PaymentTypeDescription;
import com.weavx.web.model.Property;
import com.weavx.web.model.Response;
import com.weavx.web.model.RestrictedUser;
import com.weavx.web.model.TransactionStatus;
import com.weavx.web.model.TxCampaing;
import com.weavx.web.model.TxSources;
import com.weavx.web.model.UserInfo;
import com.weavx.web.utils.ExpiredAccessTokenException;
import com.weavx.web.utils.JSONRequestFactory;
import com.weavx.web.utils.RequestWS;
import com.weavx.web.model.ExternalPaymentType;

@Controller
public class WelcomeController {
	ArrayList<Language> lan;
	ArrayList<Language> languajes;
	ArrayList<Property> prop;
	ArrayList<ListProp> listProperties;
	ArrayList<ListFund> listFund;
	ArrayList<Fund> fund;
	ArrayList<Amount> amount;
	ArrayList<FundImage> fundImage;
	ArrayList<Country> country;
	ArrayList<PaymentTypeDescription> paymentTypeDescription;
	PaymentGateways paymentGateway;
	ArrayList<PaymentGatewaysDD> paymentGatewaysDD;
	ArrayList<TransactionStatus> txStatusDD;
	ArrayList<CustomerApplications> cApp;
	ArrayList<TxSources> txSources;
	ArrayList<TxCampaing> txCampaing;
	ArrayList<ExternalPaymentType> paymentTypes;
	HashMap<String, Object> appParam = new HashMap<String, Object>();
	HashMap<Integer, String> listEvent = new HashMap<Integer, String>();
	HashMap<Integer, String> listEventHistory = new HashMap<Integer, String>();

	Logger log = LoggerFactory.getLogger(WelcomeController.class);

	public static final String RESTRICTED_BASE_VIEW = "restrictedevent/";

	private void listAllEvents(HttpSession session) {
		List<String> listEvenString = new ArrayList<String>();
		listEvenString.add("Suscripción Clases en vivo");
		
		List<String> listEvenStringHistory = new ArrayList<String>();

		RequestWS rws = new RequestWS();
		List<HashMap<String, Object>> response = new ArrayList<HashMap<String, Object>>();
		String ACCESS_TOKEN = "";
		try {
			ACCESS_TOKEN = rws.getAccessToken(1);
			try {
				response = rws.getAllEvents(JSONRequestFactory.BUSINESS_ADM,ACCESS_TOKEN);
			} catch (ExpiredAccessTokenException ex) {
				log.error("ERROR", ex);
				ACCESS_TOKEN =  rws.getAccessToken(0);
				response = rws.getAllEvents(JSONRequestFactory.BUSINESS_ADM,ACCESS_TOKEN);
			}
		} catch (Exception ex) {
			log.error("ERROR", ex);
		}
		
		int cont=0;
		boolean band = true;
		listEvenString.clear();
		while(cont<response.size() && band) {
			HashMap<String, Object> instance = response.get(cont);
			
			if("true".equals(instance.get("activeevent"))){
				listEvenString.add((String) instance.get("name")); 
				cont++;
			} else {
				band=false;
			}

		}
		
		listEventHistory.clear();
		for (int index = cont; index < response.size(); index++) {
			HashMap<String, Object> instance = response.get(index);
			listEvenStringHistory.add((String) instance.get("name")); 
				
		}
		
		listEvent.clear();
		listEventHistory.clear();
		int i = 1;
		for (String string : listEvenString) {
			listEvent.put(i, string);
			i++;
		}
		for (String string : listEvenStringHistory) {
			listEventHistory.put(i, string);
			i++;
		}
		try {
			rws.getAccessTokenAll(1, ACCESS_TOKEN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setAttribute("listEvent", listEvent);
		session.setAttribute("listEventHistory", listEventHistory);

	}

	@RequestMapping("/")
	public ModelAndView selectKey(HttpSession session, HttpServletRequest request) throws Exception {
		
		String accesstoken = (String) session.getAttribute("accesstoken");
		
		if(! (accesstoken != null) )
			refreshTokenOfert(session);
		
		try {
			allRequest(session);
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		
		long dateNow = System.currentTimeMillis();
		JSONObject amountByFund = new JSONObject();
		Iterator<ListFund> iterListFund = listFund.iterator();
		while (iterListFund.hasNext()) {
			ListFund listFundTmp = iterListFund.next();
			listFundTmp.setValidFrom(listFundTmp.getValidFrom());
			listFundTmp.setValidTo(listFundTmp.getValidTo());
			if (dateNow >= listFundTmp.getValidFrom() && dateNow <= listFundTmp.getValidTo()) {
				if (appParam.containsKey(String.valueOf(listFundTmp.getId()))
						&& amountByFund.isNull(String.valueOf(listFundTmp.getId()))) {
					Double amountTmp = Double.valueOf((String) appParam.get(String.valueOf(listFundTmp.getId())));
					amountByFund.put(String.valueOf(listFundTmp.getId()), amountTmp);
				}
			}
		}

		if (paymentGateway != null) {
			int gatewayMode = paymentGateway.getPaymentGatewayId();
			switch (gatewayMode) {
				case 2:
					session.setAttribute("stripeKey", paymentGateway.getAuthKey1());
					break;
				default:
					session.setAttribute("stripeKey", "");
					break;
			}
		}
		session.setAttribute("SHOW_SOURCE_MODAL", appParam.get("SHOW_SOURCE_MODAL"));
		session.setAttribute("amount", amount);
		session.setAttribute("amountByFund", amountByFund);
		session.setAttribute("paymentTypeDescription", paymentTypeDescription);
		session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
		session.setAttribute("txStatusDD", txStatusDD);
		session.setAttribute("cApp", cApp);
		session.setAttribute("listFund", listFund);
		session.setAttribute("languajes", languajes);
		session.setAttribute("txSources", txSources);
		session.setAttribute("txCampaing", txCampaing);
		session.setAttribute("externalPaymentTypes", paymentTypes);

		return new ModelAndView("oferta");
	}

	@RequestMapping("/login")
	public ModelAndView step1(HttpSession session) throws Exception {
		refreshToken(session);
		listAllEvents(session);
		try {			
			if (validAuthenticate(session)) {				
				return new ModelAndView("home");
			} else if (session.getAttribute("option")!=null) {
				session.setAttribute("title", listEvent.get((int)session.getAttribute("option")));
				return new ModelAndView("login");
			}else if((session.getAttribute("optionHistory")!=null)) {
				session.setAttribute("title", listEventHistory.get((int)session.getAttribute("optionHistory")));
				return new ModelAndView("login");
			}
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		return new ModelAndView("selectEventView");			
	}

	@RequestMapping("/report")
	public ModelAndView step3(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.REPORTS)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("report", "message", hola);
		}
	}

	@RequestMapping("/globalReport")
	public ModelAndView report(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.GLOBAL_REPORT)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("globalReport", "message", hola);
		}
	}
	
	@RequestMapping("/reportFinances")
	public ModelAndView reportFinances(HttpSession session) throws Exception {
		restartData();
		allRequest(session);
		String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		HashMap<String, Object> hola = new HashMap<String, Object>();
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		}
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (currentUser == null || !currentUser.userHasAccess(CustomerUser.REPORTS)) {
			return new ModelAndView("home", "message", new HashMap<>());
		}

		if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
			refreshToken(session);
			ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
		}

		try {
			// allRequest(session);
		} catch (Exception e) {
			log.error("ERROR", e);
		}

		return new ModelAndView("reportFinances", "message", hola);

	}

	@RequestMapping("/paypal")
	public ModelAndView paypal(HttpSession session) throws Exception {
		restartData();
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {

			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}
			HashMap<String, Object> hola = new HashMap<String, Object>();

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			long dateNow = System.currentTimeMillis();
			JSONObject amountByFund = new JSONObject();
			Iterator<ListFund> iterListFund = listFund.iterator();
			while (iterListFund.hasNext()) {
				ListFund listFundTmp = iterListFund.next();
				listFundTmp.setValidFrom(listFundTmp.getValidFrom());
				listFundTmp.setValidTo(listFundTmp.getValidTo());
				if (dateNow >= listFundTmp.getValidFrom() && dateNow <= listFundTmp.getValidTo()) {
					if (appParam.containsKey(String.valueOf(listFundTmp.getId()))
							&& amountByFund.isNull(String.valueOf(listFundTmp.getId()))) {
						Double amountTmp = Double.valueOf((String) appParam.get(String.valueOf(listFundTmp.getId())));
						amountByFund.put(String.valueOf(listFundTmp.getId()), amountTmp);
					}
				}
			}

			session.setAttribute("amount", amount);
			session.setAttribute("amountByFund", amountByFund);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("paypal", "message", hola);
		}
	}

	@RequestMapping(value = {"/callCenter", "/callCenter{keys}"})
	public ModelAndView stripe(@PathVariable(required = false) String keys ,HttpSession session) throws Exception {
		restartData();
		if(keys != null) {
			/*
			 * Si el flujo de ejecucion entra por aqui entonces se trara de una 
			 * peticion que viene desde el modulo users, Hoja de vida,
			 * donde 0 apunta a publicKey y 1 apunta a privateKey
			 * 
			 * Esto establecera la variable de session 'accesstoken' con el nuevo token generado
			 * con los keys en la posicion 0 y 1 del evento seleccionado en el modal de la hoja de vida.
			 */
			refreshToken(keys.split("-")[0], keys.split("-")[1], session);
		}else {
			session.setAttribute("accesstokenForLifeCycle", null);
			refreshToken(session);
		}
		allRequest(session);
		HashMap<String, Object> hola = new HashMap<String, Object>();
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		}

		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");

		if (currentUser == null || !currentUser.userHasAccess(CustomerUser.ADD_ATTENDEE)) {
			return new ModelAndView("home", "message", hola);
		}

		try {
			allRequest(session);
		} catch (Exception e) {
			log.error("ERROR", e);
		}
		long dateNow = System.currentTimeMillis();
		JSONObject amountByFund = new JSONObject();
		Iterator<ListFund> iterListFund = listFund.iterator();
		while (iterListFund.hasNext()) {
			ListFund listFundTmp = iterListFund.next();
			listFundTmp.setValidFrom(listFundTmp.getValidFrom());
			listFundTmp.setValidTo(listFundTmp.getValidTo());
			if (dateNow >= listFundTmp.getValidFrom() && dateNow <= listFundTmp.getValidTo()) {
				if (appParam.containsKey(String.valueOf(listFundTmp.getId()))
						&& amountByFund.isNull(String.valueOf(listFundTmp.getId()))) {
					Double amountTmp = Double.valueOf((String) appParam.get(String.valueOf(listFundTmp.getId())));
					amountByFund.put(String.valueOf(listFundTmp.getId()), amountTmp);
				}
			}
		}

		if (paymentGateway != null) {
			int gatewayMode = paymentGateway.getPaymentGatewayId();
			switch (gatewayMode) {
				case 2:
					session.setAttribute("stripeKey", paymentGateway.getAuthKey1());
					break;
				default:
					session.setAttribute("stripeKey", "");
					break;
			}
		}
		session.setAttribute("SHOW_SOURCE_MODAL", appParam.get("SHOW_SOURCE_MODAL"));
		session.setAttribute("amount", amount);
		session.setAttribute("amountByFund", amountByFund);
		session.setAttribute("paymentTypeDescription", paymentTypeDescription);
		session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
		session.setAttribute("txStatusDD", txStatusDD);
		session.setAttribute("cApp", cApp);
		session.setAttribute("listFund", listFund);
		session.setAttribute("languajes", languajes);
		session.setAttribute("txSources", txSources);
		session.setAttribute("txCampaing", txCampaing);
		session.setAttribute("externalPaymentTypes", paymentTypes);
		return new ModelAndView("addAttendee", "message", hola);

	}
	
	@RequestMapping(value = {"/oferta"})
	public ModelAndView formOfert(@PathVariable(required = false) String keys ,HttpSession session) throws Exception {
		
		session.getAttribute("");
		
		refreshToken(session);
		
		return new ModelAndView("oferta");

	}

	@RequestMapping("/signout")
	public ModelAndView signout(HttpSession session, HttpServletRequest request) throws Exception {
		session.invalidate();
		session = request.getSession();
		restartData();
		listAllEvents(session);
		return new ModelAndView("selectEventView");
	}

	private void allRequest(HttpSession session) throws Exception {
		String  accesstokenForLifeCycle = (String) session.getAttribute("accesstokenForLifeCycle");
		String accessTk = null;
		if(accesstokenForLifeCycle != null) {
			accessTk = accesstokenForLifeCycle;
		}else {
			accessTk = (String) session.getAttribute("accesstoken");
		}
		
		
		DonationDelegate dd = new DonationDelegate();
		//if (session.getAttribute("languajes") == null || languajes == null) {
			try {
				languajes = dd.requestLanguages(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				languajes = dd.requestLanguages(accessTk);
			}
		//}
		if (session.getAttribute("amount") == null || amount == null) {
			try {
				amount = dd.requestAmount(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				amount = dd.requestAmount(accessTk);
			}
		}

		if (session.getAttribute("paymentTypeDescription") == null || paymentTypeDescription == null) {
			try {
				paymentTypeDescription = dd.listAllPaymentTypeDescriptions(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				paymentTypeDescription = dd.listAllPaymentTypeDescriptions(accessTk);
			}
		}
		try {
			paymentGateway = dd.requestPaymentGateway(accessTk);
		} catch (ExpiredAccessTokenException ex) {
			paymentGateway = dd.requestPaymentGateway(accessTk);
		}
		if (session.getAttribute("paymentGatewaysDD") == null || paymentGatewaysDD == null) {
			try {
				paymentGatewaysDD = dd.listAllPaymentGateways(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				paymentGatewaysDD = dd.listAllPaymentGateways(accessTk);
			}
		}
		if (session.getAttribute("txStatusDD") == null || txStatusDD == null) {
			try {
				txStatusDD = dd.listTxStatus(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				txStatusDD = dd.listTxStatus(accessTk);
			}
		}
		if (session.getAttribute("cApp") == null || cApp == null) {
			try {
				cApp = dd.listCApp(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				cApp = dd.listCApp(accessTk);
			}
		}
		if (session.getAttribute("listFund") == null || listFund == null) {
			try {
				listFund = dd.requestListFund(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				listFund = dd.requestListFund(accessTk);
			}
		}
		if (session.getAttribute("txSources") == null || txSources == null) {
			try {
				txSources = dd.requestTxSources(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				txSources = dd.requestTxSources(accessTk);
			}
		}
		if (session.getAttribute("txCampaing") == null || txCampaing == null) {
			try {
				txCampaing = dd.requestTxCampaing(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				txCampaing = dd.requestTxCampaing(accessTk);
			}
		}

		// ajsb: To list payment types
		if (session.getAttribute("externalPaymentTypes") == null || paymentTypes == null) {
			try {
				paymentTypes = dd.requestExternalPaymentTypes(accessTk);
			} catch (ExpiredAccessTokenException ex) {
				paymentTypes = dd.requestExternalPaymentTypes(accessTk);
			}
		}

			try {
				appParam = dd.getApplicationParameters(accessTk);
			} catch (ExpiredAccessTokenException e) {
				appParam = dd.getApplicationParameters(accessTk);
			}
	}

	private void refreshToken(HttpSession session) {
		try {
			restartData();
			int option = (int) session.getAttribute("option");
			DonationDelegate dd = new DonationDelegate();
			session.setAttribute("accesstoken", dd.requestAccessToken(option));
		} catch (Exception e) {
			log.error("ERROR", e);
		}
	}
	
	private void refreshTokenOfert(HttpSession session) {
		try {
			restartData();
			RequestWS requestWS = new RequestWS();
			session.setAttribute("accesstoken", requestWS.getAccessToken());
		} catch (Exception e) {
			log.error("ERROR", e);
		}
	}
	
	private void refreshToken(String publicKey, String privateKey, HttpSession session) {
		try {
			restartData();
			RequestWS ws = new RequestWS();
			session.setAttribute("accesstokenForLifeCycle", ws.getAccessToken(publicKey, privateKey));
		} catch (Exception e) {
			log.error("ERROR", e);
		}
	}

	private void restartData() {
		lan = null;
		prop = null;
		listProperties = null;
		listFund = null;
		fund = null;
		amount = null;
		fundImage = null;
		txCampaing = null;
		paymentTypes = null;
		paymentTypeDescription = null;
		paymentGateway = null;
		appParam = null;
		txSources = null;
		cApp = null;
		paymentGatewaysDD = null;
		txStatusDD = null;
	}

	private boolean validAuthenticate(HttpSession session) {
		String name = (String) session.getAttribute("firstName");
		String lastname = (String) session.getAttribute("LastName");
		if (name != null && lastname != null && name.length() > 0 && lastname.length() > 0) {
			return true;
		}
		return false;
	}

	@RequestMapping("/coupons")
	public ModelAndView coupons(HttpSession session) throws Exception {
		restartData();
		allRequest(session);
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}
			HashMap<String, Object> hola = new HashMap<String, Object>();

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			long dateNow = System.currentTimeMillis();
			JSONObject amountByFund = new JSONObject();
			Iterator<ListFund> iterListFund = listFund.iterator();
			while (iterListFund.hasNext()) {
				ListFund listFundTmp = iterListFund.next();
				listFundTmp.setValidFrom(listFundTmp.getValidFrom());
				listFundTmp.setValidTo(listFundTmp.getValidTo());
				if (dateNow >= listFundTmp.getValidFrom() && dateNow <= listFundTmp.getValidTo()) {
					if (appParam.containsKey(String.valueOf(listFundTmp.getId()))
							&& amountByFund.isNull(String.valueOf(listFundTmp.getId()))) {
						Double amountTmp = Double.valueOf((String) appParam.get(String.valueOf(listFundTmp.getId())));
						amountByFund.put(String.valueOf(listFundTmp.getId()), amountTmp);
					}
				}
			}

			session.setAttribute("amount", amount);
			session.setAttribute("amountByFund", amountByFund);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("paypal", "message", hola);
		}
	}

	@RequestMapping("/restrictEvent")
	public ModelAndView restrictEvent(HttpSession session) throws Exception {

		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			HashMap<String, Object> hola = new HashMap<String, Object>();
			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			long dateNow = System.currentTimeMillis();
			JSONObject amountByFund = new JSONObject();
			Iterator<ListFund> iterListFund = listFund.iterator();
			while (iterListFund.hasNext()) {
				ListFund listFundTmp = iterListFund.next();
				listFundTmp.setValidFrom(listFundTmp.getValidFrom());
				listFundTmp.setValidTo(listFundTmp.getValidTo());
				if (dateNow >= listFundTmp.getValidFrom() && dateNow <= listFundTmp.getValidTo()) {
					if (appParam.containsKey(String.valueOf(listFundTmp.getId()))
							&& amountByFund.isNull(String.valueOf(listFundTmp.getId()))) {
						Double amountTmp = Double.valueOf((String) appParam.get(String.valueOf(listFundTmp.getId())));
						amountByFund.put(String.valueOf(listFundTmp.getId()), amountTmp);
					}
				}
			}

			if (paymentGateway != null) {
				int gatewayMode = paymentGateway.getPaymentGatewayId();
				switch (gatewayMode) {
					case 2:
						session.setAttribute("stripeKey", paymentGateway.getAuthKey1());
						break;
					default:
						session.setAttribute("stripeKey", "");
						break;
				}
			}

			session.setAttribute("amount", amount);
			session.setAttribute("amountByFund", amountByFund);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			session.setAttribute("externalPaymentTypes", paymentTypes);
			return new ModelAndView("restrictEvent", "message", hola);
		}
	}

	@RequestMapping("/restrictActualEvent")
	public ModelAndView restrictActualEvent(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			refreshToken(session);
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			DonationDelegate dd = new DonationDelegate();

			if (session.getAttribute("listFund") == null || listFund == null) {
				try {

					listFund = dd.requestListFund(ACCESS_TOKEN);
				} catch (ExpiredAccessTokenException ex) {
					refreshToken(session);
					ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
					listFund = dd.requestListFund(ACCESS_TOKEN);
				}
			}

			session.setAttribute("listFund", listFund);

			return new ModelAndView(RESTRICTED_BASE_VIEW + "restrictActualEvent");
		}
	}

	@RequestMapping("/listRestrictedUsers")
	public ModelAndView listRestrictedUsers(HttpServletRequest hsr, HttpSession session) throws Exception {
		CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			HashMap<String, Object> hola = new HashMap<String, Object>();
			DonationDelegate dd = new DonationDelegate();
			try {
				ArrayList<RestrictedUser> response = dd.getRestrictedUsers(ACCESS_TOKEN, hsr, currentUser);
				session.setAttribute("restrictedUsers", response);
			} catch (Exception e) {
				log.error("ERROR", e);
			}

			// session.setAttribute("restrictedUsers", response);
			return new ModelAndView("listUsers", "message", hola);
		}
	}

	// users module

	@RequestMapping("/users")
	public ModelAndView users(HttpSession session) throws Exception {

		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.USERS_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("users", "message", hola);
		}
	}
	

	@RequestMapping("/usersLifecycle")
	public ModelAndView usersLifecycle(HttpSession session) throws Exception {
	
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}
	
			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();
	
			if (!currentUser.userHasAccess(CustomerUser.USERS_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("usersLifecycle", "message", hola);
		}
	}

	@RequestMapping("/black-list")
	public ModelAndView blackList(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.BLACKLIST_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("black_list", "message", hola);
		}
	}

	@RequestMapping("/app-log")
	public ModelAndView getAppLog(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.BLACKLIST_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("app_log", "message", hola);
		}
	}

	@RequestMapping("/live-setting")
	public ModelAndView liveSettingView(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.EVENT_ASSETS)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("liveSetting", "message", hola);
		}
	}

	@RequestMapping("/source-settings")
	public ModelAndView sourceSettingView(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.SOURCES)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("source-settings", "message", hola);
		}
	}


		@RequestMapping("/white-list")
	public ModelAndView whiteListSettingView(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.WHILELIST)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("white-list", "message", hola);
		}
		
		
	}
		
		@RequestMapping("/scan-qr")
	public ModelAndView scanqr(HttpSession session) throws Exception {
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.BLACKLIST_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			return new ModelAndView("scan-qr", "message", hola);
		}
		
		
	}
		
	@RequestMapping("/userDetail-{userId}")
	public ModelAndView userDetail(HttpSession session, @PathVariable(required = false) String userId) throws Exception {
		
		RequestWS requestWS = new RequestWS();
		String aCCESS_TOKEN = (String) session.getAttribute("accesstoken");
		UserInfo userInfo = requestWS.findUserInfo(userId, aCCESS_TOKEN);
		List<HashMap<String, Object>> userLifecycle = requestWS.findLifecycle(userId, aCCESS_TOKEN);
		List<HashMap<String, Object>> listUserActivities = requestWS.findBusinessUserSupportHistory(userId, aCCESS_TOKEN);
		HashMap<String, Object> recentLogin = requestWS.getRecentLoginByEmail(userId, aCCESS_TOKEN);
		HashMap<String, Object> userRecentPurchase = requestWS.getRecentPurchaseByEmail(userId, aCCESS_TOKEN);
		
		if (paymentGateway != null) {
			int gatewayMode = paymentGateway.getPaymentGatewayId();
			switch (gatewayMode) {
				case 2:
					session.setAttribute("stripeKey", paymentGateway.getAuthKey1());
					break;
				default:
					session.setAttribute("stripeKey", "");
					break;
			}
		}
		
		if (!validAuthenticate(session)) {
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.BLACKLIST_QUERY)) {
				return new ModelAndView("home", "message", hola);
			}
			
			String[] lifeCycle = {"Seminario Creando Riqueza", "Seminario de Inversiones", "Planeación Estratégica", "Clase de Trading Live"};
			
			ModelAndView mAV = new ModelAndView();
			mAV.setViewName("userDetail");
			mAV.addObject("userInfo", userInfo);
			mAV.addObject("userLifecycle", userLifecycle);
			mAV.addObject("lifeCycle", lifeCycle);
			mAV.addObject("listUserActivities", listUserActivities);
			mAV.addObject("recentLogin", recentLogin);
			mAV.addObject("userRecentPurchase", userRecentPurchase);
			return mAV;
		}
	}
	
	@RequestMapping("/salesReport")
	public ModelAndView salesReport(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.REPORT_AFFILIATE)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("salesReport", "message", hola);
		}
	}
	
	@RequestMapping("/detailSalesReport")
	public ModelAndView detailSalesReport(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.REPORT_AFFILIATE)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("detailSalesReport", "message", hola);
		}
	}
	
	@RequestMapping("/salesSupervisor")
	public ModelAndView salesSupervisor(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("salesSupervisor", "message", hola);
		}
	}
	
	@RequestMapping("/SDILeads")
	public ModelAndView salesAgent(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("salesAgent", "message", hola);
		}
	}

	@RequestMapping("/SCRLeads")
	public ModelAndView salesSCRLeads(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);
		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("salesSCRLeads", "message", hola);
		}
	}
	
	@RequestMapping("/ClassLeads")
	public ModelAndView salesClassLeads(HttpSession session) throws Exception {
		restartData();
		refreshToken(session);

		allRequest(session);
		if (!validAuthenticate(session)) {
			session.invalidate();
			return new ModelAndView("redirect:/signout");
		} else {
			String ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			if (ACCESS_TOKEN == null || ACCESS_TOKEN.length() == 0) {
				refreshToken(session);
				ACCESS_TOKEN = (String) session.getAttribute("accesstoken");
			}

			CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
			HashMap<String, Object> hola = new HashMap<String, Object>();

			if (!currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {
				return new ModelAndView("home", "message", hola);
			}

			try {
				allRequest(session);
			} catch (Exception e) {
				log.error("ERROR", e);
			}
			session.setAttribute("amount", amount);
			session.setAttribute("paymentTypeDescription", paymentTypeDescription);
			session.setAttribute("paymentGatewaysDD", paymentGatewaysDD);
			session.setAttribute("txStatusDD", txStatusDD);
			session.setAttribute("cApp", cApp);
			session.setAttribute("listFund", listFund);
			session.setAttribute("txSources", txSources);
			session.setAttribute("txCampaing", txCampaing);
			return new ModelAndView("salesClassLeads", "message", hola);
		}
	}
}