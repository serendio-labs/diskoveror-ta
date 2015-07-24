// JavaScript Document
function CreateBookmarkLink(title, url) {
    if (window.sidebar) {
        window.sidebar.addPanel(title, url,"");
    } else if( window.external ) {
        window.external.AddFavorite( url, title); }
    else if(window.opera && window.print) {
        return true; }
 }
 
	function showErrorMessage(formId,msg){	
	
		var errorData,i,errVal;
		errorData=msg.split('#');
		$("#"+formId+" > input").each(function(){
			var id= this.id;		
			
			$("#"+id).removeClass("error");
		});
		
		$("#"+formId+" > div").each(function(){this.style.display="none"});
		for(i=2; i<errorData.length; i++){			
			errVal=errorData[i].split('@@');			
			$("#"+errVal[0]).addClass("error");
			$("#error_"+errVal[0]).css("display","block");			
			$("#error_"+errVal[0]).html("<span class=\"label\">&nbsp;</span>"+errVal[1]);
		}
		
	} 
	
	function showErrorMessage1(formId,msg){
	var errorData,i,errVal;
	errorData=msg.split('#');
	
	$("#"+formId+" :input").each(function(){
		var id= this.id;			
		if(id)
		{
			$("#"+id).removeClass("error");
			$("#li_"+id).removeClass("bg_error");
		}
	});
	
	$("#"+formId+" div").each(function(){									   
		if(this.id.indexOf("error_") != "-1")
		{			
			this.style.display="none";
		}
	});
	first_msg = 0;
	
	var counter		= 0;
	var first_error	= "";
	
	for(i=1; i<errorData.length; i++){			
		errVal = errorData[i].split('@@');			
		if(errVal[0]	!=	"")
		{
			$("#"+errVal[0]).addClass("error");
			$("#li_"+errVal[0]).addClass("bg_error");
			$("#error_"+errVal[0]).css("display","block");	
			$("#error_"+errVal[0]).html("<p>"+errVal[1]+"</p>");
			if(first_error == "")
			{				
				counter 	= 1;
				first_error	= errVal[0];
				if(first_error	==	'common')
				{
					first_error	=	'error_'+first_error;
				}
					 
			}
		}
	}
} 

function showErrorMessage2(formId,msg){
	var errorData,i,errVal;
	errorData=msg.split('#');
	
	$("#"+formId+" :input").each(function(){
		var id= this.id;			
		if(id)
		{
			$("#"+id).removeClass("error");
			$("#li_"+id).removeClass("bg_error");
		}
	});
	
	$("#"+formId+" div").each(function(){									   
		if(this.id.indexOf("error_") != "-1")
		{			
			this.style.display="none";
		}
	});
	first_msg = 0;
	
	var counter		= 0;
	var first_error	= "";
	
	for(i=1; i<errorData.length; i++){			
		errVal = errorData[i].split('@@');			
		if(errVal[0]	!=	"")
		{
			if(errVal[2]=="span")
			{
				$("#"+errVal[0]).addClass("error");
				$("#li_"+errVal[0]).addClass("bg_error");
				$("#error_"+errVal[0]).css("display","block");			
				$("#error_"+errVal[0]).html("<span class=\"label-02\">&nbsp;</span>"+errVal[1]);
				if(first_error == "")
				{				
					counter 	= 1;
					first_error	= errVal[0];
					if(first_error	==	'common')
					{
						first_error	=	'error_'+first_error;
					}
						 
				}
			}
			else if(errVal[2]!="span")
			{
				$("#"+errVal[0]).addClass("error");
				$("#li_"+errVal[0]).addClass("bg_error");
				$("#error_"+errVal[0]).css("display","block");			
				$("#error_"+errVal[0]).html("<span class=\"label\">&nbsp;</span>"+errVal[1]);
				if(first_error == "")
				{				
					counter 	= 1;
					first_error	= errVal[0];
					if(first_error	==	'common')
					{
						first_error	=	'error_'+first_error;
					}
						 
				}
			}
		}
	}
} 
	
	
	function showErrorMessageJson(formId,msg){				
		var errorData,i,errVal;
		errorData=msg.split('#');				
		$("#"+formId+" > input").each(function(){
			var id= this.id;			
			$("#"+id).removeClass("error");
		});		
		$("#"+formId+" > div").each(function(){this.style.display="none"});
		for(i=0; i<(parseInt(errorData.length)-1); i++){			
			
			errVal=errorData[i].split('@@');			
			$("#"+errVal[0]).addClass("error");
			$("#error_"+errVal[0]).css("display","block");
			$("#error_"+errVal[0]).html("<span class=\"label\">&nbsp;</span>"+errVal[1]);
		}		
	}
	
	function form_loading(errId){
		$("#"+errId).css("background","#fff");
		$("#"+errId).html("<center><img src=\"images/loading.gif\" /></center>");
		$("#"+errId).css("display","block");		
	}
	
	function form_loading_transparent(errId){
		$("#"+errId).css("background","none");
		$("#"+errId).html("<center><img src=\"images/loading.gif\" /></center>");
		$("#"+errId).css("display","block");		
	}

	function do_subscription(category){							   		
		data	=	 $("#wotd_sub_bness_"+category).serialize();
		tb_open_new('subscribe.php?'+data+'TB_iframe=true&height=330&width=420&modal=true');		
	}
	
	function subscribe_news(category){							   		
		data	=	 $("#wotd_sub_bness_"+category).serialize();
		tb_open_new('word-subscribe.php?'+data+'TB_iframe=true&height=330&width=420&modal=true');			
	}
	
	function do_mul_subscription()
	{
		data	=	 $("#subscribe_box").serialize();
		tb_open_new('subscribe-newsletter-action.php?'+data+'TB_iframe=true&height=330&width=420&modal=true');
	}

	function do_mul_unSubscription_2()
	{
		data	=	 $("#subscribe_box").serialize();
		tb_open_new('unsubscribe-action.php?'+data+'TB_iframe=true&height=330&width=420&modal=true');
	}


	function do_mul_unSubscription()
	{
		data	=	 $("#un_subscribe_box").serialize();
		tb_open_new('unsubscribe-action.php?'+data+'TB_iframe=true&height=330&width=420&modal=true');
	}

/* thick Box related functions*/
var jThickboxNewLink;
function tb_remove_open(reloadLink){
	jThickboxReloadLink	=	reloadLink;
	tb_remove();
	setTimeout("jThickboxNewLink();",500);
	return false;
}

function tb_open_new(jThickboxNewLink){
	tb_show(null,jThickboxNewLink,null);
}

function tb_remove(parent_func_callback) {
	$("#TB_imageOff").unbind("click");
	$("#TB_closeWindowButton").unbind("click");
	$("#TB_window").fadeOut("fast",function(){$('#TB_window,#TB_overlay,#TB_HideSelect').trigger("unload").unbind().remove();});
	$("#TB_load").remove();
	if (typeof document.body.style.maxHeight == "undefined") {//if IE 6
		$("body","html").css({height: "auto", width: "auto"});
		$("html").css("overflow","");
	}	
	if(parent_func_callback != undefined)
	eval("window."+parent_func_callback);
	document.onkeydown = "";
	document.onkeyup = "";
	return false;
}

function check_file_type(file)
{
	if(file)
		{
			gtePos= file.lastIndexOf(".");
			str1 = file.substring((gtePos + 1),file.length);
			  if(str1 == 'jpg' || str1 == 'png' || str1 == 'gif' || str1 == 'jpeg')
				return true;
			else
				return false;	
		}
	else
		{
			return true;
		}
}