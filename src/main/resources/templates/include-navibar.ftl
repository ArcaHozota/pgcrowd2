<!DOCTYPE html>
<html lang="ja-JP">
<body>
	<#assign principalAdmin = Session.SPRING_SECURITY_CONTEXT.authentication.principal
	        userAdminName = principalAdmin.originalAdmin.username
	        personalId = principalAdmin.originalAdmin.id>
	<nav class="navbar navbar-expand-lg bg-body-tertiary navbar-dark bg-dark fixed-top">
		<div class="container-fluid">
			<a class="navbar-brand effect-shine" style="font-size: 24px; font-weight: 900;" 
				href="/pgcrowd/category/toMainmenu">PGアプリケーション</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContents"
				aria-controls="navbarSupportedContents" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContents">
				<div class="input-group d-flex justify-content-end">
					<input type="text" class="form-control" id="searchInput" placeholder="検索" style="max-width: 180px;">
					<button class="input-group-text bg-transparent p-0 px-2" id="searchBtn">
						<i class="fa fa-search"></i>
					</button>
				</div>
				<ul class="navbar-nav mb-2 mb-lg-0 d-flex" id="dropdown-info">
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle btn btn-success me-2" href="#" id="navbarDropdown"
							role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
							style="height: 37.6px;">
							<i class="fa fa-user"></i> ${userAdminName}
						</a>
						<div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="#" id="toPersonal">
								<i class="fa fa-cog"></i> 個人設定
								<input type="hidden" value="${personalId}">
							</a>
							<a class="dropdown-item" href="#">
								<i class="fa fa-comments"></i> メッセージ
							</a>
							<div class="dropdown-divider"></div>
							<a id="logoutLink" class="dropdown-item" href="#">
								<i class="fa fa-sign-out"></i> ログアウト
							</a>
						</div>
					</li>
					<form id="logoutForm" method="post" action="/pgcrowd/employee/logout" 
								style="display: none;"></form>
					<#assign authNames = principalAdmin.getAuthorities()>
					<#list authNames as authName>
						<#if authName.getAuthority() == 'employee%delete' || authName.getAuthority() == 'employee%edition'>
							<#assign chkFlg = 'true'>
						</#if>
					</#list>
					<#if chkFlg?exists>
						<input type="hidden" value="${chkFlg}" id="authChkFlgContainer">
						<#else>
						<input type="hidden" value="false" id="authChkFlgContainer">
					</#if>
				</ul>
				<div class="d-flex">
					<button id="logoutBtn" type="button" class="btn btn-danger me-2">
						<i class="fa fa-power-off"></i>
					</button>
				</div>
			</div>
		</div>
	</nav>
</body>
</html>