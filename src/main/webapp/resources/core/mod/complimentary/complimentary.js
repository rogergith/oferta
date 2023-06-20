$('.collapsePurchase').collapse();
//$(".menuPurchase").addClass("active");
//$("#menuPaypal").addClass("active");
$("#amount").append('<option value='+amountByFund[$("#funds").val()]+' selected="selected">'+amountByFund[$("#funds").val()]+'</option>');

// Activate validation for paypal external register form
var ErrorActualIdiom = 1;
var dataErrors = [null,{"lettersonly_error_1":"Letras y espacios únicamente por favor","step2_error_6":"Su apellido no debe tener más de 20 caracteres","user_error_tdc_decline":"Tu Tarjeta ha sido negada. Por favor intenta con otra tarjeta.","user_error_3":"Debe colocar usuario u contraseña.","step3_error_4":"Proporcione una fecha de caducidad","vmcardsonly_error_1":"Introduzca un número de tarjeta de crédito Visa o Master válido.","step2_error_7":"Por favor, introduce una dirección de correo electrónico válida","step2_error_1":"Primer nombre requerido","step3_error_7":"Su cvv no debe tener más de 4 caracteres","step2_error_2":"Su primer nombre debe tener al menos 3 caracteres","user_error_5":"El campo es requerido.","step3_error_2":"Su tarjeta de crédito debe tener un mínimo de 12 caracteres","step3_error_3":"Su tarjeta de crédito no debe tener más de 16 caracteres","step2_error_4":"Proporcione un apellido","step3_error_1":"Proporcione una tarjeta de crédito","step2_error_8":"El correo electrónico no coincide","user_error_1":"El usuario ya existe","step3_error_6":"Su cvv debe tener al menos 3 caracteres","step2_error_5":"Su apellido debe tener al menos 3 caracteres","step3_error_5":"Proporcione un cvv","numberand_error_1":"Su tarjeta de crédito no puede contener ninguna letra","step2_error_3":"Su primer nombre no debe tener más de 20 caracteres","user_error_4":"Usuario no registrado","stepL_error_1":"Por favor, introduce una dirección de correo electrónico válida","user_error_2":"Usuario o contraseña incorrectos.","step3_error_8":"Su cvv no puede contener ninguna letra","expdate_error_1":"Por favor, introduzca una fecha de vencimiento válida","user_error_6":"Ha ocurrido un error, intente más tarde."},{"expdate_error_1":"Please enter a valid expiration date","user_error_1":"User already exists","step2_error_4":"Please provide a Last name","step3_error_1":"Please provide a credit card","numberand_error_1":"Your credit card can not content any letter","user_error_2":"Incorrect user or password.","user_error_3":"You must enter username and password.","step3_error_7":"Your cvv must not be more than 4 characters long","step3_error_6":"Your cvv must be at least 3 characters long","stepL_error_1":"Please enter a valid email address","user_error_4":"Unregistered user","step3_error_3":"Your credit card must not be more than 16 characters long","lettersonly_error_1":"Letters and spaces only please","step2_error_6":"Your Last name must not be more than 20 characters long","step2_error_3":"Your first name must not be more than 20 characters long","step2_error_5":"Your Last name must be at least 3 characters long","step2_error_1":"Please provide a a first name","vmcardsonly_error_1":"Please enter a valid Visa or Master credit card number.","step2_error_2":"Your first name must be at least 3 characters long","user_error_tdc_decline":"Your card has been declined. Please use another one.","step3_error_4":"Please provide a expiration date","step3_error_5":"Please provide a cvv","step2_error_7":"Please enter a valid email address","user_error_5":"This field is required.","user_error_6":"An error occurred, please try again later.","step3_error_8":"Your cvv card can not content any letter","step3_error_2":"Your credit card must be at least 12 characters long","step2_error_8":"The email does not match"}];

function validateComplimentaryForm(){

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