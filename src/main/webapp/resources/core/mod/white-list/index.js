$(".whiteList").addClass("active");

var path_ajax = "/scr-admin/";


$(document).ready(function()
{
	$(document).on('click', '#black-list-add', function(){
        var modal = $('#modal');
        modal.addClass('active');
        requestSourcesRegisterReset();
    });


    $(document).on('click', '#source-next-click', function(){
        requestSourcesRegisterReset();
    });

    $(document).on('click', '.delete-user', function(){
        var request     = new Object;
        request["id"]   = $(this).data("id");
        request["hash"] = $(this).data("hash");
        requestSourcesDelete(request); 
    });

    

    $(document).on('click', '#source-insert-click', function(){
        if (requestSourcesRegisterValidate())
            requestSourcesRegister();
    });
    
    $(document).on('click', '.modal-close', function(){
        var modal = $('#modal');
        modal.removeClass('active');
    });
});



function requestSourcesRegisterNormal()
{
    $("#source-insert-click").removeClass("loading");
    $("#modal .content.register").show();
    $("#modal .content.success").hide();
    $(".myErrorModal").html("");
}

function requestSourcesRegisterReset()
{
    requestSourcesRegisterNormal();
    $("#source-name").val("");
    $("#source-reason").val("");
}


function isEmail(email) 
{
  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return regex.test(email);
}

function requestSourcesRegisterValidate()
{
    $(".myErrorModal").html("");
    var nameValue  = $("#source-name").val().trim();
    if (nameValue != "")
    {
        if (!isEmail(nameValue))
        {
            $("#source-name-error").html("Please, insert a valid email");
            return false;
        }        
    }
    else
    {
        $("#source-name-error").html("Please, insert a email in this field");
        return false;
    }


    var nameValue  = $("#source-reason").val();
    if (nameValue == "")
    {
        $("#source-reason-error").html("Please, insert a reason in this field");
        return false;
    }
        
    return true;
}

function requestSourcesRegisterForm()
{
    var nameValue    = $("#source-name").val().trim();
    var reasonValue  =  $("#source-reason").val();
    return {email: nameValue, reason: reasonValue};
}


function  requestSourcesDelete(request) 
{
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        url : path_ajax + "search/api/deleteWhiteListItem",
        data : JSON.stringify({id: request["id"]}),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function()
        {   
            $("#"+request["hash"]).addClass("loading");
        },
        success : function(data) 
        {
            $("#"+request["hash"]).removeClass("loading");
            if (data.returnCode == 0 ) 
            {
                $("#"+request["hash"]).addClass("mySuccessWhite");
                setTimeout(function() 
                {
                    $("#tr-"+request["id"]).remove();
                }, 5000);
            } 
            else  
            {
                $("#"+request["hash"]).addClass("myErrorWhite");
                setTimeout(function() 
                {
                    $("#"+request["id"]).removeClass("myErrorWhite");
                }, 5000);
            }
        }
    });
}

function  requestSourcesRegister() 
{
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        url : path_ajax + "search/api/addWhileListItem",
        data : JSON.stringify(requestSourcesRegisterForm()),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function()
        {   
            requestSourcesRegisterNormal();
            $("#source-insert-click").addClass("loading");
        },
        success : function(data) 
        {
            requestSourcesRegisterNormal();
            if (data.returnCode == 0 ) 
            {
                requestAllSources();
                $("#modal .content.register").hide();
                $("#modal .content.success").show();
            } 
            else  
            {
                $("#modal #myError").html(data.returnMessage);
            }
        }
    });
}

function  requestAllSources() 
{
    
    $.ajax({
        type : "POST",
        contentType : "application/json; charset=UTF-8",
        url : path_ajax + "search/api/listWhileListItem",
        data : JSON.stringify({}),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(){
            
        },
        success : function(data) 
        {
            if (data.returnCode == 0 ) 
            {
                rawData = data.result;
                var dataFormatted = formatData(rawData);
                renderData(dataFormatted);
            } 
            else  
            {
                
                alert(data.returnMessage);
            }
        }
    });
}

requestAllSources();

function formatData(data) 
{
    var formatedData = data.whiteListItems.map( r => {
        
        var error   = '<div class="myErrorWhite"><img class="error" src="resources/core/img/cross.svg" alt="Error"></img></div>';
        var success = '<div class="mySuccessWhite"><img class="success" src="resources/core/img/check-mark-new.svg" alt="Success"></div>';
        var loading = '<div class="loading"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>';
        var actions = `<div class="actions"><span  data-hash="row-table-${r.id}" data-id="${r.id}" class="fa fa-user-times delete-user" style="font-size: 1.5em; cursor:pointer;"></span></div>`;

        return `<tr id="tr-${r.id}">
                <td class="hf-center-cell">${r.id}</td>
                <td >${r.email}</td>
                <td >${r.reason}</td>
                <td >${r.adminUserEmail}</td>
                <td class="hf-center-cell white-list" id="row-table-${r.id}">${loading} ${actions} ${error} ${success}</td>
                </tr>`;
    });
    
    return formatedData;
}


function renderData(data) 
{
    var template =  listTemplate(data);
    $("#blacklist-module").html(template);

    var blackListTable = $("#blacklist-table").DataTable({dom:
          "<'row'<'col-sm-6'><'col-sm-6'f>>" +
          "<'table-responsive'tr>" +
          "<'row'<'col-sm-6'i><'col-sm-6'p>>",
          "pageLength": 50});
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
    });
}

function listTemplate(data) 
{
    var dataJoined = data.join("");
		
    return `<div class="blacklist-container">
            <table id="blacklist-table"  class="table table-striped table-nowrap" cellspacing="0" width="100%">
                <thead>
                    <tr>
                    <th class="hf-center-cell">Id</th>
                        <th>Email</th>
                        <th>Reason</th>
                        <th>Admin</th>
                        <th class="hf-center-cell">Operations</th>
                    </tr>
                </thead>
                <tbody>
                    ${dataJoined}
                </tbody>
            </table>			 
            </div>`;
}