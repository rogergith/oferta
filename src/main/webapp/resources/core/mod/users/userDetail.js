$(document).ready(function () {
  const path_ajax = '/scr-admin/';
  const userId = $('#input-tags').data('id');
  const _tags = $('#input-tags').data('data');
  let _loading = false;

  $('#creditCardForm').validate();

  // Mask validate
  $('#cardNumber').mask('0000-0000-0000-0000');
  $('#expDate').mask('99/99');

  if (statusSales != null && statusSales != "" && statusSales === "1") {
	  $('#user-phone').addClass("phone-decoration")
	  $('#dnc-phone').show();
  }
  else {
	  $('#user-phone').removeClass("phone-decoration")
	  $('#dnc-phone').hide();
  }
  

  const $select = $('#input-tags').selectize({
    plugins: ['remove_button'],
    delimiter: ',',
    persist: false,
    addPrecedence: false,
    render: {
      option_create: function (data, escape) {
        const addString = 'Add';
        return (
          '<div class="create">' +
          addString +
          ' <strong>' +
          escape(data.input) +
          '</strong>&hellip;</div>'
        );
      },
    },
    onItemAdd: function (value) {
      if (!_loading) {
        const items = _getTagsItems();
        updateTags(userId, items);
      }
    },
    onItemRemove: function (value) {
      const items = _getTagsItems();
      updateTags(userId, items);
    },
    create: function (input) {
      return {
        value: input,
        text: input,
      };
    },
    onInitialize: function () {
      const self = this;
      _loading = true;
      $.each(_tags, function (index, item) {
        self.addOption({
          text: item,
          value: item,
        });
        self.addItem(item);
      });
      _loading = false;
    },
  });

  function _getTagsItems() {
    const selectize = $select[0].selectize;
    return selectize.items;
  }

  function updateTags(userId, tags) {
    let data = {};
    data['userId'] = userId;
    data['tags'] = tags;
    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/updateTags',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        console.log('SUCCESS: ', data);
      },
      error: function (e) {
        console.log('Error: ', e);
      },
    });
  }

  $('#btn-bitacora').on('click', function (e) {
    e.preventDefault();
    element = $(this);
    const userId = element.data('id');
    const comment = $('#comment').val();

    if (comment) {
      addBusinessUserSupportHistory(userId, comment);
    }
  });

  $('#comment').on('change keyup paste', function (e) {
    const value = $(this).val();
    $('#btn-bitacora').prop('disabled', value.length === 0);
  });

  function addBusinessUserSupportHistory(userId, comment) {
    const data = {
      userId: userId,
      comment: comment,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/addBusinessUserSupportHistory',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        $('#comment').val('');
        const items = data.result.listBusinessUserSupportHistory;
        const menu = $('#bitacora-list');
        menu.empty();
        $.each(items, function (index, item) {
          const html = `
						<li style="margin-bottom: 10px;">											
							<strong>${item.commentdate} - Operator: ${item.emailop}</strong><br>
							<span>${item.comment}</span>																									
						</li>`;
          menu.append(html);
        });
      },
      error: function (e) {
        console.log('Error: ', e);
      },
    });
  }

  $('#btnCreditCard').on('click', function (e) {
	const btn = $('#btn-container-disable');
	btn.addClass('spinner-background');  
    e.preventDefault();
    const userId = $(this).data('id');
    findListUserCardByUserId(userId);
  });
  
  function refreshDataBeta() {
	  $('#btnCreditCard').trigger('click');
  }

  $(document).off('click', '#btnCreditCardForm').on('click', '#btnCreditCardForm', function () {
    const form = $('#creditCardForm');
    form.trigger('reset');
    $('.alert-danger').hide();
    const modal = $('#creditCardModal');
    modal.addClass('active');
    modal.css({ display: 'none' });
    modal.modal('show');

    $('#myModal').modal('hide');
//    modal.on('hidden.bs.modal', function () {
//      $('#myModal').modal('show');
//    });
  });
  
  $('#btnCancelCreditCard').off('click').on('click', function (e) {
	 e.preventDefault();
	 $('#myModal').modal('show');
  });

  // Save CreditCard Form
  $('#btnSaveCreditCard').off('click').on('click', function (e) {
    e.preventDefault();
    const userId = $('#creditCardForm').data('id');
	    const expDate = $('#creditCardForm #expDate').val();
	    const number = $('#creditCardForm #cardNumber').val().replace(/-/g, '');
	    const cvc = $('#creditCardForm #cvv').val();
	    const expMonth = expDate.split('/')[0];
	    const expYear = expDate.split('/')[1];

	    if (cvc == '' || number == '' || expDate == '') {
	    	$('.alert-danger').text("The fields are required");
	        $('.alert-danger').show();
	    	return;
	    }
	    else {
	    	const payload = {
		      number: number,
		      exp_month: expMonth,
		      exp_year: expYear,
		      cvc: cvc,
		    };

		    $('#submit-spinner').show();
		    $('.alert-danger').hide();
		    Stripe.card.createToken(payload, function (status, response) {
		      if (status === 200) {
		        const token = response.id;
		        addUserCreditCard(userId, token);
		      } else {
		        $('#submit-spinner').hide();
		        $('.alert-danger').text(response.error.message);
		        $('.alert-danger').show();
		      }
		    });
	    }
  });
  
  function _displayListUserCard(items) {
    let html = '';
    $.each(items, function (index, item) {
      html += `<tr>
  					<td>XXXX-XXXX-XXXX-${item.cardLast4}</th>
  					<td class="text-center">${item.brand}</td>
  					<td class="text-center">${item.expMonth}/${item.expYear}</td>
  					<td class="text-center">
  						<span class="label label-${item.default ? 'success' : ''}">
  							${item.default ? 'default' : ''}
  						</span>
  					</td>
  					<td class="text-center">  					  					  						
  						<span class="fa fa-trash-o delete-cc" id="deleteCard" data-id="${
                item.cardId
              }" role="button" style="font-size: 1.3em; cursor:pointer;"></span>
  						<div id="delete-spinner" class="spinner spinner-default spinner-md" style="display:none;"></div>
  					</td>
  				</tr>`;
    });
    const table = $('#table-creditcard > tbody');
    table.empty();
    table.append(html);
  }

  $(document).off('click', '#deleteCard').on('click', '#deleteCard', function () {
    const cardId = $(this).data('id');
    const userId = $('#btnCreditCard').data('id');

    $('#confirm')
      .modal({
        backdrop: 'static',
        keyboard: false,
      })
      .off('click', '#delete').on('click', '#delete', function () {
        deleteUserCard(userId, cardId);
      });
  });

  function findListUserCardByUserId(userId) {
    const data = {
      userId: userId,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/listUserCreditCards',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        const items = data.result.listUserCard;
        const btn = $('#btn-container-disable');
        btn.removeClass('spinner-background');
        const modal = $('#myModal');
        const container = $('#modal-content-container');
        container.empty();
        container.addClass('modal-lg');

        const content = `<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	        			<h4 class="modal-title">Credit Cards</h4>
	      			</div>
	      			<div class="modal-body" id="body-creditcard">	      				    				    			
	      				<table id="table-creditcard" class="table table-condensed table-responsive">
  							<thead>
  								<tr>
  									<th width="350">Number</th>
  									<th class="text-center" width="150">Type</th>
  									<th class="text-center" width="150">Exp. Date</th>
  									<th class="text-center" width="100">Default</th>
  									<th class="text-center" width="100">Actions</th>
  								</tr>
  							</thead>  							
  							<tbody>						
  							</tbody>  							  							  							  							
						</table>
	      			</div>	      			
	      		</div>`;

        container.append(content);

        _displayListUserCard(items);

        modal.addClass('active');
        modal.css({ display: 'none' });
        modal.modal('show');

        $('#table-creditcard').DataTable({
          dom: '<"toolbar">frtip',
          lengthChange: false,
        });

        $('div.toolbar').html(
          '<button id="btnCreditCardForm" class="btn btn-md btn-default mglBtn">Add</button>'
        );
      },
      error: function (e) {
        console.log('Error: ', e);
        const btn = $('#btnCreditCard');
        btn.removeClass('spinner-background');
      },
    });
  }

  function addUserCreditCard(userId, tokenStripe) {
    const data = {
      userId: userId,
      token: tokenStripe,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/addUserCreditCard',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
    	const modal = $('#body-addCreditcard');
    	modal.addClass('spinner-background');
        $('#submit-spinner').show();
        $('.alert-danger').hide();
        $('#btnSaveCreditCard').attr('disabled', true);
        toastr.options = {
        	"newestOnTop": true,
        	"positionClass": "toast-bottom-center",
        }
      },
      success: function (data) {
        if (data.returnCode === 0) {
          const items = data.result.listUserCard;
          const creditCardModal = $('#creditCardModal');
          const container = $('#modal-content-container');
          container.empty();
          $('#creditCardModal').modal('hide');
          const modal = $('#myModal');
          const container2 = $('#modal-content-container');
          container2.empty();
          $('#myModal').modal('hide');
          refreshDataBeta();
          toastr.success('Record added successfully.');
        } else {
        	var errorMessage = data.returnMessage;
        	var message = errorMessage.split(";");
          $('.alert-danger').text(message[0]);
          $('.alert-danger').show();
        }
      },
      error: function (e) {
        console.log('Error: ', e);
        $('.alert-danger').text(e);
        $('.alert-danger').show();
      },
      complete: function () {
    	  const modal = $('#body-addCreditcard');
    	  modal.removeClass('spinner-background');
        $('#submit-spinner').hide();
        $('#btnSaveCreditCard').attr('disabled', false);
      },
    });
  }

  function deleteUserCard(userId, tokenStripe) {
    const data = {
      userId: userId,
      token: tokenStripe, //Aqui me puedes poner el cardId, lo puedo recibir como token
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/deleteUserCard',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      beforeSend: function () {
    	const modal = $('#body-creditcard');
    	modal.addClass('spinner-backgroud');
        $('#delete-spinner').show();
        $('.delete-cc').hide();
        toastr.options = {
        	"newestOnTop": true,
        	"positionClass": "toast-bottom-center",
        }
      },
      success: function (data) {
    	const modal = $('#myModal');
    	const container = $('#modal-content-container');
    	container.empty();
    	$('#myModal').modal('hide');
    	refreshDataBeta();
    	toastr.success('Record delete successfully.');
      },
      error: function (e) {
        console.log('Error: ', e);
        toastr.error(e.returnMessage);
      },
      complete: function () {
    	const modal = $('#body-creditcard');
    	modal.removeClass('spinner-background');
        $('#delete-spinner').hide();
        $('.delete-cc').show();
      },
    });
  }

  /**
   * Purchase User information
   */
  function purchaseUserInformationByEventCategory(userId, categoyEventId) {
    const payload = {
      userId: userId,
      categoyEventId: categoyEventId,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/purchaseUserInformationByEventCategory',
      data: JSON.stringify(payload),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        const items = data.result.successfulShoppingDetails;

        let html = '';
        $.each(items, function (index, item) {
          html += `<tr>
  							<td>${item.name}</th>
  							<td data-sort="${item.txid}">${item.txdate}</td>
  							<td>${item.txid}</td>
  							<td>${item.useragent}</td>
  							<td class="text-right">${item.amount}</td>
  						</tr>`;
        });

        const modal = $('#myModal');
        const container = $('#modal-content-container');
        container.empty();
        container.addClass('modal-lg');

        const content = `<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	        			<h4 class="modal-title">Payment Information</h4>
	      			</div>
	      			<div class="modal-body">	      				
	      				<table id="table-payments" class="table table-condensed table-responsive">
  							<thead>
  								<tr>
  									<th width="350">Event</th>
  									<th width="250">Date</th>
  									<th width="200">Transaction ID</th>
  									<th width="250">Agent</th>
  									<th width="100">Amount</th>
  								</tr>
  							</thead>  							
  							<tbody>
  								${html}								
  							</tbody>  							  							  							  							
						</table>
	      			</div>	      			
	      		</div>`;

        container.append(content);

        modal.addClass('active');
        modal.css({ display: 'none' });
        modal.modal('show');

        $('#table-payments').DataTable({
          lengthChange: false,
          order: [[2, 'desc']],
        });
      },
      error: function (e) {
        console.log('ERROR: ', e);
      },
    });
  }

  $('.item-life-cycle').on('click', function (e) {
    e.preventDefault();
    element = $(this);
    if (!element.data('disabled')) {
      const eventId = element.data('category-event');
      const userId = element.data('id');
      purchaseUserInformationByEventCategory(userId, eventId);
    }
  });

  /*
  	$(document).on('click', '#btn-transactions', function(e){	
		e.preventDefault();	
		getAllUserTransactions();
	});
	*/

  $('#btn-sell').on('click', function () {
    const email = $(this).data('email');
    const fname = $(this).data('fname');
    const lname = $(this).data('lname');
    const userId = $(this).data('id');

    getEventsAvailableForPurchase(email, fname, lname, userId);
  });

  /*
   * Funcion que se dispara cuando se preciona el boton 'sell' de la hoja de vida.
   * nos retornara los eventos disponibles en el sistema que aun el usuario no ha comprado
   */
  function getEventsAvailableForPurchase(email, firstName, lastName, userId) {
    const data = {
      email: email,
    };

    $.ajax({
      type: 'POST',
      contentType: 'application/json; charset=UTF-8',
      url: path_ajax + 'search/api/getEventsAvailableForPurchase',
      data: JSON.stringify(data),
      dataType: 'json',
      timeout: 100000,
      success: function (data) {
        const items = data.result.eventsAvailable;

        const COMPLIMENTARY_FUNC = rolesFunc.findIndex(
          (v) => v.name == 'COMPLIMENTARY'
        );
        const ADD_ATTENDEE_FUNC = rolesFunc.findIndex(
          (v) => v.name == 'ADD_ATTENDEE'
        );

        let html = '';
        $.each(items, function (index, item) {
        	if (item.customer_id != 462) {
        		const addAttendeeBtn = `<a class="btn btn-md btn-primary btn-tx-act" href="/scr-admin/callCenter${item.key_public}-${item.key_private}?email=${email}&fname=${firstName}&lname=${lastName}&from=userDetail&id=${userId}">Register</a>`;
                const grantAccessBtn =
                  COMPLIMENTARY_FUNC != -1
                    ? `<a class="btn btn-md btn-success btn-tx-act" href="/scr-admin/callCenter${item.key_public}-${item.key_private}?email=${email}&fname=${firstName}&lname=${lastName}&complimentary=true&from=userDetail&id=${userId}">Grant Access</a>`
                    : '';
                const buttons =
                  ADD_ATTENDEE_FUNC != -1 ? addAttendeeBtn + grantAccessBtn : '';
                html += `<tr>
        							<td>${item.fund_name}</th>
        							<td class="text-center">
        								${buttons}
        							</td>
        						</tr>`;
        	}
        });

        const modal = $('#myModal');
        const container = $('#modal-content-container');
        container.empty();
        // if (COMPLIMENTARY_FUNC > -1) container.addClass("modal-lg");

        const content = `<div class="modal-content">
	      			<div class="modal-header">
	        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	        			<h4 class="modal-title">Events Available</h4>
	      			</div>
	      			<div class="modal-body">	      				
	      				<table id="table-events" class="table table-condensed table-responsive">
  							<thead>
  								<tr>
  									<th class="width-auto">Event</th>
  									<th class="width-auto text-center">Action</th>
  								</tr>
  							</thead>  							
  							<tbody>
  								${html}								
  							</tbody>  							  							  							  							
						</table>
	      			</div>	      			
	      		</div>`;

        container.append(content);

        modal.addClass('active');
        modal.css({ display: 'none' });
        modal.modal('show');

        $('#table-events').DataTable({
          lengthChange: false,
        });
      },
      error: function (e) {
        console.log('Error: ', e);
      },
    });
  }
});
