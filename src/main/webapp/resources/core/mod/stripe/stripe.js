$('.collapsePurchase').collapse();
$(".sidenav-item").removeClass("active");
$(".menuPurchase").addClass("active");
$("#amount").append('<option value='+amountByFund[$("#funds").val()]+' selected="selected">'+amountByFund[$("#funds").val()]+'</option>');

var ErrorActualIdiom = 1;
var dataErrors = [null,{"lettersonly_error_1":"Letras y espacios únicamente por favor","step2_error_6":"Su apellido no debe tener más de 20 caracteres","user_error_tdc_decline":"Tu Tarjeta ha sido negada. Por favor intenta con otra tarjeta.","user_error_3":"Debe colocar usuario u contraseña.","step3_error_4":"Proporcione una fecha de caducidad","vmcardsonly_error_1":"Introduzca un número de tarjeta de crédito Visa o Master válido.","step2_error_7":"Por favor, introduce una dirección de correo electrónico válida","step2_error_1":"Primer nombre requerido","step3_error_7":"Su cvv no debe tener más de 4 caracteres","step2_error_2":"Su primer nombre debe tener al menos 3 caracteres","user_error_5":"El campo es requerido.","step3_error_2":"Su tarjeta de crédito debe tener un mínimo de 12 caracteres","step3_error_3":"Su tarjeta de crédito no debe tener más de 16 caracteres","step2_error_4":"Proporcione un apellido","step3_error_1":"Proporcione una tarjeta de crédito","step2_error_8":"El correo electrónico no coincide","user_error_1":"El usuario ya existe","step3_error_6":"Su cvv debe tener al menos 3 caracteres","step2_error_5":"Su apellido debe tener al menos 3 caracteres","step3_error_5":"Proporcione un cvv","numberand_error_1":"Su tarjeta de crédito no puede contener ninguna letra","step2_error_3":"Su primer nombre no debe tener más de 20 caracteres","user_error_4":"Usuario no registrado","stepL_error_1":"Por favor, introduce una dirección de correo electrónico válida","user_error_2":"Usuario o contraseña incorrectos.","step3_error_8":"Su cvv no puede contener ninguna letra","expdate_error_1":"Por favor, introduzca una fecha de vencimiento válida","user_error_6":"Ha ocurrido un error, intente más tarde."},{"expdate_error_1":"Please enter a valid expiration date","user_error_1":"User already exists","step2_error_4":"Please provide a Last name","step3_error_1":"Please provide a credit card","numberand_error_1":"Your credit card can not content any letter","user_error_2":"Incorrect user or password.","user_error_3":"You must enter username and password.","step3_error_7":"Your cvv must not be more than 4 characters long","step3_error_6":"Your cvv must be at least 3 characters long","stepL_error_1":"Please enter a valid email address","user_error_4":"Unregistered user","step3_error_3":"Your credit card must not be more than 16 characters long","lettersonly_error_1":"Letters and spaces only please","step2_error_6":"Your Last name must not be more than 20 characters long","step2_error_3":"Your first name must not be more than 20 characters long","step2_error_5":"Your Last name must be at least 3 characters long","step2_error_1":"Please provide a a first name","vmcardsonly_error_1":"Please enter a valid Visa or Master credit card number.","step2_error_2":"Your first name must be at least 3 characters long","user_error_tdc_decline":"Your card has been declined. Please use another one.","step3_error_4":"Please provide a expiration date","step3_error_5":"Please provide a cvv","step2_error_7":"Please enter a valid email address","user_error_5":"This field is required.","user_error_6":"An error occurred, please try again later.","step3_error_8":"Your cvv card can not content any letter","step3_error_2":"Your credit card must be at least 12 characters long","step2_error_8":"The email does not match"}];
 


// stripe with cooupon
var stripe_validation_coupon = {	
 		rules: {
			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required',
            funds: 'required',
            amount:'required',
            
            "coupon-code": {
				required: true,
				minlength: 1,
				maxlength: 30
			},
 			
 			"cardNumber": {
 				required: true,
 				minlength: 12,
 				maxlength: 20,
 				numberand: true,
 				vmcardsonly: true
 			},
 			"lang-expdate_ph":{
 				required: true,
 				minlength: 5,				
 				expdate: true
 			},
 			"lang-cvv_ph":{
 				required:true,
 				minlength:3,
 				maxlength:4,
 				number:true,
 			},
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5,
			
 			"cardNumber": {
 				required:  dataErrors[ErrorActualIdiom].step3_error_1,
 				minlength: dataErrors[ErrorActualIdiom].step3_error_2,
 				maxlength: dataErrors[ErrorActualIdiom].step3_error_3,
 			},
 			"lang-expdate_ph": {
 				required: dataErrors[ErrorActualIdiom].step3_error_4,
 			},
 			"lang-cvv_ph": {
 				required:  dataErrors[ErrorActualIdiom].step3_error_5,
 				minlength: dataErrors[ErrorActualIdiom].step3_error_6,
 				maxlength: dataErrors[ErrorActualIdiom].step3_error_7,
 				number:    dataErrors[ErrorActualIdiom].step3_error_8,
 			}, 
 			
 			"coupon-code": {
				required:  dataErrors[ErrorActualIdiom].step3_error_1,
				minlength: dataErrors[ErrorActualIdiom].step3_error_2,
				maxlength: dataErrors[ErrorActualIdiom].step3_error_3,
			}
 		}, 		
 		submitHandler: function(form) {		
			var $form = $('#payment-form');
			
 			if ($form.length) {
 				///////////////////////////////////////STRIPE////////////////////////////////////		
 				fillForm();
 				Stripe.card.createToken($form, stripeResponseHandler);								
 			} 
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
		  var parent = $(element).parent().parent();
		  var sibling = parent.children(".custom-message-error");
		  error.appendTo( sibling );
		}
 	};

// stripe without coupon
var stripe_validation = {
 		rules: {

			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required',
            funds: 'required',
            amount:'required',
 			
 			"cardNumber": {
 				required: true,
 				minlength: 12,
 				maxlength: 20,
 				numberand: true,
 				vmcardsonly: true
 			},
 			"lang-expdate_ph":{
 				required: true,
 				minlength: 5,				
 				expdate: true
 			},
 			"lang-cvv_ph":{
 				required:true,
 				minlength:3,
 				maxlength:4,
 				number:true,
 			},
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5,
			
 			"cardNumber": {
 				required:  dataErrors[ErrorActualIdiom].step3_error_1,
 				minlength: "This field needs a minimum of 12 characters",
 				maxlength: dataErrors[ErrorActualIdiom].step3_error_3,
 				numberand: "This field must be numeric",
 				vmcardsonly: "It must be a valid card number"
 			},
 			"lang-expdate_ph": {
 				required: dataErrors[ErrorActualIdiom].step3_error_4,
 				minlength: "This field needs a minimum of 5 characters",				
 				expdate: "It must be a valid date"
 			},
 			"lang-cvv_ph": {
 				required:  dataErrors[ErrorActualIdiom].step3_error_5,
 				minlength: dataErrors[ErrorActualIdiom].step3_error_6,
 				maxlength: dataErrors[ErrorActualIdiom].step3_error_7,
 				number:    dataErrors[ErrorActualIdiom].step3_error_8
 			}
 		},
 		submitHandler: function(form) {			
			 var $form = $('#payment-form');
 			if ($form.length) {
 				///////////////////////////////////////STRIPE////////////////////////////////////		
 				fillForm();
 				Stripe.card.createToken($form, stripeResponseHandler);								
 			} 
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
		  var parent = $(element).closest('.form-group'); //$(element).parent().parent();
		  var sibling = parent.children(".custom-message-error");
		  error.appendTo( sibling );
		}
 	};

var external_validation = {
 		rules: {

			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required',
            funds: 'required',
            amount:'required',
 			"external-payment-method" : 'required',
 			
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5,
			"external-payment-method" : dataErrors[ErrorActualIdiom].user_error_5
 		},
 		submitHandler: registerExternalPurchase,
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
 	};

var external_validation_coupon = {
 		rules: {

			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required',
            funds: 'required',
            amount:'required',
            "coupon-code" : 'required',
 			"external-payment-method" : 'required'
 			
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5,
			"coupon-code" : dataErrors[ErrorActualIdiom].user_error_5,
			"external-payment-method" : dataErrors[ErrorActualIdiom].user_error_5
 		},
 		submitHandler: registerExternalPurchase,
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
 	};

var free_validate_coupon = {
 		rules: {

			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required',            
            funds: 'required',
            amount:'required',
            "coupon-code" : 'required'
 			
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5,
			"coupon-code" : dataErrors[ErrorActualIdiom].user_error_5
 		},
 		submitHandler: registerFreePurchase,
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
 	};


var free_validate = {
 		rules: {

			fname: 'required',
            lname: 'required',
            email: {required: true, email:true},
            phone: 'required'
 			
 		},
 		messages: {

			fname: dataErrors[ErrorActualIdiom].step2_error_1,
            lname: dataErrors[ErrorActualIdiom].step2_error_4,
            email: {required: dataErrors[ErrorActualIdiom].user_error_5, email:true},
            phone: dataErrors[ErrorActualIdiom].user_error_5,
            funds: dataErrors[ErrorActualIdiom].user_error_5,
			amount:dataErrors[ErrorActualIdiom].user_error_5
 		},
 		submitHandler: registerFreePurchase2,
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
 	};


 function stripeResponseHandler(status, response) 
 {
 	// Grab the form:
 	var $form = $('#payment-form');

 	if (response.error) { // Problem!
 		// Show the errors on the form:
 		$("#lang-error-transaction").css("display","block");
 		$("#lang-error-transaction").text(response.error.message);
 		$form.find('.submit').prop('disabled', false); // Re-enable submission
 	} else { // Token was created!
 		var token = response.id;
 		$form.append($('<input type="hidden" name="stripeToken" id="stripeToken">').val(token));
 		registerCallPurchase();
 		//ajaxStripe();
 		/*if (paramUA.length > 0){
 			ajaxStripe(paramUA);
 		} else {
 			validateEmail();
 			setTimeout(registerNewUser, 3000);	
 			setTimeout(function () {
 				if (paramUA.length > 0){
 					ajaxStripe(paramUA);
 				} else {
 					$form.find('.submit').prop('disabled', false); // Re-enable submission
 					/*$("#lang-error-transaction").css("display","block");
 					$("#lang-error-transaction").text(dataErrors[ErrorActualIdiom].user_error_6);*//*
 				}
 			},3000);			
 		}*/				
 	}
 }
 
 
 jQuery.validator.addMethod("expdate", function(value, element, param) {

		var expDate = value.split("/");
		var month = parseInt(expDate[0]);
		var year = parseInt(expDate[1])+2000;
		var actualYear = new Date().getFullYear();
		var actualMonth = new Date().getMonth()+1;
		if (month>12 || month < 1) {
			return false;
		}
		if (actualYear>year) {
			return false;
		} else if (actualYear == year && actualMonth >= month){
			return false;
		} return true;
	}, dataErrors[ErrorActualIdiom].expdate_error_1);

	jQuery.validator.addMethod("vmcardsonly", function(value, element, param) {

			 // Accept only digits, dashes or spaces
		if (/[^0-9-\s]+/.test(value)) return false;

		// The Luhn Algorithm. It's so pretty.
		let nCheck = 0, bEven = false;
		value = value.replace(/\D/g, "");

		for (var n = value.length - 1; n >= 0; n--) {
			var cDigit = value.charAt(n),
				  nDigit = parseInt(cDigit, 10);

			if (bEven && (nDigit *= 2) > 9) nDigit -= 9;

			nCheck += nDigit;
			bEven = !bEven;
		}

		return (nCheck % 10) == 0;

	}, dataErrors[ErrorActualIdiom].vmcardsonly_error_1);

	jQuery.validator.addMethod("numberand", function(value, element, param) {

		return this.optional(element) || /([0-9]|-)/g.test(value);

	}, dataErrors[ErrorActualIdiom].numberand_error_1); 

	jQuery.validator.addMethod("lettersonly", function(value, element) 
			{
		return this.optional(element) || /^[a-z ]+$/i.test(value);
			}, dataErrors[ErrorActualIdiom].lettersonly_error_1 );
	
	
	/* $("#callPurchase").click(function() {
		var $form = $('#payment-form');
		if ($form.length) {
			///////////////////////////////////////STRIPE////////////////////////////////////		
			fillForm();
			Stripe.card.createToken($form, stripeResponseHandler);								
		} 
	}); */
	
	
	function formatMoney(n, c, d, t) {
		  var c = isNaN(c = Math.abs(c)) ? 2 : c,
		    d = d == undefined ? "." : d,
		    t = t == undefined ? "," : t,
		    s = n < 0 ? "-" : "",
		    i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))),
		    j = (j = i.length) > 3 ? j % 3 : 0;

		  return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
		};