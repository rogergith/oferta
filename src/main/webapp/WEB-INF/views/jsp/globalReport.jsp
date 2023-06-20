<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.weavx.web.model.Amount"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
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
<spring:url value="/resources/core/mod/globalReport/globalReport.css" var="globalReportCSS" />
<spring:url value="/resources/core/mod/globalReport/globalReport.js" var="globalReportJS" />
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
<link rel="stylesheet" href="${globalReportCSS}">
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
						<span class="d-ib">Global Report<small></span>
						<span class="d-ib"> <span class="sr-only"></span> </a> </span>
					</h1>
					<p class="title-bar-description">
						<small> </small>
					</p>
				</div>
				<div class="row gutter-xs">
					<div class="col-xs-12">
						<div class="card">
							<div class="card-header">
								<div class="card-actions">
									<button type="button" class="card-action card-toggler"
										title="Collapse"></button>
									<button type="button" class="card-action card-reload"
										title="Reload"></button>
									<button type="button" class="card-action card-remove"
										title="Remove"></button>
								</div>
								<strong>Filters</strong>
							</div>
							<div class="filters" style="padding: 20px 30px 10px 10px;">

								<div class="input-group input-daterange"
									data-provide="datepicker" data-date-autoclose="true"
									data-date-format="mm-dd-yyyy" style="margin: 10px 0px;">
									<div>
										<input class="form-control2 form-control" id="fromDate"
											Placeholder="From" type="tel" value=""
											style="margin-right: 3px;"> <span
											class="icon icon-calendar input-icon"></span>
									</div>
									<div style="position: relative; float: left;">
										<input class="form-control2 form-control" id="toDate"
											placeholder="To" type="tel" value=""> <span
											class="icon icon-calendar input-icon"></span>
									</div>
								</div>
								<input type="text" class="form-control2 form-control" id="fname" placeholder="Name"> 
								<input type="text" class="form-control2 form-control" id="lname" placeholder="Last name">
								<input type="text" class="form-control2 form-control" id="email" placeholder="Email"> 
								<select id="paymentGW" class="custom-select form-control2 form-control">
									<option value="" selected="selected">Payment Gateway</option>
<%
	ArrayList<PaymentGatewaysDD> varPayment = (ArrayList<PaymentGatewaysDD>) session.getAttribute("paymentGatewaysDD");
	Iterator<PaymentGatewaysDD> iterPayment = varPayment.iterator();
	while (iterPayment.hasNext()) {
		PaymentGatewaysDD paymentTmp = iterPayment.next();
%>
									<option value=<%=paymentTmp.getId()%>><%=paymentTmp.getName()%></option>
<%
	}
%>
								</select> 
								<input type="text" class="form-control2 form-control" id="txId" placeholder="Transaction Id">
								<input type="text" class="form-control2 form-control" id="operatorEmail" placeholder="Operator Email"> 
								<input type="tel" class="form-control2 form-control" maxlength="4"
									id="infoPayment" placeholder="Last 4 number"> 
								<select id="funds" class="custom-select form-control2 form-control">
									<option value="" selected="selected">Events</option>
<%
	ArrayList<ListFund> varListFund = (ArrayList<ListFund>) session.getAttribute("listFund");
	Iterator<ListFund> iterListFund = varListFund.iterator();
	while (iterListFund.hasNext()) {
		ListFund listFundTmp = iterListFund.next();
%>
									<option value=<%=listFundTmp.getId()%>><%=listFundTmp.getName()%></option>
<%
	}
%>

								</select>  <select id="containerId" class="custom-select form-control2 form-control">
									<option value="" selected="selected">Container</option>
									<%
										ArrayList<ListFund> varListFund2 = (ArrayList<ListFund>) session.getAttribute("listFund");
										Iterator<ListFund> iterListFund2 = varListFund2.iterator();
										while (iterListFund2.hasNext()) {
											ListFund listFundTmp = iterListFund2.next();
									%>
									<option value="<%=listFundTmp.getBusinessCode()%>"><%=listFundTmp.getBusinessCode()%></option>
									<%
										}
									%>
								</select> <input type="text" class="form-control2 form-control"
									id="authorizeId" placeholder="Authorize Id"> <input
									type="text" class="form-control2 form-control"
									id="approvalNumber" placeholder="Approval Number"> <input
									type="text" class="form-control2 form-control" id="form"
									placeholder="Form"> <input type="tel"
									class="form-control2 form-control" id="txInternalId"
									placeholder="Internal Id"> <select id="applicationName"
									class="custom-select form-control2 form-control">
									<option value="" selected="selected">Application</option>
									<%
										ArrayList<CustomerApplications> varCustomerApplications = (ArrayList<CustomerApplications>) session
												.getAttribute("cApp");
										Iterator<CustomerApplications> iterCustomerApplications = varCustomerApplications.iterator();
										while (iterCustomerApplications.hasNext()) {
											CustomerApplications cAppTmp = iterCustomerApplications.next();
									%>
									<option value=<%=cAppTmp.getId()%>><%=cAppTmp.getName()%></option>
									<%
										}
									%>
								</select>  <select id="txStatus"
									class="custom-select form-control2 form-control">
									<option value="" selected="selected">TxStatus</option>
									<%
										ArrayList<TransactionStatus> varTransactionStatus = (ArrayList<TransactionStatus>) session
												.getAttribute("txStatusDD");
										Iterator<TransactionStatus> iterTransactionStatus = varTransactionStatus.iterator();
										while (iterTransactionStatus.hasNext()) {
											TransactionStatus txStatusTmp = iterTransactionStatus.next();
									%>
									<option value="<%=txStatusTmp.getId()%>"><%=txStatusTmp.getName()%></option>
									<%
										}
									%>
								</select>  <select id="paymentType"
									class="custom-select form-control2 form-control">
									<option value="" selected="selected">PaymentType</option>
									<%
										ArrayList<PaymentTypeDescription> varPaymentTypeDescription = (ArrayList<PaymentTypeDescription>) session
												.getAttribute("paymentTypeDescription");
										Iterator<PaymentTypeDescription> iterPaymentTypeDescription = varPaymentTypeDescription.iterator();
										while (iterPaymentTypeDescription.hasNext()) {
											PaymentTypeDescription paymentDescTmp = iterPaymentTypeDescription.next();
											if (paymentDescTmp.getLangId() == 2) {
									%>
									<option value=<%=paymentDescTmp.getPaymentTypeId()%>><%=paymentDescTmp.getLabel()%></option>
									<%
										}
										}
									%>
								</select>  <select id="source"
									class="custom-select form-control2 form-control">
									<option value="" selected="selected">Source</option>
									<%
										ArrayList<TxSources> varTxSources = (ArrayList<TxSources>) session.getAttribute("txSources");
										Iterator<TxSources> iterTxSources = varTxSources.iterator();
										while (iterTxSources.hasNext()) {
											TxSources txSourcesTmp = iterTxSources.next();
									%>
									<option value="<%=txSourcesTmp.getId()%>"><%=txSourcesTmp.getName()%></option>
									<%
										}
									%>
								</select> <select id="campaing"
									class="custom-select form-control2 form-control">
									<option value="" selected="selected">Campaing</option>
									<%
										ArrayList<TxCampaing> varTxCampaing = (ArrayList<TxCampaing>) session.getAttribute("txCampaing");
										Iterator<TxCampaing> iterTxCampaing = varTxCampaing.iterator();
										while (iterTxCampaing.hasNext()) {
											TxCampaing txCampaingTmp = iterTxCampaing.next();
									%>
									<option value="<%=txCampaingTmp.getId()%>"><%=txCampaingTmp.getName()%></option>
									<%
										}
									%>
								</select>
								 <select id="settlement"
									class="custom-select form-control2 form-control">
									<option value="" selected>Settled</option>									
									<option value="1">Yes</option>
									<option value="0">No</option>															
								</select>
								<input type="text" class="form-control2 form-control" id="affiliateId" placeholder="Affiliate Id">
								<input type="text" class="form-control2 form-control" id="affiliateEmail" placeholder="Affiliate Email">
								 <select id="amount"
									class="custom-select form-control2 form-control">
									<option value="" selected>Amount</option>
									<%
										ArrayList<Amount> varAmount = (ArrayList<Amount>) session.getAttribute("amount");
										Iterator<Amount> iterAmount = varAmount.iterator();
										while (iterAmount.hasNext()) {
											Amount amountTmp = iterAmount.next();
									%>
									<option value="<%=amountTmp.getAmount()%>"><%=amountTmp.getAmount()%></option>
									<%
										}
									%>
									<option value="range">Amounts Range</option>
								</select> <input type="tel" class="form-control2 form-control"
									id="amountMin" placeholder="Amount min"
									style="visibility: hidden;"> <input type="tel"
									class="form-control2 form-control" id="amountMax"
									placeholder="Amount max" style="visibility: hidden;">
							</div> 
							<div class="input-with-icon">
								<button class="btn btn-primary" type="button"
									style="margin-left: 15px;" id="generateGlobalReport">Generate Report
									</button>
							</div>
							<div class="card-body"></div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="layout-footer">
			<div class="layout-footer-body">
				<small class="version">Version 1.0.1</small> <small
					class="copyright">2020 &copy; <a
					href="">${title}</a></small>
			</div>
		</div>
	</div>
	<div class="theme"></div>
	<script src="${vendorJs}"></script>
	<script src="${elephantJs}"></script>
	<script src="${applicationJs}"></script>
	<script src="${dataTableJs}"></script>
	<script src="${initJs}"></script>
	<script src="${dataTablesbuttons}"></script>
	<script src="${jszipJS}"></script>
	<script src="${dataTablesbuttons5}"></script>
	<script src="${maskJs}"></script>
	<script src="${loadingJS}"></script>
	<script src="${admin}"></script>
	<script src="${globalReportJS}"></script>
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