<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix ="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page session="false" %>


<!DOCTYPE html>


<html>

	<head>
		<title>My Schedule</title>
	</head>


	<body>
		<security:authorize access="isAuthenticated()">
    		Welcome!!! You are logged in as <security:authentication property="principal.username" />
    	</security:authorize>
		<h1>My Slots</h1>
		
		
		
		
		<script>

	
			var req = new XMLHttpRequest();
	
	  		req.open("GET", "/PPublica/api/timeslots/forpublisher/" + "<c:out value='${userId}'/>", true);
	  		req.setRequestHeader("Content-Type", "application/json");
	  		req.setRequestHeader("Authorization", "Bearer<eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuYXRmIiwidXNlcklkIjoiMiIsInJvbGUiOiJVU0VSX0FETUlOIn0.nRAMBXhGO3EA2iosHuSDjLD7FTGge8M4PmNoz0PNFk31ON0e4UDNQpt27ShtcFdg8pJINqMPfA8Oi7klE4SY8w>");
			req.send(null);
	  		req.addEventListener("load", function() {
  	  	
				var response;
  				response = req.responseText;
				
	  			if(req.status >= 300 || req.status < 200) {
	
	  		  	    console.log(response);
	  		  	    console.log(req.status);
	
	  	  		}

  		    	document.body.appendChild(document.createTextNode(response));
	  	  		
	
	  		});



		


		</script>
		

	</body>


</html>