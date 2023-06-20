

$(document).ready(function(){
	
	$('.collapseRestrict').collapse();
	$("li.sidenav-item").removeClass("active");
	$(".listRestrictedUsers").addClass("active");

});

$('#restrict-event-form').validate({
	rules: {
		required: true,
		accept: "text/csv"
	},
	submitHandler: restrictEvent,
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
	  var parent = $(element).parent().parent();
	  var sibling = parent.children(".custom-message-error");
	  error.appendTo( sibling );
	}
});

$('#registerUsers').click(function(e){
	e.preventDefault();
	$('#restrict-event-form').submit();
});

function restrictEvent() {
	
	const dataCustomers = new FormData(document.getElementById("restrict-event-form"));;
	
	$.ajax({
		type : "POST",
		contentType : false,
		url : ajaxPath + "search/api/registerRestrictEvent",
		data : dataCustomers,
		dataType : 'html',
		timeout : 0,
		processData: false,
		cache: false,
		beforeSend: function(){
			$("#registerUsers").hide();
			$("#restrict-event-spinner").show();
			$("#massive-errors").text("");
		},
		success : function(data) {
			console.log("SUCCESS: ", data);
			data = JSON.parse(data);
			if (data.code=="0") {
				 	$("#registerUsers").show();
					$("#restrict-event-spinner").hide();
					showToast('success', 'Success!!', 'Customers succesfully registered' );
					showListCustomersErrors(data);
								
			} else if (data.code == "-4"){
				$("#registerUsers").show();
				$("#restrict-event-spinner").hide();
				showListCustomersErrors(data);
				
				
			} else if(data.code == "-22" || data.code == "-2") {
				
				$("#registerUsers").show();
				$("#restrict-event-spinner").hide();
				showToast('error', 'Error!!', data.msg );
			} else {
				
				$("#registerUsers").show();
				$("#restrict-event-spinner").hide();
				showToast('error', 'Error!!', "Unexpected Error" );
			}
		},
		error : function(e) {
			$("#registerUsers").show();
			$("#restrict-event-spinner").hide();
			showToast('error', 'Error!!', e.message );

		}
	});
}