$('.collapseSale').collapse('show');
$(".menuSales").addClass("active");
$("#menuSalesSupervisor").addClass("active");

var path_ajax = '/scr-admin/';
var listAgent = "";
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
	    	var formatedData = data.result.allSelects.listEventSRC.map( s => { 
	    		return '<option value="'+s.id+'">'+s.name+'</option>';
	    	});

			var agents = data?.result?.allSelects?.agentSales?.map( s => { 
	    		return '<option value="'+s?.id+'">'+ s?.firstName + ' ' + s?.lastName +'</option>';
	    	});
			
			var status = data?.result?.allSelects?.statuSales?.map( s => { 
	    		return '<option value="'+s?.id+'">'+ s?.description +'</option>';
	    	});
			
			var typeLead = data?.result?.allSelects?.listLeadType?.map( s => { 
	    		return '<option value="'+s?.id+'">'+ s?.description +'</option>';
	    	});

	    	$('#funds').append(formatedData.join(""));
			$('#agents').append(agents.join(""));
			$('#salesStatus').append(status.join(""));
			$('#typeLead').append(typeLead.join(""));

	    	listAgent = data.result.allSelects.agentSales;
	    	
	    	var selectTypeLead = document.getElementById("typeLead");
	    	selectTypeLead.selectedIndex = 2;
	    },
	    done: function (e) {
	    	console.log('DONE');
	    },
	});
}

$("#generateSalesSupervisor").click(function() {
	$('#bulkActionSelect').val("");
	$('#alert-filter').hide();
	var btnApply = document.getElementById("applyBulkActions");
	selectAction = false;
	selectRow = false;
	btnApply.disabled = true;
	var modal = $('.modal-loading');
	modal.addClass('active');
	modal.removeClass('isError');
	modal.addClass('isLoading');
	salesSupervisor(0);
});

function salesSupervisor(pageCurrent) {
	var filter = getDetailSalesSuperFilter();
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
	else if ("lead_type" in filter && filter["lead_type"] != "" && 
			"from_date" in filter && filter["from_date"] != "" &&
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
			    url: ajaxPath + "search/api/createReportSalesAdmin",
			    data: JSON.stringify(filter),
			    dataType: "json",
			    timeout: 100000,
			    success: function(data) {
			    	$('#alert-filter').hide();
			    	if (data.code === "200") {
			    		var list = data.result;
			    		generateDetailSalesSuperReport(list, pageCurrent);
			    	} else if (data.code === "204") {
			    		alert("Error");
			    		var modal = $('.modal-loading');
			    		modal.removeClass('active');
			    		modal.removeClass('isError');
			    		modal.removeClass('isLoading');
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
			    	$(".card-body").html("");
					$('#bulkActions').hide();
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
	else if(("lead_type" in filter) === false) {
		$('#alert-filter').text('Please select a lead type');
  	  	$('#alert-filter').show();
  	  	var modal = $('.modal-loading');
		modal.removeClass('active');
		modal.removeClass('isError');
		modal.removeClass('isLoading');
		$(".card-body").html("");
		$('#bulkActions').hide();
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

function getDetailSalesSuperFilter() {
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
	if ($("#agents").val() != "" && !isNaN($("#agents").val()))
		filter["u_agent.id"] = parseInt($("#agents").val());
	if ($("#typeLead").val() != "" && !isNaN($("#typeLead").val()))
		filter["lead_type"] = parseInt($("#typeLead").val());
	return filter;
}

$("#applyBulkActions").click(function() {
	var bulkAction = $("#bulkActionSelect").val();
	var registros = getSelectedRows();
	if (bulkAction != "" && registros != undefined) {
		if (registros != null && registros != "") {
			const arrayRegister = registros.map(obj => {
				return {
					userId: obj[16],
					userEmail: obj[3].trim()
				};
			});
			const leadType = registros[0][17];
			if (bulkAction === "0") {
				var modal = new AssignModal(
						null,
						null,
						null,
						leadType,
						arrayRegister
				);
				modal.init(); 
			}
			else if (bulkAction === "3") {
				var modal = new UnassignModal(
						null,
						null,
						null,
						null,
						leadType,
						arrayRegister
				);
				modal.init(); 
			}
			else if (bulkAction === "1") {
				var modal = new DoNotCallModal(
						null,
						null,
						null,
						leadType,
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

function generateDetailSalesSuperReport(dataResult, pageCurrent) {
	$(".card-body").html(
	    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
	);
	
	var stringReport = "";
	if (dataResult.length > 0) {
		$("#bulkActions").show();
	    for (var i = 0; i < dataResult.length; ++i) {
	    	var date = dataResult[i].dateFormat != null ? dataResult[i].dateFormat : "";
	    	stringReport += "<tr>";
	    	stringReport += "<td><input data-id=" + dataResult[i].userId + " data-type=" + dataResult[i].leadTypeId + " type='checkbox' class='check'></td>";
	    	stringReport += "<td>" + dataResult[i].userName + "</td>";
	    	stringReport += "<td>" + dataResult[i].userLastname + "</td>";
	    	stringReport += "<td> <a href='" + path_ajax + 'userDetail-' + dataResult[i].userId + '?from=supervisor' + "' >" + dataResult[i].userEmail + '</a></td>';
	    	stringReport += "<td class='center-item'>" + dataResult[i].userId + "</td>";
	    	stringReport += "<td>" + dataResult[i].countryName + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].countryCode + "</td>";
	    	stringReport += "<td>" + dataResult[i].phoneNumber + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].preferredLanguage + "</td>";
	    	stringReport += "<td>" + dataResult[i].leadType + "</td>";
	    	stringReport += "<td>" + dataResult[i].agentAssigned + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].daysAssigned + "</td>";
	    	stringReport += "<td class='center-item'>" + dataResult[i].sales + "</td>";
	    	stringReport += "<td class='center-item'>" + date + "</td>";
	    	stringReport += "<td>" + dataResult[i].saleStatus + "</td>";
	    	stringReport += "<td>" + getActionSuperBtnTemplate(dataResult[i].userId, dataResult[i].userName, dataResult[i].userLastname, dataResult[i].userEmail, dataResult[i].agentAssigned, dataResult[i].saleStatus, dataResult[i].leadTypeId) + "</td>";
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
	    "<th>Lead Type</th>" +
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
	datatablesDetailSalesSuperRender(pageCurrent);
	
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
	var btnApply = document.getElementById("applyBulkActions");
    selectedRows = [];
    $('.check:checked').each(function () {
    	var id = $(this).data('id');
    	var type = $(this).data('type');
        var row = $(this).closest('tr');
        var rowData = row.find('td').map(function () {
            return $(this).text();
        }).get();
        rowData.push(id);
        rowData.push(type);
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

function getActionSuperBtnTemplate(userId, userFirstName, userLastName, userEmail, agentName, status, leadTypeId) {
	var template;
	if (status == 'CLOSED' || status == 'CLOSED BY USER') 
	{
		template = "";
	}
	else {
		var userName = userFirstName + " " + userLastName;
//		var modify_payment = rolesFunc.findIndex((v) => v.name == 'REPORT_AFFILIATE_UPDATE');
//		modify_payment != -1 // si tiene el permiso
		var dnc = false;
		var assign = false;
		if (status == 'DO NOT CALL') dnc = true;
		if (status == 'ASSIGNED') assign = true;
		var btnAssign =
			assign == false && dnc == false ?
				'<div class="select-process" data-id="' +
				userId +
			    '" data-name="' +
			    userName +
			    '" data-email="' +
			    userEmail +
			    '" data-type="' +
			    leadTypeId +
			    '" data-process="assign" data-toggle="modal" data-target="#myModal" alt="Assign Agent" title="Assign Agent"><img class="icon-width" src="resources/core/img/add.svg" alt="Assign Agent"></span></div>'
		    :
		    	'';
		var btnUnassign =
			assign == true && dnc == false ?
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
		        '" data-process="unassign" data-toggle="modal" data-target="#myModal" alt="Unassign Agent" title="Unassign Agent"><img class="icon-width" src="resources/core/img/remove.svg" alt="Unassign Agent"></div>'
	    	:
	    		'';

			var btnDoNotCall = (status != 'NOT ASSIGNED')
			?	dnc == false ?
				'<div class="select-process" data-id="' +
				userId +
					'" data-name="' +
					userName +
					'" data-email="' +
					userEmail +
					'" data-type="' +
				    leadTypeId +
						'" data-process="doNotCall" data-toggle="modal" data-target="#myModal" alt="Do Not Call" title="Do Not Call"><img class="icon-width" src="resources/core/img/no-calls.svg" alt="Do Not Call"></div>'
				: ''
			: ''
//	        	'<div class="select-process" data-id="' +
//	        	userId +
//	        	'" data-name="' +
//	        	userName +
//	        	'" data-email="' +
//			    userEmail +
//	            '" data-process="doNotCall" data-toggle="modal" data-target="#myModal" alt="Call" title="Call"><img class="icon-width" src="resources/core/img/call.svg" alt="Call"></div>';
		template = (
		    '<div id="action-btn-container-' +
		    userId +
		    '" class="action-btn-container" style="display:flex; justify-content:center;">' +
		    btnAssign +
		    btnUnassign +
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
	console.log(leadTypeId);
	if ('assign' == modalProcess) {
	    var modal = new AssignModal(
	      userId,
	      userName,
	      userEmail,
	      leadTypeId,
	      null
	    );
	    modal.init(); 
	}
	if ('unassign' == modalProcess) {
	    var modal = new UnassignModal(
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

function AssignModal(userId, userName, userEmail, leadTypeId, listBulk) {
	var id = "", name = "", email = "", listUsers = [];
	if (listBulk === null) {
		id = userId;
		name = userName;
		email = userEmail;
		let objectUser = {
		    userId: userId,
		    userEmail: email
		  };
		listUsers.push(objectUser);
	}
	else {
		listUsers = listBulk;
		name = "all selected users";
	}
	
	function init() {
		$('#myModal').removeClass('doNotCallModal');
		$('#myModal').removeClass('unassignModal');
	    $('#myModal').addClass('assignModal');
	    
	    var template = generateAssignModal();
	    $('#modal-content-container').html(template);
	    
	    $('#table-agents').DataTable({
	    	lengthChange: false,
	       	language: {
	       		 search: "_INPUT_",
	       		 searchPlaceholder: "Search…"
	       	}
	    });
	    $('#table-agents_filter input').addClass('form-control');
	    $(document).off('click', '.select-agent');
	    $(document).on('click', '.select-agent', function(e){
	    	e.preventDefault();
	    	$('.select-agent').hide();
	    	var agentId = $(this).data('id');
	    	changeStatus(0, listUsers, agentId, leadTypeId);
	    });
	}

	function generateAssignModal() {
		let html = '';
		$.each(listAgent, function (index, item) {
            html += `<tr>
						<td>${item.firstName} ${item.lastName}</th>
						<td class="text-center">
							<a data-id="${item.id}" class="btn btn-md btn-success select-agent">Select</a>
						</td>
					</tr>`;

		});
        
	    return `
			<div class="modal-content">
	  			<div class="modal-header">
	    			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	    			<h4 class="modal-title">Assign ${name} to Agent </h4>
	  			</div>
	  			<div class="modal-body">	      				
	  				<table id="table-agents" class="table table-condensed table-responsive">
						<thead>
							<tr>
								<th class="width-auto">Agent</th>
								<th class="width-auto text-center">Action</th>
							</tr>
						</thead>  							
						<tbody>
							${html}								
						</tbody>  							  							  							  							
					</table>
					<div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  			<a href="#" class="alert-link success-msg-alert"></a>
					</div>
					<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
						<a href="#" class="alert-link error-msg-alert"></a>
					</div>
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
	    	  //Assign
	    	  $('.select-agent').hide();
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
	    		  salesSupervisor(pageCurrent.page);
	    	  }
	    	  else if (data.returnCode === -49) {
	    		  $('.footerLoader').hide();
	    		  $('.select-agent').show();
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
	    	  $('.select-agent').show();
	    	  $('.alert-danger').text('Unexpected error has ocurred, please contact support.');
	    	  $('.alert-danger').show();
	      },
    });
}

function UnassignModal(userId, userName, userEmail, agentName, leadTypeId, listBulk) {
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
		$('#myModal').addClass('unassignModal');
	    $('#myModal').removeClass('assignModal');
	    
	    var template = generateUnassignModal();
	    $('#modal-content-container').html(template);
	    
	    $('#save-unassign-btn').click(function () {
	    	changeStatus(3, listUsers, null, leadTypeId);
		});
	}

	function generateUnassignModal() {
	    return `
			<div class="modal-content">
      			<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        			<h4 class="modal-title">Unassigned Agent </h4>
      			</div>
      			<div class="modal-body">	      				
      				<label>
    					Confirm to delete the agent ${agentName} to the user ${name}?
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
		        	<button id="save-unassign-btn" type="submit" class="btn btn-primary">Accept</button>
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
		$('#myModal').removeClass('unassignModal');
	    $('#myModal').removeClass('assignModal');
	    
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

function datatablesDetailSalesSuperRender(pageCurrent) {
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
		 if (data[12] === "YES" && data[14] != "DO NOT CALL") {
            $(row).find('td:eq(12)').css('background-color', '#c5e0b3');
		 } else if (data[12] === "NO" && data[14] != "DO NOT CALL") {
            $(row).find('td:eq(12)').css('background-color', '#f6ccad');
		 } else if (data[12] === "PENDING" && data[14] != "DO NOT CALL") {
			 $(row).find('td:eq(12)').css('background-color', 'rgb(246 243 173)');
		 }
		 if (data[14] === "DO NOT CALL") {
			$(row).find('td:eq(14)').css('color', 'red');
			$(row).find('td:eq(14)').css('background-color', 'transparent');
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
            .html("Supervisor View Report");
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
	         title: 'Supervisor View Report'
	     },
	     {
	         extend: 'excelHtml5',
	         title: 'Supervisor View Report'
	     },
	     {
	         extend: 'csv',
	         title: 'Supervisor View Report'
	     },
	     {
	         extend: 'colvis',
	         title: 'Supervisor View Report'
	     },
	     {
	         extend: 'pageLength',
	         title: 'Supervisor View Report'
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
	$('#agents').val("");
	$(".card-body").html("");
	$('#bulkActions').hide();
	$('#alert-filter').hide();
	$('#bulkActionSelect').val("");
	var selectTypeLead = document.getElementById("typeLead");
	selectTypeLead.selectedIndex = 2;
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