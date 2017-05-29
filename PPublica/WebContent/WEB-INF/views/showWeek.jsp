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
			background-color:orange;
			display:inline-block;
			height:100%;
			margin:0px;
        }
        div.data {
        	display:table-cell;
        	text-align:center;
        	vertical-align:middle;
        	color:white;

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

		var sourceTest = {
			"id":3,
			"info":"test1REST",
			"location":"69thSt",
			"cartId":1,
			"week":[{
				"id":6,
				"weekScheduleId":3,
				"weekday":"MONDAY",
				"minTime":[6,0],
				"maxTime":[20,0],
				"duration":1800.000000000,
				"timeSlots":[{
					"id":1,
					"dayId":6,
					"startTime":[9,0],
					"endTime":[9,30],
					"publishers":[{
						"mySlots":[],
						"id":2,
						"username":"natf",
						"password":"268711454",
						"firstName":"Nathan",
						"lastName":"Farciert"
					},
					{
						"mySlots":[],
						"id":2,
						"username":"natf",
						"password":"268711454",
						"firstName":"Karen",
						"lastName":"Alvarez"
					}],
					"maxPublishers":2
				}]
			},
			{
				"id":6,
				"weekScheduleId":3,
				"weekday":"TUESDAY",
				"minTime":[6,0],
				"maxTime":[20,0],
				"duration":1800.000000000,
				"timeSlots":[{
					"id":1,
					"dayId":6,
					"startTime":[10,0],
					"endTime":[10,30],
					"publishers":[{
						"mySlots":[],
						"id":2,
						"username":"natf",
						"password":"268711454",
						"firstName":"Edith",
						"lastName":"Alvarez"
					},
					{
						"mySlots":[],
						"id":2,
						"username":"natf",
						"password":"268711454",
						"firstName":"Jessica",
						"lastName":"Criollo"
					}],
					"maxPublishers":2
				}]
			}]

		};



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



		// draw each row
		(function drawRows() {
			for(var i = 1; i < timesInWeek.length; i++) {

				row = document.createElement("div");
				row.className = "divRow";

				var regularCell;
				var divCell;
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
					} else {
						// simply draw empty cell
						// with id = day + timeInMinutes
						// although cell is in same row as endTime, it will be identified by its startTime
						regularCell.id = daysInWeek[j - 1]["weekday"] + timesInWeek[i-1];
						regularCell.className = "data";
						console.log(daysInWeek[j - 1]["weekday"] + timesInWeek[i-1]);
					}

					// append element to row
					row.appendChild(regularCell);

				}
				// append row to table
				table.appendChild(row);

			}

		})();






		// draw publisher assignments
		(function drawPublisherAssignments() {
			// go through each day
			var timeSlotsInDay;
			var hours;
			var mins;
			var timeSlotStartTime;
			var elementInAssignment;
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
						elementInAssignment = document.getElementById(daysInWeek[i]["weekday"] + timeSlotStartTime);
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
							textDiv.className = "data";
							textDiv.appendChild(text);
							assignmentDiv.appendChild(textDiv);
							if(k == 0) {
								divSlot = document.createElement("div");

								divSlot.className = "slotWrapper"
							}	
							divSlot.appendChild(assignmentDiv);

							elementInAssignment.appendChild(divSlot);
							

						}
					}
				}
			}

		})();



	  	});


	</script>
	</body>

</html>




