

function addPublisher(relativePostUrl, redirectUrl) {
	function getFirstName() {
		publisher["firstName"] = this.value;
	}

	function getLastName() {
		publisher["lastName"] = this.value;

	}

	function getUsername() {
		publisher["username"] = this.value;

	}
	
	
	
	
	
	var button = document.getElementsByClassName("buttonSubmit")[0];
	var publisher = {"firstName": null,
					 "lastName": null,
					 "username": null
					};
	var form = document.getElementById("newPublisher");

	var firstNameField = document.getElementById("firstNameInput");
	firstNameField.addEventListener("change", getFirstName);
	
	var lastNameField = document.getElementById("lastNameInput");
	lastNameField.addEventListener("change", getLastName);
	
	var usernameField = document.getElementById("usernameInput");
	usernameField.addEventListener("change", getUsername);
	
	
	
	button.addEventListener("click", function() {
		console.log("new publisher:");
		console.log(publisher);



		var req = new XMLHttpRequest();

		req.open("POST", relativePostUrl, true);
		req.setRequestHeader("Content-Type", "application/json");
		req.send(JSON.stringify(publisher));
		req.addEventListener("load", function() {
		
		var response;
			if(req.status >= 300 || req.status < 200) {

				response = req.responseText;
		  	    console.log(response);
		  	    console.log(req.status);
		    	document.body.appendChild(document.createTextNode(response));

	  		} else {
	  			
	  			
	  			
	  			window.location.href=redirectUrl;
	  			
	  		}

		});
		
	});

}