<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix ="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page session="false" %>

<!DOCTYPE HTML>

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
			width:2em;
			height:1em;
			background-color:black;
		}


	</style>
	
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Schedules</title>
    </head>
    <body>
    	<security:authorize access="isAuthenticated()">
    	<a href="<c:url value="/myslots"/>" ><security:authentication property="principal.username" />'s slots</a>
    	<div class = "divLogoutButton">
		</div>
    	</security:authorize>
    	
    	<security:authorize access="!isAuthenticated()">
    	<a href="<c:url value="/login"/>" >Log IN</a>
    	<div class = "divLoginButton">
		</div>
    	</security:authorize>
    	
    	<security:authentication property="principal" var="userId"/>
    	<c:out value="${userId}"/>
    	<c:if test="${userId == 'anonymousUser'}">
    	Welcome!!! GUEST
    	</c:if>
    	
    	<a href="<c:url value="/69thst"/>" >69thSt</a>
        <h1>Schedules</h1>
        	
        <c:forEach items="${weekSchedules}" var="week">
        	<c:out value="Id: ${week.id}" />
        </c:forEach>
    
    
    <script>
	var req = new XMLHttpRequest();

  	req.open("GET", "/PPublica/api/week/4", true);
  	req.setRequestHeader("Accept", "application/json");
  	req.send(null);
  	req.addEventListener("load", function() {

  	var response = req.responseText;
    var week = JSON.parse(response);
  	
    console.log(week["week"]);
    
    document.body.appendChild(document.createTextNode(response));
  	});

  	var button;
	if(document.getElementsByClassName("divLogoutButton").length != 0) {
		console.log("UNNN")
		button = document.getElementsByClassName("divLogoutButton")[0];
	
		button.addEventListener("click", function() {
			console.log("attempting to log out:");
			console.log(document.cookie);
			document.cookie = 'access_token' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;'
	  			window.location.href="<c:url value="/" />";
		});
	}
	
	</script>
	</body>
</html>