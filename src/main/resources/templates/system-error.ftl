<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<style>
		.bg-dark {
			background-color: #000000 !important;
		}
	</style>
	<div>
		<nav class="navbar navbar-dark bg-dark fixed-top" role="navigation">
			<div class="container-fluid">
				<a class="navbar-brand" href="/pgcrowd/to/index"
					style="font-size: 24px;">PGアプリケーション</a>
			</div>
		</nav>
	</div>
	<div class="container" style="margin-top: 60px;">
		<div class="text-center">
			<h2>システム情報</h2>
			<h6 text="${requestScope.exception.message}"></h6>
			<button id="backBtn" style="width: 300px; margin: 0px auto 0px auto;"
				class="btn btn-lg btn-warning btn-block">戻る</button>
		</div>
	</div>
	<script type="text/javascript">
		$("#backBtn").on('click', function() {
			window.history.back();
		});
	</script>
</body>
</html>