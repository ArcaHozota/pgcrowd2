<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/static/css/style.css">
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<style type="text/css">
	input[type=password]::-ms-reveal {
		display: none;
	}
	
	input[type=password]::-ms-clear {
		display: none;
	}
	
	input[type=password]::-o-clear {
		display: none;
	}
	
	.input-box #eyeIcons {
		position: absolute;
		cursor: pointer;
		color: #000;
		top: 150px;
		right: 15px;
		font-size: 1.2em;
	}
</style>
<script type="text/javascript" src="/static/jquery/jquery-3.7.1.min.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
<script type="text/javascript" src="/static/layer/layer.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
</head>
<body>
    <div class="container">
    	<#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
		    <input type="hidden" value="${Session.SPRING_SECURITY_LAST_EXCEPTION.message}" id="errorMsgContainer">
		<#else>
			<input type="hidden" id="errorMsgContainer">
		</#if>
		<#if torokuMsg?exists!''>
			<input type="hidden" value="${torokuMsg}" id="torokuMsgContainer">
		</#if>
        <div class="login-box" id="loginBox">
        	<h2 class="login-title">
                <span>すでにアカウント持ち？</span>ログイン
            </h2>
            <form class="input-box" action="/pgcrowd/employee/doLogin" method="post" id="loginForm">
            	<#if registeredEmail?exists>
					<input type="text" value="${registeredEmail}" name="loginAcct"
						id="accountIpt" placeholder="アカウント">
				<#else>
					<input type="text" name="loginAcct" id="accountIpt" placeholder="アカウント">
				</#if>
				<ion-icon name="eye-outline" id="eyeIcons"></ion-icon>
                <input type="password" name="userPswd" id="passwordIpt" placeholder="パスワード">
            </form>
            <button type="button" id="loginBtn">ログイン</button>
        </div>
        <div class="toroku-box slide-up" id="torokuBox">
            <div class="center">
                <h2 class="toroku-title">
	                <span>アカウント無し？</span>登録
	            </h2>
	            <form class="input-box" action="/pgcrowd/employee/toroku" method="post" id="torokuForm">
	                <input type="email" name="email" id="emailIpt" placeholder="メール">
	                <input type="password" name="password" id="passwordIpt1" placeholder="パスワード">
	                <input type="password" id="passwordIpt2" placeholder="パス再入力">
	            </form>
	            <button type="button" id="torokuBtn">登録</button>
            </div>
        </div>
        <script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
		<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
    </div>
    <script type="text/javascript" src="/static/customizes/logintoroku.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
</body>
</html>