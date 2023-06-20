'use strict';

$(function() {

	//HOME
	var Home = {
			data : null,
			Constants : {
				TOP_LIMIT  : 3,
				LIST_LIMIT : 5,
				DONORS     : 0,
				RECURRINGS : 1,
			},			
			Types : {
				QUANTITY : "QUANTITY",
				AMOUNT   : "AMOUNT",
			},
			init : function init() {
				var data =  { time: $("#representation").val() };
				$.logger.log("Home.init");
				$.admin.dispatch("search/api/dashboard", this.process, data);			
			},
			process : function process(data) {
				$.logger.log("Home.process", data);
				$.home.data = data;
				var icon;
				var data1;
				var data2;
				var panel1;
				var panel2;
				var limit1 = $.home.Constants.TOP_LIMIT;
				var limit2 = $.home.Constants.LIST_LIMIT;			
				var info = [
					{
						panel1 : "#top-networks",
						panel2 : "#income-top",
						data1  : data.sources,
						data2  : data.sources,
						icon   : "icon-link",
					},
					{
						panel1 : "#top-networks-region",
						panel2 : "#income-regions",	
						data1  : data.continents,
						data2  : data.countries,
						icon   : "icon-globe",				
					},
					{
						panel1 : "#top-purposes-donors",
						panel2 : "#purpose-income-top",
						data1  : data.funds,
						data2  : data.funds,
						icon   : "icon-heart",					
					}
					]

				$("#showing").html(data.showing);
				$("#date-from").html(data.datefrom);
				$("#date-to").html(data.dateto);

				info.forEach(function(item, index, data) {
					$.home.buildPanel(item.panel1, $.home.Types.QUANTITY, item.icon, item.data1, item.data2, limit1, limit2);	
					$.home.buildPanel(item.panel2, $.home.Types.AMOUNT, item.icon, item.data1, item.data2, limit1, limit2);
				});
				$("#donors-data").html("");
				$("#income-data").html("");
				$("#recurring-donors-data").html("");
				$("#recurring-income-data").html("");
				$("#total-donors-data").html("");
				$("#total-income-data").html("");
				if (window["Donors"]) {
					window["Donors"].clear();
					window["Donors"].destroy();
				}
				if (window["Income"]) {
					window["Income"].clear();
					window["Income"].destroy();
				}
				if (data.TOTALS[$.home.Constants.DONORS]) {
					var DONORS_QUANTITY = data.TOTALS[$.home.Constants.DONORS].quantity;
					var donors_quantity = $.format.toQuantity(DONORS_QUANTITY);
					$("#donors-data").html(donors_quantity);

					var DONORS_AMOUNT = data.TOTALS[$.home.Constants.DONORS].amount;
					var donors_amount = $.format.toCurrency(DONORS_AMOUNT);
					$("#income-data").html(donors_amount);

					var RECURRINGS_QUANTITY = data.TOTALS[$.home.Constants.RECURRINGS] ? data.TOTALS[$.home.Constants.RECURRINGS].quantity : 0;
					var recurrings_quantity = $.format.toQuantity(RECURRINGS_QUANTITY);
					$("#recurring-donors-data").html(recurrings_quantity);

					var RECURRINGS_AMOUNT = data.TOTALS[$.home.Constants.RECURRINGS] ? data.TOTALS[$.home.Constants.RECURRINGS].amount : 0;
					var recurrings_amount = $.format.toCurrency(RECURRINGS_AMOUNT);
					$("#recurring-income-data").html(recurrings_amount);	
					
					var TOTAL_DONORS_QUANTITY = DONORS_QUANTITY + RECURRINGS_QUANTITY;
					var total_donors_quantity = $.format.toQuantity(TOTAL_DONORS_QUANTITY);
					$("#total-donors-data").html(total_donors_quantity);

					var TOTAL_INCOME_AMOUNT = DONORS_AMOUNT + RECURRINGS_AMOUNT;
					var total_income_amount = $.format.toCurrency(TOTAL_INCOME_AMOUNT);
					$("#total-income-data").html(total_income_amount);	

					$.home.buildChart2("#donors-visitors", $.home.Types.QUANTITY, DONORS_QUANTITY, "Donors", "19, 114, 182", data.details);
					$.home.buildChart("#incomes", $.home.Types.AMOUNT, DONORS_AMOUNT, "Income", "217, 131, 31", data.details);
				}

			},
			buildPanel : function buildPanel(target, type, icon, data1, data2, limit1, limit2) {
				$.logger.log("Home.buildPanel", target, type, data1, data2, limit1, limit2);
				var top = "";
				var list = "";
				var i;
				var style;
				var bg = type == $.home.Types.AMOUNT ? "bg-danger" : "bg-primary";
				i = 0;			
				style = "";

				var data1Order = convertObject(data1).sort(sortByProperty('amountPercentage'));

				for(var index in data1Order)  {
					var item = data1Order[index];
					var label = item.label;
					var percentage = type == $.home.Types.AMOUNT ? $.format.toPercentage(item.amountPercentage) : $.format.toPercentage(item.quantityPercentage);
					var value = type == $.home.Types.AMOUNT ? $.format.toCurrency(item.amount) : $.format.toQuantity(item.quantity);				
					top += '<li class="list-group-item" '+style+'>'+
					' <div class="media">'+
					' <div class="media-middle media-body">'+
					'<h6 class="media-heading">'+
					'<span class="text-muted">'+label+'</span>'+
					'</h6>'+
					'<h4 class="media-heading direct">'+percentage+
					'</h4>'+
					' </div>'+
					'<div class="media-middle media-right">'+
					' <small>'+value+'</small>'+
					'</div>'+
					'<div class="media-middle media-right">'+
					'<span class="' + bg +' circle sq-40">'+
					' <span class="icon '+icon+'"></span>'+
					'</span>'+
					'</div>'+
					'</div>'+
					'</li>';
					if (limit1 && i++ == limit1-1) style="style='display: none;'"; //break;
				}

				i = 0;
				style = "";
				var data2Order = convertObject(data2).sort(sortByProperty('amountPercentage'));

				for(var index in data2Order)  {				
					var item = data2Order[index];
					var label = item.label;
					var percentage = type == $.home.Types.AMOUNT ? $.format.toPercentage(item.amountPercentage) : $.format.toPercentage(item.quantityPercentage);
					var value = type == $.home.Types.AMOUNT ? $.format.toCurrency(item.amount) : $.format.toQuantity(item.quantity);
					list += ' <tr '+style+'>'+
					'<td class="col-xs-1 text-left">'+(i+1)+'.</td>'+
					' <td class="col-xs-6 text-left">'+
					'<a class="link-muted" href="#">'+
					' <span class="truncate">'+label+'</span>'+
					'</a>'+
					'</td>'+
					'<td class="col-xs-5 text-right">'+ value +'</td>'+
					'</tr>';
					if (limit2 && i++ == limit2-1) style="style='display: none;'"; //break;				
				}

				$(target + " .section1").html(top);				
				$(target + " .section2").html(list);		
			},
			buildLabel : function buildLabel(str) {
				var parts = str.split("-");
				var size = parts.length;
				switch(size) {
				case 2:
					return parts[1] + "W";
				case 3:
					return parts[2] + " " + $.label["month-"+parts[1]];
				case 4:
					return parts[3] + ":00";
				default:
					return str;
				} 
			},
			buildChart : function buildChart(target, type, total, legend, color,  data) {			
				var canvas = $(target);
				var axisY = total;
				var axisX = [];
				var labels = [];
				var QUANTITY = 0;
				var array = Object.keys(data).map(function (key) { return data[key]; });
				array.sort(function(a, b){
					var aName = a.label.toLowerCase();
					var bName = b.label.toLowerCase(); 
					return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
				});			
				$.logger.log("Home.buildChart array: ", array);
				for(var i in array) {
					var item = array[i];
					QUANTITY += type == $.home.Types.AMOUNT ? item.amount : item.quantity;
					axisX.push(QUANTITY);
					var label = $.home.buildLabel(item.label);							
					labels.push(label);
				}
				$.logger.log("Home.buildChart labels: ", labels);
				if (window[legend]) {
					window[legend].clear();
					window[legend].destroy();
				}
				window[legend] = new Chart(canvas, {
					type: 'bar',
					data: {
						datasets: [
							{
								label: legend,
								backgroundColor: 'rgb('+color+')',
								borderColor: 'rgb('+color+')',
								data: axisY,
							}, 
							{
								label: legend,
								backgroundColor: 'rgba('+color+', 0.4)',
								borderColor: 'rgb('+color+')',
								data: axisX,
								type: 'line'
							}
							],
							labels: labels
					},
					options: {
						legend: false,

						tooltips: {
							callbacks: {
								label: function(t, d) {

									if(legend=="Income"){
										var xLabel = d.datasets[t.datasetIndex].label;				                    
										var yLabel = t.yLabel >= 1000 ? '$' + t.yLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : '$' + t.yLabel;
										return xLabel + ': ' + yLabel;
									}
									else
									{
										var xLabel = d.datasets[t.datasetIndex].label;				                    
										var yLabel = t.yLabel >= 1000 ? + t.yLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") : + t.yLabel;
										return xLabel + ': ' + yLabel;

									}
								}
							}
						},

						scales: {
							yAxes: [
								{
									ticks: {
										callback: function(label) {return "$" + currencyFormatter(label)}
									},
									scaleLabel: {

									}
								}
								]
						}
					}
				});			
			},
			buildChart2 : function buildChart(target, type, total, legend, color,  data) {			
				var canvas = $(target);
				var axisY = total;
				var axisX = [];
				var labels = [];
				var QUANTITY = 0;
				var array = Object.keys(data).map(function (key) { return data[key]; });
				array.sort(function(a, b){
					var aName = a.label.toLowerCase();
					var bName = b.label.toLowerCase(); 
					return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
				});			
				$.logger.log("Home.buildChart array: ", array);
				for(var i in array) {
					var item = array[i];
					QUANTITY += type == $.home.Types.AMOUNT ? item.amount : item.quantity;
					axisX.push(QUANTITY);
					var label = $.home.buildLabel(item.label);							
					labels.push(label);
				}
				$.logger.log("Home.buildChart labels: ", labels);
				if (window[legend]) {
					window[legend].clear();
					window[legend].destroy();
				}
				window[legend] = new Chart(canvas, {
					type: 'bar',
					data: {
						datasets: [
							{
								label: legend,
								backgroundColor: 'rgb('+color+')',
								borderColor: 'rgb('+color+')',
								data: axisY,
							}, 
							{
								label: legend,
								backgroundColor: 'rgba('+color+', 0.4)',
								borderColor: 'rgb('+color+')',
								data: axisX,
								type: 'line'
							}
							],
							labels: labels
					},
					options: {
						legend: false


					}
				});			
			}



	};
	Home.init();
	$.home = Home;

	//EVENTS
	var RepresentationChangeEvent = {
			target   : "#representation",
			event    : "change",
			action   : "search/api/dashboard",
			data     : null,
			prepare  : function() {
				this.data = { 
						time: $("#representation").val()
				}		
			},
			callback : function(data) {
				$.logger.log("RepresentationChangeEvent.callback: ", data);
				$.home.process(data);
			}
	}	
	$.admin.registerEvent(RepresentationChangeEvent);


	//MODALS EVENTS
	var ModalEvent = {
			target   : ".link-modal",
			event    : "click",
			action   : null,
			data     : null,
			prepare  : null,
			callback : function(evt) {
				$.logger.log("ModalEvent.callback", evt);
				var topColumn = '';
				var title = $(evt.currentTarget).prev().html();
				var topList = '';
				var template = '<div style="width: 600px">'+
				'<div class="card">'+
				'<div class="card-header">'+
				'<strong>'+title+'</strong>'+
				'</div>'+
				$(evt.currentTarget).parent().next().html().split('style="display: none;"').join("") 
				+'</div></div>';

				$.fancybox.open(template);
			}
	}	
	$.admin.registerEvent(ModalEvent);	


	//ACTIVE MENU OPTION
	$.admin.activeMenuOption("menuDonations");
});

$('#reload').click(function(){
	$.home.init();
});

var sortByProperty = function (property) {

	return function (x, y) {

		return ((x[property] === y[property]) ? 0 : ((x[property] > y[property]) ? 1 : -1))*-1;

	};

};


function convertObject(items) {

	var itemsArr = Object.values(items);

	var sortedItems = itemsArr.sort(function(c, b){return c.percentage <= b.percentage ? -1 : 1});

	return sortedItems;

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
