'use strict';

$(function() {
	//Login Event
	var LoginEvent = {
		target   : "#login",
		event    : "click",
		action   : "search/api/authenticateAdmin",
		data     : null,
		prepare  : function() {
			this.data = { 
				user: $("#username").val(), 
				passw: $("#password").val(),
				option: optionSelected 
			}		
		},
		callback : function(data) {
			$.logger.log("LoginEvent.callback: ", data);
			location.reload();
		}
	}	
	$.admin.registerEvent(LoginEvent);
		
	//Password Event
	var KeyEnterEvent = {
		target   : "#password",
		callback : function(data) { $('#login').click(); }
	}		
	$.admin.registerDefaultEnterKeyEvent(KeyEnterEvent);
	
});

$("#choose-event-form").validate({debug: true, 
	submitHandler: handleChoosedEvent, 
	rules: {
		selectEvent: 'required'
	},
	messages: {
		selectEvent: 'You must choose an event'
	},
	errorClass: 'has-error',
	validClass: 'has-success',
	highlight: function(element, errorClass, validClass) {
	  $(element)
		.closest('.form-group')
		.addClass(errorClass).removeClass(validClass);
	},
	unhighlight: function(element, errorClass, validClass) {
	  $(element)
		.closest('.form-group')
		.addClass(validClass).removeClass(errorClass);
	},
	errorPlacement: function(error, element) {
	 error.appendTo( element.parent(".input-group") );
   }
}); 

function handleChoosedEvent(){
	console.log("You choose an event!")
}
