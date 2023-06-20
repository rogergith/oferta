function setDetailData(){
	var txDetail = [];
	for (var i = 0; i < donationAll.length; ++i)
		txDetail[i] = donationAll[i];
	return txDetail;
}

function setUserData(){
	var userData = {}
	userData["name"] = $("#lang-fname_ph").val();
	userData["lastname"] = $("#lang-lname_ph").val();
	userData["country"] = $("#type-countries select").val();
	userData["state"] = $("#lang-state_ph").val();
	userData["city"] = $("#lang-city_ph").val();
	userData["address"] = $("#lang-address_ph").val();
	userData["postcode"] = $("#lang-zip_ph").val();
	userData["email"] = $("#lang-email_ph").val();
	return userData;
}

function setPaymentGwParameters() {

	var orderDescription = "Donación en línea";

	if ($( "#newIdiom option:selected" ).text() != "ES"){
		orderDescription = "Online Donation";
	}

	var paymentGwParameters = {}
	paymentGwParameters["creditcardnumber"] = $("#lang-creditcard_ph").val();
	paymentGwParameters["creditcardexpiration"] = $("#lang-expdate_ph").val().replace("/", "-");
	paymentGwParameters["creditcardcode"] = $("#lang-cvv_ph").val();
	paymentGwParameters["orderdescription"] = orderDescription;		
	return paymentGwParameters;
}

function ajaxAuthorize(){
	var txDetail = setDetailData();
	var userData = setUserData();
	var paymentGwParameters = setPaymentGwParameters();
	
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