$(document).ready(function() {
	let treeData = [
		{
			id: "toMainmenu",
			text: "メインメニュー",
			icon: "fa fa-compass",
		},
		{
			text: "権限管理",
			icon: "fa fa-university",
			expanded: true,
			nodes: [
				{
					id: "toAdmin",
					text: "社員管理",
					icon: "fa fa-users"
				},
				{
					id: "toRole",
					text: "役割管理",
					icon: "fa fa-user-circle-o"
				},
				{
					id: "toMenu",
					text: "メニュー管理",
					icon: "fa fa-bars"
				}
			]
		},
		{
			text: "ビジネス管理",
			icon: "fa fa-steam-square",
			expanded: true,
			nodes: [
				{
					id: "toCertification",
					text: "資格維持",
					icon: "fa fa-check-circle-o"
				},
				{
					id: "toCategory",
					text: "分類管理",
					icon: "fa fa-list",
					expanded: true,
					nodes: [
						{
							id: "toDistrict",
							text: "地域一覧",
							icon: "fa fa-globe"
						},
						{
							id: "toCity",
							text: "都市一覧",
							icon: "fa fa-building"
						}
					]
				}
			]
		}
	];
	$('#mainmenuTree').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 1.5,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
	$("#logoutBtn").on('click', function() {
		swal.fire({
			title: '警告',
			text: 'ログアウトしてよろしいでしょうか。',
			icon: 'warning',
			showDenyButton: true,
			denyButtonText: 'いいえ',
			confirmButtonText: 'はい',
			confirmButtonColor: '#7F0020',
			denyButtonColor: '#002FA7'
		}).then((result) => {
			if (result.isConfirmed) {
				$("#logoutForm").submit();
			} else if (result.isDenied) {
				$(this).close();
			}
		});
	});
	$("#logoutLink").on('click', function(e) {
		e.preventDefault();
		$("#logoutForm").submit();
	});
	$("#toMainmenu").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/category/toMainmenu');
	});
	$("#toMainmenu2").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/category/toMainmenu');
	});
	$("#toPersonal").on('click', function(e) {
		e.preventDefault();
		let userId = $(this).find("input").val().replace(/,/g, '');
		let authChkFlag = $("#authChkFlgContainer").val();
		let url = '/pgcrowd/employee/toEdition?editId=' + userId + '&authChkFlag=' + authChkFlag;
		checkPermissionAndTransfer(url);
	});
	$("#toAdmin").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/employee/toPages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#toRole").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/role/toPages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#toMenu").on('click', function(e) {
		e.preventDefault();
		window.location.replace('/pgcrowd/category/menuInitial');
	});
	$("#toPages").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/employee/toPages?pageNum=' + pageNum;
		checkPermissionAndTransfer(url);
	});
	$("#toCategory").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/initial';
		checkPermissionAndTransfer(url);
	});
	$("#toDistrict").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/toDistricts';
		checkPermissionAndTransfer(url);
	});
	$("#toCity").on('click', function(e) {
		e.preventDefault();
		let url = '/pgcrowd/category/toCities';
		checkPermissionAndTransfer(url);
	});
});
function checkPermissionAndTransfer(stringUrl) {
	let ajaxResult = $.ajax({
		url: stringUrl,
		type: 'GET',
		async: false
	});
	if (ajaxResult.status === 200) {
		window.location.replace(stringUrl);
	} else {
		layer.msg(ajaxResult.responseJSON.message);
	}
}
function buildPageInfos(result) {
	let pageInfos = $("#pageInfos");
	pageInfos.empty();
	pageNum = result.data.pageNum;
	totalPages = result.data.totalPages;
	totalRecords = result.data.totalRecords;
	pageInfos.append(totalPages + "ページ中の" + pageNum + "ページ、" + totalRecords + "件のレコードが見つかりました。");
}
function buildPageNavi(result) {
	$("#pageNavi").empty();
	let ul = $("<ul></ul>").addClass("pagination");
	let firstPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("最初へ").attr("href", "#"));
	let previousPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("&laquo;").attr("href", "#"));
	if (!result.data.hasPreviousPage) {
		firstPageLi.addClass("disabled");
		previousPageLi.addClass("disabled");
	} else {
		firstPageLi.click(function() {
			toSelectedPg(1, keyword);
		});
		previousPageLi.click(function() {
			toSelectedPg(pageNum - 1, keyword);
		});
	}
	let nextPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("&raquo;").attr("href", "#"));
	let lastPageLi = $("<li class='page-item'></li>").append(
		$("<a class='page-link'></a>").append("最後へ").attr("href", "#"));
	if (!result.data.hasNextPage) {
		nextPageLi.addClass("disabled");
		lastPageLi.addClass("disabled");
	} else {
		lastPageLi.addClass("success");
		nextPageLi.click(function() {
			toSelectedPg(pageNum + 1, keyword);
		});
		lastPageLi.click(function() {
			toSelectedPg(totalPages, keyword);
		});
	}
	ul.append(firstPageLi).append(previousPageLi);
	$.each(result.data.navigatePageNums, (index, item) => {
		let numsLi = $("<li class='page-item'></li>").append(
			$("<a class='page-link'></a>").append(item).attr("href", "#"));
		if (pageNum === item) {
			numsLi.attr("href", "#").addClass("active");
		}
		numsLi.click(function() {
			toSelectedPg(item, keyword);
		});
		ul.append(numsLi);
	});
	ul.append(nextPageLi).append(lastPageLi);
	$("<nav></nav>").append(ul).appendTo("#pageNavi");
}
function formReset(element) {
	$(element)[0].reset();
	$(element).find(".form-control").removeClass("is-valid is-invalid");
	$(element).find(".custom-select").removeClass("is-valid is-invalid");
	$(element).find(".form-text").removeClass("valid-feedback invalid-feedback");
	$(element).find(".form-text").text("");
}
function showValidationMsg(element, status, msg) {
	$(element).removeClass("is-valid is-invalid");
	$(element).next("span").removeClass("valid-feedback invalid-feedback");
	$(element).next("span").text("");
	if (status === "success") {
		$(element).addClass("is-valid");
		$(element).next("span").addClass("valid-feedback");
	} else {
		$(element).addClass("is-invalid");
		$(element).next("span").addClass("invalid-feedback").text(msg);
	}
}
function pgcrowdAjaxModify(url, type, data, successFunction) {
	let header = $('meta[name=_csrf_header]').attr('content');
	let token = $('meta[name=_csrf_token]').attr('content');
	$.ajax({
		url: url,
		type: type,
		data: data,
		headers: {
			[header]: token
		},
		dataType: 'json',
		contentType: 'application/json;charset=UTF-8',
		success: successFunction,
		error: function(result) {
			layer.msg(result.responseJSON.message);
		}
	});
}
function pgcrowdNullInputboxDiscern(inputArrays) {
	for (const element of inputArrays) {
		if ($(element).val().trim() === "") {
			showValidationMsg(element, "error", "上記の入力ボックスを空になってはいけません。");
		}
	}
}
function pgcrowdInputContextGet(inputArrays) {
	let listArray = [];
	for (const element of inputArrays) {
		let inputContext = $(element).val().trim();
		if (!$(element).hasClass('is-invalid')) {
			listArray.push(inputContext);
			showValidationMsg(element, "success", "");
		}
	}
	return listArray;
}
function normalPostSuccessFunction(element) {
	$(element).modal('hide');
	layer.msg('追加処理成功');
	toSelectedPg(totalRecords, keyword);
}
function normalDeleteSuccessFunction(result) {
	if (result.status === 'SUCCESS') {
		layer.msg('削除済み');
		toSelectedPg(pageNum, keyword);
	} else {
		layer.msg(result.message);
	}
}
function normalAddbtnFunction(checkUrl, modalName) {
	let ajaxResult = $.ajax({
		url: checkUrl,
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
	let modalForm = $(modalName).find('form');
	formReset(modalForm);
	$(modalName).modal({
		backdrop: 'static'
	});
}
function normalDeletebtnFunction(url, message, deleteId) {
	let ajaxResult = $.ajax({
		url: url + 'checkDelete',
		type: 'GET',
		async: false
	});
	if (ajaxResult.status !== 200) {
		layer.msg(ajaxResult.responseJSON.message);
		return;
	}
	swal.fire({
		title: 'メッセージ',
		text: message,
		icon: 'question',
		showCloseButton: true,
		confirmButtonText: 'はい',
		confirmButtonColor: '#7F0020'
	}).then((result) => {
		if (result.isConfirmed) {
			pgcrowdAjaxModify(url + 'infoDelete?id=' + deleteId, 'DELETE', null, normalDeleteSuccessFunction);
		} else {
			$(this).close();
		}
	});
}