<%@page import="java.util.LinkedHashMap"%>
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
<%@page import="com.weavx.web.model.Language"%>
<%@page import="com.weavx.web.model.ExternalPaymentType"%>
<%@page import="com.weavx.web.utils.UtilPropertiesLoader" %>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>${title}</title>
<c:url var="home" value="/" scope="request" />
<spring:url value="/resources/core/mod/elephant/elephant.css?v=1.0.3" var="elephant" />
<spring:url value="/resources/core/mod/elephant/elephant.js?v=1.0.3" var="elephantJs" />
<spring:url value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700" var="fonts" />
<spring:url value="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" var="iconsCSS" /> 
<spring:url value="/resources/core/mod/application/application.css?v=1.0.3" var="application" />
<spring:url value="/resources/core/mod/application/application.js?v=1.0.3" var="applicationJs" />
<spring:url value="/resources/core/mod/vendor/vendor.min.css?v=1.0.3" var="vendor" />
<spring:url value="/resources/core/mod/vendor/vendor.min.js?v=1.0.3" var="vendorJs" />
<spring:url value="/resources/core/js_old/jquery.validate.js?v=1.0.3" var="validateJs" />
<spring:url value="/resources/core/css/demo.min.css?v=1.0.3" var="demo" />
<spring:url value="/resources/core/mod/datatable/datatable.min.css?v=1.0.3" var="dataTableCss" />
<spring:url value="/resources/core/mod/datatable/datatable.min.js?v=1.0.3" var="dataTableJs" />
<spring:url value="/resources/core/js/init.js?v=1.0.6" var="initJs" />
<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.js?v=1.0.3" var="dataTablesbuttons" />
<spring:url value="/resources/core/mod/datatable/dataTables.buttons.min.css?v=1.0.3" var="dataTablebuttonscss" />
<spring:url value="/resources/core/js/buttons.html5.min.js?v=1.0.3" var="dataTablesbuttons5" />
<spring:url value="/resources/core/js/jszip.min.js?v=1.0.3" var="jszipJS" />
<spring:url value="/resources/core/js/jquery.mask.min.js?v=1.0.3" var="maskJs" />
<spring:url value="/resources/core/mod/admin/admin.js?v=1.0.3" var="admin" />
<spring:url value="/resources/core/mod/stripe/stripe.css?v=1.0.3" var="stripeCSS" />
<spring:url value="/resources/core/mod/stripe/stripe.js?v=1.0.3" var="stripeJS" />
<spring:url value="/resources/core/mod/billing-comission/comission.js?v=1.0.3" var="comissionJs" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.css?v=1.0.3" var="loadingCSS" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.js?v=1.0.3" var="loadingJS" />
<spring:url value="/resources/core/js/paymentStripe.js?v=1.0.3" var="paymentStripeJs" />
<spring:url value="/resources/core/css_old/call-center.css?v=1.0.3" var="callCenterCSS" />
<spring:url value="/resources/core/js/clipboard.min.js?v=1.0.3" var="clipboardJs" />
<spring:url value="/resources/core/css/addAttendee.css?v=1.0.3" var="addAttendeeCSS" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/dayjs/1.11.1/dayjs.min.js"></script>
<script type="text/javascript" src="https://js.stripe.com/v2/"></script>

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
<link rel="stylesheet" href="${stripeCSS}">
<link rel="stylesheet" href="${iconsCSS}">
<link rel="stylesheet" href="${loadingCSS}">
<link rel="stylesheet" href="${callCenterCSS}">
<link rel="stylesheet" href="${addAttendeeCSS}">
</head>
<body class="layout layout-header-fixed">
	<%@ include file="/WEB-INF/views/jsp/header.jsp"%>
	<% String dashboard = UtilPropertiesLoader.getInstance().getPropDashboard("dashboard.active");
	String stripeKey = (String) session.getAttribute("stripeKey");
	%>
	<%
    String from = request.getParameter("from");
	String userId = request.getParameter("id");
	%>

	<div class="layout-main">
		
		<%@ include file="/WEB-INF/views/jsp/menu.jsp"%>
		
		<div class="layout-content">
			<div class="layout-content-body ">
				<div class="title-bar" >
					<h1 class="title-bar-title">
						<span class="d-ib">Call Center</span>
						<span class="d-ib"> <span class="sr-only"></span> </span>
					</h1>
					<% if(from != null) { %>
						<a class="btn btn-default btn-sm" href="/scr-admin/userDetail-<%=userId%>">
						<i class="fa fa-arrow-left" style="margin-right: 5px;"></i>
						Back
						</a>						
					<% } %>
					
					<p class="title-bar-description">
						<small> </small>
					</p>
				</div>
				
				<% 
					
					if(currentUser.userHasAccess(CustomerUser.ADD_ATTENDEEs)) {
				%>
				<div class='form-mode-container'>
					<p>Massive upload</p>
					<label class="switch switch-info">
	                  <input id="form-mode" class="switch-input" type="checkbox">
	                  <span class="switch-track"></span>
	                  <span class="switch-thumb"></span>
	                </label>
                </div>
                
                <% } %>
				<!--  FORM WIZARD -->
<div id="one-customer-form">
<form id="register-stripe-record" method="post">
  <div class="tab-content">
    <div id="step-1" class="tab-pane active">
	  
      <div class="col-sm-12">
             <div class="demo-form-wrapper">
              	<div class="form form-horizontal">
                			
                			<div class="title-bar  stripe-form-elements" style="text-align:center;" >
								<div class="title-bar-title">
									<h4>
										<span class="d-ib">User Data</span>
									</h4>
								</div>
							</div>
							
							<!-- Por ahora comentaremos el select de idiomas.  
							<div class="form-group form-group-md is-massive-too">
								<label for="payment-method" class="col-sm-4  control-label">Language:</label>
								<div class="col-sm-6 col-md-4">			
									<select id="lang" name="lang" class="custom-select  form-control">
										<option value="1">Spanish</option>
										<option value="2">English</option>
									</select> 	
								</div>
							</div>
							-->

							<div class="form-group find-email-group">
			                    <label for="email" class="col-sm-3 col-md-4 control-label">Email:</label>
			                    <div class="col-sm-6 col-md-4">
			                        <input  type="email" class="form-control special-input" id="email" name="email" placeholder="Email" value="" >
			                        
			                        <a class="special-button" id="verifyUser">
			                    		<div class="spinner_ loading-find-user">
										  <div class="bounce1"></div>
										  <div class="bounce2"></div>
										  <div class="bounce3"></div>
										</div>
										<label class="action-find-user active">Find</label>
			                        </a>
			                    	
			                    	<div id="find-email" class="my-error"></div>
			                    </div>
			                    <div class="custom-message-error"></div>
			                </div>
                			
							 <div class="form-group">
			                    <label for="email-2" class="col-sm-3 col-md-4 control-label">Name:</label>
			                    <div class="col-sm-6 col-md-4">
			                      <input placeholder="Name"  type="text" class=" form-control" id="fname" name="fname"  data-error-icon="icon-exclamation">
			                    </div>
			                    <div class="custom-message-error"></div>
			                  </div>
			                  <div class="form-group">
			                    <label for="lname" class="col-sm-3 col-md-4 control-label">Last Name:</label>
			                    <div class="col-sm-6 col-md-4">
			                      <input  type="text" class=" form-control" id="lname" name="lname" placeholder="Last name" value="" >
			                    </div>
			                    <div class="custom-message-error"></div>
			                  </div>

			                  <div class="form-group">
			                    <label for="phone" class="col-sm-3 col-md-4 control-label">Phone:</label>
			                    <div class="col-sm-6 col-md-4">
			                      <input  type="tel" class="form-control" id="phone" name="phone" placeholder="Phone" value="" >
			                    </div>
			                    <div class="custom-message-error"></div>
			                  </div>

							  <div class="form-group form-group-md is-massive-too">
								<label for="payment-method" class="col-sm-4  control-label">Country:</label>
								<div class="col-sm-6 col-md-4">
								<select  id="country" name='country' class="custom-select  form-control">

								</select> 	
								</div>
							</div>
							
							<div class="form-group form-group-md is-massive-too form-group-source">
								<div id="text-afilliate" class="text-affiliate"></div>
							</div>

			                  <!-- Lo nuevo -->
			                  <!-- step2 -->
    <div id="step-2" class="tab-pane">

     	<div class="col-sm-12">
             <div class="demo-form-wrapper">
              		
                			<div class="title-bar  stripe-form-elements" style="text-align:center;" >
								<div class="title-bar-title">
									<h4>
										<span class="d-ib">Purchase Data</span>
									</h4>
								</div>
							</div>
				<div id="event-data-form" class="form form-horizontal">
				
              		<div class="form-group form-group-md is-massive-too">
									<label for="payment-method" class="col-sm-4  control-label">Event:</label>
									<div class="col-sm-6 col-md-4">			
								<select id="funds" name="funds" class="custom-select  form-control">
									<option value="" >Event</option>
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

					</select> 	
									</div>
								</div>
								
								<div id='modify-start-date-container' class="form-group start-date-group">
			                    	<div class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
				                      <label class="custom-control custom-control-primary custom-checkbox">
						                      <input class="custom-control-input" type="checkbox" id="modify-start-date" name="modify-start-date" >
						                      <span class="custom-control-indicator"></span>
						                      <span class="custom-control-label">Modify start date</span>
					                   </label>
				                    </div>
			                  	</div>
					
								<div class="form-group start-date-input start-date-group" style="display:none;">
			                    	<label for="lname" class="col-sm-3 col-md-4 control-label">Start date:</label>
			                    	<div class="col-sm-6 col-md-4">
			                      		<div class="input-with-icon">
					                      	<div class="input-with-icon">
		                                        <input id="start-date" name="start-date" class="form-control" type="text" data-provide="datepicker">
		                                        <span class="icon icon-calendar input-icon"></span>
		                                    </div>
			                       			<div class="custom-message-error"></div>
			                      		</div>
			                    	</div>
			         			</div>
								
								<%if(Boolean.parseBoolean((String)session.getAttribute("SHOW_SOURCE_MODAL"))){%>
								<div class="form-group form-group-md is-massive-too form-group-source">
									<label for="payment-method" class="col-sm-4  control-label">Source:</label>
									<div class="col-sm-6 col-md-4">			
										<select id="sourceMarketing" name="sourceMarketing" required class="custom-select  form-control">

										</select> 	
									</div>
									<div class="custom-message-error"></div>
								</div>
								<%}%>
								
								<div class="form-group form-group-md is-massive-too">
									<label for="payment-method" class="col-sm-4  control-label">Language:</label>
									<div class="col-sm-6 col-md-4">			
										<select id="languaje" name="languaje" class="custom-select  form-control">
												<%ArrayList<LinkedHashMap<String, Object>> listLan = (ArrayList<LinkedHashMap<String, Object>>) session.getAttribute("languajes");%>
												<%for (LinkedHashMap<String, Object> lan : listLan) {%>
														<option value=<%=lan.get("id")%>><%=lan.get("description")%></option>
												<%}%>
										</select> 	
									</div>
								</div>

								
								 <div class="form-group form-group-md is-massive-too">
									<label for="payment-method" class="col-sm-4  control-label">Mount:</label>
									<div class="col-sm-6 col-md-4">
									<select  id="amount" name='amount' class="custom-select  form-control">
									
									</select> 	
									</div>
								</div>

					 <!-- COMISSION RESUME -->
			         	<div id="billing-component" class="row comission-info-table" style="display:none;">
									<div class="col-md-4 col-sm-12">
						                  <div class="card">
						                    <div class="card-header">
						                      <strong>Billing resume</strong>
						                    </div>
						                    <div class="card-body" data-toggle="match-height" >
						                      <ul class="list-group list-group-divided">
						                        <li class="list-group-item">
						                          <div class="media">
						                            <div class="media-middle media-body">
						                              <h6 class="media-heading">
						                                <span>Price:</span>
						                              </h6>
						                              <h4 id="comission-event-amount" class="">
						                                
						                              </h4>
						                            </div>
						                            <div class="media-middle media-right">
						                              <span class="bg-primary circle sq-40">
						                                <span class="icon icon-arrow-right"></span>
						                              </span>
						                            </div>
						                          </div>
						                        </li>
						                        <li class="list-group-item">
						                          <div class="media">
						                            <div class="media-middle media-body">
						                              <h6 class="media-heading">
						                                <span>Fee:</span>
						                              </h6>
						                              <h4 id="comission-mount" class="media-heading">
						                              
						                              </h4>
						                            </div>
						                            <div class="media-middle media-right">
						                              <span class="bg-primary circle sq-40">
						                                <span class="icon icon-link"></span>
						                              </span>
						                            </div>
						                          </div>
						                        </li>
						                        <li class="list-group-item coupon-apply" style="display:none;">
						                          <div class="media">
						                            <div class="media-middle media-body">
						                              <h6 class="media-heading">
						                                <span>Disccount:</span> <small id="percent-mount"> 15%</small>
						                              </h6>
						                              <h4 id="discount-mount" class="media-heading">
						                              	
						                              	
						                              </h4>
						                            </div>
						                            <div class="media-middle media-right">
						                              <span class="bg-primary circle sq-40">
						                                <span class="icon icon-link"></span>
						                              </span>
						                            </div>
						                          </div>
						                        </li>
						                        <li class="list-group-item">
						                          <div class="media">
						                            <div class="media-middle media-body">
						                              <h6 class="media-heading">
						                                <span>Total:</span>
						                              </h6>
						                              <h4 id="total-amount" class="media-heading">
						                              	
						                              </h4>
						                            </div>
						                            <div class="media-middle media-right">
						                              <span class="bg-primary circle sq-40">
						                                <span class="icon icon-search"></span>
						                              </span>
						                            </div>
						                          </div>
						                        </li>
						                      </ul>
						                    </div>
						                  </div>
						                </div>
								</div>
			         <!-- COMISSION RESUME -->
								
					<% if(currentUser.userHasAccess(CustomerUser.DISCOUNT)) { %>			
					
					<div id='apply-coupon-container' class="form-group coupon-group">
			                    <div class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
			                      <label class="custom-control custom-control-primary custom-checkbox">
					                      <input class="custom-control-input" type="checkbox" id="apply-coupon" name="apply-coupon" >
					                      <span class="custom-control-indicator"></span>
					                      <span class="custom-control-label">Apply coupon</span>
				                   </label>
			                      
			                    </div>
			                  </div>
					
					<div class="form-group coupon-input coupon-group" style="display:none;">
			                    <label for="lname" class="col-sm-3 col-md-4 control-label">Coupon code:</label>
			                    <div class="col-sm-6 col-md-4">
			                      <div class="input-with-icon">

			                      	<input maxlength="20"  type="text" class=" form-control apply-code-input" id="coupon-code" name="coupon-code" placeholder="XXXXXX">
			                       	<a id="click-apply-code" class="apply-code">Apply</a>
			                       	<div id="error-apply-code">
			                       		Fill in the name, email and the Coupon number, to apply the coupon.
			                       	</div>
			                       	<div class="custom-message-error"></div>
			                      </div>
			                    </div>
			                    
			         </div>
			         <div id="coupon-discount" class="col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4" style="display:none;">
			         	<div class="">
				              <div class="card bg-success">
				                <div class="card-body">
				                  <div class="media">
				                    <div class="media-middle media-left">
				                      <span class="bg-success-inverse circle sq-24">
				                        <span class="icon icon-ticket"></span>
				                      </span>
				                    </div>
				                    <div class="media-middle media-body">
				                      <h6 id="discount-amount-coupon" class="media-heading" style="font-size:14px;margin-top: 6px;display:none;">
				                      	<span id="amount-coupon" class="fw-l"></span>
				                      </h6>
				                      <h3 id="total-coupon" class="media-heading" style="font-size:14px;margin-top: 6px;display:none;">
				                      	<strong>Total:</strong>
				                        <span id="total-mount" class="fw-l"></span>
				                      </h3>
				                    </div>
				                  </div>
				                </div>
				              </div>
           				 </div>	         	
			         </div>
			         <% } %>
			         
			        
              		</div>
        	 </div>
        </div>

    </div>			                  
			                  <!-- /step2 -->
			                  <!-- step3 -->
    <div id="step-3" class="tab-pane">

      				<div class="col-sm-12">
              		<div class="demo-form-wrapper">
                		<div class="form form-horizontal">
								
								
			                  <div class="form-group form-group-md payment-method-container">
									<label for="payment-method" class="col-sm-4  control-label">Payment method:</label>
									<div class="col-sm-6 col-md-4">
									<select id="payment-method" name='payment-method' class="custom-select" >
										<option value="stripe">Credit Card</option>
										<% if(currentUser.userHasAccess(CustomerUser.COMPLIMENTARY)) { %>
										<option value="free">Complimentary</option>
										<% } %>
										<option id="splitPayment-method" value="splitPayment" style="display: none;">Split Payment</option>
										<% if(currentUser.userHasAccess(CustomerUser.OTHER)) { %>
										<option value="other">Other</option>
										<% } %>
									</select>
										  <div class="custom-message-error"></div>
									</div>
								</div>
								
								<div class="form-group form-group-md external-payment" style="display: none;">
									<label for="external-payment-method" class="col-sm-4  control-label"></label>
									<div class="col-sm-6 col-md-4">
									<select id="external-payment-method" name='external-payment-method' class="custom-select" >
										
										<%
	ArrayList<ExternalPaymentType> paymentTypes = (ArrayList<ExternalPaymentType>) session.getAttribute("externalPaymentTypes");
	Iterator<ExternalPaymentType> iterPaymentTypes = paymentTypes.iterator();
	while (iterPaymentTypes.hasNext()) {
		ExternalPaymentType paymentType = iterPaymentTypes.next();
%>
									<option value=<%=paymentType.getId()%> ><%=paymentType.getName()%></option>
<%
	}
%>
									</select>
										  <div class="custom-message-error"></div>
									</div>
								</div>
								
								<div class="form-group form-group-md has-feedback external-payment" style="display:none;">
									<label for="external-payment-description" class="col-sm-4  control-label">Description:</label>
									<div class="col-sm-6 col-md-4">
									<textarea  class="form-control" id="external-payment-description" name="external-payment-description" placeholder="Descripción" value="">
									</textarea>
										  <div class="custom-message-error"></div>
									</div>

								</div>
								
							
							<div class="form-group split-payment" style="display: none;">
							 <label for="lname" class="col-sm-3 col-md-4 control-label">Paid amount</label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
							           <input value="0,00" id="paidAmount" name="paidAmount" class="form-control" readonly style="border: none;"/>
							           <div class="icon icon-usd input-icon"></div>
							         </div>
			                    </div>
							</div>
							
							<div class="form-group split-payment" style="display: none;">
							 <label id="lAmount" class="col-sm-3 col-md-4 control-label"></label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
							           <input id="amountToPay" name="amountToPay" class="form-control" readonly style="border: none;"/>
							           <div class="icon icon-usd input-icon"></div>
							         </div>
			                    </div>
							</div>
							
							<div class="form-group split-payment" style="display: none;">
							 <label for="lname" class="col-sm-3 col-md-4 control-label">Payable amount</label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
			                     		<input id="payableAmount" type="number" placeholder="0,00" value="0" min="0.00" max="amountToPay" class="form-control" name="payableAmount" oninput="inputChange(this.value)" onkeyup="inputEmpty(this.value)" />
							           	<div class="icon icon-usd input-icon"></div>
							           	<input id="slid" type="range" min="0" step="1" value="0" oninput="sliderChange(this.value)" style="margin-top: 10px;">      		           
							         </div>
			                    </div>
			                    <div id="text-fee" style="margin-top: 7px; display: none;" ></div>
							</div>
			                  
							
							<div class="form-group stripe-form-elements list-credicard"></div>

							<div class="form-group stripe-form-elements card-1">
			                    <label for="lname" class="col-sm-3 col-md-4 control-label"></label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
							           <input placeholder="Credit Card" type="text" id="cardNumber" class="form-control" name="cardNumber" maxlength="20" data-inputmask="'mask': '9999-9999-9999-9999'"  />
							           <div class="icon icon-credit-card input-icon"></div>
							         </div>
			                    </div>
			                    <div class="custom-message-error"></div>
							 </div>

							 <div id="save-container" class="form-group coupon-group stripe-form-elements card-1" style="display:block;">
			                    <div class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
			                      <label class="custom-control custom-control-primary custom-checkbox">
					                      <input class="custom-control-input" type="checkbox" id="save-credit-card" name="save-credit-card">
					                      <span class="custom-control-indicator"></span>
					                      <span class="custom-control-label">Save Credit Card</span>
				                   </label>
			                    </div>
			                  </div>


							  <div class="form-group stripe-form-elements card-1">
			                    <label for="lname" class="col-sm-3 col-md-4 control-label"></label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
										<input type="tel" id="lang-expdate_ph" class="form-control" name="lang-expdate_ph" maxlength="5" placeholder="MM/AA" data-inputmask="'mask': '12/99'"  />
										<div class="icon icon-calendar input-icon"></div>
							         </div>
			                    </div>
			                    <div class="custom-message-error"></div>
							 </div>

							 <div class="form-group stripe-form-elements card-1">
			                    <label for="lname" class="col-sm-3 col-md-4 control-label"></label>
			                    <div class="col-sm-6 col-md-4">
			                     	<div class="input-with-icon">
										<input type="tel" id="lang-cvv_ph" class="form-control" name="lang-cvv_ph" maxlength="4"  placeholder="7777" />
										<div class="icon icon-lock input-icon"></div>
							         </div>
			                    </div>
			                    <div class="custom-message-error"></div>
							 </div>
							
							<!--<div class="form-group stripe-form-elements card-2">
								<div class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
								<div class="col-sm-6 col-md-6 left-input-ccform">
						            <div class="input-with-icon form-group">
						              <input type="tel" id="lang-expdate_ph" class="form-control" name="lang-expdate_ph" maxlength="5" placeholder="MM/AA" data-inputmask="'mask': '12/99'"  />
						              <div class="icon icon-calendar input-icon"></div>
									  <div class="custom-message-error"></div>
						             </div>
                  				</div>
                  				<div class="col-sm-6 col-md-6 right-input-ccform card-3 form-group">
						            <div class="input-with-icon form-group">
						                 <input type="tel" id="lang-cvv_ph" class="form-control" name="lang-cvv_ph" maxlength="4"   />
						                 <div class="icon icon-lock input-icon"></div>
										 <div class="custom-message-error"></div>
						            </div>
                  				</div>
                  				</div>
							</div>-->
							
							 <div class="form-group">
			                    <label for="comments" class="col-sm-3 col-md-4 control-label">Comments:</label>
			                    <div class="col-sm-6 col-md-4">
			                      <textarea placeholder="Write here your comments..." class="form-control"  id="comments" name="comments"  data-error-icon="icon-exclamation" value="">
			                      </textarea>
			                    </div>
			                    <div class="custom-message-error"></div>
			                  </div>
								<input type="hidden" value="<%=session.getAttribute("firstName")%> <%=session.getAttribute("LastName")%>" class="form-control2 form-control" id="source" placeholder="Source">
								<input type="hidden" value="ADMIN" class=" form-control" id="medium" placeholder="Medium">
							  
			                  <div class="col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4">
				                  <div class="form-group">
				                    <button class="btn btn-primary btn-block" id="callPurchase" data-user-id="<%=userId%>">Register Purchase</button>
														<button style="display: none;" class="btn btn-primary btn-block" id="btnFree">Register Purchase</button>
				                    <div id="submit-spinner" class="spinner spinner-default spinner-md" style="display:none;"></div>
				                  </div>
			                  </div>
                		</div>
              		</div>
           		</div>

    </div>			                  
			                  <!-- step3 -->
								
								
			</div>
		</div>

    </div>
    </div>


  </div>
	</form>	
		</div>
			<!-- FORM WIZARD -->
				<!-- FORM BODY -->
	<!-- FORM MASSIVE CUSTOMER -->
	<div id="massive-customer-form" style="display:none;">
		<form id="massive-customer-upload" enctype="multipart/form-data">
		
		     <div class="tab-pane">

     	<div class="col-sm-12">
             <div class="demo-form-wrapper">
              		
                			<div class="title-bar  stripe-form-elements" style="text-align:center;" >
								<div class="title-bar-title">
									<h4>
										<span class="d-ib">Event Data</span>
									</h4>
								</div>
							</div>
				<div id="event-data-massive" class="form form-horizontal">
				
							
								
						<div id="massive-input-container" class="form-group form-group-md">
									<label for="payment-method" class="col-sm-4  control-label">Archivo:</label>
									<div class="col-sm-6 col-md-4">
								<input type="file" id='customers' name='customers' />
									</div>
						</div>
						 <div class="col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4">
				                  <div class="form-group">
				                    <button class="btn btn-primary btn-block" id="registerCustomers">Register Customers</button>
				                    <div id="submit-spinner-3" class="spinner spinner-default spinner-md" style="display:none;"></div>
									<div id="registerCustomersErrors" class="myError"></div>
								</div>
			                  </div>
              		</div>
        	 </div>
        </div>

    </div>	
		 
		</form>
		
		<div id="massive-errors" class=" row message-error" style="display:none;"></div>
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
				<small class="version">Version 1.0.3</small> <small
					class="copyright">2020 &copy; <a
					href="">${title}</a></small>
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
             	<h4>The transaction id for this operation is:</h4>
				
				<div class="form-group clipboard-modal-form">
                    <div class="col-sm-7 col-sm-offset-3">
                      <div class="input-group">
                        <input class="form-control" type="text" id="success-tx-id">
                        <div class="input-group-btn">
                          <button id="clipboard-action" class="btn btn-success" title="" data-container="body" data-placement="top" data-toggle="tooltip" type="button" data-original-title="Copy to clipboard" data-clipboard-target="#success-tx-id">
                            <span class="icon icon-copy icon-fw"></span>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
              <div class="m-t-lg">
                <button id="success-modal-close" data-user-id="<%=userId%>" class="btn btn-success" data-dismiss="modal" type="button">Close</button>
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
	<%
				JSONObject amountByFund = (JSONObject) session.getAttribute("amountByFund");	
				%>
	<script type="text/javascript">
	var amountByFund = <%=amountByFund%>;
	Stripe.setPublishableKey("<%=stripeKey%>");
	var ErrorActualIdiom = 1;
	var dataErrors = [null,{"lettersonly_error_1":"Letras y espacios únicamente por favor","step2_error_6":"Su apellido no debe tener más de 20 caracteres","user_error_tdc_decline":"Tu Tarjeta ha sido negada. Por favor intenta con otra tarjeta.","user_error_3":"Debe colocar usuario u contraseña.","step3_error_4":"Proporcione una fecha de caducidad","vmcardsonly_error_1":"Introduzca un número de tarjeta de crédito Visa o Master válido.","step2_error_7":"Por favor, introduce una dirección de correo electrónico válida","step2_error_1":"Primer nombre requerido","step3_error_7":"Su cvv no debe tener más de 4 caracteres","step2_error_2":"Su primer nombre debe tener al menos 3 caracteres","user_error_5":"El campo es requerido.","step3_error_2":"Su tarjeta de crédito debe tener un mínimo de 12 caracteres","step3_error_3":"Su tarjeta de crédito no debe tener más de 16 caracteres","step2_error_4":"Proporcione un apellido","step3_error_1":"Proporcione una tarjeta de crédito","step2_error_8":"El correo electrónico no coincide","user_error_1":"El usuario ya existe","step3_error_6":"Su cvv debe tener al menos 3 caracteres","step2_error_5":"Su apellido debe tener al menos 3 caracteres","step3_error_5":"Proporcione un cvv","numberand_error_1":"Su tarjeta de crédito no puede contener ninguna letra","step2_error_3":"Su primer nombre no debe tener más de 20 caracteres","user_error_4":"Usuario no registrado","stepL_error_1":"Por favor, introduce una dirección de correo electrónico válida","user_error_2":"Usuario o contraseña incorrectos.","step3_error_8":"Su cvv no puede contener ninguna letra","expdate_error_1":"Por favor, introduzca una fecha de vencimiento válida","user_error_6":"Ha ocurrido un error, intente más tarde."},{"expdate_error_1":"Please enter a valid expiration date","user_error_1":"User already exists","step2_error_4":"Please provide a Last name","step3_error_1":"Please provide a credit card","numberand_error_1":"Your credit card can not content any letter","user_error_2":"Incorrect user or password.","user_error_3":"You must enter username and password.","step3_error_7":"Your cvv must not be more than 4 characters long","step3_error_6":"Your cvv must be at least 3 characters long","stepL_error_1":"Please enter a valid email address","user_error_4":"Unregistered user","step3_error_3":"Your credit card must not be more than 16 characters long","lettersonly_error_1":"Letters and spaces only please","step2_error_6":"Your Last name must not be more than 20 characters long","step2_error_3":"Your first name must not be more than 20 characters long","step2_error_5":"Your Last name must be at least 3 characters long","step2_error_1":"Please provide a a first name","vmcardsonly_error_1":"Please enter a valid Visa or Master credit card number.","step2_error_2":"Your first name must be at least 3 characters long","user_error_tdc_decline":"Your card has been declined. Please use another one.","step3_error_4":"Please provide a expiration date","step3_error_5":"Please provide a cvv","step2_error_7":"Please enter a valid email address","user_error_5":"This field is required.","user_error_6":"An error occurred, please try again later.","step3_error_8":"Your cvv card can not content any letter","step3_error_2":"Your credit card must be at least 12 characters long","step2_error_8":"The email does not match"}];
	
    </script>
	
	<script src="${vendorJs}"></script>
	<script src="${elephantJs}"></script>
	<script src="${applicationJs}"></script>
	<script src="${dataTableJs}"></script>
	<script src="${dataTablesbuttons}"></script>
	<script src="${jszipJS}"></script>
	<script src="${clipboardJs}"></script>
	<script src="${dataTablesbuttons5}"></script>
	<script src="${maskJs}"></script>
	<script src="${loadingJS}"></script>
	<script src="${admin}"></script>
	<script src="${validateJs}"></script>
	<!-- 
	<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/jquery.validate.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.17.0/dist/additional-methods.min.js"></script> -->
	<script src="${paymentStripeJs}"></script>
	<script defer src="${stripeJS}"></script>
	<script src="${comissionJs}"></script>
	<script src="${initJs}"></script>
	

	<script>
		
		$("#success-modal-close").click(function(){
			closeSuccessModal();
			$("#success-tx-id").val("");
		});
		
		$("#error-modal-close").click(function(){
			closeErrorModal();
			$("#error-modal-msg").text("");
		});
		
		new ClipboardJS('#clipboard-action');
	
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