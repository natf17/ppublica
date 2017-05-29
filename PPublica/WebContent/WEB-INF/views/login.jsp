<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix ="form" %>
<%@ page session="false" %>


<!DOCTYPE html>


<html>

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
		<title>LOGIN</title>
	</head>


	<body>
			<h1>Log In</h1>
	
		<div class = "divCredentialsForm">
			<form id="user">
				<div class="row">
					<span>Username: </span><span><input type="text" id="username" name="username" onchange="getUsername()"></span>
				</div>
				<div class="row">
					<span>Password: </span><span><input type="text" id="password" name="password" onchange="getPassword()"></span>
				</div>
			</form>
			<div class="buttonDiv">
					<button class= "buttonSubmit" type="button" name="button">Create</button>
				</div>
		</div>
		
		
		
		<script>
			var button = document.getElementsByClassName("buttonSubmit")[0];
			var credentials = {"username": null,
							 "password": null,
							};
			var form = document.getElementById("user");


			function getUsername() {
				credentials["username"] = document.getElementById("username").value;
			}
	
			function getPassword() {
				credentials["password"] = document.getElementById("password").value;
			}
	
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
	  	  			window.location.href="<c:url value="/myslots" />";
	  	  		}
	
	  		});
		});


		


		</script>
		

	</body>


</html>