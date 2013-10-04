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
		$.cookie("unit",unit,{ expires: 7});
		location.href="/form-2.html";
		
		}
	});
	
	$("#step2").click(function(){
		
		//alert();
		var rd=$("#rootDepth").val();
		var soil=$("select[name='soil type']").val();
		var area=$("#Aera").val();
		//alert(soil);
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
			
		}
		if($.inArray("Time-based with soil moisture sensor",systemSelection)!=-1){
			
			
			$(".tool").children("a[href='soil-moisture-settings.html']").parent().css("display","block");
		}
 
		
		
		if($.inArray("Time-based with soil moisture sensor",systemSelection)==-1){
			//alert();
			var $li=$("a[href='soil-moisture-settings.html']");
			$li.css("display","none");
			$("button[name='next']").click(function(){
				
				location.href="/irrigation-system.html";
				
			});
			
		}else{
			
			$("button[name='next']").click(function(){
				
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
			var $p=$("<p>The Result for "+str+" is: </p><p>Loss:value  &nbsp;  &nbsp; &nbsp; &nbsp;PerLoss:value</p><br/>");
			var $sibling=$("#result p:first");
			$sibling.before($p);
			
		}
		
		
	}
	
	$("#back").click(function(){
		
		history.back();
		
		
	});
	$("#step4").click(function(){
		
		var irriDepth=$("#irrigationDepth").val();
		var isystem=$("input[name='irrigation-system']:checked").val();
		//alert(isystem);
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
	
		location.href="/correspondence.html";
	});
	$("#step6").click(function(){
		
		location.href="/thank-you.html";
		
	});
	$("a[name='next']").click(function(){
		
		location.href="/irrigation-system.html";
		
	});
	
	
	
	
	
	
});
