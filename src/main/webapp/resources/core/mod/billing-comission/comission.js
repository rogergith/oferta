var ajaxPath = "/oferta/";
var billing;
var dataSourcesLocal = "";
$(document).ready(function(){

	$.ajax({
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		url : ajaxPath + "search/api/getComissionSettings",
		data : JSON.stringify({}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			if (data.returnCode == 0) {
				billing = configComissionEvent(data);
					
			} else {
				alert(data.returnMessage);
			}
		},
		error : function(e) {
			console.log("ERROR: ", e); 
			alert("ERROR");
		},
		done : function(e) {
			alert("DONE");
			console.log("DONE");
		}
	});	


	$.ajax({
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		url : ajaxPath + "search/api/listAllCountries",
		data : JSON.stringify({}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			if (data.returnCode == 0) 
			{
				var new_select = "";
				var formatedData = data.result.ListCountries.map( r => { 
					return '<option value="'+r.id+'">'+r.name+'</option>';
				});

				$('#country').append(formatedData.join(""));
			}
		}
	});	

	
	$.ajax({
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		url : ajaxPath + "search/api/listPurchaseSource",
		data : JSON.stringify({}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) 
		{
			
			if (data.returnCode == 0) 
			{
				dataSourcesLocal = data.result.sourceCodes;
				printLanguage();
			}
		}
	});	

});


$(document).on("change","#languaje",function(e)
{ 
	printLanguage();
});

function printLanguage(){

	var lang = $("#languaje").val();
	var new_select = "";
	var select = "";

	if (lang==2)
		select = '<option value="">Select a source</option>';
	else
		select = '<option value="">Seleccione un source</option>';
	
	var formatedData = dataSourcesLocal.map( r => { 

		if (!r.active)
			return '';

		if (parseInt(r.langId) == parseInt(lang)) {
			if (r.id === 22) 
				return '<option value="'+r.id+'" disabled>'+r.description+'</option>';
			else
				return '<option value="'+r.id+'">'+r.description+'</option>';
		}	
		else
			return ''
	});


	$('#sourceMarketing').empty();
	$('#sourceMarketing').append(select+formatedData.join(""));

}

function configComissionEvent(data) {
	
	var current_price = parseFloat($("#amount").val());
	var $show_price = $("#comission-event-amount");
	var $show_commission = $("#comission-mount");
	var $show_total = $("#total-amount");
	var eventSettings = "UNKNOWN_OBJECT";
	var $billingComponent = $("#billing-component");
	var currentPaymentType;
	var realTotalAmount;
	if (data.result.restrictEvent)
	var startDate ="";
	var subscription = false;
	
	if (data.result.restrictEvent == null) {
		$(".start-date-group").hide();
	}
	else {
		startDate = data.result.restrictEvent.startDate; //1662673895429;
	}
	
	if (data.result.subscription === true) {
		$("#splitPayment-method").hide();
		$("#save-container").hide();
		subscription = true;
	}
	else {
		$("#splitPayment-method").show();
	}
	
	function init() {	
		if(data.result.hasOwnProperty("eventCommissionSettings")){
			eventSettings = data.result.eventCommissionSettings;
		}
		
		currentPaymentType = $("#payment-method").val();
		
		$billingComponent.hide();
		
		if(currentPaymentType != "free") {
			if(eventSettings == "UNKNOWN_OBJECT" || eventSettings.userCommissionValue == 0) {
				
				showBillingComponent(current_price, current_price, " - ");
				
			} else {
				if(eventSettings.userCommissionType == 1) {
					setAbsoluteComission();
				} else {
					setPercentComsission();
				}
			}
		} else {
			return;
		}
		
	}	

	function setAbsoluteComission(){
		var absoluteComision = parseFloat(eventSettings.userCommissionValue);
		realTotalAmount = absoluteComision + current_price;		
		showBillingComponent(current_price, realTotalAmount, absoluteComision);
		
	}
	
	function setPercentComsission() {
		
		var realCommision;
		var percentage = parseFloat(eventSettings.userCommissionValue);
		var minMount = parseFloat(eventSettings.minUserCommission);
		var maxMount = parseFloat(eventSettings.maxUserCommission);
		var commision = current_price * percentage / 100;
		
		if(commision > maxMount) {
			realCommision = maxMount;
		} else if(commision < minMount) {
			realCommision = minMount;
		} else {
			realCommision = commision;
		}
		
		realTotalAmount = current_price + realCommision;
		
		showBillingComponent(current_price, realTotalAmount, realCommision);
	}
	
	function showBillingComponent(price, total, com){
		
		var the_commission = com == " - " ? com : "$ " + formatMoney(com);
		
		$show_price.text("$ " + formatMoney(price));
		$show_commission.text(the_commission);
		$show_total = $("#total-amount").text("$ " + formatMoney(total));
		$billingComponent.show();
	}
	
	init();
	
	return {
		mount: current_price,
		realMount: realTotalAmount,
		component: $billingComponent,
		settings: eventSettings,
		startDate: startDate,
		init: init,
		subscription: subscription
	}	
	
}