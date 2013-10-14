
var information = new Object();

$(document).ready(function(){

	$("#step6").click(function(){
		
		var choice = $("input[name='correspondence']:checked").val();
		//alert(choice);
		$.cookie("choice",choice,{ expires : 7});
		information.email = $.cookie("email");
		information.password = $.cookie("password");
		information.unit = $.cookie("unit");
		information.zipcode = $.cookie("zipcode");
		information.rd = $.cookie("rd");
		information.soilType = $.cookie("soilType");
	    information.area = $.cookie("area");
	    information.systemSelection = $.cookie("systemSelection");
	    if($.cookie("rainsettings")){
			information.rainsettings = $.cookie("rainsettings");
			
		}
		if($.cookie("soilthreshold")){
			
			information.soilthreshold = $.cookie("soilthreshould");
			
		}
		if($.cookie("irriDepth")){
			
			information.irriDepth = $.cookie("irriDepth");
			
		}
		
		
		if($.cookie("isystem")){
			
			information.isystem = $.cookie("isystem");
			information.irriDuration = $.cookie("irriDuraion");
			
		}
		
		information.days = $.cookie("days");
		information.hours = $.cookie("hours");
		information.minutes = $.cookie("minutes");
		information.choice = $.cookie("choice");
		var evar =information.email.toString();
		var json = $.toJSON(information);
		$.cookie(eval("evar"),json, {expires : 1});
		
		
		
		location.href="/results.html";
		
		
	});
	//alert($.cookie("jiadw007@gmail.com"));

});

//
		//alert();
		//information.choice= choice;
		//var json = $.toJSON(information);
		//var evar =information.email.toString();
		//$.cookie(eval("evar"),$.toJSON(information));
		//alert($.toJSON(information));
		//alert($.cookie("jiadw007@gmail.com"));
		//var json = $.toJSON(information);
	
//

