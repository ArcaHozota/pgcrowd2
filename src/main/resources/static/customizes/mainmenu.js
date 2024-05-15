$(document).ready(function() {
	$("#toMainmenu").css('color', '#005300');
	$("#adminKanriMainmenu").find('img').attr('src', '/pgcrowd/svgImages/getIcons?icons=castilia.svg');
	$("#roleKanriMainmenu").find('img').attr('src', '/pgcrowd/svgImages/getIcons?icons=burgundy.svg');
	$("#categoryKanriMainmenu").find('img').attr('src', '/pgcrowd/svgImages/getIcons?icons=bourbon.svg');
});
$("#categoryKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/category/initial';
	checkPermissionAndTransfer(url);
});
$("#roleKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/role/toPages?pageNum=1';
	checkPermissionAndTransfer(url);
});
$("#adminKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/employee/toPages?pageNum=1';
	checkPermissionAndTransfer(url);
});