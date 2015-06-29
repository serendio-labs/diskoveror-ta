(function($lcl){$lcl.ezoicDoctor=function(element,options){element.data('ezoicDoctor',this);this.options={};this.drWidgets=[];this.loadedWidgets=[];this.widgetRunCount=0;this.line='-----------------------------------------------------------------------------';this.space='          ';this.init=function(options){this.options=$lcl.extend({},$lcl.ezoicDoctor.defaultOptions,options);if(typeof ezpaq!="undefined"&&typeof ezpaq.is_ezoic=='function')
{this.options.widgetBaseURL='http://preview.ezoic.com/utilcave_com/dr/';}
if(typeof ez_dr_widget_base_url!="undefined")
{this.options.widgetBaseURL=ez_dr_widget_base_url;}
this.log('Initializing the plugin & setting the options');this.log(this.options);this.log(this.space);this.drWidgets=this.options.widgets;this.runWidgets();};this.runWidgets=function(){this.widgetRunCount++;var that=this;if(this.widgetRunCount<10){setTimeout(function(){that.runWidgets()},100);}
else if(this.widgetRunCount<20){setTimeout(function(){that.runWidgets()},100);}
else if(this.widgetRunCount<30){setTimeout(function(){that.runWidgets()},250);}
else if(this.widgetRunCount<50){setTimeout(function(){that.runWidgets()},2000);}
this.log(this.line);this.log(this.line);this.log(this.widgetRunCount+'. Running Widgets!');this.log(this.space);this.log('Running common checks (will add items to the dr widgets list to be loaded)');this.runCommonChecks();this.log(this.space);this.log('Making sure the dr widgets array is unique');this.drWidgets=unique(this.drWidgets);this.log(this.space);this.loadDrWidgets();};this.runCommonChecks=function(){if(this.jquery_function_exists('superfish')){this.drWidgets.push('superfish');this.log('Common Check: adding superfish widget');}
if(typeof FIX=='object'&&typeof FIX.doEndOfBody=='function')
{this.drWidgets.push('sitesell');this.log('Common Check: adding sitesell widget');}
if(ezoFormfactor!='1'&&($ezJQuery('#sthoverButtons')||$ezJQuery('#sthoverbuttons')))
{this.drWidgets.push('sharethis');this.log('Common Check: adding sharethis widget');}
if($ezJQuery('.addthis-smartlayers'))
{this.drWidgets.push('addthis');this.log('Common Check: adding addthis widget');}
if($ezJQuery("#___gcse_1.ezoic-wrapper").length>=1){$ezJQuery("#___gcse_1.ezoic-wrapper").removeClass('ezoic-wrapper');this.log('Common Check: REMOVING ezoic-wrapper from gsce_1');}
if($ezJQuery("#___gcse_0.ezoic-wrapper").length>=1){$ezJQuery("#___gcse_0.ezoic-wrapper").removeClass('ezoic-wrapper');this.log('Common Check: REMOVING ezoic-wrapper from gsce_0');}
if(typeof Rokmoomenu==='function'||typeof RokZoom==='object')
{this.drWidgets.push('rokzoom');this.log('Common Check: adding rokzoom widget');}
if(this.jquery_function_exists('quickFlip')){this.drWidgets.push('quickflip');this.log('Common Check: adding quickflip widget');}
if(this.jquery_function_exists('carousel')){this.drWidgets.push('bootstrap_carousel');this.log('Common Check: adding carousel widget');}
if($ezJQuery("a[href*='mgid.com']"))
{this.drWidgets.push('mgid');this.log('Common Check: adding mgid.com widget');}
if($ezJQuery('script[src*="feeds.feedburner.com"]'))
{this.drWidgets.push('feedburner');this.log('Common Check: feedburner');}
if(this.jquery_function_exists('cycle')&&$ezJQuery('.smooth_slider')){this.drWidgets.push('smooth_slider');this.log('Common Check: adding smooth_slider fixer');}
if($ezJQuery('.af-form-wrapper').length>0){this.drWidgets.push('aweber_email');this.log('Common Check: adding aweber email widget');}
if($ezJQuery(".mceLayout div[id='Comment_toolbargroup']").length>0){this.drWidgets.push('tinymce_editor');this.log('Common Check: adding tinymce editor widget');}
if($ezJQuery("ul.extravote-stars:not([data-ez-proc])").length>0){this.drWidgets.push('extravote_widget');this.log('Common Check: adding extravote star widget');}
if(this.jquery_function_exists('DrLazyload')&&$ezJQuery("img[data-size]").length>0){this.drWidgets.push('lazyload_images');this.log('Common Check: resize lazy loaded images');}
if($ezJQuery("iframe[src*='allposters.com']").length>0){this.drWidgets.push('allposters');this.log('Common Check: allposters');}
if($ezJQuery('[data-ez-req-px]').length>0){this.drWidgets.push('td_height');this.log('Common Check: td_height');}
if($ezJQuery(".testimonial_slider").length>0){this.drWidgets.push('testimonial');this.log('Common Check: adding testimonial fix');}};this.loadDrWidgets=function(){if(this.drWidgets.length>0){for(var i=0;i<this.drWidgets.length;i++){var widget=this.drWidgets[i];this.log(widget);this.runSingleCheck(widget);}}};this.loadWidget=function(widget){if(widget!==undefined&&$lcl.inArray(widget,this.loadedWidgets)<0){$widgetURL=this.options.widgetBaseURL+widget+'.js';if($widgetURL.indexOf("?")>0){$widgetURL=$widgetURL+'&cb='+this.options.cb;}
else{$widgetURL=$widgetURL+'?cb='+this.options.cb;}
this.log("Load Widget '"+widget+"': "+$widgetURL);$myText='<scr'+'ipt language="javascript" type="text/javascript" src="'+$widgetURL+'"></scr'+'ipt>';$lcl('body').append($myText);this.loadedWidgets.push(widget);}
else{this.log("ERROR - Load Widget '"+widget+"': The widget has already been loaded or does not exist");}};this.runSingleCheck=function(widget){this.log("Running Widget: '"+widget+"'");if(widget!==undefined){if($lcl.inArray(widget,this.loadedWidgets)<0){this.log("Widget has not been loaded...loading widget now");this.loadWidget(widget);}
else{try{this.log("Widget has already been loaded...calling the widget's function now (drwidget_"+widget+")");window["drwidget_"+widget]();}
catch(err){this.log('ERROR: '+err.message);}}}
this.log(this.space);};this.log=function(message){if(this.options.debugOutput==1){console.log(message);}};this.jquery_function_exists=function(func_name)
{if(($&&$.fn&&$.fn[func_name])||(jQuery&&jQuery.fn&&jQuery.fn[func_name]))
{return true;}
else
{return false;}}
this.init(options);};function unique(arr){if(arr.length>0){arr=$lcl.unique(arr);}
return arr;};$lcl.ezoicDoctor.defaultOptions={widgets:[],debugOutput:'0',widgetBaseURL:'/utilcave_com/dr/'};$lcl.fn.ezoicDoctor=function(options){return this.each(function(){(new $lcl.ezoicDoctor($lcl(this),options));});};}($ezJQuery));