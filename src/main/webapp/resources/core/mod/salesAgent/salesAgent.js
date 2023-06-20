$('.collapseSale').collapse('show');
$(".menuSales").addClass("active");
$("#menuSalesAgent").addClass("active");

var path_ajax = '/scr-admin/';
var selectedRows = [];
var selectAction = false;
var selectRow = false;

document.addEventListener("DOMContentLoaded", function(event) {
	loadDatesFilters();
	setDate30Days();
});

function setDate30Days() {
	var fechaInicioInput = document.getElementById("fromDate");
	var fechaFinInput = document.getElementById("toDate");
	var fechaActual = moment();
	var fechaInicio = moment(fechaActual).subtract(30, 'days');
	var fechaActualFormateada = fechaActual.format('MM-DD-YYYY');
	var fechaInicioFormateada = fechaInicio.format('MM-DD-YYYY');
	fechaInicioInput.value = fechaInicioFormateada;
	fechaFinInput.value = fechaActualFormateada;
}

function loadDatesFilters() {
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json; charset=UTF-8',
	    url: path_ajax + 'search/api/findListAllSelectReports',
	    timeout: 100000,
	    success: function (data) {
			var status = data?.result?.allSelects?.statuSales?.map( s => { 
	    		return '<option value="'+s?.id+'">'+ s?.description +'</option>';
	    	});

			$('#salesStatus').append(status.join(""));
	    },
	    done: function (e) {
	    	console.log('DONE');
	    },
	});
}

$("#generateSalesAgent").click(function() {
	$('#bulkActionSelect').val("");
	var btnApply = document.getElementById("applyBulkActions");
	selectAction = false;
	selectRow = false;
	btnApply.disabled = true;
	var modal = $('.modal-loading');
	modal.addClass('active');
	modal.removeClass('isError');
	modal.addClass('isLoading');
	salesAgent(0);
});

function salesAgent(pageCurrent) {
	var filter = getDetailSalesAgentFilter();
	var startDate, endDate;
	if (Object.keys(filter).length === 0) {
		$('#alert-filter').text('Please fill at least fill one of the input filters');
  	  	$('#alert-filter').show();
		var modal = $('.modal-loading');
		modal.removeClass('active');
		modal.removeClass('isError');
		modal.removeClass('isLoading');
		$(".card-body").html("");
		$('#bulkActions').hide();
	}
	else if ("from_date" in filter && filter["from_date"] != "" &&
			"to_date" in filter && filter["to_date"] != "") {
		if ($("#fromDate").val().length >= 10 && $("#toDate").val().length >= 10) {
			startDate = $("#fromDate").val();
			endDate = $("#toDate").val();
		}
		if (dateRangeCalculation(startDate, endDate)) {
			$('#alert-filter').hide();
			$.ajax({
				type: "POST",
			    contentType: "application/json; charset=UTF-8",
			    url: ajaxPath + "search/api/createReportSalesAgent",
			    data: JSON.stringify(filter),
			    dataType: "json",
			    timeout: 100000,
			    success: function(data) {
			    	$('#alert-filter').hide();
			    	if (data.code === "200") {
			    		var list = data.result;
			    		generateDetailSalesAgentReport(list, pageCurrent);
			    	} else if (data.code === "204") {
			    		alert("Error");
			    		
			    	} else if (data.code === "400") {
			    		alert("Error");
			    		var modal = $('.modal-loading');
			    		modal.removeClass('active');
			    		modal.removeClass('isError');
			    		modal.removeClass('isLoading');
			    	}
			    },
			    error: function(e) {
			    	$('#alert-filter').hide();
			    	console.log("ERROR: ", e);
			    	alert("ERROR");
			    	var modal = $('.modal-loading');
			    	modal.removeClass('active');
			    	modal.removeClass('isError');
			    	modal.removeClass('isLoading');
			    },
			    done: function(e) {
			    	alert("DONE");
			    	console.log("DONE");
			    }
			});
		} else {
			$('#alert-filter').text('The range between the dates cannot be greater than one year');
	  	  	$('#alert-filter').show();
	  	  	var modal = $('.modal-loading');
			modal.removeClass('active');
			modal.removeClass('isError');
			modal.removeClass('isLoading');
			$(".card-body").html("");
			$('#bulkActions').hide();
		}
	}
	else if(("from_date" in filter) === false || ("to_date" in filter) === false) {
		$('#alert-filter').text('Please select a date range');
  	  	$('#alert-filter').show();
  	  	var modal = $('.modal-loading');
		modal.removeClass('active');
		modal.removeClass('isError');
		modal.removeClass('isLoading');
		$(".card-body").html("");
		$('#bulkActions').hide();
	}
}

function dateRangeCalculation(fecha1, fecha2) {
	var fechaInicio = new Date(fecha1);
	var fechaFin = new Date(fecha2);
	var diferencia = fechaFin - fechaInicio;
	var diasDiferencia = diferencia / (1000 * 60 * 60 * 24);
	var aniosDiferencia = diasDiferencia / 365;
	if (aniosDiferencia > 1) {
		return false;
	}
	return true;
}

function getDetailSalesAgentFilter() {
	var filter = {};
	var fechaA = new Date($("#fromDate").val().replace(/-/g, '/'));
	var fechaB = new Date($("#toDate").val().replace(/-/g, '/'));
	
	if ($("#fromDate").val().length >= 10 && $("#toDate").val().length >= 10) {
	    filter["from_date"] = fechaA.getTime();
	    filter["to_date"] = fechaB.getTime();
	} else {
	    $("#fromDate").val("");
	    $("#toDate").val("");
	}
	if ($("#name").val().length > 0)
		filter["u.first_name"] = $("#name").val().trim();
	if ($("#lastName").val().length > 0)
		filter["u.last_name"] = $("#lastName").val().trim();
	if ($("#email").val().trim().length > 0) 
		filter["u.email"] = $("#email").val().trim();
	if ($("#userId").val() != "" && !isNaN($("#userId").val()))
	    filter["u.id"] = parseInt($("#userId").val().trim());
	if ($("#phone").val() != "" && !isNaN($("#phone").val()))
	    filter["u.phone_number"] = parseInt($("#phone").val().trim());
	if ($("#funds").val() != "" && !isNaN($("#funds").val()))
	    filter["f.id"] = parseInt($("#funds").val());
	if ($("#salesStatus").val() != "" && !isNaN($("#salesStatus").val()))
	    filter["utaa.status"] = parseInt($("#salesStatus").val());
	return filter;
}

$("#applyBulkActions").click(function() {
	var bulkAction = $("#bulkActionSelect").val();
	var registros = getSelectedRows();
	if (bulkAction != "" && registros != undefined) {
		if (registros != null && registros != "") {
			const arrayRegister = registros.map(obj => {
				return {
					userId: obj[15],
					userEmail: obj[3].trim()
				};
			});
			if (bulkAction === "5") {
				var modal = new QuickNoteModal(
						null,
						null,
						null,
						null,
						2,
						arrayRegister
				);
				modal.init(); 
			}
			else if (bulkAction === "1") {
				var modal = new DoNotCallModal(
						null,
						null,
						null,
						2,
						arrayRegister
				);
				modal.init(); 
			}
		}
		else {
			console.log("debe seleccionar al menos un registro para aplicar la acción");
		}
	}
	else {
		console.log("debe seleccionar una opción")
	}
	
});

function generateDetailSalesAgentReport(dataResult, pageCurrent) {
	$(".card-body").html(
	    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
	);
	
	var stringReport = "";
	if (dataResult.length > 0) {
		$("#bulkActions").show();
	    for (var i = 0; i < dataResult.length; ++i) {
	    	var date = dataResult[i].dateFormat != null ? dataResult[i].dateFormat : "";
	    	stringReport += "<tr>";
	    	stringReport += "<td><input data-id=" + dataResult[i].userId + " type='checkbox' class='check'></td>";
	    	stringReport += "<td>" + dataResult[i].userName + "</td>";
	    	stringReport += "<td>" + dataResult[i].userLastname + "</td>";
	    	stringReport += "<td> <a href='" + path_ajax + 'userDetail-' + dataResult[i].userId + '?from=agent' + "' >" + dataResult[i].userEmail + '</a></td>';
	    	stringReport += "<td class='center-item'>" + dataResult[i].userId + "</td>";
	    	stringReport += "<td>" + dataResult[i].countryName + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].countryCode + "</td>";
	    	stringReport += "<td>" + dataResult[i].phoneNumber + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].preferredLanguage + "</td>";
	    	stringReport += "<td>" + dataResult[i].agentAssigned + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].daysAssigned + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].sales + "</td>";
	    	stringReport += "<td class='center-item'>" + date + "</td>";
	    	stringReport += "<td>" + dataResult[i].saleStatus + "</td>";
	    	stringReport += "<td>" + getActionAgentBtnTemplate(dataResult[i].userId, dataResult[i].userName, dataResult[i].userLastname, dataResult[i].userEmail, dataResult[i].agentAssigned, dataResult[i].saleStatus, dataResult[i].leadTypeId) + "</td>";
	    	stringReport += "</tr>";
	    }
	}

	var stringHead =
	    "<thead><tr><th><input type='checkbox' class='check-all'></th>" +
	    "<th>Name</th>" +
	    "<th>Last Name</th>" +
	    "<th>Email</th>" +
	    "<th>User Id</th>" +
	    "<th>Country</th>" +
	    "<th>Country Code</th>" +
	    "<th>Phone</th>" +
	    "<th>Communication Language</th>" + 
	    "<th>Agent Assigned</th>" +
	    "<th>Days Assigned</th>" +
	    "<th>Sales</th>" +
	    "<th>Last Call</th>" +
	    "<th>Status</th>" +
	    "<th>Actions</th>" +
		"</tr></thead>" +
	    "<tbody id='detailReport'></tbody>";
	$("#demo-datatables-2").html(stringHead);
	$("#detailReport").html(stringReport);
	datatablesDetailSalesAgentRender(pageCurrent);
	
	var modal = $('.modal-loading');
	modal.removeClass('active');
	modal.removeClass('isError');
	modal.removeClass('isLoading');
}

$(document).on('change', "#bulkActionSelect" ,function(){
	var btnApply = document.getElementById("applyBulkActions");
	var id = $(this).val();
	if (id != "") {
		selectAction = true;
	}
	else {
		selectAction = false;
		btnApply.disabled = true;
	}
	if (selectAction === true && selectRow === true) {
		btnApply.disabled = false;
	}
});

$(document).on('click', '.check-all', function () {
    $('.check').prop('checked', $(this).prop('checked'));
    updateSelectedRows();
});

$(document).on('click', '.check', function () {
    updateSelectedRows();
});


function updateSelectedRows() {
    selectedRows = [];
    var btnApply = document.getElementById("applyBulkActions");
    $('.check:checked').each(function () {
    	var id = $(this).data('id');
        var row = $(this).closest('tr');
        var rowData = row.find('td').map(function () {
            return $(this).text();
        }).get();
        rowData.push(id);
        selectedRows.push(rowData);
    });
    if (Object.keys(selectedRows).length != 0) {
    	selectRow = true;
    }
    else {
    	selectRow = false;
    	btnApply.disabled = true;
    }
    if (selectAction === true && selectRow === true) {
		btnApply.disabled = false;
	}
}

function getSelectedRows() {
    return selectedRows;
}

function getActionAgentBtnTemplate(userId, userFirstName, userLastName, userEmail, agentName, status, leadTypeId) {
	var template;
	if (status == 'CLOSED' || status == 'CLOSED BY USER') 
	{
		template = "";
	}
	else {
		var userName = userFirstName + " " + userLastName;
		var dnc = false;
		if (status == 'DO NOT CALL') dnc = true;
		var btnQuickNote =
				'<div class="select-process" data-id="' +
				userId +
		    	'" data-name="' +
		    	userName +
		    	'" data-email="' +
			    userEmail +
		    	'" data-agent="' +
		    	agentName +
		    	'" data-type="' +
		    	leadTypeId +
		        '" data-process="quickNote" data-toggle="modal" data-target="#myModal" alt="Call Note" title="Call Note"><img class="icon-width" src="resources/core/img/quicknotes.svg" alt="Call Note"></div>';
		var btnDoNotCall =
			dnc == false ?
				'<div class="select-process" data-id="' +
				userId +
		    	'" data-name="' +
		    	userName +
		    	'" data-email="' +
			    userEmail +
			    '" data-type="' +
			    leadTypeId +
		        '" data-process="doNotCall" data-toggle="modal" data-target="#myModal" alt="Do Not Call" title="Do Not Call"><img class="icon-width" src="resources/core/img/no-calls.svg" alt="Do Not Call"></div>'
	        :
	        	'';
		template = (
		    '<div id="action-btn-container-' +
		    userId +
		    '" class="action-btn-container" style="display:flex; justify-content:center;">' +
		    btnQuickNote +
		    btnDoNotCall +
		    '</div>'
		);
	}
	return template;
}

$(document).on('click', '.select-process', function (e) {
	e.preventDefault();
	var modalProcess = $(this).data('process');
	var userId = $(this).data('id');
	var userName = $(this).data('name');
	var userEmail = $(this).data('email');
	var agentName = $(this).data('agent');
	var leadTypeId = $(this).data('type');
	if ('quickNote' == modalProcess) {
	    var modal = new QuickNoteModal(
	      userId,
	      userName,
	      userEmail,
	      agentName,
	      leadTypeId,
	      null
	    );
	    modal.init();
	}
	if ('doNotCall' == modalProcess) {
	    var modal = new DoNotCallModal(
	      userId,
	      userName,
	      userEmail,
	      leadTypeId,
	      null
	    );
	    modal.init();
	}
});

function changeStatus(statusId, listUsers, agentId, leadTypeId) {
    let data = {
    	statusId: statusId,
    	listUsers: listUsers,
    	agentId: agentId,
    	leadTypeId: leadTypeId
    };
    $.ajax({
    	type: 'POST',
	      contentType: 'application/json; charset=UTF-8',
	      url: path_ajax + 'search/api/updateStatuSales',
	      data: JSON.stringify(data),
	      dataType: 'json',
	      timeout: 100000,
	      beforeSend: function () {
	    	  $('.footerBtn').hide();
	    	  $('.footerLoader').show();
	    	  $('.alert-danger').hide();
	    	  $('#footerClose').hide();
	      },
	      success: function (data) {
	    	  if (data.returnCode === 0) {
	    		  $('.footerLoader').hide();
		          $('.footerClose').show();
		          $('.alert-success').text('Action done successfully!');
		          $('.alert-success').show();

	    		  var table = $('#demo-datatables-2').DataTable();
				  var pageCurrent = table.page.info();
				  $('#bulkActionSelect').val("");
				  var btnApply = document.getElementById("applyBulkActions");
				  selectAction = false;
				  selectRow = false;
				  btnApply.disabled = true;
	    		  salesAgent(pageCurrent.page);
	    	  }
	    	  else if (data.returnCode === -49) {
	    		  $('.footerLoader').hide();
	    		  $('.footerClose').show();
	    		  $('.alert-danger').text('An error has occurred');
		          $('.alert-danger').show();
	    	  }
	    	  else {
	    		  $('.footerLoader').hide();
	    		  $('.footerClose').show();
	    		  $('.alert-danger').text('An error has occurred');
		          $('.alert-danger').show();
	    	  }
	      },
	      error: function (e) {
	    	  console.log('ERROR: ', e);
	    	  $('.footerLoader').hide();
	    	  $('.footerClose').show();
	    	  $('.alert-danger').text('Unexpected error has ocurred, please contact support.');
	    	  $('.alert-danger').show();
	      },
    });
}

function saveQuicknote(listUsers, comment, leadTypeId) {
    let data = {
    	listUsers: listUsers,
    	comment: comment,
    	leadTypeId: leadTypeId
    };
    $.ajax({
    	type: 'POST',
	      contentType: 'application/json; charset=UTF-8',
	      url: path_ajax + 'search/api/addBusinessListUsersSupportHistory',
	      data: JSON.stringify(data),
	      dataType: 'json',
	      timeout: 100000,
	      beforeSend: function () {
	    	  $('.footerBtn').hide();
	    	  $('.footerLoader').show();
	    	  $('.alert-danger').hide();
	    	  $('#footerClose').hide();
	      },
	      success: function (data) {
	    	  if (data.returnCode === 0) {
	    		  $('.footerLoader').hide();
		          $('.footerClose').show();
		          $('.alert-success').text('Action done successfully!');
		          $('.alert-success').show();

	    		  var table = $('#demo-datatables-2').DataTable();
				  var pageCurrent = table.page.info();
				  $('#bulkActionSelect').val("");
				  var btnApply = document.getElementById("applyBulkActions");
				  selectAction = false;
				  selectRow = false;
				  btnApply.disabled = true;
	    		  salesAgent(pageCurrent.page);
	    	  }
	    	  else {
	    		  $('.footerLoader').hide();
	    		  $('.footerClose').show();
	    		  $('.alert-danger').text('An error has occurred');
		          $('.alert-danger').show();
	    	  }
	      },
	      error: function (e) {
	    	  console.log('ERROR: ', e);
	    	  $('.footerLoader').hide();
	    	  $('.footerClose').show();
	    	  $('.alert-danger').text('Unexpected error has ocurred, please contact support.');
	    	  $('.alert-danger').show();
	      },
    });
}

function QuickNoteModal(userId, userName, userEmail, agentName, leadTypeId, listBulk) {
	var id = "", name = "", email = "", agentName = "", listUsers = [];
	if (listBulk === null) {
		id = userId;
		name = userName;
		email = userEmail;
		agentName = agentName;

		let objectUser = {
		    userId: userId,
		    userEmail: userEmail
		  };
		listUsers.push(objectUser);
	}
	else {
		listUsers = listBulk;
		name = "selected";
	}
	
	function init() {
		$('#myModal').removeClass('doNotCallModal');
		$('#myModal').addClass('quickNoteModal');
	    
	    var template = generateQuickNoteModal();
	    $('#modal-content-container').html(template);
	    
	    $('#save-quicknote-btn').click(function () {
	    	var comment;
	    	if ($('#quicknote-text').val() != "") {
	    		$('#alert-quicknote').hide();
	    		comment = "CALL-NOTE: " +  $('#quicknote-text').val();
	    		saveQuicknote(listUsers, comment, leadTypeId);
	    	}
	    	else {
	    		$('#alert-quicknote').text('Please fill at least fill one of the input filters');
	      	  	$('#alert-quicknote').show();
	    	}
	    	
		});
	}

	function generateQuickNoteModal() {
	    return `
			<div class="modal-content">
      			<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        			<h4 class="modal-title">Call Note</h4>
      			</div>
      			<div class="modal-body">	      				
      				<form id="quicknote-form">
		        		<div class="mail-container">	      				
		      				<label>
		    					Description:
		    				</label>
		    				<textarea id="quicknote-text" class="form-control" name="quicknote-text" maxlength="200" minlength="3" required></textarea>
		    				<div id="alert-quicknote" class="alertFilter">
								<span class="error-message"></span>
							</div>
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
		      		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        	<button id="save-quicknote-btn" type="submit" class="btn btn-primary">Accept</button>
		      	</div>   	
		      	<div class="modal-footer footerClose" style="display:none;">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			    </div>
			    <div class="modal-footer footerLoader" style="display:none;">
			      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
			    </div>  			
      		</div>`;
	}
	
	return {
	    init: init
	};
}

function DoNotCallModal(userId, userName, userEmail, leadTypeId, listBulk) {
	var id = "", name = "", email = "", listUsers = [];
	if (listBulk === null) {
		id = userId;
		name = userName;
		email = userEmail;

		let objectUser = {
		    userId: userId,
		    userEmail: userEmail
		  };
		listUsers.push(objectUser);
	}
	else {
		listUsers = listBulk;
		name = "selected";
	}
	
	function init() {
		$('#myModal').addClass('doNotCallModal');
		$('#myModal').removeClass('quickNoteModal');
	    
	    var template = generateDoNotCallModal();
	    $('#modal-content-container').html(template);
	    
	    $('#save-donotcall-btn').click(function () {
	    	changeStatus(1, listUsers, null, leadTypeId);
		});
	}

	function generateDoNotCallModal() {
	    return `
			<div class="modal-content">
      			<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        			<h4 class="modal-title">Do Not Call</h4>
      			</div>
      			<div class="modal-body">	      				
      				<label>
    					Confirm that user ${name} does not want to receive calls?
    				</label>
    				<div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  			<a href="#" class="alert-link success-msg-alert"></a>
					</div>
					<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
						<a href="#" class="alert-link error-msg-alert"></a>
					</div>
      			</div>	 
      			<div class="modal-footer footerBtn">
		      		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        	<button id="save-donotcall-btn" type="submit" class="btn btn-primary">Accept</button>
		      	</div>  
		      	<div class="modal-footer footerClose" style="display:none;">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			    </div>
			    <div class="modal-footer footerLoader" style="display:none;">
			      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
			    </div>     			
      		</div>`;
	}
	
	return {
	    init: init
	};
}

function datatablesDetailSalesAgentRender(pageCurrent) {
	var DataTable = $.fn.dataTable;
	 $.extend(true, DataTable.Buttons.defaults, {
		 dom: {
			 button: {
				 className: "btn btn-outline-primary btn-sm"
			 }
		 }
	 });

	 var $datatablesButtons = $("#demo-datatables-2").DataTable({
	 "createdRow": function (row, data, dataIndex) {
		 if (data[11] === "YES" && data[13] != "DO NOT CALL") {
            $(row).find('td:eq(11)').css('background-color', '#c5e0b3');
		 } else if (data[11] === "NO" && data[13] != "DO NOT CALL") {
            $(row).find('td:eq(11)').css('background-color', '#f6ccad');
		 } else if (data[11] === "PENDING" && data[13] != "DO NOT CALL") {
			 $(row).find('td:eq(11)').css('background-color', 'rgb(246 243 173)');
		 }
		 if (data[13] === "DO NOT CALL") {
			$(row).find('td:eq(13)').css('color', 'red');
			$(row).find('td:eq(13)').css('background-color', 'transparent');
		 }
	 },
	 columnDefs: [
        {
            orderable: false,
            className: 'select-checkbox',
            targets:   0,
            checkboxes: {
                selectRow: true
            }
        }
    ],
    select: {
        style:    'multi',
        selector: 'td:first-child'
    },
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
            .html("Agent View Report");
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
	         title: 'Agent View Report'
	     },
	     {
	         extend: 'excelHtml5',
	         title: 'Agent View Report'
	     },
	     {
	         extend: 'csv',
	         title: 'Agent View Report'
	     },
	     {
	         extend: 'colvis',
	         title: 'Agent View Report'
	     },
	     {
	         extend: 'pageLength',
	         title: 'Agent View Report'
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
    	 order: [[1, "asc"]],
    	 lengthMenu: [
    		 [10, 25, 50, -1],
    		 ["10 rows", "25 rows", "50 rows", "Show all"]
    	 ],
    	 "pageLength": 25
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

$("#cleanFilters").click(function() {
	$('#fromDate').val("");
	$('#toDate').val("");
	$('#name').val("");
	$('#lastName').val("");
	$('#email').val("");
	$('#userId').val("");
	$('#phone').val("");
	$('#funds').val("");
	$('#salesStatus').val("");
	$(".card-body").html("");
	$('#bulkActions').hide();
	$('#alert-filter').hide();
	$('#bulkActionSelect').val("");
	$('#alert-quicknote').hide();
});

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