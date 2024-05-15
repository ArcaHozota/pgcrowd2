require(['/static/jquery/jquery-3.7.1.min.js'], function() {
	require(['/static/bootstrap/js/bootstrap.bundle.min.js'], function() {
		require(['/static/ztree/jquery.ztree.all.js', '/static/bstreeview/js/bstreeview.min.js'], function() {
			require(['/static/customizes/commons.js', '/static/layer/layer.js'], function() {
				let jsUrl = document.getElementById("jsContainer").getAttribute('value');
				require([jsUrl]);
			});
		});
	});
});