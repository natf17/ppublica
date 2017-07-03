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
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.PublisherService;
import com.ancientdawn.ppublica.service.WeekScheduleService;
import com.ancientdawn.ppublica.validator.CreateWeekScheduleValidator;
import com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator;
import com.ancientdawn.ppublica.validator.authorization.UpdateWeekScheduleForPublisher;
import com.ancientdawn.ppublica.validator.util.WeekPublisherAuth;

@RestController
@RequestMapping(value="/api/week")
public class WeekScheduleController {
	
	private WeekScheduleService weekScheduleService;
	@Autowired
	PublisherService publisherService;
	@Autowired
	private CreateWeekScheduleValidator createValidator;
	@Autowired 
	private UpdateWeekScheduleValidator updateValidator;
	@Autowired
	private UpdateWeekScheduleForPublisher pubAuthUpdateValidator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	public WeekScheduleController(WeekScheduleService weekScheduleService) {
		this.weekScheduleService = weekScheduleService;
	}
	@RequestMapping(method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<WeekSchedule> saveNewWeekSchedule(@RequestBody WeekSchedule weekSchedule, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(weekSchedule);
		
		
		createValidator.validate(weekSchedule, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("createWeekSchedule", errors);
		}
		
		weekSchedule = weekScheduleService.createWeekSchedule(weekSchedule);
		
		return new ResponseEntity<WeekSchedule>(weekSchedule, HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<WeekSchedule> readWeek(@PathVariable Long id) {
		// if id given not within the boundary, throw exception
		// if object is null, throw exception
		WeekSchedule weekSchedule = weekScheduleService.readWeekSchedule(id);
		if(weekSchedule == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.WeekSchedule.Read.NotFound", null, null));
		}
		return new ResponseEntity<WeekSchedule>(weekSchedule, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<WeekSchedule> updateWeek(@PathVariable long id, @RequestBody WeekSchedule weekSchedule, BindingResult errors) {
		// id provided in url overrides id in JSON payload
		weekSchedule.setId(id);
		
		Set<Day> week = weekSchedule.getWeek();
		
		System.out.println(week.size());
		int i = 0;
		for(Day day : week) {
			System.out.println("" + i + ": " + day.getTimeSlots().size());
			i++;
		}

		
		updateValidator.validate(weekSchedule, errors);
		
		if(errors.hasErrors()) {
			System.out.println("UPDATE HAS ERRORS");
			throw new InvalidRequestException("updateWeekSchedule", errors);
			
		}
		System.out.println("After VALIDATION " + weekSchedule);

		
		weekScheduleService.updateWeekSchedule(weekSchedule);
		
		return new ResponseEntity<WeekSchedule>(weekSchedule, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Set<WeekSchedule>> getAllSchedules() {

		Set<WeekSchedule> weeks = weekScheduleService.getAllSchedules();
		
		return new ResponseEntity<Set<WeekSchedule>>(weeks, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE, produces="application/json")
	public ResponseEntity<WeekSchedule> deleteWeek(@PathVariable Long id) {
		WeekSchedule weekSchedule = weekScheduleService.readWeekSchedule(id);
		if(weekSchedule == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.WeekSchedule.Read.NotFound", null, null));
		}
		
		weekScheduleService.deleteWeekSchedule(id);
		
		return new ResponseEntity<WeekSchedule>(HttpStatus.ACCEPTED);
		
	}
	
	@RequestMapping(value="/{weekId}/{publisherId}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<WeekSchedule> getWeekForPublisher(@PathVariable("weekId") Long weekId, @PathVariable("publisherId") Long publisherId) {
		
		// does week exist?
		WeekSchedule weekSchedule = weekScheduleService.readWeekSchedule(weekId);
		if(weekSchedule == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.WeekSchedule.Read.NotFound", null, null));
		}
		
		// does publisher exist?
		Publisher publisher = publisherService.readPublisher(publisherId);
		
		if(publisher == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.WeekSchedule.Publisher.Read.NotFound", null, null));
		}
		
		weekSchedule = weekScheduleService.getWeekForPublisher(weekId, publisherId);
		
		
		
		return new ResponseEntity<WeekSchedule>(weekSchedule, HttpStatus.ACCEPTED);

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
	
}

