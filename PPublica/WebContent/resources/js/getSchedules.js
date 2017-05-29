(function addLinks() {
	var divs = document.getElementsByClassName("weekDiv");
	
	for(var i = 0; i < divs.length; i++) {

		divs[i].addEventListener("click", function() {
			
			var idElem;
			var spanText;
			
			idElem = this.getElementsByTagName("SPAN")[0];
			spanText = (idElem.innerHTML);
			window.location.href="/PPublica/schedules/" + spanText;
		});
	}
})();


(function addDeletes() {
	var deleteDivs = document.getElementsByClassName("deleteDiv");
	
	for(var i = 0; i < deleteDivs.length; i++) {

		deleteDivs[i].addEventListener("click", function(event) {
			
			var parent = this.parentNode;
			
			var idElem;
			var spanText;
			
			idElem = parent.getElementsByTagName("SPAN")[0];
			spanText = (idElem.innerHTML);
			
			
			var reqPost = new XMLHttpRequest();
			console.log("DELETE CLICKED");
			reqPost.open("DELETE", "/PPublica/api/week/" + spanText, true);
			reqPost.setRequestHeader("Content-Type", "application/json");
			reqPost.send(null);
	  		reqPost.addEventListener("load", function() {



	  			var response;
	  			if(reqPost.status >= 300 || reqPost.status < 200) {

	  				response = reqPost.responseText;
	  		  	    console.log(response);
	  		  	    console.log(reqPost.status);
	  		    	document.body.appendChild(document.createTextNode(response));

	  	  		} else {
	  	  			document.body.removeChild(parent);
	  	  		}

	 

	    	
	  		});
			
			event.stopPropagation();

			

		});
	}
})();


/*function addScheduleDivs(weekJson) {
	
	var week;
	var div;
	var header;
	
	var idDiv;
	var idDivClass = "id";
	var locationDiv;
	var locationDivClass = "location";
	
	var deleteDiv;
	var imageElementDelete;
	
	for(var i = 0; i < weekJson.length; i++) {
		div = document.createElement("div");
		div.className = "weekDiv";
		
		week = weekJson[i];
		weekId = week["id"];
		
		idDiv = document.createElement("div");
		idDiv.className = idDivClass;
		
		locationDiv = document.createElement("div");
		locationDiv.className = locationDivClass;

		idDiv.appendChild(document.createTextNode(weekId));
		locationDiv.appendChild(document.createTextNode(week["location"]));
		//header = document.createTextNode(week["id"] + " " + week["location"]);
		
		div.appendChild(idDiv);
		div.appendChild(locationDiv);
	
		deleteDiv = document.createElement("div");
		deleteDiv.className = "deleteDiv";
		imageElementDelete = document.createElement("img");
		imageElementDelete.src = "http://localhost:8080/PPublica/resources/DELETEBUtton.png";
		/*imageElementDelete.width = "15";
		imageElementDelete.length = "15";
		
		deleteDiv.appendChild(imageElementDelete);
		
		div.appendChild(deleteDiv);
		
		div.addEventListener("click", function() {
			var idD = this.getElementsByClassName("id")[0];
			var id = idD.innerHTML;
			window.location.href="/PPublica/schedules/" + id;

		});
		document.body.appendChild(div);
	}
	console.log(weekJson);
}




(function showAllWeeks() {
	
	var req = new XMLHttpRequest();
	
	req.open("GET", "/PPublica/api/week/all", true);
	req.setRequestHeader("Accept", "application/json");
	req.send(null);
	req.addEventListener("load", function() {
		
		var response;
		
		response = JSON.parse(req.responseText);

		if(req.status >= 300 || req.status < 200) {

			console.log(response);
		  	console.log(req.status);
		    document.body.appendChild(document.createTextNode(response));

	  	} else {
	  		addScheduleDivs(response);

	  	}
	  		
	});
	
	
})();


*/