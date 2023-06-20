$(".blackList").addClass("active");

var path_ajax = "/scr-admin/";
var blackListComponent = new BlackListComponent();

$(document).ready(function()
{
	blackListComponent.init();
});

$(document).on('click', '#black-list-add', function(){
	var modal = $('#modal');
	modal.addClass('active');
});

$(document).on('click', '.modal-close', function(){
	var modal = $('#modal');
	modal.removeClass('active');
});

$(document).on('click', '#black-list-insert-click', function()
{

	let status = false;
	$("#myErrorBlackList" ).hide();
	$("#mySuccessBlackList").hide();
	
	let type    =  $( "#black-list-insert-type" ).val();
	let value   = $( "#black-list-insert-value" ).val().trim();

	if (type == 2)
		status = validateIPaddress(value); 
	else
		status = validateEmail(value); 

	if (!status)
	{
		$("#myErrorBlackList").show();
		$( "#myErrorBlackList" ).html("Please, enter a valid value");
	}
	else
	{			
		addBlackList({"data": value,   "dataTypeId": parseInt(type)});
	}
});

function validateIPaddress(ipaddress) 
{  
	if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress)) {  
	  return true;
	}  
	return false;
}

function validateEmail(object)
{
	let valid = /\S+@\S+\.\S+/;

	if(valid.test(object))
		return true;
	else 
		return false;
}

  function addBlackList(data) 
  {

	  $.ajax(
	  {
		type: "POST",
		contentType: false,
		url: path_ajax + "/search/api/blacklist",
		dataType: "html",
		timeout: 0,
		data:JSON.stringify(data),
		processData: false,
		cache: false,
		beforeSend: function() 
		{
			$("#black-list-insert-click").addClass("loading");
		},
		success: function(data) 
		{
			$("#black-list-insert-click").removeClass("loading");
			  data = JSON.parse(data);
			  if (data.returnCode == "0")
			  {
				$("#blacklist-module").html("");
				blackListComponent.init();
				$( "#black-list-insert-value" ).val("");
				$("#mySuccessBlackList").show();
				$("#mySuccessBlackList").text(
					"The record has been added successfully."
				);
			  } 
			  else
			  {
				$("#myErrorBlackList").show();
				$("#myErrorBlackList").text(data.returnMessage);
			  }
		},
		error: function(e) 
		{
			$("#black-list-insert-click").removeClass("loading");
			$("#myErrorBlackList").show();
			$("#myErrorBlackList").text(
				"Unexpected error has ocurred, please contact support."
			);
		}
	  });
	}


function BlackListComponent(data)
{
	
	var rawData;
	
	function init() {
		
		requestUserBlocked();
		
	}
	
	function generateBlackListTemplate(data) {
		
		var dataJoined = data.join("");
		
		return `<div class="blacklist-container">
				<table id="blacklist-table"  class="table table-striped table-nowrap" cellspacing="0" width="100%">
					<thead>
						<tr>
						<th class="hf-center-cell">Transaction id</th>
							<th class="hf-center-cell">Block Date</th>
							<th class="hf-center-cell">Email/IP</th>
							<th class="hf-center-cell">Data Type</th>
							<th class="hf-center-cell">Blocked by</th>
							<th class="hf-center-cell">Active</th>
							<th class="hf-center-cell">Cleaned Date</th>
							<th class="hf-center-cell">Actions</th>
						</tr>
					</thead>
					<tbody>
						${dataJoined}
					</tbody>
				</table>			 
	    		</div>`;
		
	}
	
	function  requestUserBlocked() 
	{
		
		$.ajax({
			type : "POST",
			contentType : "application/json; charset=UTF-8",
			url : path_ajax + "search/api/getBlackList",
			data : JSON.stringify({}),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function(){
				
			},
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.returnCode == 0 ) {
					rawData = data.result.blackListItems;
					var dataFormatted = formatDataForBlackList(rawData);
					renderDataTransactions(dataFormatted);
				} else  {
					
					alert(data.returnMessage);
				}
			},
			error : function(e) {
				alert("An error ocurred during the request. Please try later or contact support");
				$("#lds-modal-container-" + elmId).hide();
				$("#btn-" + elmId).show();
				
			}
		});
		
	}
	
	function formatDataForBlackList(data) 
	{
		var formatedData = data.map( r => {
			
			var blockedAt = convertDateEnglish(r.blockedAt);
			var dataTypeId = (r.dataTypeId == 1) ? "Mail" : "IP";
			var isActive = (r.active)? "Active" : "Disabled";
			var cleanedAt = (r.cleanedAt != null) ? convertDateEnglish(r.cleanedAt) : " - ";
			
			var BLACKLIST_QUERY = rolesFunc.findIndex( v => (v.name == "BLACKLIST_QUERY") );
			var btnAction = "";
			if(BLACKLIST_QUERY)
			{
				if (r.active && r.id != 220)
				{
					btnAction = `<span id="select-modal-blacklist-action-${r.id}"  data-id="${r.id}"  data-process="unblockBtns"  title="Unblock" alt="Unblock"><i class="fa fa-unlock" style="padding-right: 5px; font-size: 1.3em; cursor:pointer;"></i></span>`;
					btnAction =  	'<div  class="action-btn-container select-modal-1-container select-modal-1-container-'+r.id+'">'+
										'<div class="select" style="width:100%;text-aling:center">'+
											btnAction +
										'</div>'+
										'<div id="btn-tx-'+r.id+'" data-toggle="modal" data-target="#myModal"></div>'+
										'<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>'+
										'<div class="icons"><img class="success" src="resources/core/img/check-mark-new.svg" alt="Success"><img class="error" src="resources/core/img/cross.svg" alt="Error"></div>'+
									'</div>';
									processSelectActionClickBlack(r.id);
				}

			} else {
				btnAction = "";
			}
			
			return `<tr><td class="hf-center-cell">${r.id}</td>
					<td class="hf-center-cell">${blockedAt}</td>
					<td class="hf-center-cell">${r.data}</td>
					<td class="hf-center-cell">${dataTypeId}</td>
					<td class="hf-center-cell">${r.blockedBy}</td>
					<td class="hf-center-cell">${isActive}</td>
					<td class="hf-center-cell">${cleanedAt}</td>
					<td>
					<div class="action-btn-container">
						${btnAction}
					</div>
		            </td>
				</tr>`
		});
		
		return formatedData;
	}

	function processSelectActionClickBlack(id)
	{
		$(document).on("click","#select-modal-blacklist-action-"+id,function(e)
		{
			e.preventDefault();
			unBlockUserFromBlackList(id);
		});
	}
	
	function renderDataTransactions(data) {
		var template =  generateBlackListTemplate(data);
		$("#blacklist-module").html(template);
		var blackListTable = $("#blacklist-table").DataTable({dom:
		      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
		      "<'table-responsive'tr>" +
		      "<'row'<'col-sm-6'i><'col-sm-6'p>>"});
		$("#blacklist-table_filter input").addClass("form-control");
		$(".unblockBtns").click(function(){
			var idBL = $(this).data("id");
			unBlockUserFromBlackList(idBL);
		});
		
		
		blackListTable.on( 'draw', function () {
			$(".unblockBtns").click(function(){
				var idBL = $(this).data("id");
				unBlockUserFromBlackList(idBL);
			});
			
	    } );
	}
	
	function unBlockUserFromBlackList(elmId)
	{
		var data = {id:elmId};
		$.ajax({
			type : "POST",
			contentType : "application/json; charset=UTF-8",
			url : path_ajax + "search/api/unBlockFromBlackList",
			data : JSON.stringify(data),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function()
			{
				$("#btn-" + elmId).hide();
				$("#lds-modal-container-" + elmId).show();
				$("#success-check-" + elmId).hide();
				$('.select-modal-1-container-'+elmId).addClass("loading");
			},
			success : function(data) 
			{
				console.log("SUCCESS: ", data);
				if (data.returnCode == 0 ) 
				{
					
					$("#btn-" + elmId).hide();
					$("#lds-modal-container-" + elmId).hide();
					$("#success-check-" + elmId).show();
					
					setTimeout(function(){
						$("#success-check-" + elmId).hide();
						$("#btn-" + elmId).show();
						requestUserBlocked();
						
					},1500);

					$('.select-modal-1-container-'+elmId).removeClass("loading");
					$('.select-modal-1-container-'+elmId).addClass("success");
					setTimeout(function() 
					{
					  $('.select-modal-1-container-'+elmId).removeClass("success");
					}, 3000);

				} 
				else  
				{
					
					alert(data.returnMessage);
					$("#lds-modal-container-" + elmId).hide();
					$("#btn-" + elmId).show();

					$('.select-modal-1-container-'+elmId).removeClass("loading");
					$('.select-modal-1-container-'+elmId).addClass("error");
					setTimeout(function() 
					{
					  $('.select-modal-1-container-'+elmId).removeClass("error");
					}, 3000);
					
					
				}
			},
			error : function(e) 
			{
				alert("An error ocurred during the request. Please try later or contact support");
				$("#lds-modal-container-" + elmId).hide();
				$("#btn-" + elmId).show();
			
				$('.select-modal-1-container-'+elmId).removeClass("loading");
				$('.select-modal-1-container-'+elmId).addClass("error");
				setTimeout(function() 
				{
				  $('.select-modal-1-container-'+elmId).removeClass("error");
				}, 3000);

			}
		});
		
	}
	
	return {
		init: init,
		data: rawData
	}
	
}

function convertDateEnglish(inputFormat,langId) {
	  function pad(s) { return (s < 10) ? '0' + s : s; }
	  var d = new Date(inputFormat);
	  return [pad(d.getMonth()+1), pad(d.getDate()), d.getFullYear()].join('/');
}