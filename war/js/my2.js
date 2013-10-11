
var information = new object();

$(document).ready(function(){
	
	$("#step6").click(function(){
		
		var choice = $("input[name='correspondence']:checked").val();
		//alert(choice);
		$.cookie("choice",choice,{ expires : 7});
		//information.choice= choice;
		//var json = $.toJSON(information);
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
			
		}else{
			
			information.isystem = $.cookie("isystem");
			information.irriDuration = $.cookie("irriDuraion");
			
		}
		
		information.days = $.cookie("days");
		information.hours = $.cookie("hours");
		information.minutes = $.cookie("minutes");
		information.choice = choice;
		location.href="/results.html";
		
		
		
	});
	
		
	
});

