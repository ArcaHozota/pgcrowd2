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
				<div class="card border-klein mb-3">
					<div class="card-header text-bg-klein mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa fa-th-large"></i> 社員情報変更
						</h5>
					</div>
					<div class="card-body">
						<form role="form" id="editForm">
							<input type="hidden" value="${arawaseta.id}" id="editIdContainer">
							<input type="hidden" value="${pageNum}" id="pageNumContainer">
							<div class="form-group row">
						        <label for="loginAccountEdit" class="col-sm-3 col-form-label text-right">ログインアカウント</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" value="${arawaseta.loginAccount}" 
						            	id="loginAccountEdit" placeholder="アカウントを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
							<div class="form-group row">
						        <label for="usernameEdit" class="col-sm-3 col-form-label text-right">ユーザ名</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" value="${arawaseta.username}" 
						            	id="usernameEdit" placeholder="ユーザ名を入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
							<div class="form-group row">
						        <label for="passwordEdit" class="col-sm-3 col-form-label text-right">パスワード</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="text" class="form-control" value="${arawaseta.password}" 
						            	id="passwordEdit" placeholder="パスワードを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
							<div class="form-group row">
						        <label for="emailEdit" class="col-sm-3 col-form-label text-right">メールアドレス</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="email" class="form-control" value="${arawaseta.email}"
						            	 id="emailEdit" placeholder="メールアドレスを入力してください">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
							<div class="form-group row">
						        <label for="dateEdit" class="col-sm-3 col-form-label text-right">生年月日</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <input type="date" class="form-control" value="${arawaseta.dateOfBirth}" id="dateEdit">
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						    <div class="form-group row">
						        <label for="roleEdit" class="col-sm-3 col-form-label text-right">役割</label>
						        <div class="col-sm-7" style="height: 5.7vh;">
						            <select id="roleEdit" class="custom-select">
						                <#list employeeRoles as employeeRole>
											<option value="${employeeRole.id}">${employeeRole.name}</option>
										</#list>
						            </select>
						            <span class="text-muted" style="font-size: 10px;"></span>
						        </div>
						    </div>
						</form>
					</div>
					<div class="card-footer">
						<button type="button" class="btn btn-primary my-2 my-sm-0" id="editInfoBtn">
							<i class="fa fa-hourglass-half"></i> 変更
						</button>
						<button type="button" class="btn btn-secondary my-2 my-sm-0" id="restoreBtn">
							<i class="fa fa-trash"></i> 廃棄
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/employee-addition.js" id="jsContainer">
</body>
</html>