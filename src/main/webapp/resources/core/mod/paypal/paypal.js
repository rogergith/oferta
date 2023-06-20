$('.collapsePurchase').collapse();
$(".menuPurchase").addClass("active");
$("#menuPaypal").addClass("active");
$("#amount").append('<option value='+amountByFund[$("#funds").val()]+' selected="selected">'+amountByFund[$("#funds").val()]+'</option>');

// Activate validation for paypal external register form


function validateExternalPurchase(){

	if( formModeValidation =! null ) formModeValidation.destroy();
	
formModeValidation =  $("#register-stripe-record").validate({debug: true, 
                                       submitHandler: registerExternalPurchase, 
                                       rules: {
                                           fname: 'required',
                                           lname: 'required',
                                           email: {required: true, email:true},
                                           funds: 'required',
                                           amount:'required'
                                       },
                                       messages: {
                                           fname:  dataErrors[ErrorActualIdiom].step2_error_1,
                                           lname: dataErrors[ErrorActualIdiom].step2_error_4,
                                           email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:dataErrors[ErrorActualIdiom].step2_error_7},
                                           funds: dataErrors[ErrorActualIdiom].user_error_5,
                                           amount: dataErrors[ErrorActualIdiom].user_error_5
                                       },
                                       errorClass: 'has-error',
                                       validClass: 'has-success',
                                       highlight: function(element, errorClass, validClass) {
                                         $(element)
                                           .closest('.input-group')
                                           .addClass(errorClass).removeClass(validClass);
                                       },
                                       unhighlight: function(element, errorClass, validClass) {
                                         $(element)
                                           .closest('.input-group')
                                           .addClass(validClass).removeClass(errorClass);
                                       },
                                       errorPlacement: function(error, element) {
                                        var parent = $(element).parent(".input-group");
                                        var sibling = parent.children(".custom-message-error");
                                        error.appendTo( sibling );
                                      }
                                   }); 

}