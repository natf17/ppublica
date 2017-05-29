<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix ="form" %>
<%@ page session="false" %>

<!DOCTYPE HTML>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Schedules</title>
    </head>
    <style>
	body {
		font-size: 1.5em;
		font-family: Verdana, Geneva, sans-serif;
	}

	h1 {
		font-family: Verdana, Geneva, sans-serif;
		text-align: center;

	}
	input {
		font-size: 2em;
	}

	.row {
		padding-bottom:5%;
	}

	.buttonDiv {
		margin-top: 5%;
		margin-left: 45%;
	}


	</style>
	<head>
		<title>New Publisher</title>
		<h1>New Publisher</h1>
	</head>


	<body>
		<div class = "divPublisherForm">
			<form id="newPublisher">
				<div class="row">
					<span>First name: </span><span><input type="text" id="firstNameInput" name="firstName" onchange="getFirstName()"></span>
				</div>
				<div class="row">
					<span>Last name: </span><span><input type="text" id="lastNameInput" name="lastName" onchange="getLastName()"></span>
				</div>
				<div class="row">
					<span>Username: </span><span><input type="text" id="usernameInput" name="username" onchange="getUsername()"></span>
				</div>
			</form>
			<div class="buttonDiv">
					<button class= "buttonSubmit" type="button" name="button">Create</button>
				</div>
		</div>
		
		
		
		<script>
			var button = document.getElementsByClassName("buttonSubmit")[0];
			var publisher = {"firstName": null,
							 "lastName": null,
							 "username": null
							};
			var form = document.getElementById("newPublisher");


			function getFirstName() {
				publisher["firstName"] = document.getElementById("firstNameInput").value;
			}
	
			function getLastName() {
				publisher["lastName"] = document.getElementById("lastNameInput").value;
	
			}
	
			function getUsername() {
				publisher["username"] = document.getElementById("usernameInput").value;
	
			}

			button.addEventListener("click", function() {
				console.log("new publisher:");
				console.log(publisher);
	
	
	
				var req = new XMLHttpRequest();
	
	  		req.open("POST", "/PPublica/api/publisher", true);
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
	  	  			window.location.href="<c:url value="/allPublishers" />";
	  	  		}
	
	  		});
		});


		


		</script>
		

	</body>

	
</html>