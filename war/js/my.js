$(document).ready(function(){
	
	$('.tips').tooltip();
	
	if($.cookie("email")!=null){
		
		var email=$.cookie("email");
		//alert(email);
		var $welcome=$("<li><a>Hi,&nbsp;"+email+"</a></li>");
		var $sibling=$("#additionalbar li:first");
		$sibling.before($welcome);
		$("#additionalbar").css("display","block");
		var $login=$("a[href='login.html']");
		$login.css("display","none");
		$(".inactive").css("display","block");
	}
	
	$("#logout").click(function(){
		
		$.cookie("email","",{ expires:-1 });
		$.cookie("systemSelection","",{ expires:-1 });
		var $login=$("a[href='login.html']");
		//$login.css("display","block");
		//$(".inactive").css("display","none");
		//$(".tool").css("display","none");
		//$("#additionbar li:first").remove();
		//$("#additionbar").css("display","none");
		location.href="/index.html";
		
	});
	$("#signIn").click(function(){
		
		var email= $("#exampleInputEmail1").val();
		var password=$("#exampleInputPassword1").val();
		if(email==""){
			
			alert("email is null");
			return false;
		}else{
			
			$.cookie("email",email,{ expires: 7 });
			//alert(email);
			//alert($.cookie("email"));
		}
		if(password==""){
		
			alert("password is null");
			return false;
		}else{
			
			$.cookie("password",password,{ expires: 7 });
			
		}
		
		//alert();
		
		
	});
	$("a[name='start']").click(function(){
		
		var email=$.cookie("email");
		if(email==null){
			
			location.href="/login.html";
			
		}else{
			
			location.href="/form-1.html";
		}
		
		
	});
	
	$("#step1").click(function(){
		
		//alert();
		var zipcode=new String($("#zipcode").val());
		var unit=$("input[name='optionsRadios']:checked").val();
		if(zipcode==""){
			
			alert("zipcode is null");
			return false;
		}else{
			$.cookie("zipcode",zipcode,{ expires: 7});
			if($.cookie("unit")&&$.cookie("unit")!=unit){
				
				$.cookie("unit2",$.cookie("unit"),{expires : 7});
				$.cookie("unit3",$.cookie("unit"),{expires: 7});
				$.cookie("unit",unit,{expires: 7});
			}else{
				
				$.cookie("unit",unit,{ expires: 7});
			}
			
			location.href="/form-2.html";
		
		}
	});
	if($.cookie("zipcode")){
		
		$("#zipcode").val($.cookie("zipcode"));
	}
	if($.cookie("unit")){
		var unit1=$.cookie("unit");
		if(unit1=="English"){
			
			$("#optionRadios1").attr("checked","");
			$("#optionsRadios2").attr("checked","checked");
			var inches =Math.round(($("#rootDepth").val()/2.54)*1000)/1000;
			$("#rootDepth").attr("placeholder","Root Depth in Inches").val(inches);
			
		}
	}
	if($.cookie("rd")){
		var unit2 =$.cookie("unit");
		$("#rootDepth").val($.cookie("rd"));
		if($.cookie("unit2")){
			
			if(unit2=="Metric"){
				
				var rd=Math.round(($.cookie("rd")*2.54)*1000)/1000;
				$("#rootDepth").val(rd);
				$.cookie("rd",rd,{expires : 7});
				
			}else{
				
				var rd=Math.round(($.cookie("rd")/2.54)*1000)/1000;
				$("#rootDepth").val(rd);
				$.cookie("rd",rd,{expires : 7});
			}
			$.cookie("unit2","",{expires: -1});
		}
	}
	if($.cookie("unit")=="Metric"){
		
		$("select[name='rainsettings']").children("option[value='0.125']").val(0.3175).text("0.3175 cm");
		$("select[name='rainsettings']").children("option[value='0.25']").val(0.635).text("0.635 cm");
		$("select[name='rainsettings']").children("option[value='0.5']").val(1.27).text("1.27 cm");
		$("select[name='rainsettings']").children("option[value='0.75']").val(1.905).text("1.905 cm");
		$("select[name='rainsettings']").children("option[value='1']").val(2.54).text("2.54 cm");
	}
	if($.cookie("soilType")){
		$("select[name='soiltype']").val($.cookie("soilType"));
	}
	if($.cookie("area")){
		
		$("#Area").val($.cookie("area"));
	}
	if($.cookie("rainsettings")){
		
		$("select[name='rainsettings']").val($.cookie("rainsettings"));
		if($.cookie("unit3")){
			
			if($.cookie("unit3")=='English'){
				
				var rain = $.cookie("rainsettings")*2.54;
				
				$("select[name='rainsettings']").val(rain);
				$.cookie("rainsettings",rain,{expires : 7});
			
			}else{
				
				var rain = $.cookie("rainsettings")/2.54;
				$("select[name='rainsettings']").val(rain);
				$.cookie("rainsettings",rain,{expires : 7});
				
			}
			$.cookie("unit3","",{expires: -1});
			
		}
		
		
	}
	if($.cookie("soilthreshold")){
		
		$("select[name='soilthreshold']").val($.cookie("soilthreshold"));
	}
	if($.cookie("days")&&$.cookie("minutes")&&$.cookie("hours")){
		
		var days = $.cookie("days").split(",");
		//alert(days);
		var hours = $.cookie("hours").split(",");
		var minutes = $.cookie("minutes").split(",");
		for (day in days){
			//alert(day);
			$("input[name='schedule'][value='"+days[day]+"']").attr("checked","checked").parent().next().css("display","block");
			$("input[name='schedule'][value='"+days[day]+"']").parent().next().children("select[name='startHour']").val(hours[day]);
			$("input[name='schedule'][value='"+days[day]+"']").parent().next().children("select[name='startMinute']").val(minutes[day]);
		}
		
		
	}
	if($.cookie("choice")&&$.cookie("choice")=="no"){
		$("#correspondence2").attr("checked","checked");
		$("#correspondence1").attr("checked","");
		
	}
	
	$("#step2").click(function(){
		
		//alert();
		var rd=$("#rootDepth").val();
		var soil=$("select[name='soiltype']").val();
		var area=$("#Area").val();
		//alert(soil);
		$.cookie("rd",rd,{ expires:7 });
		$.cookie("soilType",soil,{ expires:7 });
		$.cookie("area",area,{ expires: 7 });
		location.href="/form-3.html";
		return false;
		
	});
	$("#step3").click(function(){
		    
	    var systemSelection=[];  
		$("input[name='checkbox']:checked").each(function(){
			//alert();
			//alert($(this).val());
			var str=$(this).val();
			systemSelection.push(str);
			/*
			if(str == "rainsensor"){
				
				var $li=$("<li><a href='rain-sensor-detail.html'>Rain Sensor Detail</a></li>");
				var $sibling=$("#navTool li:last-child");
				$sibling.before($li);
				
			}
			if(str == "moisture"){
				
				var $li=$("<li><a href='soil-moisture-settings.html'>Soil Moisture Settings</a></li>");
				var $sibling=$("#navTool li:last-child");
				$sibling.before($li);
				
			}
			*/
		});
		//alert(systemSelection.toString());
		if(systemSelection.length!=0){
			
			$.cookie("systemSelection",systemSelection.toString(),{ expires: 7 });
			//alert($.cookie("systemSelection"));
			
			if($.inArray("Time-based with rain sensor",systemSelection)!=-1){
				
				location.href="/rain-sensor-detail.html";
				
				
			}else if($.inArray("Time-based with soil moisture sensor",systemSelection)!=-1){
				
				location.href="/soil-moisture-settings.html";
				
			}else{
				
				location.href="/irrigation-system.html";
			}
		}else{
			
			alert("please select irrigation technology");
		}
		
		
	});
	if($.cookie("systemSelection")){
		
		var systemSelection=$.cookie("systemSelection").split(",").reverse();
		//alert(systemSelection);
		
		//alert($.inArray("moisture",systemSelection));
		
		$(".tool").children("a[href='correspondence.html']").parent().css("display","block");
		$(".tool").children("a[href='irrigation-schedule.html']").parent().css("display","block");
		$(".tool").children("a[href='irrigation-system.html']").parent().css("display","block");
		
		if($.inArray("Time-based with rain sensor",systemSelection)!=-1){
			$(".tool").children("a[href='rain-sensor-detail.html']").parent().css("display","block");
			$("input[value='Time-based with rain sensor']").attr("checked","checked");
			
		}
		if($.inArray("Time-based with soil moisture sensor",systemSelection)!=-1){
			
			$("input[value='Time-based with soil moisture sensor']").attr("checked","checked")
			$(".tool").children("a[href='soil-moisture-settings.html']").parent().css("display","block");
		}
		if($.inArray("Time-based",systemSelection)!=-1){
			
			$("input[value='Time-based']").attr("checked","checked");
			
		}
		if($.inArray("Evapotranspiration Controller",systemSelection)!=-1){
			$("input[value='Evapotranspiration Controller']").attr("checked","checked");
			
		}
		
		if($.inArray("Time-based with soil moisture sensor",systemSelection)==-1){
			//alert();
			var $li=$("a[href='soil-moisture-settings.html']");
			$li.css("display","none");
			$("button[name='next']").click(function(){
				$.cookie("rainsettings",$("select[name='rainsettings']").val(),{ expires : 7 });
				location.href="/irrigation-system.html";
				
			});
			
		}else{
			
			$("button[name='next']").click(function(){
				$.cookie("rainsettings",$("select[name='rainsettings']").val(),{ expires : 7 });
				location.href="/soil-moisture-settings.html";
				
			});
			
		}
		if($.inArray("Time-based with rain sensor",systemSelection)==-1){
			//alert();
			var $li=$("a[href='rain-sensor-detail.html']");
			$li.css("display","none");
			
			
		}
		for(var i=0;i<systemSelection.length;i++){
			
			var str=systemSelection[i];
			var $p=$("<p>The Result for "+str+" is: </p><p>Loss:value  &nbsp;  &nbsp;PerLoss:value&nbsp;  &nbsp;Water Volumes Applied and Not Used:value &nbsp;&nbsp; Water Stress Days:value &nbsp;&nbsp; Percent of Water Applied That Was Not Used:value &nbsp;&nbsp;</p><br/>");
			$("#result").append($p);
			
		}
		
		
	}
	
	$("a[name='next']").click(function(){
		$.cookie("soilthreshold",$("select[name='soilthreshold']").val(),{ expires: 7 });
		location.href="/irrigation-system.html";
		
	});
	$("#back").click(function(){
		
		history.back();
		
		
	});
	
	$("#patternRadio1").click(function(){
		
		$("#irrigation-system").hide();
		$(this).attr("checked","checked");
		$("#irrigationDepth").show();
		if($.cookie("irriDepth")){
			
			$("#irrigationDepth").val($.cookie("irriDepth"));
			
		}
		
		
	});
	$("#patternRadio2").click(function(){
		
		$("#irrigationDepth").hide();
		$(this).attr("checked","checked");
		$("#irrigation-system").show();
		if($.cookie("isystem")){
			$("#irrigationDuration").val($.cookie("irriDuration"));
			$("input[value='"+$.cookie("isystem")+"']").attr("checked","checked");
			
		}
		
	});
	$("#step4").click(function(){
		
		
		if("#patternRadio1:checked"){
			var irriDepth=$("#irrigationDepth").val();
			$.cookie("irriDepth",irriDepth,{ expires : 7 });
			
			$.cookie("isystem","",{expires: -1});
			$.cookie("minutes","",{expires:-1});
			
		}  
		
		if("#patternRadio2:checked"){
			var isystem=$("input[name='irrigation-system']:checked").val();
			//alert(isystem);
			var irriDuration=$("#irrigationDuration").val();
			//alert(minutes);
			$.cookie("isystem",isystem,{ expires : 7});
			$.cookie("irriDuration",irriDuration,{expires : 7});
			$.cookie("irriDepth","",{ expires: -1});
		}
		
		location.href="/irrigation-schedule.html";
		
	});
	$("input[name='schedule']").each(function(){
		
		$(this).click(function(){
			
			if($(this).prop("checked")){
				
				$(this).parent().next().css("display","block");
				
			}else{
				
				$(this).parent().next().css("display","none");
				
			}
			
			//alert($(this).val());
			//alert($(this).prop("checked"));
		
		});
		
		
	});
	$("#step5").click(function(){
		var days=[];
		var hours = [];
		var minutes = [];
		$("input[name='schedule']:checked").each(function(){
			
			var day=$(this).val();
			var startHour=$(this).parent().next().children("select[name='startHour']").val();
			var startMinute=$(this).parent().next().children("select[name='startMinute']").val();
			days.push(day);
			hours.push(startHour);
			minutes.push(startMinute);
		});
		//alert(days);
		//alert(hours);
		//alert(minutes);
		$.cookie("days",days.toString(),{ expires: 7});
		$.cookie("hours",hours.toString(),{ expires: 7});
		$.cookie("minutes",minutes.toString(),{expires: 7});
		location.href="/correspondence.html";
	});
	$("#step6").click(function(){
		
		var choice = $("input[name='correspondence']:checked").val();
		//alert(choice);
		$.cookie("choice",choice,{ expires : 7});
		
		location.href="/thank-you.html";
		
	});
	
	
	
	
	
	
	
});
