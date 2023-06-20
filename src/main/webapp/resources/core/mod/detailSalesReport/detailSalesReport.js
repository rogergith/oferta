$('.collapseReport').collapse('show');
$('.collapseReport2').collapse('hide');
$('.collapseSaleReport').collapse('show');
$('.collapseSaleReport2').collapse('show');
$(".menuSalesReport").addClass("active");
$("#menuReporteVentaDetalle").addClass("active");

var path_ajax = '/scr-admin/';

document.addEventListener("DOMContentLoaded", function(event) {
	loadDatesFiltersDetails();
});

function loadDatesFiltersDetails() {
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json; charset=UTF-8',
	    url: path_ajax + 'search/api/findListAllSelectReports',
	    timeout: 100000,
	    success: function (data) {
	        var formatedData = data.result.allSelects.paymentTypeForAffiliate.map( d => { 
	    		return '<option value="'+d.id+'">'+d.name+'</option>';
	    	});
	    	$('#paymentType').append(formatedData.join(""));
	    },
	    done: function (e) {
	    	console.log('DONE');
	    },
	});
}

function getDetailSalesFilter() {
	var filter = {};
	if ($("#fromDate").val().length == 10 && $("#toDate").val().length == 10) {
	    filter["date_min"] = convertFromDate();
	    filter["date_max"] = convertToDate();
	} else {
	    $("#fromDate").val("");
	    $("#toDate").val("");
	}
	if ($("#txId").val().length > 0)
		filter["t.tx_id"] = $("#txId").val();
	if ($("#stripTrxId").val().length > 0)
		filter["t.gwauthid_1"] = $("#stripTrxId").val();
	if ($("#email").val().trim().length > 0) 
		filter["u.email"] = $("#email").val().trim();
	if ($("#funds").val() != "" && !isNaN($("#funds").val()))
	    filter["f.id"] = parseInt($("#funds").val());
	if ($("#paymentType").val() != "" && !isNaN($("#paymentType").val()))
	    filter["pt.id"] = parseInt($("#paymentType").val());
	if ($("#affiliateId").val().length > 0)
		filter["a.id"] = parseInt($("#affiliateId").val());
	if ($("#affiliateEmail").val().trim().length > 0) 
		filter["u2.email"] = $("#affiliateEmail").val().trim();
	return filter;
}

$("#generateDetailSalesReport").click(function() {
	var filter = getDetailSalesFilter();
	$.ajax({
		type: "POST",
	    contentType: "application/json; charset=UTF-8",
	    url: ajaxPath + "search/api/createReportAffiliateMasterDetail",
	    data: JSON.stringify(filter),
	    dataType: "json",
	    timeout: 100000,
	    success: function(data) {
	    	if (data.code === "200") {
	    		generateDetailSalesReport(data.result);
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

function generateDetailSalesReport(dataResult) {
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
	    	stringReport += "<td>" + dataResult[i].dataPayment + "</td>";
	    	stringReport += "<td>" + dataResult[i].affiliationPayed + "</td>";
	    	stringReport += "<td>" + dataResult[i].paymentReference + "</td>";
	    	stringReport += "<td>" + dataResult[i].transactionId + "</td>";
	    	stringReport += "<td>" + dataResult[i].stripTrxId + "</td>";
	    	stringReport += "<td>" + dataResult[i].nameReferred + "</td>";
	    	stringReport += "<td>" + dataResult[i].userEmail + "</td>";
	    	stringReport += "<td>" + dataResult[i].dateFormat + "</td>";
	    	stringReport += "<td>" + dataResult[i].eventName + "</td>";
	    	stringReport += "<td>" + dataResult[i].paymentReferred + "</td>";
	    	stringReport += "<td>" + dataResult[i].amount + "</td>";
	    	stringReport += "<td>" + dataResult[i].comission + "</td>";
	    	stringReport += "</tr>";
	    }
	}

	var stringHead =
	    "<thead><tr><th>Affiliate</th>" +
	    "<th>Affiliate Name</th>" +
	    "<th>Affiliate Email</th>" +
	    "<th>Payment Type Affiliate</th>" +
	    "<th>Data Payment</th>" +
	    "<th>Affiliation Paid</th>" +
	    "<th>Payment Reference</th>" + 
	    "<th>Transaction Id</th>" +
	    "<th>Strip Trx Id</th>" +
	    "<th>Name Referred</th>" +
	    "<th>User Email</th>" +
	    "<th>Date</th>" +
	    "<th>Event Name</th>" +
	    "<th>Payment Type</th>" +
	    "<th>Amount</th>" +
	    "<th>Comission</th>" +
		"</tr></thead>" +
	    "<tbody id='detailReport'></tbody>";
	$("#demo-datatables-2").html(stringHead);
	$("#detailReport").html(stringReport);
	datatablesDetailSalesRender();
}

function datatablesDetailSalesRender() {
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
	         title: 'Affiliate Report'
	     },
	     {
	         extend: 'excelHtml5',
	         title: 'Affiliate Report'
	     },
	     {
	         extend: 'csv',
	         title: 'Affiliate Report'
	     },
	     {
	         extend: 'colvis',
	         title: 'Affiliate Report'
	     },
	     {
	         extend: 'pageLength',
	         title: 'Affiliate Report'
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
    	 order: [[15, ""]],
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

function printFilters() {
	var filters = "</br>";
	var filter = getDetailSalesFilter();
	if (filter["contribution_date_min"] != undefined && filter["contribution_date_max"] != undefined) {
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