(function logout() {
	var button;

	if(document.getElementsByClassName("divLogoutButton").length != 0) {
		console.log("UNNN")
		button = document.getElementsByClassName("divLogoutButton")[0];
		
		aElem = button.getElementsByTagName("A")[0];
		console.log(aElem);
		aElem.onclick = function() {
			console.log("link overriden");
			return false;
		};
		
		button.onclick = function() {
			console.log("attempting to log out:");
			console.log(document.cookie);
			document.cookie = 'access_token' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT';
			console.log("dfghjk");

			window.location.href="/PPublica/";
			return true;
		}
	}
})();