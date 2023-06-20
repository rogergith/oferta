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
<%@page import="com.weavx.web.utils.UtilPropertiesLoader" %>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>${title}</title>
<c:url var="home" value="/" scope="request" />
<spring:url value="/resources/core/mod/elephant/elephant.css" var="elephant" />
<spring:url value="/resources/core/mod/elephant/elephant.js" var="elephantJs" />
<spring:url value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700" var="fonts" />
<spring:url value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" var="iconsCSS" /> 
<spring:url value="/resources/core/mod/application/application.css" var="application" />
<spring:url value="/resources/core/mod/application/application.js" var="applicationJs" />
<spring:url value="/resources/core/mod/vendor/vendor.min.css" var="vendor" />
<spring:url value="/resources/core/mod/vendor/vendor.min.js" var="vendorJs" />
<spring:url value="/resources/core/css/demo.min.css" var="demo" />
<spring:url value="/resources/core/mod/datatable/datatable.min.css" var="dataTableCss" />
<spring:url value="/resources/core/mod/datatable/datatable.min.js" var="dataTableJs" />
<spring:url value="/resources/core/js/init.js" var="initJs" />
<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.js" var="dataTablesbuttons" />
<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.css" var="dataTablebuttonscss" />
<spring:url value="/resources/core/js/buttons.html5.min.js" var="dataTablesbuttons5" />
<spring:url value="/resources/core/js/jszip.min.js" var="jszipJS" />
<spring:url value="/resources/core/js/jquery.mask.min.js" var="maskJs" />
<spring:url value="/resources/core/mod/admin/admin.js" var="admin" />
<spring:url value="/resources/core/mod/paypal/paypal.css" var="reportCSS" />
<spring:url value="/resources/core/mod/paypal/paypal.js" var="paypalJS" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.css" var="loadingCSS" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.js" var="loadingJS" />


<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="description" content="">
<link rel="apple-touch-icon" sizes="180x180" href="resources/core/img/Favicon.png">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="32x32">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="16x16">
<link rel="manifest" href="manifest.json">
<link rel="mask-icon" href="safari-pinned-tab.svg" color="#448aff">
<meta name="theme-color" content="#ffffff">
<link rel="stylesheet" href="${fonts}">
<link rel="stylesheet" href="${vendor}">
<link rel="stylesheet" href="${elephant}">
<link rel="stylesheet" href="${application}">
<link rel="stylesheet" href="${dataTableCss}">
<link rel="stylesheet" href="${dataTablebuttonscss}">
<link rel="stylesheet" href="${reportCSS}">
<link rel="stylesheet" href="${iconsCSS}">
<link rel="stylesheet" href="${loadingCSS}">
</head>
<body class="layout layout-header-fixed">
	<%@ include file="/WEB-INF/views/jsp/header.jsp"%>
	<% String dashboard = UtilPropertiesLoader.getInstance().getPropDashboard("dashboard.active");%>
	<div class="layout-main">
		<%@ include file="/WEB-INF/views/jsp/menu.jsp"%>

		<div class="layout-content">
			<div class="layout-content-body">
				<div class="title-bar">
					<h1 class="title-bar-title">
						<span class="d-ib">Registro de pagos Paypal</span>
						<span class="d-ib"> <span class="sr-only"></span> </span>
					</h1>
					<p class="title-bar-description">
						<small> </small>
					</p>
				</div>
				<div class="row gutter-xs" style="margin-top: 30px;">
					<div class="col-xs-12 ">
					 <form id="register-paypal-record" class="form form-horizontal" >
							
							<div class="filters" style="">

								<div class="form-group form-group-md has-feedback">
									<label for="fname" class="col-sm-1  control-label">Nombre:</label>
									<div class="col-sm-9 input-group">
									<input data-msg-required="First name is required" type="text" class=" form-control" id="fname" name="fname" placeholder="Nombre" data-error-icon="icon-exclamation"> 
									<span class="form-control-feedback" aria-hidden="true">
											<span class="icon"></span>
									 </span>
									 <div class="custom-message-error"></div>
									</div>
								</div>
								<div class="form-group form-group-md has-feedback">
									<label for="lname" class="col-sm-1  control-label">Apellido:</label>
									<div class="col-sm-9 input-group">
									<input  type="text" class=" form-control" id="lname" name="lname" placeholder="Apellido">
									<span class="form-control-feedback" aria-hidden="true">
											<span class="icon"></span>
										  </span>
										  <div class="custom-message-error"></div>									
									</div>
									</div>
								<div class="form-group form-group-md has-feedback">
									<label for="email" class="col-sm-1  control-label">Correo:</label>
									<div class="col-sm-9 input-group">
									<input  type="text" class=" form-control" id="email" name="email" placeholder="Correo"> 
									<span class="form-control-feedback" aria-hidden="true">
											<span class="icon"></span>
										  </span>
									<div class="custom-message-error"></div>

									</div>

								</div>
								
							</div>
								
								<!--<select id="paymentGW" class="custom-select form-control2 form-control">
									<option value="">PaymentGateway</option>
									<option value="Paypal" selected="selected">Paypal</option>

								</select> -->
						<div class="col-sm-11">
							<div class="row gutter-sm">
							<div class="form-group form-group-md">
								<div class="col-sm-6" style="margin-right: 9px;  padding-left: 12px;">
								<label for="funds" class="col-sm-2 control-label">Evento:</label>	
								<div class=" input-group">
									
										<span class="input-group-addon">
												<span class="icon icon-ticket"></span>
											</span>				
								<select id="funds" name="funds" class="custom-select  form-control">
									
<%
	ArrayList<ListFund> varListFund = (ArrayList<ListFund>) session.getAttribute("listFund");
	Iterator<ListFund> iterListFund = varListFund.iterator();
	while (iterListFund.hasNext()) {
		ListFund listFundTmp = iterListFund.next();
%>
									<option value=<%=listFundTmp.getId()%> selected="selected"><%=listFundTmp.getName()%></option>
<%
	}
%>
<%
				JSONObject amountByFund = (JSONObject) session.getAttribute("amountByFund");				
				%>
					</select> 	
					

					</div>
					<div class="input-group">
					<div class="custom-message-error"></div>
					</div>
				</div>
						<div class="col-sm-5">
						<label for="amount" class="col-sm-2 control-label">Monto:</label>
						<div class=" input-group">
							<span class="input-group-addon"><span class="icon icon-money"></span></span>
								<select  id="amount" name='amount' class="custom-select  form-control">
								
									

								</select> 
							</div>
							<div class="input-group">
								<div class="custom-message-error"></div>
							</div>
						</div>
							</div>
							</div>
						</div>
								 <input type="hidden" value="<%=session.getAttribute("firstName")%> <%=session.getAttribute("LastName")%>" class="form-control2 form-control" id="source" placeholder="Source">
								<input type="hidden" value="ADMIN" class=" form-control" id="medium" placeholder="Medium">
							</div>
							<div class="form-grop form-group-md">
							<div class="col-sm-offset-4 col-sm-3">
							<div class="input-with-icon">
								<button type="button" class="btn btn-primary pull-right btn-block"
									style="margin-left: 15px;" id="externalPurchase">Registrar Usuario
									</button>
									<div style="margin-left:90px; display: none;" id="paypal-form-spinner" class="spinner spinner-primary spinner-lg pos-r sq-100" style="display:none; margin-top:15px;"></div>

							</div>
						</div>
						</div>
					</form>
					</div>
				</div>
			</div>
		</div>

		<div class="layout-footer">
			<div class="layout-footer-body">
				<small class="version">Version 1.0.1</small> <small
					class="copyright">2018 &copy; <a
					href="">Seminario</a></small>
			</div>
		</div>
	<div class="theme"></div>
	
	<script type="text/javascript">
	var amountByFund = <%=amountByFund%>;
    </script>
	
	<script src="${vendorJs}"></script>
	<script src="${elephantJs}"></script>
	<script src="${applicationJs}"></script>
	<script src="${dataTableJs}"></script>
	<script src="${dataTablesbuttons}"></script>
	<script src="${jszipJS}"></script>
	<script src="${dataTablesbuttons5}"></script>
	<script src="${maskJs}"></script>
	<script src="${loadingJS}"></script>
	<script src="${admin}"></script><!-- 
	<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/jquery.validate.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/additional-methods.min.js"></script> -->
	<script defer src="${paypalJS}"></script>
	<script src="${initJs}"></script>

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