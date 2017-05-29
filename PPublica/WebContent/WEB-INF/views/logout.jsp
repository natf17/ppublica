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


	.divLogoutButton {
		margin-top: 5%;
		margin-left: 45%;
		width:10em;
		height:8em;
		background-color:black;
	}


	</style>
	<head>
		<title>LOGOUT</title>
	</head>


	<body>
			<h1>Log Out</h1>
	
		<div class = "divLogoutButton">
		</div>
		
		
		
		<script>
			var button = document.getElementsByClassName("divLogoutButton")[0];
	
			button.addEventListener("click", function() {
				console.log("attempting to log out:");
				console.log(document.cookie);
				document.cookie = 'access_token' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;'
	  	  		window.location.href="<c:url value="/myslots" />";

	
	  		});


		</script>
		

	</body>


</html>