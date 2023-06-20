<%@page import="java.util.TreeMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
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
<spring:url value="/resources/core/mod/login/event.js" var="eventJS" />

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Seminario Admin</title>
    
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
        <div>
            <h3>Upcoming events:</h3>
        </div>
        <div class="login-form">
          <form id="choose-event-form">
            <div id="select-event-container" class="form-group">
              <select id="select-event" name="selectvent" class="form-control" required>
              <%HashMap<Integer,String> varEvent = (HashMap<Integer,String>) session.getAttribute("listEvent");%>
              <% TreeMap<Integer, String> varEventSorted = new TreeMap<>(); %>
              <% varEventSorted.putAll(varEvent); %>	
              <option>Select</option>
              <%if (varEventSorted != null){%>
			  		<%for(Map.Entry<Integer,String> entry : varEventSorted.entrySet()) {%>
			  			<%int id = entry.getKey();%>
			  			<%String name = entry.getValue();%>	
			  			<%="<option value="+id+">"+name+"</option>" %>
			  		<%}%>
		  	  <%}%>
              </select>
             
            </div>
           
          <!--  <div>
            	<small  class="help-block text-warning">You must choose an event</small>
            </div> -->
            
            <div class="form-group">
               <button id="choose-event" class="btn btn-primary btn-block">Choose</button>
            </div>
           
          </form>
        </div>
      </div>
      
      <!-- SELECT EVENT HISTORY  -->
      
	      <div class="login-body">
	        <div>
	            <h3>Past Events:</h3>
	        </div>
	        <div class="login-form">
	          <form id="choose-event-form">
	            <div id="select-event-container" class="form-group">
	              <select id="select-event-history" name="selectvent" class="form-control" required>
	              <%HashMap<Integer,String> varEvent1 = (HashMap<Integer,String>) session.getAttribute("listEventHistory");%>
	              <% TreeMap<Integer, String> varEvent1Sorted = new TreeMap<>(); %>
              	  <% varEvent1Sorted.putAll(varEvent1); %>	
	              <option>Select</option>
	              <%if (varEvent1Sorted != null){%>
				  		<%for(Map.Entry<Integer,String> entry : varEvent1Sorted.entrySet()) {%>
				  			<%int id = entry.getKey();%>
				  			<%String name = entry.getValue();%>	
				  			<%="<option value="+id+">"+name+"</option>" %>
				  		<%}%>
			  	  <%}%>
	              </select>
	             
	            </div>
	           
	          <!--  <div>
	            	<small  class="help-block text-warning">You must choose an event</small>
	            </div> -->
	            
	            <div class="form-group">
	               <button id="choose-event-history" class="btn btn-primary btn-block">Choose</button>
	            </div>
	           
	          </form>
	        </div>
	      </div>
      
      <!-- END SELECT EVENT HISTORY -->
      
      <div class="login-footer">
        <small class="version">Version 1.0.1</small> 
        <small class="copyright">2018 &copy; <a href="">Seminario</a></small>
      </div>
    </div>    
     <script src="${vendorJS}"></script>
	 <script src="${elephantJS}"></script>
	 <script src="${adminJS}"></script>
     <script src="${modalJS}"></script>
     <script src="${loadingJS}"></script>
     <script src="${eventJS}"></script>     
  </body>
</html>