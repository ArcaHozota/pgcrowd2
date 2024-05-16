let pageNum, totalRecords, totalPages, keyword;
$(document).ready(function() {
	$("#toCity").css('color', '#005300');
	toSelectedPg(1, keyword);
	$("#toCity").addClass('animate__animated animate__flipInY');
});
$("#searchBtn2").on('click', function() {
	keyword = $("#keywordInput").val();
	toSelectedPg(1, keyword);
});
function toSelectedPg(pageNum, keyword) {
	$.ajax({
		url: '/pgcrowd/city/pagination',
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
		let pronunciationTd = $("<td class='text-center' style='width:100px;vertical-align:middle;'></td>").append(item.pronunciation);
		let districtTd = $("<td class='text-center' style='width:70px;vertical-align:middle;'></td>").append(item.districtName);
		let populationTd = $("<td class='text-center' style='width:50px;vertical-align:middle;'></td>").append(patternedPop);
		let flagImg = $("<img>").attr('src', '/pgcrowd/svgImages/getCityFlags?flags=' + item.cityFlag + '.svg').attr('alt', '').height(27).width(40);
		let flagTd = $("<td class='text-center city-flag-td' role='button' style='width:50px;vertical-align:middle;'></td>").append(flagImg);
		let editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit-btn").attr('data-bs-toggle', 'modal')
			.attr('data-bs-target', '#cityEditModal').append($("<i class='fa fa-pencil'></i>")).append("編集");
		editBtn.attr("editId", item.id);
		let deleteBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete-btn")
			.append($("<i class='fa fa-trash'></i>")).append("削除");
		deleteBtn.attr("deleteId", item.id);
		let btnTd = $("<td class='text-center' style='width:100px;vertical-align:middle;'></td>").append(editBtn).append(" ").append(deleteBtn);
		$("<tr></tr>").append(idTd).append(nameTd).append(pronunciationTd).append(districtTd).append(populationTd).append(flagTd).append(btnTd).appendTo("#tableBody");
	});
}
$("#addCityBtn").on('click', function() {
	getDistricts("#districtInput", null);
	normalAddbtnFunction('/pgcrowd/city/checkEdition', "#cityAddModal");
});
$("#nameInput").on('change', function() {
	checkCityName(this, "#districtInput");
});
$("#populationInput").on('change', function() {
	let populationVal = Number(this.value.replace(/,/g, ''));
	let regularPopulation = /^\d{3,12}$/;
	if (!regularPopulation.test(populationVal)) {
		showValidationMsg(this, "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "");
	}
});
$("#districtInput").on('change', function() {
	checkCityName("#nameInput", this);
	let districtFlg = $("#districtInput option:selected").attr('flagval').substring(8);
	$("#cityFlagInput").val(districtFlg + '/flag-of-');
});
$("#cityInfoSaveBtn").on('click', function() {
	let inputArrays = ["#nameInput", "#poInput", "#populationInput", "#cityFlagInput"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#cityAddModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正');
	} else {
		let postData = JSON.stringify({
			'name': $("#nameInput").val().trim(),
			'pronunciation': $("#poInput").val().trim(),
			'districtId': $("#districtInput").val(),
			'population': Number($("#populationInput").val().trim().replace(/,/g, '')),
			'cityFlag': $("#cityFlagInput").val().trim()
		});
		pgcrowdAjaxModify('/pgcrowd/city/infoSave', 'POST', postData, normalPostSuccessFunction("#cityAddModal"));
	}
});
$("#tableBody").on('click', '.edit-btn', function() {
	let ajaxResult = $.ajax({
		url: '/pgcrowd/city/checkEdition',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
	formReset("#cityEditModal form");
	let editId = $(this).attr("editId");
	$("#cityInfoChangeBtn").val(editId);
	let nameVal = $(this).parent().parent().find("td:eq(0)").text();
	let poVal = $(this).parent().parent().find("td:eq(1)").text();
	let populationVal = $(this).parent().parent().find("td:eq(3)").text();
	$("#nameEdit").val(nameVal);
	$("#poEdit").val(poVal);
	$("#populationEdit").val(populationVal);
	getDistricts("#districtEdit", editId);
	$("#cityEditModal").modal({
		backdrop: 'static'
	});
});
$("#nameEdit").on('change', function() {
	checkCityName(this, "#districtEdit");
});
$("#populationEdit").on('change', function() {
	let populationVal = Number(this.value.replace(/,/g, ''));
	let regularPopulation = /^\d{3,12}$/;
	if (!regularPopulation.test(populationVal)) {
		showValidationMsg(this, "error", "入力した人口数量が3桁から12桁までの数字にしなければなりません。");
	} else {
		showValidationMsg(this, "success", "");
	}
});
$("#districtEdit").on('change', function() {
	checkCityName("#nameEdit", this);
});
$("#cityInfoChangeBtn").on('click', function() {
	let inputArrays = ["#nameEdit", "#poEdit", "#populationEdit"];
	let listArray = pgcrowdInputContextGet(inputArrays);
	if (listArray.includes("")) {
		pgcrowdNullInputboxDiscern(inputArrays);
	} else if ($("#cityEditModal form").find('*').hasClass('is-invalid')) {
		layer.msg('入力情報不正');
	} else {
		let putData = JSON.stringify({
			'id': this.value,
			'name': $("#nameEdit").val().trim(),
			'pronunciation': $("#poEdit").val().trim(),
			'districtId': $("#districtEdit").val(),
			'population': Number($("#populationEdit").val().trim().replace(/,/g, ''))
		});
		pgcrowdAjaxModify('/pgcrowd/city/infoUpdate', 'PUT', putData, cityPutSuccessFunction);
	}
});
$("#tableBody").on('click', '.delete-btn', function() {
	let cityId = $(this).attr("deleteId");
	let cityName = $(this).parents("tr").find("td:eq(0)").text().trim();
	normalDeletebtnFunction('/pgcrowd/city/', 'この「' + cityName + '」という都市の情報を削除する、よろしいでしょうか。', cityId);
});
$("#tableBody").on('click', '.city-flag-td', function() {
	let nameVal = $(this).parent().find("td:eq(0)").text();
	window.open('https://ja.wikipedia.org/wiki/' + nameVal);
});
function checkCityName(cityName, district) {
	let nameVal = $(cityName).val().trim();
	let districtVal = $(district).val();
	if (nameVal === "") {
		showValidationMsg(cityName, "error", "名称を空になってはいけません。");
	} else {
		$.ajax({
			url: '/pgcrowd/city/check',
			data: {
				'name': nameVal,
				'districtId': districtVal
			},
			type: 'GET',
			success: function(result) {
				if (result.status === 'SUCCESS') {
					showValidationMsg(cityName, "success", "√");
				} else {
					showValidationMsg(cityName, "error", result.message);
				}
			}
		});
	}
}
function getDistricts(element, cityId) {
	$(element).empty();
	$.ajax({
		url: '/pgcrowd/city/getDistrictList',
		data: 'cityId=' + cityId,
		type: 'GET',
		success: function(result) {
			$.each(result.data, (index, item) => {
				let optionElement = $("<option></option>").attr('value', item.id)
					.attr('flagval', item.districtFlag).text(item.name + '----------------' + item.chiho);
				optionElement.appendTo(element);
			});
		}
	});
}
function cityPutSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		$("#cityEditModal").modal('hide');
		layer.msg('更新済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}