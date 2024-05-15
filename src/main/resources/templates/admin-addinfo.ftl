<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<nav aria-label="breadcrumb">
				    <ol class="breadcrumb">
				        <li class="breadcrumb-item"><a href="#" style="text-decoration: none;" 
				        	id="toMainmenu2">メインメニュー</a></li>
				        <li class="breadcrumb-item"><a href="#" style="text-decoration: none;" 
				        	id="toPages">データリスト</a></li>
				        <li class="breadcrumb-item active" aria-current="page">データ追加</li>
				    </ol>
				</nav>
				<div class="card border-darkgreen mb-3">
					<div class="card-header text-bg-darkgreen mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa fa-th-large"></i> 社員情報追加
						</h5>
					</div>
					<div class="card-body">
						<form role="form" id="inputForm">
						    <div class="form-group row">
						        <label for="loginAccountInput" class="col-sm-3 col-form-label text-right">ログインアカウント</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" id="loginAccountInput" placeholder="ログインアカウントを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="usernameInput" class="col-sm-3 col-form-label text-right">ユーザ名</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" id="usernameInput" placeholder="ユーザ名を入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="passwordInput" class="col-sm-3 col-form-label text-right">パスワード</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" id="passwordInput" placeholder="パスワードを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="emailInput" class="col-sm-3 col-form-label text-right">メールアドレス</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="email" class="form-control" id="emailInput" placeholder="メールアドレスを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="dateInput" class="col-sm-3 col-form-label text-right">生年月日</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="date" class="form-control" id="dateInput">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="roleInput" class="col-sm-3 col-form-label text-right">役割</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <select id="roleInput" class="custom-select">
						                <#list employeeRoles as employeeRole>
											<option value="${employeeRole.id}">${employeeRole.name}</option>
										</#list>
						            </select>
						        </div>
						    </div>
						</form>
					</div>
					<div class="card-footer">
						<button type="button" class="btn btn-success my-2 my-sm-0" id="saveInfoBtn">
							<i class="fa fa-user-plus"></i> 追加
						</button>
						<button type="button" class="btn btn-secondary my-2 my-sm-0" id="resetBtn">
							<i class="fa fa-undo"></i> リセット
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/employee-addition.js" id="jsContainer">
</body>
</html>