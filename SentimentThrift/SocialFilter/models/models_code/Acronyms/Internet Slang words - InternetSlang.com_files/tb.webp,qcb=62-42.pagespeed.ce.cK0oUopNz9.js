var isCtrl=false;document.onkeyup=function(e)
{if(e.which==17)isCtrl=false;if(e.which==18)isAlt=false;}
document.onkeydown=function(e)
{if(e.which==17)isCtrl=true;if(e.which==18)isAlt=true;if(e.which==69&&isCtrl==true&&isAlt==true)
{if($ezJQuery('#ezoic-bar-overlay').length)
{$ezJQuery('#ezoic-bar-overlay').slideDown('slow').remove();}
else
{init_ez_bar();}}}
function ez_force_recache()
{var url=document.URL;var form=document.createElement("form");form.setAttribute("id","force_recache");form.setAttribute("method","POST");form.setAttribute("action",url);var hiddenField=document.createElement("input");hiddenField.setAttribute("type","hidden");hiddenField.setAttribute("name","ez_force_recache");hiddenField.setAttribute("value","force_recache");form.appendChild(hiddenField);document.body.appendChild(form);form.submit();}
function init_ez_bar(){var ezOverlay=$ezJQuery('#ezoic-overlay');if($ezJQuery(ezOverlay.length)){$ezJQuery(ezOverlay).remove();}
$ezJQuery('head').append("<link href='//fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>");if(document.cookie.indexOf("ezoic_show_disable_ads")>=0){var disable_link="<li><a href=\"#\" class=\"js-ez-enable-ad-positions\">Hide \"Disable this ad position\" placeholders.</a></li>";}else{var disable_link="<li><a href=\"#\" class=\"js-ez-disable-ad-positions\">Disable Ad Positions</a></li>";}
var overlay=$ezJQuery('<div id="ezoic-bar-overlay">'+'<div id="ezoic-bar-container">'+'<div id="ezoic-bar-logo"><a href="http://ezoic.com" target="_ezoic"><img src="http://www.ezoic.com/assets/img/ezoic.png" alt="Ezoic"></a></div>'+'<ul id="ezoic-bar-actions">'+'<li><a href="#" class="js-ez-force-recache">Clear Cache</a></li>'+disable_link+'</ul>'+'<a href="#" id="ezoic-bar-close">Close this window</a>'+'</div>'+'</div>');$ezJQuery(overlay).appendTo('body');overlay.css({'fontFamily':'Open Sans','position':'fixed','bottom':'0px','left':'0px','height':'50px','width':'100%','z-index':'1000000','backgroundColor':'rgba(255, 255, 255, 0.9)','border-top':'1px solid #eeeeee','box-shadow':'0px 2px 2px -2px rgba(0, 0, 0, 0.25)','text-shadow':'0 -1px 0 rgba(0, 0, 0, 0.25)'});$ezJQuery('#ezoic-bar-overlay a').css({'color':'#444444','fontFamily':'Open Sans','text-shadow':'0 -1px 0 rgba(0, 0, 0, 0.25)'});$ezJQuery('#ezoic-bar-container').css({'max-width':'none','width':'100%'});$ezJQuery('ul#ezoic-bar-actions').css({'float':'left','height':'50px','line-height':'50px','list-style':'none','margin':'0px','max-width':'none','padding':'0px'});$ezJQuery('#ezoic-bar-actions li').css({'display':'inline','font-size':'14px','padding-left':'0px'});$ezJQuery('#ezoic-bar-actions li a').css({'color':'#444444','font-size':'14px','height':'50px','line-height':'50px','padding-left':'15px','padding-right':'15px','text-align':'center','text-decoration':'none'}).hover(function(){$ezJQuery(this).css('color','#888888');},function(){$ezJQuery(this).css('color','#444444');});$ezJQuery('#ezoic-bar-logo').css({'color':'#444444','float':'left','font-weight':'700','padding-left':'20px','padding-right':'20px','padding-top':'12px'});$ezJQuery('#ezoic-bar-logo img').css({'max-height':'26px'});$ezJQuery('#ezoic-bar-close').on('click',function(e){e.preventDefault();$ezJQuery('#ezoic-bar-overlay').remove();}).css({'float':'right','line-height':'50px','padding':'0 20px','color':'#444444'});$ezJQuery('.js-ez-force-recache').on('click',function(e){e.preventDefault();var self=$ezJQuery(this);var text=self.text();self.text('Clearing cache...');ez_force_recache();});$ezJQuery('.js-ez-disable-ad-positions').on('click',function(e){e.preventDefault();var self=$ezJQuery(this);var text=self.text();self.text('Authorizing...');window.location.replace("http://"+window.location.host+"/utilcave_com/auth.php?enable_ad_placeholders=1&rurl="+encodeURIComponent(window.location.href)+"&did="+did);});$ezJQuery('.js-ez-enable-ad-positions').on('click',function(e){e.preventDefault();var self=$ezJQuery(this);var text=self.text();self.text('Refreshing...');window.location.replace("http://"+window.location.host+"/utilcave_com/auth.php?disable_ad_placeholders=1&rurl="+encodeURIComponent(window.location.href));});$ezJQuery('.js-ez-hide-template').on('click',function(e){e.preventDefault();var self=$ezJQuery(this);var text=self.text();self.text('Saving preference...');$ezJQuery.ajax({url:'/ezoic_ajax/hide_template.php',data:{ezdomain:ezdomain},success:function(data){self.text(text);ez_force_recache();}});});}
if(typeof execute_ez_queue=="function")
{if(typeof $ezJQuery!='undefined')
{$ezJQuery(window).load(function(){execute_ez_queue()});}
else
{window.onload=execute_ez_queue;}}
if(typeof $ezJQuery!='undefined')
{var is_ez_awesome=false;var ez_awesome_params=new Array();var inf_load;var old_inf_count=0;var ez_inf_timer=0;var ez_awesome_done=false;$ezJQuery("div[data-role='page']").show();$ezJQuery(document).bind("mobileinit",function(){set_mobile_vars();});function load_il_track()
{ez_inf_timer++;var new_inf_count=$ezJQuery("span[ class *= IL_AD ],.IL_BASE").length;if(new_inf_count>5&&old_inf_count<6)
{$ezJQuery("span[ id *= IL_AD ]").mouseover(function()
{clearInterval(inf_load);ez_inf_timer=0;inf_load=setInterval(load_il_track,500);});clearInterval(inf_load);}
if(old_inf_count<1)
{old_inf_count=new_inf_count;}
if(new_inf_count>(old_inf_count+10)||ez_inf_timer>20)
{old_inf_count=new_inf_count;clearInterval(inf_load);}
$ezJQuery("span[ class *= IL_AD ],.IL_BASE").click(function(){set_ez_awesome_params('over',4,this);ez_awesome_click()});}
function ez_awesome_click(type,sourceid)
{document.cookie="ezoawesome="+ez_awesome_params[2]+"; path=/;";if(is_ez_awesome&&ez_awesome_done!=true)
{is_ez_awesome=false;ez_awesome_done=true;$ezJQuery.ajax({type:"post",url:"/utilcave_com/awesome.php",data:{url:window.location.href,width:ez_awesome_params[0],height:ez_awesome_params[1],did:did,sourceid:ez_awesome_params[2],uid:ezouid,template:ezoTemplate}})}}
function set_ez_awesome_params(type,sourceid,item)
{if(type=="over")
{if(item.width!=undefined)
{ez_awesome_params[0]=item.width;ez_awesome_params[1]=item.height;}
ez_awesome_params[2]=sourceid;is_ez_awesome=true;}
else
{ez_awesome_params=new Array();is_ez_awesome=false;}}
$ezJQuery(window).load(function()
{$ezJQuery("iframe[ id *= ox_frame_ ]").mouseover(function(){set_ez_awesome_params('over',33,this);}).mouseout(function(){set_ez_awesome_params('out',33,this);});$ezJQuery("iframe[ id *= google ],iframe[ id *= aswift ],ins[id *= aswift]").mouseover(function(){set_ez_awesome_params('over',5,this);}).mouseout(function(){set_ez_awesome_params('out',5,this);});$ezJQuery("iframe[ id *= google ],iframe[ id *= aswift ]").contents().find("script[ src *= 'casalemedia.com']").parent().mouseover(function(){set_ez_awesome_params('over',9,this);}).mouseout(function(){set_ez_awesome_params('out',9,this);});$ezJQuery("iframe[ id *= google ],iframe[ id *= aswift ]").contents().find("script[ src *= 'fastclick']").parent().mouseover(function(){set_ez_awesome_params('over',9,this);}).mouseout(function(){set_ez_awesome_params('out',9,this);});$ezJQuery("iframe[ id *= google ],iframe[ id *= aswift ]").contents().find("script[ src *= 'ad.afy']").parent().mouseover(function(){set_ez_awesome_params('over',6,this);}).mouseout(function(){set_ez_awesome_params('out',6,this);});inf_load=setInterval(load_il_track,500);$ezJQuery(window).blur(function(){ez_awesome_click();}).focus();});var ezo_resize_interval;var ezo_resize_interval_count=0;var ezo_resize_interval_count_limit=10;$ezJQuery(function(){ezo_resize_start_timer();});function ezo_resize_start_timer()
{ezo_resize_interval=setInterval(ezo_resize_checker,200);}
function ezo_resize_checker(e)
{ezo_resize_interval_count++;for(index=0;index<ezo_elements_to_check.length;++index)
{var $elm=$ezJQuery(ezo_elements_to_check[index]);if(parseInt($elm.width())>parseInt($elm.parent().width()))
{ezo_resize(ezo_elements_to_check[index],$elm.parent().width()/$elm.width());}}
if(ezo_resize_interval_count>ezo_resize_interval_count_limit)
{clearInterval(ezo_resize_interval);}}
function ezo_resize($elm,percent)
{if(typeof console=="object")
{}
var new_width=parseInt($ezJQuery($elm).css('width'))*percent;$ezJQuery($elm).css('width',new_width+'px');$ezJQuery($elm).children().each(function(i,j)
{ezo_resize($ezJQuery(this),percent);});}}