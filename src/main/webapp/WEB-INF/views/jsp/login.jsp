<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jsp/include.jsp"%>


<c:url var="home" value="/" scope="request" />

<spring:url value="https://fonts.googleapis.com/css?family=Roboto:300,400,400italic,500,700" var="fonts" />

<spring:url value="/resources/core/mod/elephant/elephant.css" var="elephantCSS" />
<spring:url value="/resources/core/mod/elephant/elephant.js" var="elephantJS" />

<spring:url value="/resources/core/mod/vendor/vendor.min.css" var="vendorCSS" />
<spring:url value="/resources/core/mod/vendor/vendor.min.js" var="vendorJS" />

<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.css" var="loadingCSS" />
<spring:url value="/resources/core/mod/loading/jquery.loadingModal.min.js" var="loadingJS" />

<spring:url value="/resources/core/mod/modal/jquery.fancybox.min.css" var="modalCSS" />
<spring:url value="/resources/core/mod/modal/jquery.fancybox.min.js" var="modalJS" />

<spring:url value="/resources/core/mod/admin/admin.css" var="adminCSS" />
<spring:url value="/resources/core/mod/admin/admin.js" var="adminJS" />

<spring:url value="/resources/core/mod/login/login.css" var="loginCSS" />
<spring:url value="/resources/core/mod/login/login.js" var="loginJS" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${title}</title>
    
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
    <meta name="description" content="Elephant is a front-end template created to help you build modern web applications, fast and in a professional manner.">
    <meta property="og:url" content="http://demo.naksoid.com/elephant">
    <meta property="og:type" content="website">
    <meta property="og:title" content="The fastest way to build modern admin site for any platform, browser, or device">
    <meta property="og:description" content="Elephant is a front-end template created to help you build modern web applications, fast and in a professional manner.">
    <meta property="og:image" content="http://demo.naksoid.com/elephant/img/ae165ef33d137d3f18b7707466aa774d.jpg">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@naksoid">
    <meta name="twitter:creator" content="@naksoid">
    <meta name="twitter:title" content="The fastest way to build modern admin site for any platform, browser, or device">
    <meta name="twitter:description" content="Elephant is a front-end template created to help you build modern web applications, fast and in a professional manner.">
    <meta name="twitter:image" content="http://demo.naksoid.com/elephant/img/ae165ef33d137d3f18b7707466aa774d.jpg">


    <link rel="apple-touch-icon" sizes="180x180" href="resources/core/img/Favicon.png">
    <link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="32x32">
    <link rel="icon" type="image/png" href="resources/core/img/Favicon.png" sizes="16x16">
    <link rel="manifest" href="manifest.json">
    <link rel="mask-icon" href="safari-pinned-tab.svg" color="#d9230f">
    <meta name="theme-color" content="#ffffff">

    <link rel="stylesheet" href="${fonts}">
    <link rel="stylesheet" href="${vendorCSS}">
    <link rel="stylesheet" href="${elephantCSS}">
    <link rel="stylesheet" href="${adminCSS}">
	<link rel="stylesheet" href="${modalCSS}">
	<link rel="stylesheet" href="${loadingCSS}">
	<link rel="stylesheet" href="${loginCSS}">
	    
  </head>
  <body>
    <div class="login">
      <div class="login-body">
        <div style="background-color: black; padding: 10px 10px 10px 10px">
        <a class="login-brand" href="#">
          <img class="img-responsive" src="resources/core/img/Logo-admin.svg" alt="Elephant">
        </a>
        </div>
        <h3 class="login-heading">Sign in</h3>
        <div class="login-form">
          <form data-toggle="validator">
            <div class="form-group">
              <label for="username" class="control-label">Username</label>
              <input id="username" class="form-control" type="text" name="username" spellcheck="false" autocomplete="off" data-msg-required="Please enter your username." required>
            </div>
            <div class="form-group">
              <label for="password" class="control-label">Password</label>
              <input id="password" class="form-control" type="password" name="password" minlength="6" data-msg-minlength="Password must be 6 characters or more." data-msg-required="Please enter your password." required>
            </div>
            
            <div>
            	<small id="message" class="text-warning"></small>
            </div>
            
            <div class="form-group">
              <button id="login" class="btn btn-primary btn-block" type="button">Sign in</button>
            </div>
            <div class="form-group">
              <ul class="list-inline">
                <li>
                  <label class="custom-control custom-control-primary custom-checkbox">
                    <input class="custom-control-input" type="checkbox">
                    <span class="custom-control-indicator"></span>
                    <span class="custom-control-label">Keep me signed in</span>
                  </label>
                </li>
              </ul>
              <ul class="list-inline">
              </ul>
            </div>
          </form>
        </div>
      </div>
      <div class="login-footer">
        <small class="version">Version 1.0.1</small> 
        <small class="copyright">2020 &copy; <a href="">Seminario</a></small>
        <%
        	String option = null;
        	if(session.getAttribute("option") != null){
        		option = String.valueOf((int) session.getAttribute("option"));	
        	}else if(session.getAttribute("optionHistory") != null){
        		option = String.valueOf((int) session.getAttribute("optionHistory"));
        	}
        %>
        <script type="text/javascript">
			var optionSelected = <%=option%>; 
        </script>
      </div>
    </div>    
     <script src="${vendorJS}"></script>
	 <script src="${elephantJS}"></script>
	 <script src="${adminJS}"></script>
     <script src="${modalJS}"></script>
     <script src="${loadingJS}"></script>
     <script src="${loginJS}"></script>     
  </body>
</html>