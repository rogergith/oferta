$(".appLog").addClass("active");

var path_ajax = "/scr-admin/";
var nameReport = "";
var userTable;
var deviceCodeId;

jQuery.validator.addMethod(
  "requiredAtLeastLog",
  function(value, element) {
    var what = $("#what")
      .val()
      .trim();
    var who = $("#who")
      .val()
      .trim();
   /* var ip_address = $("#ip_field")
      .val()
      .trim();*/
   /* var user_agent = $("#user_agent")
    .val()
    .trim();*/

    if (what.length > 2 || who.length > 2 ) {
      return true;
    } else {
      return false;
    }
  },
  "Please fill at least fill one of the input filters"
);

$("#app-log-form").validate({
  rules: {
    what: {
      minlength: 3,
      
    },
    who: {
      minlength: 3,
      requiredAtLeastLog:true
    }
  },
  submitHandler: submitGenerateAppLogList,
  errorClass: "has-error",
  validClass: "has-success",
  highlight: function(element, errorClass, validClass) {
    $(element)
      .addClass(errorClass)
      .removeClass(validClass);
  },
  unhighlight: function(element, errorClass, validClass) {
    $(element)
      .addClass(validClass)
      .removeClass(errorClass);
  },
  errorPlacement: function(error, element) {
    var parent = $(element).parent();
    var sibling = parent.children(".custom-message-error");
    error.appendTo(sibling);
  }
});

$("#generateSearch").click(function() {
  $("#app-log-form").submit();
});

function submitGenerateAppLogList() {
	  var filter = getAppLogFilters();
	  var filterKeys = Object.keys(filter);
	  console.log(filter)
	  $("#report-user-error-container").hide();

	  if (filterKeys.length <= 0) {
	    $("#report-user-error-container span").text(
	      "You need at least fill one of the inputs filters"
	    );
	    $("#report-user-error-container").show();
	    return;
	  }

	  $.ajax({
	    type: "POST",
	    contentType: "application/json",
	    url: path_ajax + "search/api/getAppLogList",
	    data: JSON.stringify(filter),
	    dataType: "json",
	    timeout: 100000,
	    beforeSend: function() {
	      $("#report-user-error-container").hide();
	      $("#generateUserSearch").attr("disabled", "true");
	    },
	    success: function(data) {
	      console.log("SUCCESS: ", data);
	      $("#generateUserSearch").removeAttr("disabled");
	      if (data.code === "200") {
	    	  generateLogReport(data.result);
	        userTable.on("draw", function() {
	          activateBtns();
	        });
	        console.log(data);
	      } else if (data.code === "204") {
	        $("#report-user-error-container span").text(data.msg);
	        $("#report-user-error-container").show();
	      } else if (data.code === "400") {
	        $("#report-user-error-container span").text(data.msg);
	        $("#report-user-error-container").show();
	      }
	    },
	    error: function(e) {
	      $("#generateUserSearch").removeAttr("disabled");
	      console.log("ERROR: ", e);
	      alert("ERROR");
	    },
	    done: function(e) {
	      alert("DONE");
	      console.log("DONE");
	    }
	  });
	}

function getAppLogFilters() {
	  var filter = {};
	  	if ($("#fromDate").val().length==10 && $("#toDate").val().length==10){
			filter["min_date"] = convertFromDate();
			filter["max_date"] = convertToDate();
		}
		else {
			$("#fromDate").val("");
			$("#toDate").val("")
		}

	  if ($("#what").val().length > 0) filter["what"] = $("#what").val();
	  if ($("#who").val().length > 0) filter["who"] = $("#who").val();
	  if ($("#ip_field").val().length > 0) filter["ip_address"] = $("#ip_field").val();
	  if ($("#user_agent").val().length > 0) filter["user_agent"] = $("#user_agent").val();
	  if ($("#source_filter").val().length > 0) filter["source"] = $("#source_filter").val();
	  
	  return filter;
}

function convertFromDate() {
	  if ($("#fromDate").val().length == 10) {
	    startdate = $("#fromDate").val() + " 00:00:00";
	    var a = startdate.split(" ");
	    var d = a[0].split("-");
	    var t = a[1].split(":");
	    startdate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
	    startdate = startdate.getTime();
	    return startdate;
	  }
	  return "";
}

	function convertToDate() {
	  if ($("#toDate").val().length == 10) {
	    enddate = $("#toDate").val() + " 23:59:59";
	    var a = enddate.split(" ");
	    var d = a[0].split("-");
	    var t = a[1].split(":");
	    enddate = new Date(d[2], d[0] - 1, d[1], t[0], t[1], t[2]);
	    enddate = enddate.getTime();
	    return enddate;
	  }
	  return "";
}
	
	function convertDateEnglish(inputFormat, langId) {
		  function pad(s) {
		    return s < 10 ? "0" + s : s;
		  }
		  var d = new Date(inputFormat);
		  return [pad(d.getMonth() + 1), pad(d.getDate()), d.getFullYear()].join("/");
		}
	
	function generateLogReport(dataResult) {
		  $(".card-body").html(
		    '<table id="demo-datatables-2" class="table table-striped table-nowrap" cellspacing="0" width="100%"></table>'
		  );

		  var stringReport = "";
		  if (dataResult.length > 0 && dataResult[0].contribution_amount != -1) {
		    for (var i = 0; i < dataResult.length; ++i) {
		      stringReport += "<tr>";
		      stringReport += "<td>" + dataResult[i].id + "</td>";
		      stringReport += "<td>" + dataResult[i].customerId + "</td>";
		      stringReport += "<td>" + dataResult[i].applicationId+ "</td>";
		      stringReport += "<td>" + dataResult[i].auditLevel + "</td>";
		      stringReport += "<td>" + dataResult[i].what + "</td>";
		      stringReport += "<td>" + dataResult[i].who + "</td>";
		      stringReport += "<td>" + dataResult[i].source + "</td>";
		      stringReport +=
		        "<td>" + convertDateEnglish(dataResult[i].createdAt) + "</td>";
		      stringReport += "<td>" + dataResult[i].ipAddress + "</td>";
		      stringReport += "<td>" + dataResult[i].userAgent + "</td>";
		      stringReport += "<td>" + (dataResult[i].data || "") + "</td></tr>";
		    }
		  }

		  var stringHead =
		    "<thead><tr><th>ID</th>" +
		    "<th>Customer Id</th>" +
		    "<th>Application Id</th>" +
		    "<th>Audit Level</th>" +
		    "<th>Action event</th>" +
		    "<th>Action executor</th>" +
		    "<th>Source</th>" +
		    "<th>Created At</th>" +
		    "<th>IP Address</th>" +
		    "<th>User agent</th>" +
		    "<th>Data</th>" +
		    "</tr></thead>" +
		    "<tbody id='detailReport'></tbody>";
		  $("#demo-datatables-2").html(stringHead);
		  $("#detailReport").html(stringReport);
		  datatablesRender();
}
	
function datatablesRender() {
		
		  userTable = $("#demo-datatables-2").DataTable({
			  dom:
			      "<'row'<'col-sm-6'><'col-sm-6'f>>" +
			      "<'table-responsive'tr>" +
			      "<'row'<'col-sm-6'i><'col-sm-6'p>>"
		  });
		  $("#demo-datatables-2_filter input").addClass("form-control");
}
