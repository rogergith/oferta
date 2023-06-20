<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="com.weavx.web.model.AdminRole"%>
<%@page import="com.weavx.web.model.Amount" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.json.JSONObject" %>
<%@page import="com.weavx.web.model.PaymentTypeDescription" %>
<%@page import="com.weavx.web.model.PaymentGatewaysDD" %>
<%@page import="com.weavx.web.model.TransactionStatus" %>
<%@page import="com.weavx.web.model.CustomerApplications" %>
<%@page import="com.weavx.web.model.ListFund" %>
<%@page import="com.weavx.web.model.TxSources" %>
<%@page import="com.weavx.web.model.TxCampaing" %>
<%@page import="com.weavx.web.model.ExternalPaymentType" %>
<%@page import="com.weavx.web.utils.UtilPropertiesLoader" %>
<%@ include file="/WEB-INF/views/jsp/include.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>${title}</title>
    <c:url var="home" value="/" scope="request"/>
    <spring:url value="/resources/core/mod/elephant/elephant.css?v=1.0.1"
                var="elephant"/>
    <spring:url value="/resources/core/mod/elephant/elephant.js?v=1.0.1"
                var="elephantJs"/>
    <spring:url
            value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700"
            var="fonts"/>
    <spring:url
            value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
            var="iconsCSS"/>
    <spring:url value="/resources/core/mod/application/application.css?v=1.0.1"
                var="application"/>
    <spring:url value="/resources/core/mod/application/application.js?v=1.0.1"
                var="applicationJs"/>
    <spring:url value="/resources/core/mod/vendor/vendor.min.css?v=1.0.1"
                var="vendor"/>
    <spring:url value="/resources/core/mod/vendor/vendor.min.js?v=1.0.1"
                var="vendorJs"/>
    <spring:url value="/resources/core/css/demo.min.css?v=1.0.1" var="demo"/>
    <spring:url value="/resources/core/mod/datatable/datatable.min.css?v=1.0.1"
                var="dataTableCss"/>
    <spring:url value="/resources/core/js/init.js?v=1.0.2" var="initJs"/>
    <spring:url value="/resources/core/js/restrictedevent/restrictActualEvent.js?v=1.0.1"
                var="restrictActualEventJs"/>
    <spring:url
            value="/resources/core/mod/datatable/dataTables.buttons.min.css?v=1.0.1"
            var="dataTablebuttonscss"/>
    <spring:url value="/resources/core/js/jszip.min.js?v=1.0.1" var="jszipJS"/>
    <spring:url value="/resources/core/js/jquery.mask.min.js?v=1.0.1" var="maskJs"/>
    <spring:url value="/resources/core/mod/admin/admin.js?v=1.0.1" var="admin"/>
    <spring:url value="/resources/core/mod/stripe/stripe.css?v=1.0.1"
                var="stripeCSS"/>
    <spring:url value="/resources/core/mod/stripe/stripe.js?v=1.0.1" var="stripeJS"/>
    <spring:url value="/resources/core/mod/black-list/blacklist.js?v=1.0.1" var="blackListJs"/>
    <spring:url value="/resources/core/mod/black-list/blacklist.css?v=1.0.1" var="blackListCSS"/>
    <spring:url
            value="/resources/core/mod/loading/jquery.loadingModal.min.css?v=1.0.1"
            var="loadingCSS"/>
    <spring:url
            value="/resources/core/mod/loading/jquery.loadingModal.min.js?v=1.0.1"
            var="loadingJS"/>
    <spring:url value="/resources/core/js/paymentStripe.js?v=1.0.1"
                var="paymentStripeJs"/>
    <spring:url value="/resources/core/css_old/call-center.css?v=1.0.1"
                var="callCenterCSS"/>
                
    <spring:url value="/resources/core/mod/users/users.css?v=1.0.1" var="userCSS" />

    <spring:url value="/resources/core/js/jsQR.js?v=1.0.1" var="qrJS"/>
    <spring:url value="/resources/core/js/qr-reader.js?v=1.0.1" var="qrReader"/>
    <spring:url value="/resources/core/js/custom-qr.js?v=1.0.1" var="customQR"/>
    <spring:url value="/resources/core/css/scan-qr.css?v=1.0.1" var="qrCSS" />

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
    <link rel="stylesheet" href="${userCSS}">
    <link rel="stylesheet" href="${blackListCSS}">
    <link rel="stylesheet" href="${qrCSS}">
    <style>
    	.has-error {
    		color: red;
    	}
    
    </style>
</head>
<body class="layout layout-header-fixed">


<%@ include file="/WEB-INF/views/jsp/header.jsp" %>

<div class="layout-main">
  <%@ include file="/WEB-INF/views/jsp/menu.jsp" %>

  <div class="layout-content">
    <div class="layout-content-body ">
      <div class="title-bar">
        <h1 class="title-bar-title">
          <span class="d-ib">Scan QR</span> 
          <span class="d-ib">
            <span class="sr-only"></span>
          </span>
        </h1>
        <p class="title-bar-description">
          <small></small>
        </p>
      </div>

      <div class="content-qr">
        <input type="button" id="openreader-btn" class="btn btn-primary" value="Scan QRCode"/>

        <div id="container-camera" class="container-camera"></div>

        <div id="result"></div>
      </div>
    </div> 
  </div>
</div>

<div class="layout-footer">
    <div class="layout-footer-body">
        <small class="version">Version 1.0.1</small>
        <small class="copyright">2018
            &copy; <a href="">Seminario</a>
        </small>
    </div>
</div>
<div class="theme"></div>


<script>
	<%  
		String rolesObj = "[";
		@SuppressWarnings("unchecked")
		ArrayList<AdminRole> roles = (ArrayList<AdminRole>) currentUser.getRoleFunctions();
		Iterator<AdminRole> iterRoles = roles.iterator();
		while (iterRoles.hasNext()) {
			AdminRole funcRole = iterRoles.next();
			rolesObj += "{\"id\":\"" + funcRole.getId() + "\", \"name\":\""+funcRole.getName()+"\",\"description\":\"" + funcRole.getDescription()+ "\"  },\n";
		}
		rolesObj += "]";
	%>
  var rolesFunc = <%=rolesObj%>
</script>

<script src="${vendorJs}"></script>
<script src="${elephantJs}"></script>
<script src="${applicationJs}"></script>
<script src="${jszipJS}"></script>
<script src="${maskJs}"></script>
<script src="${loadingJS}"></script>
<script src="${admin}"></script>
<script src="${qrJS}"></script>
<script src="${qrReader}"></script>
<script src="${customQR}"></script>

</body>
</html>