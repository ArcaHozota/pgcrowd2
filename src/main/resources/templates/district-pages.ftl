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
							<i class="fa fa-bars"></i> 地域情報メンテナンス
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
						</div>
						<table class="table table-sm table-hover">
							<caption style="font-size: 10px;">地域情報一覧</caption>
							<thead class="thead-dark">
								<tr>
									<th scope="col" class="text-center" style="width: 150px;">ID</th>
									<th scope="col" class="text-center" style="width: 70px;">名称</th>
									<th scope="col" class="text-center" style="width: 70px;">県庁</th>
									<th scope="col" class="text-center" style="width: 70px;">地域</th>
									<th scope="col" class="text-center" style="width: 50px;">人口数量</th>
									<th scope="col" class="text-center" style="width: 50px;">道府県旗</th>
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
	<div class="modal fade" id="districtEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h4 class="modal-title">地域情報変更</h4>
	            </div>
	            <div class="modal-body">
	                <form class="form-horizontal">
	                    <div class="form-group row">
	                        <label for="nameEdit" class="col-sm-3 col-form-label text-right">地域名称</label>
	                        <div class="col-sm-9" style="height: 5.7vh;">
	                            <input type="text" class="form-control" id="nameEdit" placeholder="地域の名称"> 
	                            <span class="text-muted" style="font-size: 10px;"></span>
	                        </div>
	                    </div>
	                    <div class="form-group row">
	                        <label for="shutoEdit" class="col-sm-3 col-form-label text-right">州都</label>
	                        <div class="col-sm-9" style="height: 5.7vh;">
	                            <select class="custom-select" id="shutoEdit"></select>
	                        </div>
	                    </div>
	                    <div class="form-group row">
	                        <label for="chihoEdit" class="col-sm-3 col-form-label text-right">地方</label>
	                        <div class="col-sm-9" style="height: 5.7vh;">
	                            <select class="custom-select" id="chihoEdit"></select>
	                        </div>
	                    </div>
	                    <div class="form-group row">
	                        <label for="populationEdit" class="col-sm-3 col-form-label text-right">人口数量</label>
	                        <div class="col-sm-9" style="height: 5.7vh;">
	                            <p type="text" class="form-control" id="populationEdit"></p>
	                        </div>
	                    </div>
	                </form>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">
	                    <i class="fa fa-times"></i> 閉じる
	                </button>
	                <button type="button" class="btn btn-success" id="districtInfoChangeBtn">
	                    <i class="fa fa-leaf"></i> 更新
	                </button>
	            </div>
	        </div>
	    </div>
	</div>
	<input type="hidden" value="/static/customizes/district-pages.js" id="jsContainer">
</body>
</html>