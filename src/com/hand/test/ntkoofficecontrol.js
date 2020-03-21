 // 请勿修改，否则可能出错
 function ntkoObject() {
 	var arr = [];
 	var userAgent = navigator.userAgent, rMsie = /(msie\s|trident.*rv:)([\w.]+)/, rFirefox = /(firefox)\/([\w.]+)/, rOpera = /(opera).+version\/([\w.]+)/, rChrome = /(chrome)\/([\w.]+)/, rSafari = /version\/([\w.]+).*(safari)/;
 	var browser;
 	var version;
 	var ua = userAgent.toLowerCase();
 	function uaMatch(ua) {
 		var match = rMsie.exec(ua);
 		if (match != null) {
 			return {
 				browser : "IE",
 				version : match[2] || "0"
 			};
 		}
 		var match = rFirefox.exec(ua);
 		if (match != null) {
 			return {
 				browser : match[1] || "",
 				version : match[2] || "0"
 			};
 		}
 		var match = rOpera.exec(ua);
 		if (match != null) {
 			return {
 				browser : match[1] || "",
 				version : match[2] || "0"
 			};
 		}
 		var match = rChrome.exec(ua);
 		if (match != null) {
 			return {
 				browser : match[1] || "",
 				version : match[2] || "0"
 			};
 		}
 		var match = rSafari.exec(ua);
 		if (match != null) {
 			return {
 				browser : match[2] || "",
 				version : match[1] || "0"
 			};
 		}
 		if (match != null) {
 			return {
 				browser : "",
 				version : "0"
 			};
 		}
 	}
 	var browserMatch = uaMatch(userAgent.toLowerCase());
 	if (browserMatch.browser) {
 		browser = browserMatch.browser;
 		version = browserMatch.version;
 	}
 	// arr.push(browser);
 	/*
 	 * 谷歌浏览器事件接管
 	 */
 	save_success_flag = 'N';
 	add_pic_success_flag = 'N';
 	OnAddPicFromURLComplete = function(type, code, html)// 新建事件以便注册接管AddPicFromURL方法，如下类似
 	{
 		// alert(type);
 		// alert(code);
 		// alert(html);
 		// alert("SaveToURL成功回调");
 		add_pic_success_flag = 'Y';
 	}
 	OnComplete2 = function(type, code, html)// 新建事件以便注册接管SaveToURL方法，如下类似
 	{
 		// alert(type);
 		// alert(code);
 		// alert(html);
 		// alert("SaveToURL成功回调");
 		save_success_flag = 'Y';
 	}
 	OnComplete = function(type, code, html) {
 		// alert(type);
 		// alert(code);
 		// alert(html);
 		// alert("BeginOpenFromURL成功回调");
 	}
 	OnComplete3 = function(str, doc)// 新建事件以便注册接管OndocumentOpened事件，如下类似
 	{
 		// alert("ondocumentopened成功回调");
 	}
 	function publishashtml(type, code, html) {// Onpublishashtmltourl事件
 		// alert(html);
 		// /alert("Onpublishashtmltourl成功回调");
 	}
 	function publishaspdf(type, code, html) {// Onpublishaspdftourl事件
 		// alert(html);
 		// //alert("Onpublishaspdftourl成功回调");
 	}
 	function saveasotherurl(type, code, html) {
 		// alert(html);
 		// alert("SaveAsOtherformattourl成功回调");
 	}
 	function dowebget(type, code, html) {
 		alert(html);
 		alert("OnDoWebGet成功回调");
 	}
 	function webExecute(type, code, html) {
 		alert(html);
 		alert("OnDoWebExecute成功回调");
 	}
 	function webExecute2(type, code, html) {
 		alert(html);
 		alert("OnDoWebExecute2成功回调");
 	}
 	function FileCommand(TANGER_OCX_str, TANGER_OCX_obj) {
 		if (TANGER_OCX_str == 3) {
 			alert("不能保存！");
 			TANGER_OCX_OBJ.CancelLastCommand = true;
 		}
 	}
 	function CustomMenuCmd(menuPos, submenuPos, subsubmenuPos, menuCaption,
 			menuID) {
 		alert("第" + menuPos + "," + submenuPos + "," + subsubmenuPos
 				+ "个菜单项,menuID=" + menuID + ",菜单标题为\"" + menuCaption
 				+ "\"的命令被执行.");
 	}
 	var crxpath = location.pathname.match(/^\/[^\/]*/)[0]
 			+ '/office_edit_online/ntkoplugins.crx';
 	var officepath = location.pathname.match(/^\/[^\/]*/)[0]
 	+ '/office_edit_online/ofctnewclsid.cab#version=5,0,2,8';
 	var classid = '01DFB4B4-0E07-4e3f-8B7A-98FD6BFF153F';
 	// alert(officepath);
 	// alert(browser);
 	if (browser == "IE") {
 		arr.push('<!-- 用来产生编辑状态的ActiveX控件的JS脚本-->   ');
 		arr.push('<!-- 因为微软的ActiveX新机制，需要一个外部引入的js-->   ');
 		/*
 		 * 引用控件最需要注意的地方： 所使用控件的Classid、codebase(包含该控件包在服务器上的路径以及控件的版本号
 		 * classid以及版本号均可以控件对应的Cab包里的inf文件中可查到，此处三个属性均不可出错，否则可能会出现控件 加载不正常的情况)
 		 */
 		if(window.navigator.platform=='Win64'){
 			officepath = location.pathname.match(/^\/[^\/]*/)[0]
 			+ '/office_edit_online/OfficeControl.cab#version=5,0,2,8';
 			classid = 'C9BC4DFF-4248-4a3c-8A49-63A7D317F404';
 		}

 		arr.push('<object id="TANGER_OCX" classid="clsid:' + classid + '"');
 		arr.push('codebase=' + officepath
 				+ ' width="1200px" height="400px;">   ');
 		arr.push('<param name="IsUseUTF8URL" value="-1">   ');
 		arr.push('<param name="IsUseUTF8Data" value="-1">   ');
 		arr.push('<param name="BorderStyle" value="1">   ');
 		arr.push('<param name="BorderColor" value="14402205">   ');
 		arr.push('<param name="TitlebarColor" value="15658734">   ');
 		arr.push('<param name="isoptforopenspeed" value="0">   ');

 		/* ProductCaption以及ProductKey属性为正式版控件所有，演示版控件此两个属性不需要写 */
 		arr.push('<param name="ProductCaption" value="中海网络科技股份有限公司"> ');
 		arr.push('<param name="ProductKey" value="417100079C8D3DB37501D86592D028F3D1223391"> ');
 		arr.push('<param name="TitlebarTextColor" value="0">   ');
 		arr.push('<param name="MenubarColor" value="14402205">   ');
 		arr.push('<param name="MenuButtonColor" VALUE="16180947">   ');
 		arr.push('<param name="MenuBarStyle" value="3">   ');
 		arr.push('<param name="MenuButtonStyle" value="7">   ');
 		arr.push('<param name="WebUserName" value="NTKO">   ');
 		arr.push('<param name="Caption" value="文档控件">   ');
 		arr
 				.push('<SPAN STYLE="color:red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>   ');
 		arr.push('</object>');
 	} else if (browser == "firefox") {
 		/*
 		 * 引用控件最需要注意的地方： 所使用控件的Classid、codebase(包含该控件包在服务器上的路径以及控件的版本号
 		 * classid以及版本号均可以控件对应的Cab包里的inf文件中可查到，此处三个属性均不可出错，否则可能会出现控件 加载不正常的情况)
 		 */
 		arr
 				.push('<object id="TANGER_OCX" clsid="{classid}" ForOnSaveToURL="OnComplete2" ForOnBeginOpenFromURL="OnComplete" ForOndocumentopened="OnComplete3" ');
 		/*
 		 * 在非IE环境下，由于代码以及事件等的执行均属于异步，故而无法判断代码是否完成操作，需要对此进行监听
 		 * 需要设置控件对应方法的回调函数以达到如确保文档打开后再对文档对象ActiveDocument进行操作确保代码正确执行
 		 * 并打开代码应该有的效果，如对于SaveToURL方法的执行，需要对应的事件OnSaveToURL，注册该事件的时候则为
 		 * ForOnSaveToURL="OnComplete2"(OnComplete2为我们新建的)
 		 * 此外对于控件已有方法以及属性的回调函数的命名规则为：方法对应的事件：On+方法名，控件已有事件的则无需更改，
 		 * 然需要注意在注册方法或事件对应的回调函数时候则为For+(On+方法名)/事件名
 		 */

 		arr.push('ForOnpublishAshtmltourl="publishashtml" ');
 		arr.push('ForOnpublishAspdftourl="publishaspdf" ');
 		arr.push('ForOnSaveAsOtherFormatToUrl="saveasotherurl" ');
 		arr.push('ForOnDoWebGet="dowebget" ');
 		arr.push('ForOnDoWebExecute="webExecute" ');
 		arr.push('ForOnDoWebExecute2="webExecute2" ');
 		arr.push('ForOnFileCommand="FileCommand" ');
 		arr.push('ForOnCustomMenuCmd2="CustomMenuCmd" ');
 		arr.push('_IsUseUTF8URL="-1"   ');

 		arr
 				.push('codebase='
 						+ officepath
 						+ ' width="1200px" height="500px" type="application/ntko-plug" ');

 		/* ProductCaption以及ProductKey属性为正式版控件所有，演示版控件此两个属性不需要写 */
 		arr.push('_ProductCaption="中海网络科技股份有限公司" ');
 		arr.push('_ProductKey="417100079C8D3DB37501D86592D028F3D1223391" ');
 		arr.push('_IsUseUTF8Data="-1"   ');
 		arr.push('_BorderStyle="1"   ');
 		arr.push('_BorderColor="14402205"   ');
 		arr.push('_MenubarColor="14402205"   ');
 		arr.push('_MenuButtonColor="16180947"   ');
 		arr.push('_MenuBarStyle="3"  ');
 		arr.push('_MenuButtonStyle="7"   ');
 		arr.push('_WebUserName="NTKO"   ');
 		arr.push('clsid="{classid}" >');
 		arr
 				.push('<SPAN STYLE="color:red">尚未安装NTKO Web FireFox跨浏览器插件。请点击<a href="ntkoplugins.xpi">安装组1件</a></SPAN>   ');
 		arr.push('</object>   ');
 	} else if (browser == "chrome") {
 		/*
 		 * 引用控件最需要注意的地方： 所使用控件的Classid、codebase(包含该控件包在服务器上的路径以及控件的版本号
 		 * classid以及版本号均可以控件对应的Cab包里的inf文件中可查到，此处三个属性均不可出错，否则可能会出现控件 加载不正常的情况)
 		 */

 		/*
 		 * 在非IE环境下，由于代码以及事件等的执行均属于异步，故而无法判断代码是否完成操作，需要对此进行监听
 		 * 需要设置控件对应方法的回调函数以达到如确保文档打开后再对文档对象ActiveDocument进行操作确保代码正确执行
 		 * 并打开代码应该有的效果，如对于SaveToURL方法的执行，需要对应的事件OnSaveToURL，注册该事件的时候则为
 		 * ForOnSaveToURL="OnComplete2"(OnComplete2为我们新建的)
 		 * 此外对于控件已有方法以及属性的回调函数的命名规则为：方法对应的事件：On+方法名，控件已有事件的则无需更改，
 		 * 然需要注意在注册方法或事件对应的回调函数时候则为For+(On+方法名)/事件名
 		 */
 		arr
 				.push('<object id="TANGER_OCX" clsid="{classid}" forOnAddPicFromURL="OnAddPicFromURLComplete" ForOnSaveToURL="OnComplete2" ForOnBeginOpenFromURL="OnComplete" ForOndocumentopened="OnComplete3" ');
 		arr.push('ForOnpublishAshtmltourl="publishashtml" ');
 		arr.push('ForOnpublishAspdftourl="publishaspdf" ');
 		arr.push('ForOnSaveAsOtherFormatToUrl="saveasotherurl" ');
 		arr.push('ForOnDoWebGet="dowebget" ');
 		arr.push('ForOnDoWebExecute="webExecute" ');
 		arr.push('ForOnDoWebExecute2="webExecute2" ');
 		arr.push('ForOnFileCommand="FileCommand" ');
 		arr.push('ForOnCustomMenuCmd2="CustomMenuCmd" ');

 		arr.push('_ProductCaption="中海网络科技股份有限公司" ');

 		arr.push('_ProductKey="417100079C8D3DB37501D86592D028F3D1223391" ');

 		arr
 				.push('codebase='
 						+ officepath
 						+ ' width="1200px" height="500px" type="application/ntko-plug" ');
 		arr.push('_IsUseUTF8URL="-1"   ');
 		arr.push('_IsUseUTF8Data="-1"   ');
 		arr.push('_BorderStyle="1"   ');
 		arr.push('_BorderColor="14402205"   ');
 		arr.push('_MenubarColor="14402205"   ');
 		arr.push('_MenuButtonColor="16180947"   ');
 		arr.push('_MenuBarStyle="3"  ');
 		arr.push('_MenuButtonStyle="7"   ');
 		arr.push('_WebUserName="NTKO"   ');
 		arr.push('_Caption="NTKO OFFICE文档控件示例演示 http://www.ntko.com">    ');
 		arr
 				.push('<SPAN STYLE="color:red">尚未安装NTKO Web Chrome跨浏览器插件。请点击<a href='
 						+ crxpath + '>安装组件</a></SPAN>   ');
 		arr.push('</object>');
 	} else if (Sys.opera) {
 		alert("sorry,ntko web印章暂时不支持opera!");
 	} else if (Sys.safari) {
 		alert("sorry,ntko web印章暂时不支持safari!");
 	}

 	new Ext.Template(arr.join('')).insertAfter(Ext.fly('placeholder'), {
 		classid : '{' + classid + '}'
 	});
 }