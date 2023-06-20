'use strict';


$("#choose-event").click(function(event){
	event.preventDefault()
	var id = $('#select-event').val();
	if (id!="Select"){
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "search/api/selectkey",
			data : id,
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.code==="200") {
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

$("#choose-event-history").click(function(event){
	var id = $('#select-event-history').val();
	event.preventDefault()
	if (id!="Select"){
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "search/api/selectkeyHistory",
			data : id,
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.code==="200") {
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
