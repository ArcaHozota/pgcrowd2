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
							<i class="fa fa-bars"></i> 役割情報メンテナンス
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
								<button class="btn btn-warning my-2 my-sm-0" id="addRoleBtn">
									<i class="fa fa-id-badge"></i> 役割情報追加
								</button>
							</div>
						</div>
						<table class="table table-sm table-hover">
							<caption style="font-size: 10px;">役割情報一覧</caption>
							<thead class="thead-dark">
								<tr>
									<th scope="col" class="text-center" style="width: 70px;">ID</th>
									<th scope="col" class="text-center" style="width: 120px;">名称</th>
									<th scope="col" class="text-center" style="width: 120px;">操作</th>
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
	<div class="modal fade" id="roleAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">役割情報追加</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group row">
					        <label for="nameInput" class="col-sm-3 col-form-label text-right">役割名</label>
					        <div class="col-sm-9" style="height: 5.7vh;">
					            <input type="text" class="form-control" id="nameInput" placeholder="役割の名称">
					            <span class="text-muted" style="font-size: 10px;"></span>
					        </div>
					    </div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">
						<i class="fa fa-times"></i> 閉じる
					</button>
					<button type="button" class="btn btn-primary" id="roleInfoSaveBtn">
						<i class="fa fa-inbox"></i> 保存
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="roleEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">役割情報変更</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group row">
					        <label for="nameEdit" class="col-sm-3 col-form-label text-right">役割名</label>
					        <div class="col-sm-9" style="height: 5.7vh;">
					            <input type="text" class="form-control" id="nameEdit" placeholder="役割の名称">
					            <span class="text-muted" style="font-size: 10px;"></span>
					        </div>
					    </div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">
						<i class="fa fa-times"></i> 閉じる
					</button>
					<button type="button" class="btn btn-success" id="roleInfoChangeBtn">
						<i class="fa fa-leaf"></i> 更新
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="authEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="roleName"></h4>
				</div>
				<div class="modal-body">
					<ul id="authTree" class="ztree"></ul>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">
						<i class="fa fa-times"></i> 閉じる
					</button>
					<button type="button" class="btn btn-success" id="authChangeBtn">
						<i class="fa fa-google-wallet"></i> 変更
					</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/role-pages.js" id="jsContainer">
</body>
</html>