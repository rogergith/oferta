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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
    <spring:url value="/resources/core/mod/vendor/vendor.min.js" var="vendorJs" />
    <spring:url value="/resources/core/css/demo.min.css?v=1.0.1" var="demo"/>
    <spring:url value="/resources/core/mod/datatable/datatable.min.css?v=1.0.1"
                var="dataTableCss"/>
    <spring:url value="/resources/core/js/init.js?v=1.0.3" var="initJs"/>
    <spring:url value="/resources/core/js/restrictedevent/restrictActualEvent.js?v=1.0.1"
                var="restrictActualEventJs"/>                
    <spring:url value="/resources/core/mod/users/users.js?v=1.0.8"
                var="usersJs"/>
    <spring:url value="/resources/core/mod/users/userDetail.js?v=1.0.4"
                var="userDetailJs"/>
    <spring:url value="/resources/core/mod/users/userAccess.js?v=1.0.5" var="userAccessJS" />
    <spring:url
            value="/resources/core/mod/datatable/dataTables.buttons.min.css?v=1.0.1"
            var="dataTablebuttonscss"/>
    <spring:url value="/resources/core/js/jszip.min.js?v=1.0.1" var="jszipJS"/>
    <spring:url value="/resources/core/js/jquery.mask.min.js?v=1.0.1" var="maskJs"/>
    <spring:url value="/resources/core/js_old/jquery.validate.js?v=1.0.3" var="validateJs" />
    <spring:url value="/resources/core/mod/admin/admin.js?v=1.0.1" var="admin"/>
    <spring:url value="/resources/core/mod/stripe/stripe.css?v=1.0.1"
                var="stripeCSS"/>
    <spring:url value="/resources/core/mod/stripe/stripe.js?v=1.0.1" var="stripeJS"/>
    <spring:url value="/resources/core/mod/black-list/blacklist.js?v=1.0.1" var="blackListJs"/>
    <spring:url value="/resources/core/mod/black-list/blacklist.css?v=1.0.1" var="blackListCSS"/>    
    <spring:url value="/resources/core/js/selectize.min.js?v=1.0.1"
                var="selectizeJs"/>  
    <spring:url value="/resources/core/js/toastr.min.js?v=1.0.1" var="toastrJS"/>
    <spring:url value="/resources/core/css/toastr.min.css?v=1.0.1" var="toastrCSS"/>
    <spring:url
            value="/resources/core/css/selectize.bootstrap3.min.css?v=1.0.1"
            var="selectizeCSS"/>  
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
                
    <spring:url value="/resources/core/mod/users/users.css?v=1.0.3" var="userCSS" />

    <spring:url value="/resources/core/js/jsQR.js?v=1.0.1" var="qrJS"/>
    <spring:url value="/resources/core/js/qr-reader.js?v=1.0.1" var="qrReader"/>
    <spring:url value="/resources/core/js/custom-qr.js?v=1.0.1" var="customQR"/>
    <spring:url value="/resources/core/css/scan-qr.css?v=1.0.1" var="qrCSS" />
    <script type="text/javascript" src="https://js.stripe.com/v2/"></script>
    <spring:url value="/resources/core/js/bs-modal-fullscreen.min.js?v=1.0.1" var="bsModalFS"/>

    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="height=device-height, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
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
    <link rel="stylesheet" href="${selectizeCSS}">   
    <link rel="stylesheet" href="${toastrCSS}">  
    
    <style>
    	.has-error {
    		color: red;
    	}
    	   	
    	.title {
    		font-size: 18px;
    		font-weight: bold;
    	}
    	
    	.title small {
    		font-size: 80%;
    	}
    	
    	.avatar {
    		padding: 1.5rem;
    		background-color: #fde83c;
    	}
    	
    	.mb-4 {
    		margin-bottom: 25px;
    	}
    	    		
    	.wizard {
    		display: flex;
    	}    
    				
		.wizard a {
    		background: #dfd9d3;
    		display: inline-block;
    		margin-right: 5px;
    		min-width: 150px;
    		outline: none;
    		padding: 10px 25px 10px;
    		position: relative;
    		text-decoration: none;
    		color: white;
		}
		
		.wizard a.done {
    		background: #6fc436;
		}
		
		.wizard a.pending {
    		background: #e85100;
		}
		
		.wizard .active {
    		background: #007ACC;
    		color: #fff;
		}
		
		.wizard a:before {
        	width: 0;
        	height: 0;
        	border-top: 20px inset transparent;
        	border-bottom: 20px inset transparent;
        	border-left: 20px solid #fff;
        	position: absolute;
        	content: "";
        	top: 0;
        	left: 0;
    	}
    	    	
    	.wizard a:after {
        	width: 0;
        	height: 0;
        	border-top: 20px inset transparent;
        	border-bottom: 20px inset transparent;
        	border-left: 21px solid #dfd9d3; /* Arrow color */
        	position: absolute;
        	content: "";
        	top: 0;
        	right: -20px;
        	z-index: 2;
    	}
    	
    	.wizard a.done:after {
    		border-left: 21px solid #6fc436;    	
    	}
    	
    	.wizard a.pending:after {
    		border-left: 21px solid #e85100;    	
    	}
    	
    	.wizard a:first-child:before,
		.wizard a:last-child:after {
        	border: none;
   	 	}
   	 	
   	 	.wizard a:first-child {
        	-webkit-border-radius: 8px 0 0 8px;
        	-moz-border-radius: 8px 0 0 8px;
        	border-radius: 8px 0 0 8px;
    	}

		.wizard a:last-child {
        	-webkit-border-radius: 0 8px 8px 0;
        	-moz-border-radius: 0 8px 8px 0;
        	border-radius: 0 8px 8px 0;
    	}
    	
    	.wizard a.active.after {
			border-left: 21px solid green;
		}
		
		.item-life-cycle.done,
		.item-life-cycle.pending {
			cursor: pointer;
		}
		
		.card-title {
			padding: 15px 15px 0 15px;
		}
		
		/* .modal-body {
    		max-height: calc(100vh - 212px);
    		overflow-y: auto;
		} */
		
		.nav-pills > li > a {
			color: #777;
		}
		
		.nav-pills > li.active > a, .nav-pills > li.active > a:hover, .nav-pills > li.active > a:focus {
			color: #777;
    		background-color: transparent;
    		font-weight: bold;
    		font-size: 14px;
		}
				
		.nav-pills > li.active {
    		border-bottom: 3px solid #838383;
		}
		
		.width-auto {
			width: auto !important;
		}
		
		#toast-container > .toast {
			background-image: none !important;
		}

    </style>
</head>
<body class="layout layout-header-fixed">
<% 
String stripeKey = (String) session.getAttribute("stripeKey");
%>
<%
   	String from = request.getParameter("from");
%>

<%@ include file="/WEB-INF/views/jsp/header.jsp" %>

<div class="layout-main">
  <%@ include file="/WEB-INF/views/jsp/menu.jsp" %>

  <div class="layout-content">
    <div class="layout-content-body ">
            	   
    	<div class="title-bar mb-4">
			<h1 class="title-bar-title">
				<span class="d-ib">Customer Profile</span> <span class="d-ib">
					<span class="sr-only"></span>
				</span>
			</h1>
			<% String agent = "agent"; 
				String supervisor = "supervisor";
			if(from != null && from.equals(agent)) { %>
				<a class="btn btn-default btn-sm" onclick="window.history.back()">
				<i class="fa fa-arrow-left" style="margin-right: 5px;"></i>
				Back
				</a>						
			<% } 
			else if(from != null && from.equals(supervisor)) { %>
				<a class="btn btn-default btn-sm" onclick="window.history.back()">
				<i class="fa fa-arrow-left" style="margin-right: 5px;"></i>
				Back
				</a>						
			<% } %>
			<p class="title-bar-description">
				<small> </small>
			</p>
		</div>
    
    	<div class="row gutter-xs">
    		<div class="col-12 col-lg-4">
    			<div class="card">
    				<div class="card-body" style="padding: 25px;">
    					<!-- HEADER  -->
    					<div class="row mb-4">
    						<div class="col-xs-12">
    							<div class="flex" style="display: flex; justify-content: start;">
    								<span class="border success circle avatar" style="margin-right: 10px; align-self: baseline;">
    								${fn:substring(userInfo.firstName, 0, 1)}${fn:substring(userInfo.lastName, 0, 1)}
    								</span>
    								<span class="title"><strong id="user-fullname">${userInfo.firstName} ${userInfo.lastName}</strong><br>
    									<small class="text-muted">User Id: ${userInfo.id} </small>
    								</span>
    							</div>    							    		
    						</div>
    						<div class="col-xs-12" id="btn-container-disable">
    						
    							<div id="action-btn-container-${userInfo.id}" class="action-btn-container" style="display: flex; margin-left: 62px;">    							
    								<div 
    									id="btnCreditCard"
    									data-id="${userInfo.id}" 
    									alt="Credit Cards" 
    									title="See Credit Cards"
    									style="display: flex; margin-top: 2px; cursor:pointer;padding-right: 5px;"
    								>
    									<img height="16" src="resources/core/img/icons/credit-card-edit-outline.png">
    								</div>
    								
    								
    								<div class="select-process" 
    									data-id="${userInfo.id}" 
    									data-email="${userInfo.email}" 
    									data-lname="${userInfo.lastName}" 
    									data-fname="${userInfo.id}" 
    									data-process="editBtn" 
    									data-toggle="modal" 
    									data-target="#myModal" 
    									alt="Edit user email" 
    									title="Edit user email"
    								>
    									<span class="fa fa-envelope" style="padding-right: 5px; font-size: 1.3em; cursor:pointer;"></span>
    								</div>    							
																					
									<div class="select-process" 
										data-id="${userInfo.id}" 
										data-email="${userInfo.email}" 
										data-lname="${userInfo.lastName}" 
										data-fname="${userInfo.firstName}"
										data-phone="${userInfo.phoneNumber}"  
										data-country="${userInfo.countryId}"
										data-language="${userInfo.preferredLanguage}"
										data-process="editUser" 
										data-toggle="modal" 
										data-target="#myModal" 
										alt="Edit user information" 
										title="Edit user information"
									>
  										<span class="fa fa-user" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span>
  									</div>	
  									  									
  									<div class="select-process"
  										data-id="${userInfo.id}"
  										data-email="${userInfo.email}"
  										data-lname="${userInfo.lastName}"
  										data-fname="${userInfo.firstName}"
  										data-process="requestAllTx"
  										data-toggle="modal" 
										data-target="#myModal"
  										alt="User transactions"
  										title="See transactions"
									>
  										<span class="fa fa-credit-card" style="padding-right: 5px; font-size: 1.3em; cursor: pointer"></span>
									</div>	
																																																																																							
									<div class="select-process" 
										id="btua-'${userInfo.id}'" 
										data-id="${userInfo.id}" 
										data-email="${userInfo.email}"
  										data-lname="${userInfo.lastName}"
  										data-fname="${userInfo.firstName}" 
										data-process="userAccess" 
										data-toggle="modal" 
										data-target="#myModal" 
										alt="Show user access log" 
										title="Show user access log"
									>
										<span class="fa fa-address-book-o" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span>
									</div>		
									
									<div class="select-process"  
										id="btn-kick-${userInfo.id}" 
										data-id="${userInfo.id}" 
										data-email="${userInfo.email}"
  										data-lname="${userInfo.lastName}"
  										data-fname="${userInfo.firstName}" 
										data-process="userChangePass" 
										alt="Change Password" 
										title="Change Password"
									>
										<span class="fa fa fa-key" style="padding-right: 5px; font-size: 1.55em; cursor:pointer;"></span>
									</div>
	
									<div class="loading">
										<div class="lds-ring">
											<div></div><div></div><div></div><div></div>
										</div>
									</div>
									 									  
								</div>							    							    							    						    						
    						</div>
    					</div>
    					
    					<!--  CONTACT INFO -->
    					<div class="row">
    						<div class="col-xs-12">
    							<ul class="list-unstyled" style="line-height: 2;">
    								<li>Phone: <span id="user-phone">${userInfo.phoneNumber}</span> 
	    								<span id="dnc-phone" style="display:none;" class="phone-dnc">DNC
	    									<img class="icon-width" src="resources/core/img/no-calls.svg" style="width: 2em;margin-left: 0.5em;" alt="Do Not Call">
	    								</span>
	    							</li>
    								<li>Email: <span id="user-email">${userInfo.email}</span></li>
    								<li><span>Source: ${userInfo.sourceCode}</span></li>
    								<li>Country: <span id="user-country">${userInfo.country}</span></li>
    							</ul>
    						</div>
    					</div>
    					
    					<!--  TOTAL INFO -->
    					<% if (currentUser.userHasAccess(CustomerUser.VIEW_AMOUNT)) {		%>
    					<div class="row mb-4">
    						<div class="col-xs-12">
    							<span><strong>Total Amount</strong></span><br>
    							<span><fmt:formatNumber currencySymbol="$" value="${userInfo.totalAmount}" type="currency" /></span>
    						</div>
    					</div>
    					 <% } %>
    					
    					<div class="row">
    						<div class="col-xs-12">
    							<button 
    								class="btn btn-success btn-block" 
    								id="btn-sell" 
    								data-email="${userInfo.email}" 
    								data-fname="${userInfo.firstName}"
    								data-lname="${userInfo.lastName}"
    								data-id="${userInfo.id}" 
    							>
    								SELL
    							</button>
    							<!-- 
    							<a class="btn btn-success btn-block" href="${pageContext.request.contextPath}/callCenter?email=${userInfo.email}&fname=${userInfo.firstName}&lname=${userInfo.lastName}">
    								SELL
    							</a>
    							-->
    						</div>
    					</div>
    				</div>    				
    			</div>
    		
    			<!--  TAGS -->
    			<div class="card">    			
    				<div class="card-title">
    					<span class="title">Tags:</span> 
    				</div>
    				<div class="card-body">
    					<input 
    						type="text" 
    						id="input-tags" 
    						class="form-control" 
    						data-id="${userInfo.id}"     	
    						data-data='${userInfo.tags}'
    						placeholder="Write a tag to mark the customer" 
    					/>
    				</div>
    			</div>
    		</div>
    	
    		<div class="col-12 col-lg-8">
    			<!-- LIFE CYCLE -->
    			<div class="card">    				
    				<div class="card-title">   
    					<span class="title">Life Cycle:</span> 
    				</div>
    				<div class="card-body" style="overflow-x: auto;">       										    			    		            				            			           		         		   			            		            	
            			<div class="wizard">            				
    						<%-- JSP foreach tag --%>
							<c:forEach var="item" items="${userLifecycle}" varStatus="status">	
								<c:set var="itemText" value = "${item.label}" />
								<a 
									data-id="${userInfo.id}" 
									data-disabled="${itemText == ''}" 
									data-category-event="${item.categoy_event}" 
									class="item-life-cycle ${itemText != '' && item.condition_color == 0 ? 'done' : ''} ${itemText != '' && item.condition_color == 1 ? 'pending' : ''}"
								>
									${lifeCycle[status.index]}
								</a>								
							</c:forEach>
						</div>            			            			            			            
            		</div>
            	</div>
    		
    			<!-- USER ACTIVITY -->
    			<div class="card" style="min-height: 215px;">  
    				<div class="card-title">   
    					<span class="title">User Activity:</span> 
    				</div>  				
    				<div class="card-body">       					    					
    					<ul class="list-unstyled">
    					    						    					
    						<li style="margin-bottom: 10px;">											
								<strong>Last purchase:</strong><br>																																
								<fmt:parseDate value="${userRecentPurchase.recentpurchase}" var="parsedDate" pattern="dd-MM-yyyy H:m:s" />																
								<span>
									${userRecentPurchase.eventname}. 
									<fmt:formatDate type="date" pattern="dd MMMM yyyy" value="${parsedDate}"/> @ <fmt:formatDate type="time" pattern="HH:mm:ss" value="${parsedDate}" />
									Sold by: ${userRecentPurchase.emailoperator}
								</span>	
							</li>
							
							<li style="margin-bottom: 10px;">											
								<strong>Last Login:</strong><br>		
								<fmt:parseDate value="${recentLogin.recentlogin}" var="parsedDate" pattern="dd-MM-yyyy H:m:s" />																
								<span>
									${recentLogin.eventologin}. <fmt:formatDate type="date" pattern="dd MMMM yyyy" value="${parsedDate}"/> @ <fmt:formatDate type="time" pattern="HH:mm:ss" value="${parsedDate}" />
								</span>																								
							</li>
														    					    					    					    																																							
    					</ul>    					
    				</div>
    			</div>	
    		    			    		
    			<!-- BITACORA -->
    			<div class="card">  			
    				<div class="card-body">    				
    					<ul class="nav nav-pills">
  							<li class="active"><a data-toggle="tab" href="#bitacora">Notes</a></li>
  							<!--
  							<li><a data-toggle="tab" href="#freshdesk">FreshDesk</a></li>
  							<li><a data-toggle="tab" href="#shopify">Shopify</a></li>
  							-->
						</ul>
						
						<div class="tab-content">
  							<div id="bitacora" class="tab-pane fade in active">  	
  								<div class="row">
  									<div class="col-12">
  										<label class="text-uppercase">NOTE</label>
    									<textarea id="comment" maxlength="255" class="form-control" rows="4"></textarea>
    									<div class="col-12 text-right" style="margin-top: 15px;">							
    										<button 
    											data-id="${userInfo.id}" 
    											id="btn-bitacora" 
    											class="btn btn-primary"
    											disabled
    										>
    												<span class="text-uppercase">Add Note</span>
    										</button>
    									</div>  									
  									</div>
  								</div>							
  					
    							<div class="row">
    								<div class="col-12">
    									<ul class="list-unstyled" id="bitacora-list">
    										<%-- JSP foreach tag --%>
											<c:forEach var="item" items="${listUserActivities}" varStatus="status">							
												<li style="margin-bottom: 10px;">											
													<strong>${item.commentdate} - Operator: ${item.emailop}</strong><br>
													<span>${item.comment}</span>																									
												</li>
											</c:forEach>
    									</ul>
    								</div>
    							</div>
  							</div>
  							<div id="freshdesk" class="tab-pane fade">
    							<!-- TODO -->
  							</div>
  							<div id="shopify" class="tab-pane fade">
    							<!-- TODO -->
  							</div>
						</div>
    				
    				</div>
    			</div>
    				
    		</div>		
    </div> 
  </div>
</div>


<!-- MODAL -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
  <div id="modal-content-container" class="modal-dialog" role="document">
  

  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!--  MODAL -->

<!-- MODAL -->
<div class="modal fade" id="creditCardModal" tabindex="-1" role="dialog">
  <div id="modal-content-container" class="modal-dialog" role="document">
  	<div class="modal-content" id="body-addCreditcard">
		<div class="modal-header">
	    	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	     	<h4 class="modal-title">New Credit Card</h4>
	    </div>
	    <div class="modal-body">
	    	<form id="creditCardForm" data-id="${userInfo.id}" action="" method="post">
	    		    			    			    
  				<div class="form-group stripe-form-elements list-credicard"></div>
  				<!-- CREDIT CARD -->
  				<div class="form-group stripe-form-elements card-1">
    				<label for="cardNumber" class="col-12 control-label">Credit Card Number:</label>
    				<div class="col-12">
      					<div class="input-with-icon">
        					<input
          						placeholder="Credit Card"
          						type="text"
          						id="cardNumber"
          						class="form-control"
          						name="cardNumber"
          						maxlength="19"
          						data-inputmask="'mask': '9999-9999-9999-9999'"
          						required
        					/>
        					<div class="icon icon-credit-card input-icon"></div>
      					</div>
    				</div>
    				<div class="custom-message-error"></div>
  				</div>

  				<!-- EXP DATE -->
  				<div class="form-group stripe-form-elements card-1">
    				<label for="expDate" class="col-12 control-label">Expiration Date:</label>
    				<div class="col-12">
      					<div class="input-with-icon">
        					<input
          						type="tel"
          						id="expDate"
          						class="form-control"
          						name="expDate"
          						maxlength="5"
          						placeholder="MM/AA"
          						data-inputmask="'mask': '12/99'"
          						required
        					/>
        					<div class="icon icon-calendar input-icon"></div>
      					</div>
    				</div>
    				<div class="custom-message-error"></div>
  				</div>

  				<!-- CVV -->
  				<div class="form-group stripe-form-elements card-1">
    				<label for="cvv" class="col-12 control-label">Security Code:</label>
    				<div class="col-12">
      					<div class="input-with-icon">
        					<input
          						type="tel"
          						id="cvv"
          						class="form-control"
          						name="cvv"
          						maxlength="4"
          						placeholder="7777"
          						required
        					/>
        					<div class="icon icon-lock input-icon"></div>
      					</div>
    				</div>
    				<div class="custom-message-error"></div>
  				</div>
  				
			</form>
			
			<div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  	<a href="#" class="alert-link success-msg-alert"></a>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
				<a href="#" class="alert-link error-msg-alert"></a>
			</div>
	    </div>
	    
	    <div class="modal-footer">	    
        	<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        	<button type="button" id="btnSaveCreditCard" class="btn btn-primary">
        		Save
        	</button>        	
        	<div id="submit-spinner" class="spinner spinner-default spinner-md" style="display:none;"></div>        	
     	</div>
	</div>	
			
  </div>
</div><!-- /.modal -->
<!--  MODAL -->

<!--  CONFIRM -->
<div id="confirm" class="modal fade" tabindex="-1" role="dialog">
	<div id="modal-content-container" class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
	    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	     		<h4 class="modal-title">Confirmation</h4>
	    	</div>
  			<div class="modal-body">
    			 Are you sure?
  			</div>
  			<div class="modal-footer">
    			<button type="button" data-dismiss="modal" class="btn btn-primary" id="delete">Delete</button>
    			<button type="button" data-dismiss="modal" class="btn">Cancel</button>
  			</div>
  		</div>
  	</div>
</div>


<script type="text/javascript">
	Stripe.setPublishableKey("<%=stripeKey%>");
	var statusSales = "${userInfo.statusSales}";
</script>



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
	
		UtilPropertiesLoader props = UtilPropertiesLoader.getInstance();
		Boolean emailConfirmAct = Boolean.valueOf(props.getProp("email.confirm"));
	%>
  var emailConfirmActivate = <%=emailConfirmAct%>;	
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
<script src="${usersJs}"></script>
<script src="${userAccessJS}"></script>
<script src="${userDetailJs}"></script>
<script src="${selectizeJs}"></script>
<script src="${validateJs}"></script>
<script src="${toasrtJS}"></script>
<script src="${bsModalFS}"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/dayjs/1.11.1/dayjs.min.js"></script>
<script>
	$(".usersModule").removeClass("active");
	$(".lifeCycleModule").addClass("active");	
</script>

</body>
</html>