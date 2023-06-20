var ajaxPath = "/oferta/";

$(document).ready(function()
{
	var qr 		 = $('#qrr-container');
	var target = $('#container-camera');

	target.html(qr);
});

$(document).on('click', '#openreader-btn' ,function()
{
	$('#result').empty();
});

$("#openreader-btn").qrCodeReader(
{
	audioFeedback: false,
  callback: function(code) 
	{
		$.ajax(
		{
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			url: ajaxPath + "search/api/scan-qr",
			data: JSON.stringify({qr: code}),
			dataType: "json",
			success: function(response)
			{
				try 
				{
					if(response.returnCode == 0)
					{
						showSuccess(response.result);
					}
					else if(response.returnCode == -61){
						showCase69(response);
					}else{
						showError(response.returnMessage);
					}
				} 
				catch (err) 
				{
					console.log(err)
					showError(err)
				}
			},
			error: function(err)
			{
				showError(err.responseText);
			}
		});
  }
});

function showSuccess(data)
{
	var target = $('#result');

	var template =
	`
		<div class="container-success">
			<div class="title">Ticket registrado</div>

			<div class="items">
				<div class="item">
					<div class="left">Nombre:</div>
					<div class="right">${data.ticketOwner?.firstName} ${data.ticketOwner?.lastName}</div>
				</div>

				<div class="item">
					<div class="left">Correo Electrónico:</div>
					<div class="right">${data.ticketOwner?.email}</div>
				</div>
			</div>
		</div>
	`;

	target.html(template);
}

function showCase69(data)
{
	var target = $('#result');
	var registrationDate = new Date();
	registrationDate.setTime(data.result.ticketData?.checkInDate);
	var registrationDateString = registrationDate.toUTCString();
	var template =
		`
		<div class="container-success">
			<div class="title">Ticket Usado</div>

			<div class="items">
				<div class="item">
					<div class="left">Correo Operador:</div>
					<div class="right">${data.result.ticketData?.checkInAdminUserEmail} </div>
				</div>
				<div class="item">
					<div class="left">Fecha de uso:</div>
					<div class="right">${registrationDateString}</div>
				</div>
				<div class="item">
					<div class="left">Id Transacción:</div>
					<div class="right">${data.result.ticketData?.transactionId}</div>
				</div>
			</div>
		</div>
	`;

	target.html(template);
}

function showError(data)
{
	var target = $('#result');

	target.html(data);
}