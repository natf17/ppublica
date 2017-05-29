function addSchedule() {
	var weekJson = {
	    	"info": null,
	    	"location": null,
	    	"cartId": null,
	    	"week": []
	    	/*
	      	{
	        	"weekday": "MONDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "TUESDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "WEDNESDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "THURSDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "FRIDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "SATURDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	},
	      	{
	        	"weekday": "SUNDAY",
	        	"minTime": [
	          	6,
	          	0
	        	],
	        	"maxTime": [
	          	20,
	          	0
	        	],
	        	"duration": null,
	        	"defaultMaxPublishers": null
	      	}


	    	*/
	  	}


		// data shared among function... directory of current day objects in existence
		// used to find their span elements by generating their id
		// this is the default
		var currentDays; //= [["Monday", "day1"], ["Tuesday", "day2"], ["Wednesday", "day3"], ["Thursday", "day4"], ["Friday", "day5"], ["Saturday", "day6"], ["Sunday", "day7"]];
		var order; //= [["Monday", "day1"], ["Tuesday", "day2"], ["Wednesday", "day3"], ["Thursday", "day4"], ["Friday", "day5"], ["Saturday", "day6"], ["Sunday", "day7"]];
		var tempDefaults;
		var deletedDays = [];

		// by default, addNewDay is disabled
		// prefix:add
		var addNewDay = ["add", "disabled"];


	/*

		// read current days... generate directory
		// called when a day gets deleted or added by user
		function repopulateDayDirectory() {
			// clear the currentDays array;
			currentDays = [];

			var dayContainerElems = document.getElementsByClassName("dayContainer");
			var groupForDirectory;
			var dayElem;
			var transitElem;
			var dayName;
			var dayDenom;

			console.log(dayContainerElems.length);


			for(var i = 0; i < dayContainerElems.length; i++) {
				// get day child
				dayElem = dayContainerElems[i].childNodes;
				groupForDirectory = [];


				for(var j = 0; j < dayElem.length; j++) {
					if (dayElem[j].className == "day") {
						dayElem = dayElem[j];
						break;
					}
				}



				// get weekDay child
				transitElem = dayElem.childNodes;

				for(var j = 0; j < transitElem.length; j++) {
					if (transitElem[j].className == "weekDay") {
						transitElem = transitElem[j];
						break;
					}
				}

				// get span child
				transitElem = transitElem.childNodes;

				for(var j = 0; j < transitElem.length; j++) {
					if (transitElem[j].tagName == "SPAN") {
						transitElem = transitElem[j];
						break;
					}
				}

				dayName = transitElem.innerHTML;



				// get dayInfoContainer child
				transitElem = dayElem.childNodes;

				for(var j = 0; j < transitElem.length; j++) {
					if (transitElem[j].className == "dayInfoContainer") {
						transitElem = transitElem[j];
						break;
					}
				}

				dayDenom = transitElem.id;
				

				groupForDirectory.push(dayName, dayDenom);
				currentDays.push(groupForDirectory);


			}

			console.log(currentDays);



		}
	*/
		function onChangeListener() {
			console.log("Listener called... " + this.id);
			console.log("Current value " + this.value);

			var suffixCorrespondingElementsId = (this.id).replace('temp', "");
			var correspondingElementsId;
			var correspondingSelectElem;
			var event = new Event('change');

			// iterate through directory to generate prefixes...
			var prefixCorrespondingElementsId;
			for(var i = 0; i < currentDays.length; i++) {
				prefixCorrespondingElementsId = currentDays[i][1];
				correspondingElementsId = prefixCorrespondingElementsId + suffixCorrespondingElementsId;
				console.log("Updating select with id " + correspondingElementsId);

				correspondingSelectElem = document.getElementById(correspondingElementsId);
				correspondingSelectElem.value = this.value;
				correspondingSelectElem.dispatchEvent(event);

			}
			// if new day exists... update
			if(addNewDay[1] == "enabled") {
				prefixCorrespondingElementsId = addNewDay[0];
				correspondingElementsId = prefixCorrespondingElementsId + suffixCorrespondingElementsId;
				console.log("Updating ADDDDDDD select with id " + correspondingElementsId);

				correspondingSelectElem = document.getElementById(correspondingElementsId);
				correspondingSelectElem.value = this.value;
			}



		}

		function addListenersToSelect(selectElem) {


			var defaultValue;


			selectElem.onchange = onChangeListener;

			// enable a default value for this template
		}


		// receives a span element and adds listeners to each row
		function addListenersToRow(spanElement) {
			// string: 'temp' + 'object/property' + 'field'
			// ex: tempMinTimeHrs
			var selectId;

			/*
			var stringPropertyParts;
			var stringCorrespondingDayProperty;

			*/

			var selectElemsArray = [];
			var tempElems;
			var selectElem;

			// grab each select element
			tempElems = spanElement.childNodes;

			// determine what property in WeekSchedule this row is for...

			
			for(var i = 0; i < tempElems.length; i++) {
				if(tempElems[i].tagName == "SELECT") {
					selectElemsArray.push(tempElems[i]);
				}
			}


			// what Day property is this for?
			var selectId = selectElemsArray[0].id;
			stringPropertyParts = selectId.split('-');

			console.log(stringPropertyParts);

			// the second property should always display the Day property this row is for
			//stringCorrespondingDayProperty = stringPropertyParts[1];


			for(var i = 0; i < selectElemsArray.length; i++) {
				selectElem = selectElemsArray[i];
				addListenersToSelect(selectElem);
			}
			


		}




		function itsPm(hoursInput) {
			var finalVal = parseInt(hoursInput) + 12;

			if(finalVal == 24) {
				finalVal = 12; // 12 PM is actually [12,00]
			}

			return finalVal;
		}

		function itsAm(hoursInput) {
			var finalVal = parseInt(hoursInput);

			if(finalVal == 12) {
				finalVal = 0; // 12 AM is actually [0,0]
			}

			return finalVal;
		}

		// args: a string representing the name of the field property and its corresponding Json
		function timeListener() {

			var thisSelectsParent = this.parentElement;
			console.log(thisSelectsParent);

			var selectFields = thisSelectsParent.getElementsByTagName("SELECT");

			var selectElemHrs;
			var selectElemMins;
			var selectElemPeriod;


			var propertyNeeded;
			var aSelectField;
			for(var i = 0; i < selectFields.length; i++) {
				aSelectField = selectFields[i];
				console.log(aSelectField);
				propertyNeeded = ((aSelectField.id).split('-'))[2]; // Hrs, Mins, Period

				if(propertyNeeded == "Hrs") {
					selectElemHrs = selectFields[i];

				}
				else if (propertyNeeded == "Mins"){
					selectElemMins = selectFields[i];

				}
				else if (propertyNeeded == "Period"){
					selectElemPeriod = selectFields[i];
		
				}
				else {
					console.log("*******UNKNOWN fieldName******");

				}
		
			}

			var thisIdPieces = ((this.id).split('-'));

			var fieldNameProperty = thisIdPieces[2];
			var fieldName = thisIdPieces[1];
			var day = thisIdPieces[0];

			// use previous info to locate corresponding json fragment........
			var thisDay;
			for(var i = 0; i < currentDays.length; i++) {
				if(currentDays[i][1] == day) {
					thisDay = currentDays[i][0];
					break;
				}

			}

			if(!thisDay) {
				console.log("******* DAY NOT FOUND IN DIRECTORY******");

			} else {
				thisDay = thisDay.toUpperCase();
			}

			var daysJson = weekJson["week"];
			var thisDayJson;
			for(var i = 0; i < daysJson.length; i++) {
				if(daysJson[i]["weekday"] == thisDay) {
					thisDayJson = daysJson[i];
					break;
				}

			}


			if(!thisDayJson) {
				console.log("******* DAY NOT FOUND IN JSON******");
				return;
			}




			var hrsValue = parseInt(selectElemHrs.value);
			var minsValue = parseInt(selectElemMins.value);
			var periodValue = selectElemPeriod.value;

			var currentValue = this.value;

			var finalValue;
			// is this an hour field or minute field or period field?



			var fieldJson;


			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1, fieldName.length);
			fieldJson = thisDayJson[fieldName];




			if(fieldNameProperty == "Hrs") {
				if(periodValue == "PM") {
					finalValue =  itsPm(currentValue);	
					
				} else if(periodValue == "AM") {

					finalValue = itsAm(currentValue);

				}

				fieldJson[0] = parseInt(finalValue);
			}
			else if(fieldNameProperty == "Mins") {
				finalValue = currentValue;
				fieldJson[1] = parseInt(finalValue);

			}
			else if(fieldNameProperty == "Period") {
				if(currentValue == "PM") {
					finalValue = itsPm(hrsValue);

				} else if(periodValue == "AM") {

					finalValue = itsAm(hrsValue);
				}

				fieldJson[0] = parseInt(finalValue);

		
			} else {
				console.log("*******UNKNOWN fieldNameProperty******");
			}

			console.log("I WAS clicked...");
			console.log(fieldJson);
			console.log(weekJson);

		}

		function durationListener() {
			var thisSelectsParent = this.parentElement;
			console.log(thisSelectsParent);

			var selectFields = thisSelectsParent.getElementsByTagName("SELECT");

			var thisIdPieces = ((this.id).split('-'));

			var fieldNameProperty = thisIdPieces[2];
			var fieldName = thisIdPieces[1]; // should be "duration"

			var day = thisIdPieces[0];

			// use previous info to locate corresponding json fragment........
			var thisDay;
			for(var i = 0; i < currentDays.length; i++) {
				if(currentDays[i][1] == day) {
					thisDay = currentDays[i][0];
					break;
				}

			}

			if(!thisDay) {
				console.log("******* DAY NOT FOUND IN DIRECTORY******");

			} else {
				thisDay = thisDay.toUpperCase();
			}

			var daysJson = weekJson["week"];
			var thisDayJson;
			for(var i = 0; i < daysJson.length; i++) {
				if(daysJson[i]["weekday"] == thisDay) { // find this day object (all caps)
					thisDayJson = daysJson[i];
					break;
				}

			}


			if(!thisDayJson) {
				console.log("******* DAY NOT FOUND IN JSON******");
				return;
			}


			var currentValue = this.value;


			var fieldJson;


			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1, fieldName.length);

			
			thisDayJson[fieldName] = parseInt(currentValue) * 60; // objects stores duration in seconds


			console.log("I WAS clicked...");
			console.log(weekJson);
		}


		function defaultMaxPublishersListener() {

			var thisIdPieces = ((this.id).split('-'));

			var fieldNameProperty = thisIdPieces[2];
			var fieldName = thisIdPieces[1]; // should be "defaultMaxPublishers"

			var day = thisIdPieces[0];

			// use previous info to locate corresponding json fragment........
			var thisDay;
			for(var i = 0; i < currentDays.length; i++) {
				if(currentDays[i][1] == day) {
					thisDay = currentDays[i][0];
					break;
				}

			}

			if(!thisDay) {
				console.log("******* DAY NOT FOUND IN DIRECTORY******");

			} else {
				thisDay = thisDay.toUpperCase();
			}

			var daysJson = weekJson["week"];
			var thisDayJson;
			for(var i = 0; i < daysJson.length; i++) {
				if(daysJson[i]["weekday"] == thisDay) { // find this day object (all caps)
					thisDayJson = daysJson[i];
					break;
				}

			}


			if(!thisDayJson) {
				console.log("******* DAY NOT FOUND IN JSON******");
				return;
			}


			var currentValue = this.value; // should be integer


			var fieldJson;


			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1, fieldName.length);


			
			thisDayJson[fieldName] = parseInt(currentValue);


			console.log("I WAS clicked...");
			console.log(weekJson);
		}

		// args: select fields - which holds all info for a Day property + corresponding Json field
		function addListenerToFieldInDay(selectFields, fieldJson) {

			var selectIdPieces = ((selectFields[0]).id).split('-');

			var fieldName = selectIdPieces[1];
			var fieldNameProperty;
			var thisSelectField;

		
			// trigger onchane once to set json to current value...
			var event = new Event('change');


			var listenerFunction;
			// what kind of field is it?


			if((fieldName == "MinTime") || (fieldName == "MaxTime")) {
				
				for(var i = 0; i < selectFields.length; i++) {
					thisSelectField = selectFields[i];

					thisSelectField.onchange = timeListener;
					thisSelectField.dispatchEvent(event);


				}
			} 


			else if (fieldName == "Duration") {
				console.log("setting duration");
				

				for(var i = 0; i < selectFields.length; i++) {
					thisSelectField = selectFields[i];
					thisSelectField.onchange = durationListener;
					thisSelectField.dispatchEvent(event);

				}
				
			} else if(fieldName == "DefaultMaxPublishers") {
				console.log("setting defaultMaxPublishers");


				thisSelectField = selectFields[0]; //
				thisSelectField.onchange = defaultMaxPublishersListener;
				thisSelectField.dispatchEvent(event);



			} else {
				console.log("*******UNKNOWN fieldName******");
				console.log(fieldName);
			}



		}

		// args: dayInfoRow that corresponds to day property and pointer to day object in json
		function addListenersToRowDaySchedule(dayInfoRow, dayObjectJsonRef) {
			// what is the name of the property?
			var selectField = dayInfoRow.getElementsByTagName("SELECT")[0];
			var id = selectField.id;
			var idParts = id.split('-');
			var fieldName = idParts[1];

			// make first character lower case
			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1, fieldName.length);

			var fieldJson = dayObjectJsonRef[fieldName];

			addListenerToFieldInDay(dayInfoRow.getElementsByTagName("SELECT"), fieldJson);
		}

		// expects a dayContainerObject
		function addListenersToDaySchedule(dayContainerDiv) {
			console.log("Found dayContainerDiv");

			// get dayOfWeek
			var dayOfWeek;
			var temp = dayContainerDiv.getElementsByClassName("weekDay")[0];
			var newDay = {
	        	"weekday": "NEW",
	        	"minTime": [0,0],
	        	"maxTime": [0,0],
	        	"duration": 0,
	        	"defaultMaxPublishers": 0
	      	};

			dayOfWeek = temp.getElementsByTagName("SPAN")[0].innerHTML;

			console.log(dayOfWeek);

			// this object should not exist in model (JSON)
			var weekArrayJSON = weekJson["week"];
			var currentDayOfWeekInCaps = dayOfWeek.toUpperCase();

			for(var i = 0; i < weekArrayJSON.length; i++) {
				if((weekArrayJSON[i])["weekday"] == currentDayOfWeekInCaps) {
					console.log("THIS DAY ALREADY EXISTS IN JSON");
					return;
				}
			}

			newDay["weekday"] = currentDayOfWeekInCaps;
			// add new day to model;
			weekArrayJSON.push(newDay);

			// add a listener to each row in this day div - which corresponds to a field in the day object
			var dayInfoRows = dayContainerDiv.getElementsByClassName("dayInfoRow");
			console.log(dayInfoRows.length);
			for(var i = 0; i < dayInfoRows.length; i++) {
				addListenersToRowDaySchedule(dayInfoRows[i], newDay);
			}



		}

		// add listeners to fields to update JSON
		function listenersToUpdateJSON() {
			var dayContainerDivs = document.getElementsByClassName("dayContainer");

			for(var i = 0; i < dayContainerDivs.length; i++) {
				addListenersToDaySchedule(dayContainerDivs[i]);
			}

		}



		function enableTemplateFunctionality() {
				// add event listeners to template fields
				var transitElem;
				var spanParentElem;
				var templateElem;
				var rowsArrays = [];

				transitElem = document.getElementsByClassName("templateRow")[0];



				// get dayContainerTemp child
				transitElem = transitElem.childNodes;

				for(var i = 0; i < transitElem.length; i++) {
					if (transitElem[i].className == "dayContainerTemp") {
						transitElem = transitElem[i];
						break;
					}
				}

				// get day child
				transitElem = transitElem.childNodes;

				for(var i = 0; i < transitElem.length; i++) {
					if (transitElem[i].className == "day") {
						transitElem = transitElem[i];
						break;
					}
				}


				// get dayInfoContainer child
				transitElem = transitElem.childNodes;

				for(var i = 0; i < transitElem.length; i++) {
					if (transitElem[i].className == "dayInfoContainer") {
						transitElem = transitElem[i];
						break;
					}
				}


				// get dayInfoRow child
				transitElem = transitElem.childNodes;

				for(var i = 0; i < transitElem.length; i++) {
					if (transitElem[i].className == "dayInfoRow") {
						rowsArrays.push(transitElem[i]);
					}
				}

				for(var k = 0; k < rowsArrays.length; k++) {
					console.log(rowsArrays[k]);
					// get span child
					transitElem = rowsArrays[k].childNodes;

					for(var i = 0; i < transitElem.length; i++) {
						if (transitElem[i].tagName == "SPAN") {
							transitElem = transitElem[i];
							// there is one span element per row
							break;
						}
					}

					spanParentElem = transitElem;

					addListenersToRow(spanParentElem);
				}

		}



		function setTemplateDefaults(defaultValuesObj) {

			// retrieve all select elements in template
			var templateRow = document.getElementsByClassName("templateRow")[0];
			var selectValues = templateRow.getElementsByTagName("SELECT");

			var selectId;
			var defaultValue;
			var event;
			for(var i = 0; i < selectValues.length; i++) {
				selectId = selectValues[i].id;
				defaultValue = defaultValuesObj[selectId];
				defaultValue = defaultValue.toString();

				if(selectId.includes("Mins")) {
					if(0 <= parseInt(defaultValue) &&  parseInt(defaultValue) < 10) {
						defaultValue = "0" + defaultValue;
					}
				}
				selectValues[i].value = defaultValue;
				event = new Event('change');
				selectValues[i].dispatchEvent(event);

			}



		}








		function deleteButtonClickAction() {
			console.log("DELETE CLICKED");

			var parent = (this.parentNode).parentNode; // dayContainer

			var id = (parent.getElementsByClassName("dayInfoContainer")[0]).id;

			console.log(id); // like day3


			// remove from currentDays directory;
			var index;
			var weekDay;
			for(index = 0; index < currentDays.length; index++) {
				if(currentDays[index][1] == id) {
					// extract day
					weekDay = currentDays[index][0];
					break; // found
				}

			}

			if(index == currentDays.length) {
				console.log("ERRORRRRR : day not found to remove from directory");
				console.log(id);
				console.log(currentDays);
				return;
			} 

			var day = currentDays[index][0]; // first extract some useful information;

			currentDays.splice(index,1);



			// add this day info to deletedDays array
			// in proper order!!
			deletedDays = orderedInsertDayArrays([day, id], deletedDays, order);

			//deletedDays.push([day, id]);

			console.log(deletedDays);

			// delete this div
			(parent.parentNode).removeChild(parent);

			// call ifPermitAddDay...
			decideIfPermitAddDay(currentDays);

			// remove from JSON
			var week = weekJson["week"];
			var j;

			for(j = 0; j < week.length; j++) {

				if(week[j]["weekday"] == weekDay.toUpperCase()) {
					console.log("TO DELETE FOUND JSON");
					break; // found in JSON
				}

			}

			week.splice(j,1);


			console.log(weekJson);

		}


		function orderedInsertDayArrays(elem, array, order) {
			var target = elem;
			var orderRules = order;
			var targetArray = array;
			var last = targetArray.length;
			var first = 0;

			var positionTarget;
			var postionLast;

			function findInArray(target, array) {
				var position = -1;

				for(var i = 0; i < array.length; i++) {
					if(array[i][0] == target[0] && array[i][1] == target[1]) {
						position = i;
						break;
					}
				}

				return position;

			}


			while(last > first) {
				positionTarget = findInArray(target, order);
				positionLast = findInArray(targetArray[last - 1], order);
				console.log(positionTarget + "     " + positionLast);
				if(positionTarget < positionLast) {
					targetArray[last] = targetArray[last -1];
					last = last - 1;
				} else {
					break;
				}
			}

			targetArray[last] = target;

			return targetArray;


		}
	/*
		function deleteButtonFunctionality() {
			var deleteButtons = document.getElementsByClassName("deleteButton");
			var image;

			for(var i = 0; i < deleteButtons.length; i++) {
				image = (deleteButtons[i].getElementsByTagName("IMG"))[0];

				image.addEventListener("click", deleteButtonClickAction);
			}



		}
	*/
		function addButtonClickAction() {
			// get this button's daydiv
			var parent = this.parentNode; // addButtonDiv
			parent = parent.parentNode; // dayContainerDiv

			// go down DOM nodes
			var dayDiv = parent.getElementsByClassName("dayDiv");




			// get newDayweekDay
			var daySelect = parent.getElementsByTagName("SELECT")[0];
			daySelect = daySelect.options[daySelect.selectedIndex].text;


			var dayContainer = dayContainerFactory(dayDiv, daySelect);


			// delete day from deletedDays array
			var dayId;
			var index;
			for(index = 0; index < deletedDays.length; index++) {
				if(deletedDays[index][0] == daySelect) {
					dayId = deletedDays[index][1];
					break;
				}
			}

			if(index == deletedDays.length) {
				console.log("****** Day not found in deletedDays array ******");
			}


			deletedDays.splice(index, 1);
			console.log(deletedDays);


			// add new day to currentDays array

			currentDays = orderedInsertDayArrays([daySelect, dayId], currentDays, order);

			console.log(currentDays);

			// now that we have a dayContainer, append it to the DOM
			appendCreatedDayToDOM(dayContainer);

			// call ifPermitAddDay...
			decideIfPermitAddDay(currentDays);

			console.log(weekJson);


		}

		function dayContainerFactory(dayDivToCopy, stringDayWeek) {
			var divClass = "dayContainer";
			var deleteButtonClass = "deleteButton";

			var dayContainerDiv = document.createElement("div");
			dayContainerDiv.className = divClass;

			// append deleteButton div
			var deleteButtonDiv = document.createElement("div");
			deleteButtonDiv.className = deleteButtonClass;

			var deleteButtonImg = document.createElement("img");
			deleteButtonImg.src = "DELETEBUtton.png";
			deleteButtonImg.width = "20";
			deleteButtonImg.height = "20";
			deleteButtonImg.addEventListener("click", deleteButtonClickAction);

			deleteButtonDiv.appendChild(deleteButtonImg);

			dayContainerDiv.appendChild(deleteButtonDiv);


			// CREATE day DIV
			var dayDiv;
			var dayDivClass;

		
			dayDivClass = "day";


			if(dayDivToCopy && stringDayWeek) {
				// dayDivToCopy provided
				// copy it to a new dayContainer

				dayDiv = copyCreateDayDiv(dayDivToCopy, stringDayWeek);
				


			}
			else if(stringDayWeek) {
				
				dayDiv = copyCreateDayDiv(null, stringDayWeek);




			}

			dayDiv.className = dayDivClass;


			dayContainerDiv.appendChild(deleteButtonDiv);

			dayContainerDiv.appendChild(dayDiv);

			return dayContainerDiv;


		}

		function copyCreateDayDiv(dayDivToCopy, stringNewDayWeek) {
			var dayDiv = document.createElement("div");
			var dayDivClass = "day";
			dayDiv.className = dayDivClass;

			var weekDayDiv = document.createElement("div");
			var weekDayDivClass = "weekDay";
			weekDayDiv.className = weekDayDivClass;


			var spanElem = document.createElement("span");
			var spanElemId;


			// grab corresponding code from ORDER directory 
			var newDayId;

			for(var i = 0; i < order.length; i++) {
				if((order[i][0]) == stringNewDayWeek) {
					newDayId = order[i][1];
					console.log("+++++++++++++++++ID");
					console.log(newDayId);
					break;
				}
			}

			var weekDayText = document.createTextNode(stringNewDayWeek);
			spanElemId = newDayId + "weekDay";

			spanElem.id = spanElemId;
			spanElem.appendChild(weekDayText);

			weekDayDiv.appendChild(spanElem);

			var dayContainerInfoDiv;
			var dayContainerInfoDivId = newDayId;
			if(dayDivToCopy) {
				// extract values from add-
				dayInfoContainerDiv = createDayInfoContainer(newDayId, "add");

			}
			else {
				// grab values from template
				dayInfoContainerDiv = createDayInfoContainer(newDayId, "temp");

			}


			dayInfoContainerDiv.id = newDayId;
			dayDiv.appendChild(weekDayDiv);
			dayDiv.appendChild(dayInfoContainerDiv);

			return dayDiv;



		}

		function appendCreatedDayToDOM(dayContainer) {
			// this function must be called after currentDays has been updated!!

			var formElement = document.getElementsByTagName("FORM")[0];

			// find current dayContainer position in currentDays array...

			var day = (dayContainer.getElementsByTagName("SPAN"))[0].innerHTML;
			console.log(day);

			var index;
			for(index = 0; index < currentDays.length; index++) {
				if(currentDays[index][0] == day) {
					break;
				}
			}

			if(index == currentDays.length) {
				console.log("***** Could not find day in currendDays array *******");
				formElement.appendChild(dayContainer);

			}

			// get next
			var nextDayContainer = formElement.getElementsByClassName("dayContainer")[index]; // this one will be moved forward once to make way for the new div....
			console.log(index);
			console.log(nextDayContainer);

			formElement.insertBefore(dayContainer,nextDayContainer);




			// add listeners!!
			addListenersToDaySchedule(dayContainer);


		}

		function addAddDayDiv(selectIdPrefix) {
			var divClass = "dayContainerNewDay";
			var addButtonDivClass = "addButton";


			var dayContainerDiv = document.createElement("div");
			dayContainerDiv.className = divClass;



			// append addButton div
			var addButtonDiv = document.createElement("div");
			addButtonDiv.className = addButtonDivClass;

			var addButtonImg = document.createElement("img");
			addButtonImg.src = "PLUS.png";
			addButtonImg.width = "20";
			addButtonImg.height = "20";
			addButtonImg.addEventListener("click", addButtonClickAction);

			addButtonDiv.appendChild(addButtonImg);

			dayContainerDiv.appendChild(addButtonDiv);



			// create day div
			var dayDiv = document.createElement("div");
			dayDiv.className = "day";

			// create day header (selet elem...)
			var weekDayDiv = document.createElement("div");
			weekDayDiv.className = "weekDay";

			var spanElem = document.createElement("span");
			spanElem.id = "newDayweekDay";

			var selectElem = document.createElement("select");

			selectElem = generateOptionsForAddDay(deletedDays);
			selectElem.id = "newDayDay";

			spanElem.appendChild(selectElem);
			weekDayDiv.appendChild(spanElem);
			dayDiv.appendChild(weekDayDiv);


			// create dayInfoContainer
			// delegate to helper function
			var dayInfoContainerDiv = createDayInfoContainer(selectIdPrefix, "temp");

			dayDiv.appendChild(dayInfoContainerDiv);


			dayContainerDiv.appendChild(dayDiv);

			// add whole div to its parent; form class = "inputWeekForm"
			var form = document.getElementsByClassName("inputWeekForm")[0];

			form.appendChild(dayContainerDiv);




		}

		function createDayInfoContainer(selectIdPrefix, defaultValuesElemPrefix) {
			var dayInfoContainerDiv = document.createElement("div");
			dayInfoContainerDiv.className = "dayInfoContainer";

			var fieldNames = [];

			var fieldNamesRow1 = [];
			fieldNamesRow1.push("-MinTime-Hrs");
			fieldNamesRow1.push("-MinTime-Mins");
			fieldNamesRow1.push("-MinTime-Period");

			var fieldNamesRow2 = [];
			fieldNamesRow2.push("-MaxTime-Hrs");
			fieldNamesRow2.push("-MaxTime-Mins");
			fieldNamesRow2.push("-MaxTime-Period");

			var fieldNamesRow3 = [];
			fieldNamesRow3.push("-Duration-Mins");

			var fieldNamesRow4 = [];
			fieldNamesRow4.push("-DefaultMaxPublishers");


			fieldNames.push(fieldNamesRow1);
			fieldNames.push(fieldNamesRow2);
			fieldNames.push(fieldNamesRow3);
			fieldNames.push(fieldNamesRow4);

			var templateValue;
			var templateElem;

			var dayInfoRowDiv;
			var spanElem;
			var selectElem;
			var optionElem;
			var optionText;
			// create 4 rows
			for(var i = 0; i < 4; i++) {
				dayInfoRowDiv = document.createElement("div");
				dayInfoRowDiv.className = "dayInfoRow";

				spanElem = document.createElement("span");

				for(var j = 0; j < (fieldNames[i]).length; j++) {
					selectElem = document.createElement("select");
					selectElem.id = selectIdPrefix + fieldNames[i][j];
					console.log(fieldNames[i][j]);
					if((i == 0 || i == 1) && (j == 0)) {
						// this is an hours field
						for(var k = 1; k < 13; k++) {
							optionElem = document.createElement("option");
							optionText = document.createTextNode(k);

							optionElem.appendChild(optionText);

							selectElem.appendChild(optionElem);

							// set option's value: "temp"
							templateElem = document.getElementById(defaultValuesElemPrefix + fieldNames[i][j]);
							templateValue = templateElem.options[templateElem.selectedIndex].text;
							selectElem.value = templateValue;
							console.log(templateValue);


						}
					}

					else if((i == 0 || i == 1) && (j == 1)) {
						// this is a minutes field
						for(var k = 0; k <= 60; k+=15) {
							optionElem = document.createElement("option");
							if((0 <= k)  && (k < 10)) {
								console.log(k);
								optionText = document.createTextNode("0" + k);

							} else {
								optionText = document.createTextNode(k);

							}

							optionElem.appendChild(optionText);

							selectElem.appendChild(optionElem);

							// set option's value: "temp"
							templateElem = document.getElementById(defaultValuesElemPrefix + fieldNames[i][j]);
							templateValue = templateElem.options[templateElem.selectedIndex].text;
							selectElem.value = templateValue;
							console.log(templateValue);

						}
					} 

					else if(i == 2) {
						// this is a minutes field
						for(var k = 0; k <= 60; k+=15) {
							optionElem = document.createElement("option");
							if(0 <= k  && k < 10) {
								optionText = document.createTextNode("0" + k);

							} else {
								optionText = document.createTextNode(k);

							}

							optionElem.appendChild(optionText);

							selectElem.appendChild(optionElem);

							// set option's value: "temp"
							templateElem = document.getElementById(defaultValuesElemPrefix + fieldNames[i][j]);
							templateValue = templateElem.options[templateElem.selectedIndex].text;
							selectElem.value = templateValue;
							console.log(templateValue);

						}
					}

					else if((i == 0 || i == 1) && (j == 2)) {
						// this is a period field
						optionElem = document.createElement("option");
						optionText = document.createTextNode("AM");
						optionElem.appendChild(optionText);
						selectElem.appendChild(optionElem);

						optionElem = document.createElement("option");
						optionText = document.createTextNode("PM");
						optionElem.appendChild(optionText);
						selectElem.appendChild(optionElem);

						// set option's value: "temp"
						templateElem = document.getElementById(defaultValuesElemPrefix + fieldNames[i][j]);
						templateValue = templateElem.options[templateElem.selectedIndex].text;
						selectElem.value = templateValue;
						console.log(templateValue);


					}

					else if(i == 3) {
						// this is a defaultMaxPublishers field
						for(var k = 1; k < 10; k++) {
							optionElem = document.createElement("option");
							optionText = document.createTextNode(k);

							optionElem.appendChild(optionText);

							selectElem.appendChild(optionElem);

							// set option's value: "temp"
							templateElem = document.getElementById(defaultValuesElemPrefix + fieldNames[i][j]);
							templateValue = templateElem.options[templateElem.selectedIndex].text;
							selectElem.value = templateValue;
							console.log(templateValue);

						}

					}


					spanElem.appendChild(selectElem);


				}

				dayInfoRowDiv.appendChild(spanElem);
				dayInfoContainerDiv.appendChild(dayInfoRowDiv);


			}


			return dayInfoContainerDiv;


		}

		function generateOptionsForAddDay(deletedDays) {
			var optionElem;
			var optionText;
						console.log(deletedDays);
			var selectElem = document.createElement("select");

			// create options based on deleted days;
			for (var i = 0; i < deletedDays.length; i++) {
				optionElem = document.createElement("option");
				optionText = document.createTextNode((deletedDays[i])[0]);
				optionElem.appendChild(optionText);
				console.log(optionText);
				selectElem.appendChild(optionElem);
			}
			console.log("AFTER");

			console.log(selectElem);
			return selectElem;
		}

		function updateAvailableDaysSelect(addDayTemplate, selectId, daysAvailable) {
			var originalDaySelect = document.getElementById(selectId);

			console.log("IN updateAvailableDaysSelect");
			console.log(originalDaySelect);

			// remove current options
			while (originalDaySelect.firstChild) {
				console.log(originalDaySelect.firstChild)
	    		originalDaySelect.removeChild(originalDaySelect.firstChild);
	    		//console.log(originalDaySelect.firstChild)
			}


			var optionElem;
			var optionText;
			var dayText;
			for(var i = 0; i < deletedDays.length; i++) {
				optionElem = document.createElement("option");
				dayText = deletedDays[i][0];
				optionText = document.createTextNode(dayText);
				optionElem.appendChild(optionText);
				console.log(optionText);
				originalDaySelect.appendChild(optionElem);
			}


		}

		function decideIfPermitAddDay(currentDays) {
			var addDayTemplateExists = true;

			var addDayTemplate = document.getElementsByClassName("dayContainerNewDay")[0];
			console.log(addDayTemplate);

			if(!addDayTemplate) {
				addDayTemplateExists = false;
			}


			console.log(addDayTemplateExists);


			if(currentDays.length < 7) {
				// add day... or leave it if it already exists
				if(addDayTemplateExists) {
					console.log("NO NEED TO ADD ADD INTERFACE");
					// but you do need to update select with available days
					updateAvailableDaysSelect(addDayTemplate, "newDayDay", deletedDays);

				} else {
					console.log("ADD INTERFACE");
					addNewDay[1] = "enabled";

					addAddDayDiv(addNewDay[0]);

				}
			} else {
				// remove it if it exists
				if(addDayTemplateExists) {
					addDayTemplate.parentNode.removeChild(addDayTemplate);
					addNewDay[1] = "disabled";
				} else {
					console.log("NO INTERFACE TO REMOVE");

				}
			}
		}


		function buildDefaultDays(defaultDays) {



			// for each day...
			var day;
			var dayId;
			var dayContainer;
			for(var i = 0; i < defaultDays.length; i++) {
				day = defaultDays[i][0];
				dayId = defaultDays[i][1];
				dayContainer = dayContainerFactory(null, day);
				appendCreatedDayToDOM(dayContainer);


			}

			console.log(weekJson);


		}



		(function build() {
			// the days we want to start off with
			currentDays = [["Monday", "day1"], ["Tuesday", "day2"], ["Wednesday", "day3"], ["Thursday", "day4"], ["Friday", "day5"], ["Saturday", "day6"], ["Sunday", "day7"]];

			// desired order
			order = [["Monday", "day1"], ["Tuesday", "day2"], ["Wednesday", "day3"], ["Thursday", "day4"], ["Friday", "day5"], ["Saturday", "day6"], ["Sunday", "day7"]];

			// template defaults
			tempDefaults = {
				"temp-MinTime-Hrs": 6,
				"temp-MinTime-Mins": 0,
				"temp-MinTime-Period": 'AM',
				"temp-MaxTime-Hrs": 8,
				"temp-MaxTime-Mins": 0,
				"temp-MaxTime-Period": 'PM',
				"temp-Duration-Mins": 30,
				"temp-DefaultMaxPublishers": 2,

			}

			// draw default days...
			buildDefaultDays(currentDays);

			// prepare directory based on provided html...
			//repopulateDayDirectory();

			// add listeners to existing days
			listenersToUpdateJSON();

			// give deleteButton functionality
			//deleteButtonFunctionality();

			// decide if to allow new day to be added
			decideIfPermitAddDay(currentDays);
			// add listeners to all elems in existing day objects
			// they will update json

			// add listeners to template fields... to update corresponding day objects and fields
			enableTemplateFunctionality();

			// set defaults in template
			setTemplateDefaults(tempDefaults);





			console.log(weekJson);
		})();

	
	// left: 37, up: 38, right: 39, down: 40,
	// spacebar: 32, pageup: 33, pagedown: 34, end: 35, home: 36
	var keys = {37: 1, 38: 1, 39: 1, 40: 1};

	function preventDefault(e) {
	  e = e || window.event;
	  if (e.preventDefault)
	      e.preventDefault();
	  e.returnValue = false;  
	}

	function preventDefaultForScrollKeys(e) {
	    if (keys[e.keyCode]) {
	        preventDefault(e);
	        return false;
	    }
	}

	
	function disableScroll() {
		  if (window.addEventListener) // older FF
		      window.addEventListener('DOMMouseScroll', preventDefault, false);
		  window.onwheel = preventDefault; // modern standard
		  window.onmousewheel = document.onmousewheel = preventDefault; // older browsers, IE
		  window.ontouchmove  = preventDefault; // mobile
		  document.onkeydown  = preventDefaultForScrollKeys;
		}
	(function extraData() {
		// find location, cartId, info divs
		var locationDiv = document.getElementsByClassName("location")[0];
		var cartIdDiv = document.getElementsByClassName("cartId")[0];

		var infoDiv = document.getElementsByClassName("info")[0];

		var locationField = locationDiv.getElementsByTagName("INPUT")[0];
		var cartIdField = cartIdDiv.getElementsByTagName("INPUT")[0];
		var infoField = infoDiv.getElementsByTagName("TEXTAREA")[0];
		console.log(locationField);
		console.log(cartIdField);
		console.log(infoField);

		
		// add listeners
		
		locationField.addEventListener("change", function() {
			weekJson["location"] = this.value;
		});
		
		cartIdField.addEventListener("change", function() {
			weekJson["cartId"] = parseInt(this.value);
		});
		
		infoField.addEventListener("change", function() {
			weekJson["info"] = this.value;
		});
		
	})();
	
	(function submitButton() {
		var form = document.getElementsByClassName("weekDiv")[0];
		var next = form.nextSibling;
		
		var submitDiv = document.createElement("div");
		submitDiv.className = "submitDiv";
		
		var overlayElem = document.createElement("div");
		overlayElem.className = "overlay";
		
		
		
		var proceedContainer = document.createElement("div");
		proceedContainer.className = "proceedContainer";
		
		
		
		var proceedQuestion = document.createElement("div");
		proceedQuestion.className = "proceedQuestion";
	
		var textOption = "Would you like to add slots now or later?";
		var proceedQuestionText = document.createTextNode(textOption);
		proceedQuestion.appendChild(proceedQuestionText);
		
		
		var optionsRow = document.createElement("div");
		optionsRow.className = "optionsRow";
		
		
		
		


		submitDiv.addEventListener("click", function(event) {
			console.log("*********");
			console.log(weekJson);
			console.log("*********");
			
			var reqPost = new XMLHttpRequest();

			reqPost.open("POST", "/PPublica/api/week", true);
			reqPost.setRequestHeader("Content-Type", "application/json");
			reqPost.send(JSON.stringify(weekJson));
	  		reqPost.addEventListener("load", function() {



	  			var response;
	  			if(reqPost.status >= 300 || reqPost.status < 200) {

	  				response = reqPost.responseText;
	  		  	    console.log(response);
	  		  	    console.log(reqPost.status);
	  		    	document.body.appendChild(document.createTextNode(response));

	  	  		} else {
	  	  			
	  	  			// successful!!!
	  	  			//parse new week id...
	  	  		window.scrollTo(0, 0);
				document.body.appendChild(overlayElem);
				disableScroll();
  				response = reqPost.responseText;

				response = JSON.parse(response);
							
				// show dialog
				
				var proceedNow = document.createElement("div");
				proceedNow.className = "proceedNow";
				
				var op1 = "Now";
				var proceedNowText = document.createTextNode(op1);
				proceedNow.appendChild(proceedNowText);
				proceedNow.addEventListener("click", function() {
					window.location.href = "/PPublica/schedules/" + response["id"] + "/edit";
				});
				
				
				var proceedLater = document.createElement("div");
				proceedLater.className = "proceedLater";
				
				var op2 = "Later";
				var proceedLaterText = document.createTextNode(op2);
				proceedLater.appendChild(proceedLaterText);
				proceedLater.addEventListener("click", function() {
					window.location.href = "/PPublica/schedules";
				});
				
				
				proceedContainer.appendChild(proceedQuestion);
				optionsRow.appendChild(proceedNow);
				optionsRow.appendChild(proceedLater);
				proceedContainer.appendChild(optionsRow);
				
				
				document.body.appendChild(proceedContainer);
				
	  	  		
	  	  		}
			
			
			
	  		});
			
			
			
			/*
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
	  	  			window.location.href="/PPublica/69thst";
	  	  		}

	 

	    	
	  		});

	  		*/
	  		console.log(JSON.stringify(weekJson));
	  		

		});

		document.body.insertBefore(submitDiv, next);
		

	})();
	
	
}