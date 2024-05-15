<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<div class="card border-steelblue mb-3">
					<div class="card-header text-bg-steelblue mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-bars-staggered"></i> 分類管理
						</h5>
					</div>
					<div class="card-body">
						<div id="categroyTreeView"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/categorykanri.js" id="jsContainer">
</body>
</html>