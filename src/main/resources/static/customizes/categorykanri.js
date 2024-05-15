$(document).ready(function() {
	$("#toCategory").css('color', '#005300');
	let treeData = [
		{
			text: "分類管理",
			icon: "fa fa-list",
			expanded: true,
			nodes: [
				{
					id: "districtQueryTree",
					text: "地域一覧",
					icon: "fa fa-globe"
				},
				{
					id: "cityQueryTree",
					text: "都市一覧",
					icon: "fa fa-building"
				}
			]
		}
	];
	$('#categroyTreeView').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 2,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
	$("#districtQueryTree").on('click', function() {
		let url = '/pgcrowd/category/toDistricts';
		checkPermissionAndTransfer(url);
	});
	$("#cityQueryTree").on('click', function() {
		let url = '/pgcrowd/category/toCities';
		checkPermissionAndTransfer(url);
	});
});