var ajaxPath = "/oferta/";
var nameReport = "";
var fee = "";
var checkStartDate = false;
var startDateFull = "";
var applyCoupon = false;

$("#generateReport").click(function() {
  var filter = getFilter();
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/createReport",
    data: JSON.stringify(filter),
    dataType: "json",
    timeout: 100000,
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "200") {
        generateReport(data.result);
        console.log(data);
      } else if (data.code === "204") {
        alert("Error");
      } else if (data.code === "400") {
        alert("Error");
      }
    },
    error: function(e) {
      console.log("ERROR: ", e);
      alert("ERROR");
    },
    done: function(e) {
      alert("DONE");
      console.log("DONE");
    }
  });
});

$("#generateGlobalReport").click(function() {
  var filter = getFilter();
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/createGlobalReport",
    data: JSON.stringify(filter),
    dataType: "json",
    timeout: 100000,
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "200") {
        generateReport(data.result);
        console.log(data);
      } else if (data.code === "204") {
        alert("Error");
      } else if (data.code === "400") {
        alert("Error");
      }
    },
    error: function(e) {
      console.log("ERROR: ", e);
      alert("ERROR");
    },
    done: function(e) {
      alert("DONE");
      console.log("DONE");
    }
  });
});


$("#verifyUser").click(function() 
{
  FindUserPreLoadController();
});

function FindUserPreLoadController()
{

  FindUserPreLoadRequestNormal();
  let email = $("#email").val().trim();
  
  if (email.length < 1)
  {
    FindUserPreLoadRequestError("Please, complete the email field before making the search");
    return false;
  }

  if (emailVerify(email))
  {
    FindUserPreLoadRequestError("Please, enter a correct email");
    return false;
  }

  FindUserPreLoadRequest(email);
  return true;
}

function FindUserPreLoadRequest (email)
{
  FindUserPreLoadRequestLoading();
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/verifyUserForAddAttendee",
    data: email,
    dataType: "json",
    timeout: 100000,
    success: function(data)
    {

      if (data.code === "200" || data.code === "401") 
      {
          FindUserPreLoadRequestResolve(data.result);
      }
      else if (data.code === "400") 
      {
        FindUserPreLoadRequestError("");
      }
    },
    error: function(e) 
    {
      FindUserPreLoadRequestError("");
    },
  });
}

function FindUserPreLoadRequestLoading()
{
  $('input, select, checkbox, a, button').attr('disabled', 'disabled');
  $(".loading-find-user").addClass('active');
  $(".action-find-user").removeClass('active');
}

function FindUserPreLoadRequestResolve(data)
{
  FindUserPreLoadRequestNormal();
  $("#fname").val(data.user.firstName);
  $("#lname").val(data.user.lastName);
  $("#phone").val(data.user.phoneNumber);
  $("#country option[value='"+data.user.countryId+"']").attr("selected", true);
  controllerListCard(data);
  if (data.preferredLang != null) {
	  $('#languaje').val(data.preferredLang.id);
	  $('#language option[value="'+data.preferredLang.id+'"]').attr("selected", "selected");
  }
  if (data.affiliate != null) {
	  $("#sourceMarketing option[value='22']").attr("disabled", false);
	  $("#sourceMarketing option[value='22']").attr("selected", true);
	  $('#sourceMarketing').attr('disabled', 'disabled');
	  $("#text-afilliate").show();
	  
	  document.getElementById('text-afilliate').innerHTML = "AFILIADO: " +  data.affiliate.emailUser;
  } else {
	  $("#text-afilliate").hide();
  }
  var isSplitPayment = data.hasSplitPayment;
  if (isSplitPayment) {
	  $("#payment-method").val("splitPayment");
	  $(".payment-method-container").hide();
	  var paidAmount = data.acumAmount.toFixed(2);
	  var paidAmountCommission = data.acumAmountWithCommission;
	  $("#paidAmount").val(paidAmountCommission.toFixed(2));
	  showSplitPayment(paidAmount,paidAmountCommission);
	  var sourceId = data.txSourceCodeId;
	  $("#sourceMarketing").val(sourceId);	 
	  $("#text-fee").hide();
	  $("#apply-coupon-container").hide();
	  var defaultDate = data.defaultDate; //1663175209000
	  if (defaultDate != null) {
		  $("#modify-start-date").attr('checked', true);
		  checkStartDate = true;
	      $(".start-date-input").show();
		  var formatDateDefault = new Date(defaultDate).toLocaleDateString('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'});
	      
	      var formatDate = new Date(billing.startDate).toLocaleDateString('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'});
	      var date = new Date(formatDate);
	      var devolucion = new Date();
	      devolucion.setDate(date.getDate() + 1);
	      var formatStartDate = new Date(devolucion).toLocaleDateString('en-CA');

	      $('#start-date').datepicker({
	    	  format: "mm/dd/yyyy",
	    	  startDate: new Date(formatStartDate)
	      }).datepicker('setDate', formatDateDefault);
	  }
  }  
}

function controllerListCard(data)
{
  try 
  {
    $("#sectionlistCredicards").remove();
    if (Array.isArray(data.cards))
    {
      var array = data.cards;
      var myData = setAllCards(array);
      var myId   = getIdCards(myData);
      var tag   = '<label for="payment-method" class="col-sm-4  control-label">Select Credit Card:</label>'+"<div id='sectionlistCredicards' class='col-sm-6 col-md-4'><select class='custom-select' id='listCredicards'>"+options_(myData, myId)+"</select></div>";
      $(".list-credicard").html(tag);
      controllerListCreditCards($("#listCredicards").val());
      $('#listCredicards').on('change', function() {
        controllerListCreditCards(this.value);
      });

    }
  }
  catch 
  {
	  
  }
}

function getIdCards(myData)
{
	try
	{
		data = myData.filter(x => x.default == true);
		return data[0].id;
	} 
	catch (error)
	{
		return 0;
	}
}

function controllerListCreditCards(value)
{
	if (value == 'new')
	{
		$(".card-1").removeClass( "disabled_");
		$(".card-2").removeClass( "disabled_");
    $(".card-3").removeClass( "disabled_");
    $("#save-container").removeClass("disabled_");
	}
	else
	{
		$(".card-1").addClass( "disabled_");
		$(".card-2").addClass( "disabled_");
    $(".card-3").addClass( "disabled_");
    $("#save-container").addClass("disabled_");
	}
}

function setAllCards(data)
{
  try
  {
	//getImgCard()
	var d = data.map((e, i) => { 
	  return { id: e.cardId, name : e.brand+" **** **** **** "+e.cardLast4, default: e.default };
	});
	d.unshift({id:'new', name: "Add Credit Card", default:false});

	return d;
  }
  catch (error)
  {
	return [];
  }
}

function optionSelect_(value,name,selected)
{
	return "<option "+selected+" value='"+value+"' >"+name+"</option>";
}

function options_(options, id)
{
	var length = options.length;
	var tags   = "";
	var select = "";
	if ( length > 0 ) 
	{
		for ( var i = 0; i < length; i++ )
		{
			select = "";
			if (options[i].id == id)
			{
				select = " selected ";
			}

			if (options[i].id == "")
			{
				select = select + " selected disabled ";
			}  
			tags   =  tags + optionSelect_(options[i].id,options[i].name, select);   
		}
	}
	return tags;
}

function FindUserPreLoadRequestNormal()
{
  $(".action-find-user").addClass('active');
  $(".loading-find-user").removeClass('active');
  $('input, select, checkbox, a, button').removeAttr('disabled');
  $("#find-email").html("");
}

$( "#email"  ).keypress(function() {
  var email  =  $(this).val().trim();
  FindUserPreLoadRequestError("");
  $("#email").val(email);
});

function FindUserPreLoadRequestError(msg)
{
  $("#fname").val("");
  $("#lname").val("");
  $("#phone").val("");
  FindUserPreLoadRequestNormal();
  $("#sectionlistCredicards").remove();
  $(".card-1").removeClass( "disabled_");
  $(".card-2").removeClass( "disabled_");
  $(".card-3").removeClass( "disabled_");
  $("#save-container").removeClass("disabled_");
  $(".custom-message-error").empty();
  $(".form-group").removeClass("has-error");
  $(".find-email-group").addClass('has-error');
  $("#sectionlistCredicards").remove();
  $("#find-email").html(msg);
}

function emailVerify(object)
{
  let valid = /\S+@\S+\.\S+/;

  if(valid.test(object))
    return false;
  else 
    return true;
}

$("#generateReportFinances").click(function() {
  var filter = getFilterFinances();
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/reportFinances",
    data: JSON.stringify(filter),
    dataType: "json",
    timeout: 100000,
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "200") {
        generateReportFinances(data.result);
        console.log(data);
      } else if (data.code === "204") {
        alert("Error");
      } else if (data.code === "400") {
        alert("Error");
      }
    },
    error: function(e) {
      console.log("ERROR: ", e);
      alert("ERROR");
    },
    done: function(e) {
      alert("DONE");
      console.log("DONE");
    }
  });
});

function isEmail(email) 
{
  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return regex.test(email);
}

$("#externalPurchase").click(function() {
  event.preventDefault();
  $("#register-paypal-record").submit();
});

/*$("#payment-method").val() == "stripe" 
      && ($("#listCredicards").val() == undefined
      || $("#listCredicards").val() == "new"*/

/*
function validSource()
{
  var val = $("#sourceMarketing").val();
  if (val == "")
  {
    $(".form-group-source").addClass("has-error");
    $(".form-group-source .custom-message-error").html("El campo es requerido.");
    return false;
  }
  else
  {
    $(".form-group-source .custom-message-error").html("");
    $(".form-group-source").removeClass("has-error");
    return true;
  }    
}
*/

$("#callPurchase").click(function(event) 
{
  const validator = $("#register-stripe-record").data("validator");
  const source = $("#sourceMarketing").length;
  let isValid = false;
  	if (source > 0) {
		isValid = 
    		validator.element("#fname") &&
    		validator.element("#lname") &&
    		validator.element("#email") &&
    		validator.element("#sourceMarketing") &&
    		validator.element("#phone");
  	} else {
		isValid = 
    		validator.element("#fname") &&
    		validator.element("#lname") &&
    		validator.element("#email") &&    
    		validator.element("#phone");
	}
  
  /*	
  if (!validSource())
    return false;
  */  
  	
  const comment = $("#comments").val();

  if (isValid) { 
    if ($("#payment-method").val() == "free") {
    	event.preventDefault();
    	if (comment != '' && comment != ' ') {
    		$("#register-stripe-record").submit();
    	} else {
    		showErrorModal(
    		"The comment is required."
    		);
    	}
    }
    else if ($("#payment-method").val() == "stripe") {
        if ($("#listCredicards").val() != undefined 
        && $("#listCredicards").val()  != "" 
        &&  $("#listCredicards").val() != "new"
        && $("#stripeToken").val() != "") {
          registerCallPurchase();
        }
        else {
          event.preventDefault();
          $("#register-stripe-record").submit();
        }
    }
    else if ($("#payment-method").val() == "splitPayment") {
        if ($("#listCredicards").val() != undefined 
        && $("#listCredicards").val()  != "" 
        &&  $("#listCredicards").val() != "new"
        && $("#stripeToken").val() != "") {
          registerCallPurchaseSplitPayment();
        }
        else {
          event.preventDefault();
          $("#register-stripe-record").submit();
        }
    }
    else {
      event.preventDefault();
      $("#register-stripe-record").submit();
    }
  }
});

function clearPurchaseForm() 
{
  $("#fname").val("");
  $("#lname").val("");
  $("#email").val("");
  $("#phone").val("");
  $("#list-credicard").empty();
  $(".coupon-input").hide();
  $(".coupon-input .custom-message-error").text("");
  $("#cardNumber").val("");
  $("#lang-expdate_ph").val("");
  $("#lang-cvv_ph").val("");
  $("#external-payment-description").val("");
  $("#comments").val("");
  if ($("#apply-coupon").is(":checked")) $("#apply-coupon").click();

  if ($("#register-stripe-record").data("validator"))
    $("#register-stripe-record")
      .data("validator")
      .destroy();
  $("#text-fee").hide();
  $("#apply-coupon-container").hide();
  var clear=true;
  currentPaymentType(clear);
}

function showToast(status, head, msg) {
  var title = head,
    message = msg,
    type = status,
    options = {};

  toastr[type](message, title, options);
}
// This function implement the ajax call to register the paypal purchase
function registerExternalPurchase() {
  var dataP = getDataExternalPurchase();
  console.log(dataP);
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/externalPurchase",
    data: JSON.stringify(dataP),
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $("#callPurchase").hide();
      $("#submit-spinner").show();
    },
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "0") {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        showSuccessModal(data.txId);
      } else {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        showErrorModal(data.msg);
      }
    },
    error: function(e) {
      $("#callPurchase").show();
      $("#submit-spinner").hide();
      showErrorModal(
        "An error has ocurred trying to connect to the server. Please contact support."
      );
    }
  });
}

function registerCallPurchase() {
	if ($("#payment-method").val() == "splitPayment") {
		registerCallPurchaseSplitPayment();
	}
	else {
		var dataP = getDataCallCenterPurchase();
		  //TODO
		  $.ajax({
		    type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: ajaxPath + "search/api/assistedCCPurchase",
		    data: JSON.stringify(dataP),
		    dataType: "json",
		    timeout: 100000,
		    beforeSend: function() {
		      $("#callPurchase").hide();
		      $("#submit-spinner").show();
		    },
		    success: function(data) {
		      console.log("SUCCESS: ", data);
		      if (data.code === "200") {
		        $("#callPurchase").show();
		        $("#submit-spinner").hide();
		        showSuccessModal(data.txId);
		      } else {
		        $("#callPurchase").show();
		        $("#submit-spinner").hide();
		        showErrorModal(data.msg);
		      }
		    },
		    error: function(e) {
		      $("#callPurchase").show();
		      $("#submit-spinner").hide();
		      showErrorModal(
		        "An error has ocurred trying to connect to the server. Please contact support."
		      );
		    }
		  });
	}
  
}

function registerCallPurchaseSplitPayment() {
	var dataP = getDataCallCenterPurchaseSplitPayment();
  //TODO
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/assistedCCPurchaseSplitPayment",
    data: JSON.stringify(dataP),
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $("#callPurchase").hide();
      $("#submit-spinner").show();
    },
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "200") {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        showSuccessModal(data.txId);
      } else {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        showErrorModal(data.msg);
      }
    },
    error: function(e) {
      $("#callPurchase").show();
      $("#submit-spinner").hide();
      showErrorModal(
        "An error has ocurred trying to connect to the server. Please contact support."
      );
    }
  });
}

function registerFreePurchase() {
  var dataP = getDataFree();

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/externalPurchase",
    data: JSON.stringify(dataP),
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $("#step2-action").hide();
      $("#submit-spinner-2").show();
      $("#callPurchase").hide();
      $("#submit-spinner").show();
      $('#btnFree').hide();
    },
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "0") {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showSuccessModal(data.txId);
      } else {
        $("#btnFree").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showErrorModal(data.msg);
      }
    },
    error: function(e) {
      $("#btnFree").show();
      $("#submit-spinner").hide();
      $("#step2-action").show();
      $("#submit-spinner-2").hide();
      showErrorModal(
        "An error has ocurred trying to connect to the server. Please contact support."
      );
    }
  });
}

function registerFreePurchase2() {
  var dataP = getDataFree2();

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/externalPurchase",
    data: JSON.stringify(dataP),
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $("#step2-action").hide();
      $("#submit-spinner-2").show();
      $("#callPurchase").hide();
      $("#submit-spinner").show();
    },
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "0") {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showSuccessModal(data.txId);
      } else {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showErrorModal(data.msg);
      }
    },
    error: function(e) {
      $("#callPurchase").show();
      $("#submit-spinner").hide();
      $("#step2-action").show();
      $("#submit-spinner-2").hide();
      showErrorModal(
        "An error has ocurred trying to connect to the server. Please contact support."
      );
    }
  });
}


$("#btnFree").click(function(e) {
  e.preventDefault();
  //registerFreePurchase();
  
  var dataP = getDataFreePurchase(); 
  var txDetail = [
	  	{
	  		typeDonation: $("#funds").val(), 
	  		amount: $("#amount").val()
	  	}
  ];
  
  var userData = {}
  userData["name"] = $("#fname").val();
  userData["lastname"] = $("#lname").val();
  userData["email"] = $("#email").val().trim();
  userData["phone"] = $("#phone").val();
  userData["langid"] = $('#languaje').val();
	
  var data = JSON.stringify({
     txDetail: txDetail,
 	 fundId: $("#funds").val(), 
	 userData: userData, 
	 source : $("#source").val(),  
	 campaign : '', 
	 medium: $("#medium").val(),   
	 coupon: $("#coupon-code").val().trim() || "",
	 comments: $("#comments").val() || "",
	 country: $("#country").val(),
	 source_code: $("#sourceMarketing").val(),
	 checkStartDate: checkStartDate,
	 startDate: formatStartDateFull()
  });
  
  console.log(data);
  
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/freePurchase",
    data: data,
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $("#step2-action").hide();
      $("#submit-spinner-2").show();
      $("#callPurchase").hide();
      $("#submit-spinner").show();
      $('#btnFree').hide();
    },
    success: function(data) {
      console.log("SUCCESS: ", data);
      if (data.code === "200") {
        $("#callPurchase").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showSuccessModal(data.txId);
      } else {
        $("#btnFree").show();
        $("#submit-spinner").hide();
        $("#step2-action").show();
        $("#submit-spinner-2").hide();
        showErrorModal(data.msg);
      }
    },
    error: function(e) {
      $("#btnFree").show();
      $("#submit-spinner").hide();
      $("#step2-action").show();
      $("#submit-spinner-2").hide();
      showErrorModal(
        "An error has ocurred trying to connect to the server. Please contact support."
      );
    }
  });
  
});

function showSuccessModal(tx) {
  $("#success-tx-id").val(tx);
  $("#successModalAlert").modal("show");
  $("#text-fee").hide();
  $("#apply-coupon-container").hide();
}

function showErrorModal(error) {
  $("#error-modal-msg").text(error);
  $("#dangerModalAlert").modal("show");
  $("#text-fee").hide();
  $("#apply-coupon-container").hide();
}

function closeSuccessModal() {
	$("#text-fee").hide();
	$("#apply-coupon-container").hide();
	const userId = $("#success-modal-close").data("user-id");	
	const url = userId ? `/scr-admin/userDetail-${userId}` : "/scr-admin/callCenter"
  	setTimeout(function() {
    	window.location.replace(url);
    	$("#text-fee").hide();
    	$("#apply-coupon-container").hide();
  	}, 1000);
  	clearPurchaseForm();
}

function closeErrorModal() {}

function getDataFree() {
  var data = {};
  data["name"] = $("#fname").val();
  data["lastname"] = $("#lname").val();
  data["email"] = $("#email").val().trim();
  data["phone"] = $("#phone").val();
  data["fund"] = $("#funds").val();
  data["amount"] = $("#amount").val();
  data["medium"] = $("#medium").val();
  data["source"] = $("#source").val();
  data["paymentType"] = "2";
  data["country"] = $("#country").val();
  data["source_code"] = $("#sourceMarketing").val();
  data["concept"] = $("#external-payment-description").val() || "";
  data["coupon"] = $("#coupon-code").val().trim() || "";
  data["comments"] = $("#comments").val() || "";
  data["langid"] = $('#lang').val();
  data["checkStartDate"] = checkStartDate;
  data["startDate"]	= formatStartDateFull();
  
  return data;
}

function getDataFreePurchase() {
	  var txDetail = [];
	  
	  var data = {};
	  txDetail[0] = $("#funds").val();
	  txDetail[1] = $("#amount").val();
	  
	  var userData = {}
	  userData["name"] = $("#fname").val();
	  userData["lastname"] = $("#lname").val();
	  userData["email"] = $("#email").val().trim();

	  data["checkStartDate"]   = checkStartDate;
	  data["startDate"]	   	   = formatStartDateFull();
	  
	  data = JSON.stringify({txDetail: txDetail, 
		  					 fundId: $("#funds").val(), 
		  					 userData: userData, 
		  					 source : $("#source").val(),  
		  					 campaign : '', 
		  					 medium: $("#medium").val(),   
		  					 coupon: $("#coupon-code").val().trim() || "",
		  					 comments: $("#comments").val() || "",
		  					 country: $("#country").val(),
		  					 source_code: $("#sourceMarketing").val(),
		  					 checkStartDate: checkStartDate,
		  					 startDate: formatStartDateFull()
		  					 });
	  return data;
	}

function getDataFree2() {
  var data = {};
  data["name"] = $("#fname").val();
  data["lastname"] = $("#lname").val();
  data["email"] = $("#email").val().trim();
  data["phone"] = $("#phone").val();
  data["fund"] = $("#funds").val();
  data["amount"] = "0";
  data["medium"] = $("#medium").val();
  data["source"] = $("#source").val();
  data["paymentType"] = "2";
  data["country"] = $("#country").val();
  data["source_code"] = $("#sourceMarketing").val();
  data["concept"] = $("#external-payment-description").val() || "";
  data["coupon"] = "";
  data["comments"] = $("#comments").val() || "";
  data["langid"] = $('#languaje').val();
  data["checkStartDate"]   = checkStartDate;
  data["startDate"]	   	   = formatStartDateFull();

  return data;
}

function getFilter() {
  var filter = {};
  if ($("#fromDate").val().length == 10 && $("#toDate").val().length == 10) {
    filter["contribution_date_min"] = convertFromDate();
    filter["contribution_date_max"] = convertToDate();
  } else {
    $("#fromDate").val("");
    $("#toDate").val("");
  }
  if ($("#operatorEmail").val().trim().length > 0)
    filter["operator_email"] = $("#operatorEmail").val().trim();
  if ($("#fname").val().length > 0)
    filter["person_firstname"] = $("#fname").val();
  if ($("#lname").val().length > 0)
    filter["person_lastname"] = $("#lname").val();
  if ($("#email").val().trim().length > 0) filter["person_email"] = $("#email").val().trim();
  if ($("#paymentGW").val() != "" && !isNaN($("#paymentGW").val()))
    filter["payment_gw_id"] = parseInt($("#paymentGW").val());
  if ($("#txId").val().length > 0)
    filter["contribution_onl_transactionid"] = $("#txId").val();
  if ($("#infoPayment").val().length > 0)
    filter["contribution_paymentinfo"] = "XXXX" + $("#infoPayment").val();
  if ($("#funds").val() != "" && !isNaN($("#funds").val()))
    filter["contribution_fund_id"] = parseInt($("#funds").val());
  if ($("#containerId").val().length > 0)
    filter["contribution_containerid"] = $("#containerId").val();
  if ($("#authorizeId").val().length > 0)
    filter["person_authorizenetprofileid"] = $("#authorizeId").val();
  if ($("#approvalNumber").val().length > 0)
    filter["contribution_onl_approvalnumber"] = $("#approvalNumber").val();
  if ($("#form").val().length > 0)
    filter["contribution_form"] = $("#form").val();
  if ($("#txInternalId").val().length > 0)
    filter["transactioninternalid"] = $("#txInternalId").val();
  if ($("#applicationName").val() != "" && !isNaN($("#applicationName").val()))
    filter["application_id"] = parseInt($("#applicationName").val());
  if ($("#txStatus").val() != "" && !isNaN($("#txStatus").val()))
    filter["transaction_status_id"] = parseInt($("#txStatus").val());
  if ($("#paymenttype").val() != "" && !isNaN($("#paymenttype").val()))
    filter["payment_type_id"] = parseInt($("#paymenttype").val());
  if ($("#source").val() != "" && !isNaN($("#source").val()))
    filter["donation_source_id"] = parseInt($("#source").val());
  if ($("#campaing").val() != "" && !isNaN($("#campaing").val()))
    filter["donation_campaing_id"] = parseInt($("#campaing").val());
  if ($("#settlement").val() != "" && !isNaN($("#settlement").val())) {
    if (parseInt($("#settlement").val()) > 0) {
      filter["is_settled"] = true;
    } else {
      filter["is_settled"] = false;
    }
  }
  if ($("#affiliateEmail").val().trim().length > 0)
	  filter["affiliate_email"] = $("#affiliateEmail").val().trim();
  if ($("#affiliateId").val().length > 0)
	  filter["affiliate_id"] = parseInt($("#affiliateId").val());
  if (
    !isNaN($("#amount").val()) &&
    $("#amount").val().length > 0 &&
    parseInt($("#amount").val()) > 0
  )
    filter["contribution_amount"] = parseInt($("#amount").val());
  else {
    if (
      $("#amountMin").val().length > 0 &&
      $("#amountMax").val().length > 0 &&
      !isNaN($("#amountMin").val()) &&
      !isNaN($("#amountMax").val())
    ) {
      filter["contribution_amount_min"] = parseInt($("#amountMin").val());
      filter["contribution_amount_max"] = parseInt($("#amountMax").val());
    } else {
      $("#amountMin").val("");
      $("#amountMax").val("");
    }
  }
  return filter;
}

function getFilterFinances() {
  var filter = {};
  if ($("#fromDate").val().length == 10 && $("#toDate").val().length == 10) {
    filter["contribution_date_min"] = convertFromDate();
    filter["contribution_date_max"] = convertToDate();
  } else {
    $("#fromDate").val("");
    $("#toDate").val("");
  }

  return filter;
}

function generateReportFinances(dataResult) {
  nameReport = "";
  $(".card-body").html(
    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
  );

  var stringReport = "";
  if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
    for (var i = 0; i < dataResult.length; ++i) {
      stringReport += "<tr>";
      stringReport += "<td>" + dataResult[i].person_firstname + "</td>";
      stringReport += "<td>" + dataResult[i].person_lastname + "</td>";
      stringReport += "<td>" + dataResult[i].person_addressline1 + "</td>";
      stringReport += "<td>" + dataResult[i].person_addresscity + "</td>";
      stringReport += "<td>" + dataResult[i].person_addressstate + "</td>";
      stringReport += "<td>" + dataResult[i].person_addresszip + "</td>";
      stringReport += "<td>" + dataResult[i].person_country + "</td>";
      stringReport += "<td>" + dataResult[i].person_homephone + "</td>";
      stringReport +=
        "<td>" + convertDateEnglish(dataResult[i].contribution_date) + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_amount + "</td>";
      stringReport +=
        "<td>" +
        dataResult[i].contribution_onl_transactionid +
        "-" +
        dataResult[i].contribution_containerid +
        "</td>";
      stringReport += "<td>" + dataResult[i].contribution_comments + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_containerid + "</td>";
      stringReport += "<td>" + dataResult[i].person_email + "</td>";
      stringReport +=
        "<td>" + dataResult[i].person_authorizenetprofileid + "</td>";
      stringReport +=
        "<td>" +
        /*dataResult[i].contribution_onl_approvalnumber*/ dataResult[i]
          .contribution_onl_transactionid +
        "</td>";
      stringReport += "<td>" + dataResult[i].contribution_form + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_status + "</td>";
 	  stringReport += "<td>" + dataResult[i].ip_address + "</td>";
 	  stringReport += "<td>" + dataResult[i].user_agent + "</td>";
 	  stringReport += "<td>" + dataResult[i].sourceCode + "</td>";
      stringReport += "</tr>";
    }
  }
  var stringHead =
    "<thead><tr>" +
    "<th>Person_FirstName</th>" +
    "<th>Person_LastName</th>" +
    "<th>Person_AddressLine1</th>" +
    "<th>Person_AddressCity</th>" +
    "<th>Person_AddressState</th>" +
    "<th>Person_AddressZIP</th>" +
    "<th>Person_Country</th>" +
    "<th>Person_HomePhone</th>" +
    "<th>Contribution_Date</th>" +
    "<th>Contribution_Amount</th>" +
    "<th>Contribution_ONL_TransactionID</th>" +
    "<th>Contribution_Comments</th>" +
    "<th>Contribution_ContainerID</th>" +
    "<th>Person_eMail</th>" +
    "<th>Person_AuthorizeNetProfileID</th>" +
    "<th>Contribution_ONL_ApprovalNumber</th>" +
    "<th>Contribution_Form</th>" +
    "<th>Contribution_Status</th>" +
    "<th>Ip_Address</th>" +
 	"<th>User_Agent</th>" +
 	"<th>Source_Code</th>" +
	 "</tr></thead>" +
    "<tbody id='detailReport'></tbody>";
  $("#demo-datatables-2").html(stringHead);
  $("#detailReport").html(stringReport);
  datatablesRenderFinances();
}

function generateReport(dataResult) {
  $(".card-body").html(
    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
  );

  var stringReport = "";
  if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
    for (var i = 0; i < dataResult.length; ++i) {
      stringReport += "<tr>";
      stringReport += "<td>" + dataResult[i].dateFormat + "</td>";
      stringReport += "<td>" + dataResult[i].timeFormat + "</td>";
      stringReport += "<td>" + dataResult[i].person_firstname + "</td>";
      stringReport += "<td>" + dataResult[i].person_lastname + "</td>";
      stringReport += "<td>" + dataResult[i].person_country + "</td>";
      stringReport += "<td>" + dataResult[i].person_addressstate + "</td>";
      stringReport += "<td>" + dataResult[i].person_addresscity + "</td>";
      stringReport += "<td>" + dataResult[i].person_addressline1 + "</td>";
      stringReport += "<td>" + dataResult[i].person_addresszip + "</td>";
      stringReport += "<td>" + dataResult[i].person_email + "</td>";
      stringReport += "<td>" + dataResult[i].person_phone + "</td>";
      stringReport += "<td>" + dataResult[i].payment_gw + "</td>";
      stringReport +=
        "<td>" + dataResult[i].contribution_onl_transactionid + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_paymentinfo + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_fund + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_containerid + "</td>";
      stringReport +=
        "<td>" + dataResult[i].person_authorizenetprofileid + "</td>";
      stringReport +=
        "<td>" + dataResult[i].contribution_onl_approvalnumber + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_form + "</td>";
      stringReport += "<td>" + dataResult[i].transactioninternalid + "</td>";
      stringReport += "<td>" + dataResult[i].application_name + "</td>";
      stringReport += "<td>" + dataResult[i].transaction_status + "</td>";
      stringReport += "<td>" + dataResult[i].payment_type + "</td>";
      stringReport += "<td>" + dataResult[i].donation_source + "</td>";
      stringReport += "<td>" + dataResult[i].transaction_medium + "</td>";
      stringReport += "<td>" + dataResult[i].donation_campaing + "</td>";
      stringReport += "<td>" + dataResult[i].transaction_term + "</td>";
      stringReport += "<td>" + dataResult[i].transaction_content + "</td>";
      if (dataResult[i].is_settled) stringReport += "<td>Yes</td>";
      else stringReport += "<td>No</td>";
      stringReport += "<td>" + dataResult[i].operatorEmail + "</td>";
      stringReport += "<td>" + dataResult[i].comments + "</td>";
      stringReport += "<td>" + dataResult[i].affiliateId + "</td>";
      stringReport += "<td>" + dataResult[i].affiliateEmail + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_amount + "</td>";
      stringReport += "<td>" + dataResult[i].contribution_discount + "</td>";
 	  stringReport += "<td>" + dataResult[i].ip_address + "</td>";
 	  stringReport += "<td>" + dataResult[i].user_agent + "</td>";
 	  stringReport += "<td>" + dataResult[i].sourceCode + "</td>";
      stringReport += "</tr>";
    }
  }

  var stringHead =
    "<thead><tr><th>Date</th>" +
    "<th>Time</th>" +
    "<th>Name</th>" +
    "<th>Last Name</th>" +
    "<th>Country</th>" +
    "<th>State</th>" +
    "<th>City</th>" +
    "<th>Address</th>" +
    "<th>Zip</th>" +
    "<th>Email</th>" +
    "<th>Phone</th>" +
    "<th>Payment Gateway</th>" +
    "<th>TransactionId</th>" +
    "<th>Last 4 Numbers</th>" +
    "<th>Event</th>" +
    "<th>ContainerId</th>" +
    "<th>AuthorizeProfileId</th>" +
    "<th>Approval Number</th>" +
    "<th>Form</th>" +
    "<th>InternalId</th>" +
    "<th>Application Name</th>" +
    "<th>TxStatus</th>" +
    "<th>Payment Type</th>" +
    "<th>Source</th>" +
    "<th>Medium</th>" +
    "<th>Campaing</th>" +
    "<th>Term</th>" +
    "<th>Content</th>" +
    "<th>Settlement</th>" +
    "<th>OperatorEmail</th>" +
    "<th>OperatorComments</th>" +
    "<th>Affiliate</th>" +
    "<th>AffiliateEmail</th>" +
    "<th>Amount</th>" +
    "<th>Discount</th>"+
    "<th>Ip_Address</th>" +
 	"<th>User_Agent</th>" +
 	"<th>Source_Code</th>" +
	"</tr></thead>" +
    "<tbody id='detailReport'></tbody>";
  $("#demo-datatables-2").html(stringHead);
  $("#detailReport").html(stringReport);
  datatablesRender();
}

function datatablesRender() {
  var DataTable = $.fn.dataTable;
  $.extend(true, DataTable.Buttons.defaults, {
    dom: {
      button: {
        className: "btn btn-outline-primary btn-sm"
      }
    }
  });

  var $datatablesButtons = $("#demo-datatables-2").DataTable({
    buttons: [
      {
        extend: "print",
        text: "Print",
        autoPrint: false,
        exportOptions: {
          columns: ":visible"
        },
        customize: function(win) {
          $(win.document.body)
            .find("table")
            .addClass("display")
            .css("font-size", "9px");
          $(win.document.body)
            .find("tr:nth-child(odd) td")
            .each(function(index) {
              $(this).css("background-color", "#F9F9F9");
            });
          $(win.document.body)
            .find("h1")
            .css("text-align", "center");
          $(win.document.body).prepend("</br>");
          $(win.document.body)
            .find("h1")
            .html("Contribution Report");
          $(win.document.body)
            .find("div")
            .css("text-align", "center");
          $(win.document.body)
            .find("div")
            .css("margin-top", "-17px")
            .append(printFilters());
        }
      },
      "copy",
      "excelHtml5",
      "csv",
      "colvis",
      "pageLength"
    ],
    rowReorder: false,
    dom:
      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
      "<'table-responsive'tr>" +
      "<'row'<'col-sm-6'i><'col-sm-6'p>>",
    language: {
      paginate: {
        previous: "&laquo;",
        next: "&raquo;"
      },
      search: "_INPUT_",
      searchPlaceholder: "Search…"
    },
    order: [[18, "asc"]],
    lengthMenu: [
      [10, 25, 50, -1],
      ["10 rows", "25 rows", "50 rows", "Show all"]
    ]
  });

  $datatablesButtons
    .buttons()
    .container()
    .appendTo("#demo-datatables-2_wrapper .col-sm-6:eq(0)");

  $(".btn-default")
    .addClass("btn-outline-primary")
    .removeClass("btn-default");
  $("input[type=search]").addClass("form-control input-sm");
  $("a.btn, a.buttons-print, a.btn-outline-primary").attr("id", "printBtn");
}

function datatablesRenderFinances() {
  nameReport = "FullReport";
  if ($("#fromDate").val().length == 10 && $("#toDate").val().length == 10) {
    if ($("#fromDate").val() != $("#toDate").val()) {
      nameReport = $("#fromDate").val() + "to" + $("#toDate").val() + "Report";
    } else {
      nameReport = $("#toDate").val() + "Report";
    }
  }

  var DataTable = $.fn.dataTable;
  $.extend(true, DataTable.Buttons.defaults, {
    dom: {
      button: {
        className: "btn btn-outline-primary btn-sm"
      }
    }
  });

  var $datatablesButtons = $("#demo-datatables-2").DataTable({
    buttons: [
      {
        extend: "csv",
        filename: nameReport
      }
    ],
    rowReorder: false,
    dom:
      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
      "<'table-responsive'tr>" +
      "<'row'<'col-sm-6'i><'col-sm-6'p>>",
    language: {
      paginate: {
        previous: "&laquo;",
        next: "&raquo;"
      },
      search: "_INPUT_",
      searchPlaceholder: "Search…"
    },
    order: [[8, "asc"]],
    lengthMenu: [
      [10, 25, 50, -1],
      ["10 rows", "25 rows", "50 rows", "Show all"]
    ]
  });

  $datatablesButtons
    .buttons()
    .container()
    .appendTo("#demo-datatables-2_wrapper .col-sm-6:eq(0)");

  $(".btn-default")
    .addClass("btn-outline-primary")
    .removeClass("btn-default");
  $("input[type=search]").addClass("form-control input-sm");
  $("a.btn, a.buttons-print, a.btn-outline-primary").attr("id", "printBtn");
}

function convertFromDate() {
  if ($("#fromDate").val().length == 10) {
    startdate = $("#fromDate").val() + " 00:00:00";
    var a = startdate.split(" ");
    var d = a[0].split("-");
    var t = a[1].split(":");
    startdate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
    startdate = startdate.getTime();
    return startdate;
  }
  return "";
}

function convertToDate() {
  if ($("#toDate").val().length == 10) {
    enddate = $("#toDate").val() + " 23:59:59";
    var a = enddate.split(" ");
    var d = a[0].split("-");
    var t = a[1].split(":");
    enddate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
    enddate = enddate.getTime();
    return enddate;
  }
  return "";
}

$("#amount").on("change", function(e) {
  var optionSelected = $("option:selected", this);
  var valueSelected = this.value;
  if (valueSelected == "range") {
    $("#amountMin").css("visibility", "visible");
    $("#amountMax").css("visibility", "visible");
  } else {
    $("#amountMin").css("visibility", "hidden");
    $("#amountMax").css("visibility", "hidden");
  }
});

$("#fromDate").click(function() {
  if ($(this).val().length == 0) {
    $(this).datepicker("setDate", "today");
  }
});

function printFilters() {
  var filters = "</br>";
  var filter = getFilter();
  if (
    filter["contribution_date_min"] != undefined &&
    filter["contribution_date_max"] != undefined
  ) {
    filter["contribution_date_min"] = new Date(filter["contribution_date_min"])
      .toISOString()
      .slice(0, 10);
    filter["contribution_date_max"] = new Date(filter["contribution_date_max"])
      .toISOString()
      .slice(0, 10);
  }
  var count = 0;
  for (var key in filter) {
    if (filter.hasOwnProperty(key)) {
      filters +=
        "<strong>" +
        nameKey(key) +
        ":</strong> " +
        translateId(key, filter[key]) +
        "&emsp;&emsp;";
      count++;
      if (count == 6) {
        filters += "</br>";
        count = 0;
      }
    }
  }
  if (filters.length > 6) {
    filters = filters + "</br></br>";
  }
  return filters;
}

function translateId(key, id) {
  switch (key) {
    case "payment_gw_id":
      id = $("#paymentGW option:selected").text();
      return id;
      break;
    case "contribution_fund_id":
      id = $("#funds option:selected").text();
      return id;
      break;
    case "contribution_containerid":
      id = $("#containerId option:selected").text();
      return id;
      break;
    case "application_id":
      id = $("#applicationName option:selected").text();
      return id;
      break;
    case "transaction_status_id":
      id = $("#txStatus option:selected").text();
      return id;
      break;
    case "payment_type_id":
      id = $("#paymentType option:selected").text();
      return id;
      break;
    case "donation_source_id":
      id = $("#source option:selected").text();
      return id;
      break;
    case "donation_campaing_id":
      id = $("#campaing option:selected").text();
      return id;
      break;
    case "is_settled":
      id = $("#settlement option:selected").text();
      return id;
      break;
    default:
      return id;
  }
}

function nameKey(key) {
  switch (key) {
    case "person_firstname":
      return "First Name";
      break;
    case "person_lastname":
      return "Last Name";
      break;
    case "person_email":
      return "Email";
      break;
    case "payment_gw_id":
      return "Payment Gateway";
      break;
    case "contribution_onl_transactionid":
      return "Transactionid";
      break;
    case "contribution_paymentinfo":
      return "Last 4 Numbers";
      break;
    case "contribution_fund_id":
      return "Fund Id";
      break;
    case "contribution_containerid":
      return "Container Id";
      break;
    case "person_authorizenetprofileid":
      return "AuthorizeProfileId";
      break;
    case "contribution_onl_approvalnumber":
      return "ApprovalNumber";
      break;
    case "contribution_form":
      return "Form";
      break;
    case "transactioninternalid":
      return "InternalId";
      break;
    case "application_id":
      return "Aplication Name";
      break;
    case "transaction_status_id":
      return "Status Id";
      break;
    case "payment_type_id":
      return "Payment Type";
      break;
    case "donation_source_id":
      return "Source";
      break;
    case "donation_campaing_id":
      return "Campaing";
      break;
    case "is_settled":
      return "Settlement";
      break;
    case "contribution_amount":
      return "Amount";
      break;
    case "contribution_amount_min":
      return "Amount Min";
      break;
    case "contribution_amount_max":
      return "Amount Max";
      break;
    case "contribution_date_min":
      return "From Date";
      break;
    case "contribution_date_max":
      return "To Date";
      break;
    default:
      return key;
  }
}
function convertDateEnglish(inputFormat, langId) {
  function pad(s) {
    return s < 10 ? "0" + s : s;
  }
  var d = new Date(inputFormat);
  return [pad(d.getMonth() + 1), pad(d.getDate()), d.getFullYear()].join("/");
}

function getDataExternalPurchase() {
  var data = {};
  data["name"] = $("#fname").val();
  data["lastname"] = $("#lname").val();
  data["email"] = $("#email").val().trim();
  data["phone"] = $("#phone").val();
  data["fund"] = $("#funds").val();
  data["amount"] = $("#amount").val();
  data["medium"] = $("#medium").val();
  data["source"] = $("#source").val();
  data["country"] = $("#country").val();
  data["source_code"] = $("#sourceMarketing").val();
  data["paymentType"] = $("#external-payment-method").val();
  data["concept"] = $("#external-payment-description").val() || "";
  data["coupon"] = $("#coupon-code").val().trim() || "";
  data["comments"] = $("#comments").val() || "";
  data["checkStartDate"]   = checkStartDate;
  data["startDate"]	   	   = formatStartDateFull();
  data["langid"] = $('#languaje').val();
  
  return data;
}

function getDataCallCenterPurchase() {
  var data = {};
  data["name"] = $("#fname").val();
  data["lastname"] = $("#lname").val();
  data["email"] = $("#email").val().trim();
  data["phone"] = $("#phone").val();
  data["fund"] = $("#funds").val();
  data["amount"] = $("#amount").val();
  data["medium"] = $("#medium").val();
  data["country"] = $("#country").val();
  data["source"] = $("#source").val();
  data["source_code"] = $("#sourceMarketing").val();
  data["orderDescription"] = $("#funds option:selected").text();
  data["token"] = $("#stripeToken").val() || "";
  data["paymentGatewayId"] = 2;
  data["langid"] = $('#languaje').val();
  data["coupon"]           = "";
  data["comments"]         = $("#comments").val() || "";
  data["card"]             = $("#listCredicards").val() || "";
  
  data["savePaymentData"]  = false;
    
  data["checkStartDate"]   = checkStartDate;
  data["startDate"]	   	   = formatStartDateFull();

  return data;
}

function getDataCallCenterPurchaseSplitPayment() {
	  var data = {};
	  data["name"] = $("#fname").val();
	  data["lastname"] = $("#lname").val();
	  data["email"] = $("#email").val().trim();
	  data["phone"] = $("#phone").val();
	  data["fund"] = $("#funds").val();
	  data["amount"] = $("#amount").val();
	  data["medium"] = $("#medium").val();
	  data["country"] = $("#country").val();
	  data["source"] = $("#source").val();
	  data["source_code"] = $("#sourceMarketing").val();
	  data["orderDescription"] = $("#funds option:selected").text();
	  data["token"] = $("#stripeToken").val() || "";
	  data["paymentGatewayId"] = 2;
	  data["langid"] = $('#languaje').val();
	  data["coupon"]           = $("#coupon-code").val().trim() || "";
	  data["comments"]         = $("#comments").val() || "";
	  data["card"]             = $("#listCredicards").val() || "";
	  data["savePaymentData"]  = $("#save-credit-card").prop('checked') || false;
	  data["payableAmount"]	   = $("#payableAmount").val();
	  data["checkStartDate"]   = checkStartDate;
	  data["startDate"]	   	   = formatStartDateFull();
	  return data;
	}

function formatStartDateFull() {
	var dateFull = new Date( $("#start-date").val());
    const now = dayjs();
    const fullDate = dayjs(dateFull).hour(now.hour()).minute(now.minute()).second(now.second());			
    startDateFull = dateFull.valueOf();
    
    return startDateFull;
}

// 1.Crear evento en el select de metodo de pago, que dependiendo del valor cambia el formulario
//    - Para stripe, es el formulario inicial no cambia nada
//    - Para cash desaparece todo campo relacionado a pagos de tarjeta, etc.
//    - Para Other, deberia desaparecer datos de tarjeta y aparecer input text indicando el tipo de pago empleado
//      - Para complimentary deberia aplicar desplegar un campo select con los tipos de cupones existentes incluyendo GRATIS
// 2. Debe arreglarse el submit relacionado a los diferentes formularios, actualmente existe 2 enfoques posibles de unificarlo:
//      - Podria dejarse inicialmente los metodos asociados a la ruta y con javascript solo se cambiarian los script y el path a
//      la llamada ajax.
//      - Todos los formularios van al mismo submit que dirigen al mismo metodo en el controlador y desde ahi manejar la logica.

//SECCION DE DATOS PERSONALES

// validacion datos personales

function validateCoupon() {
  var dataCoupon = {
    couponCode: $("#coupon-code").val().trim(),
    amount: $("#amount").val(),
    email: $("#email").val().trim()
  };

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=UTF-8",
    url: ajaxPath + "search/api/validateCoupon",
    data: JSON.stringify(dataCoupon),
    dataType: "json",
    timeout: 100000,
    beforeSend: function() {
      $(".coupon-input .input-with-icon").append(
        '<span class="spinner spinner-default spinner-sm input-icon"></span>'
      );
      $("#coupon-code").attr("readonly", "readonly");
    },
    success: function(data) {
      console.log("SUCCESS: ", data);

      if (data.returnCode == 0) {
    	  applyCoupon = true;
        couponApplied(data.result.cuponDiscount, "success");
      } else if (
        data.returnCode == 1 ||
        data.returnCode == 4 ||
        data.returnCode == 6 ||
        data.returnCode == 7 ||
        data.returnCode == 8 ||
        data.returnCode == 10
      ) {
        wrongCoupon();
      } else {
        $(".coupon-input .custom-message-error").addClass("has-error");
        $(".coupon-input .custom-message-error").text("Cupón inválido");
        $(".coupon-input .input-with-icon .spinner").remove();
        $("#coupon-code").removeAttr("readonly");
      }
    },
    error: function(e) {
      $(".coupon-input .input-with-icon .spinner").remove();
      $("#coupon-code").removeAttr("readonly");
    }
  });
}

function couponApplied(discount) 
{
  $(".coupon-input .custom-message-error").removeClass("has-error");
  $(".coupon-input .custom-message-error").addClass("has-success");
  $("#coupon-code").addClass("has-success");
  $(".coupon-input .input-with-icon .spinner").remove();
  $(".coupon-input .custom-message-error").text("");
  $(".coupon-input .custom-message-error").text("Valid coupon!!");


  //$("#amount").val()
  var result = 0;
  if (discount >= billing.mount)
  {
    result = billing.settings.freeUserCommission;
  }
  else
  {
    var mount = billing.realMount;
    result = mount - discount;
  }

  if ($("#payment-method").val() == "splitPayment") {
	  $("#total-coupon").hide();
	  $("#discount-amount-coupon").show();
	  getAmounts(0.00,billing.settings.freeUserCommission, false, discount);
  }
  else {
	  $("#total-coupon").show();
	  $("#discount-amount-coupon").hide();
  }
  
  $("#total-mount").text("$" + formatMoney(result, ",", "."));
  $("#amount-coupon").text("Discount: $" + formatMoney(discount, ",", "."));
  $("#coupon-discount").show();
  $("#coupon-discount").attr("data-free", "false");

  if (result < 1) {
    if ($("#register-stripe-record").data("validator"))
      $("#register-stripe-record")
        .data("validator")
        .destroy();

    $("#coupon-discount").attr("data-free", "true");
    billing.component.hide();

    $("#register-stripe-record").validate(free_validate_coupon);
    $('#callPurchase').hide();
    $('#btnFree').show();

    hideAllMethods();
  }
}

function wrongCoupon() {
  $(".coupon-input .custom-message-error").removeClass("has-success");
  $(".coupon-input .custom-message-error").addClass("has-error");
  $(".coupon-input .custom-message-error").text("");
  $(".coupon-input .custom-message-error").text("Invalid code, try again");
  $(".coupon-input .input-with-icon .spinner").remove();
  $("#coupon-code").removeAttr("readonly");
  $("#coupon-code").val("");
}

function enableCoupons() {
  if ($("#apply-coupon").is(":checked")) {
    $(".coupon-group").show();
  } else {
    $("#apply-coupon-container").show();
  }
}

// SECCION DE PAGO
function showStripeForm() {
	$(".split-payment").hide();
	$(".stripe-form-elements").show();
	$(".external-payment").hide();
	$("#complimentary-element").hide();
	$("#save-container").show();
	enableCoupons();
	validateWithStripe();
	if (billing.subscription) {
		$("#save-container").hide();
	}
	console.log($("#coupon-code").val().trim());
	if ($("#coupon-code").val().trim() != null && $("#coupon-code").val().trim() != undefined && $("#coupon-code").val().trim() != "" && applyCoupon == true) {
		validateCoupon();
	}
	if (applyCoupon == false) {
		$("#coupon-code").val("")
	}
}

function showSplitPayment(paidAmount,paidAmountCommission,clear) {
	$(".split-payment").show();
	$(".stripe-form-elements").show();
	$(".external-payment").hide();
	$("#complimentary-element").hide();
	$("#save-container").show();
	validateWithStripe();
	getAmounts(paidAmount,paidAmountCommission, clear, null);
	if ($("#coupon-code").val().trim() != null && $("#coupon-code").val().trim() != undefined && $("#coupon-code").val().trim() != "" && applyCoupon == true) {
		validateCoupon();
	}
	if (applyCoupon == false) {
		$("#coupon-code").val("")
	}
}

function showExternalPaymentForm() {
	$(".split-payment").hide();
  $(".stripe-form-elements").hide();
  $("#complimentary-element").hide();
  $(".external-payment").show();
  enableCoupons();
  validateExternalPurchase();
  	if ($("#coupon-code").val().trim() != null && $("#coupon-code").val().trim() != undefined && $("#coupon-code").val().trim() != "" && applyCoupon == true) {
  		validateCoupon();
	}
	if (applyCoupon == false) {
		$("#coupon-code").val("")
	}
}

function hideAllMethods() {
  $(".payment-method-container").hide();
  $(".stripe-form-elements").hide();
  $(".external-payment").hide();
  $(".split-payment").hide();
}

function showFreeMode() {
	$(".split-payment").hide();
  $(".stripe-form-elements").hide();
  $(".external-payment").hide();
  $("#complimentary-element").hide();
  $(".coupon-group").hide();
  $("#save-container").hide();
  validateFree();
  hideCoupon();
}

function hideCoupon() {
	$(".coupon-input .custom-message-error").text("");
	$(".coupon-input .custom-message-error").removeClass("has-success");
	$(".coupon-input .custom-message-error").removeClass("has-error");
	$("#coupon-discount").hide();
}

function getAmounts(paidAmount,paidAmountCommission, clear, discount) {
	const amountToPay = ($("#amount").val() - paidAmount).toFixed(2);
	const amountToPayWithCommission = (billing.realMount - (typeof paidAmountCommission=== 'undefined'? 0:paidAmountCommission)).toFixed(2);
	fee = (billing.realMount - $("#amount").val()).toFixed(2);
	if (discount != null) {
		var couponAply = (amountToPay - discount).toFixed(2);
		var couponAplyWithCommission = (amountToPayWithCommission - discount).toFixed(2);
		$("#payableAmount").val(couponAply);
		$("#amountToPay").val(couponAplyWithCommission);
		document.getElementById("slid").setAttribute("max", couponAply);
		document.getElementById('slid').value = couponAply;
	}
	else {
		$("#payableAmount").val(amountToPay);
		$("#amountToPay").val(amountToPayWithCommission);
		$("#amountToPay").val(amountToPayWithCommission);
		document.getElementById("slid").setAttribute("max", amountToPay);
		document.getElementById('slid').value = amountToPay;
	}
	document.getElementById('lAmount').innerHTML = "Amount to pay (includes fee)";
	document.getElementById('text-fee').innerHTML = "The fee ($" +  fee + ") will be charged only in the first transaction";
	if (paidAmount == 0 && clear == false) {
		$("#text-fee").show();
		enableCoupons();
	}
	else if (paidAmount != 0) {
		$("#text-fee").hide();
		$("#apply-coupon-container").hide();
	}
}

function sliderChange(val) {
    document.getElementById('payableAmount').value = val;
}

function inputChange(val) {
    document.getElementById('slid').value = val;
}

function inputEmpty(val) {
	if ($("#payableAmount").val().length == 0) {
		document.getElementById('slid').value = 0;
	}
}

function validateWithStripe() {
  if ($("#apply-coupon").is(":checked")) {
    if ($("#register-stripe-record").data("validator"))
      $("#register-stripe-record")
        .data("validator")
        .destroy();
    $("#register-stripe-record").validate(stripe_validation_coupon);
  } else {
    if ($("#register-stripe-record").data("validator"))
      $("#register-stripe-record")
        .data("validator")
        .destroy();
    $("#register-stripe-record").validate(stripe_validation);
  }
}

function validateExternalPurchase() {
  if ($("#apply-coupon").is(":checked")) {
    if ($("#register-stripe-record").data("validator"))
      $("#register-stripe-record")
        .data("validator")
        .destroy();
    $("#register-stripe-record").validate(external_validation_coupon);
  } else {
    if ($("#register-stripe-record").data("validator"))
      $("#register-stripe-record")
        .data("validator")
        .destroy();
    $("#register-stripe-record").validate(external_validation);
  }
}

function validateFree() {
  if ($("#register-stripe-record").data("validator"))
    $("#register-stripe-record")
      .data("validator")
      .destroy();
  $("#register-stripe-record").validate(free_validate);
}

function currentPaymentType(clear) {
  $(".payment-method-container").show();
  const current_value = $("#payment-method").val();
  console.log("This actually works");
  switch (current_value) {
    case "stripe":
      showStripeForm();
      break;
    case "free":
      showFreeMode();
      break;
    case "splitPayment":
    	showSplitPayment(0.00, 0.00, clear);
        break;
    case "other":
      showExternalPaymentForm();
      break;
    default:
      showStripeForm();
      break;
  }
}

$(document).ready(function() {
  $("#fromDate").mask("00-00-0000");
  $("#toDate").mask("00-00-0000");
  $("#cardNumber").mask("0000-0000-0000-0000");
  $("#cardNumber").on("keyup", function(e) {
    const numberCard = $("#cardNumber").val();
    setCreditCardMask(numberCard);
  });
  $("#lang-expdate_ph").mask("99/99");

  $("#external-payment-description").val("");

  $("#payment-method").change(function() {
	  var clear=false;
    currentPaymentType(clear);
    billing.init();
  });

  formValidation = $("#register-stripe-record").validate(stripe_validation);

  //CUPONES EVENTS

  $("#apply-coupon").click(function() {
    console.log("work checkbox!");
    billing.init();
    if ($("#apply-coupon").is(":checked")) {
      $(".coupon-input").show();
      var clear=false;
      currentPaymentType(clear);
    } else {
      $(".coupon-input").hide();
      $(".coupon-input .custom-message-error").text("");
      $("#coupon-code").val("");
      $("#coupon-code").removeAttr("readonly");
      $("#coupon-discount").hide();
      var clear=false;
      currentPaymentType(clear);
    }
  });
  
  $("#modify-start-date").click(function() {
	  console.log("work checkbox start date!");
	  if ($("#modify-start-date").is(":checked")) {
		  checkStartDate = true;
	      $(".start-date-input").show();
	      billing.init();
	      var formatDate = new Date(billing.startDate).toLocaleDateString('en-US', {year: 'numeric', month: '2-digit', day: '2-digit'});
	      var date = new Date(formatDate);
	      var devolucion = new Date();
	      devolucion.setDate(date.getDate() + 1);
	      var formatStartDate = new Date(devolucion).toLocaleDateString('en-CA');

	      $('#start-date').datepicker({
	    	  format: "mm/dd/yyyy",
	    	  startDate: new Date(formatStartDate)
	      }).datepicker('setDate', formatDate);
	      	      
	  } else {
		  checkStartDate = false;
	      $(".start-date-input").hide();
	      $(".start-date-input .custom-message-error").text("");
	      $("#start-date").val("");
	      $("#start-date").removeAttr("readonly");
	  }
  });
 
  $("#click-apply-code")
  .click(function() 
  {
      $(".coupon-input .custom-message-error").removeClass("has-error");
      $("#error-apply-code").removeClass('active');
      loadMyCoupon();
  });

  function loadMyCoupon()
  {
    let value = $("#coupon-code").val().trim();
    if (value.length > 1)
    {
      const validator = $("#register-stripe-record").data("validator");
      if (
        validator.element("#email") &&
        validator.element("#amount")
      )
      {
        validateCoupon();
      }
      else
      {
        $("#error-apply-code").addClass('active');
      }

    }
    else
    {
      $("#error-apply-code").addClass('active');
    }
  }

  $("#form-mode").click(function() {
    if ($("#form-mode").is(":checked")) {
      $("#one-customer-form").hide();
      $(".is-massive-too").prependTo("#event-data-massive");
      $("#massive-customer-form").show();
    } else {
      $("#massive-customer-form").hide();
      $(".is-massive-too").prependTo("#event-data-form");
      $("#one-customer-form").show();
    }
  });

  // Carga masiva
  $("#registerCustomers").click(function(e) {
    e.preventDefault();
    $("#massive-customer-upload").submit();
  });
  $("#comments").val("");

  var urlParams = window.location.search;
  var params = new URLSearchParams(urlParams);
  var emailParam = params.has("email") ? params.get("email") : "";
  var nameParam = params.has("fname") ? params.get("fname") : "";
  var lnameParam = params.has("lname") ? params.get("lname") : "";
  var complimentaryParam = params.has("complimentary") ? true : false;

  $("#fname").val(nameParam);
  $("#lname").val(lnameParam);
  $("#email").val(emailParam);

  if (complimentaryParam) {
    $("#payment-method").val("free");
    $(".stripe-form-elements").hide();
    $("#comments").focus();
    var clear=false;
    currentPaymentType(clear);
  }
  
  if(emailParam.length > 1){
	  FindUserPreLoadRequest(emailParam);  
  }
  
});

//to know what kind of credit card is
function getCreditCardService(value) {
  var visaPattern = /^4/.test(value);
  var masterPattern = /5[1-5][0-9]|(2(?:2[2-9][^0]|2[3-9]|[3-6]|22[1-9]|7[0-1]|72[0]))\d*/.test(
    value
  );
  var amexPattern = /^34|^37/.test(value);
  var discoverPattern = /^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)/.test(
    value
  );
  var cardType = "invalid";

  if (visaPattern) {
    cardType = "visa";
  } else if (masterPattern) {
    cardType = "master";
  } else if (amexPattern) {
    cardType = "amex";
  } else if (discoverPattern) {
    cardType = "discover";
  }

  return cardType;
}
// set a mask to a input
function setCreditCardMask(val) {
  var prefixCardNumber = val;
  var prefixCardLenght = prefixCardNumber.length;

  if (prefixCardLenght < 5) {
    var creditCardType = getCreditCardService(prefixCardNumber);
    if (creditCardType == "amex") {
      $("#cardNumber").unmask();
      $("#cardNumber").mask("0000-000000-00000");
    } else if (creditCardType == "visa") {
      $("#cardNumber").unmask();
      $("#cardNumber").mask("0000-0000-0000-0000");
    } else if (creditCardType == "master") {
      $("#cardNumber").unmask();
      $("#cardNumber").mask("0000-0000-0000-0000");
    } else if (creditCardType == "discover") {
      $("#cardNumber").unmask();
      $("#cardNumber").mask("0000-0000-0000-0000");
    } else {
      $("#cardNumber").unmask();
      $("#cardNumber").mask("0000-0000-0000-0000");
    }
  } else {
    return false;
  }
}

// Carga masiva

// Carga masiva

function registerCustomersList() 
{
  //TODOD1 erros massive
  const dataCustomers = getDataCustomerListForm();
  controllerRegisterCustomersErrors("");

  $.ajax(
  {
    type: "POST",
    contentType: false,
    url: ajaxPath + "search/api/registerCustomersList",
    data: dataCustomers,
    dataType: "html",
    timeout: 0,
    processData: false,
    cache: false,
    beforeSend: function() 
    {
      $("#registerCustomers").hide();
      $("#submit-spinner-3").show();
      $("#massive-errors").text("");
    },
    success: function(data) 
    {

      data = JSON.parse(data);
      if (data.returnCode == "0")
      {
        $("#registerCustomers").show();
        $("#submit-spinner-3").hide();
        showToast("success", "Success", "The result of the transaction will be notified via email");
        //showListCustomersErrors(data.result.verifiedTransactions);
      } 
      else if (data.returnCode == "-4") 
      {
        $("#registerCustomers").show();
        $("#submit-spinner-3").hide();
        showListCustomersErrors(data);
        controllerRegisterCustomersErrors("An unexpected error has occurred. Please try again or contact the System Admin");
      }
      else if (data.returnCode == "-50") 
      {
        $("#registerCustomers").show();
        $("#submit-spinner-3").hide();
        controllerRegisterCustomersErrors("The csv file is corrupt");
      }
      else if (data.returnCode == "-22" || data.code == "-2") 
      {
        $("#registerCustomers").show();
        $("#submit-spinner-3").hide();
        showToast("error", "Error!!", data.msg);
        controllerRegisterCustomersErrors("An unexpected error has occurred. Please try again or contact the System Admin");
      } 
      else 
      {
        $("#registerCustomers").show();
        $("#submit-spinner-3").hide();
        showToast("error", "Error!!", "Unexpected Error");
        controllerRegisterCustomersErrors("An unexpected error has occurred. Please try again or contact the System Admin");
      }
    },
    error: function(e) 
    {
      $("#registerCustomers").show();
      $("#submit-spinner-3").hide();
      controllerRegisterCustomersErrors("An unexpected error has occurred. Please try again or contact the System Admin");
      //showToast("error", "Error!!", data.msg);
    }
  });
}

function controllerRegisterCustomersErrors(msg)
{
  if (    msg != "" 
      ||  msg != undefined )
  {
    $("#registerCustomersErrors").show();
    $("#registerCustomersErrors").html(msg);
  }
  else
  {
    $("#registerCustomersErrors").hide();
    $("#registerCustomersErrors").html("");
  }
}

function showListCustomersErrors(d) {
  console.log(d);
  const errorBox = document.querySelector("#massive-errors");
  const table = document.createElement("table");
  table.setAttribute("id", "table-status")
  const thead = `<thead>
            <tr>
              <th>Status</th>
              <th>Email</th>
              <th>Name</th>
              <th>Last Name</th>
              <th>Payment type</th>
              <th>Phone</th>
              <th>Payment details</th>
              <th>Transaction</th>
            </tr>
          </thead>`;
  
  const tbody = d.map(function(row){
    const valid = row.valid ? "OK" : "FAIL";
    const validColor = row.valid ? "color:green;" : "color:red;" ;
    return `
      <tr>
         <td style="${validColor}">${valid}</td>
         <td>${row.item.email}</td>
         <td>${row.item.name}</td>
         <td>${row.item.lastname}</td>
         <td>${row.item.paymentType}</td>
         <td>${row.item.phone}</td>
         <td>${row.item.paymentDetails}</td>
         <td>${row.message}</td>
      </tr>
    
    `
  }).join("");
  const content = thead + "<tbody>" + tbody + "</tbody>";
  errorBox.appendChild(table);  
  $("#table-status").html(content);
  $("#table-status").addClass("table table-striped table-nowrap");
  var tableInserted = $("#table-status").DataTable({
    dom:
        "<'row'<'col-sm-6'><'col-sm-6'f>>" +
        "<'table-responsive'tr>" +
        "<'row'<'col-sm-6'i><'col-sm-6'p>>"
  });
  $("#table-status_filter input").hide();
  $("#massive-errors").show();
  
}

function getDataCustomerListForm() {
  const source = $("#source").val();
  const medium = $("#medium").val();

  const customersForm = new FormData(
    document.getElementById("massive-customer-upload")
  );

  customersForm.append("source", source);
  customersForm.append("medium", medium);

  console.log(customersForm);
  return customersForm;
}

$("#massive-customer-upload").validate({
  rules: {
    funds: "required",
    amount: "required",
    customers: "required"
  },
  messages: {
    funds: dataErrors[ErrorActualIdiom].user_error_5,
    amount: dataErrors[ErrorActualIdiom].user_error_5,
    customers: dataErrors[ErrorActualIdiom].user_error_5
  },
  submitHandler: registerCustomersList,
  errorClass: "has-error",
  validClass: "has-success",
  highlight: function(element, errorClass, validClass) {
    $(element)
      .closest(".form-group")
      .addClass(errorClass)
      .removeClass(validClass);
  },
  unhighlight: function(element, errorClass, validClass) {
    $(element)
      .closest(".form-group")
      .addClass(validClass)
      .removeClass(errorClass);
  },
  errorPlacement: function(error, element) {
    var parent = $(element)
      .parent()
      .parent();
    var sibling = parent.children(".custom-message-error");
    error.appendTo(sibling);
  }
});

$('form').on('focus', 'input[type=number]', function (e) {
	$(this).on('wheel.disableScroll', function (e) {
		e.preventDefault()
	})
})
$('form').on('blur', 'input[type=number]', function (e) {
	$(this).off('wheel.disableScroll')
})