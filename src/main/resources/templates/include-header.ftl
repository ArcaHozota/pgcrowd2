<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf_token" content="${_csrf.token}">
<link rel="stylesheet" type="text/css" href="/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/static/css/main.css">
<link rel="stylesheet" type="text/css" href="/static/ztree/metroStyle.css">
<link rel="stylesheet" type="text/css" href="/static/bstreeview/css/bstreeview.min.css">
<link rel="stylesheet" type="text/css" href="/static/css/customize.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<style type="text/css">
	a {
		text-decoration: none;
	}
	
	.bg-dark {
		background-color: #000000 !important;
	}
	
	.effect-shine {
		transition: 500ms;
	}
	
	.effect-shine:hover {
		animation: shine 1500ms infinite alternate;
	}
	
	@keyframes shine {
		from {
			color: #fff;
			text-shadow: 0 0 5px #03e9f4,
			            0 0 25px #03e9f4,
			            0 0 50px #03e9f4,
			            0 0 100px #03e9f4;
		}

		to {
			color: #fff;
			text-shadow: 0 0 2px #03e9f4,
			            0 0 5px #03e9f4,
			            0 0 7px #03e9f4,
			            0 0 10px #03e9f4;
		}
	}
</style>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/sweetalert2@11" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
<script data-main="/static/requirejs/main" src="/static/requirejs/require.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn" strict-dynamic></script>
</head>
</html>