//$(document).on("click", ".ui-datatable-tablewrapper .ui-datatable-data tr.ui-widget-content .row-toggler", function() {
function toggleTableRow(table, button) {
	/*1 - for expanded area*/
	var row = $(button).parents("tr");
	if (row.hasClass('ui-expanded-row-content'))
		return;
	/*2 - to collapse open ones*/
	expandedRow = table.jq.find('.ui-expanded-row');
	if (expandedRow.length !== 0 && !row.hasClass('ui-expanded-row')) {
		$('.ui-expanded-row-content').css('display', 'none');
		var untoggler = expandedRow.find('.ui-row-toggler');
		table.toggleExpansion(untoggler);
	}
	/*3 - for main expand feature*/
	var toggler = row.find('.ui-row-toggler');
	table.toggleExpansion(toggler);
};