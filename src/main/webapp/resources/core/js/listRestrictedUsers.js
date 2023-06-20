var ajaxPath = "/oferta/";
var dataTable;
$(document).ready( function () {

	$('.collapseRestrict').collapse();
	$("li.sidenav-item").removeClass("active");
	$(".menuListUsersRestricted").addClass("active");

    dataTable = $('#restricted_users').DataTable({
    	columns: [
    		{data: "id"},
    		{data: "email"},
    		{data: "active"},
    		{data: "actions"}

    	]
    });
    $("#restricted_users_filter input").addClass("form-control");

    activateBtns();



} );

function changeStatusActive(id){
	var isActive = $("#active-"+id)[0].checked;
	var mail = $("#mail-"+id)[0].value;
	console.log(isActive);
	console.log(mail);

    var data = {
        name: "name",
        lastName: "lastName",
        active: isActive,
        id: id,
        email: mail
    };

    var ajaxActions = new AjaxConsumtionEndpoints(data);

   /* if(isActive){ */


        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : ajaxPath + "search/api/editRestrictedUser",
            data : JSON.stringify(data),
            dataType : 'json',
            timeout : 100000,
            beforeSend: function(){
                $(".footerLoader").show();
                $(".footerBtn").hide();
            },
            success : function(data) {
                console.log("SUCCESS: ", data);
                if (data.returnCode=="0") {
                    $(".alert-success").show();
                    refreshRestrictedUsers();
                    setTimeout(function(){
                        $(".alert-success").hide();
                    }, 3000);

                } else if (data.returnCode=="-4") {
                    $(".footerLoader").hide();
                    $(".footerBtn").show();
                    $(".alert-danger").text(data.returnMessage);
                    $(".alert-danger").show();
                } else  {
                    $(".footerLoader").hide();
                    $(".footerBtn").show();
                    $(".alert-danger").text(data.returnMessage);
                    $(".alert-danger").show();
                }
            },
            error : function(e) {
                console.log("ERROR: ", e);
                $(".footerLoader").hide();
                $(".footerBtn").show();
                $(".alert-danger").text(data.returnMessage);
                $(".alert-danger").show();
            }
        });


	/*}else {

    	ajaxActions.deleteRestrictedUser(data);

	}*/








}

function AjaxConsumtionEndpoints(user) {

    var id = user.id;
    var email = user.email;
    var action = user.action;

    function deleteRestrictedUser(user) {

        var id = user.id;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: ajaxPath + "search/api/deleteRestrictedUser",
            data: JSON.stringify({id: id}),
            dataType: 'json',
            timeout: 100000,
            beforeSend: function () {
                $(".footerLoader").show();
                $(".footerBtn").hide();
            },
            success: function (data) {
                console.log("SUCCESS: ", data);
                if (data.returnCode == "0") {
                    $(".footerLoader").hide();
                    $(".footerBtn").show();
                    $(".alert-success").show();
                    refreshRestrictedUsers();
                    setTimeout(function () {
                        $(".alert-success").hide();
                        $("#myModal").modal("hide");
                    }, 1000);

                } else if (data.returnCode == "-4") {
                    $(".footerLoader").hide();
                    $(".footerBtn").show();
                    $(".alert-danger").text(data.returnMessage);
                    $(".alert-danger").show();
                } else {
                    $(".footerLoader").hide();
                    $(".footerBtn").show();
                    $(".alert-danger").text(data.returnMessage);
                    $(".alert-danger").show();
                }

            },
            error: function (e) {
                console.log("ERROR: ", e);
                alert("ERROR");
                $(".footerLoader").hide();
                $(".footerBtn").show();
                $(".alert-danger").text(data.returnMessage);
                $(".alert-danger").show();
            }
        });
    }

    function refreshRestrictedUsers(){
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : ajaxPath + "search/api/getRestrictedUsers",
            data : JSON.stringify({id: id}),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
                console.log("SUCCESS: ", data);
                if (data.returnCode=="0") {

                    var filterValues = data.result.filter( v => true );
                    var values = filterValues.map( v => ( {"id": v.id,

                        "email":
                            `<input id="mail-${v.id}" type="hidden" value="${v.email}">
					${v.email}`,

                        "active":

                            `<label class="switch switch-info">
											<input class="switch-input" type="checkbox" id="active-${v.id}" onchange="changeStatusActive(${v.id})"  ${v.active ? "checked":""} >
											<span class="switch-track"></span>
											<span class="switch-thumb"></span>
										</label>`
                        ,

                        "actions": getTableActionBtnTemplate(v.id, v.email,v.active)} ));
                    console.log(values);
                    dataTable.clear();
                    dataTable.rows.add(values);
                    dataTable.draw();
                    activateBtns();


                }
            },
            error : function(e) {
                console.log("ERROR: ", e);
                alert("ERROR");
            }
        });
    }

    function getTableActionBtnTemplate(id, email,active) {

        return `
		
		<div class="action-btn-container">
						            	<div class="btn-container" >
							            	<button data-id="${id}" data-email="${email}" data-active="${active}" class="btn btn-sm btn-primary editBtn" data-toggle="modal" data-target="#myModal">
							            		<span class="fa fa-pencil"></span>Edit
							            	</button>
						            	</div>
						            	<div class="btn-container">
						            		<button data-id="${id}" data-email="${email}" data-active="${active}" class="btn btn-sm btn-danger deleteBtn" data-toggle="modal" data-target="#myModal">
						            			<span class="fa fa-trash"></span>Delete
						            		</button>
						            	</div>
					            	</div>
		
		`;
    }

    return {
        deleteRestrictedUser: deleteRestrictedUser
    }

}


function getTableActionBtnTemplate(id, email, active) {

    return `
		
		<div class="action-btn-container">
						            	<div class="btn-container" >
							            	<button data-id="${id}" data-email="${email}" data-active="${active}" class="btn btn-sm btn-primary editBtn" data-toggle="modal" data-target="#myModal">
							            		<span class="fa fa-pencil"></span>Edit
							            	</button>
						            	</div>
						            	<div class="btn-container">
						            		<button data-id="${id}" data-email="${email}" data-active="${active}" class="btn btn-sm btn-danger deleteBtn" data-toggle="modal" data-target="#myModal">
						            			<span class="fa fa-trash"></span>Delete
						            		</button>
						            	</div>
					            	</div>
		
		`;
}
function refreshRestrictedUsers(id){
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : ajaxPath + "search/api/getRestrictedUsers",
        data : JSON.stringify({id: id}),
        dataType : 'json',
        timeout : 100000,
        success : function(data) {
            console.log("SUCCESS: ", data);
            if (data.returnCode=="0") {

                var filterValues = data.result.filter( v => true );
                var values = filterValues.map( v => ( {"id": v.id,

					"email":
					`<input id="mail-${v.id}" type="hidden" value="${v.email}">
					${v.email}`,

					"active":

                    `<label class="switch switch-info">
											<input class="switch-input" type="checkbox" id="active-${v.id}" onchange="changeStatusActive(${v.id})"  ${v.active ? "checked":""} >
											<span class="switch-track"></span>
											<span class="switch-thumb"></span>
										</label>`
                    ,

                    "actions": getTableActionBtnTemplate(v.id, v.email,v.active)} ));
                console.log(values);
                dataTable.clear();
                dataTable.rows.add(values);
                dataTable.draw();
                activateBtns();


            }
        },
        error : function(e) {
            console.log("ERROR: ", e);
            alert("ERROR");
        }
    });
}



function activateBtns () {

	 $(".editBtn").click( function(e){
	    	e.preventDefault();
	    	var idUser = $(this).data("id");
	    	var emailUser = $(this).data("email");
	    	var activeUser = $(this).data("active");
	    	var modal = new EditModal(idUser, emailUser,activeUser, "edit");
	    	
	    	modal.init();
	    } );

	    $(".deleteBtn").click( function(e){
	    	e.preventDefault();
	    	var idUser = $(this).data("id");
	    	var emailUser = $(this).data("email");
	    	var activeUser = $(this).data("active");
	    	var modal = new EditModal(idUser, emailUser,activeUser, "delete");

	    	modal.init();
	    } );
}

function EditModal(idUser, emailUser,activeUser, userAction) {

	var id = idUser;
	var email = emailUser;
	var action = userAction;
	var active = activeUser;
	
	function getActive(){
		return active;
	}
	
	function getId() {

		return id
	}

	function getEmail() {

		return email
	}

	function generateEditModal() {

		return	`
		<div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Edit User registered</h4>
	      </div>
	      <div class="modal-body">
	        <form id="edit-${id}">
	        	<label for"mail">
	        		Email:
	        	</label>
	        	<input type="email" id="mail" class="form-control" name="mail" value="${email}" required>
	         	<input type="text" id="isActive" class="form-control" name="isActive" value="${active}" style="display:none">

	        </form>
	        <div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  <a href="#" class="alert-link">The user was succesfully deleted from this list!</a>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
				<a href="#" class="alert-link"></a>
			</div>
	      </div>
	      <div class="modal-footer footerBtn">
	        <button id="destroy-modal"  type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button id="submit-information" type="submit" class="btn btn-primary">Save changes</button>
	      </div>
	       <div class="modal-footer footerLoader" style="display:none;">
	      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
	      </div>
	    </div><!-- /.modal-content -->	`


	}

	function generateDeleteModal() {

		return	`
		<div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">Delete User</h4>
	      </div>
	      <div class="modal-body">
	        <p> Are you sure to delete ${email} user from the list?</p>
	        <div class="alert alert-success" role="alert" style="display:none; margin-top:30px">
			  <a href="#" class="alert-link">The user was succesfully deleted from this list!</a>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none; margin-top:30px">
				<a href="#" class="alert-link"></a>
			</div>
	      </div>
	      <div class="modal-footer footerBtn">
	        <button id="destroy-modal"  type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	        <button id="delete-information" type="button" class="btn btn-primary">Delete</button>
	      </div>
	      <div class="modal-footer footerLoader" style="display:none;">
	      	<div class="lds-ring"><div></div><div></div><div></div><div></div></div>
	      </div>
	    </div><!-- /.modal-content -->	`


	}

	/*function getTableActionBtnTemplate(id, email) {

		return `

		<div class="action-btn-container">
						            	<div class="btn-container" >
							            	<button data-id="${id}" data-email="${email}" class="btn btn-sm btn-primary editBtn" data-toggle="modal" data-target="#myModal">
							            		<span class="fa fa-pencil"></span>Edit
							            	</button>
						            	</div>
						            	<div class="btn-container">
						            		<button data-id="${id}" data-email="${email}" class="btn btn-sm btn-danger deleteBtn" data-toggle="modal" data-target="#myModal">
						            			<span class="fa fa-trash"></span>Delete
						            		</button>
						            	</div>
					            	</div>

		`;
	}*/


	function deleteRestrictedUser() {

		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : ajaxPath + "search/api/deleteRestrictedUser",
			data : JSON.stringify({id: id}),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function(){
				$(".footerLoader").show();
				$(".footerBtn").hide();
			},
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.returnCode=="0") {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-success").show();
					refreshRestrictedUsers();
					setTimeout(function(){
						$(".alert-success").hide();
						$("#myModal").modal("hide");
					}, 1000);

				} else if (data.returnCode=="-4") {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-danger").text(data.returnMessage);
					$(".alert-danger").show();
				} else  {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-danger").text(data.returnMessage);
					$(".alert-danger").show();
				}
			},
			error : function(e) {
				console.log("ERROR: ", e);
				alert("ERROR");
				$(".footerLoader").hide();
				$(".footerBtn").show();
				$(".alert-danger").text(data.returnMessage);
				$(".alert-danger").show();
			}
		});
	}


	function submitEditData() {

		var data = {
				name: "name",
				lastName: "lastName",
				email: $("#mail").val().trim(),
				active: ($("#isActive").val() === 'true'),
				id: id
		};

		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : ajaxPath + "search/api/editRestrictedUser",
			data : JSON.stringify(data),
			dataType : 'json',
			timeout : 100000,
			beforeSend: function(){
				$(".footerLoader").show();
				$(".footerBtn").hide();
			},
			success : function(data) {
				console.log("SUCCESS: ", data);
				if (data.returnCode=="0") {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-success").show();
					refreshRestrictedUsers();
					setTimeout(function(){
						$(".alert-success").hide();
						$("#myModal").modal("hide");
					}, 1000);

				} else if (data.returnCode=="-4") {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-danger").text(data.returnMessage);
					$(".alert-danger").show();
				} else  {
					$(".footerLoader").hide();
					$(".footerBtn").show();
					$(".alert-danger").text(data.returnMessage);
					$(".alert-danger").show();
				}
			},
			error : function(e) {
				console.log("ERROR: ", e);
				$(".footerLoader").hide();
				$(".footerBtn").show();
				$(".alert-danger").text(data.returnMessage);
				$(".alert-danger").show();
			}
		});
	}

	function init() {

		if(action == "edit") {
			var template =  generateEditModal();
			$("#modal-content-container").html(template);

			$("#edit-" + id).validate({
				rules: {
					email: {
						required: true,
						email: true
					}
				},
				submitHandler: submitEditData,
				 errorClass: 'has-error',
	             validClass: 'has-success'
			});

			$("#submit-information").click(function(){
				console.log("works")
				$("#edit-" + id).submit();

			});
		} else {

				var template =  generateDeleteModal();
				$("#modal-content-container").html(template);


				$("#delete-information").click(function(){

					deleteRestrictedUser();

				});
			}

	}

	return {
		init: init,
		getId: getId,
		getEmail: getEmail,
		getActive: getActive
		
	}
}