var fullData;
var totals = {
		donors: 0,
		recurringDonors: 0,
		income: 0,
		recurringIncome: 0,
		direct: 0,
		direct_income: 0,
		referrals: 0,
		search_engines: 0,
		referrals_income: 0,
		search_engines_income: 0,
		continents: [],
		purposes: []
	};

var details = {

		donors:[],
		countries: []
};

var reload = document.getElementById("reload");
var select_type = document.getElementById("representation");
var representation = document.getElementById("representation").value;
var donors = document.getElementById("donors-data");
var recurrings = document.getElementById("recurring-donors-data");
var income = document.getElementById("income-data");
var income_recurring = document.getElementById("recurring-income-data");
var dateTo = document.getElementById("date-to");
var dateFrom = document.getElementById("date-from");
var ctx_donors = document.getElementById("donors-visitors").getContext('2d');;
var ctx_incomes = document.getElementById("incomes").getContext('2d');;

reload.addEventListener("click", function(e){

	e.preventDefault();
	document.querySelector(".container-spinner").innerHTML = '<div class="spinner spinner-info custom-icon"></div>';
	id = e.target.id;
	e.target.classList.add("hidden"); 
	totals = {
		donors: 0,
		recurringDonors: 0,
		income: 0,
		recurringIncome: 0,
		direct: 0,
		direct_income: 0,
		referrals: 0,
		search_engines: 0,
		referrals_income: 0,
		search_engines_income: 0,
		continents: []
	};

	details = {

		donors:[],
		countries: []
	};


	init();

});

select_type.addEventListener("change", function(e){

	e.preventDefault();
	representation = e.target.value;
	reload.click();
});



function init() {

	let header = new Headers();
	header.append("Content-type", "application/json");

	let init = {
		headers: header
	};

	fetch("resources/core/js_old/sample_days.json", init)
		.then((response, body) => {

			 let contentType = response.headers.get("content-type");
			  if(contentType && contentType.indexOf("application/json") !== -1) {
			    
			    response.json().then( (json) =>{
			      // process your JSON further
			      document.querySelector("#reload").classList.remove("hidden");
				 document.querySelector(".container-spinner").innerHTML = '';

			     fullData = json[representation];
				setDateRange(fullData.dayFrom, fullData.dayTo);

			    buildDataAndPrint(fullData);

			    });

			  } else {
			    console.log("Error, no trajo el JSON");
			  }
		});
}

init();

function getRefreshData() {


	fetch("resources/core/js_old/sample_days.json", init)
		.then((response, body) => {

			 let contentType = response.headers.get("content-type");
			  if(contentType && contentType.indexOf("application/json") !== -1) {
			    
			    response.json().then( (json) =>{
			      // process your JSON further

			     fullData = json[representation]; 

			    });

			  } else {
			    console.log("Error, no trajo el JSON");
			  }
		});

}

function buildDataAndPrint(completeData) {

	groupNetworks();

	totals.continents = totals.continents.sort(function(v, d){
		return v.donor < d.donor;
	});

	buildCharts(completeData.data);
	setTotalData(completeData.data);
	fillTopNetworks(completeData.data, details.donors, false);
	fillTopNetworksByCountries(completeData.data, details.countries, false);
	fillTopIncomes(completeData.data, details.donors, false);
	fillTopIncomesByCountries(completeData.data, details.countries, false);
	fillPurposesDonors(completeData.data, totals.purposes, false);
	 fillPurposesIncome(completeData.data, totals.purposes, false);
}


function setTotalData(data) {

	
	data.forEach(v => {

		// suma de datos totales
		totals.donors += parseInt(v.totals.donors);
		totals.recurringDonors += parseInt(v.totals.recurrings);
		totals.income += parseInt(v.totals.donors_income);
		totals.recurringIncome += parseInt(v.totals.recurring_income);
		totals.direct += parseInt(v.totals.direct);
		totals.referrals += parseInt(v.totals.referrals);
		totals.search_engines += parseInt(v.totals.search_engines);

		totals.direct_income += parseInt(v.totals.direct_income);
		totals.referrals_income += parseInt(v.totals.referrals_income);
		totals.search_engines_income += parseInt(v.totals.search_engines_income);

		

	});




	donors.innerText = currencyFormatter(totals.donors, false);
	recurrings.innerText = currencyFormatter(totals.recurringDonors, false);
	income.innerText = "$ " + currencyFormatter(totals.income, true);
	income_recurring.innerText = "$ " + currencyFormatter(totals.recurringIncome, true);
}


function setDateRange(from, to) {

	dateFrom.innerText = from;
	dateTo.innerText = to;


}

function fillTopNetworks (data, dataList, isModal) {

	let total_networks = totals.donors;
	let list_networks = [];
	let direct = totals.direct;
	let direct_percent = Math.round((direct/total_networks) * 100);
	let referrals = totals.referrals;
	let referrals_percent = Math.round((referrals/total_networks) * 100);
	let search_engines = totals.search_engines;
	let search_engines_percent = Math.round((search_engines/total_networks) * 100);

	let topNetworks = [

			{label: "Direct", percent: direct_percent, cant: direct, icon:"icon-arrow-right"},
			{label: "Referrals", percent: referrals_percent, cant: referrals, icon:"icon-link"},
			{label: "Search Engines", percent: search_engines_percent, cant: search_engines, icon:"icon-search"},
	];

	 topNetworks = topNetworks.sort(function (v, d) {return v.cant < d.cant});
	
	let templateNetwowrks = "";

	for(let i = 0; i < topNetworks.length; i++) {

		templateNetwowrks += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+topNetworks[i].label+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+topNetworks[i].percent+
		                               ' <small>'+currencyFormatter(topNetworks[i].cant, false)+'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-primary circle sq-40">'+
		                               ' <span class="icon '+topNetworks[i].icon+'"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
	}

	let orderedList = dataList.sort(function (v, d) {return v.donor < d.donor});

	let template = "";
	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;
		

	for(let i = 0; i < limit; i++) {

	 template += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                         '<td class="col-xs-4 text-right">'+ currencyFormatter(orderedList[i].donor, false)+'</td>'+
                    '</tr>';

       

      }

	 if(!isModal) {
	    document.querySelector("#top-networks .network-list-top").innerHTML = template;
		document.querySelector("#top-networks .list-group").innerHTML = templateNetwowrks;

	}


	return template


}

function fillTopNetworksByCountries (data, dataList, isModal) {

	let total_networks = totals.donors;
	let list_continents = totals.continents;
	// let direct_percent = Math.round((direct/total_networks) * 100);
	

	let orderedList = dataList.sort(function (v, d) {return v.donor < d.donor});

	console.log(orderedList);
	let templateCountries = "";
	let templateContinents ="";

	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;

	let limitContinents = list_continents.length < 3 ? list_continents.length : 3;


	for(let i = 0; i < limit; i++) {

	 templateCountries += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                         '<td class="col-xs-4 text-right">'+ currencyFormatter(orderedList[i].donor, false)+'</td>'+
                    '</tr>';

      }

     for (let i = 0; i < limitContinents; i++) {

        templateContinents += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+list_continents[i].name+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+Math.round((list_continents[i].donor/total_networks) * 100)+
		                               ' <small>'+ currencyFormatter(list_continents[i].donor, false) +'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-primary circle sq-40">'+
		                               ' <span class="icon icon-globe"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
     }

    if(!isModal) {
    document.querySelector("#top-networks-region .list-group").innerHTML = templateContinents;
    document.querySelector("#top-networks-region .network-list-top").innerHTML = templateCountries;
}

    return templateCountries
}


function fillTopIncomesByCountries (data, dataList, isModal) {

	let total_networks = totals.donors;
	let list_continents = totals.continents;	

	let orderedList = dataList.sort(function (v, d) {return v.income < d.income});

	let templateCountries = "";
	let templateContinents ="";

	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;

	let limitContinents = list_continents.length < 3 ? list_continents.length : 3;
		list_continents = list_continents.sort(function (v, d) {return v.income < d.income});



	for(let i = 0; i < limit; i++) {

	 templateCountries += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                         '<td class="col-xs-4 text-right">$'+ currencyFormatter(orderedList[i].income, true)+'</td>'+
                         
                    '</tr>';

      }

     for (let i = 0; i < limitContinents; i++) {

        templateContinents += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+list_continents[i].name+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+Math.round((list_continents[i].income/total_networks) * 100)+
		                               ' <small>$'+ currencyFormatter(list_continents[i].income, true) +'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-danger circle sq-40">'+
		                               ' <span class="icon icon-globe"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
     }

    if(!isModal) {
    document.querySelector("#income-regions .list-group").innerHTML = templateContinents;
    document.querySelector("#income-regions .network-list-top").innerHTML = templateCountries;

	}

    return templateCountries

}


function fillTopIncomes (data, dataList, isModal) {

	let total_networks = totals.income;
	let list_networks = [];
	let direct = totals.direct_income;
	let direct_percent = Math.round((direct/total_networks) * 100);
	let referrals = totals.referrals_income;
	let referrals_percent = Math.round((referrals/total_networks) * 100);
	let search_engines = totals.search_engines_income;
	let search_engines_percent = Math.round((search_engines/total_networks) * 100);

	let orderedList = dataList.sort(function (v, d) {return v.income < d.income});

	let template = "";
	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;

	for(let i = 0; i < limit; i++) {

	 template += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                         '<td class="col-xs-4 text-right">$'+ currencyFormatter(orderedList[i].income, true)+'</td>'+
                    '</tr>';

      }

    let topNetworks = [

			{label: "Direct", percent: direct_percent, cant: direct, icon:"icon-arrow-right"},
			{label: "Referrals", percent: referrals_percent, cant: referrals, icon:"icon-link"},
			{label: "Search Engines", percent: search_engines_percent, cant: search_engines, icon:"icon-search"},
	];

	 topNetworks = topNetworks.sort(function (v, d) {return v.income < d.income});
	
	let templateNetwowrks = "";

	for(let i = 0; i < topNetworks.length; i++) {

		templateNetwowrks += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+topNetworks[i].label+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+topNetworks[i].percent+
		                               ' <small>$'+  currencyFormatter(topNetworks[i].cant, true)+'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-danger circle sq-40">'+
		                               ' <span class="icon '+topNetworks[i].icon+'"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
	}

 if(!isModal) {
    document.querySelector("#income-top .network-list-top").innerHTML = template;
	document.querySelector("#income-top .list-group").innerHTML = templateNetwowrks;
}

	return template


}

function fillPurposesDonors (data, dataList, isModal) {

	let total_networks = totals.donors;
	let list_purposes = totals.purposes;	

	let orderedList = dataList.sort(function (v, d) {return v.donor < d.donor});
	
	
	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;

	let limitTop = list_purposes.length < 3 ? list_purposes.length : 3;
		
	let templateList = '', templateTop = '';


	for(let i = 0; i < limit; i++) {

	 templateList += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                           ' <td class="col-xs-4 text-right">'+ currencyFormatter(orderedList[i].donor, false)+'</td>'+
                         
                    '</tr>';

      }

     for (let i = 0; i < limitTop; i++) {

        templateTop += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+orderedList[i].name+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+Math.round((orderedList[i].donor/total_networks) * 100)+
		                               ' <small>'+ currencyFormatter(orderedList[i].donor, false)+'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-primary circle sq-40">'+
		                               ' <span class="icon icon-heart"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
     }

    if(!isModal) {
    document.querySelector("#top-purposes-donors .list-group").innerHTML = templateTop;
    document.querySelector("#top-purposes-donors .purpose-list-top").innerHTML = templateList;

	}

    return templateList

}

function fillPurposesIncome(data, dataList, isModal) {

	let total_networks = totals.donors;
	let list_purposes = totals.purposes;	

	let orderedList = dataList.sort(function (v, d) {return v.income < d.income});
	
	
	let limit = orderedList.length < 5 ? orderedList.length : 5;
		limit = isModal ? orderedList.length : limit;

	let limitTop = list_purposes.length < 3 ? list_purposes.length : 3;
		
	let templateList = '', templateTop = '';


	for(let i = 0; i < limit; i++) {

	 templateList += ' <tr>'+
                   		'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
                        ' <td class="col-xs-7 text-left">'+
                              '<a class="link-muted" href="#">'+
                               ' <span class="truncate">'+orderedList[i].name+'</span>'+
                              '</a>'+
                            '</td>'+
                           ' <td class="col-xs-4 text-right">$'+ currencyFormatter(orderedList[i].income, true)+'</td>'+
                         
                    '</tr>';

      }

     for (let i = 0; i < limitTop; i++) {

        templateTop += '<li class="list-group-item">'+
		                         ' <div class="media">'+
		                           ' <div class="media-middle media-body">'+
		                              '<h6 class="media-heading">'+
		                                '<span class="text-muted">'+orderedList[i].name+'</span>'+
		                              '</h6>'+
		                              '<h4 class="media-heading direct">%'+Math.round((orderedList[i].donor/total_networks) * 100)+
		                               ' <small>$'+currencyFormatter(orderedList[i].income, true)+'</small>'+
		                              '</h4>'+
		                           ' </div>'+
		                            '<div class="media-middle media-right">'+
		                              '<span class="bg-danger circle sq-40">'+
		                               ' <span class="icon icon-heart"></span>'+
		                              '</span>'+
		                            '</div>'+
		                          '</div>'+
                        	'</li>'
     }

    if(!isModal) {
    document.querySelector("#purpose-income-top .list-group").innerHTML = templateTop;
    document.querySelector("#purpose-income-top .purpose-income-list-top").innerHTML = templateList;

	}

    return templateList

}

function buildModal(target, title, topColumn) {

	var topList;

	if(target == 'top-networks') {

		topList = fillTopNetworks(fullData.data, details.donors, true);

	} else if (target == 'income-top') {

		topList = fillTopIncomes(fullData.data, details.donors, true);

	} else if (target == 'top-networks-region') {

		topList = fillTopNetworksByCountries(fullData.data, details.countries, true);

	} else if (target == 'income-regions') {

		topList = fillTopIncomesByCountries(fullData.data, details.countries, true);

	} else if (target == 'top-purposes-donors') {

		topList = fillPurposesDonors(fullData.data, totals.purposes, true);

	}  else if (target == 'purpose-income-top') {

		topList = fillPurposesIncome(fullData.data, totals.purposes, true);

	}


	var template = '<div id="modal-details-donation" class="modal-donation-container">'+
				        '<div class="card">'+
				                '<div class="card-header">'+
				                  '<div class="card-actions">'+
				                    '<button type="button" class="card-action card-remove" title="Remove"></button>'+
				                  '</div>'+
				                  '<strong>'+title+'</strong>'+
				                '</div>'+
				               ' <div class="card-body">'+
				                  '<div class="row">'+
				                    '<div class="col-md-6 m-y">'+
				                      '<ul class="list-group list-group-divided">'+
				                       topColumn +
				                      '</ul>'+
				                    '</div>'+
				                    '<div class="col-md-6 m-y">'+
				                      '<table class="table table-borderless table-fixed">'+
				                        '<tbody>'+
				                          topList+
				                        '</tbody>'+
				                      '</table>'+
				                    '</div></div></div></div></div>';

	$("body").append(template);

	var modalClose = document.querySelector(".modal-donation-container .card-remove");
	var modal = document.getElementById("modal-details-donation");

	modalClose.addEventListener("click", (e)=>{  e.preventDefault(); modal.remove(); });


}

var modal_trigger = $(".link-modal");

modal_trigger.click(function(e){

	e.preventDefault();
	var dataTarget = e.target.getAttribute("data-target");
	var modalTitle = $("#"+dataTarget+" .card-header strong").text();
	var firstColumn = $("#"+dataTarget+" .list-group").html();

	 buildModal(dataTarget, modalTitle, firstColumn);
});


function groupNetworks() {

	var data = fullData.data;

	var group = [];
	var countries = [];
	var continents = [];
	var purposes = [];

	// Grouping social medias and search engines
	data.forEach(function (v){

		group = group.concat(v.details.social_networks.concat(v.details.websites.concat(v.details.search_engines)));

		countries = countries.concat(v.details.countries);

		continents = continents.concat(v.totals.continents);

		purposes = purposes.concat(v.totals.purposes);

	});

	var newArr = [], newCont = [], newCountries = [], newPurposes = [];

	group.forEach(function(v){

		var exist = newArr.findIndex(function(j){ return j.name == v.name});
		if(exist == -1) {

			newArr.push(v);

		} else {

			newArr[exist].donor += v.donor;
			newArr[exist].income += v.income;
		}

	});

	continents.forEach(function(v){

		var exist = newCont.findIndex(function(j){ return j.name == v.name});
		if(exist == -1) {

			newCont.push(v);

		} else {

			newCont[exist].donor += v.donor;
			newCont[exist].income += v.income;
		}

	});

	countries.forEach(function(v){

		var exist = newCountries.findIndex(function(j){ return j.name == v.name});
		if(exist == -1) {

			newCountries.push(v);

		} else {

			newCountries[exist].donor += v.donor;
			newCountries[exist].income += v.income;
		}

	});

	purposes.forEach(function(v){

		var exist = newPurposes.findIndex(function(j){ return j.name == v.name});
		if(exist == -1) {

			newPurposes.push(v);

		} else {

			newPurposes[exist].donor += v.donor;
			newPurposes[exist].income += v.income;
		}

	});


	details.donors = newArr;
	details.countries = newCountries;

	totals.continents = newCont;
	totals.purposes = newPurposes;

}


function buildCharts(data) {

	var labels = data.map(function(v){
		return v.label
	});

	var donorsData = data.map(function(v){
		return v.totals.donors
	});

	var memory = 0;

	var donorsAglomeratte = data.map(function(v){

		memory += v.totals.donors
		return memory
	});

	
	var incomesData = data.map(function(v){
		return v.totals.donors_income
	});

	var memoryIncome = 0;
	
	var incomeAglomeratte = data.map(function(v){

		memoryIncome += v.totals.donors_income
		return memoryIncome
	});

	var chartDonors = new Chart(ctx_donors, {

   type: 'bar',
  data: {
    datasets: [{
          label: 'Bar donors',
          backgroundColor: 'rgb(19, 114, 182)',
          borderColor: 'rgb(19, 114, 182)',
          data: donorsData
        }, {
          label: 'Line donors',
          backgroundColor: 'rgba(19, 114, 182, 0.4)',
          borderColor: 'rgb(19, 114, 182)',
          data: donorsAglomeratte,

          // Changes this dataset to become a line
          type: 'line'
        }],
    labels: labels
  },
  options: {legend: false
  			 

    	}
});

var chartIncomes = new Chart(ctx_incomes, {

   type: 'bar',
  data: {
    datasets: [{
          label: 'Bar donors',
          backgroundColor: 'rgb(243, 105, 34)',
          borderColor: 'rgb(243, 105, 34)',
          data: incomesData
        }, {
          label: 'Line donors',
          backgroundColor: 'rgba(243, 105, 34, 0.4)',
          borderColor: 'rgb(243, 105, 34)',
          data: incomeAglomeratte,

          // Changes this dataset to become a line
          type: 'line'
        }],
    labels: labels
  },
  options: {legend: false
  			 

    	}
});



}


function currencyFormatter(n, income) {

	var numFormatted;

	if(income){

		numFormatted = n.toFixed(2).replace(/./g, function(c, i, a) {
	    return i && c !== "." && ((a.length - i) % 3 === 0) ? ',' + c : c;
		});
	} else {

		numFormatted = n.toString().replace(/./g, function(c, i, a) {
	    return i && c !== "." && ((a.length - i) % 3 === 0) ? ',' + c : c;
		});
	}
	

	return numFormatted
}



$( document ).ready(function() {
	home();
});


$('#representation').on('change', function() {
	home();
})


function home(){
	console.log("Dashboard");
	var period = {}
	period["time"] =  $("#representation").val();
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/radio-zoeadmin/search/api/dashboard",
		data : JSON.stringify(period),
		dataType : 'json',		
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			if (data.code==="200") {
				console.log(data);	
				refreshDashboard(data);
			} else if (data.code==="204") {
				alert("Error");
			} else if (data.code==="400") {
				alert("Error");
			}
		},
		error : function(e) {
			console.log("ERROR: ", e); 
			alert("ERROR");
		},
		done : function(e) {
			alert("DONE");
			console.log("DONE");
		}
	});
}



function refreshDashboard(data) {
		
	$.each(JSON.parse(data.msg), function(i,dashBoard){
		console.log("<br>"+i+" - "+dashBoard.date+" - "+dashBoard.customerId +" - "+dashBoard.fundsDash[0].amount);
	})


		

}

