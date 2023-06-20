$('.usersModule').addClass('active');
var txTable;
var path_ajax = '/scr-admin/';
var nameReport = '';
var userTable;
var deviceCodeId;
var isEventsPrint = false;
// Para usarla en el metodo registerNewUser
var validateEmailVar = false;

jQuery.validator.addMethod(
  'requiredAtLeast',
  function (value, element) {
    var fname = $('#fname').val().trim();
    var lname = $('#lname').val().trim();
    var email = $('#email').val().trim();
    var userId = $('#userId').val().trim();
    const phone = $('#phoneNumber').val();

    if (phone) {
      const value = phone.trim();
      return (
        fname.length > 2 ||
        lname.length > 2 ||
        email.length > 2 ||
        userId.length > 2 ||
        value.length > 2
      );
    } else {
      return (
        fname.length > 2 ||
        lname.length > 2 ||
        email.length > 2 ||
        userId.length > 2
      );
    }
  },
  'Please fill at least fill one of the input filters'
);

$('#users-filter-form').validate({
  rules: {
    fname: {
      minlength: 3,
      requiredAtLeast: true,
    },
    lname: {
      minlength: 3,
    },
    email: {
      minlength: 3,
    },
    userId: {
      minlength: 3,
    },
    phoneNumber: {
      minlength: 3,
      maxlength: 14,
    },
  },
  submitHandler: submitGenerateUserSearch,
  errorClass: 'has-error',
  validClass: 'has-success',
  highlight: function (element, errorClass, validClass) {
    $(element).addClass(errorClass).removeClass(validClass);
  },
  unhighlight: function (element, errorClass, validClass) {
    $(element).addClass(validClass).removeClass(errorClass);
  },
  errorPlacement: function (error, element) {
    var parent = $(element).parent();
    var sibling = parent.children('.custom-message-error');
    error.appendTo(sibling);
  },
});

$('#users-filter-form-lifecyle').validate({
  rules: {
    fname: {
      minlength: 3,
      requiredAtLeast: true,
    },
    lname: {
      minlength: 3,
    },
    email: {
      minlength: 3,
    },
    userId: {
      minlength: 3,
    },
    phoneNumber: {
      minlength: 3,
      maxlength: 14,
    },
  },
  submitHandler: submitGenerateUserSearchLifeCycle,
  errorClass: 'has-error',
  validClass: 'has-success',
  highlight: function (element, errorClass, validClass) {
    $(element).addClass(errorClass).removeClass(validClass);
  },
  unhighlight: function (element, errorClass, validClass) {
    $(element).addClass(validClass).removeClass(errorClass);
  },
  errorPlacement: function (error, element) {
    var parent = $(element).parent();
    var sibling = parent.children('.custom-message-error');
    error.appendTo(sibling);
  },
});

$('#generateUserSearch').click(function () {
  $('#users-filter-form').submit();
});

$('#generateUserSearchLifeCycle').click(function () {
  $('#users-filter-form-lifecyle').submit();
});

function submitGenerateUserSearch() {
  var filter = getUserFilter();
  var filterKeys = Object.keys(filter);
  $('#report-user-error-container').hide();

  if (filterKeys.length <= 0) {
    $('#report-user-error-container span').text(
      'You need at least fill one of the inputs filters'
    );
    $('#report-user-error-container').show();
    return;
  }

  $.ajax({
    type: 'POST',
    contentType: 'application/json; charset=UTF-8',
    url: path_ajax + 'search/api/userSearch',
    data: JSON.stringify(filter),
    dataType: 'json',
    timeout: 100000,
    beforeSend: function () {
      $('#report-user-error-container').hide();
      $('#generateUserSearch').attr('disabled', 'true');
    },
    success: function (data) {
      $('#generateUserSearch').removeAttr('disabled');
      if (data.code === '200') {
        generateUserReport(data.result);
        // activateBtns();
        userTable.on('draw', function () {
          // activateBtns();
        });
      } else if (data.code === '204') {
        $('#report-user-error-container span').text(data.msg);
        $('#report-user-error-container').show();
      } else if (data.code === '400') {
        $('#report-user-error-container span').text(data.msg);
        $('#report-user-error-container').show();
      }
    },
    error: function (e) {
      $('#generateUserSearch').removeAttr('disabled');
      console.log('ERROR: ', e);
    },
    done: function (e) {
      console.log('DONE');
    },
  });
}

function submitGenerateUserSearchLifeCycle() {
  var filter = getUserFilter();
  var filterKeys = Object.keys(filter);
  $('#report-user-error-container').hide();

  if (filterKeys.length <= 0) {
    $('#report-user-error-container span').text(
      'You need at least fill one of the inputs filters'
    );
    $('#report-user-error-container').show();
    return;
  }

  $.ajax({
    type: 'POST',
    contentType: 'application/json; charset=UTF-8',
    url: path_ajax + 'search/api/userSearchAllEvent',
    data: JSON.stringify(filter),
    dataType: 'json',
    timeout: 100000,
    beforeSend: function () {
      $('#report-user-error-container').hide();
      $('#generateUserSearch').attr('disabled', 'true');
    },
    success: function (data) {
      $('#generateUserSearch').removeAttr('disabled');
      if (data.code === '200') {
        generateUserReportLifecycle(data.result);
        // activateBtns();
        userTable.on('draw', function () {
          // activateBtns();
        });
        console.log(data);
      } else if (data.code === '204') {
        $('#report-user-error-container span').text(data.msg);
        $('#report-user-error-container').show();
      } else if (data.code === '400') {
        $('#report-user-error-container span').text(data.msg);
        $('#report-user-error-container').show();
      }
    },
    error: function (e) {
      $('#generateUserSearch').removeAttr('disabled');
    },
    done: function (e) {
      console.log('DONE');
    },
  });
}

function getUserFilter() {
  var filter = {};
  /*
	 * if ($("#fromDate").val().length==10 && $("#toDate").val().length==10){
	 * filter["created_at_min"] = convertFromDate(); filter["created_at_max"] =
	 * convertToDate(); } else { $("#fromDate").val(""); $("#toDate").val("") }
	 */

  if ($('#fname').val().length > 0) filter['first_name'] = $('#fname').val();
  if ($('#lname').val().length > 0) filter['last_name'] = $('#lname').val();
  if ($('#email').val().trim().length > 0) filter['email'] = $('#email').val().trim();
  if ($('#userId').val().length > 0) filter['userId'] = $('#userId').val();
  if ($('#phoneNumber').val() && $('#phoneNumber').val().length > 0)
    filter['phone_number'] = $('#phoneNumber').val();

  return filter;
}

function generateUserReport(dataResult) {
  $('.card-body').html(
    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
  );

  var stringReport = '';
  if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
    for (var i = 0; i < dataResult.length; ++i) {
      var signed = dataResult[i].signed
        ? "<a target='_blank' href='" + dataResult[i].signatureUrl + "'>Yes</a>"
        : 'No';
      var phoneNumber =
        dataResult[i].phoneNumber == '' || dataResult[i].phoneNumber == null
          ? '-'
          : dataResult[i].phoneNumber;
      var dateSign =
        dataResult[i].signedAt == 0
          ? '-'
          : convertDateEnglish(dataResult[i].signedAt);
      stringReport += '<tr>';
      stringReport += '<td>' + dataResult[i].userId + '</td>';
      stringReport += '<td>' + dataResult[i].email + '</td>';
      stringReport += '<td>' + dataResult[i].firstName + '</td>';
      stringReport += '<td>' + dataResult[i].lastName + '</td>';
      stringReport += '<td>' + phoneNumber + '</td>';
      stringReport += '<td>' + signed + '</td>';
      stringReport +=
        '<td>' + convertDateEnglish(dataResult[i].createdAt) + '</td>';
      stringReport += '<td>' + dateSign + '</td>';
      stringReport +=
        '<td>' +
        getTableActionBtnTemplate(
          dataResult[i].userId,
          dataResult[i].email,
          dataResult[i].firstName,
          dataResult[i].lastName,
          phoneNumber,
          dataResult[i].countryId,
          dataResult[i].preferredLanguage
        ) +
        '</td></tr>';
    }
  }

  var stringHead =
    '<thead><tr><th>User ID</th>' +
    '<th>Email</th>' +
    '<th>Name</th>' +
    '<th>Last Name</th>' +
    '<th>Phone</th>' +
    '<th>Signed</th>' +
    '<th>Created At</th>' +
    '<th>Signed at</th>' +
    '<th>Actions</th>' +
    '</tr></thead>' +
    "<tbody id='detailReport'></tbody>";
  $('#demo-datatables-2').html(stringHead);
  $('#detailReport').html(stringReport);
  datatablesRender();
}

function generateUserReportLifecycle(dataResult) {
  $('.card-body').html(
    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
  );

  var stringReport = '';
  if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
    for (var i = 0; i < dataResult.length; ++i) {
      const phoneNumber =
        dataResult[i].phoneNumber == '' || dataResult[i].phoneNumber == null
          ? '-'
          : dataResult[i].phoneNumber;
      stringReport += '<tr>';
      stringReport += '<td>' + dataResult[i].userId + '</td>';
      stringReport +=
        "<td><a href='" +
        path_ajax +
        'userDetail-' +
        dataResult[i].userId +
        "' >" +
        dataResult[i].email +
        '</a></td>';
      // stringReport += `<td>${dataResult[i].email}</td>`;
      stringReport += '<td>' + dataResult[i].firstName + '</td>';
      stringReport += '<td>' + dataResult[i].lastName + '</td>';
      stringReport += '<td>' + phoneNumber + '</td>';
      stringReport +=
        '<td>' + convertDateEnglish(dataResult[i].createdAt) + '</td>';
      stringReport +=
        '<td>' +
        `<a href="${path_ajax}userDetail-${dataResult[i].userId}">View profile</a>`;
      /*
		 * getTableActionBtnTemplate( dataResult[i].userId, dataResult[i].email,
		 * dataResult[i].firstName, dataResult[i].lastName, phoneNumber ) +
		 */
      ('</td></tr>');
    }
  }

  var stringHead =
    '<thead><tr><th>User ID</th>' +
    '<th>Email</th>' +
    '<th>Name</th>' +
    '<th>Last Name</th>' +
    '<th>Phone</th>' +
    '<th>Created At</th>' +
    '<th>Actions</th>' +
    '</tr></thead>' +
    "<tbody id='detailReport'></tbody>";
  $('#demo-datatables-2').html(stringHead);
  $('#detailReport').html(stringReport);
  datatablesRender();
}

	$('body').on('click', '#dropdownMenu', function(e) {
		const element = $(e.currentTarget);
		const id = element.data('id');
		const dropDownElement = $("#dropdown-menu-items-" + id);		
		const buttonElement = $("#dropdown-action-" + id);
		let safariAgent = navigator.userAgent.indexOf("Safari") > -1;
		let chromeAgent = navigator.userAgent.indexOf("Chrome") > -1;
		if ((chromeAgent) && (safariAgent)) {
            safariAgent = false;
		}
		if (safariAgent == false) {
			dropDownFixPosition(buttonElement, dropDownElement);
		}
	});
	
	function dropDownFixPosition(button, dropdown) {
		var dropDownTop;
		if (screen.width <= 430) {
			dropDownTop = button.position().top + button.outerHeight() + 212 - 130;
		}
		else {
			dropDownTop = button.position().top + button.outerHeight() + 175 -98;
		}
		dropdown.css('top', dropDownTop + "px");
		dropdown.css('right', "30px");
	}	

$(document).on('click', '.select-process', function (e) {
  e.preventDefault();
  var id = $(this).data('id');
  var modalId = $(this).data('id');
  var modalEmail = $(this).data('email');
  var modalName = $(this).data('fname');
  var modalLast = $(this).data('lname');
  var modalProcess = $(this).data('process');
  var userPhone = $(this).data('phone');
  var modalCountry = $(this).data('country');
  if (modalCountry === undefined) {
	  modalCountry = 231;
  }
  var modalLanguage = $(this).data('language');
  if ('editBtn' == modalProcess) {
    var modal = new EditModal(
      id,
      modalEmail,
      'edit-mail',
      modalName,
      modalLast,
      userPhone,
      modalCountry,
      modalLanguage
    );
    modal.init(); 
  }

  if ('editUser' == modalProcess) {
    var modal = new EditModal(
      id,
      modalEmail,
      'edit-user',
      modalName,
      modalLast,
      userPhone,
      modalCountry,
      modalLanguage
    );
    modal.init();
    listCountries(modalCountry);
    listLanguage(modalLanguage);
  }

  if ('requestTx' == modalProcess) {
    var modal = new TransactionModal(
      id,
      modalEmail,
      'request-transaction',
      modalName,
      modalLast
    );
    modal.init();
  }

  if ('requestAllTx' == modalProcess) {
    var modal = new TransactionModal(
      id,
      modalEmail,
      'request-all-transaction',
      modalName,
      modalLast
    );
    modal.init();
  }

  if ('userAccess' == modalProcess) {
    var modal = new UserAccessModal(
      id,
      modalEmail,
      'request-user-access',
      modalName,
      modalLast
    );
    modal.init();
  }

  if ('userChangePass' == modalProcess) {
    changePass(id, modalEmail);
  }
});

// Duplique la función por que no existia antes de abrir el modal y generaba
// error
// Todo lo anterior para actualizar la contraseña esta comentado por si se
// necesita revertir en un futuro
function changePass(id, email) {
  var data = { customerUserId: id, email: email };
  $.ajax({
    type: 'POST',
    contentType: 'application/json; charset=UTF-8',
    url: path_ajax + 'search/api/sendTemporyPasswodReset',
    data: JSON.stringify(data),
    dataType: 'json',
    timeout: 100000,
    beforeSend: function () {
      $('#action-btn-container-' + id).addClass('loading');
    },
    success: function (data) {
      console.log('SUCCESS: ', data);
      if (data.returnCode == 0) {
        $('#action-btn-container-' + id).removeClass('loading');
        $('#action-btn-container-' + id).addClass('success');
        setTimeout(function () {
          $('#action-btn-container-' + id).removeClass('success');
        }, 3000);
      } else {
        $('#action-btn-container-' + id).removeClass('loading');
        $('#action-btn-container-' + id).addClass('error');
        setTimeout(function () {
          $('#action-btn-container-' + id).removeClass('error');
        }, 3000);
      }
    },
    error: function (e) {
      $('#action-btn-container-' + id).removeClass('loading');
      $('#action-btn-container-' + id).addClass('error');
      setTimeout(function () {
        $('#action-btn-container-' + id).removeClass('error');
      }, 3000);
    },
  });
}

function getTableActionBtnTemplate(id, email, fname, lname, phone, countryId, langId) {
  var modify_email = rolesFunc.findIndex((v) => v.name == 'MODIFY_EMAIL');
  var btnEmail =
    modify_email != -1
      ? '<div class="select-process" value="" data-id="' +
        id +
        '" data-email="' +
        email +
        '" data-lname="' +
        lname +
        '" data-fname="' +
        fname +
        '" data-process="editBtn" data-toggle="modal" data-target="#myModal" alt="Edit user email" title="Edit user email"><span class="fa fa-envelope" style="padding-right: 5px; font-size: 1.3em; cursor:pointer;"></span></div>'
      : '';

  var modify_user = rolesFunc.findIndex((v) => v.name == 'MODIFY_USER_DATA');
  var btnUserData =
    modify_user != -1
      ? '<div class="select-process" data-id="' +
        id +
        '" data-email="' +
        email +
        '" data-lname="' +
        lname +
        '" data-fname="' +
        fname +
        '" data-phone="' +
        phone +
        '" data-country="' +
        countryId +
        '" data-language="' +
        langId +
        '" data-process="editUser" data-toggle="modal" data-target="#myModal" alt="Edit user information" title="Edit user information"><span class="fa fa-user" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span></div>'
      : '';

  var tx_query = rolesFunc.findIndex((v) => v.name == 'TX_QUERY');
  var btnTrans =
    tx_query != -1
      ? '<div  class="select-process" data-id="' +
        id +
        '" data-email="' +
        email +
        '" data-lname="' +
        lname +
        '" data-fname="' +
        fname +
        '" data-process="requestTx" data-toggle="modal" data-target="#myModal" alt="User transactions" title="See transactions"><span class="fa fa-credit-card" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span></div>'
      : '';

  var user_access = rolesFunc.findIndex((v) => v.name == 'USERS_ACCESS_QUERY');
  var btnUserAccess =
    user_access != -1
      ? '<div class="select-process" id="btua-' +
        id +
        '" data-id="' +
        id +
        '" data-email="' +
        email +
        '" data-lname="' +
        lname +
        '" data-fname="' +
        fname +
        '" data-process="userAccess" data-toggle="modal" data-target="#myModal"  alt="Show user access log" title="Show user access log"><span class="fa fa-address-book-o" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span></div>'
      : '';

  var changePass = rolesFunc.findIndex(
    (v) => v.name == 'SEND_EMAIL_PASSWORD_RESET'
  );
  var btnchangePass =
    changePass != -1
      ? `<div class="select-process"  id="btn-kick-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="userChangePass"  alt="Change Password" title="Change Password"><span class="fa fa fa-key" style="padding-right: 5px; font-size: 1.55em; cursor:pointer;" ></span></div>`
      : '';

  return (
    '<div id="action-btn-container-' +
    id +
    '" class="action-btn-container ">' +
    btnEmail +
    btnUserData +
    btnTrans +
    btnUserAccess +
    btnchangePass +
    '<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>' +
    '<div class="icons"><img class="success" src="resources/core/img/check-mark-new.svg" alt="Success"><img class="error" src="resources/core/img/cross.svg" alt="Error"></div>' +
    '</div>'
  );
}

function datatablesRender() {
  userTable = $('#demo-datatables-2').DataTable({
    dom:
      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
      "<'table-responsive'tr>" +
      "<'row'<'col-sm-6'i><'col-sm-6'p>>",
  });
  $('#demo-datatables-2_filter input').addClass('form-control');
}

function convertFromDate() {
  if ($('#fromDate').val().length == 10) {
    startdate = $('#fromDate').val() + ' 00:00:00';
    var a = startdate.split(' ');
    var d = a[0].split('-');
    var t = a[1].split(':');
    startdate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
    startdate = startdate.getTime();
    return startdate;
  }
  return '';
}

function convertToDate() {
  if ($('#toDate').val().length == 10) {
    enddate = $('#toDate').val() + ' 23:59:59';
    var a = enddate.split(' ');
    var d = a[0].split('-');
    var t = a[1].split(':');
    enddate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
    enddate = enddate.getTime();
    return enddate;
  }
  return '';
}

function convertDateEnglish(inputFormat, langId) {
  function pad(s) {
    return s < 10 ? '0' + s : s;
  }
  var d = new Date(inputFormat);
  return [pad(d.getMonth() + 1), pad(d.getDate()), d.getFullYear()].join('/');
}

function convertFullDateEnglish(inputFormat, langId) {
	  function pad(s) {
	    return s < 10 ? '0' + s : s;
	  }
	  var d = new Date(inputFormat);
	  return [pad(d.getMonth() + 1), pad(d.getDate()), d.getFullYear()].join('/')+' '+[pad(d.getHours()),pad(d.getMinutes()),pad(d.getSeconds())].join(':');
}

function EditModal(idUser, emailUser, userAction, fname, lname, userPhone, modalCountry, modalLanguage) {
  var id = idUser;
  var userName = fname;
  var userLname = lname || ' ';
  var email = emailUser;
  var action = userAction;
  var phone = userPhone || '';
  var userModalCountry = modalCountry;
  var userModalLanguage = modalLanguage;
  var validator;

  function getId() {
    return id;
  }

  function getEmail() {
    return email;
  }

  if (!isEventsPrint) {
    isEventsPrint = true;

    $(document).on('click', '#edit-user-cancel', 0, function () {
      $('.editModal').removeClass('confirm');
      $('#myModal').modal('hide');
    });

    $(document).on('click', '#edit-user-request', 0, function () {
      editEmailWithNoConfRequest();
    });

    $(document).on('click', '#edit-user-request-translate', 0, function () {
      transferTransactionToAnotherUser();
    });
  }

  function generateEditMailModal() {
    return `
		<div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Edit User Email</h4>
	      </div>
	      <div class="modal-body">
            <div class="isConfirm">
              <div class="title-edit">
                Change the email of the account, it will delete the data of the stored cards, are you sure to make this action?
              </div>
              <div class="buttons-edit">
                <button id="edit-user-cancel" type="button" class="btn btn-default">Cancel</button>
                <button id="edit-user-request" type="button" class="btn btn-primary">Confirm</button>
              </div>
            </div>

            <!-- Translate -->
            <div class="isConfirmTranslate">
              <div class="title-edit">
                The email <span class="updateEmail"></span> already exists. Are you sure you want to transfer the transaction to user, <span class="updateEmail"></span>?
              </div>
              <div class="buttons-edit">
                <button id="edit-user-cancel" type="button" class="btn btn-default">Cancel</button>
                <button id="edit-user-request-translate" type="button" class="btn btn-primary">Confirm</button>
              </div>
            </div>
          <!-- Translate -->

	        <form id="edit-${id}-mail" class="no-confirm">
	        	<div class="mail-container">
	        	<label for"mail">
	        		New Email:
	        	</label>
	        	<input type="email" id="mail" class="form-control" name="mail" value="${email}" required>
	        	<span class="error-message"></span>
	        	</div>
	        	<div class="mail-confirm-container">
	        	<label for"mail2">
	        		Confirm New Email:
	        	</label>
	        	
	        		<input type="email" id="mail2" class="form-control" name="mail2" value="" required>
	        		<span class="error-message"></span>
	        	</div>
	        	<div class="confirm-code-container" style="display:none;">
	        	<label for"code-val">
	        		Please insert the code sent to the new email to validate:
	        	</label>
	        	
	        		<input type="text" id="code-validation" class="form-control" name="code-validation" value="" required>
	        		<span class="error-message"></span>
	        	</div>
	        </form>
	        <div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  <a href="#" class="alert-link success-msg-alert"></a>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
				<a href="#" class="alert-link error-msg-alert"></a>
			</div>
	      </div>
	      <div class="modal-footer footerBtn">
	        <button id=""  type="button" class="btn btn-default destroy-modal" data-dismiss="modal">Close</button>
	        <button id="edit-mail" type="submit" class="btn btn-primary">Submit changes</button>
	      </div>
	       <div class="modal-footer footerValidate" style="display:none;">
	        <button id=""  type="button" class="btn btn-default destroy-modal" data-dismiss="modal">Cancel</button>
	        <button id="validate-code-btn" type="submit" class="btn btn-primary">Validate code</button>
	      </div>
	       <div class="modal-footer footerLoader" style="display:none;">
	      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
	      </div>
	    </div><!-- /.modal-content -->	`;
  }

  function transferTransactionToAnotherUser() {
    var data = {};
    data['emailOrigin'] = email;
    data['emailToTransfer'] = $('#mail').val().trim();

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/transferTransactionToAnotherUser',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function (data) {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.footerValidate').hide();
        $('.alert-danger').hide();
        $('.alert-danger').hide();
        $('.isConfirmTranslate').hide();
        $('.isConfirm').show();
      },
      success: function (resp) {
        if (resp.returnCode == 0) {
          $('.footerLoader').hide();
          $('.footerValidate').hide();
          $('.footerBtn').show();
          $('#edit-mail').hide();
          $('.alert-success').text('The email was succesfully edited!');
          $('.alert-success').show();
          $('#generateUserSearch').click();
        } else {
          $('.footerLoader').hide();
          $('.footerValidate').hide();
          $('.footerBtn').show();
          $('.alert-danger').text(resp.returnMessage);
          $('.alert-danger').show();
        }
      },
    }).fail(function (errMsg) {
      $('.footerLoader').hide();
      $('.footerValidate').hide();
      $('.footerBtn').show();
      $('.alert-danger').text(
        'An error has ocurred, please contact the admin. code: 404'
      );
      $('.alert-danger').show();
    });
  }

  function generateEditUserModal() {
    return `
		<div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Edit User registered</h4>
	      </div>
	      <div class="modal-body">
	        <form id="edit-${id}-user">
	        	<div class="email-input-container">
		        	<label for"mail">
		        		Email:
		        	</label>
		        	<input type="email" id="mail" class="form-control" name="mail" value="${email}" readonly required>
	        	</div>
	        	<div class="name-container">
					<label for"mail">
		        		Name:
		        	</label>
		        	<input type="text" id="fname2" class="form-control" name="fname2" value="${userName}"  required>
		        	<span class="error-message"></span>
	        	</div>
	        	<div class="lname-container">
		        	<label for"mail">
		        		Last Name:
		        	</label>
		        	<input type="text" id="lname2" class="form-control" name="lname2" value="${userLname}"  required>
		        	<span class="error-message"></span>
	        	</div>
	        	<div class="phone-container">
		        	<label for"phone">
		        		Phone:
		        	</label>
		        	<input type="tel" id="phone" class="form-control"  data-phone="${phone}" name="phone" value="${phone}"  
		        		required
            			minlength="5"
            			maxlength="15"
            		/>            
		        	<span class="error-message"></span>
	        	</div>
	        	<div class="country-container">  
	        		<label for"country">
		        		Country:
		        	</label>  
					<select id="listCountry" name='listCountry' class="custom-select form-control">
					</select>
		        	<span class="error-message"></span>
	        	</div>
	        	<div class="language-container">  
	        		<label for"language">
		        		Language:
		        	</label>  
					<select id="listLanguage" name='listLanguage' class="custom-select form-control">
						<option value="0">Select</option>
					</select>
		        	<span class="error-message"></span>
	        	</div>
	        </form>
	        <div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  <a href="#" class="alert-link"></a>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
				<a href="#" class="alert-link"></a>
			</div>
	      </div>
	      <div class="modal-footer footerBtn">
	        <button id="destroy-modal"  type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button id="edit-information" type="submit" class="btn btn-primary">Save changes</button>
	      </div>
	       <div class="modal-footer footerLoader" style="display:none;">
	      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
	      </div>
	    </div><!-- /.modal-content -->	`;
  }

  function editUserInformation() {
	  let email = $('#mail').val().trim().toString();
	  let langId = parseInt($('#listLanguage').val());
    let data = {
      userId: id,
      fname: $('#fname2').val().toString(),
      lname: $('#lname2').val().toString(),
      email: email,
      phone: $('#phone').val().toString(),
      countryId: parseInt($('#listCountry').val()),
      nameBefore: userName.toString(),
      lnameBefore: userLname.toString(),
      phoneBefore: `${userPhone}`,
      countryIdBefore: userModalCountry,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/editUserRegistered',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.alert-danger').hide();
      },
      success: function (data) {
        if (data.returnCode == '0') {
          const userInfo = data.result.customer_user;
          console.log(userInfo);
          $('#user-phone').text(userInfo.phoneNumber);
          $('#user-fullname').text(
            userInfo.firstName + ' ' + userInfo.lastName
          );
          $('#user-country').text(userInfo.country);
          const input = $("div [data-process='editUser']");
          input.data('phone', userInfo.phoneNumber);
          input.data('fname', userInfo.firstName);
          input.data('lname', userInfo.lastName);
          input.data('country', userInfo.countryId);
          input.data('language', langId);
          
          if (userInfo.hasOwnProperty('statusSales')) {
        	  if (statusSales != null && statusSales === 1) {
            	  $('#user-phone').addClass("phone-decoration")
            	  $('#dnc-phone').show();
              }
              else {
            	  $('#user-phone').removeClass("phone-decoration")
            	  $('#dnc-phone').hide();
              }
          }
          
          
          if (langId != 0)
        	  saveLanguage(email, langId);
          
          const items = data.result.listBusinessUserSupportHistory;
          const bitacora = $('#bitacora-list');
          bitacora.empty();

          $.each(items, function (index, item) {
            const html = `<li style="margin-bottom: 10px;">											
					<strong>${item.commentdate} - Operator: ${item.emailop}</strong><br>
					<span>${item.comment}</span>																									
				</li>`;
            bitacora.append(html);
          });

          $('.footerLoader').hide();
          $('.footerBtn').show();
          $('#edit-information').hide();
          $('.alert-success').text('User information edited succesfully!');
          $('.alert-success').show();
          $('#generateUserSearch').click();
        } else if (data.returnCode == '204') {
        	if (userModalLanguage != langId && langId != 0) {
        		saveLanguage(email, langId);
        		const input = $("div [data-process='editUser']");
  	    		input.data('language', langId);
        		$('.footerLoader').hide();
        		$('.footerBtn').show();
        		$('#edit-information').hide();
                $('.alert-success').text('User information edited succesfully!');
                $('.alert-success').show();
                $('#generateUserSearch').click();
        	}
        }
        else {
          $('.footerLoader').hide();
          $('.footerBtn').show();
          $('.alert-danger').text(data.returnMessage);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('ERROR: ', e);
        $('.footerLoader').hide();
        $('.footerBtn').show();
        $('.alert-danger').text(
          'Unexpected error has ocurred, please contact support. code:404'
        );
        $('.alert-danger').show();
      },
    });
  }
  
  function saveLanguage(email, langId) {
	  const data = {
			email : email,
  	    	langId: langId
  	  };
  	  $.ajax({
  	    	type: 'POST',
  	    	contentType: 'application/json; charset=UTF-8',
  	    	url: path_ajax + 'search/api/updatePreferredLanguage',
  	    	data: JSON.stringify(data),
  	    	dataType: 'json',
  	    	timeout: 100000,
  	    	success: function (data) {
  	    		console.log("Success");
  	    	},
  	    	error: function (e) {
  	    		console.log('Error: ', e);
  	    	}
  	  });
  }

  function updateTags(userId, tags) {
    var data = {};
    data['userId'] = userId;
    data['tags'] = tags; // Example : {"xxxxxxxx", "xxxxxxxxxxxxxxx",
							// "xxxxxxx"}
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/updateTags',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {},
      success: function (data) {
        console.log('SUCCESS: ', data);
      },
      error: function (e) {},
    });
  }

  function submitEditData() {
    var data = {
      lname: userLname,
      fname: userName,
      email: $('#mail').val().trim(),
      id: id,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/validateDevice',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.alert-danger').hide();
      },
      success: function (data) {
        console.log('SUCCESS: ', data);
        if (data.returnCode == 0) {
          $('.footerLoader').hide();
          $('.footerValidate').show();
          deviceCodeId = data.result.validation_code.id;
          activateCodeValidation();
        } else {
          $('.footerLoader').hide();
          $('.footerBtn').show();
          $('.alert-danger').text(data.returnMessage);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('ERROR: ', e);
        $('.footerLoader').hide();
        $('.footerBtn').show();
        $('.alert-danger').text(
          'An error has ocurred, please contact the admin. code: 404'
        );
        $('.alert-danger').show();
      },
    });
  }

  function init() {
    $('#myModal').removeClass('txModal');
    $('#myModal').addClass('editModal');
    $('.editModal').removeClass('confirm');

    if (action == 'edit-mail') {
      var template = generateEditMailModal();
      $('#modal-content-container').html(template);
      $('.isConfirmTranslate').hide();

      var editTrigger = emailConfirmActivate
        ? submitEditData
        : editEmailWithNoConf;

      validator = $('#edit-' + id + '-mail').validate({
        rules: {
          mail: {
            required: true,
            email: true,
          },
          mail2: {
            required: true,
            email: true,
            equalTo: '#mail',
          },
        },
        submitHandler: editTrigger,
        errorClass: 'has-error',
        validClass: 'has-success',
        highlight: function (element, errorClass, validClass) {
          $(element).addClass(errorClass).removeClass(validClass);
        },
        unhighlight: function (element, errorClass, validClass) {
          $(element).addClass(validClass).removeClass(errorClass);
        },
        errorPlacement: function (error, element) {
          var parent = $(element).parent();
          var sibling = parent.children('.error-message');
          error.appendTo(sibling);
        },
      });

      $('#edit-mail').click(function () {
        console.log('works');
        $('#edit-' + id + '-mail').submit();
      });
      document.querySelector('#mail2').onpaste = function (e) {
        e.preventDefault();
        return;
      };
    } else {
      var template = generateEditUserModal();
      $('#modal-content-container').html(template);

      $('#edit-' + id + '-user').validate({
        rules: {
          email: {
            required: true,
            email: true,
          },
          fname2: {
            required: true,
          },
          lname2: {
            required: true,
          },
        },
        submitHandler: editUserInformation,
        errorClass: 'has-error',
        validClass: 'has-success',
        highlight: function (element, errorClass, validClass) {
          $(element).addClass(errorClass).removeClass(validClass);
        },
        unhighlight: function (element, errorClass, validClass) {
          $(element).addClass(validClass).removeClass(errorClass);
        },
        errorPlacement: function (error, element) {
          var parent = $(element).parent();
          var sibling = parent.children('.error-message');
          error.appendTo(sibling);
        },
      });

      $('#edit-information').click(function () {
        $('#edit-' + id + '-user').submit();
      });
    }
  }

  // function to validate code: is inside Modal Module

  function activateCodeValidation() {
    document.querySelector('#mail').setAttribute('readonly', '');
    document.querySelector('#mail2').setAttribute('readonly', '');
    $('.confirm-code-container').show();
    validator.destroy();
    $('#edit-' + id + '-mail').validate({
      rules: {
        mail: {
          required: true,
          email: true,
        },
        mail2: {
          required: true,
          email: true,
          equalTo: '#mail',
        },
        'code-validation': {
          required: true,
          minlength: 6,
          maxlength: 6,
        },
      },
      submitHandler: validateCode,
      errorClass: 'has-error',
      validClass: 'has-success',
      highlight: function (element, errorClass, validClass) {
        $(element).addClass(errorClass).removeClass(validClass);
      },
      unhighlight: function (element, errorClass, validClass) {
        $(element).addClass(validClass).removeClass(errorClass);
      },
      errorPlacement: function (error, element) {
        var parent = $(element).parent();
        var sibling = parent.children('.error-message');
        error.appendTo(sibling);
      },
    });

    $('#validate-code-btn').click(function () {
      $('#edit-' + id + '-mail').submit();
    });
  }

  function editEmailWithNoConfRequest() {
    $('.editModal').removeClass('confirm');
    let data = {
      emailNew: $('#mail').val().trim(),
      userId: id,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/editWithNoConfirm',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.footerValidate').hide();
        $('.alert-danger').hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('.footerLoader').hide();
          $('.footerValidate').hide();
          $('.footerBtn').show();
          $('#edit-mail').hide();
          $('.alert-success').text('The email was succesfully edited!');
          $('.alert-success').show();
          $('#generateUserSearch').click();
          // Update data-email
          const email = $('#mail').val().trim();
          $('span#user-email').text(email);
          $('.select-process').data('email', email);
        } else if (data.returnCode == '-9') {
          $('.footerLoader').hide();
          $('.footerBtn').hide();
          $('.footerValidate').hide();
          $('.alert-danger').hide();
          $('.isConfirm').hide();
          $('.no-confirm').hide();
          $('.isConfirmTranslate').show();
          $('.updateEmail').html($('#mail').val().trim());
        } else {
          $('.footerLoader').hide();
          $('.footerValidate').hide();
          $('.footerBtn').show();
          $('.alert-danger').text(data.returnMessage);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('ERROR: ', e);
        $('.footerLoader').hide();
        $('.footerValidate').hide();
        $('.footerBtn').show();
        $('.alert-danger').text(data.returnMessage);
        $('.alert-danger').show();
      },
    });
  }

  function editEmailWithNoConf() {
    $('.editModal').addClass('confirm');
    // editEmailWithNoConfRequest();
  }

  // function which ask if the code validation inserted is valid
  function validateCode() {
    let data = {
      validationCode: $('#code-validation').val(),
      validationId: deviceCodeId,
      userId: id,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/validateCodeAndEdit',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.footerValidate').hide();
        $('.alert-danger').hide();
      },
      success: function (data) {
        console.log('SUCCESS: ', data);
        if (data.returnCode == 0) {
          $('.footerLoader').hide();
          $('.footerValidate').hide();
          $('.footerBtn').show();
          $('#edit-mail').hide();
          $('.alert-success').text('The email was succesfully edited!');
          $('.alert-success').show();
          $('#generateUserSearch').click();
        } else {
          $('.footerLoader').hide();
          $('.footerValidate').show();
          $('.alert-danger').text(data.returnMessage);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('ERROR: ', e);
        $('.footerLoader').hide();
        $('.footerValidate').show();
        $('.alert-danger').text(data.returnMessage);
        $('.alert-danger').show();
      },
    });
  }

  return {
    init: init,
    getId: getId,
    getEmail: getEmail,
  };
}

function TransactionModal(idUser, emailUser, action, fname, lname) {
  var id = idUser;
  var email = emailUser;
  var action$ = action;
  var firstName = fname;
  var lastName = lname;
  var dataTx;

  /*
	 * $(document).on("click","",function(e) { e.preventDefault(); var modalId =
	 * $(this).data('id'); var modalEmail = $(this).data('email'); var modalName =
	 * $(this).data('fname'); var modalLast = $(this).data('lname'); var
	 * modalProcess = $(this).data('process'); var idTx = $(this).data('txid');
	 * processSelectModal1Action( modalId, modalEmail, modalName, modalLast,
	 * modalProcess, idTx); });
	 */

  function init() {
    $('#myModal').removeClass('editModal');
    $('#myModal').addClass('txModal');
    $('#modal-content-container').html('');

    if (action === 'request-transaction') requestUserTransactions();
    else getAllUserTransactions();
  }

  function generateTransactionModalTemplate(data, showDateAccess = false) {
    // https://www.jqueryscript.net/time-clock/Super-Tiny-jQuery-HTML5-Date-Picker-Plugin.html
    var dataJoined = data.join('');
    const dateAccess = showDateAccess ? `<th style="min-width:100px;">Access from</th><th style="min-width:100px">Access to</th>` : '';
    return `
		<div class="modal-content transaction-body">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Transaction from ${firstName} ${lastName} - ${email}</h4>
	      </div>
			  <div class="modal-body">
				<div id="actions-transaction-container">
				</div>
				<table id="user-transaction-table" class="table-striped" style="width:100%">
					<thead>
						<tr>
							<th>ID</th>
							<th>Date</th>
							<th>Event</th>
							${dateAccess}													
							<th>Credit Card</th>
							<th>Card brand</th>
							<th>Amount</th>
							<th>Status</th>
							<th>Internal ID</th>
							<th>Payment Info</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						${dataJoined}
					</tbody>
				</table>
			 </div>
	       	<div class="modal-footer footerLoader" style="display:none;">
	      		 <button id="destroy-modal"  type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
	    </div>
	    <div class="modal-content signature-body" style="display:none;"></div>
      <div class="modal-content changeEmail-body" style="display:none;"></div>
		`;
  }

  function processSelectModal1Action(
    modalId,
    modalEmail,
    modalName,
    modalLast,
    modalProcess,
    idTx,
    lifeCycle = false
  ) {
    if ('magicLink' == modalProcess) {
      sendMagicLink(modalEmail, id, idTx);
    }
    
    if ('magicLinkOperator' == modalProcess) {
		sendMagicLinkToOperator(modalEmail, id, idTx);
	}
	
	console.log('Process: ', modalProcess);

    if ('resendBtns' == modalProcess) {
      resendReceipt(id, idTx, idTx);
    }

    if ('resendOpBtns' == modalProcess) {
      resendReceiptOp(id, idTx, idTx);
    }

    if ('invalidateBtns' == modalProcess) {
      invalidateTxModalComment(id, idTx, idTx);
    }

    if ('invalidateTxWithRefund' == modalProcess) {
      invalidateTxModalComment(id, idTx, idTx, true);
    }
    
    if ('expireAccess' == modalProcess) {
		expireAccess(id, idTx);
	}

    if ('kickUser' == modalProcess) {
      kickUser(id, idTx, idTx);
    }

    if ('userChangePass' == modalProcess) {
      changePass(id, modalEmail, idTx, idTx);
    }

    if ('signApprove' == modalProcess) {
      var modal = new ApproveSignModal(
        id,
        modalEmail,
        'approve-signature',
        modalName,
        modalLast,
        idTx
      );
    }

    if ('changeEmail' == modalProcess) {
      var modal = new changeEmail(
        id,
        modalEmail,
        'changeEmail',
        modalName,
        modalLast,
        idTx,
        lifeCycle
      );
    }
          
  }

  	function refreshTable() {	
    	if (action === 'request-transaction') {
			requestUserTransactions();
		} 
    	else {
			getAllUserTransactions();
		}
	}
	
	$('#myModal').off('hidden.bs.modal').on('hidden.bs.modal', function () {
		$('#myModal .modal-dialog').html('');
		refreshLifeCycle();
	})
	
	function refreshLifeCycle() {
		const data = { userId: id };
		$.ajax({
			type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: path_ajax + "search/api/findLifeCycleByUser",
		    data: JSON.stringify(data),
		    dataType: "json",
			success: (data) => {
				if (data.returnCode == 0) {
					$('.wizard').empty();
					const { userLifecycle, lifeCycle } = data.result;
					userLifecycle.forEach((item, index) => {
						const name = lifeCycle[index];
						console.log(name);
						const itemText = item.label;
						const a = 
							`<a class="item-life-cycle ${itemText != '' && item.condition_color == 0 ? 'done' : ''} ${itemText != '' && item.condition_color == 1 ? 'pending' : ''}">${name}</a>`;
						$('.wizard').append(a);
					})
				}
			}
		})
	}

  // function changePass(id, email, idTx, idTx)
  // {
  // //ROGER FUNCION QUE SE DISPARA AL DARLE CLICK
  // var data = { customerUserId: id, email: email };
  // $.ajax({
  // type: "POST",
  // contentType: "application/json; charset=UTF-8",
  // url: path_ajax + "search/api/sendTemporyPasswodReset", //ROGER COLOCAR LA
	// RUTA
  // data: JSON.stringify(data),
  // dataType: "json",
  // timeout: 100000,
  // beforeSend: function()
  // {
  // $('.select-modal-1-container-'+id).addClass("loading");
  // },
  // success: function(data)
  // {
  // console.log("SUCCESS: ", data);
  // if (data.returnCode == 0)
  // {
  // $('.select-modal-1-container-'+id).removeClass("loading");
  // $('.select-modal-1-container-'+id).addClass("success");
  // setTimeout(function()
  // {
  // $('.select-modal-1-container-'+id).removeClass("success");
  // }, 3000);
  // }
  // else
  // {
  // $('.select-modal-1-container-'+id).removeClass("loading");
  // $('.select-modal-1-container-'+id).addClass("error");
  // setTimeout(function()
  // {
  // $('.select-modal-1-container-'+id).removeClass("error");
  // }, 3000);
  // }
  // },
  // error: function(e)
  // {
  // alert(
  // "An error ocurred during the request. Please try later or contact
	// support"
  // );
  // $('.select-modal-1-container-'+id).removeClass("loading");
  // $('.select-modal-1-container-'+id).addClass("error");
  // setTimeout(function()
  // {
  // $('.select-modal-1-container-'+id).removeClass("error");
  // }, 3000);
  // }
  // });

  // }

  function invalidateTxModalComment(id, idTx, elmId, refund = false) {
    var modal = $('.modal-description');
    var modal_ = $('#myModal');
    var container = modal.find(
      '.modal-container .modal-content .modal-description-content'
    );
	container.empty();
    container.append(
    	`<div class='container-input-description'>
      		<div class='title'>Description</div>
      		<textarea class='form-control' id='text-description-invalidate' name='invalidate-description'></textarea>
      		<div class='button'> 
      			<button id='btn-send' data-id=${id} data-idTx=${idTx} data-elmId=${elmId} data-refund=${refund} class='btn btn-primary' disabled>
      				Send
      			</button>
      		</div>
      	</div>`
    );

    modal.addClass('active');
    modal_.css({ display: 'none' });
  }
  
    $('body').on('input keydown paste', '#text-description-invalidate', function() {
		const disabled = $(this).val().length < 2;	
		$('#btn-send').prop('disabled', disabled);				
	})
  
  	$(document).off('click', '#btn-send').on('click', '#btn-send', e => {
		e.preventDefault();
		const id = $(e.target).attr('data-id');
    	var idTx = $(e.target).attr('data-idTx');
    	var elmId = $(e.target).attr('data-elmId');
    	var description = $('#text-description-invalidate').val();
    	const refund = $(e.target).data('refund');

    	closeModalInvalidateTxtComment();
    	if (refund) {
      		cancelTx(id, idTx, elmId, description);
    	} else {
      		invalidateTx(id, idTx, elmId, description);
    	}
	})

  function closeModalInvalidateTxtComment() {
    var modal = $('.modal-description');
    var modal_ = $('#myModal');
    var container = modal.find(
      '.modal-container .modal-content .modal-description-content'
    );

    container.empty();
    modal.removeClass('active');
    modal_.css({ display: 'block' });
  }

  $(document).on('click', '.modal-close-description', function () {
    closeModalInvalidateTxtComment();
  });
  
  /*
	 * $(document).one('click', '.btn-send-description', function () { var id =
	 * $(this).attr('data-id'); var idTx = $(this).attr('data-idTx'); var elmId =
	 * $(this).attr('data-elmId'); var description =
	 * $('#text-description-invalidate').val(); const refund =
	 * $(this).data('refund');
	 * 
	 * closeModalInvalidateTxtComment(); if (refund) { cancelTx(id, idTx, elmId,
	 * description); } else { invalidateTx(id, idTx, elmId, description); } });
	 */
  	$('body').off('click', '#edit-access-from');	
  	$('body').on('click', '#edit-access-from', function(e){
		e.preventDefault();
		const id = $(this).data('id');
		
		const inputElement = '#edit-access-from-' + id;
		$(inputElement).removeClass('hide');
		
		const textElement = '#text-access-from-' + id;
		$(textElement).addClass('hide');		
					
		// const minDate = $(this).data('min-date');
		const pickerElement = '#access-from-' + id;
		$( pickerElement ).datepicker();			
	})
	$('body').off('click', '#accept-access-from');	
	$('body').on('click', '#accept-access-from', function(e){
		e.preventDefault();
		
		const id = $(this).data('id');
		const userId = $(this).data('user-id');
		const inputElement = '#edit-access-from-' + id;
		const value = $(inputElement).datepicker('getDate');		
		const date = new Date(value);		
		const textElement = '#text-access-from-' + id;	
		
		$(textElement).removeClass('hide');		
		$(inputElement).datepicker('hide');
		$(inputElement).datepicker( "destroy" );
		$(inputElement).addClass('hide');

		// Show loading
		$(textElement + ' .loading').show();
		$(textElement + ' span').hide();
		$(textElement + ' a').hide();
					
		const now = dayjs();
		const fullDate = dayjs(value).hour(now.hour()).minute(now.minute()).second(now.second());
							
		const payload = {			
			txId: id,
			accessDateFrom: fullDate.valueOf(), // $(this).data('access-from'),
			accessDateToUpdate: null, // fullDate.valueOf(), //
										// date.getTime(),
			userId: userId,
			timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone,
			update: 'from'
		}
						
		$.ajax({
			type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: path_ajax + "search/api/updateToDateUserAccess",
		    data: JSON.stringify(payload),
		    dataType: "json",
		    beforeSend: () => {
				toastr.options = {
					"newestOnTop": true,
					"positionClass": "toast-bottom-center",			
				}										
		    },
			success: (data) => {
				if (data.returnCode == 0) {
					// Display Value
					$(textElement + ' span').html(date.toISOString().split('T')[0]);
					toastr.success('Record updated successfully.');					
					
					if (action === 'request-transaction') requestUserTransactions();
    				else getAllUserTransactions();
    																			
				} else {
					toastr.error(data.returnMessage);
				}
			},
			complete: () => {				
				$(textElement + ' .loading').hide();
				$(textElement + ' span').show();
				$(textElement + ' a').show();				
			},
			error: (err) => {
				toastr.error(err.returnMessage);
			}
		});
	
	})
  
	$('body').off('click', '#edit-access-to');
  	$('body').on('click', '#edit-access-to', function(e){
		e.preventDefault();
		const id = $(this).data('id');
		
		const inputElement = '#edit-access-' + id;
		$(inputElement).removeClass('hide');
		
		const textElement = '#text-access-to-' + id;
		$(textElement).addClass('hide');		
					
		// const minDate = $(this).data('min-date');
		const pickerElement = '#access-to-' + id;
		$( pickerElement ).datepicker();			
	})   
	$('body').off('click', '#accept-access-to');
	$('body').on('click', '#accept-access-to', function(e){
		e.preventDefault();
		
		const id = $(this).data('id');
		const userId = $(this).data('user-id');
		const inputElement = '#edit-access-' + id;
		const value = $(inputElement).datepicker('getDate');		
		const date = new Date(value);		
		const textElement = '#text-access-to-' + id;	
		
		$(textElement).removeClass('hide');		
		$(inputElement).datepicker('hide');
		$(inputElement).datepicker( "destroy" );
		$(inputElement).addClass('hide');
		// Show loading
		$(textElement + ' .loading').show();
		$(textElement + ' span').hide();
		$(textElement + ' a').hide();
					
		const now = dayjs();
		const fullDate = dayjs(value).hour(now.hour()).minute(now.minute()).second(now.second());
							
		const payload = {
			txId: id,
			accessDateFrom: $(this).data('access-from'),
			accessDateToUpdate: fullDate.valueOf(), // date.getTime(),
			userId: userId,
			timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone,
			update: 'to'
		}
			
		$.ajax({
			type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: path_ajax + "search/api/updateToDateUserAccess",
		    data: JSON.stringify(payload),
		    dataType: "json",
		    beforeSend: () => {
				toastr.options = {
					"newestOnTop": true,
					"positionClass": "toast-bottom-center",			
				}										
		    },
			success: (data) => {
				if (data.returnCode == 0) {
					// Display Value
					$(textElement + ' span').html(date.toISOString().split('T')[0]);
					toastr.success('Record updated successfully.');
					/*
					 * setTimeout(() => { window.location.reload(); }, 3000)
					 */
					
					if (action === 'request-transaction') requestUserTransactions();
    				else getAllUserTransactions();
    																			
				} else {
					toastr.error(data.returnMessage);
				}
			},
			complete: () => {				
				$(textElement + ' .loading').hide();
				$(textElement + ' span').show();
				$(textElement + ' a').show();				
			},
			error: (err) => {
				toastr.error(err.returnMessage);
			}
		});
						
	})
  
  
  
  function processDataTransactions(data, showAccessDate = false) {
		if (data.code == 0) {
          var formatedData = data.result.map((r) => {
            var transDate = convertDateEnglish(r.txDate);
            var accessDateFrom =
              r.accessDateTxt == null
                ? convertFullDateEnglish(r.accessDateFrom)
                : r.accessDateTxt;
            var accessDateTo =
              r.accessDateTxt == null
                ? r.accessDateTo != null
                  ? convertFullDateEnglish(r.accessDateTo)
                  : ''
                : r.accessDateTxt;
            var is_scheduled = r.isScheduled ? 'Yes' : 'No';
            var cardMasked =
              r.cardMasked == 'Unknown' || r.cardMasked == null
                ? '--'
                : r.cardMasked;
            var cardBrand =
              r.cardBrand == 'Unknown' || r.cardBrand == null
                ? '--'
                : r.cardBrand;
            var paymentInfo = r.paymentInfo == null ? '--' : r.paymentInfo;
            var paymentInfoClass =
              r.paymentInfo == null ? 'hf-center-cell' : '';
            var cardMaskedClass =
              r.cardMasked == 'Unknown' || r.cardMasked == null
                ? 'hf-center-cell'
                : '';

            var send_purchase_receipt = rolesFunc.findIndex(
              (v) => v.name == 'SEND_PURCHASE_RECEIPT'
            );
            var btnAction;
            var btnAction2;

            var invalidate_transaction = rolesFunc.findIndex(
              (v) => v.name == 'INVALIDATE_TRANSACTION'
            );
            var btnInvalidate;

            var magic_link = rolesFunc.findIndex(
              (v) => v.name == 'SEND_MAGIC_LINK'
            );

            var kickUser = rolesFunc.findIndex((v) => v.name == 'KICK_USER');

            var changeEmail_ = rolesFunc.findIndex(
              (v) => v.name == 'KICK_USER'
            );
            
            var amount = true;
            if (r.amount - r.discount <= 0) {
            	amount = false;
            }

            // var changePass = rolesFunc.findIndex(
            // v => v.name == "SEND_EMAIL_PASSWORD_RESET"
            // );
            var btnMagicLinkOperator =
              magic_link != -1 && r.activeEvent == 'true'
                ? `<span class="select-modal-1-action-user" data-txid="${r.id}" id="btnmlo-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="magicLinkOperator" alt="Send Magic Link To Operator" title="Send magic link to operator"><img height="20" src="resources/core/img/icons/send_user_link.png" style="margin-bottom: 4px; cursor:pointer;"></span>`
                : '';
                
            var btnkickUser =
              kickUser != -1 && r.activeEvent == 'true'
                ? `<span class="select-modal-1-action-user"  data-txid="${r.id}" id="btn-kick-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="kickUser"  alt="Kick User" title="Kick User"><span class="fa fa-lock" style="padding-right: 5px; font-size: 1.55em; cursor:pointer;" ></span></span>`
                : '';
            var btnMagicLink =
              magic_link != -1 && r.activeEvent == 'true'
                ? `<span class="select-modal-1-action-user" data-txid="${r.id}" id="btnml-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="magicLink"  alt="Send Magic Link" title="Send magic link"> <span class="fa fa-link" style="padding-right: 5px; font-size: 1.45em; cursor:pointer;" ></span></span>`
                : '';
            var changeEmail =
              changeEmail_ != -1 && r.activeEvent == 'true' && r.status === 'Success'
                ? `<span class="select-modal-1-action-user" data-txid="${r.id}" data-life-cycle="true" id="btnml-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="changeEmail"  alt="Transfer Email" title="Transfer Email"> <span class="fa fa-exchange" style="padding-right: 5px; font-size: 1.45em; cursor:pointer;" ></span></span>`
                : '';

            if (send_purchase_receipt != -1) {
              btnAction =
                (r.activeEvent == 'true' && r.status === 'Success') || r.status === 'Subscription Cancelled'
                  ? `<span class="select-modal-1-action-user"  id="btn-${r.id}" data-id="${id}" data-txid="${r.id}" data-process="resendBtns" title="Resend Receipt" alt="Resend Receipt"><i class="fa fa-copy" style="padding-right: 5px; font-size: 1.5em; cursor:pointer;"></i></span>`
                  : '';
              btnAction2 =
                (r.activeEvent == 'true' && r.status === 'Success') || r.status === 'Subscription Cancelled'
                  ? `<span class="select-modal-1-action-user"  id="btn-${r.id}-op" data-id="${id}" data-txid="${r.id}" data-process="resendOpBtns" title="Send Receipt to operator" alt="Send Receipt to operator"> <span class="fa fa-file-o" style="padding-right: 5px; font-size: 1.4em; cursor:pointer;" ></span></span>`
                  : '';
            } else {
              btnAction = '';
            }

            /*
			 * if (invalidate_transaction != -1) { btnInvalidate = r.status ==
			 * "Success" ? `<span class="select-modal-1-action-user"
			 * id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}"
			 * data-process="invalidateBtns" title="Invalidate transaction"
			 * alt="Invalidate transaction"> <span class="fa fa-ban"
			 * style="padding-right: 5px; font-size: 1.5em; cursor:pointer;" ></span>
			 * </span>`:''; } else { btnInvalidate = ""; }
			 */
            let invalidateAction = '';
            if (invalidate_transaction != -1) {
            	if (r.subscription === false) {
	              invalidateAction =
	                (r.activeEvent == 'true' && r.status === 'Success') || r.status != 'Subscription Cancelled'
	                  ? `<li><a href="#" class="select-modal-1-action-user" id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}" data-process="invalidateBtns" title="Cancel without refund" alt="Cancel without refund">Cancel without refund</a></li>`
	                  : '';
            	}
            	else if (r.subscription === true) {
            		invalidateAction =
    	                (r.activeEvent == 'true' && r.status === 'Success') || r.status != 'Subscription Cancelled'
    	                  ? `<li><a href="#" class="select-modal-1-action-user" id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}" data-process="invalidateBtns" title="Cancel without refund" alt="Cancel without refund">Cancel subscription without refund</a></li>`
    	                  : '';
            	}
            }

            // TODO: implementar rol
            let invalidateWithRefundAction = '';
            if (r.subscription === false) {
	            invalidateWithRefundAction = r.amount > 0 && r.status === 'Success' && amount ? `<li><a href="#" class="select-modal-1-action-user" id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}" data-process="invalidateTxWithRefund" title="Cancel transaction with refund" alt="Cancel transaction with Refund">Cancel Transaction with Refund</a></li>` : '';
	            // let invalidateWithRefundAction = `<li><a href="#"
				// class="select-modal-1-action-user"
				// id="btn-${r.id}-invalidate" data-id="${id}"
				// data-txid="${r.id}" data-process="invalidateTxWithRefund"
				// title="Cancel transaction with refund" alt="Cancel
				// transaction with Refund">Cancel Transaction with
				// Refund</a></li>`;
            }
        	else if (r.subscription === true) {
        		invalidateWithRefundAction = r.amount > 0 && (r.status === 'Success' || r.status === 'Subscription Cancelled') && amount ? `<li><a href="#" class="select-modal-1-action-user" id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}" data-process="invalidateTxWithRefund" title="Cancel transaction with refund" alt="Cancel transaction with Refund">Cancel subscription with Refund</a></li>` : '';
        	}
			const canExpireAccess = rolesFunc.findIndex(v => v.name == 'EXPIRED_ACCESS') > -1;            
			const btnExpireAccessAction = canExpireAccess && r.status === 'Success' && r.subscription === false ? `<li><a href="#" class="select-modal-1-action-user" id="btn-${r.id}-invalidate" data-id="${id}" data-txid="${r.id}" data-process="expireAccess" title="Expire access" alt="expire access">Expire access</a></li>` : ''; 
            
            var approve_signature = rolesFunc.findIndex(
              (v) => v.name == 'REGISTER_DOCUMENT_SIGNATURE'
            );
            var btnSignApprove =
              approve_signature != -1 && r.activeEvent == 'true' && r.subscription === false
                ? `<span class="select-modal-1-action-user" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-txid="${r.id}" data-process="signApprove"   alt="Approve user signature" title="Approve user signature"><span class="fa fa-check-circle-o" style="padding-right: 9px; font-size: 1.6em; cursor:pointer;" ></span></span>`
                : '';

            const btnkickUserAction =
              kickUser != -1 && r.activeEvent == 'true' && r.status === 'Success'
                ? `<li><a href="#" class="select-modal-1-action-user" data-txid="${r.id}" id="btn-kick-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="kickUser"  alt="Kick and Ban User" title="Kick and Ban User">Kick and Ban User</a></li>`
                : '';
            const changeEmailAction =
              changeEmail_ != -1 && r.activeEvent == 'true' && r.status === 'Success' && !r.splitPayment && r.subscription === false
                ? `<li><a href="#" class="select-modal-1-action-user" data-txid="${r.id}" id="btnml-${id}" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-process="changeEmail"  alt="Transfer Transaction" title="Transfer Transaction">Transfer Transaction</a></li>`
                : '';
            const btnSignApproveAction =
              approve_signature != -1 && r.activeEvent == 'true' && r.subscription === false
                ? `<li><a href="#" class="select-modal-1-action-user" data-id="${id}" data-email="${email}" data-lname="${lname}" data-fname="${fname}" data-txid="${r.id}" data-process="signApprove" alt="Signature Override" title="Signature Override">Signature Override</a></li>`
                : '';
            
            let subMenu = '';

            if (
              btnkickUserAction != '' ||
              changeEmailAction != '' ||
              btnSignApproveAction != '' ||
              invalidateAction != '' ||
              invalidateWithRefundAction != ''
            ) {
              subMenu = `<div class="dropdown" id="dropdown-action-${r.id}">
  							<button class="btn btn-default btn-sm dropdown-toggle" style="background: white; color: #777777;" type="button" id="dropdownMenu" data-id="${r.id}" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
    							More
    							<i class="fa fa-caret-down" aria-hidden="true"></i>
  							</button>
  							<ul class="dropdown-menu dropdown-menu-right safari" id="dropdown-menu-items-${r.id}" aria-labelledby="dropDownControl">
  								${invalidateAction}
    							${btnkickUserAction}
    							${btnSignApproveAction}
    							${changeEmailAction}
    							${invalidateWithRefundAction}
    							${btnExpireAccessAction}
  							</ul>
						</div>`;
            }
            
            let selects = '';
                        
            if (
              (btnAction != '' ||
                btnAction2 != '' ||
                btnMagicLink != '' ||
                btnSignApprove != '') &&
              (r.activeEvent == 'true' || r.currentEvent == 0)
            ) {
				const justify = btnAction === '' ? 'end; column-gap: 5px' : 'space-around';
              selects =
                `<div id="container-${r.id}" class="action-btn-container select-modal-1-container select-modal-1-container-${id}">` +
                '<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>' +
                '<div class="icons"><img class="success" src="resources/core/img/check-mark-new.svg" alt="Success"><img class="error" src="resources/core/img/cross.svg" alt="Error"></div>' +
                `<div class="select" style="display: flex; align-items: center; justify-content: ${justify}; width: 100%;">` +                
                btnAction +
                btnAction2 +
                btnMagicLink +
                btnMagicLinkOperator +
                subMenu;
              // btnchangePass+
              '</div>' +
                '<div id="btn-tx-' +
                id +
                '" data-toggle="modal" data-target="#myModal"></div>' +                
                '</div>';
            }

            const accessTo = dayjs(r.accessDateTo);
            const currentDate = dayjs();

            if (accessTo.isValid() && currentDate.isSame(accessTo, 'date') && !currentDate.isAfter(accessTo, 'hm') && r.accessDateTxt == null) {		
				accessDateTo = accessDateTo.substr(0, 10) + ' 00:00:00';					
			}
			
			// Access date from
			
			const canEditAccessFrom = rolesFunc.findIndex(v => v.name === "RESTRICT_EVENT") > -1;
            const btnEditAccessFrom = canEditAccessFrom
              ? `<a href="#" style="margin-left: 5px;" data-id="${r.id}" data-min-date="${accessDateFrom}" id="edit-access-from" data-toggle="tooltip" data-placement="top" title="Edit access date">
              		<i class="fa fa-pencil" />
              	</a>`
              : '';
              
              
			// Access date to
            const canEditAccessTo = rolesFunc.findIndex(v => v.name === "RESTRICT_EVENT") > -1;
            const btnEditAccessTo = canEditAccessTo
              ? `<a href="#" style="margin-left: 5px;" data-id="${r.id}" data-min-date="${accessDateFrom}" id="edit-access-to" data-toggle="tooltip" data-placement="top" title="Edit access date">
              		<i class="fa fa-pencil" />
              	</a>`
              : '';
              
            let body;
            
            if (showAccessDate) {	
				const editAccessFrom = 
					`<td>
						<div class="edit-access-from-${r.id} hide" id="edit-access-from-${r.id}" style="display: flex; align-items: center;">
							<input type="text" id="access-from-${r.id}" name="access-from-${r.id}" value="${accessDateFrom != '' && accessDateFrom != null ? accessDateFrom : convertDateEnglish(new Date())}" onkeypress="return false" style="max-width: 85px;">
							<a href="#" style="margin-left: 5px;" data-user-id="${id}" data-id="${r.id}" id="accept-access-from">
								<i class="fa fa-check" />
							</a>
						</div>
						<div id="text-access-from-${r.id}" style="display:flex;justify-content: center;text-align: center;align-items: center;">
							<span>${accessDateFrom}</span>	
							${btnEditAccessFrom}
							<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>
						</div>
					</td>`;
						
				const inputAccess = r.accessDateTxt == null ? editAccessFrom : `<td class="hf-center-cell">${accessDateFrom}</td>`;								
				body = `<tr>	
	            		<td class="hf-center-cell">${r.id}</td>						
						<td class="hf-center-cell">${transDate}</td>
						<td>${r.fund}</td>
						${inputAccess}`;	
	
				/*
				 * body = `<tr> <td class="hf-center-cell">${r.id}</td>
				 * <td class="hf-center-cell">${transDate}</td> <td>${r.fund}</td>
				 * <td class="hf-center-cell">${accessDateFrom}</td>`;
				 */
			} else {
				body = `<tr>	
						<td class="hf-center-cell">${r.id}</td>					
						<td class="hf-center-cell">${transDate}</td>
						<td>${r.fund}</td>`;
			}
			
			/*
			 * let body = `<tr> <td class="hf-center-cell">${transDate}</td>
			 * <td>${r.fund}</td> <td class="hf-center-cell">${accessDateFrom}</td>`;
			 */
	
			if (showAccessDate) {
				if (r.accessDateTxt == null) {
              		body += 
              			`<td>
							<div class="edit-access-to-${r.id} hide" id="edit-access-${r.id}" style="display: flex; align-items: center;">
							<input type="text" id="access-to-${r.id}" name="access-to-${r.id}" value="${accessDateTo != '' && accessDateTo != null ? accessDateTo : convertDateEnglish(new Date())}" onkeypress="return false" style="max-width: 85px;">
							<a href="#" style="margin-left: 5px;" data-access-from="${r.accessDateFrom}" data-user-id="${id}" data-id="${r.id}" id="accept-access-to">
								<i class="fa fa-check" />
							</a>
						</div>
						<div id="text-access-to-${r.id}" style="display:flex;justify-content: center;text-align: center;align-items: center;">
							<span>${accessDateTo}</span>	
							${btnEditAccessTo}
							<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>
						</div>
					</td>`;
            	} else {
              		body += `<td class="hf-center-cell">${accessDateTo}</td>`;
            	}				
			}

            body += 
				`<td class="${cardMaskedClass}">${cardMasked}</td>
					<td class="hf-center-cell">${cardBrand}</td>
				 	<td class="hf-right-cell">${r.amount}</td>
					<td class="hf-center-cell">${r.status}</td>
					<td class="hf-center-cell">${r.internationalId}</td>
					<td class="${paymentInfoClass}">${paymentInfo}</td>
					<td style="min-width: 170px;">
						<div class="action-btn-container">							
							${selects}
						</div>
					</td>
				</tr>`;
            return body;
          });
          var existSuccess = data.result.findIndex(
            (v) => v.status == 'Success'
          );

          renderDataTransactions(formatedData, showAccessDate);

          $('#actions-transaction-container').hide();

          if (existSuccess == -1) {
            displayActionBtnsForNoSuccess();
          } else {
            $('#actions-transaction-container').html('');
            displayActionBtnsForSuccess();
          }
                    
          	const rows = data.result.length;
// if (rows < 5) {
// $('.modal-content.transaction-body').css('height', '640px');
// }
        } else {
          /*
			 * $(".footerLoader").hide(); $(".footerValidate").show();
			 * $(".alert-danger").text(data.returnMessage);
			 * $(".alert-danger").show();
			 */
        }
	}
	
  function requestUserTransactions() {
    var data = { id: id };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/getUserTransactions',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#modal-content-container').html('');
      },
      success: function (data) {
		processDataTransactions(data, true);		
      },
      error: function (e) {
        /*
		 * console.log("ERROR: ", e); $(".footerLoader").hide();
		 * $(".footerValidate").show();
		 * $(".alert-danger").text(data.returnMessage);
		 * $(".alert-danger").show();
		 */
      },
    });
  }

  /*
	 * $(document).on('click', '#btn-transactions', function(e){
	 * console.log("Click"); e.preventDefault(); getAllUserTransactions(); });
	 */

  function getAllUserTransactions() {
    var data = { id: id };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/getAllUserTransactions',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#modal-content-container').html('');
      },
      success: function (data) {
		processDataTransactions(data, true);
      },
      error: function (e) {
        /*
		 * console.log("ERROR: ", e); $(".footerLoader").hide();
		 * $(".footerValidate").show();
		 * $(".alert-danger").text(data.returnMessage);
		 * $(".alert-danger").show();
		 */
      },
    });
  }
  
  	$('body').off('click', '.select-modal-1-action-user');	
  	
  	$('body').on('click', '.select-modal-1-action-user', (e) => {
		e.preventDefault();
      	const modalId = $(e.currentTarget).data('id');
      	const modalEmail = $(e.currentTarget).data('email');
      	const modalName = $(e.currentTarget).data('fname');
      	const modalLast = $(e.currentTarget).data('lname');
      	const modalProcess = $(e.currentTarget).data('process');
      	const idTx = $(e.currentTarget).data('txid');
      	const lifeCycle = $(e.currentTarget).data('lifeCycle') == undefined ? false : true;
      	
      	processSelectModal1Action(
        	modalId,
        	modalEmail,
        	modalName,
        	modalLast,
        	modalProcess,
        	idTx,
        	lifeCycle
      	);
	});

/*
 * function createEventClick() { //$("#user-transaction-table_wrapper
 * .paginate_button").off('click'); $('#user-transaction-table_wrapper
 * .paginate_button').click(function (e) { createEventClick(); });
 *  // $('.select-modal-1-action-user').off('click');
 * $('.select-modal-1-action-user').click(function (e) { e.preventDefault(); var
 * modalId = $(this).data('id'); var modalEmail = $(this).data('email'); var
 * modalName = $(this).data('fname'); var modalLast = $(this).data('lname'); var
 * modalProcess = $(this).data('process'); var idTx = $(this).data('txid');
 * const lifeCycle = $(this).data('lifeCycle') == undefined ? false : true;
 * 
 * processSelectModal1Action( modalId, modalEmail, modalName, modalLast,
 * modalProcess, idTx, lifeCycle ); }); }
 */


  function renderDataTransactions(data, showAccessDate = false) {
    var template = generateTransactionModalTemplate(data, showAccessDate);

    $('#modal-content-container').html(template);
    $('.transaction-body').show();
    $('.signature-body').hide();

    txTable = $('#user-transaction-table').DataTable({
      order: [[0, 'desc']],
      "columnDefs": [
		{ "visible": false, "targets": 0 },    	
    	{ "width": "5%", "targets": 3 },
    	{ "width": "5%", "targets": 2 },            	
  	  ],
      dom:
        "<'row'<'col-sm-6'><'col-sm-6'f>>" +
        "<'table-responsive'tr>" +
        "<'row'<'col-sm-6'i><'col-sm-6'p>>",
      rowReorder: false,
    });

    $('#user-transaction-table_filter input').addClass('form-control');
    $('.resendBtns').click(function () {
      var idUser = $(this).data('id');
      var idTx = $(this).data('txid');
      var idElement = $(this).attr('id');
      resendReceipt(idUser, idTx, idTx);
    });
    $('.resendOpBtns').click(function () {
      var idUser = $(this).data('id');
      var idTx = $(this).data('txid');
      var idElement = $(this).attr('id');
      resendReceiptOp(idUser, idTx, idTx);
    });
    $('.invalidateBtns').click(function () {
      var idUser = $(this).data('id');
      var idTx = $(this).data('txid');
      var idElement = $(this).attr('id');
      invalidateTx(idUser, idTx, idTx);
    });
    $('.signApprove').click(function (e) {
      e.preventDefault();
      var idUser = $(this).data('id');
      var emailUser = $(this).data('email');
      var idTx = $(this).data('txid');
      var fname = $(this).data('fname');
      var lname = $(this).data('lname');
      var modal = new ApproveSignModal(
        idUser,
        emailUser,
        'approve-signature',
        fname,
        lname,
        idTx
      );
    });

    /*
	 * $(".magicLink").click(function(e) { e.preventDefault(); var emailUser =
	 * $(this).data("email"); var id = $(this).data("id");
	 * sendMagicLink(emailUser, id); });
	 */

    txTable.on('draw', function () {
      $('.resendBtns').click(function () {
        var idUser = $(this).data('id');
        var idTx = $(this).data('txid');
        var idElement = $(this).attr('id');
        resendReceipt(idUser, idTx, idTx);
      });
      $('.resendOpBtns').click(function () {
        var idUser = $(this).data('id');
        var idTx = $(this).data('txid');
        var idElement = $(this).attr('id');
        resendReceiptOp(idUser, idTx, idTx);
      });
      $('.invalidateBtns').click(function () {
        var idUser = $(this).data('id');
        var idTx = $(this).data('txid');
        var idElement = $(this).attr('id');
        invalidateTx(idUser, idTx, idTx);
      });
      $('.magicLink').click(function (e) {
        e.preventDefault();
        var emailUser = $(this).data('email');
        var id = $(this).data('id');
        sendMagicLink(emailUser, id);
      });
      $('.signApprove').click(function (e) {
        e.preventDefault();
        var idUser = $(this).data('id');
        var emailUser = $(this).data('email');
        var idTx = $(this).data('txid');
        var fname = firstName;
        var lname = lastName;
        var modal = new ApproveSignModal(
          idUser,
          emailUser,
          'approve-signature',
          fname,
          lname,
          idTx
        );
      });
    });
  }

  function displayActionBtnsForNoSuccess() {
    var ADD_ATTENDEE_FUNC = rolesFunc.findIndex(
      (v) => v.name == 'ADD_ATTENDEE'
    );
    var COMPLIMENTARY_FUNC = rolesFunc.findIndex(
      (v) => v.name == 'COMPLIMENTARY'
    );
    var containerAction = $('#actions-transaction-container');
    var addAttendeeBtn = `<a class="btn btn-md btn-primary btn-tx-act" href="/scr-admin/callCenter?email=${email}&fname=${firstName}&lname=${lastName}">Complete Purchase</a>`;
    var grantAccessBtn =
      COMPLIMENTARY_FUNC != -1
        ? `<a class="btn btn-md btn-success btn-tx-act" href="/scr-admin/callCenter?email=${email}&fname=${firstName}&lname=${lastName}&complimentary=true">Grant Access</a>`
        : '';

    if (ADD_ATTENDEE_FUNC != -1)
      containerAction.html(addAttendeeBtn + grantAccessBtn);
  }

  function displayActionBtnsForSuccess() {
    var SEND_MAGIC_LINK = rolesFunc.findIndex(
      (v) => v.name == 'SEND_MAGIC_LINK'
    );
    if (SEND_MAGIC_LINK == -1) return;

    var containerAction = $('#actions-transaction-container');
    var magicLinkBtn = `<button class="btn btn-md btn-default mglBtn">Send Magic Link</button>	
                          <div id="lds-modal-container-mgl" style="display:none;">
						            	<div class="lds-ring2"><div></div><div></div><div></div><div></div></div>
						            	</div>
						            	<div id="success-check-mgl" class="success-check" style="display:none;">
						            		<span class="fa fa-check" ></span>
                          </div></div>`;
    containerAction.html(magicLinkBtn);
    $('.mglBtn').click(function (e) {
      e.preventDefault();
      var emailUser = email;
      var idUser = id;
      sendMagicLink(emailUser, idUser);
    });
  }

  function sendMagicLink(email, id, elmId) {
    var data = { userId: id, email: email, txId: elmId };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/sendMagicCustomerLink',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
//        $('#btnml-' + id).hide();
        $('#lds-modal-container-' + id).show();
        $('#success-check-' + id).hide();
        $('.mglBtn').attr('disabled', 'true');
        $('#lds-modal-container-mgl').show();
        $('#success-check-mgl').hide();
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('#lds-modal-container-' + id).hide();
          $('#success-check-' + id).show();

          $('#lds-modal-container-mgl').hide();
          $('#success-check-mgl').show();
          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('success');
            $('#success-check-' + id).hide();
            $('.mglBtn').removeAttr('disabled');
            $('#success-check-mgl').hide();
            refreshTable();
          }, 3000);
        } else {
          $('#lds-modal-container-' + id).hide();
          $('#lds-modal-container-mgl').hide();
          $('.mglBtn').removeAttr('disabled');

          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('error');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }
      },
      error: function (e) {
        $('#lds-modal-container-' + id).hide();
        $('#btn-' + id).show();
        $('#lds-modal-container-mgl').hide();
        $('.mglBtn').removeAttr('disabled');
        $('.select-modal-1-container-' + id).removeClass('loading');
        $(`#container-${elmId}`).addClass('error');
        setTimeout(function () {
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }

	function sendMagicLinkToOperator(email, id, elmId) {
		const data = { userId: id, email: email, txId: elmId };
    	$.ajax({
      		type: 'POST',
      		contentType: 'application/json; charset=UTF-8',
      		url: path_ajax + 'search/api/sendMagicLinkOperator',
      		data: JSON.stringify(data),
      		dataType: 'json',
      		timeout: 100000,
      		beforeSend: function () {
        		$('#btnmlo-' + id).hide();
        		$('#lds-modal-container-' + id).show();
        		$('#success-check-' + id).hide();
        		$('.mglBtn').attr('disabled', 'true');
        		$('#lds-modal-container-mgl').show();
        		$('#success-check-mgl').hide();
        		$(`#container-${elmId}`).addClass('loading');
        		$(`#container-${elmId} .select`).hide();
      		},
      		success: function (data) {
        		if (data.returnCode == 0) {
          			$('#btnmlo-' + id).hide();
          			$('#lds-modal-container-' + id).hide();
          			$('#success-check-' + id).show();
          			$('#lds-modal-container-mgl').hide();
          			$('#success-check-mgl').show();
          			$('.select-modal-1-container-' + id).removeClass('loading');
          			$(`#container-${elmId}`).addClass('success');
          			setTimeout(function () {
            			$('.select-modal-1-container-' + id).removeClass('success');
            			$('#success-check-' + id).hide();
            			$('#btnmlo-' + id).show();
            			$('.mglBtn').removeAttr('disabled');
            			$('#success-check-mgl').hide();
            			refreshTable();
          			}, 3000);
        		} else {
          			$('#lds-modal-container-' + id).hide();
          			$('#btnmlo-' + id).show();
          			$('#lds-modal-container-mgl').hide();
          			$('.mglBtn').removeAttr('disabled');
          			$('.select-modal-1-container-' + id).removeClass('loading');
          			$(`#container-${elmId}`).addClass('error');
          			setTimeout(function () {
            			$('.select-modal-1-container-' + id).removeClass('error');
          			}, 3000);
        		}
      		},
      		error: function (e) {
        		$('#lds-modal-container-' + id).hide();
        		$('#btn-' + id).show();
        		$('#lds-modal-container-mgl').hide();
        		$('.mglBtn').removeAttr('disabled');
        		$('.select-modal-1-container-' + id).removeClass('loading');
        		$(`#container-${elmId}`).addClass('error');
        		setTimeout(function () {
          			$('.select-modal-1-container-' + id).removeClass('error');
        		}, 3000);
      		},
      		complete: function () {
        		setTimeout(() => {
          			$(`#container-${elmId} .select`).show();
          			$('#btnmlo-' + id).show();
        		}, 3000);
      		},
    	});
	}


  function resendReceipt(id, tx, elmId) {
    const data = { userId: id, txId: tx };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/resendUserReceiptTx',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#btn-' + elmId).hide();
        $('#lds-modal-container-' + elmId).show();
        $('#success-check-' + elmId).hide();
        // $('.select-modal-1-container-'+id).addClass("loading");
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('#btn-' + elmId).hide();
          $('#lds-modal-container-' + elmId).hide();
          $('#success-check-' + elmId).show();

          setTimeout(function () {
            $('#success-check-' + elmId).hide();
            $('#btn-' + elmId).show();
            refreshTable();
          }, 1500);

          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          // $('.select-modal-1-container-'+id).addClass("success");
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('success');
          }, 3000);
        } else {
          $('#lds-modal-container-' + elmId).hide();
          $('#btn-' + elmId).show();
          $('.select-modal-1-container-' + id).removeClass('loading');
          // $('.select-modal-1-container-'+id).addClass("error");
          $(`#container-${elmId}`).addClass('error');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }
      },
      error: function (e) {
        $('#lds-modal-container-' + elmId).hide();
        $('#btn-' + elmId).show();

        $('.select-modal-1-container-' + id).removeClass('loading');
        $('.select-modal-1-container-' + id).addClass('error');
        setTimeout(function () {
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }
  function resendReceiptOp(id, tx, elmId) {
    var data = { userId: id, txId: tx };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/resendUserReceiptOp',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#btn-' + elmId + '-op').hide();
        $('#lds-modal-container-' + elmId + '-op').show();
        $('#success-check-' + elmId + '-op').hide();
        // $('.select-modal-1-container-'+id).addClass("loading");
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('#btn-' + elmId + '-op').hide();
          $('#lds-modal-container-' + elmId + '-op').hide();
          $('#success-check-' + elmId + '-op').show();

          setTimeout(function () {
            $('#success-check-' + elmId + '-op').hide();
            $('#btn-' + elmId + '-op').show();
          }, 1500);

          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('success');
            refreshTable();
          }, 3000);
        } else {
          $('#lds-modal-container-' + elmId + '-op').hide();
          $('#btn-' + elmId + '-op').show();
          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('error');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }
      },
      error: function (e) {
        $('#lds-modal-container-' + elmId + '-op').hide();
        $('#btn-' + elmId + '-op').show();
        $('.select-modal-1-container-' + id).removeClass('loading');
        $(`#container-${elmId}`).addClass('error');
        setTimeout(function () {
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }
  
  	function expireAccess(id, elmId) {
		const data = { userId: id, txId: elmId,timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone};
    	$.ajax({
      		type: 'POST',
      		contentType: 'application/json; charset=UTF-8',
      		url: path_ajax + 'search/api/expireUserAccess',
      		data: JSON.stringify(data),
      		dataType: 'json',
      		timeout: 100000,
      		beforeSend: function () {
        		$(`#container-${elmId}`).addClass('loading');
        		$(`#container-${elmId} .select`).hide();
      		},
      		success: function (data) {
        		if (data.returnCode == 0) {
          			$('.select-modal-1-container-' + id).removeClass('loading');
          			$(`#container-${elmId}`).addClass('success');
          			setTimeout(function () {
            			$(`#container-${elmId} .select`).show();
            			$('.select-modal-1-container-' + id).removeClass('success');
            			refreshTable();
          			}, 3000);
        		} else {
          			$('.select-modal-1-container-' + id).removeClass('loading');
          			$(`#container-${elmId}`).addClass('error');
          			setTimeout(function () {
            			$(`#container-${elmId} .select`).show();
            			$('.select-modal-1-container-' + id).removeClass('error');
          			}, 3000);
        		}
      		},
      		error: function (e) {
        		$('.select-modal-1-container-' + id).removeClass('loading');
        		$(`#container-${elmId}`).addClass('error');
        		setTimeout(function () {
          			$(`#container-${elmId} .select`).show();
          			$('.select-modal-1-container-' + id).removeClass('error');
        		}, 3000);
      		},
      		complete: function () {
        		setTimeout(() => {
          			$(`#container-${elmId} .select`).show();
        			}, 3000);
      			},
    		});
		}

  function kickUser(id, tx, elmId) {
    var data = { customerUserId: id, txId: elmId };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + '/search/api/reportUserFraud',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          setTimeout(function () {
            $(`#container-${elmId} .select`).show();
            $('.select-modal-1-container-' + id).removeClass('success');
            refreshTable();
          }, 3000);
        } else {
          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('error');

          setTimeout(function () {
            $(`#container-${elmId} .select`).show();
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }        
      },
      error: function (e) {
        $('.select-modal-1-container-' + id).removeClass('loading');
        $(`#container-${elmId}`).addClass('error');
        setTimeout(function () {
          $(`#container-${elmId} .select`).show();
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }

  function invalidateTx(id, tx, elmId, description) {
    var data = { customerUserId: id, txId: tx, commentary: description };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/invalidateTx',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#btn-' + elmId + '-invalidate').hide();
        $('#lds-modal-container-' + elmId + '-invalidate').show();
        $('#success-check-' + elmId + '-invalidate').hide();
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('#btn-' + elmId + '-invalidate').hide();
          $('#lds-modal-container-' + elmId + '-invalidate').hide();
          $('#success-check-' + elmId + '-invalidate').show();

          setTimeout(function () {
            $('#success-check-' + elmId + '-invalidate').hide();
            $('#btn-tx-' + id).click();
          }, 1500);

          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('success');
            refreshTable();
          }, 3000);
        } else {
          $('#lds-modal-container-' + elmId + '-invalidate').hide();
          $('#btn-' + elmId + '-invalidate').show();
          $(`#container-${elmId}`).removeClass('loading');
          $(`#container-${elmId}`).addClass('error');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }
      },
      error: function (e) {
        $('#lds-modal-container-' + elmId).hide();
        $('#btn-' + elmId).show();
        $('.select-modal-1-container-' + id).removeClass('loading');
        $(`#container-${elmId}`).addClass('error');
        setTimeout(function () {
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }

  function cancelTx(id, tx, elmId, description) {
    const data = { customerUserId: id, txId: tx, commentary: description };
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/cancelTx',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('#btn-' + elmId + '-invalidate').hide();
        $('#lds-modal-container-' + elmId + '-invalidate').show();
        $('#success-check-' + elmId + '-invalidate').hide();
        $(`#container-${elmId}`).addClass('loading');
        $(`#container-${elmId} .select`).hide();
      },
      success: function (data) {
        if (data.returnCode == 0) {
          $('#btn-' + elmId + '-invalidate').hide();
          $('#lds-modal-container-' + elmId + '-invalidate').hide();
          $('#success-check-' + elmId + '-invalidate').show();

          setTimeout(function () {
            $('#success-check-' + elmId + '-invalidate').hide();
            $('#btn-tx-' + id).click();
            refreshTable();
          }, 1500);

          $('.select-modal-1-container-' + id).removeClass('loading');
          $(`#container-${elmId}`).addClass('success');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('success');
          }, 3000);
        } else {
          $('#lds-modal-container-' + elmId + '-invalidate').hide();
          $('#btn-' + elmId + '-invalidate').show();
          $(`#container-${elmId}`).removeClass('loading');
          $(`#container-${elmId}`).addClass('error');
          setTimeout(function () {
            $('.select-modal-1-container-' + id).removeClass('error');
          }, 3000);
        }
      },
      error: function (e) {
        $('#lds-modal-container-' + elmId).hide();
        $('#btn-' + elmId).show();
        $('.select-modal-1-container-' + id).removeClass('loading');
        $(`#container-${elmId}`).addClass('error');
        setTimeout(function () {
          $('.select-modal-1-container-' + id).removeClass('error');
        }, 3000);
      },
      complete: function () {
        setTimeout(() => {
          $(`#container-${elmId} .select`).show();
        }, 3000);
      },
    });
  }

  return {
    init: init,
  };
}

function ApproveSignModal(idUser, emailUser, userAction, fname, lname, txId) {
  var id = idUser;
  var userName = fname;
  var userLname = lname || ' ';
  var email = emailUser;
  var action = userAction;
  var validator;

  function generateSignatureModal() {
    return `
				
			      <div class="modal-header ">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">Approve for ${userName} ${userLname} (${email}) that signed the event's terms and conditions</h4>
			      </div>
			      <div class="modal-body">
			       <div id="signature-container">
			        <form id="approve-signature">
			        	<div class="mail-container">
			        	<label for"mail">
			        		Comments:
			        	</label>
			        	<input type="hidden" id="transaction-id" name="transaction-id" value="${txId}">
			        	<textarea  id="approve-comment" class="form-control" name="approve-comment"  required></textarea>
			        	<span class="error-message"></span>
			        	</div>
			        </form>
			        <div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
					  <a href="#" class="alert-link success-msg-alert"></a>
					</div>
					<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
						<a href="#" class="alert-link error-msg-alert"></a>
					</div>
					</div>
					
					<div id="clipboard-container" style="display:none;">
					 <p class="error-message">The user ${email} doesn't have a contract document attached. Please share to the client the link below  to sign the new document generated</p>
					<div class="form-group clipboard-signature-form" style="margin-top:10px;">
                    <div class="col-sm-7 col-sm-offset-3">
                      <div class="input-group">
                        <input class="form-control" type="text" id="signature-link-url">
                        <div class="input-group-btn">
                          <button id="clipboard-action" class="btn btn-danger" title="" data-container="body" data-placement="top" data-toggle="tooltip" type="button" data-original-title="Copy to clipboard" data-clipboard-target="#signature-link-url">
                            <span class="icon icon-copy icon-fw"></span>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                  </div>
			      </div>
			      <div class="modal-footer footerBtn">
			        <button id=""  type="button" class="btn btn-default destroy-signature-body" >Cancel</button>
			        <button id="approve-btn" type="submit" class="btn btn-primary">Approve</button>
			      </div>
			      <div class="modal-footer footerClose" style="display:none;">
			        <button id=""  type="button" class="btn btn-default destroy-signature-body" >Close</button>
			      </div>
			       <div class="modal-footer footerLoader" style="display:none;">
			      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
			      </div>
			    <!-- /.modal-content -->	`;
  }
  function approveCustomerUserSignature() {
    let data = {
      userId: id,
      transactionId: $('#transaction-id').val(),
      comments: $('#approve-comment').val(),
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/approveSignature',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
        $('.footerLoader').show();
        $('.footerBtn').hide();
        $('.alert-danger').hide();
        $('#footerClose').hide();
        $('#clipboard-container').hide();
      },
      success: function (data) {
        console.log('SUCCESS: ', data);
        if (data.returnCode == '0') {
          $('.footerLoader').hide();
          $('.footerClose').show();
          $('#approve-btn').hide();
          $('.alert-success').text('User signature approved succesfully!');
          $('.alert-success').show();
          $('#generateUserSearch').click();
        } else if (data.returnCode == 403 && data.returnMessage == 'logout') {
          window.location = '/scr-admin/';
        } else if (data.returnCode == -49) {
          $('#signature-container').hide();
          $('#clipboard-container').show();
          $('.footerLoader').hide();
          $('.footerBtn').hide();
          $('.footerClose').show();
          $('#signature-link-url').val(data.result.signature_url);
        } else {
          $('.footerLoader').hide();
          $('.footerBtn').show();
          $('.alert-danger').text(data.returnMessage);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('ERROR: ', e);
        $('.footerLoader').hide();
        $('.footerBtn').show();
        $('.alert-danger').text(
          'Unexpected error has ocurred, please contact support.'
        );
        $('.alert-danger').show();
      },
    });
  }

  function registerNewUser() {
    var notExistUser = true;
    if (!validateEmailVar) {
      notExistUser = false;
    }

    var emailUser;
    if ($('#confirm_lang-email_ph').val().trim().length > 0) {
      emailUser = $('#confirm_lang-email_ph').val().trim();
    } else {
      emailUser = $('#lang-email_ph').val().trim();
    }

    var newUser = {};
    newUser['email'] = emailUser;
    newUser['firstName'] = $('#lang-fname_ph').val();
    newUser['lastName'] = $('#lang-lname_ph').val();
    newUser['countryId'] = $('#type-countries select').val();
    newUser['newUser'] = false; // siempre debe ir 'false'
    newUser['userId'] = 0; // userIdVar;
    newUser['phoneNumber'] = removeFormatNum(
      $('#lang-area-code_ph').val(),
      $('#lang-phone_ph').val()
    );
    newUser['password'] = null;

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: ajaxPath + 'search/api/createUser',
      data: JSON.stringify({ newUser: newUser }),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        if (data.code === '200') {
        } else if (data.code === '401') {
          validateEmailVar = false;
        } else {
          validateEmailVar = false;
        }
      },
      error: function (e) {
        validateEmailVar = false;
      },
    });

    return false;
  }

  function init() {
    $('#myModal').removeClass('txModal');
    $('#myModal').addClass('editModal');

    var template = generateSignatureModal();
    $('.signature-body').html(template);

    $('.transaction-body').hide();
    $('.signature-body').show();

    validator = $('#approve-signature').validate({
      rules: {
        'approve-comment': {
          required: true,
        },
      },
      submitHandler: approveCustomerUserSignature,
      errorClass: 'has-error',
      validClass: 'has-success',
      highlight: function (element, errorClass, validClass) {
        $(element).addClass(errorClass).removeClass(validClass);
      },
      unhighlight: function (element, errorClass, validClass) {
        $(element).addClass(validClass).removeClass(errorClass);
      },
      errorPlacement: function (error, element) {
        var parent = $(element).parent();
        var sibling = parent.children('.error-message');
        error.appendTo(sibling);
      },
    });

    $('#approve-btn').click(function () {
      console.log('work');
      $('#approve-signature').submit();
    });

    $('.destroy-signature-body').click(function (e) {
      e.preventDefault();
      $('.signature-body').hide();
      $('.transaction-body').show();
      $('#myModal').removeClass('editModal');
      $('#myModal').addClass('txModal');
      $('#clipboard-container').hide();
      $('#signature-container').show();
      $('#clipboard-container').hide();
    });

    var clipboard = new ClipboardJS('#clipboard-action');
  }

  init();
}

// TODO ANGEL
function changeEmail(
  idUser,
  emailUser,
  userAction,
  fname,
  lname,
  txId,
  lifeCycle = false
) {
  var id = idUser;
  var userName = fname;
  var userLname = lname || ' ';
  var email = emailUser;
  var action = userAction;
  var validator;
  var request = false;

  function generateChangeModal() {
    return `
				
			      <div class="modal-header">
			        <!--<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
			      </div>

			      <div class="modal-body">
            
			       <div id="changeEmail-container">

              <form id="changeEmail-success">
                <div class="successModalImg"><img class="success" src="resources/core/img/check-mark-new.svg" alt="Success"></div>
                <div class="successModalTitle">The transaction was completed successfully</div>
                <div class="modal-footer footerBtn" style="text-align: center;">
                  <button type="button" class="btn btn-default destroy-emailChange-body" >Close</button>
                </div>
              </form>

              <form id="approve-changeEmail-newUser">

              <h4 class="modal-title"> 
                You have to add the email data <span id="approve-changeEmail-newUser-email"></span>, to transfer all the information from ${email}
              </h4>
              <input type="hidden" id="transaction-id" name="transaction-id" value="${txId}">

                <div class="mail-container">
                  <label for="myName">Name:</label>
                  <input type="text" id="myName" class="form-control" name="myName" required="" aria-required="true">
                  <span class="error-message"></span>
                </div>

                <div class="mail-container">
                  <label for="myLastName">Last Name:</label>
                  <input type="text" id="myLastName" class="form-control" name="myLastName" required="" aria-required="true">
                  <span class="error-message"></span>
                </div>

                <div class="mail-container">
                  <label for="myPhone">Phone:</label>
                  <input type="tel" id="myPhone" class="form-control" name="myPhone" required="" aria-required="true">
                  <span class="error-message"></span>
                </div>

                <div id="approve-changeEmail-newUser-error" class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
                  <a href="#" class="alert-link error-msg-alert"></a>
                </div>

                <div class="modal-footer approve-changeEmail-newUser-btn">
                  <button type="button" class="btn btn-default destroy-emailChange-body" >Cancel</button>
                  <button id="approve-changeEmail-newUser-run" type="submit" class="btn btn-primary">Add</button>
                </div>

                <div class="modal-footer approve-changeEmail-newUser-loader" style="display:none;">
                <div class="lds-ring"><div></div><div></div><div></div><div></div></div>
              </div>
                
              </form>

			        <form id="approve-changeEmail">
			        	<div class="mail-container">
                			<h4 class="modal-title"> 
                				Transfer transaction from ${email} to the following email: 
                			</h4>
			        	<input type="hidden" id="transaction-id" name="transaction-id" value="${txId}">			        	
			        	<input type="hidden" id="user-id" name="user-id" value="${idUser}">
			        	<input type="hidden" id="email-origin" name="email-origin" value="${email}">
			        	<input type="hidden" id="life-cycle" name="life-cycle" value="${lifeCycle}">			        	
			        				        	
                <div class="mail-container">
                  <label for"mail"="">Email:</label>
                  <input type="email" id="mail" class="form-control" name="mail" required="" aria-required="true">
                  <span class="error-message"></span>
                </div>

                <div class="mail-confirm-container">
                  <label for"mail2"="">Confirm Email:</label>
                  <input type="email" id="mail2" class="form-control" name="mail2" value="" required="" aria-required="true">
                  <span class="error-message"></span>
                </div>

			        	</div>

                <div id="changeEmail-error" class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
                  <a href="#" class="alert-link error-msg-alert"></a>
                </div>

                <div class="modal-footer footerBtn">
                  <button type="button" class="btn btn-default destroy-emailChange-body" >Cancel</button>
                  <button id="changeEmail-btn" type="submit" class="btn btn-primary">Transfer</button>
                </div>

                <div class="modal-footer footerLoader" style="display:none;">
                  <div class="lds-ring"><div></div><div></div><div></div><div></div></div>
                </div>
			        </form>


          `;
  }

  function transferTransactionToAnotherUser() {
    var data = {};
    data['emailOrigin'] = email;
    data['emailToTransfer'] = $('#approve-changeEmail #mail2').val().trim();

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/transferTransactionToAnotherUser',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function (data) {
        request = true;
        $('#changeEmail-success').hide();
        $('#changeEmail-error').hide();
        $('.modal-footer.footerBtn').hide();
        $('#approve-changeEmail').show();
        $('.modal-footer.footerLoader').show();
        $('#approve-changeEmail-newUser').hide();
      },
      success: function (resp) {
        if (resp.returnCode == '200' || resp.returnCode == '0') {
          $('#changeEmail-success').show();
          $('#approve-changeEmail').hide();

          $('#approve-changeEmail-newUser').hide();
        } else if (resp.returnCode == '-54') {
          $('#approve-changeEmail').hide();
          $('#approve-changeEmail-newUser-email').html(data['emailToTransfer']);
          $('#approve-changeEmail-newUser').show();
          // se tiene que abrir un modal donde el operador tiene que registrar
			// el usuario
        } else {
          $('#changeEmail-error').show();
          $('#changeEmail-error a').html(resp.returnMessage);
        }
      },
    })
      .done(function () {
        request = false;
        $('.modal-footer.footerBtn').show();
        $('.modal-footer.footerLoader').hide();
      })
      .fail(function (errMsg) {
        request = false;
        $('.modal-footer.footerBtn').show();
        $('.modal-footer.footerLoader').hide();
      });
  }

  function transferTransactionToAnotherUserforLifeCycle() {
    const data = {
      customerId: $('#user-id').val(),
      transactionId: $('#transaction-id').val(),
      emailOrigin: $('#email-origin').val().trim(),
      emailToTransfer: $('#approve-changeEmail #mail2').val().trim(),
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url:
        path_ajax + 'search/api/transferTransactionToAnotherUserUserLifeCycle',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function (data) {
        request = true;
        $('#changeEmail-success').hide();
        $('#changeEmail-error').hide();
        $('.modal-footer.footerBtn').hide();
        $('#approve-changeEmail').show();
        $('.modal-footer.footerLoader').show();
        $('#approve-changeEmail-newUser').hide();
      },
      success: function (resp) {
        if (resp.returnCode == '200' || resp.returnCode == '0') {
          $('#changeEmail-success').show();
          $('#approve-changeEmail').hide();

          $('#approve-changeEmail-newUser').hide();
          // Reload userDetail page
          window.location.reload();
        } else if (resp.returnCode == '-54') {
          $('#approve-changeEmail').hide();
          $('#approve-changeEmail-newUser-email').html(data['emailToTransfer']);
          $('#approve-changeEmail-newUser').show();
          // se tiene que abrir un modal donde el operador tiene que registrar
			// el usuario
        } else {
          $('#changeEmail-error').show();
          $('#changeEmail-error a').html(resp.returnMessage);
        }
      },
    })
      .done(function () {
        request = false;
        $('.modal-footer.footerBtn').show();
        $('.modal-footer.footerLoader').hide();
      })
      .fail(function (errMsg) {
        request = false;
        $('.modal-footer.footerBtn').show();
        $('.modal-footer.footerLoader').hide();
      });
  }

  function registerNewUserForm() {
    var newUser = {};
    newUser['email'] = $('#approve-changeEmail #mail2').val().trim();
    newUser['firstName'] = $('#approve-changeEmail-newUser #myName').val();
    newUser['lastName'] = $('#approve-changeEmail-newUser #myLastName').val();
    newUser['phoneNumber'] = $('#approve-changeEmail-newUser #myPhone').val();
    newUser['password'] = '';
    newUser['countryId'] = '0';
    newUser['newUser'] = false;
    newUser['userId'] = 0;
    newUser['transactionId'] = $('#transaction-id').val();
    return newUser;
  }

  function registerNewUser() {
    const lifeCycle = $('#life-cycle').val();
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/createUser',
      data: JSON.stringify({ newUser: registerNewUserForm() }),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function (data) {
        request = true;
        $('#approve-changeEmail-newUser').show();
        $('#approve-changeEmail-newUser-error').hide();
        $('.approve-changeEmail-newUser-btn').hide();
        $('.approve-changeEmail-newUser-loader').show();
        $('#approve-changeEmail').hide();
      },
      success: function (data) {
        if (data.code == '200' || data.code == '0') {
          if (lifeCycle) {
            transferTransactionToAnotherUserforLifeCycle();
          } else {
            transferTransactionToAnotherUser();
          }
        } else {
          $('#approve-changeEmail-newUser-error').show();
          $('#approve-changeEmail-newUser-error a').html(data.msg);
        }
      },
    })
      .done(function () {
        request = false;
        $('.approve-changeEmail-newUser-btn').show();
        $('.approve-changeEmail-newUser-loader').hide();
      })
      .fail(function (errMsg) {
        request = false;
        $('.approve-changeEmail-newUser-btn').show();
        $('.approve-changeEmail-newUser-loader').hide();
      });
  }

  function validatorChangeEmail() {
    jQuery.validator.addMethod(
      'lettersonly',
      function (value, element) {
        return (
          this.optional(element) ||
          /^[a-z áãâäàéêëèíîïìóõôöòúûüùçñ]+$/i.test(value)
        );
      },
      'Letters only please'
    );

    validator = $('#approve-changeEmail-newUser').validate({
      rules: {
        myName: {
          required: true,
          minlength: 3,
          maxlength: 30,
          lettersonly: true,
        },
        myLastName: {
          required: true,
          minlength: 3,
          maxlength: 30,
          lettersonly: true,
        },
        myPhone: {
          required: true,
          minlength: 5,
          maxlength: 15,
          number: true,
        },
      },
      submitHandler: function (form) {
        if (!request) registerNewUser();
      },
      errorClass: 'has-error',
      validClass: 'has-success',
      highlight: function (element, errorClass, validClass) {
        $(element).addClass(errorClass).removeClass(validClass);
      },
      unhighlight: function (element, errorClass, validClass) {
        $(element).addClass(validClass).removeClass(errorClass);
      },
      errorPlacement: function (error, element) {
        var parent = $(element).parent();
        var sibling = parent.children('.error-message');
        error.appendTo(sibling);
      },
    });

    validator = $('#approve-changeEmail').validate({
      rules: {
        mail: {
          required: true,
          email: true,
        },
        mail2: {
          required: true,
          email: true,
          equalTo: '#mail',
        },
      },
      submitHandler: function (form) {
        if (!request) transferTransactionToAnotherUserforLifeCycle();
        // transferTransactionToAnotherUser();
      },
      errorClass: 'has-error',
      validClass: 'has-success',
      highlight: function (element, errorClass, validClass) {
        $(element).addClass(errorClass).removeClass(validClass);
      },
      unhighlight: function (element, errorClass, validClass) {
        $(element).addClass(validClass).removeClass(errorClass);
      },
      errorPlacement: function (error, element) {
        var parent = $(element).parent();
        var sibling = parent.children('.error-message');
        error.appendTo(sibling);
      },
    });
  }
  
  	    	
  function eventChangeEmail() {
    // $("#changeEmail-btn").click(function()
    $('#changeEmail-btn').one('click', function () {
      $('#approve-changeEmail').submit();
    });

    // $("#approve-changeEmail-newUser-run").click(function()
    $('#approve-changeEmail-newUser-run').one('click', function () {
      $('#approve-changeEmail-newUser').submit();
    });

    $('.destroy-emailChange-body').click(function (e) {
      e.preventDefault();
      $('.changeEmail-body').hide();
      $('.transaction-body').show();
      $('#myModal').removeClass('changeModal');
      $('#myModal').addClass('txModal');
      $('#changeEmail-container').show();
    });
  }

  function init() {
    $('#myModal').removeClass('txModal');
    $('#myModal').addClass('changeModal');
    var template = generateChangeModal();
    $('.changeEmail-body').html(template);
    $('.transaction-body').hide();
    $('.changeEmail-body').show();
    $('#changeEmail-success').hide();
    $('#changeEmail-error').hide();
    $('#approve-changeEmail-newUser').hide();
    $('.modal-footer.footerBtn').show();
    $('#approve-changeEmail').show();
    validatorChangeEmail();
    eventChangeEmail();
    /*
	 * var clipboard = new ClipboardJS('#clipboard-action');
	 */
  }

  init();
}

function listCountries(country) {
	$.ajax({
		type : "POST",
		contentType : "application/json; charset=UTF-8",
		url : path_ajax + "search/api/listAllCountries",
		data : JSON.stringify({}),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			if (data.returnCode == 0) 
			{
				var new_select = "";
				var formatedDataC = data.result.ListCountries.map( r => { 
					var option;
					if (country === r.id) {
						option = '<option value="'+r.id+'" selected>'+r.name+'</option>';
					}
					else {
						option = '<option value="'+r.id+'">'+r.name+'</option>';
					}
					return option;
				});
	
				$('#listCountry').append(formatedDataC.join(""));
			}
		}
	});	
}

function listLanguage(langId) {
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json; charset=UTF-8',
	    url: path_ajax + 'search/api/findListAllSelectReports',
	    timeout: 100000,
	    success: function (data) {
	        var formatedData = data.result.allSelects.languages.map( d => { 
	        	var option;
	        	if (langId === d.id) {
	        		option = '<option value="'+d.id+'" selected>'+d.description+'</option>';
				}
				else {
					option = '<option value="'+d.id+'">'+d.description+'</option>';
				}
	    		return option;
	    	});
	    	$('#listLanguage').append(formatedData.join(""));
	    },
	    done: function (e) {
	    	console.log('DONE');
	    },
	});
}
