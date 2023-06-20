$('.collapseReport').collapse('show');
$('.collapseReport2').collapse('hide');
$('.collapseSaleReport').collapse('show');
$('.collapseSaleReport2').collapse('show');
$(".menuSalesReport").addClass("active");
$("#menuReporteVentaGeneral").addClass("active");

var path_ajax = '/scr-admin/';
var detailTable;

document.addEventListener("DOMContentLoaded", function(event) {
	loadDatesFilters();
});

function loadDatesFilters() {
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json; charset=UTF-8',
	    url: path_ajax + 'search/api/findResponseDateOptionsReports',
	    timeout: 100000,
	    success: function (data) {
	        var formatedData = data.result.options.map( d => { 
	    		return '<option value="'+d.value+'">'+d.label+'</option>';
	    	});
	    	$('#dateMonths').append(formatedData.join(""));
	    },
	    done: function (e) {
	    	console.log('DONE');
	    },
	});
}

function getSalesFilter() {
	var filter = {};
	if ($("#email").val().trim().length > 0) filter["person_email"] = $("#email").val().trim();
	if ($("#dateMonths").val() != "")
	    filter["date_months"] = $("#dateMonths").val();
	return filter;
}

$("#generateSalesReport").click(function() {
	createReport(0);
});

function createReport(pageCurrent) {
	var filter = getSalesFilter();
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=UTF-8",
	    url: ajaxPath + "search/api/createReportAffiliateMaster",
	    data: JSON.stringify(filter),
	    dataType: "json",
	    timeout: 100000,
	    success: function(data) {
	    	if (data.code === "200") {
	    		generateSalesReport(data.result, pageCurrent);
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
}

function generateSalesReport(dataResult, pageCurrent) {
	$(".card-body").html(
	    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
	);

	var stringReport = "";
	if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
	    for (var i = 0; i < dataResult.length; ++i) {
	    	stringReport += "<tr>";
	    	stringReport += "<td>" + dataResult[i].affiliate + "</td>";
	    	stringReport += "<td>" + dataResult[i].affiliateName + "</td>";
	    	stringReport += "<td>" + dataResult[i].affiliateEmail + "</td>";
	    	stringReport += "<td>" + dataResult[i].paymentTypeAffiliate + "</td>";
	    	stringReport += "<td>" + dataResult[i].countAffiliationConvertion + "</td>";
	    	stringReport += "<td>" + dataResult[i].sumAffiliationBalance + "</td>";
	    	stringReport += "<td>" + dataResult[i].dataPayment + "</td>";
	    	stringReport += "<td>" + dataResult[i].paymentReference + "</td>";
	    	stringReport += "<td>" + dataResult[i].affiliationPayed + "</td>";
	    	stringReport += "<td>" + getTableActionBtnTemplate(dataResult[i].affiliate, dataResult[i].affiliateName, dataResult[i].affiliationPayed) + "</td>";
	    	stringReport += "</tr>";
	    }
	}

	var stringHead =
	    "<thead><tr>" +
	    "<th>Affiliate Id</th>" +
	    "<th>Affiliate Name</th>" +
	    "<th>Affiliate Email</th>" +
	    "<th>Payment Type Affiliate</th>" +
	    "<th>Affiliation Convertion</th>" +
	    "<th>Affiliation Balance</th>" +
	    "<th>Payment Info</th>" +
	    "<th>Payment Confirmation</th>" +
	    "<th>Processed</th>" +
	    "<th>Actions</th>" +
		"</tr></thead>" +
	    "<tbody id='detailReport'></tbody>";
	$("#demo-datatables-2").html(stringHead);
	$("#detailReport").html(stringReport);
	datatablesSalesRender(pageCurrent);
}

function getTableActionBtnTemplate(affiliateId, affiliateName, affiliationPayed) {
	var date = null;
	if ($("#dateMonths").val() != "")
	    date = $("#dateMonths").val();
	var modify_payment = rolesFunc.findIndex((v) => v.name == 'REPORT_AFFILIATE_UPDATE');
	var paid = false;
	if (affiliationPayed == 'Paid') paid = true;
	var btnPayment =
		modify_payment != -1 && paid == false
	      ? '<div class="select-process" data-id="' +
	    	affiliateId +
	    	'" data-name="' +
	    	affiliateName +
	    	'" data-date="' +
	        date +
	        '" data-process="payment" data-toggle="modal" data-target="#myModal" alt="Add payment" title="Add payment"><span class="fa fa-credit-card" style="padding-right: 5px; font-size: 1.3em; cursor:pointer;"></span></div>'
	      : '';
	var btnDetails =
			'<div class="select-process" data-id="' +
	    	affiliateId +
	    	'" data-name="' +
	    	affiliateName +
	    	'" data-date="' +
	        date +
	        '" data-process="details" data-toggle="modal" data-target="#myModal" alt="View details" title="View details"><span class="fa fa-file-text-o" style="padding-right: 5px; font-size: 1.3em;cursor:pointer;"></span></div>';
	return (
	    '<div id="action-btn-container-' +
	    affiliateId +
	    '" class="action-btn-container" style="display:flex; justify-content:center;">' +
	    btnDetails +
	    btnPayment +
	    '</div>'
	);
}

$(document).on('click', '.select-process', function (e) {
	e.preventDefault();
	var modalProcess = $(this).data('process');
	var affiliateId = $(this).data('id');
	var affiliateName = $(this).data('name');
	var date = $(this).data('date');
	if ('payment' == modalProcess) {
	    var modal = new PaymentModal(
	      affiliateId,
	      affiliateName,
	      date
	    );
	    modal.init(); 
	}
	if ('details' == modalProcess) {
	    var modal = new DetailsModal(
	      affiliateId,
	      affiliateName,
	      date
	    );
	    modal.init();
	}
});

function DetailsModal(affiliateId, affiliateName, date) {
	var id = affiliateId;
	var name = affiliateName;
	var date = date;

	function getId() {
		return id;
	}
	
	function getName() {
		return name;
	}

	function getDate() {
	    return date;
	}

	function init() {
		$('#myModal').removeClass('paymentModal');
	    $('#myModal').addClass('detailsModal');
	    requestReportDetail();
	}
	
	function requestReportDetail() {
	    var data = { 
	    	affiliate_id: id, 
	    	date_months: date 
	    };
	    $.ajax({
	    	type: 'POST',
	    	contentType: 'application/json; charset=UTF-8',
	    	url: path_ajax + 'search/api/createReportAffiliateDetail',
	    	data: JSON.stringify(data),
	    	dataType: 'json',
	    	timeout: 100000,
	    	beforeSend: function () {
	    		$('#modal-content-container').html('');
	    	},
	    	success: function (data) {
	    		processDataDetail(data);		
	    	},
	    	error: function (e) {
	    		$('#myModal').modal('hide');
		        console.log("ERROR: ", e);
	    	},
	    });
	}
	
	function processDataDetail(data) {
		if (data.code == 200) {
			var formatedData = data.result.map((r) => {
				var transactionId = r.transactionId == null ? '--' : r.transactionId;
				var stripTrxId = r.stripTrxId == null ? '--' : r.stripTrxId;
				var userEmail = r.userEmail == null ? '--' : r.userEmail;
				var dateFormat = r.dateFormat == null ? '--' : r.dateFormat;
				var eventName = r.eventName == null ? '--' : r.eventName;
				var paymentType = r.paymentReferred == null ? '--' : r.paymentReferred;
				var amount = r.amount == null ? '--' : r.amount;
				var comission = r.comission == null ? '--' : r.comission;
				var affiliateEmail = r.affiliateEmail == null ? '--' : r.affiliateEmail;
				var affiliateId = r.affiliate == null ? '--' : r.affiliate;
				
				let body;

				body = 
					`<tr>	
	            		<td class="hf-center-cell">${transactionId}</td>						
						<td class="hf-center-cell">${stripTrxId}</td>
						<td class="hf-center-cell">${userEmail}</td>
						<td class="hf-center-cell">${dateFormat}</td>
						<td class="hf-right-cell">${eventName}</td>
						<td class="hf-center-cell">${paymentType}</td>
						<td class="hf-center-cell">${amount}</td>
						<td class="hf-center-cell">${comission}</td>
						<td class="hf-center-cell">${affiliateEmail}</td>
						<td class="hf-center-cell">${affiliateId}</td>
					</tr>`;
				return body;
			});
         
			renderDataDetails(formatedData);
		}
	}
	
	function renderDataDetails(data) {
		var template = generateDetailsModal(data);
		$('#modal-content-container').html(template);
	    $('.details-body').show();
	    $('.signature-body').hide();

	    detailTable = $('#details-table').DataTable({
	    	ordering: true,
	    	order: [[0, 'desc']],

	    	dom:
	    		"<'row'<'col-sm-6'><'col-sm-6'f>>" +
	    		"<'table-responsive'tr>" +
	    		"<'row'<'col-sm-6'i><'col-sm-6'p>>",
	    });
	    $('#details-table_filter input').addClass('form-control');
	    detailTable.columns.adjust().draw();
	    $('.table').css({"width":"100%"});
	}
	
	function generateDetailsModal(data) {
	    var dataJoined = data.join('');
	    return `
			<div class="modal-content details-body">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title">Affiliate Report: ${id} - ${name}</h4>
		      	</div>
				<div class="modal-body">
					<table id="details-table" class="table-striped" style="width:100%;">
						<thead>
							<tr>
								<th>Transaction ID</th>
								<th>Strip Trx Id</th>
								<th>User Email</th>													
								<th>Date</th>
								<th>Event Name</th>
								<th>Payment Type</th>
								<th>Amount</th>
								<th>Comission</th>
								<th>Affiliate Email</th>
								<th>Affiliate ID</th>
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
			`;
	}

	return {
	    init: init,
	    getId: getId,
	    getName: getName,
	    getDate: getDate,
	};
}

function PaymentModal(affiliateId, affiliateName, date) {
	var id = affiliateId;
	var name = affiliateName;
	var date = date;
	
	function getId() {
		return id;
	}
	
	function getName() {
		return name;
	}
	
	function getDate() {
	    return date;
	}
	
	function init() {
		$('#myModal').removeClass('detailsModal');
	    $('#myModal').addClass('paymentModal');
	    
	    var template = generatePaymentModal();
	    $('#modal-content-container').html(template);
	    
	    $('#save-payment-btn').click(function () {
		    $('#payment-form').submit();
		});
	    
	    validator = $('#payment-form').validate({
	        rules: {
	        	paymentConfirmation: {
	        		required: true,
	        		minlength: 3,
	                maxlength: 20
	        	},
	        },
	        submitHandler: savePaymentReference,
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
	        	$("#payment-reference").addClass("has-error");
	        },
	    });
	}

	function generatePaymentModal() {
	    return `
			<div class="modal-content details-body">
		    	<div class="modal-header ">
		        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title">Paying to: ${id} - ${name}</h4>
		      	</div>
		      	<div class="modal-body">
		       		<div id="signature-container">
		        		<form id="payment-form">
		        			<div class="mail-container">
		        				<label for"mail">
		        					Payment Confirmation:
		        				</label>
		        				<textarea id="payment-reference" class="form-control" name="payment-reference" maxlength="20" minlength="3" required></textarea>
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
		      	</div>
		      	<div class="modal-footer footerBtn">
		      		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        	<button id="save-payment-btn" type="submit" class="btn btn-primary">Save</button>
		      	</div>
		      	<div class="modal-footer footerClose" style="display:none;">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			    </div>
			    <div class="modal-footer footerLoader" style="display:none;">
			      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
			    </div>
		    </div>`;
	}
	
	function savePaymentReference() {
	    let data = {
	    	affiliateId: id,
	    	dateFilter: date,
	    	paymentReference: $('#payment-reference').val()
	    };

	    $.ajax({
	    	type: 'POST',
		      contentType: 'application/json; charset=UTF-8',
		      url: path_ajax + 'search/api/findResponseUpdatePaymentAffiliateReport',
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
		    	  if (data.result.updateTransactions.transactions) {
		    		  $('.footerLoader').hide();
			          $('.footerClose').show();
			          $('#save-payment-btn').hide();
			          $('.alert-success').text('Payment reference added successfully!');
			          $('.alert-success').show();
			          var table = $('#demo-datatables-2').DataTable();
					  var pageCurrent = table.page.info();
			          createReport(pageCurrent.page);
			          
					  
					  console.log(pageCurrent);
					  
		    	  } else {
			          $('.footerLoader').hide();
			          $('.footerBtn').show();
			          $('.alert-danger').text('Cannot process payments in the current month');
			          $('.alert-danger').show();
		        }
		      },
		      error: function (e) {
		    	  console.log('ERROR: ', e);
		    	  $('.footerLoader').hide();
		    	  $('.footerBtn').show();
		    	  $('.alert-danger').text('Unexpected error has ocurred, please contact support.');
		    	  $('.alert-danger').show();
		      },
	    });
	}

	return {
	    init: init,
	    getId: getId,
	    getName: getName,
	    getDate: getDate
	};
}

function datatablesSalesRender(pageCurrent) {
	var DataTable = $.fn.dataTable;
	$.extend(true, DataTable.Buttons.defaults, {
		 dom: {
			 button: {
				 className: "btn btn-outline-primary btn-sm"
			 }
		 }
	 });

	 var $datatablesButtons = $("#demo-datatables-2").DataTable({
	 buttons: [{
		 extend: "print",
		 text: "Print",
		 autoPrint: false,
		 exportOptions: {
			 columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
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
            .html("Affiliate Report");
			 $(win.document.body)
            .find("div")
            .css("text-align", "center");
			 $(win.document.body)
            .find("div")
            .css("margin-top", "-17px")
            .append(printFilters());
        }
	 },
	 {
         extend: 'copy',
         title: 'Affiliate Report',
         exportOptions: {
             columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
         }
     },
     {
         extend: 'excelHtml5',
         title: 'Affiliate Report',
         exportOptions: {
             columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
         }
     },
     {
         extend: 'csv',
         title: 'Affiliate Report',
         exportOptions: {
             columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
         }
     },
     {
         extend: 'colvis',
         title: 'Affiliate Report',
         exportOptions: {
             columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
         }
     },
     {
         extend: 'pageLength',
         title: 'Affiliate Report',
         exportOptions: {
             columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
         }
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
    	 order: [[0, "asc"]],
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
  	
  	$datatablesButtons.page( parseInt(pageCurrent) ).draw( 'page' );
}

function printFilters() {
	var filters = "</br>";
	var filter = getSalesFilter();
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