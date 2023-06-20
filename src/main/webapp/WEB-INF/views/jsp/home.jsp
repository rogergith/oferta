<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>
<%@ page import="com.weavx.web.utils.UtilPropertiesLoader"%>
<%@page import="java.util.*" %>
<%
Random rand = new Random();
int n = rand.nextInt(90000) + 10000;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>${title}</title>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Last-Modified" content="0">
<meta http-equiv="Cache-Control" content="no-cache, mustrevalidate">
<meta http-equiv="Pragma" content="no-cache">

<c:url var="home" value="/" scope="request" />

<spring:url value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700" var="fonts" />

<spring:url value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" var="iconsCSS" /> 

<spring:url value="/resources/core/mod/elephant/elephant.css" var="elephantCSS" />
<spring:url value="/resources/core/mod/elephant/elephant.js" var="elephantJS" />

<spring:url value="/resources/core/mod/application/application.css" var="applicationCSS" />
<spring:url value="/resources/core/mod/application/application.js" var="applicationJS" />

<spring:url value="/resources/core/mod/vendor/vendor.min.css" var="vendorCSS" />
<spring:url value="/resources/core/mod/vendor/vendor.min.js" var="vendorJS" />

<spring:url value="/resources/core/mod/datatable/datatable.min.css" var="dataTableCSS" />
<spring:url value="/resources/core/mod/datatable/datatable.min.js" var="dataTableJS" />

<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.css" var="dataTablebuttonsCSS" />
<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.js" var="dataTablesbuttonsJS" />

<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.css" var="loadingCSS" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.js" var="loadingJS" />

<spring:url value="/resources/core/mod/modal/jquery.fancybox.min.css" var="modalCSS" />
<spring:url value="/resources/core/mod/modal/jquery.fancybox.min.js" var="modalJS" />

<spring:url value="/resources/core/mod/admin/admin.css" var="adminCSS" />
<spring:url value="/resources/core/mod/admin/admin.js" var="adminJS" />

<spring:url value="/resources/core/mod/home/home.css" var="homeCSS" />
<spring:url value="/resources/core/mod/home/home.js" var="homeJS" />

<spring:url value="/resources/core/js/buttons.html5.min.js"
var="dataTablesbuttons5"/>
<spring:url value="/resources/core/js/jszip.min.js" var="jszipJS"/>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<meta name="viewport"content="width=device-width,initial-scale=1,user-scalable=no">
<meta name="description" content="">
<link rel="apple-touch-icon" sizes="180x180" href="resources/core/img/Favicon.png">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="32x32">
<link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="16x16">
<link rel="manifest" href="manifest.json">
<link rel="mask-icon" href="safari-pinned-tab.svg" color="#448aff">
<meta name="theme-color" content="#ffffff">
<link rel="stylesheet" href="${fonts}?<%=n%>">
<link rel="stylesheet" href="${vendorCSS}?<%=n%>">
<link rel="stylesheet" href="${elephantCSS}?<%=n%>">
<link rel="stylesheet" href="${applicationCSS}?<%=n%>">
<link rel="stylesheet" href="${dataTableCSS}">
<link rel="stylesheet" href="${dataTablebuttonsCSS}?<%=n%>">
<link rel="stylesheet" href="${adminCSS}?<%=n%>">
<link rel="stylesheet" href="${modalCSS}?<%=n%>">
<link rel="stylesheet" href="${loadingCSS}?<%=n%>">
<link rel="stylesheet" href="${homeCSS}?<%=n%>">
<link rel="stylesheet" href="${iconsCSS}?<%=n%>">

</head>
<body class="layout layout-header-fixed">

	<%@ include file="/WEB-INF/views/jsp/header.jsp"%>
	
	<div class="layout-main">
		<%String dashboard = UtilPropertiesLoader.getInstance().getPropDashboard("dashboard.active");%>
		<%@ include file="/WEB-INF/views/jsp/menu.jsp"%>	
		<%
		if (dashboard.equals("true")) {			
		%>
		<div class="layout-content">
	        <div class="layout-content-body">
	          <div class="title-bar">
	            <div class="title-bar-actions">

	            </div>
	            <h1 class="title-bar-title">
	              <span class="d-ib">Dashboard</span>
	            </h1>
	            <div class="title-bar-description">
	              <small><span id="showing"> </span>   	              
	              <select id="representation" class="selectpicker dropdown" name="period" data-dropdown-align-right="true" data-style="btn-default btn-sm button-custom-select" data-width="fit">
	                <option value="0T">Today</option>
	                <option value="24H">Yesterday</option>
	                <option value="7D">Last 7 days</option>
	                <option value="1M">Last month</option>
	                <option value="3M">Last 3 months</option>
	              </select> 
	              (<span id="date-from"></span> - <span id="date-to"></span>)</small>
	              <a id="reload" class="remove-relaod-id" href="#">Refresh</a>
	              <div class="container-spinner">
	              </div>
	              	<div>
            			<small id="message" class="text-warning"></small>
            		</div>
	            </div>
	          </div>
	          <div class="row gutter-xs">
	            <div class="col-md-4 col-lg-2 col-lg-push-0">
	              <div class="card">
	                <div class="card-body">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="bg-primary circle sq-44">
	                        <span class="icon icon-user"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Donors</h7>
	                      <h4 class="media-heading">
	                        <span id="donors-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-3 col-lg-2 col-lg-push-4">
	              <div class="card">
	                <div class="card-body">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="bg-danger circle sq-44">
	                        <span class="icon icon-usd"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Donors Income</h7>
	                      <h4 class="media-heading">
	                        <span id="income-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-3 col-lg-2 col-lg-pull-2">
	              <div class="card">
	                <div class="card-body">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="bg-primary circle sq-44">
	                        <span><img class="img-icon" src="resources/core/img/icon-people.svg"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Recurrings</h7>
	                      <h4 class="media-heading">
	                        <span id="recurring-donors-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-3 col-lg-2 col-lg-push-2">
	              <div class="card">
	                <div class="card-body">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="bg-danger circle sq-44">
	                        <span><img class="img-icon" src="resources/core/img/icon-dolars.svg"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Recurrings income</h7>
	                      <h4 class="media-heading">
	                        <span id="recurring-income-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-3 col-lg-2 col-lg-pull-4">
	              <div class="card">
	                <div class="card-body bg-primary">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="circle sq-44 color-circle">
	                        <span class="icon icon-user color-icon"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Total Donors</h7>
	                      <h4 class="media-heading">
	                        <span id="total-donors-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-3 col-lg-2 col-lg-pull-0">
	              <div class="card">
	                <div class="card-body bg-danger">
	                  <div class="media">
	                    <div class="media-middle media-left">
	                      <span class="circle sq-44 color-circle">
	                        <span class="icon icon-usd color-icon-danger"></span>
	                      </span>
	                    </div>
	                    <div class="media-middle media-body">
	                      <h7 class="media-heading">Total income</h7>
	                      <h4 class="media-heading">
	                        <span id="total-income-data" class="fw-l"></span>
	                      </h4>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <div class="row gutter-xs">
	            <div class="col-md-6">
	              <div class="card">
	                <div class="card-body">
	                  <h4 class="card-title">Donors</h4>
	                </div>
	                <div class="card-body">
	                  <div class="card-chart">
	                    <canvas id="donors-visitors" ></canvas>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-6">
	              <div class="card">
	                <div class="card-body">
	                  <h4 class="card-title">Incomes</h4>
	                </div>
	                <div class="card-body">
	                  <div class="card-chart">
	                    <canvas id="incomes"></canvas>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          
	         <!-- CARDS -->
	         
	         <!-- Source / Referrals -->
	         <div class="row gutter-xs">
	            <div class="col-md-6">
	              <div id="top-networks" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <!-- <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Traffic Source / Top Referrals</strong>
	                  <a class="link-modal" data-target="top-networks">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">  
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="network-list-top section2"> 
	                        </tbody>
	                      </table>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-6">
	              <div id="income-top" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <button type="button" class="card-action card-reload" title="Reload"></button>
	                   <!--  <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Traffic Source / Top Referrals</strong>
	                  <a class="link-modal" data-target="income-top">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="network-list-top section2">
							</tbody>
	                      </table>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          
	          <!-- Continentes / Countries -->
	          <div class="row gutter-xs">
	            <div class="col-md-6">
	              <div id="top-networks-region" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <button type="button" class="card-action card-reload" title="Reload"></button>
	                    <!-- <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Continents / Top Countries</strong>
	                  <a class="link-modal" data-target="top-networks-region">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="network-list-top section2" >     
	                        </tbody>
	                      </table>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-6">
	              <div id="income-regions" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <button type="button" class="card-action card-reload" title="Reload"></button>
	                    <!-- <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Continents / Top Countries</strong>
	                  <a class="link-modal" data-target="income-regions">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="network-list-top section2">
	                        </tbody>
	                      </table>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          
	          <!-- Purpose -->
	          <div class="row gutter-xs">
	            <div class="col-md-6">
	              <div id="top-purposes-donors" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <button type="button" class="card-action card-reload" title="Reload"></button>
	                   <!--  <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Purpose / Top purposes</strong>
	                  <a class="link-modal" data-target="top-purposes-donors">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="purpose-list-top section2" >
	                        </tbody>
	                      </table>	                      
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	            <div class="col-md-6">
	              <div id="purpose-income-top" class="card">
	                <div class="card-header">
	                  <div class="card-actions">
	                    <button type="button" class="card-action card-toggler" title="Collapse"></button>
	                    <button type="button" class="card-action card-reload" title="Reload"></button>
	               <!--      <button type="button" class="card-action card-remove" title="Remove"></button> -->
	                  </div>
	                  <strong>Purpose income / Top purposes income</strong>
	                  <a class="link-modal" data-target="purpose-income-top">View full report</a>
	                </div>
	                <div class="card-body">
	                  <div class="row">
	                    <div class="col-md-6 m-y">
	                      <ul class="list-group list-group-divided section1">	                        
	                      </ul>
	                    </div>
	                    <div class="col-md-6 m-y">
	                      <table class="table table-borderless table-fixed">
	                        <tbody class="purpose-income-list-top section2">
	                        </tbody>
	                      </table>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	          <!-- /CARDS -->
	          
	        </div>
	      </div>

		<div class="layout-footer">
			<div class="layout-footer-body">
				<small class="version">Version 1.0.1</small> <small
					class="copyright">2018 &copy; <a
					href="">Seminario</a></small>
			</div>
		</div>	
		<%
		}
		%>
	</div>
	
	<div class="theme"></div>
	<script src="${vendorJS}?<%=n%>"></script>
	<script src="${elephantJS}?<%=n%>"></script>
	<script src="${applicationJS}?<%=n%>"></script>
	<script src="${dataTableJS}?<%=n%>"></script>
	<script src="${dataTablesbuttonsJS}?<%=n%>"></script>
	<script src="${jszipJS}"></script>
	<script src="${dataTablesbuttons5}"></script>

	<script src="${adminJS}?<%=n%>"></script>
	<script src="${modalJS}?<%=n%>"></script>
	<script src="${loadingJS}?<%=n%>"></script>
	<script src="${homeJS}?<%=n%>"></script>
	<script type="text/javascript">
		$(".menuDashboard").addClass("open");
		$('.menuDashboard').addClass("active");
		$('#menuDonations').addClass("active");
		$('.collapseDashboard').collapse();
	</script>
</body>
</html>