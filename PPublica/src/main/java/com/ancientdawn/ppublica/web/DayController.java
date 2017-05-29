package com.ancientdawn.ppublica.web;

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

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.DayService;
import com.ancientdawn.ppublica.validator.CreateDayControllerValidator;
import com.ancientdawn.ppublica.validator.UpdateDayControllerValidator;

@RestController
@RequestMapping(value="/api/day")
public class DayController {
	
	DayService dayService;
	
	@Autowired
	CreateDayControllerValidator createControllerValidator;
	
	@Autowired
	UpdateDayControllerValidator updateControllerValidator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	public DayController(DayService dayService) {
		this.dayService = dayService;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<Day> saveNewDay(@RequestBody Day day, BindingResult errors) {
		
		System.out.println("Before validating");
		System.out.println(day);
		
		createControllerValidator.validate(day, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("createDay", errors);
		}
		
		day = dayService.createDay(day);
		
		return new ResponseEntity<Day>(day, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Day> updateDay(@RequestBody Day day, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(day);
		
		updateControllerValidator.validate(day, errors);
	
		if(errors.hasErrors()) {
			throw new InvalidRequestException("updateDay", errors);
		}
		
		day = dayService.updateDay(day);
		
		return new ResponseEntity<Day>(day, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Day> readDay(@PathVariable Long id) {
		// if id given not within the boundary, throw exception
		// if object is null, throw exception
		Day day = dayService.readDay(id);
		if(day == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.Day.Read.NotFound", null, null));
		}
		
		return new ResponseEntity<Day>(day, HttpStatus.OK);
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Set<Day>> getAllPublishers() {

		Set<Day> days = dayService.getAllDays();
		
		return new ResponseEntity<Set<Day>>(days, HttpStatus.OK); 
	}

}
