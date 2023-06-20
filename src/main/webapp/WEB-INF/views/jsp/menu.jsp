<%@page import="com.weavx.web.model.CustomerUser"%>
<%@page import="java.util.ArrayList"%>
<div class="layout-sidebar">
	<div class="layout-sidebar-backdrop"></div>
	<div class="layout-sidebar-body">
		<div class="custom-scrollbar">
			<nav id="sidenav" class="sidenav-collapse collapse">
				<ul class="sidenav level-1">
					<li class="sidenav-search hidden-md hidden-lg">
						<form class="sidenav-form" action="/">
							<div class="form-group form-group-sm">
								<div class="input-with-icon">
									<input class="form-control" type="text" placeholder="Search">
									<span class="icon icon-search input-icon"></span>
								</div>
							</div>
						</form>
					</li>
					<li class="sidenav-heading">Navigation</li>
				 	
                    
                    <%
                    
                    CustomerUser currentUser = (CustomerUser) session.getAttribute("userData");
                    
					if (currentUser.userHasAccess(CustomerUser.REPORTS)) {		
					%>	
					<li class="sidenav-item has-subnav menuReport">
			        	<a href="" aria-haspopup="true">
                           	<span class="sidenav-icon"><i class="fa fa-file-text"></i></span>             
			                <span class="sidenav-label">Reports</span>
			            </a>
			            <ul class="sidenav level-2 collapse collapseReport">
			            	<li class="sidenav-heading menuReport">Reports</li>
		                	<li class="sidenav-item has-subnav menuReport collapseReport">
		                    	<a href="">Registrations</a>
		                      	<ul class="sidenav level-3 collapse collapseReport2">
		                        	<li id="menuReportGeneral" class="sidenav-item">
		                          		<a href="${home}report">This event</a>
		                         	</li>
		                         	<%
		                         	if (currentUser.userHasAccess(CustomerUser.GLOBAL_REPORT)) {		
									%>	
		                         	<li id="menuReportGlobal" class="sidenav-item">
		                          		<a href="${home}globalReport">Global</a>
		                         	</li>
		                         	<%}
		                         	if ((currentUser.userHasAccess(CustomerUser.REFRESH_REPORTING_TABLES))){
		                         	%>
		                         	<li id="menuRefreshData" class="sidenav-item">
		                          		<a href="#" class="modal-open-refresh-data">Refresh data</a>
		                         	</li>
		                         	   	<%}%>
		                        	<%-- <li id="menuReportFinance" class="sidenav-item">
		                          		<a href="${home}reportFinances">Custom Finance</a>
		                         	</li> --%>
		                        </ul>
		                	</li>
			            </ul>
			            <%
                       	if (currentUser.userHasAccess(CustomerUser.REPORT_AFFILIATE)) {		
						%>	
			            <ul class="sidenav level-2 collapse collapseSaleReport">
			            	<li class="sidenav-heading menuSalesReport">Reports</li>
		                	<li class="sidenav-item has-subnav menuReport collapseSaleReport">
		                    	<a href="">Affiliates</a>
		                      	<ul class="sidenav level-3 collapse collapseSaleReport2">
		                        	<li id="menuReporteVentaGeneral" class="sidenav-item">
		                          		<a href="${home}salesReport">Comissions</a>
		                         	</li>
		                         	<li id="menuReporteVentaDetalle" class="sidenav-item">
		                          		<a href="${home}detailSalesReport">Details</a>
		                         	</li>     	
		                        </ul>
		                	</li>
			            </ul>
			            <%}%>
			    	</li>	
			    	<% } %>
			    	
			    	<%  if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN) && currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {	%>	
					<li class="sidenav-item has-subnav menuSales">
			        	<a href="" aria-haspopup="true">
                           	<span class="sidenav-icon"><i class="fa fa-file-text"></i></span>             
			                <span class="sidenav-label">Sales</span>
			            </a>
			           <ul class="sidenav level-2 collapse collapseSale">
							<%
                         	if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN)) {		
							%>
							<li id="menuSalesSupervisor" class="sidenav-item"><a
								href="${home}salesSupervisor">Lead Manager</a></li>
							<%
                         	}
                         	if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {		
							%>
							<li id="menuSalesSCR" class="sidenav-item"><a
								href="${home}SCRLeads">SCR Leads</a></li>
							<li id="menuSalesAgent" class="sidenav-item"><a
								href="${home}SDILeads">SDI Leads</a></li>
							<li id="menuSalesClass" class="sidenav-item"><a
								href="${home}ClassLeads">Class Leads</a></li>
							<%}%>
						</ul>
					</li>	
			    	<% } %>
			    	
			    	<%  if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN) && !currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {	%>	
					<li class="sidenav-item has-subnav menuSales">
			        	<a href="${home}salesSupervisor" aria-haspopup="true">
                           	<span class="sidenav-icon"><i class="fa fa-file-text"></i></span>             
			                <span class="sidenav-label">Sales</span>
			            </a>
					</li>	
			    	<% } %>
			    	
			    	<%  if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT) && !currentUser.userHasAccess(CustomerUser.VIEW_SALES_ADMIN)) {	%>	
					<li class="sidenav-item has-subnav menuSales">
			        	<a href="" aria-haspopup="true">
                           	<span class="sidenav-icon"><i class="fa fa-file-text"></i></span>             
			                <span class="sidenav-label">Sales</span>
			            </a>
			           <ul class="sidenav level-2 collapse collapseSale">
							<%
                         	if (currentUser.userHasAccess(CustomerUser.VIEW_SALES_AGENT)) {		
							%>
							<li id="menuSalesSCR" class="sidenav-item"><a
								href="${home}SCRLeads">SCR Leads</a></li>
							<li id="menuSalesAgent" class="sidenav-item"><a
								href="${home}SDILeads">SDI Leads</a></li>
							<li id="menuSalesClass" class="sidenav-item"><a
								href="${home}ClassLeads">Class Leads</a></li>
							<%}%>
						</ul>
					</li>	
			    	<% } %>
			    	
			    	<% if (currentUser.userHasAccess(CustomerUser.USERS_QUERY)) {	%>	
			    		<li class="sidenav-item has-subnav usersModule">
				        	<a href="${home}users" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-user"></i></span>             
				                <span class="sidenav-label">Registered Users</span>
				            </a>
			    		</li>	
			    	<% } %>
			    	
			    	<% if (currentUser.userHasAccess(CustomerUser.HOJA_VIDA_MENU)) {	%>	
			    		<li class="sidenav-item has-subnav lifeCycleModule">
				        	<a href="${home}usersLifecycle" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-user"></i></span>             
				                <span class="sidenav-label">Users</span>
				            </a>
			    		</li>	
			    	<% } %>
			    	
			    	<% if (currentUser.userHasAccess(CustomerUser.ADD_ATTENDEE) && currentUser.getCustomerId() != 462) {		%>
			    	<li class="sidenav-item menuPurchase" >
					 	<a href="${home}callCenter" >
                        	<span class="sidenav-icon"><i class="fa fa-phone"></i></span>                         	            
                        	<span class="sidenav-label">Add Attendee</span>
	                  	</a>
	                  <!--  	<ul class="sidenav level-2 collapse collapsePurchase">
	                    	<li class="sidenav-heading ">Paypal</li>
	                    	<li id="menuPaypal" ><a href="${home}paypal">Paypal</a></li>
	                  	</ul>
	                  	<ul class="sidenav level-2 collapse collapsePurchase">
	                    	<li class="sidenav-heading ">Call Center</li>
	                    	<li id="menuStripe" ><a href="${home}callCenter">Call Center</a></li>
	                  	</ul>-->
	                </li> 
	                <% } %>
	                
	                <%  if (currentUser.userHasAccess(CustomerUser.RESTRICT_EVENT)) {		%>
	                <li class="sidenav-item has-subnav menuRestrict">
			        	<a href="" aria-haspopup="true">
                           	<span class="sidenav-icon"><i class="fa fa-lock"></i></span>             
			                <span class="sidenav-label">Restrict Event</span>
			            </a>
			            <ul class="sidenav level-2 collapse collapseRestrict">


							<li class="sidenav-item menuAddUsersRestricted" >
								<a href="${home}restrictActualEvent" >
									<span class="sidenav-label">Event Settings</span>
								</a>
							</li>
			            	
			            	<li class="sidenav-item menuAddUsersRestricted" >
			            		<a href="${home}restrictEvent" >                       	            
		                        	<span class="sidenav-label">Add users</span>
			                  	</a>
			            	</li>
			            	
			            	<li class="sidenav-item listRestrictedUsers">
			            		<a href="${home}listRestrictedUsers" >                       	            
		                        	<span class="sidenav-label">List users</span>
			                  	</a>
			            	</li>
		                	
			            </ul>
			    	</li>	
	                <% } %>
	                
	                <% if (currentUser.userHasAccess(CustomerUser.BLACKLIST_QUERY)) {	%>	
			    		<li class="sidenav-item has-subnav blackList">
				        	<a href="${home}black-list" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-user-times"></i></span>             
				                <span class="sidenav-label">Blacklist Users</span>
				            </a>
			    		</li>	
			    	<% } %>

					<% if (currentUser.userHasAccess(CustomerUser.WHILELIST)) {	%>	
			    		<li class="sidenav-item has-subnav whiteList">
				        	<a href="${home}white-list" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-user"></i></span>             
				                <span class="sidenav-label">Whitelist Users</span>
				            </a>
			    		</li>	
			    	<% } %>
			    	
			    	 <% if (currentUser.userHasAccess(CustomerUser.AUDIT_QUERY)) {	%>	
			    		<li class="sidenav-item has-subnav appLog">
				        	<a href="${home}app-log" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-folder-open-o"></i></span>             
				                <span class="sidenav-label">App log events</span>
				            </a>
			    		</li>	
			    	<% } %>
	             <!--     <li class="sidenav-item  menuCoupon" >
					 	<a href="${home}coupons" >
                        	<span class="sidenav-icon"><i class="fa fa-ticket"></i></span>                         	            
                        	<span class="sidenav-label">Coupon Generator</span>
	                  	</a>
	                	<ul class="sidenav level-2 collapse collapsePurchase">
	                    	<li class="sidenav-heading ">Paypal</li>
	                    	<li id="menuPaypal" ><a href="${home}paypal">Paypal</a></li>
	                  	</ul>
	                  	<ul class="sidenav level-2 collapse collapsePurchase">
	                    	<li class="sidenav-heading ">Call Center</li>
	                    	<li id="menuStripe" ><a href="${home}callCenter">Call Center</a></li>
	                  	</ul>
	                </li> -->
			    	  <% if (currentUser.userHasAccess(CustomerUser.EVENT_ASSETS)) {	%>	
			    		<li class="sidenav-item has-subnav liveSettings">
				        	<a href="${home}live-setting" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-file-video-o"></i></span>             
				                <span class="sidenav-label">Live Event Assets</span>
				            </a>
			    		</li>	
			    	<% } %>
					<% if (currentUser.userHasAccess(CustomerUser.SOURCES)) {	%>	
			    		<li class="sidenav-item has-subnav sourceAdmin">
				        	<a href="${home}source-settings" aria-haspopup="true">
	                           	<span class="sidenav-icon"><i class="fa fa-bars"></i></span>             
				                <span class="sidenav-label">Sources</span>
				            </a>
			    		</li>	
			    	<% } %>		
					
						<li class="sidenav-item">
							<a href="${home}scan-qr" aria-haspopup="true">
								<span class="sidenav-icon"><i class="fa fa-qrcode" aria-hidden="true"></i></span>             
								<span class="sidenav-label">Scan QR</span>
							</a>
						</li>	
				</ul>
			</nav>
		</div>
	</div>
</div>