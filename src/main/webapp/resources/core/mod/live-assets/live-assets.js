$(".liveSettings").addClass("active");

var path_ajax = "/scr-admin/";
// load all the languages available to config
var videoDuration = 0;
var fieldAssets;
var startDateMilis = 0;
var months = [ "January", "February", "March", "April", "May", "June",
   					 "July", "August", "September", "October", "November", "December" ];
function getLanguagesConfig() {
	  $.ajax({
		    type: "POST",
		    contentType: "application/json",
		    url: path_ajax + "search/api/getAllLanguages",
		    data: JSON.stringify(''),
		    dataType: "json",
		    timeout: 100000,
		    beforeSend: function() {
		    },
		    success: function(data) {
		      //console.log("SUCCESS: ", data);
		      if (data.returnCode === 0) {
		    	  insertOptionsLanguages(data.result);
		      } else {
		    	  if(data.returnMessage == "logout") {
		    		  window.location = path_ajax;
		    	  } else {
		    		  showErrorModal(data.returnMessage);
		    	  }
		      }
		    },
		    error: function(e) {
		    	showErrorModal("An error has ocurred, please check you internet connection or contact support.");
		    }
		  });
}

function listAllAssets() {
	  $.ajax({
		    type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: path_ajax + "search/api/getAllAssets",
		    data: JSON.stringify(''),
		    dataType: "json",
		    timeout: 100000,
		    beforeSend: function() {
		    },
		    success: function(data) {
		      //console.log("SUCCESS: ", data);
		      if (data.returnCode === 0) {
		    	  buildAssetsForm(data.result);
		      } else {
		    	  if(data.returnMessage == "logout") {
		    		  window.location = path_ajax;
		    	  } else {
		    		  showErrorModal(data.returnMessage);
		    	  }
		      }
		    },
		    error: function(e) {
		    	showErrorModal("An error has ocurred, please check you internet connection or contact support.");
		    }
		  });
}

function insertOptionsLanguages(langs){
	//console.log(langs.languages)
	
	let langOptions = langs.languages.forEach(function(l){
		
		let elm = document.createElement("option");
			elm.innerText = l.description;
			elm.value = l.id;
			$("#config-lang-select").append(elm);
	});
	
	$("#config-lang-select").change(function(event){
		event.preventDefault();
		$(".mySortPersonal").remove();
		cleanFields();
		findAssetsEvents();
	});
	
	listAllAssets();
	
}

function cleanFields(){
	var inputs = document.querySelectorAll('.asset-input');
	inputs.forEach(function(i){
		i.value = "";
	});
	$("#fields-config").hide();
	var isChecked = $('#toggle-simlive').is(':checked');
	if(isChecked) {
		 $('#toggle-simlive').click();
		 $(".simlive-config-container").html("");
	}
}

function findAssetsEvents() {
	  var lang_id = $("#config-lang-select").val();
	  $.ajax({
		    type: "POST",
		    contentType: "application/json; charset=UTF-8",
		    url: path_ajax + "search/api/getEventsAssetsByLang",
		    data: JSON.stringify({lang_id: lang_id}),
		    dataType: "json",
		    timeout: 100000,
		    beforeSend: function() {
		    	$("#get-lang-conf").hide();
		    	$("#config-lang-select").attr("readonly", "true");
		    	$(".lang-conf-loader").show();
		    },
		    success: function(data) {
		      //console.log("SUCCESS: ", data);
		        $("#get-lang-conf").show();
		    	$("#config-lang-select").removeAttr("readonly");
		    	$(".lang-conf-loader").hide();
		      if (data.returnCode === 0) {
		    	  setDataAssetsInForm(data.result.eventAssets);		    	  
		      } else {
		    	  if(data.returnMessage == "logout") {
		    		  window.location = path_ajax;
		    	  } else {
		    		  $("#get-lang-conf").show();
				    	$("#config-lang-select").removeAttr("readonly");
				    	$(".lang-conf-loader").hide();
		    		  showErrorModal(data.returnMessage);
		    	  }
		      }
		    },
		    error: function(e) {
		    	 $("#get-lang-conf").show();
			    	$("#config-lang-select").removeAttr("readonly");
			    	$(".lang-conf-loader").hide();
		    	alert("An error has ocurred, please check you internet connection or contact support.");
		    }
		  });
}

function setDataAssetsInForm(data) 
{
	var lengthData = data.length;
	if(lengthData == 0) 
	{
		$("#assets-event-form").attr("data-form", "addSetting");
		$("#fields-config").show();
	} 
	else 
	{
		$("#asset-76").attr("readonly", "true");
		$("#assets-event-form").attr("data-form", "updateSetting");
		data.forEach(function(v)
		{
			if ($("#asset-"+v.assetId).attr("type") == "checkbox")
			{
				if (v.assetValue == "true") {
					$("#asset-"+v.assetId).prop('checked', true);
					if (v.assetId ==  75 && v.assetValue == "true") {
						$("#asset-76").removeAttr("readonly");
					}
				}
					
				else if (v.assetValue == "false") {
					$("#asset-"+v.assetId).prop('checked', false);
					if (v.assetId ==  75 && v.assetValue == "false") {
						$("#asset-76").attr("readonly", "true");
					}
				}
					
			}
			else if ($("#asset-"+v.assetId).data("type") == "array")
			{
				if (v.assetValue != "" && v.assetValue != "[]" && v.assetValue != undefined)
					setValueArray(v);
			}
			else
			{
				$("#asset-"+v.assetId).val(v.assetValue);
			}

			$("#asset-"+v.assetId).attr("data-update", v.id);
		});
		$("#fields-config").show();
		validateVimeoVideoDuration();
	}
}

function setValueArray(v)
{
	try
	{
		var data = JSON.parse(v.assetValue);

		data.forEach(function(e, i) {
			
			$("#asset-"+v.assetId+" .element-add-item-link").click();

			Object.entries(e).forEach(([key, value]) => {
				console.log(key, value)
				$("#asset-"+v.assetId+" .mySortPersonal_"+i+" input[name='"+key+"']").val(value);
			});
		});
		return true;
	}
	catch
	{
		return false;
	}


	console.log("#asset-"+v.assetId);
}

function myIscheckTab(c)
{
	if (!document.getElementById("TAB-"+c.categoryId)) 
		myCreateTab(c);
	return  true;
}

function myCreateTab(c)
{
	var element = "<div id='TAB-"+c.categoryId+"' class='myTab'><div class='myTitle'></div><div class='myContent'></div></div>";
	var main    = "<div id='TAB-MAIN-"+c.categoryId+"' data-id='TAB-"+c.categoryId+"' data-sort='"+c.categoryId+"' class='main-myTab'>"+c.categoryName+"</div>"
	$("#dynamic-form-wrapper").append(element);
	$("#dynamic-form-wrapper-tab").append(main);
}


function myAssetsEvents()
{
	$("#save-assets").click(function(e){
		e.preventDefault();
		saveAssets();
		//$("#assets-event-form").submit();
	});

}

$(document).on('click', ".main-myTab", function(e) {
	e.preventDefault();
	var el = $(this).attr("data-id");
	$(".main-myTab").removeClass("active");
	$(".myTab").removeClass("active");
	$(this).addClass("active");
	$("#"+el).addClass("active");
})

$(document).on('click', '.element-add-item-link', function(e) 
{
	e.preventDefault();
	myGenerateInputsArray(this);
});

$(document).on('click', '.element-delete-item-link', function(e) 
{
	e.preventDefault();
	$(this).parents(".mySortPersonal").remove();
});

/**
 * 
 * @param {Al presionar  ADD+ se retorna todo el objeto } here 
 * @returns boleano para saber si se genero el nuevo elemento dentro de la seccion
 */
function myGenerateInputsArray(here)
{
	try
	{
		var html = "";
		var arr  = $(here).parents(".asset-input.container-element").data("input");
		var id   = $(here).parents(".asset-input.container-element").attr("id");
		var total = $("#"+id+" .mySortPersonal").length
		//Genera los objeto
		arr.forEach(function(e) {
			// TODO Mejorar todos, por ahora da Error
			//html = html + myControllerPrintNewInputs(e);
		
			//Genero el input html
			if (e.id === "available_time") {
				html = html + "<span class='width-"+e.width+"'><label class='theTitle '>"+e.name+"</label><input class='theInput form-control'  type='number'  name='"+e.id+"'></span>";
			}
			else {
				html = html + "<span class='width-"+e.width+"'><label class='theTitle '>"+e.name+"</label><input class='theInput form-control'  type='text'  name='"+e.id+"'></span>";
			}
			
		});

		//Añade una Capa a los objeto generados y lo inserta
		html = "<div class='element-move-item'><i class='fa fa-bars' aria-hidden='true'></i></div><div class='item-inputs-ui'>"+html+"</div>";
		html = html + "<div class='element-delete-item'><a class='element-delete-item-link'> <i class='fa fa-trash fa-trash-array' aria-hidden='true'></i>Delete</a></div>";
		html = "<div class='mySortPersonal ui-state-default mySortPersonal_"+total+"'>"+html+"</div>";
		$("#"+id+" .sortable").append(html);

		return true;
	}
	catch
	{
		return false;
	}
}

/**
 * 
 * @param {elemento del array de objeto que esta impreso en el tag } e 
 * @returns elemento generado html
 */
function myControllerPrintNewInputs(e)
{
	try
	{
		var c 	  	  = new Object();
		c.id   		  = e.id;
		c.name 		  = e.id;
		c.dimension   = e.width;
		c.description = e.name;
		return myGenerateInputs(c)
	}
	catch
	{
		return "";
	}
}

function myMapInputs(fields)
{
	fields.assets.map(function(c)
	{
		var object = myGenerateInputs(c);
		if (myIscheckTab(c))
			$("#TAB-"+c.categoryId+" .myContent").append(object);
	});
}

function myGenerateInputs(c)
{
	var type = ['check_', 'array_object', 'number'];
	if (c.name.indexOf(type[0]) >= 0)
		return myContainer_(c, myCheck_(c));
	if (c.name.indexOf(type[1]) >= 0)
		return myArray_(c);
	if (c.name.indexOf(type[2]) >= 0)
		return myContainer_(c, myNumber_(c));
	else
		return myContainer_(c, myInput_(c));
}

function activeEventInTime(id)
{
	setTimeout(() => 
	{
		$( "#sortable_"+id ).sortable({
			placeholder: "ui-state-highlight"
		});
	
		$( "#sortable_"+id ).disableSelection();
	}, 500);
}

function myArray_(c)
{

	activeEventInTime(c.id);
	return `
			<div id="asset-${c.id}" name="asset-${c.id}" data-type="array" data-input='${c.description.replace(/\s+/g, ' ').trim()}' class="asset-input container-element" data-id="${c.id}">
				<div id="sortable_${c.id}" class="sortable">
				</div>
				<div class="element-add-item">
					<a class="element-add-item-link">Add +</a>
				</div>
			</div>
			`;
}

function myCheck_(c)
{
	return `<input id="asset-${c.id}" name="asset-${c.id}" class="form-control asset-input" type="checkbox" data-type="checkbox" data-id="${c.id}"></input>`;
}

function myNumber_(c)
{
	return `<input id="asset-${c.id}" name="asset-${c.id}" class="form-control asset-input" type="number" data-type="number" data-id="${c.id}"></input>`;
}

function myInput_(c)
{
	return `<input id="asset-${c.id}" name="asset-${c.id}" class="form-control asset-input" type="text"  data-type="text" data-id="${c.id}"></input>`;
}

function myContainer_(c, input)
{
	var dimension = c.dimension || "100";
	return `<div class=" width-${dimension} ${c.name}">
			<div class="panel panel-body text-center" data-toggle="match-height">
				<h5>${c.description} </h5>                
				<div class="input-with-icon">
					${input}
					<span class="custom-message-error"></span>
				</div>
				</div>
			</div>`;
}

function sortByAssets(fields)
{
	try {
		fields.assets.sort(function(a, b) { 
			return parseInt(a.position) - parseInt(b.position) 
		});
		return fields;

	} catch (error) {
		return fields;
	}
}

function sortTabs()
{
	$('#dynamic-form-wrapper-tab > div').sort(function (a, b) 
	{ 
		var b1 = parseInt($(b).attr("data-sort")); 
		var a1 = parseInt($(a).attr("data-sort"));
		return a1 - b1; 
	}).appendTo('#dynamic-form-wrapper-tab');

	$("#TAB-1").addClass("active");
	$("#TAB-MAIN-1").addClass("active");
}

function buildAssetsForm(fields) 
{
	//findAssetsEvents();
	myMapInputs(sortByAssets(fields));
	sortTabs();
	myAssetsEvents();
	//createRulesForm();
	
	$("#config-lang-select").change();


	//$("#dynamic-form-wrapper").append(componentForm.join(''));
	
	//fieldAssets = fields.assets;
	//var ruleObject = getValidationObject(fields.assets);
	//console.log(JSON.parse(ruleObject));
	
	//createRulesForm(fields.assets);
	
	/*$("#save-assets").click(function(){
		event.preventDefault();
		$("#assets-event-form").submit();
	})
	
	$("#fields-config").hide();
	
	*/
}

function createRulesForm(ruleObject)
{
	$("#assets-event-form").validate({
		rules: JSON.parse(ruleObject),
		submitHandler: saveAssets,
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
}

function saveAssets() {
	var methodCall = $("#assets-event-form").data("form");
	var data = getDataFromInputs();
	console.log(data);
	$.ajax({
	    type: "POST",
	    contentType: "application/json; charset=UTF-8",
	    url: path_ajax + "search/api/"+methodCall,
	    data: JSON.stringify({eventAssets: data}),
	    dataType: "json",
	    timeout: 100000,
	    beforeSend: function() {
	    	$("#save-assets").hide();
	    	$(".save-assets-loader").show();
	    },
	    success: function(data) {
	      console.log("SUCCESS: ", data);
	      if (data.returnCode === 0) {
	    	  showSuccessModal();
	    	  $("#save-assets").show();
		      $(".save-assets-loader").hide();
	      } else {
	    	  
	    	  if(data.returnMessage == "logout") {
	    		  window.location = path_ajax;
	    	  } else {

		    	  $("#save-assets").show();
			      $(".save-assets-loader").hide();
	    		  if(data.returnMessage == "logout") {
		    		  window.location = path_ajax;
		    	  } else {
		    		  showErrorModal(data.returnMessage);
		    	  }
	    	  }
	    	  
	      }
	    },
	    error: function(e) {
	    	showErrorModal("An error has ocurred, please check you internet connection or contact support.");
	    	  $("#save-assets").show();
		      $(".save-assets-loader").hide();
	    }
	  });
}


function getValuesAssets(v)
{ 
	if ($(v).data("type") == "checkbox")
		return v.checked.toString();
	if ($(v).data("type") == "array")
		return 	getValueTypeArray(v);
	else
		return v.value;
}

function getValueTypeArray(v)
{
	var data       = new Array();
	var array      = new Array();
	var name       = "";
	var value      = "";
	var count      = 0;

	try
	{
		$(v).find(".mySortPersonal").each(function( index_ ) 
		{
				array[count] = new Object();
				$( this ).find("input").each(function( index__ ) 
				{
					name  = $(this).attr("name");
					value = $(this).val();
					array[count][name] = value; 
				});
				count++;
		});
		return JSON.stringify(array);
	}
	catch
	{
		return "";
	}
}

function getDataFromInputs()
{
	var elms 	   = document.querySelectorAll(".asset-input");
	var methodCall = $("#assets-event-form").data("form");

	var data = [...elms].map(function(v)
	{

		var e = { assetId: parseInt(v.getAttribute("data-id")), langId: parseInt($("#config-lang-select").val()), assetValue: getValuesAssets(v), };
		
		if (v.getAttribute("data-update") != "" && v.getAttribute("data-update") != null && v.getAttribute("data-update") != undefined)
			e.id = parseInt(v.getAttribute("data-update"))

		return e;
	});

	return data;
}

function showSuccessModal() {
	  $("#successModalAlert").modal("show");
	}

	function showErrorModal(error) {
	  $("#error-modal-msg").text(error);
	  $("#dangerModalAlert").modal("show");
	}


$(document).ready(function(){
	getLanguagesConfig();
});

function activateSimLive() {
	var isChecked = $('#toggle-simlive').is(':checked');
	if(isChecked){
		$(".urlPlayer").removeClass("col-md-4");
		$(".urlPlayer").addClass("col-md-12");
		
		let simConfigTemplate = buildSimLiveForm();
		$(".simlive-config-container").html(simConfigTemplate);
		timepickerWithDatepicker();

		var rules = getValidationObject(fieldAssets);
		if ($("#assets-event-form").data("validator"))
        $("#assets-event-form").data("validator").destroy();
    
		$("#assets-event-form").validate({
		  rules: JSON.parse(rules),
		  submitHandler: saveAssets,
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

		$("#start_time").change(function(e){
			e.preventDefault();
			var url = $("#asset-1").val();
			var stringStartDate = $("#start_date").val() + " " +  $("#start_time").val();
				stringStartDate = stringStartDate.trim();
			var currentStartDate = new Date( stringStartDate);
				currentStartDate = currentStartDate.getTime();
				startDateMilis = currentStartDate;
				if(url != null && url != "" && stringStartDate != "" && !(typeof videoDuration === "undefined")) {
					calculateEndEventTime();
				}
		});

		$("#start_date").change(function(e){
			e.preventDefault();
			var url = $("#asset-1").val();
			var stringStartDate = $("#start_date").val() + " " +  $("#start_time").val();
				stringStartDate = stringStartDate.trim();
			var currentStartDate = new Date( stringStartDate);
				currentStartDate = currentStartDate.getTime();
				startDateMilis = currentStartDate;
				if(url != null && url != "" && stringStartDate != "" && !(typeof videoDuration === "undefined")) {
					calculateEndEventTime();
				}
		});

		
	} else {
		$(".urlPlayer").removeClass("col-md-12");
		$(".urlPlayer").addClass("col-md-4");
		$(".simlive-config-container").html("");
	}

	function timepickerWithDatepicker() {
    var $datepair = $('#demo-timepicker-6'),
        $timepicker = $datepair.find('.time'),
        $datepicker = $datepair.find('.date');

    $timepicker.timepicker({
      'showDuration': true,
      'timeFormat': 'G:i:s'
    });

    $datepicker.datepicker({
      'format': 'MM d, yyyy',
      'autoclose': true
    });

    $datepair.datepair();
  }
	
}

function buildSimLiveForm() {
	let componentSimLive = `		
			<div  class="col-xs-12 col-md-12">
              <div class="panel panel-body text-center">
                <div id="demo-timepicker-6" class="row gutter-xs">
                  <div class="col-xs-12 col-md-12 flex-container">
	                  <div class="col-xs-12 col-md-4 m-b">
	                    <div class="input-with-icon">
	                      <input class="form-control date start" name="start_date" id="start_date" type="text" placeholder="From date">
	                      <span class="icon icon-calendar input-icon"></span>
	                       <span class="custom-message-error"></span>
	                    </div>
	                  </div>

	                  <div class="col-xs-12 col-md-4 m-b">
	                    <div class="input-with-icon">
	                      <input class="form-control time start ui-timepicker-input" name="start_time" id="start_time" type="text" placeholder="From time" autocomplete="off">
	                      <span class="icon icon-clock-o input-icon"></span>
	                       <span class="custom-message-error"></span>
	                    </div>
	                  </div>
                  </div>
                  <div class="col-xs-12 col-md-12 flex-container">
	                  <div class="col-xs-12 col-md-4">
	                    <div class="input-with-icon">
	                      <input readonly class="form-control" name="end_date" id="end_date" type="text" placeholder="Until date">
	                      <span class="icon icon-calendar input-icon"></span>
	                       <span class="custom-message-error"></span>
	                    </div>
	                  </div>
	                  <div class="col-xs-12 col-md-4">
	                    <div class="input-with-icon">
	                      <input readonly class="form-control ui-timepicker-input" name="end_time" id="end_time" type="text" placeholder="Until time" autocomplete="off">
	                      <span class="icon icon-clock-o input-icon"></span>
	                       <span class="custom-message-error"></span>
	                    </div>
	                  </div>
	              </div>
                  <div class="col-xs-12 col-md-12">
                    <div class="input-with-icon end_message_container" style="margin-top:15px;">
                       <label for="end_message">End event message:</label>
                      <textarea class="form-control" name="end_message" id="end_message"  placeholder="End event message" ></textarea>
                       <span class="custom-message-error"></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
		`;

	return componentSimLive;
}

function getValidationObject(elms) {
	let ruleObject = "{";
	var isChecked = $('#toggle-simlive').is(':checked');

	for(var i = 0; i < elms.length; i++) {

		var vimeoRequired = (elms[i].id == 1 && isChecked == true) ? ', "requiredVimeo" : true' : "";

		ruleObject += '"asset-' + elms[i].id +'":{ },';

		if(i == (elms.length - 1) && isChecked == false){
			ruleObject += '"asset-' + elms[i].id +'":{  }';
		}
	}

	if(isChecked){
		ruleObject += '"start_date":{"required": true}, '; 
		ruleObject += '"start_time":{"required": true}, '; 
		ruleObject += '"end_date":{"required": true}, '; 
		ruleObject += '"end_time":{"required": true}, '; 
		ruleObject += '"end_message":{"required": true} '; 
	}

	ruleObject += "}";



	return ruleObject;
}

//vimeo validation
function validateVimeoVideoDuration() {
	try {

		var oldIframe = document.querySelector("#liveVideo");

		if($("#asset-1").val() == "" || $("#asset-1").val() == null)
			throw "No video url";

		if(oldIframe != null)
			$("#liveVideo").remove();

		var iframeVideo = document.createElement('iframe');
		iframeVideo.frameBorder = 0;
		var iframeContainer = document.querySelector('#video-container');
		iframeVideo.src = $("#asset-1").val();
		iframeVideo.id = "liveVideo";
		iframeVideo.style.margin = "auto";
		iframeContainer.style = {};	
		document.querySelector("#video-duration").innerText = "";
		iframeContainer.insertAdjacentElement("afterbegin", iframeVideo);
		
		var player = new Vimeo.Player(iframeVideo);

		player.getDuration().then(function(duration) {
			videoDuration = duration * 1000;
			var hours = duration / 3600;
			var hoursRounded = Math.floor(hours);
			var minutes = (hours - hoursRounded) * 60;
			var minutesRounded = Math.floor(minutes);
			var minuteFormatted = minutesRounded < 10 ? "0" + minutesRounded : minutesRounded;
			var seconds = (minutes - minutesRounded) * 60;
			var secondsRounded = Math.floor(seconds);
			var secondsFormatted = secondsRounded < 10 ? "0" + secondsRounded : secondsRounded;

			document.querySelector("#video-duration").innerText = " " + hoursRounded+" hours "+minuteFormatted+" minutes " + secondsRounded + " seconds";
			
			iframeContainer.style.justifyContent = "center";
			iframeContainer.style.flexDirection  = "column";
			iframeContainer.style.alignItems = "center";
			iframeContainer.style.display = "flex";

			calculateEndEventTime();

		});
	} catch(error) {
		console.error(error);
	}
	
}
function calculateEndEventTime() {
	if(startDateMilis != 0 && videoDuration != 0) {
		var calculatedEndDate = startDateMilis + videoDuration;
		var endDate = new Date(calculatedEndDate);
		var minutes_end = endDate.getMinutes() < 10 ? "0"+ (endDate.getMinutes() ) : (endDate.getMinutes());
		var seconds_end = endDate.getSeconds() < 10 ? "0" + (endDate.getSeconds()) : endDate.getSeconds();
		$("#end_date").val(months[endDate.getMonth()]+" "+endDate.getDate()+", "+endDate.getFullYear());
		$("#end_time").val(endDate.getHours() + ":" + minutes_end + ":" + seconds_end); 
	}
}

$("#asset-1").change(function(e){
	var url = $("#asset-1").val();
	var stringStartDate = $("#start_date").val() + " " +  $("#start_time").val();
		stringStartDate = stringStartDate.trim();
	var currentStartDate = new Date( stringStartDate);
		currentStartDate = currentStartDate.getTime();

		if(url != null && url != "" && stringStartDate != "" && !(typeof videoDuration === "undefined")) {
			validateVimeoVideoDuration();
		}
});

$(document).on('click', '#asset-75', function(e) {
	if ($("#asset-75").is(":checked")) {
		$("#asset-76").removeAttr("readonly");
	}
	else {
		$("#asset-76").attr("readonly", "readonly");
		$("#asset-76").val("");
	}
});

jQuery.validator.addMethod(
  "requiredVimeo",
  function(value, element) {
    var valueUrl = $("#asset-1").val();
    var regex = /https:\/\/player\.vimeo\.com\/video\/\d+/;
    var isVimeoUrl = regex.test(valueUrl);

    if(isVimeoUrl) {
    	validateVimeoVideoDuration();
    	return true;
    } else {
    	return false;
    }

  },
  "Please insert a valid vimeo url"
);
