<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<div class="card border-tunagray mb-3">
					<div class="card-header text-bg-tunagray mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa fa-bars"></i> 社員情報メンテナンス
						</h5>
					</div>
					<div class="card-body">
						<div class="row">
							<form class="form-inline col-md-5" role="form">
								<div class="input-group mb-3" style="width: 100%;">
									<input id="keywordInput" class="form-control" type="text"
										placeholder="検索条件を入力してください" aria-label="検索条件入力" aria-describedby="searchBtn2">
									<div class="input-group-append">
										<button id="searchBtn2" class="btn btn-secondary my-2 my-sm-0" type="button">
											<i class="fa fa-search"></i> 検索
										</button>
									</div>
								</div>
							</form>
							<div class="col-md-2 offset-md-5">
								<button class="btn btn-success my-2 my-sm-0" id="addInfoBtn">
									<i class="fa fa-id-card"></i> 社員情報追加
								</button>
							</div>
						</div>
						<#if pageNum?exists!''>
							<input type="hidden" value="${pageNum}" id="pageNumContainer">
						</#if>
						<table class="table table-sm table-hover">
							<caption style="font-size: 10px;">社員情報一覧</caption>
							<thead class="thead-dark">
								<tr>
									<th scope="col" class="text-center" style="width: 150px;">ID</th>
									<th scope="col" class="text-center" style="width: 70px;">名称</th>
									<th scope="col" class="text-center" style="width: 100px;">メール</th>
									<th scope="col" class="text-center" style="width: 70px;">生年月日</th>
									<th scope="col" class="text-center" style="width: 100px;">操作</th>
								</tr>
							</thead>
							<tbody id="tableBody" class="table-group-divider"></tbody>
						</table>
						<div class="row">
							<div id="pageInfos" class="col-md-5" style="font-size: 12px;"></div>
							<div id="pageNavi" class="col-md-7 d-flex justify-content-end"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/employee-pages.js" id="jsContainer">
</body>
</html>