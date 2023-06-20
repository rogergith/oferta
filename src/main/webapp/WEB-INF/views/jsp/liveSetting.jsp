<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.weavx.web.model.Amount"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.weavx.web.model.PaymentTypeDescription"%>
<%@page import="com.weavx.web.model.PaymentGatewaysDD"%>
<%@page import="com.weavx.web.model.TransactionStatus"%>
<%@page import="com.weavx.web.model.CustomerApplications"%>
<%@page import="com.weavx.web.model.ListFund"%>
<%@page import="com.weavx.web.model.TxSources"%>
<%@page import="com.weavx.web.model.TxCampaing"%>
<%@page import="com.weavx.web.model.ExternalPaymentType"%>
<%@page import="com.weavx.web.utils.UtilPropertiesLoader"%>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>${title}</title>
<c:url var="home" value="/" scope="request" />
<spring:url value="/resources/core/mod/elephant/elephant.css"
	var="elephant" />
<spring:url value="/resources/core/mod/elephant/elephant.js"
	var="elephantJs" />
<spring:url
	value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700"
	var="fonts" />
<spring:url
	value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
	var="iconsCSS" />
<spring:url value="/resources/core/mod/application/application.css"
	var="application" />
<spring:url value="/resources/core/mod/application/application.js"
	var="applicationJs" />
<spring:url value="/resources/core/mod/vendor/vendor.min.css"
	var="vendor" />
<spring:url value="/resources/core/mod/vendor/vendor.min.js"
	var="vendorJs" />
<spring:url value="/resources/core/js_old/jquery.validate.js"
	var="validateJs" />
<spring:url value="/resources/core/css/demo.min.css" var="demo" />
<spring:url value="/resources/core/js/demo.min.js" var="demoJs" />
<spring:url value="/resources/core/mod/datatable/datatable.min.css"
	var="dataTableCss" />
<spring:url value="/resources/core/mod/datatable/datatable.min.js"
	var="dataTableJs" />
<spring:url value="/resources/core/js/init.js?v=1.0.1" var="initJs" />
<spring:url value="/resources/core/mod/live-assets/live-assets.js?v=1.0.5"
	var="liveAssetsJs" />
<spring:url value="/resources/core/mod/live-assets/live-assets.css?v=1.0.5" var="liveAssetCss" />
<spring:url
	value="/resources/core/mod/datatable/dataTables.buttons.min.js"
	var="dataTablesbuttons" />
<spring:url
	value="/resources/core/mod/datatable/dataTables.buttons.min.css"
	var="dataTablebuttonscss" />
<spring:url value="/resources/core/js/buttons.html5.min.js"
	var="dataTablesbuttons5" />
<spring:url value="/resources/core/js/jszip.min.js" var="jszipJS" />
<spring:url value="/resources/core/js/jquery.mask.min.js" var="maskJs" />
<spring:url value="/resources/core/js/jquery-ui.js" var="maskUI" />
<spring:url value="/resources/core/mod/admin/admin.js" var="admin" />
<spring:url value="/resources/core/mod/stripe/stripe.css"
	var="stripeCSS" />
<spring:url value="/resources/core/mod/stripe/stripe.js" var="stripeJS" />
<spring:url
	value="/resources/core/mod/loading/jquery.loadingModal.min.css"
	var="loadingCSS" />
<spring:url
	value="/resources/core/mod/loading/jquery.loadingModal.min.js"
	var="loadingJS" />
<spring:url value="/resources/core/js/paymentStripe.js"
	var="paymentStripeJs" />
<spring:url value="/resources/core/css_old/call-center.css"
	var="callCenterCSS" />

<script type="text/javascript" src="https://js.stripe.com/v2/"></script>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="description" content="">
<link rel="apple-touch-icon" sizes="180x180"
	href="resources/core/img/Favicon.png">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png"
	sizes="32x32">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png"
	sizes="16x16">
<link rel="manifest" href="manifest.json">
<link rel="mask-icon" href="safari-pinned-tab.svg" color="#448aff">
<meta name="theme-color" content="#ffffff">
<link rel="stylesheet" href="${fonts}">
<link rel="stylesheet" href="${vendor}">
<link rel="stylesheet" href="${elephant}">
<link rel="stylesheet" href="${application}">
<link rel="stylesheet" href="${dataTableCss}">
<link rel="stylesheet" href="${dataTablebuttonscss}">
<link rel="stylesheet" href="${stripeCSS}">
<link rel="stylesheet" href="${iconsCSS}">
<link rel="stylesheet" href="${loadingCSS}">
<link rel="stylesheet" href="${callCenterCSS}">
<link rel="stylesheet" href="${liveAssetCss}">
</head>
<body class="layout layout-header-fixed">
	<%@ include file="/WEB-INF/views/jsp/header.jsp"%>
	<%
		String dashboard = UtilPropertiesLoader.getInstance().getPropDashboard("dashboard.active");
		String stripeKey = (String) session.getAttribute("stripeKey");
	%>
	<div class="layout-main">

		<%@ include file="/WEB-INF/views/jsp/menu.jsp"%>

		<div class="layout-content">
			<div class=" layout-content-body ">
				<div class="title-bar">
					<h1 class="title-bar-title">
						<span class="d-ib">Live Assets Events</span> <span class="d-ib">
							<span class="sr-only"></span>
						</span>
					</h1>
					<p class="title-bar-description">
						<small> </small>
					</p>
				</div>
				<!--  FORM WIZARD -->

				<!-- FORM WIZARD -->
				<!-- FORM BODY -->
				<!-- FORM MASSIVE CUSTOMER -->
				<div id="restrict-event-container-form" style="margin-top: 5em;">
					<form id="assets-event-form" >
						<div class="col-sm-12 col-md-4 select-language-config flex-container">
							<label for="config-lang-select">Select a language to config the assets:</label>
							<select id="config-lang-select" class="form-control" name="config-lang-select">
							</select>
							<span class="custom-message-error"></span>
						</div>
						<div class="col-sm-12 col-md-2">
							<div class="lds-ring lang-conf-loader" style="display:none;"><div></div><div></div><div></div><div></div></div>
						</div>
						<div id="video-container" class='col-sm-12' style="display:none;">
							
							<div>
							  <strong>Duration:</strong><span id="video-duration"></span>
							</div>
						</div>
						<div class="row tab-pane">
							
							<div id="fields-config" class="col-sm-12"  style="display:none;">
								<div id="dynamic-form-wrapper-tab"></div>
								<div id="dynamic-form-wrapper" class="demo-form-wrapper"></div>
								<div class="row flex-container" >
								<button id="save-assets" class="btn btn-md btn-primary">Save assets</button>
								<div class="lds-ring save-assets-loader" style="display:none;"><div></div><div></div><div></div><div></div></div>
								</div>
							</div>

						</div>

					</form> 
				</div>
				<!-- /FORM MASSIVE CUSTOMER -->
			

				<div class="donation-form" style="display: none;">
					<form action="" method="POST" id="payment-form">
						<span class="payment-errors"></span>

					</form>

				</div>
				<!-- /FORM BODY -->
			</div>
		</div>
	</div>


	<div class="layout-footer">
		<div class="layout-footer-body">
			<small class="version">Version 1.0.1</small> <small class="copyright">2018
				&copy; <a href="">Seminario</a>
			</small>
		</div>
	</div>
	<!-- MODAL SUCCESS body -->	
	<div id="successModalAlert" tabindex="-1" role="dialog" class="modal fade in" style="display: none;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">
              <span aria-hidden="true">×</span>
              <span class="sr-only">Close</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="text-center">
              <span class="text-success icon icon-check icon-5x"></span>
              <h3 class="text-success">Success</h3>
             	<h4>The assets were saved succesfully!</h4>
              <div class="m-t-lg">
                <button id="success-modal-close" class="btn btn-success" data-dismiss="modal" type="button">Close</button>
              </div>
            </div>
          </div>
          <div class="modal-footer"></div>
        </div>
      </div>
    </div>
    <!-- //MODAL SUCCESS body -->	
    <!-- //MODAL error body -->	
    <div id="dangerModalAlert" tabindex="-1" role="dialog" class="modal fade in" style="display: none;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">
              <span aria-hidden="true">×</span>
              <span class="sr-only">Close</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="text-center">
              <span class="text-danger icon icon-times-circle icon-5x"></span>
              <h3 class="text-danger">Error</h3>
             	<h5 id="error-modal-msg"></h5>
              <div class="m-t-lg">
                <button id="error-modal-close" class="btn btn-danger" data-dismiss="modal" type="button">Accept</button>
              </div>
            </div>
          </div>
          <div class="modal-footer"></div>
        </div>
      </div>
    </div>
    <!-- //MODAL error body -->	
	<div class="theme"></div>
	<script type="text/javascript">

		var ErrorActualIdiom = 1;
		var dataErrors = [
				null,
				{
					"lettersonly_error_1" : "Letras y espacios únicamente por favor",
					"step2_error_6" : "Su apellido no debe tener más de 20 caracteres",
					"user_error_tdc_decline" : "Tu Tarjeta ha sido negada. Por favor intenta con otra tarjeta.",
					"user_error_3" : "Debe colocar usuario u contraseña.",
					"step3_error_4" : "Proporcione una fecha de caducidad",
					"vmcardsonly_error_1" : "Introduzca un número de tarjeta de crédito Visa o Master válido.",
					"step2_error_7" : "Por favor, introduce una dirección de correo electrónico válida",
					"step2_error_1" : "Primer nombre requerido",
					"step3_error_7" : "Su cvv no debe tener más de 4 caracteres",
					"step2_error_2" : "Su primer nombre debe tener al menos 3 caracteres",
					"user_error_5" : "El campo es requerido.",
					"step3_error_2" : "Su tarjeta de crédito debe tener un mínimo de 12 caracteres",
					"step3_error_3" : "Su tarjeta de crédito no debe tener más de 16 caracteres",
					"step2_error_4" : "Proporcione un apellido",
					"step3_error_1" : "Proporcione una tarjeta de crédito",
					"step2_error_8" : "El correo electrónico no coincide",
					"user_error_1" : "El usuario ya existe",
					"step3_error_6" : "Su cvv debe tener al menos 3 caracteres",
					"step2_error_5" : "Su apellido debe tener al menos 3 caracteres",
					"step3_error_5" : "Proporcione un cvv",
					"numberand_error_1" : "Su tarjeta de crédito no puede contener ninguna letra",
					"step2_error_3" : "Su primer nombre no debe tener más de 20 caracteres",
					"user_error_4" : "Usuario no registrado",
					"stepL_error_1" : "Por favor, introduce una dirección de correo electrónico válida",
					"user_error_2" : "Usuario o contraseña incorrectos.",
					"step3_error_8" : "Su cvv no puede contener ninguna letra",
					"expdate_error_1" : "Por favor, introduzca una fecha de vencimiento válida",
					"user_error_6" : "Ha ocurrido un error, intente más tarde."
				},
				{
					"expdate_error_1" : "Please enter a valid expiration date",
					"user_error_1" : "User already exists",
					"step2_error_4" : "Please provide a Last name",
					"step3_error_1" : "Please provide a credit card",
					"numberand_error_1" : "Your credit card can not content any letter",
					"user_error_2" : "Incorrect user or password.",
					"user_error_3" : "You must enter username and password.",
					"step3_error_7" : "Your cvv must not be more than 4 characters long",
					"step3_error_6" : "Your cvv must be at least 3 characters long",
					"stepL_error_1" : "Please enter a valid email address",
					"user_error_4" : "Unregistered user",
					"step3_error_3" : "Your credit card must not be more than 16 characters long",
					"lettersonly_error_1" : "Letters and spaces only please",
					"step2_error_6" : "Your Last name must not be more than 20 characters long",
					"step2_error_3" : "Your first name must not be more than 20 characters long",
					"step2_error_5" : "Your Last name must be at least 3 characters long",
					"step2_error_1" : "Please provide a a first name",
					"vmcardsonly_error_1" : "Please enter a valid Visa or Master credit card number.",
					"step2_error_2" : "Your first name must be at least 3 characters long",
					"user_error_tdc_decline" : "Your card has been declined. Please use another one.",
					"step3_error_4" : "Please provide a expiration date",
					"step3_error_5" : "Please provide a cvv",
					"step2_error_7" : "Please enter a valid email address",
					"user_error_5" : "This field is required.",
					"user_error_6" : "An error occurred, please try again later.",
					"step3_error_8" : "Your cvv card can not content any letter",
					"step3_error_2" : "Your credit card must be at least 12 characters long",
					"step2_error_8" : "The email does not match"
				} ];
	</script>
	

	<script src="${vendorJs}"></script>
	<script src="${elephantJs}"></script>
	<script src="${applicationJs}"></script>
	<script src="${demoJs}"></script>
	<script src="${dataTableJs}"></script>
	<script src="${dataTablesbuttons}"></script>
	<script src="${jszipJS}"></script>
	<script src="${dataTablesbuttons5}"></script>
	<script src="${maskJs}"></script>
	<script src="${maskUI}"></script>
	<script src="${loadingJS}"></script>
	<script src="${admin}"></script>
	<script src="${validateJs}"></script>
	<script src="https://player.vimeo.com/api/player.js"></script>
	<script src="${liveAssetsJs}"></script>
	

	<script>
		var formValidation;
	</script>

	<!-- <script src="${validateJS}"></script>
	<script src="${validateMethodsJS}"></script> -->

	<!-- 	<script type="text/javascript">
		$("#menuReport").addClass("active");
	</script> -->
	<!--     <script>
//       (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
//       (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
//       m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
//       })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
//       ga('create', 'UA-83990101-1', 'auto');
//       ga('send', 'pageview');
    </script> -->
</body>
</html>