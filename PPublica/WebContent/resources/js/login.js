(function login() {
	var button = document.getElementsByClassName("buttonSubmit")[0];
	console.log("Sert");

	var form = document.getElementById("user");
	
	function getUsername(event) {
		credentials["username"] = this.value;
		console.log(this.value);
	}
		
	function getPassword(event) {
		credentials["password"] = this.value;
		console.log(this.value);

	}

	var credentials = {"username": null,
					"password": null,
	};
	
	var usernameField = document.getElementById("username");
	console.log(usernameField);
	usernameField.addEventListener("change", getUsername);
	
	var passwordField = document.getElementById("password");
	passwordField.addEventListener("change", getPassword);

		
	button.addEventListener("click", function() {
		console.log("attempting to log in:");
		console.log(credentials);
		
		var req = new XMLHttpRequest();
		
		req.open("POST", "/PPublica/login", true);
		req.setRequestHeader("Content-Type", "application/json");
		req.send(JSON.stringify(credentials));
		req.addEventListener("load", function() {
	  	  		
			var response;
			if(req.status >= 300 || req.status < 200) {
		
				response = req.responseText;
				console.log(response);
				console.log(req.status);
				document.body.appendChild(document.createTextNode(response));
		
			} else {
				window.location.href="/PPublica/myslots";
			}
		
		});
	});
})();



