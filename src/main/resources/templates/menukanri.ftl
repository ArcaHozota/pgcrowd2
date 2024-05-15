<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<div class="card border-darkgreen mb-3">
					<div class="card-header text-bg-darkgreen mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-bars-staggered"></i> メニューメンテナンス
						</h5>
					</div>
					<div class="card-body">
						<div id="treeView"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/menukanri.js" id="jsContainer">
</body>
</html>