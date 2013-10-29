
var information = new Object();

$(document).ready(function(){
	
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
		
		information.soilthreshold = $.cookie("soilthreshold");
		
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
	//var evar =information.email.toString();
	var json = $.toJSON(information);
	//alert(json);
	/*
	$("#step6").click(function(){
		
		$.ajax({
			
			url:"/calculate",
			type:"post",
			data:{"json":json},
			dataType:"json",
			success: function(data){
				
				var loss = data.Loss;
				var hour = data.Hour;
				alert(hour);
				
			},
			error:function(){
				
				alert("exception");
			}
			
			
			
		});
		
		
		
		//$.cookie(eval("evar"),json, {expires : 1});
		//alert($.cookie(eval("evar")));
		//location.href="/results.html";
		
		
	});
	*/
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

