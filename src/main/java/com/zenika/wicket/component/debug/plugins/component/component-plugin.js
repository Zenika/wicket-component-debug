(function($) {
	var WicketComponentDebug = {
	
		debugWindowId : "wicketComponentDebugWindow",
	
		debugWindowDragHandleId : "wicketComponentDebugWindowDragHandle",
	
		debugWindowResizeHandleId : "wicketComponentDebugWindowResizeHandle",
	
		debugWindowLogId : "wicketComponentDebugWindowLogId",
	
		showDebugWindow : function() {
			WicketComponentDebug.init();
			wicketShow(WicketComponentDebug.debugWindowId);
		},
	
		hideDebugWindow : function() {
			WicketComponentDebug.init();
			wicketHide(WicketComponentDebug.debugWindowId);
		},
	
		clear : function() {
			$(".debug-black-overlay").css("display", "");
			$(".debug-span").css("color", "black");
			$('.jquery-tree-checked').each(function() {
				$(this).removeClass("jquery-tree-checked").addClass("jquery-tree-unchecked");
				var checkbox = $('input:checkbox', this);
				var checked = checkbox.attr('checked');
				checkbox.attr('checked', !checked);
			});
			WicketComponentDebug.borderize();
		},
	
		init : function() {
			var firstTime = document.getElementById(WicketComponentDebug.debugWindowId) == null;
			if (firstTime) {
				/**
				 * Add the popup to the DOM
				 */
				WicketComponentDebug.addLinkAndPopupElements();
	
				/**
				 * Initialize Popup Drag and Rezise
				 */
				WicketComponentDebug.initDradAndResize();
	
				/**
				 * Transorm the ul list in jQueryTree
				 */
				WicketComponentDebug.buildTree();
	
				/**
				 * Initialize borders on all the wrappers
				 */
				WicketComponentDebug.borderize();
	
				/**
				 * Add black-overlay div to the page end
				 */
				$("body").append($(document.createElement("div")).addClass('debug-black-overlay'));
			}
		},
	
		initDradAndResize : function() {
			Wicket.Drag.init(wicketGet(WicketComponentDebug.debugWindowDragHandleId), function() {
			}, function() {
			}, WicketComponentDebug.onDrag);
			Wicket.Drag.init(wicketGet(WicketComponentDebug.debugWindowResizeHandleId), function() {
			}, function() {
			}, WicketComponentDebug.onResize);
		},
	
		addLinkAndPopupElements : function() {
			var html = "";
			html += "<div style='width: 450px; display: none; left: 200px; top: 300px;' id='" + WicketComponentDebug.debugWindowId + "'>";
			html += "		<div style='border: 1px solid black; padding: 1px; background-color: #eee'>";
			html += "			<div style='overflow: auto; width: 100%'>";
			html += "				<div style='float: right; padding: 0.2em; padding-right: 1em; color: black;'>";
			html += "					<a href='javascript:WicketComponentDebug.clear()' style='color:blue'>clear</a>";
			html += "					<a href='javascript:WicketComponentDebug.hideDebugWindow()' style='color:blue'>close</a>";
			html += "				</div>";
			html += "			<div id='" + WicketComponentDebug.debugWindowDragHandleId + "'>" + "Wicket Component Debug Window" + "</div>";
			html += "			<div id='" + WicketComponentDebug.debugWindowLogId + "' style='height: 200px;'>";
			html += "			</div>";
			html += "     		<div style='height: 10px; margin:0px; padding:0px;overflow:hidden;'>";
			html += "        		<div style='height: 10px; width: 10px;'  id='" + WicketComponentDebug.debugWindowResizeHandleId + "'>";
			html += "           	</div>";
			html += "       	</div>";
			html += "		</div>";
			html += "	</div>";
			html += "</div>";
			if (Wicket.Browser.isIE() && (Wicket.Browser.isIEQuirks() || !Wicket.Browser.isIE7())) {
				html += "<a id='wicketComponentDebugLink' style='position:absolute; right: 10px; bottom: 45px; z-index:1000000; padding-top: 0.3em; padding-bottom: 0.3em; line-height: normal ; _padding-top: 0em; width: 12em; border: 1px solid black; background-color: white; text-align: center; opacity: 0.7; filter: alpha(opacity=70); color: blue; "
				html += " left: expression(-10 - wicketComponentDebugLink.offsetWidth + eval(document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft) +(document.documentElement.clientWidth ? document.documentElement.clientWidth : document.body.clientWidth));"
				html += " top: expression(-10 - wicketComponentDebugLink.offsetHeight + eval(document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + (document.documentElement.clientHeight ? document.documentElement.clientHeight : document.body.clientHeight));'";
			} else {
				html += "<a id='wicketComponentDebugLink' style='position:fixed; right: 10px; bottom: 45px; z-index:1000000; padding-top: 0.3em; padding-bottom: 0.3em; line-height: normal ; _padding-top: 0em; width: 12em; border: 1px solid black; background-color: white; text-align: center; opacity: 0.7;  color: blue;'";
			}
			html += "  href='javascript:WicketComponentDebug.showDebugWindow()'>COMPONENT DEBUG</a>";
			WicketComponentDebug.addElement(html);
		},
	
		buildTree : function(arg) {
	
			WicketComponentDebug.clear();
			
			var ajax = (arg == null) ? false : true;
	
			/**
			 * Insert or Update the tree div
			 */
			var ulTree = (ajax) ? arg : tree;
			$("#" + WicketComponentDebug.debugWindowLogId).html(ulTree);
	
			/**
			 * Wrap debug components
			 */
			WicketComponentDebug.wrapDebugComponents(ajax);
	
			/**
			 * Transform 'ul' tree to the jQuery Tree
			 */
			$("#wicket-component-debug-tree").Tree();
	
			/**
			 * Attach checkbox change behavior
			 */
			$(".jquery-tree-title input").each(function() {
				$(this).change(function() {
					if (Wicket.Browser.isIE()) {
						setTimeout(function() {
							WicketComponentDebug.borderize()
						}, 50);
					} else {
						WicketComponentDebug.borderize();
					}
				})
			});
	
			/**
			 * Attach the tree leaf onmouseover behavior. <br>
			 * Shows the components informations on the 'span' text mouseover
			 */
			$('.debug-span').each(function() {
				WicketComponentDebug.attachToolTip(this);
			});
	
		},
	
		/**
		 * Wrap all the components to debug
		 * if ajax, check if the component is not already wrapped
		 */
		wrapDebugComponents : function(ajax) {
			var active = document.activeElement;
				$(".component-debug").each(function() {
					if(!ajax || (ajax && !$(this).parent().first().hasClass("wicket-component-debug-wrapper"))){
						var wrapper = $("<div>").attr("id", $(this).attr("debugid") + "-wrapper");
						wrapper.addClass("wicket-component-debug-wrapper");
						wrapper.css("display", $(this).css("display"));
						$(this).wrap(wrapper);
					}
				});
			$(active).focus();
		},
	
		attachToolTip : function(spanObj) {
	
			var informations = $(spanObj).attr("tooltip").split(";");
	
			for ( var info in informations) {
				informations[info] = informations[info].split(",");
			}
	
			var content = WicketComponentDebug.buildToolTip(informations);
	
			$(spanObj).qtip({
				content : { prerender : false, text : content },
				position : { corner : { tooltip : "leftMiddle", target : "rightMiddle" } },
				show : { delay : 0, solo : true, when : {
						target : false,
						event : 'mouseover'
					}, effect : {
						length : 0
					}
				},
				hide : { delay : 0, event : { when : "mouseout"} , fixed : true },
				style : { width : '100%', border : { width : 1, radius : 5 }, 
					padding : 0,
					textAlign : 'left',
					tip : true,
					name : 'dark'
				}
			});
		},
	
		buildToolTip : function(informations) {
	
			var colors = dpc['component.plugin.border.colors'].split(",");
			var useClipboard = (dpc["component.plugin.clipboard.copy"] == "true");
			var emptyCell = (useClipboard) ? ("<td>") : "";
			var wrapper = $("#" + informations[0][1] + "-wrapper");
	
			var tooltip = $("<table>").addClass("debug-tooltip");
	
			// Informations Rows
			for ( var int = 0; int < informations.length; int++) {
				tooltip.append($("<tr>").append(function() {
					if (useClipboard){
						return $("<td>").html(WicketComponentDebug.buildFlashCopier(informations[int][1]));
					}
				}).append($("<td>").addClass("bold").html(informations[int][0]), $("<td>").html(": " + informations[int][1])));
			}
	
			// Color Row
			var colorCell = $("<td>");
			for ( var key in colors) {
				colorCell.append($("<div>").addClass("picker").attr("color", colors[key]).css("background-color", colors[key]).click(function() {
					WicketComponentDebug.changeBorderColor(wrapper, $(this).attr('color'));
				}));
			}
			tooltip.append($("<tr>").append(emptyCell).append($("<td>").addClass("bold").html("color"), colorCell));
	
			// Highlight Row
			tooltip.append($("<tr>").append(emptyCell).append($("<td>").addClass("bold").html("highlight"), $("<td>").append(function() {
				var input = $("<input>").addClass("highlight").attr("type", "checkbox").click(function() {
					WicketComponentDebug.highlight(wrapper);
				});
				if (wrapper.hasClass("debug-highlight")) {
					return input.attr("checked", "checked");
				} else {
					return input;
				}
			})));
	
			return tooltip;
		},
	
		buildFlashCopier : function(value) {
			var flash = "";
			flash += "<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000'>";
			flash += "<param name='movie' value='" + dpc['swf.url'] + "'/>";
			flash += "<param name='FlashVars' value='text=" + value + "'/>"
			flash += "<param name='allowScriptAccess' value='always' />"
			flash += "<param name='quality' value='high' />"
			flash += "<param name='scale' value='noscale' />"
			flash += "<param name='bgcolor' value='#505050'/>"
			flash += "<param name='wmode' value='opaque'/>"
			flash += "<embed src='" + dpc['swf.url'] + "'"
			flash += " FlashVars='text=" + value + "'"
			flash += " width='14'"
			flash += " height='14'"
			flash += " name='clippy'"
			flash += " quality='high'"
			flash += " allowScriptAccess='always'"
			flash += " type='application/x-shockwave-flash'"
			flash += " pluginspage='http://www.macromedia.com/go/getflashplayer'"
			flash += " bgcolor='#505050' wmode='opaque' scale='noscale'/>"
			flash += "</object>";
			return flash;
		},
	
		/**
		 * Add border to checked wrapper
		 */
		borderize : function() {
			var wrapper = {};
			$('.jquery-tree-checked').each(function() {
				wrapper = $("#" + $(this).attr("id").replace("-label", ""));
				if (wrapper.length > 0 && wrapper.get()[0].style.borderWidth != "1px") {
					wrapper.css("border", "1px solid black");
				}
			});
			$('.jquery-tree-unchecked').each(function() {
				wrapper = $("#" + $(this).attr("id").replace("-label", ""));
				wrapper.css("border", "0px solid black");
			});
		},
	
		changeBorderColor : function(wrapper, color) {
			if (wrapper.length > 0 && wrapper.get()[0].style.borderWidth == "1px") {
				wrapper.css("border", "1px solid " + color);
			}
		},
	
		highlight : function(wrapper) {
	
			if (wrapper.length == 0) {
				return;
			}
	
			// Manage class
			if (wrapper.hasClass("debug-highlight")) {
				wrapper.removeClass("debug-highlight");
				$("#" + wrapper.attr("id") + "-label > span").css("color", "black");
			} else {
				wrapper.addClass("debug-highlight");
				$("#" + wrapper.attr("id") + "-label > span").css("color", "#993333");
			}
	
			// Manage black overlay
			if ($(".debug-highlight").length > 0) {
				$(".debug-black-overlay").css("display", "block");
			} else {
				$(".debug-black-overlay").css("display", "");
			}
		},
	
		onResize : function(element, deltaX, deltaY) {
			var window = wicketGet(WicketComponentDebug.debugWindowId);
			var log = wicketGet(WicketComponentDebug.debugWindowLogId);
	
			var width = parseInt(window.style.width, 10) + deltaX;
			var height = parseInt(log.style.height, 10) + deltaY;
	
			var res = [ 0, 0 ];
	
			if (width < 300) {
				res[0] = 300 - width;
				width = 300;
			}
	
			if (height < 100) {
				res[1] = 100 - height;
				height = 100;
			}
	
			window.style.width = width + "px";
			log.style.height = height + "px";
	
			return res;
		},
	
		onDrag : function(element, deltaX, deltaY) {
			var w = wicketGet(WicketComponentDebug.debugWindowId);
	
			var x = parseInt(w.style.left, 10) + deltaX;
			var y = parseInt(w.style.top, 10) + deltaY;
	
			var res = [ 0, 0 ];
	
			if (x < 0) {
				res[0] = -deltaX;
				x = 0;
			}
			if (y < 0) {
				res[1] = -deltaY;
				y = 0;
			}
	
			w.style.left = x + "px";
			w.style.top = y + "px";
	
			return res;
		},
	
		addElement : function(html) {
			var element = document.createElement("div");
			element.innerHTML = html;
			document.body.appendChild(element);
		},
	
		addEvent : function(obj, evType, fn) {
			if (obj.addEventListener) {
				obj.addEventListener(evType, fn, false);
				return true;
			} else if (obj.attachEvent) {
				var r = obj.attachEvent("on" + evType, fn);
				return r;
			} else {
				return false;
			}
		}
	
	}
	
	WicketComponentDebug.addEvent(window, "load", WicketComponentDebug.init);
	window.WicketComponentDebug = WicketComponentDebug;
})(jQueryComponentDebug);
