let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#toDistrict").css('color', '#005300');
	toSelectedPg(1, keyword);
	$("#toDistrict").addClass('animate__animated animate__flipInY');
});
$("#searchBtn2").on('click', function() {
	keyword = $("#keywordInput").val();
	toSelectedPg(1, keyword);
});
function toSelectedPg(pageNum, keyword) {
	$.ajax({
		url: '/pgcrowd/district/pagination',
		data: {
			'pageNum': pageNum,
			'keyword': keyword
		},
		type: 'GET',
		success: function(result) {
			buildTableBody(result);
			buildPageInfos(result);
			buildPageNavi(result);
		},
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
}
function buildTableBody(result) {
	$("#tableBody").empty();
	let index = result.data.records;
	$.each(index, (index, item) => {
		let patternedPop = Number(item.population).toLocaleString('en-US');
		let idTd = $("<th scope='row' class='text-center' style='width:150px;vertical-align:middle;'></th>").append(item.id);
		let nameTd = $("<td class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.name);
		let shutoTd = $("<td class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.shutoName);
		let chihoTd = $("<td class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.chiho);
		let populationTd = $("<td class='text-center' style='width:50px;vertical-align:middle;'></td>").append(patternedPop);
		let flagImg = $("<img>").attr('src', '/pgcrowd/svgImages/getFlags?flags=' + item.districtFlag + '.svg').attr('alt', '').height(27).width(40);
		let flagTd = $("<td class='text-center district-flg-td' role='button' style='width:50px;vertical-align:middle;'></td>").append(flagImg);
		let editBtn = $("<button style='width:100px;'></button>").addClass("btn btn-success btn-sm edit-btn")
			.append($("<i class='fa fa-pencil-square-o'></i>")).append(" 編集");
		editBtn.attr("editId", item.id);
		let btnTd = $("<td class='text-center' style='width:100px;vertical-align:middle;'></td>").append(editBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(shutoTd).append(chihoTd).append(populationTd).append(flagTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#tableBody").on('click', '.edit-btn', function() {
	let ajaxResult = $.ajax({
		url: '/pgcrowd/district/checkEdition',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
	formReset("#districtEditModal form");
	let editId = $(this).attr("editId");
	$("#districtInfoChangeBtn").val(editId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	let shutoVal = $(this).parent().parent().find("td:eq(1)").text();
	let chihoVal = $(this).parent().parent().find("td:eq(2)").text();
	let populationVal = $(this).parent().parent().find("td:eq(3)").text();
	$("#nameEdit").val(nameVal);
	getChihos("#chihoEdit", chihoVal);
	getShutos("#shutoEdit", editId, shutoVal);
	$("#populationEdit").text(populationVal);
	$("#districtEditModal").modal({
		backdrop: 'static'
	});
});
$("#districtInfoChangeBtn").on('click', function() {
	let inputArrays = ["#nameEdit", "#chihoEdit"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#districtEditModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正');
	} else {
		let putData = JSON.stringify({
			'id': this.value,
			'name': $("#nameEdit").val().trim(),
			'chihoId': $("#chihoEdit").val().trim(),
			'shutoId': $("#shutoEdit").val().trim()
		});
		pgcrowdAjaxModify('/pgcrowd/district/infoUpdate', 'PUT', putData, districtPutSuccessFunction);
	}
});
$("#tableBody").on('click', '.district-flg-td', function() {
	let nameVal = $(this).parent().find("td:eq(0)").text();
	window.open('https://ja.wikipedia.org/wiki/' + nameVal);
});
function getChihos(element, chihoVal) {
	$(element).empty();
	$.ajax({
		url: '/pgcrowd/district/getChihoList',
		data: 'chihoName=' + chihoVal,
		type: 'GET',
		success: function(result) {
			$.each(result.data, (index, item) => {
				let optionElement = $("<option></option>").attr('value', item.id).text(item.name);
				optionElement.appendTo(element);
			});
		}
	});
}
function getShutos(element, editId, shutoVal) {
	let header = $('meta[name=_csrf_header]').attr('content');
	let token = $('meta[name=_csrf_token]').attr('content');
	$(element).empty();
	$.ajax({
		url: '/pgcrowd/district/getShutoList',
		type: 'POST',
		headers: {
			[header]: token
		},
		data: JSON.stringify({
			'id': editId,
			'shutoName': shutoVal
		}),
		dataType: 'json',
		contentType: 'application/json;charset=UTF-8',
		success: function(result) {
			$.each(result.data, (index, item) => {
				let optionElement = $("<option></option>").attr('value', item.id).text(item.name);
				optionElement.appendTo(element);
			});
		}
	});
}
function districtPutSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		$("#districtEditModal").modal('hide');
		layer.msg('更新済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}