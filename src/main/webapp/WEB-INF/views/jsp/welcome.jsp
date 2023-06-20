<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.weavx.web.model.Amount"%>
<%@page import="com.weavx.web.model.FundLanguage"%>
<%@page import="com.weavx.web.model.Fund"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.weavx.web.model.Language"%>
<%@page import="com.weavx.web.model.PropertyLanguage"%>
<%@page import="com.weavx.web.model.Country"%>
<%@page import="com.weavx.web.model.PaymentTypeDescription"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>

<c:url var="home" value="/" scope="request" />

<spring:url value="/resources/core/css/materialize.css"
	var="materializeCss" />
<spring:url value="/resources/core/css/style.css" var="styleCss" />
<spring:url value="/resources/core/css/animate.css" var="animateCss" />
<spring:url value="/resources/core/js/jquery-2.1.1.min.js"
	var="jqueryJs" />
<spring:url value="/resources/core/js/materialize.js"
	var="materializeJs" />
<spring:url value="/resources/core/js/init.js" var="initJs" />
<spring:url value="/resources/core/js/paymentAuthorize.js" var="paymentAuthorizeJs" />
<spring:url value="/resources/core/js/jquery.validate.js" var="validateJs" />
<spring:url value="/resources/core/js/animatesvg.js" var="animateJs" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="google-signin-client_id" content="891100152335-8trida2pbldokccn2c8e9n8cmrqsb5bl.apps.googleusercontent.com">
<meta name="HandheldFriendly" content="true">
<meta name="viewport" content="width=device-width, initial-scale=1.0, 
minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>Donation On Line</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css?family=Karla:400,400i,700,700i"
	rel="stylesheet">
<link href="${materializeCss}" type="text/css" rel="stylesheet"
	media="screen,projection" />
<link href="${styleCss}" type="text/css" rel="stylesheet"
	media="screen,projection" />
<link href="${animateCss}" type="text/css" rel="stylesheet"
	media="screen,projection" />

</head>
<body id="background"
	style="background-image: url('resources/core/img/donation.jpg');">

	<div class="background-black"></div>
	<div class="menu">
		<div class="idiom">
			<select id="newIdiom">
				<%
					ArrayList<Language> varLang = (ArrayList<Language>) session.getAttribute("lan");
					Iterator<Language> iter = varLang.iterator();
					int countLang = 0;
					while (iter.hasNext()) {
						Language languagesTmp = iter.next();
						languagesTmp.getLocale();
						countLang++; 
				%>
				<option value="<%=languagesTmp.getId()%>"
					<%=(languagesTmp.isDefault()) ? "selected" : ""%>><%=languagesTmp.getLocale().substring(0, 2).toUpperCase().trim()%></option>
				<%
					}
				%>
			</select>
		</div>
	</div>

	<div id="section-donation" class="section">
		<div class="container">
			<div class="background-mobile"
				style="background-image: url('resources/core/img/donation.jpg');"></div>
			<div class="logo center-align">
				<img src="resources/core/img/logo_donaciones.svg"
					class="responsive-img" id="lang-logo">
			</div>

			<div class="donation-box" id="box1">

				<div class="security">
						<span class="lang-secure"></span> <img
							src="resources/core/img/candado.svg" class="padlock">
				</div>

				<div class="details">

					<div class="bullet">
						<div class="action"></div>
						<div></div>
						<div></div>
					</div>

					<div class="title center-align">
						<span id="lang-title-1"></span>
					</div>

					<div id="type-donation"></div>

					<div id="addDonation" class="button-transparent-blue button-donate">
						<a class="waves-effect waves-light btn"><span
							id="lang-add-dontation"></span></a>
						<div class="divisor">
							<div class="line">
								<hr>
							</div>
							<div class="line-text">
								<span id="lang-or"></span>
							</div>
							<div class="line">
								<hr>
							</div>
						</div>

					</div>

					<div id="bntContinue" class="button-blue button-donate">
						<a class="waves-effect waves-light btn">
						<span class="lang-continue"></span>
						<i class="material-icons">keyboard_arrow_right</i></a>
					</div>

				</div>

			</div>

			<div class="donation-box" id="box2" style="display: none;">
			

				<div class="security row">
						<div class="col s6 truncate left-align no-padding">
								<span class="totalAmount grey-small-bold"></span> <span class="grey-soft-small">[<span id="lang-edit" class="cursor"></span>]</span>
						</div>
						<div class="col s6 truncate  right-align no-padding">
								<span class="lang-secure"></span> 
								<img src="resources/core/img/candado.svg" class="padlock">
						</div>
				</div>



				<div class="details">					

					<div class="bullet">
						<div></div>
						<div class="action"></div>
						<div></div>
					</div>

					<div class="formS2 row">
						<form action="" id="step2">
							<div class="title center-align">
								<span id="lang-personal"></span>
							</div>
							<div class="col s12 m6 input">
								<input class="input-left" type="text" id="lang-fname_ph" name="lang-fname_ph" maxlength="20" />
							</div>
							<div class="col s12 m6 input">
								<input class="input-right" type="text" id="lang-lname_ph" name="lang-lname_ph" maxlength="20" />
							</div>
							<div class="col s12 input">
								<div id="type-countries"></div>
							</div>
							<div class="col s12 m6 input">
								<input class="input-left" type="text" id="lang-state_ph" name="lang-state_ph" maxlength="40" />
							</div>
							<div class="col s12 m6 input">
							 	<input class="input-right" type="text" id="lang-city_ph" name="lang-city_ph" maxlength="40" />
							</div>
							<div class="col s12 m8 input">
								<input class="input-left" type="text" id="lang-address_ph" name="lang-address_ph" maxlength="255" />
							</div>
							<div class="col s12 m4 input">	
								<input class="input-right" type="text" id="lang-zip_ph" name="lang-zip_ph" maxlength="6" />
							</div>
							<div class="col s12 input">
								<div id="lang-send" class="grey-small-bold"></div>
								<input type="text" id="lang-email_ph" name="lang-email_ph" maxlength="50" /> 
							</div>
							<div class="col s12 grey-box">
								 <input type="checkbox"  id="lang-remember-input" class="filled-in" />
	      						 <label id="lang-remember"  for="lang-remember-input" class="grey-small-bold lang-remember-label" ></label>
	      						 <div id="password" style="display: none;" class="col s12 no-padding">
									<div id="lang-next" class="grey-small"></div>
									<input type="password" id="lang-passw_ph" name="lang-passw_ph" maxlength="20" /> 
								</div>
							</div>
						</form>
					</div>
					<div id="bntContinue2" class="button-blue button-donate">
						<a class="waves-effect waves-light btn">
						<span class="lang-continue"></span>
						<i class="material-icons">keyboard_arrow_right</i></a>
					</div>

				</div>

			</div>

			<div class="donation-box" id="box3" style="display: none;">

				<div class="security row">
						<div class="col s6 truncate left-align no-padding">
							<span id="backS2" class="cursor grey-small-bold"> <i class="material-icons">keyboard_arrow_left</i></span>
							<span class="totalAmount grey-small-bold"> </span>
						</div>
						<div class="col s6 truncate  right-align no-padding">
								<span class="lang-secure"></span> 
								<img src="resources/core/img/candado.svg" class="padlock">
						</div>
				</div>


				<div class="details">

					<div class="bullet">
						<div></div>
						<div></div>
						<div class="action"></div>
					</div>

					<div class="formS3 row">
						<form action="" id="step3">
							<div class="title center-align">
								<span id="lang-ways"></span>
							</div>
							
							<div class="col s12 input">
								<div id="type-payment"></div>
							</div>
							
							<div class="col s12 input">
								<input type="text" id="lang-creditcard_ph" name="lang-creditcard_ph" maxlength="16" pattern="[0-9]{13,16}" />
							</div>
							
							<div class="col s12 m6 input">
								<label for="lang-expdate" id="lang-expph" class="grey-small-bold"></label> 
								<input type="text" class="input-left" id="lang-expdate_ph" name="lang-expdate_ph" /> 
							</div>
							<div class="col s12 m6 input">
								<label for="cvv" id="lang-cvvph" class="grey-small-bold"></label> 
								<input type="text" id="lang-cvv_ph" class="input-right" name="lang-cvv_ph" maxlength="4" /> 
							</div>
							<div class="col s12 grey-box">
								<input type="checkbox" class="filled-in" id="scheduledPayment"> 
								<label for="scheduledPayment" id="lang-scheduled" class="grey-small-bold lang-remember-label"></label> 
								<div id="scheduledBox" style="display: none;"></div>
							</div>
						</form>

					</div>
					
					<div class="loader col s12 center center-align">
						  <div class="preloader-wrapper active">
						    <div class="spinner-layer spinner-blue-only">
						      <div class="circle-clipper left">
						        <div class="circle"></div>
						      </div><div class="gap-patch">
						        <div class="circle"></div>
						      </div><div class="circle-clipper right">
						        <div class="circle"></div>
						      </div>
						    </div>
						  </div>
					</div>
					<div id="bntContinue3" class="button-blue button-donate">
						<a class="waves-effect waves-light btn">
						<span class="lang-donate" id="lang-donate-btn"></span> 
						<i class="material-icons">keyboard_arrow_right</i></a>
					</div>
					<label id="lang-error-transaction" class="error" style="width: 100%; text-align: center;" ></label>
				</div>

			</div>

			<div class="donation-box" id="box4" style="display: none;">
				<div class="details">
					<div class="formS4 row">
						<div class="col s8 offset-s2">
								<svg  version="1.1" id="svg-animation" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0" y="0" viewBox="0 0 354 354" xml:space="preserve">
		  						<style type="text/css"> .st0{fill:#3d62ee;} .st1{fill:#A0AFB5;} .st2{fill:#A0AFB5;stroke:#737F84;stroke-width:2;stroke-linejoin:round;stroke-miterlimit:10;} .st3{fill:#FFFFFF;} .st4{fill:none;stroke:#A0AFB5;stroke-width:2;stroke-miterlimit:10;} .st5{fill:#3d62ee;} .st6{fill:#3d62ee;} .st7{fill:none;stroke:#FFFFFF;stroke-width:2;stroke-miterlimit:10;} .st8{fill:#3d62ee;} .st9{fill:none;stroke:#666;stroke-width:2;stroke-miterlimit:10;} .st10{fill:#666;} .st11{fill:#F9F9F9;stroke:#737F84;stroke-width:2;stroke-linejoin:round;stroke-miterlimit:10;} .st12{fill:#E2E6E8;stroke:#737F84;stroke-width:2;stroke-linejoin:round;stroke-miterlimit:10;} </style>
		  						<circle id="bg-circle" class="st0" cx="177" cy="177" r="177"/>
		  						<g id="all-items">
		  							<polygon id="envelope-bg" class="st1" points="89 117 91 115 261 115 262 116 264 118 264 236 88 236 88 118 "/>
		  							<polygon id="envelope-lip" class="st2" points="265 116 176 56 87 116"/>
		  							<g id="screens">
		  								<g id="screen3">
		  									<path class="st3" d="M45 270c-2.8 0-5-2.2-5-5V165c0-2.8 2.2-5 5-5h144c2.8 0 5 2.2 5 5v100c0 2.8-2.2 5-5 5H45z"/>
		  									<path class="st1" d="M189 161c2.2 0 4 1.8 4 4v100c0 2.2-1.8 4-4 4H45c-2.2 0-4-1.8-4-4V165c0-2.2 1.8-4 4-4H189M189 159H45c-3.3 0-6 2.7-6 6v100c0 3.3 2.7 6 6 6h144c3.3 0 6-2.7 6-6V165C195 161.7 192.3 159 189 159L189 159z"/>
		  									<line class="st4" x1="108" y1="184" x2="171" y2="184"/>
		  									<line class="st4" x1="108" y1="192" x2="171" y2="192"/>
		  									<line class="st4" x1="108" y1="200" x2="171" y2="200"/>
		  									<line class="st4" x1="108" y1="208" x2="171" y2="208"/>
		  									<line class="st4" x1="108" y1="216" x2="171" y2="216"/>
		  									<line class="st4" x1="108" y1="224" x2="171" y2="224"/>
		  									<line class="st4" x1="108" y1="232" x2="171" y2="232"/>
		  									<line class="st4" x1="108" y1="240" x2="171" y2="240"/>
		  									<line class="st4" x1="108" y1="248" x2="148" y2="248"/>
		  									<rect x="61.5" y="182.5" class="st5" width="41" height="43"/>
		  									<path class="st5" d="M102 183v42H62v-42H102M103 182H61v44h42V182L103 182z"/>
		  									<polygon class="st3" points="77 197 88 204 77 211 "/>
		  								</g>
		  								<g id="screen2">
		  									<path class="st3" d="M104 231c-2.8 0-5-2.2-5-5V126c0-2.8 2.2-5 5-5h144c2.8 0 5 2.2 5 5v100c0 2.8-2.2 5-5 5H104z"/>
		  									<path class="st1" d="M248 122c2.2 0 4 1.8 4 4v100c0 2.2-1.8 4-4 4H104c-2.2 0-4-1.8-4-4V126c0-2.2 1.8-4 4-4H248M248 120H104c-3.3 0-6 2.7-6 6v100c0 3.3 2.7 6 6 6h144c3.3 0 6-2.7 6-6V126C254 122.7 251.3 120 248 120L248 120z"/>
		  									<rect x="121" y="144" class="st6" width="39" height="41"/>
		  									<path class="st6" d="M159 145v39h-37v-39H159M161 143h-41v43h41V143L161 143z"/>
		  									<polyline class="st7" points="117.7 186.2 133.7 170.3 151.2 187.8 "/>
		  									<polyline class="st7" points="137.2 173.7 147.5 163.4 161.1 176.9 "/>
		  									<path class="st3" d="M134.6 154.7c2.1 0 3.7 1.7 3.7 3.7s-1.7 3.7-3.7 3.7 -3.7-1.7-3.7-3.7S132.5 154.7 134.6 154.7M134.6 152.7c-3.2 0-5.7 2.6-5.7 5.7s2.6 5.7 5.7 5.7 5.7-2.6 5.7-5.7S137.7 152.7 134.6 152.7L134.6 152.7z"/>
		  									<rect x="168.5" y="143.5" class="st5" width="63" height="42"/>
		  									<path class="st5" d="M231 144v41h-62v-41H231M232 143h-64v43h64V143L232 143z"/>
		  									<polygon class="st3" points="195 158 206 165 195 172 "/>
		  									<line class="st4" x1="120" y1="194" x2="232" y2="194"/>
		  									<line class="st4" x1="120" y1="202" x2="232" y2="202"/>
		  									<line class="st4" x1="120" y1="210" x2="189" y2="210"/>
		  								</g>
		  								<g id="screen1">
		  									<path class="st3" d="M163 194c-2.8 0-5-2.2-5-5V89c0-2.8 2.2-5 5-5h144c2.8 0 5 2.2 5 5v100c0 2.8-2.2 5-5 5H163z"/>
		  									<path class="st1" d="M307 85c2.2 0 4 1.8 4 4v100c0 2.2-1.8 4-4 4H163c-2.2 0-4-1.8-4-4V89c0-2.2 1.8-4 4-4H307M307 83H163c-3.3 0-6 2.7-6 6v100c0 3.3 2.7 6 6 6h144c3.3 0 6-2.7 6-6V89C313 85.7 310.3 83 307 83L307 83z"/>
		  									<g id="screen2sim_1_">
		  										<rect x="250.5" y="107.5" class="st8" width="40" height="41"/>
		  										<path class="st8" d="M290 108v40h-39v-40H290M291 107h-41v42h41V107L291 107z"/>
		  									</g>
		  									<path class="st7" d="M279.4 124.6c2 4.5-0.1 9.8-4.7 11.8s-9.8-0.1-11.8-4.7c-2-4.5 0.1-9.8 4.7-11.8C272.2 117.9 277.4 120 279.4 124.6z"/>
		  									<path class="st3" d="M280 129.7c-0.1 0.6-0.3 1.2-0.5 1.7 1.4 1.1 2.1 2.1 1.9 2.6 -0.3 0.7-1.5 0.9-3.8 0.4 -1.8-0.4-4.3-1.2-7.7-2.5 -3.5-1.4-6-2.8-7.6-4 -1.7-1.3-2.4-2.3-2.1-2.9 0.3-0.5 1.3-0.6 2.8-0.4 0.2-0.5 0.5-1.1 0.9-1.5 -5.2-1.4-9.1-1.7-9.8-0.5 -0.8 1.4 2 4.5 8.5 7.8 2 1 4.2 2.1 6.9 3.1 2.4 0.9 4.5 1.7 6.4 2.4 7.7 2.6 11.1 2.7 11.8 1.1C288.2 135.6 285.2 132.7 280 129.7z"/>
		  									<line class="st4" x1="181" y1="108" x2="244" y2="108"/>
		  									<line class="st4" x1="181" y1="116" x2="244" y2="116"/>
		  									<line class="st4" x1="181" y1="124" x2="244" y2="124"/>
		  									<line class="st4" x1="181" y1="132" x2="244" y2="132"/>
		  									<line class="st4" x1="181" y1="140" x2="244" y2="140"/>
		  									<line class="st4" x1="181" y1="148" x2="220" y2="148"/>
		  									<line class="st9" x1="181" y1="165" x2="291" y2="165"/>
		  									<path class="st3" d="M199 170c-2.8 0-5-2.2-5-5s2.2-5 5-5 5 2.2 5 5S201.8 170 199 170z"/>
		  									<path class="st10" d="M199 161c2.2 0 4 1.8 4 4s-1.8 4-4 4 -4-1.8-4-4S196.8 161 199 161M199 159c-3.3 0-6 2.7-6 6s2.7 6 6 6 6-2.7 6-6S202.3 159 199 159L199 159z"/>
		  								</g>
		  							</g>
		  							<g id="envelope-fg">
		  								<polygon class="st11" points="265 235 87 235 87 117 "/>
		  								<polygon class="st12" points="265 117 265 235 176 176 "/>
		  							</g>
		  						</g>
		  					</svg>
						</div>
						<div class="col s12 center-align semi-title">
							<span id="lang-thanks">Muchas gracias</span>
							<span id="lang-name"> </span></div>
						<div class="col s12 center-align text-light">
							<span id="lang-salutation">Hemos enviado el recibo a tu correo Electrónico</span>
						</div>
					</div>
				</div>
			</div>

			<div class="donation-box" id="login" style="display: none;">

				<div class="details">


					<div class="security">
						<span class="lang-secure"></span> <img
							src="resources/core/img/candado.svg" class="padlock">
					</div>

					<div class="form-login row">

							<div class="title center-align title-extra-top">
								<span id="lang-login"></span>
							</div>
							<div class="col s12 input">
								<input type="text" id="lang-mail-login_ph">
							</div>
							<div class="col s12 input"> 
								<input type="password" id="lang-passw-login_ph">
							</div>
							

							<div id="bntLogin" class="button-blue button-donate extra-margin-vertical">
								<a class="waves-effect waves-light btn">
									<span class="lang-donate" id="lang-log-in"></span>
								</a>
							</div>

							<div class="col s12">
								<span class="line-space-2 line-center"></span>
								<span class="line-space-1 grey-small" id="lang-or2"></span>
								<span class="line-space-2 line-center"></span>
							</div>
							<div class="col s12 m6">
								<fb:login-button scope="public_profile,email" onlogin="checkLoginState();"></fb:login-button>
								<div id="status"></div>
							</div>
							<div class="col s12 m6">
								<div id="bntLogin3" class="button-blue button-donate g-signin2" data-onsuccess="onSignIn">
									<a class="waves-effect waves-light btn">
										<span class="lang-donate">Google+</span> 
									</a>
								</div>
							</div>
					</div>

				</div>
				<div id="last-box" style="display: none;"></div>
				<div id="login-with" style="display: none;"></div>
			</div>

		</div>
	</div>


	<div id="foot" class="section">
		<div class="page-footer">
			<span id="lang-is-registered"></span>&nbsp<a id="lang-sign-in"> </a>
			<span id="lang-is-registered2" style="display: none;"></span>&nbsp<a
				id="lang-sign-in2" style="display: none;"> </a>
			<div class="text-lighten-4" id="lang-footer"></div>
			<div class="upline">
				<span id="lang-term"></span><a target="_blank" id="lang-url-footer"
					href=""></a>
			</div>
			<div class="upimg">
				<div class="imgSingle">
					<a href="https://www.authorize.net/"> <img id="img1"
						class="imgClass" src="resources/core/img/authorize.svg" border="0">
					</a>
				</div>
				<div class="imgSingle">
					<a href="https://www.paypal.com/"> <img id="img2"
						class="imgClass paypal" src="resources/core/img/paypal.svg" border="0"></a>
				</div>
				<div class="imgSingle">
					<a href="https://stripe.com/"> <img id="img3" class="imgClass"
						src="resources/core/img/stripe.svg" border="0"></a>
				</div>
			</div>
		</div>
	</div>

	<!--  Scripts-->
	
	<script src="${jqueryJs}"></script>
	<script type="text/javascript">
				var amount = [
					<%String amountFormat = "";
					ArrayList<Amount> varAmount = (ArrayList<Amount>) session.getAttribute("amount");
					Iterator<Amount> iterAmount = varAmount.iterator();
					while (iterAmount.hasNext()) {
							Amount amountTmp = iterAmount.next();
							amountFormat += "{\"id\":\"" + amountTmp.getId() + "\", \"simbol\":\"$\",\"value\":\"" + amountTmp.getAmount()+ "\"  },\n";
						}%>
					<%=amountFormat%>
					
					{"id":"8", "simbol":"$","value":'<input type="tel" onkeypress="return (event.keyCode == 46 || event.keyCode == 8 ||( event.charCode >= 48 && event.charCode <= 57));" maxlength="7" placeholder="Otro" />'}];
	  			
	  				<% 	String donationFormat1 ="";
	  					String donationFormat2 ="";
	  					String firstdonationFormat1 = "{\"id\":\"\",  \"name\":\"Seleccione Otra Donación\"},";
	  					String firstdonationFormat2 = "{\"id\":\"\",  \"name\":\"Select Another Donation\"},";
	  				try{
	  					HashMap<FundLanguage,Fund> varFund = (HashMap<FundLanguage,Fund>) session.getAttribute("fundLang");
	  					if (varFund != null){
			  				for(Map.Entry<FundLanguage, Fund> entry : varFund.entrySet()) {
			  					FundLanguage tmpFundLan = entry.getKey();
			  					Fund tmpFund = entry.getValue();
			  					int tmpFundDefault = (Integer) session.getAttribute("fundDefault");
			  					if (tmpFundDefault==tmpFund.getFundId()) {
			  						if (tmpFund.getLangId()==1){
				  						firstdonationFormat1 += "{\"id\":\""+tmpFund.getFundId()+"\", \"name\":\""+tmpFund.getFundLabel()+"\", \"description\":\""+tmpFund.getFundDescription()+"\"},\n";                 
		  							}	
		  							else {
		  								firstdonationFormat2 += "{\"id\":\""+tmpFund.getFundId()+"\", \"name\":\""+tmpFund.getFundLabel()+"\", \"description\":\""+tmpFund.getFundDescription()+"\"},\n";
		  							}			  						
			  					} else {
			  						if (tmpFund.getLangId()==1){
				  						donationFormat1 += "{\"id\":\""+tmpFund.getFundId()+"\", \"name\":\""+tmpFund.getFundLabel()+"\", \"description\":\""+tmpFund.getFundDescription()+"\"},\n";                 
		  							}	
		  							else {
		  								donationFormat2 += "{\"id\":\""+tmpFund.getFundId()+"\", \"name\":\""+tmpFund.getFundLabel()+"\", \"description\":\""+tmpFund.getFundDescription()+"\"},\n";
		  							}			  						
			  					}
			  					
			  				}
		  				}
	  				}
	  				catch(Exception e){
	  					System.out.print("ExceptionED: ");
	  			        System.out.println(e.getMessage());
	  				}
	  				
	  				%>
	  				var typeDonations = [];
	  				typeDonations[1] = [<%=firstdonationFormat1+donationFormat1.substring(0, donationFormat1.length()-2)+"\n"%>];
	  				typeDonations[2] = [<%=firstdonationFormat2+donationFormat2.substring(0, donationFormat2.length()-2)+"\n"%>];
	  			
	  			<% String[] propertyLangFormat = new String[countLang];
	  				for (int l = 0; l < countLang; l++){
	  					propertyLangFormat[l]="";
	  				}
	  					  			
	  			try{
  					HashMap<PropertyLanguage, String> varProp = (HashMap<PropertyLanguage,String>) session.getAttribute("propertyLang");
  					if (varProp != null){
		  				for(Map.Entry<PropertyLanguage, String> entry : varProp.entrySet()) {
		  					PropertyLanguage tmpPropLan = entry.getKey();
		  					String tmpValue = entry.getValue();
		  					int indexLang = tmpPropLan.getLangugeId()-1;
		  						switch (tmpPropLan.getPropertyId()){
		  							case  1: //LOGO
		  								propertyLangFormat[indexLang] += "\"#lang-logo\":\""+tmpValue+"\",";
		  								break;
		  							case 2: //TITLE
		  								//propertyLangFormat1 += "\"#lang-title-1\":\""+tmpValue+"\",";
		  								break;
		  							case 3: //SECURE_TEXT
		  								propertyLangFormat[indexLang] += "\".lang-secure\":\""+tmpValue+"\",";
		  								break;
		  							case 4: //INVITATION
		  								propertyLangFormat[indexLang] += "\"#lang-title-1\":\""+tmpValue+"\",";
		  								break;
		  							case 5: //AMOUNT_SELECTION
		  								propertyLangFormat[indexLang] += "\".lang-amount\":\""+tmpValue+"\",";
		  								break;
		  							case 6: //CONTINUE_BUTTON
		  								propertyLangFormat[indexLang] += "\".lang-continue\":\""+tmpValue+"\",";
		  								break;
		  							case 7: //IS_REGISTERED
		  								propertyLangFormat[indexLang] += "\"#lang-is-registered\":\""+tmpValue+"\",";
		  								break;
		  							case 8: //DO_LOGIN
		  								propertyLangFormat[indexLang] += "\"#lang-sign-in\":\""+tmpValue+"\",";
		  								break;
		  							case 9: //FOOTER
		  								propertyLangFormat[indexLang] += "\"#lang-footer\":\""+tmpValue+"\",";
		  								break;
		  							case 10: //TERM_CONDITIONS
		  								propertyLangFormat[indexLang] += "\"#lang-condition\":\""+tmpValue+"\",";
		  								break;
		  							case 11: //URL_FOOTER
		  								propertyLangFormat[indexLang] += "\"#lang-url-footer\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 12: //URL_FOOTER
		  								propertyLangFormat[indexLang] += "\"#lang-term\":\""+tmpValue+" | \",";
		  								break;		  								
		  							case 13: //other
		  								propertyLangFormat[indexLang] += "\".lang-other\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 14: //edit
		  								propertyLangFormat[indexLang] += "\"#lang-edit\":\""+tmpValue+"\",";
		  								break;
		  							case 15: //Personal
		  								propertyLangFormat[indexLang] += "\"#lang-personal\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 16: //name
		  								propertyLangFormat[indexLang] += "\"#lang-fname_ph\":\""+tmpValue+"\",";
		  								break;
		  							case 17: //last name
		  								propertyLangFormat[indexLang] += "\"#lang-lname_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 18: //country
		  								propertyLangFormat[indexLang] += "\"#lang-country\":\""+tmpValue+"\",";
		  								break;
		  							case 19: //city
		  								propertyLangFormat[indexLang] += "\"#lang-city_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 20: //state
		  								propertyLangFormat[indexLang] += "\"#lang-state_ph\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 21: //address
		  								propertyLangFormat[indexLang] += "\"#lang-address_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 22: //zip code
		  								propertyLangFormat[indexLang] += "\"#lang-zip_ph\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 23: //send
		  								propertyLangFormat[indexLang] += "\"#lang-send\":\""+tmpValue+"\",";
		  								break;
		  							case 24: //email
		  								propertyLangFormat[indexLang] += "\"#lang-email_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 25: //remember
		  								propertyLangFormat[indexLang] += "\"#lang-remember\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 26: //next
		  								propertyLangFormat[indexLang] += "\"#lang-next\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 27: //passw
		  								propertyLangFormat[indexLang] += "\"#lang-passw_ph\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 28: //ways
		  								propertyLangFormat[indexLang] += "\"#lang-ways\":\""+tmpValue+"\",";
		  								break;
		  							case 29: //creditcard
		  								propertyLangFormat[indexLang] += "\"#lang-creditcard_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 30: //expdate
		  								propertyLangFormat[indexLang] += "\"#lang-expph\":\""+tmpValue+"\",";
		  								break;
		  							case 31: //expph
		  								propertyLangFormat[indexLang] += "\"#lang-expdate_ph\":\""+tmpValue+"\",";
		  								break;		  								
		  							case 32: //cvv
		  								propertyLangFormat[indexLang] += "\"#lang-cvvph\":\""+tmpValue+"\",";
		  								break;
		  							case 33: //cvvph
		  								propertyLangFormat[indexLang] += "\"#lang-cvv_ph\":\""+tmpValue+"\",";
		  								break;		  						
		  							case 34: //scheduled
		  								propertyLangFormat[indexLang] += "\"#lang-scheduled\":\""+tmpValue+"\",";
		  								break;
		  							case 35: //scheduled
		  								propertyLangFormat[indexLang] += "\"#lang-add-dontation\":\""+tmpValue+"\",";
		  								break;	
		  							case 36:
		  								propertyLangFormat[indexLang] += "\"#lang-or\":\""+tmpValue+"\",";
		  								break;	  	
		  							case 37:
		  								propertyLangFormat[indexLang] += "\".lang-warning-cell\":\""+tmpValue+"\",";
		  								break;
		  							case 38:
		  								propertyLangFormat[indexLang] += "\"#lang-donate-btn\":\""+tmpValue+"\",";
		  								break;
		  							case 39:
		  								propertyLangFormat[indexLang] += "\"#lang-login\":\""+tmpValue+"\",";
		  								break;
		  							case 40:
		  								propertyLangFormat[indexLang] += "\"#lang-log-in\":\""+tmpValue+"\",";
		  								break;
		  							case 41:
		  									propertyLangFormat[indexLang] += "\"#lang-mail-login_ph\":\""+tmpValue+"\",";
		  								break;
		  							case 42:
		  									propertyLangFormat[indexLang] += "\"#lang-passw-login_ph\":\""+tmpValue+"\",";
		  								break;
		  							case 43:	
		  									propertyLangFormat[indexLang] += "\"#lang-or2\":\""+tmpValue+"\",";
		  								break;								
		  						}
	  				}
		  				
  				}
  					}
  				catch(Exception e){
  					System.out.print("ExceptionED: ");
  			        System.out.println(e.getMessage());
  				}
	  			%>
	  			var language = [];
	  			<%
	  			
	  			for( int i = 0; i < propertyLangFormat.length; i++)
	  			{          
	  		       %>language[<%=i+1%>]=[{<%=propertyLangFormat[i]%>}]; 
	  		    <%}%>
	  			
	  			
	  			
	  			<% String fundImage = ""; 
	  			try{
  					HashMap<Integer, String> varFundImage = (HashMap<Integer,String>) session.getAttribute("fundImage");
  					if (varFundImage != null){
	  						for(Map.Entry<Integer, String> entry : varFundImage.entrySet()) {
	  							fundImage += "{\"id\":"+entry.getKey()+", \"urlImage\":\""+entry.getValue()+"\"},\n";
	  						}
  						}
  					} catch (Exception e){
  						System.out.print("ExceptionED: ");
  	  			        System.out.println(e.getMessage());
  					}
	  			%>
	  			
	  			var fundImages = [<%=fundImage.substring(0, fundImage.length()-2)+"\n" %>];
	  			
	  			<% String listCountry = "{\"id\":\"\", \"name\":\"País\",\"shortName\":\"\"},\n";
				ArrayList<Country> varCountry = (ArrayList<Country>) session.getAttribute("country");
				Iterator<Country> iterCountry = varCountry.iterator();
				while (iterCountry.hasNext()) {
						Country countryTmp = iterCountry.next();
						listCountry += "{\"id\":\"" + countryTmp.getId() + "\", \"name\":\""+countryTmp.getName()+"\",\"shortName\":\"" + countryTmp.getShortName()+ "\"  },\n";
					}%>
	  			var country = [<%=listCountry.substring(0, listCountry.length()-2)+"\n" %>];
	  			
	  			<% String listPaymentDesc1 = "{\"id\":\"\", \"name\":\"Forma de Pago\",\"lang\":\"1\"},\n";
	  			   String listPaymentDesc2 = "{\"id\":\"\", \"name\":\"Way to Pay\",\"lang\":\"2\"},\n";
	  			   
				ArrayList<PaymentTypeDescription> varPaymentTypeDescription = (ArrayList<PaymentTypeDescription>) session.getAttribute("paymentTypeDescription");
				Iterator<PaymentTypeDescription> iterPaymentTypeDescription = varPaymentTypeDescription.iterator();
				while (iterPaymentTypeDescription.hasNext()) {
						PaymentTypeDescription paymentDescTmp = iterPaymentTypeDescription.next();
						if (paymentDescTmp.getLangId() == 1) {
							listPaymentDesc1 += "{\"id\":\"" + paymentDescTmp.getPaymentTypeId() + "\", \"name\":\""+paymentDescTmp.getLabel()+"\",\"lang\":\"" + paymentDescTmp.getLangId()+ "\"  },\n";
						} else {
							listPaymentDesc2 += "{\"id\":\"" + paymentDescTmp.getPaymentTypeId() + "\", \"name\":\""+paymentDescTmp.getLabel()+"\",\"lang\":\"" + paymentDescTmp.getLangId()+ "\"  },\n";
						}
					}%>

	  			var paymentDesc = [];
	  			paymentDesc[1] = [<%=listPaymentDesc1.substring(0, listPaymentDesc1.length()-2)+"\n"%>];
	  			paymentDesc[2] = [<%=listPaymentDesc2.substring(0, listPaymentDesc2.length()-2)+"\n"%>];
	  			
	  			
	  			var nameAuthenticate = "";
	  			var identityProviderId = "";
	  			var contentVar = "";
	  			<%
	  			
	  			String nameAuthenticate = (String) session.getAttribute("firstName");
	  			String identityProviderId = (String) session.getAttribute("identityProviderId");
	  			
	  			if (nameAuthenticate != null && nameAuthenticate != "null" && nameAuthenticate.length() > 0){ %>
	  				nameAuthenticate = "<%=nameAuthenticate%>";
		  			identityProviderId = "<%=identityProviderId%>";
		  			
		  			if (nameAuthenticate!=null && nameAuthenticate != "null" && nameAuthenticate.length > 0) {
		  				$('#login').hide();
		  				$('#lang-is-registered').hide();
		  				$('#lang-sign-in').hide();
		  				$('#lang-sign-in2').text("Cerrar sesión.");
		  				$('#lang-is-registered2').text("Hola "+nameAuthenticate+". ");
		  				$('#lang-is-registered2').show();
		  				$('#lang-sign-in2').show();
		  				if (identityProviderId!=null && identityProviderId != "null" && identityProviderId.length > 0){
			  				$("#login-with").text(identityProviderId);
			  			}
		  			}
	  			<%}
	  			%>
	  			
	</script>

	<script src="${materializeJs}"></script>
	<script src="${validateJs}"></script>
	<script src="${animateJs}"></script>
	<script src="${initJs}"></script>
	<script src="${paymentAuthorizeJs}"></script>
	<script>
  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      testAPI(response.authResponse.accessToken);
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into this app.';
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
      document.getElementById('status').innerHTML = 'Please log ' +
        'into Facebook.';
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }
  function logoutFacebook() {
	  FB.logout(function(response) {
		  // user is now logged out
		});
  }
  window.fbAsyncInit = function() {
  FB.init({
    appId      : '292927431105648',
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.8' // use graph api version 2.8
  });

  // Now that we've initialized the JavaScript SDK, we call 
  // FB.getLoginStatus().  This function gets the state of the
  // person visiting this page and can return one of three states to
  // the callback you provide.  They can be:
  //
  // 1. Logged into your app ('connected')
  // 2. Logged into Facebook, but not your app ('not_authorized')
  // 3. Not logged into Facebook and can't tell if they are logged into
  //    your app or not.
  //
  // These three cases are handled in the callback function.

  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });

  };

  // Load the SDK asynchronously
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "http://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  // Here we run a very simple test of the Graph API after login is
  // successful.  See statusChangeCallback() for when this call is made.
  function testAPI(accessToken2) {
    //console.log('Welcome!  Fetching your information.... ');
    loginExternal(1,accessToken2);
    
  }
  
</script>
	
<script src="https://apis.google.com/js/platform.js" async defer></script>
<script language="javascript">
	function onSignIn(googleUser) {
		loginExternal(2,googleUser.getAuthResponse().id_token);
	  var profile = googleUser.getBasicProfile();
	  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
	  console.log('Name: ' + profile.getName());
	  console.log('Image URL: ' + profile.getImageUrl());
	  console.log('Email: ' + profile.getEmail());
	  console.log('Token: '+googleUser.getAuthResponse().id_token);
	}
</script>
<script>
  function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
  }
</script>


</body>
</html>