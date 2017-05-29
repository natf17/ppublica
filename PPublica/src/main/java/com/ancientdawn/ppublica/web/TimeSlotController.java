package com.ancientdawn.ppublica.web;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.TimeSlotService;
import com.ancientdawn.ppublica.validator.CreateTimeSlotControllerValidator;
import com.ancientdawn.ppublica.validator.UpdateTimeSlotControllerValidator;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {
	private TimeSlotService timeSlotService;
			
	@Autowired
	private CreateTimeSlotControllerValidator createControllerValidator;
	
	@Autowired
	private UpdateTimeSlotControllerValidator updateControllerValidator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	public TimeSlotController(TimeSlotService timeSlotService) {
		this.timeSlotService = timeSlotService;
	}
	@RequestMapping(method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<TimeSlot> saveNewTimeSlot(@RequestBody TimeSlot timeSlot, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(timeSlot);
		
		createControllerValidator.validate(timeSlot, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("createTimeSlot", errors);
		}
		
		timeSlot = timeSlotService.createTimeSlot(timeSlot);
		
		return new ResponseEntity<TimeSlot>(timeSlot, HttpStatus.CREATED); 
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<TimeSlot> updateTimeSlot(@RequestBody TimeSlot timeSlot, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(timeSlot);
		
		updateControllerValidator.validate(timeSlot, errors);
	
		if(errors.hasErrors()) {
			throw new InvalidRequestException("updateTimeSlot", errors);
		}
		
		timeSlot = timeSlotService.updateTimeSlot(timeSlot);
		
		return new ResponseEntity<TimeSlot>(timeSlot, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<TimeSlot> readTimeSlot(@PathVariable Long id) {
		// if id given not within the boundary, throw exception
		// if object is null, throw exception
		TimeSlot timeSlot = timeSlotService.readTimeSlot(id);
		if(timeSlot == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.TimeSlot.Read.NotFound", null, null));
		}
		
		return new ResponseEntity<TimeSlot>(timeSlot, HttpStatus.OK);
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Set<TimeSlot>> getAllPublishers() {

		Set<TimeSlot> slots = timeSlotService.getAllTimeSlots();
		
		return new ResponseEntity<Set<TimeSlot>>(slots, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/forpublisher/{publisherId}", method= RequestMethod.GET, produces="application/json")
	public ResponseEntity<Set<TimeSlot>> getSlotsForPublisher(@PathVariable Long publisherId) {
		Set<TimeSlot> mySlots = timeSlotService.getAllTimeSlotsForPublisher(publisherId);
		System.out.println("FOR ID : " + publisherId);
		
		return new ResponseEntity<Set<TimeSlot>>(mySlots, HttpStatus.OK);

	}
}
