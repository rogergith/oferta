function fillForm(){
	$("#card-stripe").val($("#lang-creditcard_ph").val());
	$("#month-stripe").val($("#lang-expdate_ph").val().substring(0, 2));
	$("#year-stripe").val($("#lang-expdate_ph").val().substring(3, 7));
	$("#cvv-stripe").val($("#lang-cvv_ph").val());
	$("#zip-stripe").val($("#lang-zip_ph").val());

	$("#name-stripe").val($("#lang-fname_ph").val()+" "+$("#lang-lname_ph").val());
	$("#city-stripe").val($("#lang-city_ph").val());
	$("#state-stripe").val($("#lang-state_ph").val());
	$("#country-stripe").val($("#type-countries option:selected" ).text());
	$("#email-stripe").val($("#lang-email_ph").val().trim());
}

function ajaxStripe(){
	
	var txDetail = [];
	for (var i = 0; i < donationAll.length; ++i)
		txDetail[i] = donationAll[i];

	var userData = {}
	userData["name"] = $("#lang-fname_ph").val();
	userData["lastname"] = $("#lang-lname_ph").val();
	userData["country"] = $("#type-countries select").val();
	userData["state"] = $("#lang-state_ph").val();
	userData["city"] = $("#lang-city_ph").val();
	userData["address"] = $("#lang-address_ph").val();
	userData["postcode"] = $("#lang-zip_ph").val();
	userData["email"] = $("#lang-email_ph").val().trim();
	
	var orderDescription = "Donación en línea";

	if ($( "#newIdiom option:selected" ).text() != "ES"){
		orderDescription = "Online Donation";
	}
	
	var paymentGwParameters = {}
	paymentGwParameters["token"] = $("#stripeToken").val();	
	paymentGwParameters["orderdescription"] = orderDescription;	
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/DonationForm/search/api/creditCardPurchase",
		data : JSON.stringify({txDetail: txDetail, userData: userData, paymentGwParameters : paymentGwParameters}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			$(".loader").css("display","none");
			if (data.code==="200") {					
				$('#lang-name').text($("#lang-fname_ph").val());
				$('#box3').hide();
				$('#box4').show();
				animate();
				console.log(data);						
			} else if (data.code==="203") {
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text("Declined transaction: "+data.msg);
			} else if (data.code==="204"){
				$("#lang-error-transaction").css("display","block");
				$("#lang-error-transaction").text("Error: "+data.msg);
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
			$(".loader").css("display","none");
		},
		done : function(e) {
			//alert("DONE");
			console.log("DONE");
		},
		always : function(e) {
			$(".loader").css("display","none");
		},
		beforeSend:function(e) {
			$(".loader").css("display","block");
			$("#lang-error-transaction").css("display","none");
		},
	});
}