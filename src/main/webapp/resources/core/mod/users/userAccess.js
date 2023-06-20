function UserAccessModal(idUser, emailUser, action, fname, lname ) {
	
	var id = idUser;
	var email = emailUser;
	var action$ = action;
	var firstName = fname;
	var lastName = lname;
	var dataTx;
	
	function init() {
		
		$("#myModal").removeClass("editModal");
		$("#myModal").addClass("txModal");
		requestUserAccess();
		
	}
	
	function requestUserAccess() {
		var data = {id: id, email: email};
		$.ajax({
			type : "POST",
			contentType : "application/json; charset=UTF-8",
			url : path_ajax + "search/api/getUserAccessList",
			data : JSON.stringify(data),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function(){
			/*
			 * $(".footerLoader").show(); $(".footerBtn").hide();
			 * $(".footerValidate").hide(); $(".alert-danger").hide();
			 */
				
			},
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.code == 0 ) {
					
					var formatedData = getDataRowsFormatted(data);
					
					renderUserAccessData(formatedData);

				} else  {
				/*
				 * $(".footerLoader").hide(); $(".footerValidate").show();
				 * $(".alert-danger").text(data.returnMessage);
				 * $(".alert-danger").show();
				 */
					alert("An error ocurred during the request. Please try later or contact support");
				}
			},
			error : function(e) {
				/*
				 * console.log("ERROR: ", e); $(".footerLoader").hide();
				 * $(".footerValidate").show();
				 * $(".alert-danger").text(data.returnMessage);
				 * $(".alert-danger").show();
				 */
				alert("An error ocurred during the request. Please try later or contact support");
			}
		});
	}
	
	function generateUserAccessModalTemplate(data) {
		var dataJoined = data.join("");
		return `
		<div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">User Access Log from ${firstName} ${lastName} - ${email}</h4>
	      </div>
	      	<div class="modal-body">
				<table id="user-access-table"  class="table table-striped" cellspacing="0">
					<thead>
						<tr>
							<th>Token id</th>
							<th>Email</th>
							<th>Customer</th>
							<th>Application</th>
							<th>IP address</th>
							<th>User Agent</th>
							<th>Created at</th>
							<th>Status</th>
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
	
	function getDataRowsFormatted(data) {
		var dataFormatted = data.result.map( r => {
			var createdAt = convertDateEnglish(r.createdAt);
		
			
			return `
				<tr>
					<td class="hf-center-cell">${r.id}</td>
					<td class="hf-center-cell">${r.email}</td>
					<td class="hf-center-cell">${r.customerName}</td>
					<td class="hf-center-cell">${r.applicationName}</td>
					<td class="hf-center-cell">${r.ipAddress}</td>
					<td class="hf-center-cell">${r.userAgent}</td>
					<td class="hf-center-cell">${createdAt}</td>
					<td class="hf-center-cell">${r.tokenStatusName}</td>
				</tr>
			
			`
		} );
		
		return dataFormatted;
	}
	
	function renderUserAccessData(data) {
		var template =  generateUserAccessModalTemplate(data);
		$("#modal-content-container").html(template);
		var uaTable = $("#user-access-table").DataTable({
			buttons: [
				{
		            extend: 'print',
		            text: 'Print',
		            autoPrint: false,
		            exportOptions: {
		                columns: ':visible',
		            },
		            customize: function (win) {
		                $(win.document.body).find('table').addClass('display').css('font-size', '9px');
		                $(win.document.body).find('tr:nth-child(odd) td').each(function(index){
		                    $(this).css('background-color','#F9F9F9');
		                });
		                $(win.document.body).find('h1').css('text-align','center');
		                $(win.document.body).prepend('</br>');
		                $(win.document.body).find('h1').html('Contribution Report');
		                $(win.document.body).find('div').css('text-align','center');
		               	$(win.document.body).find('div').css('margin-top','-17px').append(printFilters());
		            }
		        },
				'copy', 'excelHtml5', 'csv', 'colvis', 'pageLength' 
		        
		    ],
		    dom:
			      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
			      "<'table-responsive'tr>" +
			      "<'row'<'col-sm-6'i><'col-sm-6'p>>"
		});
		uaTable.buttons().container().appendTo("#user-access-table_wrapper .dataTables_length");
		$("#user-access-table_wrapper select").parent().hide();
		$('.btn-default').addClass('btn-outline-primary').removeClass('btn-default');
		$('.btn-default').addClass('btn-outline-primary').removeClass('btn-default');
		$("#user-access-table_filter input").addClass("form-control");
	}
	
	return  {
		init : init
	}
}