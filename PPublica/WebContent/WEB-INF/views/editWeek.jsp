<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix ="form" %>
<%@ page session="false" %>

<!DOCTYPE html>


<html>
	<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.1.1.min.js"></script>

	<style>
		table {
   			 width: 100%;
   			 border-collapse: collapse;
   			 border-left-color:transparent; 
   			 border-bottom-color:black; 

		}

		.divTable
    {
        display:  table;
        width:100%;
        background-color:#eee;
        border:1px solid  #666666;
        /*-spacing:5px;/*cellspacing:poor IE support for  this*/
       /* border-collapse:separate;*/
    }

    .divRow
    {
       display:table-row;
       width:auto;
    }

    .divCell
    {
        float:left;/*fix for  buggy browsers*/
        display:table-column;
        width:200px;
        background-color:#ccc;
        height:50px;
    }

    .headRow {
    	display:table-row;
    }

		td,th {
    		height: 50px;
  			position: relative;
    		border-bottom-color:brown;

		}

		div.submitDiv {
			height:50px;
			position:relative;
			background-color: black;
			width:100%;
			margin-top:1%;
		}

		div.data1{
			display:inline-table;
			height:100%;
			margin:0px;
			vertical-align: middle;
		}
		div.data2{
			display:inline-block;
			height:100%;
			margin:0px;
		}
		div.data3{
			display:table-cell;
        	text-align:center;
        	vertical-align:middle;
        }
        div.data {
        	display:table-cell;
        	text-align:center;
        	vertical-align:middle;
        	color:white;

        }

        div.slotWrapper {
        	height:100%;
        }

		div.timeCell {
			vertical-align:bottom;
			text-align:right;
			border-bottom-color:transparent;
			display: table-cell;
			height:50px;
			top:0.6em;
			position:relative;
		}
		div.headerCell {
			display: table-cell;
			height:50px;
			vertical-align:bottom;
			text-align:center;

		}




		.row {
		display:table-row;
		width:100%;
		height:50px;
	}
	.name {
		display:inline-table;
		float:left;
		width:auto;
		height:50px;
	}

	.button {
		display:inline-table;
		height:50px;
		float:right;
	}


	</style>
	<head>
	</head>


	<body>
		<div class = "divTable">
		</div>



	<script>

	var colors = [
	  			"BlueViolet",
	  			"Brown",
	  			"BurlyWood",
	  			"CadetBlue",
	  			"Chocolate",
	  			"CornflowerBlue",
	  			"Crimson",
	  			"DarkBlue",
	  			"DarkCyan",
	  			"DarkGoldenRod",
	  			"DarkGrey",
	  			"DarkMagenta",
	  			"DarkOliveGreen",
	  			"DarkOrange",
	  			"DarkOrchid",
	  			"DarkRed",
	  			"DarkSalmon",
	  			"DarkSeaGreen",
	  			"DarkSlateBlue",
	  			"DarkSlateGray",
	  			"DarkTurquoise",
	  			"DarkViolet",
	  			"DeepPink",
	  			"DeepSkyBlue",
	  			"FireBrick",
	  			"ForestGreen",
	  			"Gold",
	  			"Green",
	  			"HotPink",
	  			"IndianRed",
	  			"Indigo",
	  			"Khaki",
	  			"LightCoral",
	  			"Maroon",
	  			"Navy",
	  			"Olive",
	  			"Orange",
	  			"OrangeRed",
	  			"Peru",
	  			"Red",
	  			"Salmon",
	  			"SeaGreen",
	  			"Teal"
	  		];

		var personToColor = [];

		var reqP = new XMLHttpRequest();

	  	reqP.open("GET", "/PPublica/api/publisher/all", true);
	  	reqP.setRequestHeader("Accept", "application/json");
	  	reqP.send(null);
	  	reqP.addEventListener("load", function() {

	  	var responsePubs = reqP.responseText;
	    var availablePublishers = JSON.parse(responsePubs);





		

		var req = new XMLHttpRequest();

	  	req.open("GET", "/PPublica/api/week/4", true);
	  	req.setRequestHeader("Accept", "application/json");
	  	req.send(null);
	  	req.addEventListener("load", function() {

	  		var response = req.responseText;
	    	var source = JSON.parse(response);




			// iterate through week array of objects (days) to determine smallest startTime;

			var daysInWeek = source["week"];

			var minTimeInMinutes;

			// start at second element if it exists
			// else return first as min
		(function calcWeekMinTime() {
			var minTimeHour = daysInWeek[0]["minTime"][0];
			var minTimeMinute = daysInWeek[0]["minTime"][1];
			minTimeInMinutes = minTimeHour * 60 + minTimeMinute;

			var minTimeHourCompare;
			var minTimeMinuteCompare;
			var minTimeInMinutesCompare;


			for(var i = 1; i < daysInWeek.length; i++) {
				minTimeHourCompare = daysInWeek[i]["minTime"][0];
				minTimeMinuteCompare = daysInWeek[i]["minTime"][1];
				minTimeInMinutesCompare = minTimeHourCompare * 60 + minTimeMinuteCompare;


				if(minTimeInMinutesCompare < minTimeInMinutes) {
					minTimeInMinutes = minTimeInMinutesCompare;
				}
			}


		})();


		console.log("WEEK minTime: " + minTimeInMinutes);






		var maxTimeInMinutes;

		(function calcWeekMaxTime() {
			var maxTimeHour = daysInWeek[0]["maxTime"][0];
			var maxTimeMinute = daysInWeek[0]["maxTime"][1];
			maxTimeInMinutes = maxTimeHour * 60 + maxTimeMinute;
			
			var maxTimeHourCompare;
			var maxTimeMinuteCompare;
			var maxTimeInMinutesCompare;



			for(var i = 1; i < daysInWeek.length; i++) {
				maxTimeHourCompare = daysInWeek[i]["maxTime"][0];
				maxTimeMinuteCompare = daysInWeek[i]["maxTime"][1];
				maxTimeInMinutesCompare = maxTimeHourCompare * 60 + maxTimeMinuteCompare;


				if(maxTimeInMinutesCompare > maxTimeInMinutes) {
					maxTimeInMinutes = maxTimeInMinutesCompare;
				}
			}
		})();

		console.log("WEEK maxTime: " + maxTimeInMinutes);









		var durationInWeek = daysInWeek[0]["duration"] / 60;

		console.log("WEEK duration: " + durationInWeek);










		// generate all the times in days in week
		var timesInWeek = [];
		var hours;
		var mins;
		var timeString;
		for(var i = minTimeInMinutes; i <= maxTimeInMinutes; i += durationInWeek) {
			mins = i % 60;
			hours = (i - mins)/60;
			timeString = hours + ":" + mins;
			mins < 10 ? timeString += "0": null;
			timesInWeek.push(timeString);
		}
		console.log(timesInWeek);









		var table = document.getElementsByClassName("divTable")[0];
		var row;
		var numberOfColumns;

		// draw headers
		(function drawHeaders() {
			// 1 column for times + (1 column / daysInWeek.length)
			numberOfColumns = 1 + daysInWeek.length;

			console.log("#columns: " + numberOfColumns);

			

			// create row for headers......
			row = document.createElement("div");
			row.className = "headRow";

			var headerCell;
			var divCell;
			var contentsCell;
			var text;

			// create header data cells

			// starting with regular cell for times	
			divCell = document.createElement("div");
			divCell.className = "timeCell";
			text = timesInWeek[0];
			contentsCell = document.createTextNode(text);
			
			// append first element to row
			divCell.appendChild(contentsCell);
			row.appendChild(divCell);


			// ...continue with rest of headers
			for(var i = 0; i < numberOfColumns - 1; i++) {
				headerCell = document.createElement("div");
				headerCell.className = "headerCell";
				divCell = document.createElement("div");
				divCell.className = "day";
				text = daysInWeek[i]["weekday"];
				contentsCell = document.createTextNode(text);

				divCell.appendChild(contentsCell);
				headerCell.appendChild(divCell);
				row.appendChild(headerCell);

			}

			// append finished row to table
			table.appendChild(row);


		})();

		var arrayScheduleElements = [];
		var arrayEditElements = [];

		// draw each row
		(function drawRows() {
			for(var i = 1; i < timesInWeek.length; i++) {

				row = document.createElement("div");
				row.className = "divRow";

				var regularCell;
				var wrapperCell;
				var contentsCell;
				var text;

				for(var j = 0; j < numberOfColumns; j++) {
					console.log("in j");
					regularCell = document.createElement("div");

					// first element in row is under time column
					if(j == 0) {
						regularCell.className = "timeCell";		
						text = timesInWeek[i];
						contentsCell = document.createTextNode(text);

						regularCell.appendChild(contentsCell);

						// append element to row
						row.appendChild(regularCell);
					} else {
						// simply draw empty cell
						// with id = day + timeInMinutes
						// although cell is in same row as endTime, it will be identified by its startTime
						regularCell.id = daysInWeek[j - 1]["weekday"] + timesInWeek[i-1];
						regularCell.className = "data";
						//wrapperCell = document.createElement("div");
						//wrapperCell.className ="slotWrapper";

						console.log(daysInWeek[j - 1]["weekday"] + timesInWeek[i-1]);

						//regularCell.appendChild(wrapperCell);

						row.appendChild(regularCell);
					}

					

				}
				// append row to table
				table.appendChild(row);

			}

		})();




		// draw publisher assignments
		// NO SUCH THING AS AN EMPTY TIMESLOT
		(function drawPublisherAssignments() {
			// go through each day
			var timeSlotsInDay;
			var hours;
			var mins;
			var timeSlotStartTime;
			var elementInAssignment;
			var divSlot;
			var numberOfPublishers;
			var assignmentDiv;
			var textDiv;
			var publisher;
			var color;
			var counter = 0;
			var initialsText;

			for(var i = 0; i < daysInWeek.length; i++) {
				timeSlotsInDay = daysInWeek[i]["timeSlots"];
				// go through each timeSlot
				if(timeSlotsInDay != null && timeSlotsInDay.length > 0) {
					for(var j = 0; j < timeSlotsInDay.length; j++) {
						//inside timeSlot
						hours = timeSlotsInDay[j]["startTime"][0];
						mins = timeSlotsInDay[j]["startTime"][1];
						timeSlotStartTime = hours + ":" + mins;
						mins < 10 ? timeSlotStartTime += "0": null;
						// find element by id...
						console.log(daysInWeek[i]["weekday"] + timeSlotStartTime);
						// get cell... but you still need to get wrapper inside
						elementInAssignment = document.getElementById(daysInWeek[i]["weekday"] + timeSlotStartTime);
						//divSlot = elementInAssignment.childNodes[0];
						console.log(elementInAssignment);

						// how many publishers are in this slot??
						numberOfPublishers = timeSlotsInDay[j]["publishers"].length;

						for(var k = 0; k < numberOfPublishers; k++) {
							counter = 0;
							// draw div
							assignmentDiv = document.createElement("div");
							assignmentDiv.className = "data1";
							assignmentDiv.style.height = "50px";

							publisher = timeSlotsInDay[j]["publishers"][k];

							// find publisher's color
							// if it doesn't exist, this is first slot
							// --> pick next available color and save it
							color = personToColor.find(function(elem) {
								if(elem[0] == (publisher["firstName"] + publisher["lastName"])) {
									// color exists
									console.log("COLOR EXISTS");

									return elem[1];

								}

								counter++;
							});
							console.log("COLOR: " + color);
							console.log("counter:" + counter);
							console.log("Colors saved: " + personToColor.length);

							// if publisher does not have assigned color
							if(typeof(color) == "undefined") {
								console.log("IN UNDEFINED");
								color = colors[counter];
								personToColor.push([publisher["firstName"] + publisher["lastName"], color]);
							} else {
								color = color[1];
							}
							text = document.createTextNode(publisher["firstName"][0] + publisher["lastName"][0]);
							assignmentDiv.style.width = (100/numberOfPublishers) + "%";
							console.log(color);
							assignmentDiv.style.backgroundColor = color;
							textDiv = document.createElement("div");
							textDiv.className = "data3";
							textDiv.appendChild(text);
							assignmentDiv.appendChild(textDiv);
							if(k == 0) {
								divSlot = document.createElement("div");

								divSlot.className = "slotWrapper"
							}	
							divSlot.appendChild(assignmentDiv);

							elementInAssignment.appendChild(divSlot);

						}

						// save divSlot (wrapper div) in array so it can later be dynamically removed from/added to DOM
						arrayScheduleElements.push([daysInWeek[i]["weekday"] + timeSlotStartTime, divSlot]);
						
						//send entire timeSlot for form to be created and appended to array
						createFormDivs(timeSlotsInDay[j], daysInWeek[i]["weekday"] + timeSlotStartTime);
					}
				}
			}

			console.log("ARRAY_SCHEDULE_ELEMENTS_ARRAY: " + arrayScheduleElements);

		})();


		function findDrawSlot(dayAndTime) {
			var counter = 0;
			var slotWrapper = arrayScheduleElements.find(function(elem) {
				counter++;
				return elem[0] == dayAndTime;
			});

			return [slotWrapper, counter];
		}


		function drawSlot(timeSlot) {

			var hours;
			var mins;
			var timeSlotStartTime;
			var divSlot;
			var numberOfPublishers;
			var assignmentDiv;
			var textDiv;
			var publisher;
			var color;
			var text;
			var counter = 0;

			hours = timeSlot["startTime"][0];
			mins = timeSlot["startTime"][1];
			timeSlotStartTime = hours + ":" + mins;
			mins < 10 ? timeSlotStartTime += "0": null;

			// how many publishers are in this slot??
			numberOfPublishers = timeSlot["publishers"].length;

			for(var k = 0; k < numberOfPublishers; k++) {
				counter = 0;
				// draw div
				assignmentDiv = document.createElement("div");
				assignmentDiv.className = "data1";
				assignmentDiv.style.height = "50px";

				publisher = timeSlot["publishers"][k];

				// find publisher's color
				// if it doesn't exist, this is first slot
				// --> pick next available color and save it
				color = personToColor.find(function(elem) {
					if(elem[0] == (publisher["firstName"] + publisher["lastName"])) {
						// color exists
						console.log("COLOR EXISTS");
	
						return elem[1];

					}
					counter++;
				});
				
				console.log("COLOR: " + color);

				// if publisher does not have assigned color
				if(typeof(color) == "undefined") {
					console.log("IN UNDEFINED");
					color = colors[counter];
					personToColor.push([publisher["firstName"] + publisher["lastName"], color]);
				} else {
					color = color[1];
				}
				
				text = document.createTextNode(publisher["firstName"][0] + publisher["lastName"][0]);
				assignmentDiv.style.width = (100/numberOfPublishers) + "%";
				console.log(color);
				assignmentDiv.style.backgroundColor = color;
				textDiv = document.createElement("div");
				textDiv.className = "data3";
				textDiv.appendChild(text);
				assignmentDiv.appendChild(textDiv);
				if(k == 0) {
					// if first to be added...
					divSlot = document.createElement("div");

					divSlot.className = "slotWrapper"
				}	
				divSlot.appendChild(assignmentDiv);


			}

			return divSlot;
		}








		function createFormDivs(timeSlotObj, id) {
			// expects a timeSlot Object
			// called by drawPublisherAssignments()

			var editWrapper = document.createElement("div");
			editWrapper.className = "editWrapper";

			var rowDiv;
			var nameDiv;
			var selectElem = document.createElement("select");
			selectElem.className = "selectName";

			var rowDivAdd = document.createElement("div");
			rowDivAdd.className = "row";

			var nameDivAdd = document.createElement("div");
			nameDivAdd.className = "inputName";



			var buttonDiv;

			// one add button per form
			var imageElementAdd = document.createElement("img");
			imageElementAdd.src = "<c:url value="/resources/PLUS.png" />";
			imageElementAdd.width = "15";
			imageElementAdd.length = "15";


			// multiple delete buttons
			var imageElementDelete;


			var name;
			var namesOfPublishersInSlot = [];
			var text;


			var publishers = timeSlotObj["publishers"];
			var maxPublishers = timeSlotObj["maxPublishers"];

			var existingDrawSlot;


			// add event listener to image

			imageElementAdd.addEventListener("click", function(event) {
				console.log("clickAddEvent");
				var elementClicked = event.target;
				var parentDiv = elementClicked.parentElement;
				var parentRow = parentDiv.parentElement;
				var parentContainer = parentRow.parentElement;
				var nameElement = null;
				var selectedElem = null;
				var divChildren = parentRow.childNodes;
				var selectedIndex;
				var text;
				var addFirst;
				var addLast;
				var publisherObject;

				for(var i = 0; i < divChildren.length; i++) {
				
					if(divChildren[i].className = "name") {
						nameElement = divChildren[i];
						break;
					}
				}

				if(nameElement == null) {
					console.log("No text element found");
				}

				// extract select div
				selectedElem = nameElement.getElementsByClassName("selectName")[0];
				selectedIndex = selectedElem.selectedIndex;
				text = selectedElem.options[selectedIndex].textContent;
				text = text.split(" ");
				firstName = text[0];
				lastName = text[1];

				console.log("ADD PUBLISHER");
				console.log(firstName + lastName);

				// grab publisher to add...
				publisherObject = availablePublishers.find(function(element) {
					return element["firstName"] == firstName && element["lastName"] == lastName;
				});

				console.log(publisherObject);


				// add publisher to json
				publishers.push(publisherObject);
				console.log(source);

				// add publisher to namesOfPublishersInSlot
				// this is dine in function that adds the corresponding row
				//namesOfPublishersInSlot.push(firstName + " " + lastName);


		

				// save current form into form array
				// no need... live view object modified original
				console.log(arrayEditElements);
				console.log(namesOfPublishersInSlot);

				// add new row for added publishers
				rowForPublishersInSlot(publisherObject);

				// refresh select element
				// show only if maxPublishers hasn't been reached
				if(namesOfPublishersInSlot.length < maxPublishers) {
					parentContainer.removeChild(parentRow);
					selectedElem.options.remove(selectedIndex);
					parentContainer.appendChild(parentRow);
				} else {
					// remove entire row from parent div
					parentContainer.removeChild(parentRow);
				}

				// construct new draw slot div
				existingDrawSlot = findDrawSlot(id);

				// if undefined, no drawslot exists
				console.log("ADD - find draw slot");
				console.log(existingDrawSlot);
				if(typeof existingDrawSlot[0] == "undefined") {
					// add new drawSlot to  arrayScheduleElements
					console.log("pushing: " + id);
					arrayScheduleElements.push([id, drawSlot(timeSlotObj)]);
					console.log(arrayScheduleElements.length);
				} else {
					// replace drawSlot

					existingDrawSlot[0][1] = drawSlot(timeSlotObj);

				}


				event.stopPropagation();


			
			});


			
			function rowForPublishersInSlot(publisher) {
				rowDiv = document.createElement("div");
				rowDiv.className = "row";

				nameDiv = document.createElement("div");
				nameDiv.className = "name";

				buttonDiv = document.createElement("div");
				buttonDiv.className = "button";

				name = publisher["firstName"] + " " + publisher["lastName"];
				console.log("created row for: " + name);
				namesOfPublishersInSlot.push(name);

				text = document.createTextNode(name);

				nameDiv.appendChild(text);

				imageElementDelete = document.createElement("img");
				imageElementDelete.src = "<c:url value="/resources/DELETEBUtton.png" />";
				imageElementDelete.width = "15";
				imageElementDelete.length = "15";


				imageElementDelete.addEventListener("click", function(event) {
					console.log("clickDeleteEvent");
					var elementClicked = event.target;
					console.log(elementClicked);
					var parentDiv = elementClicked.parentElement; //class=button
					var parentRow = parentDiv.parentElement; //class=row
					var parentContainer = parentRow.parentElement; //class=editWrapper
					var dataCell = parentContainer.parentElement; //class=data
					var nameElement = null;
					var firstName;
					var lastName;
					var nameArray;
					var divChildren = parentRow.childNodes; //div name and button
					var otherRows = parentContainer.childNodes;

					for(var i = 0; i < divChildren.length; i++) {
				
						if(divChildren[i].className = "name") {
							nameElement = divChildren[i];
							break;
						}
					}

					if(nameElement == null) {
						console.log("No text element found");
						return;
					}
					// save firstName and lastName
					nameArray = nameElement.textContent.split(" ");
					firstName = nameArray[0];
					lastName = nameArray[1];
					

					// remove entire row from parent div
					parentContainer.removeChild(parentRow);



					// change corresponding json
					var elementToBeDeleted;
					var indexOfRemovedElem;

					// find element
					for(indexOfRemovedElem = 0; indexOfRemovedElem < publishers.length; indexOfRemovedElem++) {
						console.log("looking for publisher json");
						if (publishers[indexOfRemovedElem]["lastName"] == lastName && publishers[indexOfRemovedElem]["firstName"] == firstName) {
							console.log("publisher json found");
							elementToBeDeleted = publishers[indexOfRemovedElem];
							break;
						}
					}

					// delete from publishers using splice
					console.log("PUBIIIII SIZE : " + publishers.length);
					publishers.splice(indexOfRemovedElem, 1);
					console.log("PUBIIIII AFTERR SIZE : " + publishers.length);

					// delete from namesOfPublishersInSlot array that will be used later...
					var indexPubsInSlot;
					var elemInPubsInSlot;
					for(indexPubsInSlot = 0; indexPubsInSlot < namesOfPublishersInSlot.length; indexPubsInSlot++) {


						if (namesOfPublishersInSlot[indexPubsInSlot] == firstName + " " + lastName) {
							elemInPubsInSlot = namesOfPublishersInSlot[indexPubsInSlot];
							console.log("breaking......");
							break;
						}
					}

					console.log("indexPubsInSlot: " + indexPubsInSlot);
					namesOfPublishersInSlot.splice(indexPubsInSlot, 1);




					console.log("JSON: ");
					console.log(source);

					// add new row for adding publisher
					// first delete current select object

					// delete select from parent container (DOM) if it exists
					var selectInexistence = parentContainer.getElementsByClassName("selectName");
					if(selectInexistence.length != 0){
						console.log(rowDivAdd);
						console.log(parentContainer);
						parentContainer.removeChild(rowDivAdd);
					}
					selectElem = document.createElement("select");
					selectElem.className = "selectName";

					var optionElement;
					var optionText;
					var couldAddPublisherFirstName;
					var couldAddPublisherLastName;
					var k;



					for(var j = 0; j < availablePublishers.length; j++) {
						couldAddPublisherFirstName = availablePublishers[j]["firstName"];
						couldAddPublisherLastName = availablePublishers[j]["lastName"];

						for(k = 0; k < namesOfPublishersInSlot.length; k++) {
							console.log("LENG: " + namesOfPublishersInSlot.length);
							console.log(namesOfPublishersInSlot[k]);
							if(namesOfPublishersInSlot[k] == couldAddPublisherFirstName + " " + couldAddPublisherLastName) {
								console.log("IN SELECT - excluding name");
								break;
							}
						}

						// if break happened... don't include name in option menu
						// if break didn't happen, add option
						if(k == namesOfPublishersInSlot.length) {
							optionElement = document.createElement("option");
							optionText = document.createTextNode(couldAddPublisherFirstName + " " + couldAddPublisherLastName);
							optionElement.appendChild(optionText);
							selectElem.appendChild(optionElement);

						} else {
								// do nothing... continue to next approved publisher
						}
					}

					selectElem.addEventListener("click", function(elemPicked) {
						console.log("inner stop");

						elemPicked.stopPropagation();
					});

					rowDivAdd = document.createElement("div");
					rowDivAdd.className = "row";

					nameDivAdd = document.createElement("div");
					nameDivAdd.className = "inputName";

					nameDivAdd.appendChild(selectElem);
					nameDivAdd.appendChild(imageElementAdd);
					rowDivAdd.appendChild(nameDivAdd);

					parentContainer.appendChild(rowDivAdd);

					// save current form into form array
					var originalEditElement = arrayEditElements.find(function(elem) {
							return elem[0] == dataCell.id;
						});

					console.log("ORIGINAL EDIT ELEMENT: " + originalEditElement[1]);

					originalEditElement[1] = parentContainer;

					console.log("NEW ELEMERT : " + originalEditElement[1]);


					// construct new draw slot div
					existingDrawSlot = findDrawSlot(dataCell.id);

					// one MUST EXIST
					// if no publishers... delete existing slot;
					if(namesOfPublishersInSlot.length < 1) {
						arrayScheduleElements.splice(existingDrawSlot[1] - 1,1);
						console.log("deleting WHOLE EMPTY SLOT");

					} else {
						existingDrawSlot[0][1] = drawSlot(timeSlotObj);
					}

					event.stopPropagation();

				});

				buttonDiv.appendChild(imageElementDelete);
				console.log("appended: ");
				console.log(imageElementDelete);

				rowDiv.appendChild(nameDiv);
				rowDiv.appendChild(buttonDiv);

				editWrapper.appendChild(rowDiv);

			}

			// add row for publishers in slot with delete button
			for(var i = 0; i < publishers.length; i++) {
				rowForPublishersInSlot(publishers[i]);
			}

			// if there is space for more publishers...
			// only add one row 

			var optionElement;
			var optionText;
			var couldAddPublisherFirstName;
			var couldAddPublisherLastName;
			var k;
			console.log("DECIDING ADD BUTTON");
			console.log("publishers length: " + publishers.length + "    maxPublishers: " + maxPublishers);
			if(publishers.length < maxPublishers) {
				rowDivAdd = document.createElement("div");
				rowDivAdd.className = "row";

				nameDiv = document.createElement("div");
				nameDiv.className = "inputName";

				for(var j = 0; j < availablePublishers.length; j++) {
					couldAddPublisherFirstName = availablePublishers[j]["firstName"];
					couldAddPublisherLastName = availablePublishers[j]["lastName"];

					for(k = 0; k < namesOfPublishersInSlot.length; k++) {
						console.log(couldAddPublisherFirstName + couldAddPublisherLastName);
						if(namesOfPublishersInSlot[k] == couldAddPublisherFirstName + " " + couldAddPublisherLastName) {
							console.log("IN SELECT - excluding name");

							break;
						}
					}

					// if break happened... don't include name in option menu
					// if break didn't happen, add option
					if(k == namesOfPublishersInSlot.length) {
						optionElement = document.createElement("option");
						optionText = document.createTextNode(couldAddPublisherFirstName + " " + couldAddPublisherLastName);
						optionElement.appendChild(optionText);
						selectElem.appendChild(optionElement);

					} else {
						// do nothing... continue to next approved publisher
					}
				}

				selectElem.addEventListener("click", function(elemPicked) {
					console.log("outer stop");
					elemPicked.stopPropagation();
				})
				// add selectElem div to nameDiv
				nameDiv.appendChild(selectElem);


				// create add button
				buttonDiv = document.createElement("div");
				buttonDiv.class = "button";

				buttonDiv.appendChild(imageElementAdd);

				rowDivAdd.appendChild(nameDiv);
				rowDivAdd.appendChild(buttonDiv);

				editWrapper.appendChild(rowDivAdd);
			}




			arrayEditElements.push([id, editWrapper]);
			
		}

		

		//******** make div clickable;
		(function makeDivsClickable() {
			// regularCell.id = daysInWeek[j - 1]["weekday"] + timesInWeek[i-1];

			// elements of class "data" are clickable/editable cells
			var clickableDivs = document.getElementsByClassName("data");
			console.log(clickableDivs.length);
			var clickableDiv;

			for(var i = 0; i < clickableDivs.length; i++) {
				// add event listener for click event
				clickableDiv = clickableDivs[i];

				clickableDiv.addEventListener("click", function() {
					console.log("CLICKED: " + this.id);
					var childrenOfWrapper = this.childNodes;
					var showingSlots = true;
					var dayAndTime = this.id;
					var slotWrapper;
					var emptyTimeSlotObject;
					var dayString;
					var dayObj;
					var startTimeString;
					var startTimeArray;
					var endTimeHour;
					var endTimeMinutes;
					var endTimeArray;
					var maxPublishersNum;
					// if it has div of class slotWrapper; remove it
					if (childrenOfWrapper.length > 0) {
						console.log("Has children");
						if(childrenOfWrapper[0].className == "slotWrapper") {
							console.log("previously SHOWING slots");
							showingSlots = true;
							// remove current slotWrapper
							this.removeChild(arrayScheduleElements.find(function(elem) {
								return elem[0] == dayAndTime;
							})[1]);

							// add corresponding form editWrapper in its place
							this.appendChild(arrayEditElements.find(function(elem) {
								return elem[0] == dayAndTime;
							})[1]);
						} else if(childrenOfWrapper[0].className == "editWrapper") {
							console.log("previously SHOWING editForm");
							showingSlots = false;

							// remove current editWrapper
							this.removeChild(arrayEditElements.find(function(elem) {
								return elem[0] == dayAndTime;
							})[1]);

							// search for slotWrapper
							// if it doesn't exist, do nothing (editWrapper has already been removed)
							slotWrapper = arrayScheduleElements.find(function(elem) {
								return elem[0] == dayAndTime;
							});

							console.log(slotWrapper);


							if(typeof slotWrapper != "undefined") {
								// if it does exist, append

								// add corresponding slotWrapper in its place
								this.appendChild(slotWrapper[1]);
							}

						}
						
					} else {
						// it's an empty slot - it should be showing slotWrapper, but it doesn't exist
						console.log("No children");
						emptyTimeSlotObject = {
							"id":null,
							"dayId":null,
							"startTime": [],
							"endTime": [],
							"maxPublishers": null,
							"publishers": []
						};

						console.log(emptyTimeSlotObject);
						dayString = dayAndTime.match(/[A-Z]*/)[0];
						console.log(dayString);
						startTimeString = dayAndTime.match(/[0-9]*:[0-9]*/)[0];
						startTimeArray = startTimeString.split(":");
						startTimeArray[0] = (new Number(startTimeArray[0])).valueOf();
						startTimeArray[1] = (new Number(startTimeArray[1])).valueOf();
						console.log(startTimeArray);


						// get day for this timeSlot
						dayObj = daysInWeek.find(function(element) {
							return element["weekday"] == dayString;
						});

						console.log(dayObj);

						maxPublishersNum = dayObj["defaultMaxPublishers"];
						
						console.log(maxPublishersNum);

						// calculate hour
						endTimeHour = startTimeArray[0];
						endTimeMinutes = (dayObj["duration"] / 60) + startTimeArray[1];

						if(endTimeMinutes >= 60) {
							endTimeHour += endTimeMinutes / 60;
							endTimeMinutes = endTimeMinutes % 60;

						}

						endTimeArray = [endTimeHour, endTimeMinutes];


						console.log(endTimeArray);

						emptyTimeSlotObject["startTime"] = startTimeArray;
						emptyTimeSlotObject["endTime"] = endTimeArray;
						emptyTimeSlotObject["maxPublishers"] = maxPublishersNum;

						console.log("FINISHED OBJ template");
						console.log(emptyTimeSlotObject);

						// create form , which saves it to form array
						createFormDivs(emptyTimeSlotObject, dayAndTime);

						// add empty slot to JSON source - it will be deleted if empty (by submit validator)
						dayObj["timeSlots"].push(emptyTimeSlotObject);

						// show form
						showingSlots = true;
						this.appendChild(arrayEditElements.find(function(elem) {
								return elem[0] == dayAndTime;
						})[1]);
					}


				});

			}

		})();


		function validateJSON(input) {

			var dayArray = input["week"];
			var timeSlotsArray;
			var timeSlotV;



			for(var i = 0; i < dayArray.length; i++) {
				timeSlotsArray = dayArray[i]["timeSlots"];

				// in each timeSlot...
				for(var j = timeSlotsArray.length - 1; j > 0; j--) {
					timeSlotV = timeSlotsArray[j];
					if(timeSlotV["publishers"].length < 1) {
						// remove
						timeSlotsArray.splice(j, 1);
					}
				}
			}

			console.log("AFTER:: ");
			console.log(input);

		}


		(function submitButton() {
			var submitDiv = document.createElement("div");
			submitDiv.className = "submitDiv";

			submitDiv.addEventListener("click", function(event) {
				console.log("*********");
				console.log(source);
				console.log("*********");
				validateJSON(source);
				var reqPost = new XMLHttpRequest();

				reqPost.open("PUT", "/PPublica/api/week/4", true);
				reqPost.setRequestHeader("Content-Type", "application/json");
				reqPost.send(JSON.stringify(source));
		  		reqPost.addEventListener("load", function() {



		  			var response;
		  			if(reqPost.status >= 300 || reqPost.status < 200) {

		  				response = reqPost.responseText;
		  		  	    console.log(response);
		  		  	    console.log(reqPost.status);
		  		    	document.body.appendChild(document.createTextNode(response));

		  	  		} else {
		  	  			window.location.href="<c:url value="/69thst" />";
		  	  		}

		 

		    	
		  		});

		  		
		  		//console.log(JSON.stringify(source));
		  		

			});

			document.body.appendChild(submitDiv);
			

		})();

	  	});


	  	});



	</script>

	</body>
</html>





