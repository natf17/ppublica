package com.ancientdawn.ppublica.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.ProfilePublisher;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.service.PublisherService;

@Component
public class CreatePublisherValidator implements Validator {
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	UpdateDayValidator updateDayValidator;
	
	@Autowired
	UpdateTimeSlotValidator updateTimeSlotValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return Publisher.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Publisher publisher = (Publisher)target;
	
		
		Long id = publisher.getId();
		String username = publisher.getUsername();
		String firstName = publisher.getFirstName();
		String lastName = publisher.getLastName();
		String password = null;
		

		// new publisher can't have id
		if(id != null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.id.new");
		}
		
		// must have username
		if(username == null || username.length() == 0) {
			errors.rejectValue("username", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.username.notNull"); 
		} else {
			// username must not already exist
			if(publisherService.existsUsername(username)) {
				errors.rejectValue("username", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.username.mustBeUnique");
			}
		}
		
		// must have firstName
		if(firstName == null || firstName.length() == 0) {
			errors.rejectValue("firstName", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.firstName.mustExist"); 
		}
		
		// must have lastName
		if(lastName == null || lastName.length() == 0) {
			errors.rejectValue("lastName", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.lastName.mustExist"); 
		}
		

		if(publisher instanceof ProfilePublisher) {
			password = ((ProfilePublisher)publisher).getPassword();

		} else {
			return;
		}
		
		// must have password
		if(password == null || password.length() == 0) {
			errors.rejectValue("password", "com.ancientdawn.ppublica.validator.CreatePublisherValidator.password.mustExist"); 
		}
		
	}

}
