$(document).ready(function(){
	
	var systemSelection=$.cookie("systemSelection").split(",");
	//alert(systemSelection);
	
	//alert($.inArray("moisture",systemSelection));
	
	if($.inArray("moisture",systemSelection)==-1){
		//alert();
		var $li=$("a[href='soil-moisture-settings.html']");
		$li.css("display","none");
		
		
	}
	if($.inArray("rainsensor",systemSelection)==-1){
		alert();
		var $li=$("a[href='rain-sensor-detail.html']");
		$li.css("display","none");
		
		
	}
});
