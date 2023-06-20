
(function($) {
	'use strict';
	
	//LOGGER
	var Logger = {
		DEBUG : true,
		init : function init() {
			this.log("Logger.init");
		},		
		log : function(){
		    if (this.DEBUG) {
		    	var args = Array.prototype.slice.call(arguments);
		        console.log.apply(console, args);
		    }
		}
	};
	Logger.init();
	$.logger = Logger;
	
	//ADMIN
	var Admin = {
		Constants : {
			CONTEXT : "/scr-admin/",
			SUCCESS : "Operation Success",
			ERROR   : "Operation Fail",
		},
		HttpCodes: {
			SUCCESS : 200,
			"200"   : "Success",
			"400"   : "Bad Request",
			"404"   : "Page Not Found",
			"500"   : "Internal Server Error",
		},
	    KeyCodes: {
	        ENTER : 13,
	    },		
		init : function init() {
			$.logger.log("Admin.init");
		},
		dispatch : function dispatch(action, callback, data) {	
			var url  = this.Constants.CONTEXT + action;
			$.logger.log("Admin.dispatch: ", url, "|", data);	
			$('body').loadingModal({text: 'loading...', 'animation': 'wanderingCubes'});
			$('body').loadingModal('show');
			$.ajax({
				type        : "POST",
				contentType : "application/json",
				url         : url,
				data        : JSON.stringify(data),
				dataType    : 'json',
				timeout     : 100000,
				complete    : function(e) {
					$.logger.log("ajax fething [" + url + "] | code: " , e.status, "|", e);
					$('body').loadingModal('hide');
					if (e.status === $.admin.HttpCodes.SUCCESS) {
						if (callback) {
							var json = JSON.parse(e.responseText);
							if (json.code) {
								if (json.code == $.admin.HttpCodes.SUCCESS) {									
									json = json.result ? json.result : json.msg ? JSON.parse(json.msg) : null;
									callback(json);
								} else {
									$.admin.message(json.msg);
								}
							} else {
								callback(json);
							}
						} else {
							$.admin.message(this.Constants.SUCCESS);
						}
					} else {
						$.logger.log("ajax error on [" + url + "] | code: ", e.status, "|", e);
						$.admin.error(e);
					}
				}
			});
			return this;
		},
		message : function message(msg, type) {
			$.logger.log("Admin.message: ", msg, type);
			$("#message").removeClass(".error");
			$("#message").html(msg);
			if (type == this.Constants.ERROR) {
				$("#message").addClass(".error");
			}
			$.fancybox.open('<div><div style="background-color: black; padding: 10px 10px 10px 10px"><img  width="100" src="resources/core/img/Logo-admin.svg" alt="Elephant"></div><br/><div class="modal-message">'+msg+'</div></div>');
			return this;
		},
		error : function error(e) {
			if (typeof e == 'object') {
				var status = e.status;
				e = this.HttpCodes[status];
			}
			return this.message(e, this.Constants.ERROR);
		},
		registerEvent : function registerEvent(object) {
			$.logger.log("Admin.registerEvent: ", object.target, "|", object.event, "|",  object.action);
			if (object && object.target && object.event) {
				if (object.action) {
					$(object.target).on(object.event, function() {
						if (object.prepare) object.prepare();
						$.admin.dispatch(object.action, object.callback, object.data);
					}.bind(this));
				} else {
					$(object.target).on(object.event, object.callback);					
				}
			}
		},
		registerDefaultEnterKeyEvent : function registerDefaultEnterKeyEvent(object) {
			$.logger.log("Admin.registerDefaultEnterKeyEvent: ", object.target);
			if (object && object.target && object.callback) {
				$(object.target).keypress(function (e) {
					var key = e.which;
					if(key == $.admin.KeyCodes.ENTER) {
						object.callback();
						return false;  
					}
				});   
			}
		},
		activeMenuOption : function activeMenuOption(option) {
			$("#"+option).addClass("active");
		}
	};
	Admin.init();	
	$.admin = Admin;
		
	

	
	//LABEL	
	var Label = {
		init: function init() {
			$.logger.log("Label.init...");
			var url =  $.admin.Constants.CONTEXT + "resources/core/labels.properties";
			$.ajax({
				url         : url,								
				complete    : function(e) {
					$.logger.log("label ajax fething [" + url + "] | code: " + e.status);
					if (e.status === $.admin.HttpCodes.SUCCESS) {							
						$.label.process(e.responseText);
					} else {
						$.logger.log("label ajax error on [" + url + "] | code: " + e.status);
						$.admin.error(e);
					}
				}
			});
			return this;
		},
		process : function process(data) {
			$.logger.log("Label.processing...");
			var linesArray = data.split('\n');
			if (linesArray){
				linesArray.forEach(function (line, index, array){
					var keyValPair, value = '';						
					line = line.trim();
					if (line === '' || line.charAt(0) === '#'){
						return;
					}					
					keyValPair = line.match(/([^=]*)=(.*)$/);
					if (keyValPair && keyValPair[1]){
						if (keyValPair[2]){
							value = keyValPair[2].trim();
						}
						var k = keyValPair[1].trim();
						var keyArray = k.split(".");
						var target = $.label;						 
						keyArray.forEach(function(label, index, array) {
							k = label;
							if (index == keyArray.length - 1) {
								return;
							}							
							if (typeof target[k] == 'undefined') {
								target[k] = {};
							}
							target = target[k];
						});
						target[k] = value;
					} else {
						$.logger.log('Error Label processing | invalid line: ' + line);
					}
				});
			}	
			$.logger.log("Label.loaded!");
		}
	};
	Label.init();
	$.label = Label;
		
	//UTIL
	var Util = {
		init : function init() {
			$.logger.log("Util.init");
		},
		sizeOf : function sizeOf(obj) {
		    var size = 0, key;
		    for (key in obj) {
		        if (obj.hasOwnProperty(key)) size++;
		    }
		    return size;							
		}
	};
	Util.init();
	$.util = Util;

	//UTIL
	var Format = {
		init : function init() {
			$.logger.log("Format.init");
		},
		toCurrency : function toCurrency(number) {
		    return "$" + number.format("2", ".", ",");							
		},
		toPercentage : function toPercentage(number) {
			return "%" + number.format("2", ".", ",");
		},
		toQuantity : function toQuantity(number) {
			return number.format("0", ".", ",");
		}
	};
	Format.init();
	$.format = Format;
	
	//LOGOUT EVENT
	var LogoutEvent = {
		target   : "#signOut, #signOutMobile",
		event    : "click",
		action   : "search/api/closeAuthenticateAdmin",
		data     : null,
		prepare  : function() {
			this.data = { 
				AuthenticateUser: null
			}		
		},		
		callback : function(data) {window.location = $.admin.Constants.CONTEXT;}
	};	
	$.admin.registerEvent(LogoutEvent);

})(jQuery);

Number.prototype.format = function(c, d, t){
	var n = this, 
	    c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "." : d, 
	    t = t == undefined ? "," : t, 
	    s = n < 0 ? "-" : "", 
	    i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))), 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
 };	
 
 $(".changeKey").click(function() {
	 	var id = this.id;
	 	if (id!="Select"){
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "search/api/selectkeyInside",
				data : id,
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					if (data.code==="200") {
							window.location.href = data.msg;
						} else  {
							window.location.href = data.msg;
						}

				},
				error : function(e) {
					console.log("ERROR: ", e);
					//alert("ERROR");
				},
				done : function(e) {
					//alert("DONE");
					console.log("DONE");
				}
			});
		}
		
	});

	var mytimeOnline = setTimeout(() => {
		$("#navbar-info-title").html(document.title);
		customerOnlineInf();
	}, 3000);

	$("#navbar-info-refresh").click(function() {
		customerOnlineInf();
	});

	
	function customerOnlineInf() 
	{
		clearTimeout(mytimeOnline);
		$.ajax(
		{
		  type: "GET",
		  contentType: false,
		  url: "search/api/online",
		  dataType: "html",
		  timeout: 0,
		  processData: false,
		  cache: false,
		  beforeSend: function() 
		  {
			$(".navbar-info").addClass("loading");
		  },
		  success: function(data) 
		  {
			data = JSON.parse(data);
			if (data.returnCode == "0")
			{ 
				$("#navbar-info-title").html(document.title);
				$("#navbar-info-online").html(data.result.online_users);
				$("#navbar-info-reg").html(data.result.registered_users);
				$(".navbar-info").removeClass("loading");
				mytimeOnline = setTimeout(() => { customerOnlineInf();}, 300000);
			} 
			else
			{
				$(".navbar-info").addClass("loading");
				mytimeOnline = setTimeout(() => { customerOnlineInf();}, 60000);
			}
		  },
		  error: function(e) 
		  {
			$(".navbar-info").addClass("loading");
			mytimeOnline = setTimeout(() => { customerOnlineInf();}, 60000);
		  }
		});
	  }

/********** */
/**REGISTER */
/********** */


$(document).on('click', '.modal-open-register', function()
{
	registeredUsersRun();
});

$(document).on('click', '.modal-close-register', function()
{
	var modal = $('.modal-register');
	modal.removeClass('active');
});

function registeredUsersRun()
{
	registeredUsers();
	var modal = $('.modal-register');
	$(".modal").removeClass("active");
	modal.addClass('active');
	modal.addClass('isLoading');
	modal.removeClass('isError');
}

function registeredUsers() 
{
		$.ajax(
		{
		  type: "GET",
		  contentType: false,
		  url: "search/api/registeredUsers",
		  dataType: "html",
		  timeout: 0,
		  processData: false,
		  cache: false,
		  beforeSend: function() 
		  {
	
		  },
		  success: function(data) 
		  {
			data = JSON.parse(data);
			if (data.returnCode == "0")
			{ 
				if (data.result.details.length > 0)
				{
					desingDetailsRegisterUsers(data.result.details);
				}
				else
				{
					$('.modal-register').removeClass('isLoading');
					$('.modal-register').addClass('isError');
					printError(".modal-register-error", "Ups! An error has occurred", "There are no records to show", "");
				}
			} 
			else
			{
				$('.modal-register').removeClass('isLoading');
				$('.modal-register').addClass('isError');
				printError(".modal-register-error", "Ups! An error has occurred", "An error has occurred in the data", "");
			}
		  },
		  error: function(e) 
		  {
			$('.modal-register').removeClass('isLoading');
			$('.modal-register').addClass('isError');
			printError(".modal-register-error", "Ups! An error has occurred", "An unexpected error has occurred", "");
		  }
		});
}

function desingDetailsRegisterUsers(elements)
{
	$('.modal-register').removeClass('isLoading');
	var print = "";
	var date  = "";
	elements.forEach(e => {
		
		date  = e.dayofweek+", "+e.month+"-"+e.day+"-"+e.year;
		order = e.month+"-"+e.day+"-"+e.year;
		print = print +
						"<tr>"     +
						"<td data-order="+order+">"+date+"</td>"+
						"<td>"+e.transaction+"</td>"+
						"</tr>";
		});
	
	$(".modal-register-content").html('<h1 class="title-bar-title"><span class="d-ib">Users Registered<small></small></span></h1><table id="table-content-register" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>');
	$("#table-content-register").html("<thead><tr><th>Date</th><th>Total</th></tr></thead><tbody id='table-content-register-rows'></tbody>");
	$("#table-content-register-rows").html(print);

	userTable = $("#table-content-register").DataTable({
		dom: 'Bfrtip',
		buttons: [
			{
				text: '<i class="fa fa-refresh" aria-hidden="true" onclick="registeredUsersRun()"></i>',
			},
			{
				extend: 'excel',
				text: '<i class="fa fa-download" aria-hidden="true"></i>',
			},
		],
	});
	$("#table-content-register_filter input").addClass("form-control");
	
}


/********** */
/**ONLINE */
/********** */
function detailsOnlineUsers() 
{
		$.ajax(
		{
		  type: "GET",
		  contentType: false,
		  url: "search/api/detailsOnlineUsers",
		  dataType: "html",
		  timeout: 0,
		  processData: false,
		  cache: false,
		  beforeSend: function() 
		  {
	
		  },
		  success: function(data) 
		  {
				data = JSON.parse(data);
				if (data.returnCode == "0")
				{ 
					if (data.result.details.length > 0)
					{
						desingDetailsOnlineUsers(data.result.details);
					}
					else
					{
						$('.modal-online').removeClass('isLoading');
						$('.modal-online').addClass('isError');
						printError(".modal-online-error", "Ups! An error has occurred", "There are no records to show", "");
					}
				} 
				else
				{
					$('.modal-online').removeClass('isLoading');
					$('.modal-online').addClass('isError');
					printError(".modal-online-error", "Ups! An error has occurred", "An error has occurred in the data", "");
				}
		  },
		  error: function(e) 
		  {
			$('.modal-online').removeClass('isLoading');
			$('.modal-online').addClass('isError');
			printError(".modal-online-error", "Ups! An error has occurred", "An unexpected error has occurred", "");
		  }
		});
}

function desingDetailsOnlineUsers(elements)
{
	$('.modal-online').removeClass('isLoading');
	var print = "";
	var date  = "";
	var order = "";

	elements.forEach(e => 
	{
		try { order = e.hourByDay.split(' ').pop(); } catch { order = "" }

		print = print +
						"<tr>"+
						"<td data-order="+order+">"+e.hourByDay+"</td>"+
						"<td>"+e.sessions+"</td>"+
						"</tr>";
	});
	
	$(".modal-online-content").html('<h1 class="title-bar-title"><span class="d-ib">Users Online<small></small></span></h1><table id="table-content-online" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>');
	$("#table-content-online").html("<thead><tr><th>Date</th><th>Sessions</th></tr></thead><tbody id='table-content-online-rows'></tbody>");
	$("#table-content-online-rows").html(print);

	userTable = $("#table-content-online").DataTable({
		dom: 'Bfrtip',
		buttons: [
			{
				text: '<i class="fa fa-refresh" aria-hidden="true" onclick="detailsOnlineUsersRun()"></i>',
			},
			{
				extend: 'excel',
				text: '<i class="fa fa-download" aria-hidden="true"></i>',
			},
		]
	});
	$("#table-content-online_filter input").addClass("form-control");

}

function detailsOnlineUsersRun()
{
	detailsOnlineUsers();
	var modal = $('.modal-online');
	$(".modal").removeClass("active");
	modal.addClass('active');
	modal.addClass('isLoading');
	modal.removeClass('isError');
}

$(document).on('click', '.modal-open-online', function()
{
	detailsOnlineUsersRun();
});

$(document).on('click', '.modal-close-online', function()
{
	var modal = $('.modal-online');
	modal.removeClass('active');
});





/********** */
/**ONLINE */
/********** */

function processData()
{
	var modal = $('.modal-refresh-data');
	$(".modal").removeClass("active");
	modal.addClass('active');
	modal.removeClass('isError');
}

$(document).on('click', '.modal-open-refresh-data', function()
{
	processData();
});

$(document).on('click', '.modal-close-refresh-data', function()
{
	var modal = $('.modal-refresh-data');
	modal.removeClass('active');
});

$(document).on('click', '.modal-process-refresh-data', function()
{
	var modal = $('.modal-refresh-data');
	modal.addClass('isLoading');
	updateReportingTables();
});



function updateReportingTables() 
{
	$.ajax(
	{
	  type: "GET",
	  contentType: false,
	  url: "search/api/updateReportingTables",
	  dataType: "html",
	  timeout: 0,
	  processData: false,
	  cache: false,
	  beforeSend: function() 
	  {		
	  },
	  success: function(data) 
	  {
		data = JSON.parse(data);
		if (data.returnCode == "0")
		{
			$('.modal-refresh-data').removeClass('isLoading');
			$('.modal-refresh-data').addClass('isError');
			printError(".modal-refresh-data-error", "Process Completed", "The data was processed successfully", "/scr-admin/resources/core/img/check-mark-new.svg");
		}
		else
		{
			$('.modal-refresh-data').removeClass('isLoading');
			$('.modal-refresh-data').addClass('isError');
			printError(".modal-refresh-data-error", "Ups! An error has occurred", "An unexpected error has occurred", "/scr-admin/resources/core/img/empty.svg");
		}
	  },
	  error: function(e) 
	  {
		$('.modal-refresh-data').removeClass('isLoading');
		$('.modal-refresh-data').addClass('isError');
		printError(".modal-refresh-data-error", "Ups! An error has occurred", "An unexpected error has occurred", "/scr-admin/resources/core/img/empty.svg");
	  }
	});
}

/********** */
/**ERROR    */
/********** */

function printError(id, title, description, img)
{
	if (img != "")
		$(id+" .thelogo").attr("src", img);
	
		$(id+" .thetitle").html(title);
	$(id+" .thedescription").html(description);
}