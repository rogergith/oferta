var form = $('#restrictedDialog');
var fundId = parseInt(funds[0].id);

$(document).ready(function () {


    
    $('#submit-information').click(function (e) {
        
        e.preventDefault();
         $("#restrictedActualEventInformation").submit();
        
    });
    
   $('#toggle-signature').click(function(){
	   activateSignatureRequired();
    });

  //  var data = getFormData();
  //  var ajaxRequest = new AjaxRequest(data);
   // ajaxRequest.getEventSettings();
    getEventSettings();
});

var validationConfig = {
    rules: {

        initialDate: 'required',
        endDate: {
            required: true,
            validateEndDate: "#initial-date"
        },
        allowedDays: 'required',
        "template-id": "required"

    },
    submitHandler:  handleFormRequest,
    errorClass: 'has-error',
    validClass: 'has-success',
    highlight: function (element, errorClass, validClass) {
        $(element)
            .closest('.input-with-icon')
            .addClass(errorClass).removeClass(validClass);
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element)
            .closest('.input-with-icon')
            .addClass(validClass).removeClass(errorClass);
    },
    errorPlacement: function (error, element) {
        var parent = $(element).parent().parent();
        var sibling = parent.children(".input-with-icon");
        error.appendTo(sibling);
    }
};

function handleFormRequest() {

    if(settingId == 0) {
        addEventSettings();
    } else {
        updateEventSettings();
    }
}


function toggleRestricted() {
    var isChecked = $('#toggleRestrict').is(':checked');

    if (isChecked) {
        form.show();
        $("#restrictedActualEventInformation").validate(validationConfig);

    } else {
        deleteEventSettings();
    }

}

function activateSignatureRequired() {
    var isChecked = $('#toggle-signature').is(':checked');

    if (isChecked) {
    	$(".document-template-container").show();
       // $("#restrictedActualEventInformation").validate(validationConfig);

    } else {
    	$(".document-template-container").hide();
    }

}


function getEventSettings() {
    var fund = {fundId: parseInt(funds[0].id)};
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: ajaxPath + "search/api/getEventSettings",
        data: JSON.stringify(fund),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function() {
            $(".event-setting").hide();
            $(".setting-spinner").show();
        },
        success: function (data) {
            $(".setting-spinner").hide();
            console.log("success: ", data)
            if(!data) {
                $(".event-setting").show();
                form.hide();
            } else if( data.returnCode == 0 ) {
                
                var formInfo = data.result.eventFundSettings;
                activateAndLoadDataForm(formInfo);
            }
        },
        error: function (e) {
            $(".event-setting").show();
            $(".setting-spinner").hide();
            form.hide();
            console.log("ERROR: ", e);
        }
    });
}

function deleteEventSettings() {
    
    if(settingId == 0 ){
        form.hide();
        return;
    } else {
          var data = {id: settingId, fundId: parseInt(funds[0].id)};
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: ajaxPath + "search/api/removeEventSettings",
                data: JSON.stringify(data),
                dataType: 'json',
                timeout: 100000,
                beforeSend: function() {
                    $(".event-setting").hide();
                    $(".setting-spinner").show();
                },
                success: function (data) {
                    $(".setting-spinner").hide();
                    console.log("Borrado: ", data)
                    if(!data) {
                        console.log("Unable to remove, try later..")
                    } else if( data.returnCode == 0 ) {
                        
                        $(".event-setting").show();
                        settingId = 0;
                        $("#initial-date").val("");
                        $("#end-date").val("");
                        $("#number-days").val("");
                        
                        var signatureRequired = $("#toggle-signature").is(":checked");
                        if(signatureRequired){
                        	$("#toggle-signature").click();
                        } 
                        
                        form.hide();
                    }
                },
                error: function (e) {
                    $(".event-setting").show();
                    $(".setting-spinner").hide();
                    console.log("ERROR: ", e);
                    console.log("Unable to remove, try later..");
                    $('#toggleRestrict').click();
                    showToast("error", "Error: ", "Unable to remove, try later...");
                }
            });
    }
}


function activateAndLoadDataForm(data) {
    $(".event-setting").show();
    
     var isChecked = $('#toggleRestrict').is(':checked');

        if (!isChecked) {
            $('#toggleRestrict').click();
            form.validate(validationConfig);
        }
    settingId = data.id;
    $("#initial-date").val(dateFormatter(new Date(data.startDate)));
    $("#end-date").val(dateFormatter(new Date(data.endDate)));
    $("#number-days").val(data.allowedDaysToAccess);
    
    if(data.signatureRequired){
    	$('#toggle-signature').click();
        $("#template-id").val(data.signatureDocumentId);
    }
    
    
}

function dateFormatter(date) {
    
    var dd = date.getDate();
    var mm = date.getMonth() + 1; //January is 0!

    var yyyy = date.getFullYear();
    if (dd < 10) {
      dd = '0' + dd;
    } 
    if (mm < 10) {
      mm = '0' + mm;
    } 
    var formattedDate = mm + '/' + dd + '/' + yyyy;
    
    return formattedDate;
}

function addEventSettings() {
    
    var initialDate = new Date($("#initial-date").val());
        initialDate = initialDate.getTime();
    var endDate = new Date($("#end-date").val());
        endDate = endDate.getTime();
    
    var signatureRequired = $("#toggle-signature").is(":checked");   
        
    var data = {
            fundId: fundId,
            startDate: initialDate,
            endDate: endDate,
            allowDays: $("#number-days").val(),
            signatureRequired: signatureRequired
    }
    
    if(signatureRequired) {
    	data.signatureDocumentId = $("#template-id").val();
    }
    
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: ajaxPath + "search/api/addEventSettings",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 100000,
        success: function (data) {
            console.log("SUCCESS: ", data);
            if(!data) {
                showToast("error", "Error:", "An general error has ocurred, please report to admin.");
            } else if(data.returnCode == 0) {
                settingId = data.result.eventFundSettings.id;
                showToast("success", "Success:", "The settings were enabled succesfully.");
            } else if(data.returnCode == -2 || data.returnCode == -22 ) {
                showToast("error", "Error:", "The token expired, log in and try again.");
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
            showToast("error", "Error:", "An uncontrolled error has ocurred, please contact the admin.");
        },
        done: function (e) {
            alert("DONE");
            console.log("DONE");
        }
    });
}

function updateEventSettings() {
    
    var initialDate = new Date($("#initial-date").val());
        initialDate = initialDate.getTime();
    var endDate = new Date($("#end-date").val());
        endDate = endDate.getTime();
   var signatureRequired = $("#toggle-signature").is(":checked");   
        
   var data = {
        		id: settingId,
                fundId: fundId,
                startDate: initialDate,
                endDate: endDate,
                allowDays: $("#number-days").val(),
                signatureRequired: signatureRequired
        }
        
    if(signatureRequired) {
        	data.signatureDocumentId = $("#template-id").val();
   }
        
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: ajaxPath + "search/api/updateEventSettings",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 100000,
        success: function (data) {
            console.log("SUCCESS: ", data);
            if(!data) {
                showToast("error", "Error:", "An general error has ocurred, please report to admin.");
            } else if( data.returnCode == 0) {
                showToast("success", "Success:", "The settings were updated succesfully.");
            } else if(data.returnCode == -2 || data.returnCode == -22 ) {
                showToast("error", "Error:", "The token expired, log in and try again.");
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
            showToast("error", "Error:", "An uncontrolled error has ocurred, please contact the admin.");
        },
        done: function (e) {
            alert("DONE");
            console.log("DONE");
        }
    });
}

function getFormData() {

    var initialDate = new Date($('#initial-date').val());
    var endDate = new Date($('#end-date').val());
    var numberDays = $('#number-days').val();

    return data = {

        fundId: fundId,
        startDate: initialDate.getTime(),
        endDate: endDate.getTime(),
        allowDays: numberDays
    }

}

function showToast(status, head, msg) {
    var title   = head,
                    message = msg,
                    type    = status,
                    options = {};

                    toastr[type](message, title, options);
}

jQuery.validator.addMethod("validateEndDate", 
        function(value, element, params) {

            if (!/Invalid|NaN/.test(new Date(value))) {
                return new Date(value) >= new Date($(params).val());
            }

            return isNaN(value) && isNaN($(params).val()) 
                || (Number(value) >= Number($(params).val())); 
        },'Must be greater or equal than start date.');
