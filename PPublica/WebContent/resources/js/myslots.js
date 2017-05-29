(function addButton() {
	var addSlots = document.getElementsByClassName("addSlot");
	
	for(var i = 0; i < addSlots.length; i++) {
		addSlots[i].addEventListener("click", function() {
			window.location.href = "/PPublica/schedules/" + this.id + "/edit";
		});
	}
	
})();












(function addDeletes() {
	var deleteDivs = document.getElementsByClassName("deleteDiv");
	
	for(var i = 0; i < deleteDivs.length; i++) {

		deleteDivs[i].addEventListener("click", function(event) {
			var parent = this.parentNode;
			var dayDiv = parent.parentNode;
			var weekDiv = dayDiv.parentNode;
			var weekParent = weekDiv.parentNode;
			var correspondingAddDiv = weekParent.getElementsByClassName("addDiv")[0];
			
			var weekChildren = weekDiv.getElementsByClassName("dayDiv");
			var dayChildren = dayDiv.getElementsByClassName("slotDiv");
			
			var id = this.id;
			console.log(parent);
			var idPieces = id.split("-");
			var slotId;
			var publisherId;
			
			slotId = idPieces[1];
			publisherId = idPieces[2];
						

			var reqPost = new XMLHttpRequest();
			console.log("DELETE CLICKED");
			reqPost.open("DELETE", "/PPublica/api/assignments/" + slotId + "/" + publisherId, true);
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
	  	  			// are there any more slots in this day?
	  	  			if(dayChildren.length - 1 == 0) {
	  	  				// this day will be deleted...
	  	  				
	  	  				// are there any more days in this week?
	  	  				if(weekChildren.length - 1 == 0) {
	  	  					// this week will be deleted
	  	  					weekParent.removeChild(weekDiv);
	  	  					weekParent.removeChild(correspondingAddDiv);
	  	  				}
	  	  				else {
	  	  					// delete the day only
	  	  					weekDiv.removeChild(dayDiv);
	  	  				}
	  	  			}
	  	  			else {
	  	  				// delete the slot only
		  	  			dayDiv.removeChild(parent);

	  	  			}
	  	  				  	  			
	  	  				  	  			
	  	  		}

	 

	    	
	  		});
			
			event.stopPropagation();

			

		});
	}
})();
