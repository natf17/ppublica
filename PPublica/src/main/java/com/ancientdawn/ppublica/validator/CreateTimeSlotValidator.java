package com.ancientdawn.ppublica.validator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.service.PublisherService;

@Component
public class CreateTimeSlotValidator implements Validator {
	@Autowired
	private PublisherService publisherService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return TimeSlot.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		TimeSlot timeSlot = (TimeSlot)target;
		
		Long id = timeSlot.getId();
		Long dayId = timeSlot.getDayId();
		LocalTime startTime = timeSlot.getStartTime();
		Set<Publisher> publishers = timeSlot.getPublishers();
		Short maxPublishers = timeSlot.getMaxPublishers();

		// make sure timeslot doesn't have assigned id
		if(id != null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.id.new"); 
		}
		
		// make sure timeSlot has dayId
		if(dayId == null) {
			errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.dayId.mustHave"); 
		}
		
		// make sure it has a start time
		if(startTime == null) {
			errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.startTime.mustExist"); 
		}
		
		
		// make sure it specifies max number of publishers
		if(maxPublishers == null) {
			errors.rejectValue("maxPublishers", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.maxPublishers.mustExist"); 
		} else if(maxPublishers < 1) {
			errors.rejectValue("maxPublishers", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.maxPublishers.mustBeGreaterThanOne"); 

		}
		
		// must have publishers				
		if(publishers == null) {
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.mustHave");
			System.out.println("pub NULLLLLLLLLLLLLLLLLL");
			System.out.println(timeSlot);
		} else if(publishers.size() < 1) {
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.mustHave"); 
			System.out.println("pub LESSSSSSSSS TTTTHHHHAAANNN 11111");
			System.out.println(timeSlot);

		}
		else {
			// number of publishers must be less than or equal to max
			if(maxPublishers != null && publishers.size() > timeSlot.getMaxPublishers()) {
				errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.tooMany"); 
			}
			
			// publisher can't be repeated
			List<Long> ids = new ArrayList<Long>();
			int index = 0;
			for (Publisher publisher : timeSlot.getPublishers()) {
				errors.pushNestedPath("publishers[" + index + "]");

				// he must have an id
				if(publisher.getId() == null) {
					errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.id.required"); 
				} else {
					if(ids.contains(publisher.getId())) {
						errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.id.repeated"); 
					} else {
						ids.add(publisher.getId());
						// check to see if id exists in db
						if(!publisherService.existsWithId(publisher.getId())) {
							errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreateTimeSlotValidator.publishers.id.notFound"); 
						}
				
					}

				}
				
				// all other publisher info is ignored!!!!!!
				index++;
				errors.popNestedPath();
			}
			
		}

	}

}
