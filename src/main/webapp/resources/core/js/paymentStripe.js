function fillForm(){
	var re = new RegExp("-", 'g');
	$('#payment-form').empty();
	var $form = $('#payment-form');
	$form.append($('<input type="text" size="20" id="card-stripe" data-stripe="number">'));
	$form.append($('<input type="text" size="2" id="month-stripe" data-stripe="exp_month">'));
	$form.append($('<input type="text" size="4" id="year-stripe" data-stripe="exp_year">'));
	$form.append($('<input type="text" size="4" id="cvv-stripe" data-stripe="cvc">'));
	$form.append($('<input type="text" id="name-stripe" data-stripe="name">'));
	$form.append($('<input type="text" id="email-stripe" data-stripe="emailAddress">'));	
	$("#card-stripe").val($("#cardNumber").val().replace(re, ""));
	$("#month-stripe").val($("#lang-expdate_ph").val().substring(0, 2));
	$("#year-stripe").val("20"+$("#lang-expdate_ph").val().substring(3, 5));
	$("#cvv-stripe").val($("#lang-cvv_ph").val());
	$("#name-stripe").val($("#fname").val()+" "+$("#lname").val());
	$("#email-stripe").val($("#email").val().trim());
}

function getSource(){
	var source = QueryString.source;
	if (source != null && source.length>2) {
		for (var i = 0; i < sources.length; ++i){
			if (sources[i].key.toUpperCase() == source.toUpperCase()){
				return parseInt(sources[i].id);
			}
		}		
	}
	return -1;
}

function ajaxStripe(){
	
	var txDetail = [];
	for (var i = 0; i < donationAll.length; ++i)
		txDetail[i] = donationAll[i];

	var userData = {}
	userData["name"] = $("#lang-fname_ph").val();
	userData["lastname"] = $("#lang-lname_ph").val();
	userData["email"] = $("#lang-email_ph").val().trim();
	
	var orderDescription = $("#funds option:selected").text();
	
	var paymentGwParameters = {}
	paymentGwParameters["token"] = $("#stripeToken").val();	
	paymentGwParameters["orderdescription"] = orderDescription;
	var source = getSource();
	var langId = 1;
	
	$.ajax({
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		url : ajaxPath+"search/api/assistedCCPurchase",
		data : JSON.stringify({txDetail: txDetail, fundId: $('#funds').val(), userData: userData, paymentGwParameters : paymentGwParameters, donation_source_id : source, paramUA: paramUA, langId : langId}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			closeLoader('#bntContinue3');
			if (data.code==="200") {					
				$('#lang-name').text($("#lang-fname_ph").val());
				$('#box3').hide();
				transId = data.msg;
				//$('#box4').show();
				//animate();
				buy_success();
				console.log(data);						
			} else if (data.code==="203") {
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text("Declined transaction: "+data.msg);
			} else if (data.code==="204"){
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text("Error: "+dataErrors[ErrorActualIdiom].user_error_tdc_decline);
			} else if (data.code==="206"){
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text($("#lang-payment-duplicate").text());
			}
			else if (data.code==="400") {
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text("Error: "+data.msg);
				console.log(data);
			}
		},
		error : function(e) {
			console.log("ERROR: ", e);
			//alert("ERROR");
			closeLoader('#bntContinue3');
		},
		done : function(e) {
			//alert("DONE");
			console.log("DONE");
		},
		always : function(e) {
			closeLoader('#bntContinue3');
		},
		beforeSend:function(e) {			
			$("#lang-error-transaction").css("display","none");
		},
	});
}