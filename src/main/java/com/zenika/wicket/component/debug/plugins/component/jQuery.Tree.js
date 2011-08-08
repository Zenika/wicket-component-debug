(function($) {

	var CLASS_JQUERY_TREE = 'jquery-tree';
	var CLASS_JQUERY_TREE_CONTROLS = 'jquery-tree-controls';
	var CLASS_JQUERY_TREE_COLLAPSE_ALL = 'jquery-tree-collapseall';
	var CLASS_JQUERY_TREE_EXPAND_ALL = 'jquery-tree-expandall';
	var CLASS_JQUERY_TREE_COLLAPSED = 'jquery-tree-collapsed';
	var CLASS_JQUERY_TREE_HANDLE = 'jquery-tree-handle';
	var CLASS_JQUERY_TREE_TITLE = 'jquery-tree-title';
	var CLASS_JQUERY_TREE_NODE = 'jquery-tree-node';
	var CLASS_JQUERY_TREE_LEAF = 'jquery-tree-leaf';
	var CLASS_JQUERY_TREE_CHECKED = 'jquery-tree-checked';
	var CLASS_JQUERY_TREE_UNCHECKED = 'jquery-tree-unchecked';
	var CLASS_JQUERY_TREE_CHECKED_PARTIAL = 'jquery-tree-checked-partial';

	var COLLAPSE_ALL_CODE = '<span class="' + CLASS_JQUERY_TREE_COLLAPSE_ALL + '"></span>';
	var EXPAND_ALL_CODE = '<span class="' + CLASS_JQUERY_TREE_EXPAND_ALL + '"></span>';
	var TREE_CONTROLS_CODE = '<div class="' + CLASS_JQUERY_TREE_CONTROLS + '">' + COLLAPSE_ALL_CODE + EXPAND_ALL_CODE + '</div>';

	var TREE_NODE_HANDLE_CODE = '<span class="' + CLASS_JQUERY_TREE_HANDLE + '">+</span>';
	var TREE_NODE_HANDLE_COLLAPSED = "+";
	var TREE_NODE_HANDLE_EXPANDED = "&minus;";

	$.fn.extend({

		Tree : function() {
			$(this).addClass(CLASS_JQUERY_TREE).before(TREE_CONTROLS_CODE).prev('.' + CLASS_JQUERY_TREE_CONTROLS).find('.' + CLASS_JQUERY_TREE_COLLAPSE_ALL)
					.click(
							function() {
								$(this).parent().next('.' + CLASS_JQUERY_TREE).find('li:has(ul)').addClass(CLASS_JQUERY_TREE_COLLAPSED).find(
										'.' + CLASS_JQUERY_TREE_HANDLE).html(TREE_NODE_HANDLE_COLLAPSED);
							})

					.parent('.' + CLASS_JQUERY_TREE_CONTROLS).find('.' + CLASS_JQUERY_TREE_EXPAND_ALL).click(
							function() {
								$(this).parent().next('.' + CLASS_JQUERY_TREE).find('li:has(ul)').removeClass(CLASS_JQUERY_TREE_COLLAPSED).find(
										'.' + CLASS_JQUERY_TREE_HANDLE).html(TREE_NODE_HANDLE_EXPANDED);
							});

			$('input:checkbox', this).click(function() {
				setLabelClass(this);
				checkCheckbox(this);
			}).each(function() {
				setLabelClass(this);
				if (this.checked)
					checkParentCheckboxes(this);
			}).closest('label').click(function() {
				labelClick(this);
				checkCheckbox($('input:checkbox', this));
			});

			$('li', this).find(':first').addClass(CLASS_JQUERY_TREE_TITLE).closest('li').addClass(CLASS_JQUERY_TREE_LEAF);

			$('li:has(ul:has(li))', this).find(':first').before(TREE_NODE_HANDLE_CODE).closest('li').addClass(CLASS_JQUERY_TREE_NODE).addClass(
					CLASS_JQUERY_TREE_COLLAPSED).removeClass(CLASS_JQUERY_TREE_LEAF);

			$('.' + CLASS_JQUERY_TREE_HANDLE, this).bind('click', function() {
				var leafContainer = $(this).parent('li');
				var leafHandle = leafContainer.find('>.' + CLASS_JQUERY_TREE_HANDLE);

				leafContainer.toggleClass(CLASS_JQUERY_TREE_COLLAPSED);

				if (leafContainer.hasClass(CLASS_JQUERY_TREE_COLLAPSED))
					leafHandle.html(TREE_NODE_HANDLE_COLLAPSED);
				else
					leafHandle.html(TREE_NODE_HANDLE_EXPANDED);
			});

		}

	});

	function checkParentCheckboxes(checkboxElement) {
		if (typeof checkboxElement == 'undefined' || !checkboxElement)
			return;

		var closestNode = $(checkboxElement).closest('ul');
		var allCheckboxes = closestNode.find('input:checkbox');
		var checkedCheckboxes = closestNode.find('input:checkbox:checked');

		// Fix by Nagi
		var allChecked = false;
		// var allChecked = allCheckboxes.length == checkedCheckboxes.length;

		var parentCheckbox = closestNode.closest('li').find('>.' + CLASS_JQUERY_TREE_TITLE + ' input:checkbox');

		if (parentCheckbox.length > 0) {
			parentCheckbox.get(0).checked = allChecked;

			if (!allChecked && checkedCheckboxes.length > 0)
				parentCheckbox.closest('label').addClass(CLASS_JQUERY_TREE_CHECKED_PARTIAL).removeClass(CLASS_JQUERY_TREE_CHECKED).removeClass(
						CLASS_JQUERY_TREE_UNCHECKED);
			else if (allChecked)
				parentCheckbox.closest('label').removeClass(CLASS_JQUERY_TREE_CHECKED_PARTIAL).removeClass(CLASS_JQUERY_TREE_UNCHECKED).addClass(
						CLASS_JQUERY_TREE_CHECKED);
			else
				parentCheckbox.closest('label').removeClass(CLASS_JQUERY_TREE_CHECKED_PARTIAL).removeClass(CLASS_JQUERY_TREE_CHECKED).addClass(
						CLASS_JQUERY_TREE_UNCHECKED);

			checkParentCheckboxes(parentCheckbox.get(0));
		}
	}

	function checkCheckbox(checkboxElement) {

		// Fix by Nagi to allow checking a node without checking all its
		// children
		/*
		 * $(checkboxElement).closest('li').find('input:checkbox').each(function(){
		 * this.checked = $(checkboxElement).attr('checked');
		 * setLabelClass(this); });
		 */

		// Fix
		// checkParentCheckboxes(checkboxElement);
	}
	;

	function setLabelClass(checkboxElement) {
		isChecked = $(checkboxElement).attr('checked');

		if (isChecked) {
			$(checkboxElement).closest('label').addClass(CLASS_JQUERY_TREE_CHECKED).removeClass(CLASS_JQUERY_TREE_UNCHECKED).removeClass(
					CLASS_JQUERY_TREE_CHECKED_PARTIAL);
		} else {
			$(checkboxElement).closest('label').addClass(CLASS_JQUERY_TREE_UNCHECKED).removeClass(CLASS_JQUERY_TREE_CHECKED).removeClass(
					CLASS_JQUERY_TREE_CHECKED_PARTIAL);
		}
	}
	;

	function labelClick(labelElement) {
		var checkbox = $('input:checkbox', labelElement);
		var checked = checkbox.attr('checked');
		checkbox.attr('checked', !checked);
		setLabelClass(checkbox);
	}

})(jQuery);
