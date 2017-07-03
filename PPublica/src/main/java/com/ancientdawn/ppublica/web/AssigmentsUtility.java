package com.ancientdawn.ppublica.web;

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

import com.ancientdawn.ppublica.domain.Assignment;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.PublisherService;
import com.ancientdawn.ppublica.service.WeekScheduleService;
import com.ancientdawn.ppublica.validator.authorization.UpdateWeekScheduleForPublisher;
import com.ancientdawn.ppublica.validator.util.WeekPublisherAuth;

@RestController
@RequestMapping(value="/test")
public class AssigmentsUtility {
	
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UpdateWeekScheduleForPublisher pubAuthUpdateValidator;
	@Autowired
	private WeekScheduleService weekScheduleService;

	
	@RequestMapping(value="/{timeSlotId}/{publisherId}", method = RequestMethod.DELETE, produces="application/json")
	public ResponseEntity<Assignment> deleteAssignment(@PathVariable("timeSlotId") Long timeSlotId, @PathVariable("publisherId") Long publisherId) {
		
		
		Assignment assignment = publisherService.getAssignment(timeSlotId, publisherId);
		
		if(assignment == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.AssignmentsUtility.Read.NotFound", null, null));
		}
		
		publisherService.deleteAssignment(timeSlotId, publisherId);
		
		return new ResponseEntity<Assignment>(assignment, HttpStatus.ACCEPTED);
	}
	
	
	@RequestMapping(value="/publisher/{publisherId}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<WeekSchedule> updatePublisherAssignmentsInAWeek(@PathVariable("publisherId") Long publisherId, @RequestBody WeekSchedule weekSchedule, BindingResult errors) {
		
		pubAuthUpdateValidator.validate(new WeekPublisherAuth(publisherId, weekSchedule), errors);
		
		if(errors.hasErrors()) {
			System.out.println("UPDATE HAS ERRORS");
			throw new InvalidRequestException("updateWeekSchedule", errors);
			
		}
		
		return new ResponseEntity<WeekSchedule>(weekSchedule, HttpStatus.OK);
	}
	
	@RequestMapping(value="/sample", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<WeekSchedule> sampleAssignment() {
		
		
		return new ResponseEntity<WeekSchedule>(weekScheduleService.getWeekForPublisher(9L,1L), HttpStatus.OK);
	}
	

	
}


// @RequestMapping("api/assignments")

