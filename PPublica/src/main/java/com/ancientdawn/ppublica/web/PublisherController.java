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

import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;
import com.ancientdawn.ppublica.service.PublisherService;
import com.ancientdawn.ppublica.validator.CreatePublisherValidator;

@RestController
@RequestMapping(value="/api/publisher")
public class PublisherController {
	
	private PublisherService publisherService;
	
	@Autowired
	private CreatePublisherValidator createValidator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<Publisher> saveNewPublisher(@RequestBody Publisher publisher, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(publisher);
		
		createValidator.validate(publisher, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("createPublisher", errors);
		}
		
		publisherService.createPublisher(publisher);

		return new ResponseEntity<Publisher>(publisher, HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Publisher> readPublisher(@PathVariable Long id) {

		Publisher publisher = publisherService.readPublisher(id);
		
		return new ResponseEntity<Publisher>(publisher, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<Set<Publisher>> getAllPublishers() {

		Set<Publisher> publishers = publisherService.getAllPublishers();
		
		return new ResponseEntity<Set<Publisher>>(publishers, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE, produces="application/json")
	public ResponseEntity<Publisher> deletePublisherWithId(@PathVariable Long id) {
		Publisher publisher = publisherService.readPublisher(id);
		
		if(publisher == null) {
			throw new ResourceNotFoundException(messageSource.getMessage("com.ancientdawn.ppublica.controller.Publisher.Read.NotFound", null, null));
		}
		
		publisherService.deletePublisher(id);
		
		return new ResponseEntity<Publisher>(publisher, HttpStatus.ACCEPTED);
	}

	
	@RequestMapping(value="/update/slots", method = RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Publisher> updateSlots(@RequestBody Publisher publisher, BindingResult errors) {
		System.out.println("Before validating");
		System.out.println(publisher);
		
		createValidator.validate(publisher, errors);
		
		if(errors.hasErrors()) {
			throw new InvalidRequestException("createPublisher", errors);
		}
		
		publisherService.createPublisher(publisher);

		return new ResponseEntity<Publisher>(publisher, HttpStatus.CREATED); 
	}
	
	
	
	
}