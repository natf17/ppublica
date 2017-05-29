function addPublisherDivs(publisherJson) {
	
	var publisher;
	var div;
	var header;
	for(var i = 0; i < publisherJson.length; i++) {
		div = document.createElement("div");
		div.className = "pubDiv";
		
		publisher = publisherJson[i];
		
		header = document.createTextNode(publisher["firstName"] + " " + publisher["lastName"]);
		
		div.appendChild(header);
		document.body.appendChild(div);
	}
	console.log(publisherJson);
}




(function showAllPublishers() {
	
	var req = new XMLHttpRequest();
	
	req.open("GET", "/PPublica/api/publisher/all", true);
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
	  		addPublisherDivs(response);

	  	}
	  		
	});
	
	
})();