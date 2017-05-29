package com.ancientdawn.ppublica.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ancientdawn.ppublica.domain.Assignment;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.PublisherService;

@RestController
@RequestMapping("api/assignments")
public class AssigmentsUtility {
	
	
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private MessageSource messageSource;

	
	@RequestMapping(value="/{timeSlotId}/{publisherId}", method = RequestMethod.DELETE, produces="application/json")
	public ResponseEntity<Assignment> deleteAssignment(@PathVariable("timeSlotId") Long timeSlotId, @PathVariable("publisherId") Long publisherId) {
		
		
		Assignment assignment = publisherService.getAssignment(timeSlotId, publisherId);
		
		if(assignment == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.AssignmentsUtility.Read.NotFound", null, null));
		}
		
		publisherService.deleteAssignment(timeSlotId, publisherId);
		
		return new ResponseEntity<Assignment>(assignment, HttpStatus.ACCEPTED);
	}
	
	
}
