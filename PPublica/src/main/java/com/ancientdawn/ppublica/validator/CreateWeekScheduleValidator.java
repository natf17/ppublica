package com.ancientdawn.ppublica.validator;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.WeekSchedule;


@Component
public class CreateWeekScheduleValidator implements Validator {
	@Autowired
	private CreateDayValidator createDayValidator;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WeekSchedule.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		WeekSchedule weekSchedule = (WeekSchedule)target;
		
		Long id = weekSchedule.getId();
		Set<Day> week = weekSchedule.getWeek();

		
		// week can't exist - can't specify id - it's given by database
		if(id != null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.CreateWeekScheduleValidator.id.new"); 
		}
		
		
		// make sure week has at least one day
		if(week == null || week.size() == 0) {
			errors.rejectValue("week", "com.ancientdawn.ppublica.validator.CreateWeekScheduleValidator.week.atLeastOne"); 
		} else {
			
			// week cannot have more than 7 days
			if(week.size() > 7) {
				errors.rejectValue("week", "com.ancientdawn.ppublica.validator.CreateWeekScheduleValidator.week.tooMany"); 
			}
			
			// validate each day
			// make sure day doesn't have assigned week id
			int index = 0;
			for(Day day : weekSchedule.getWeek()) {
				errors.pushNestedPath("week[" + index + "]");
				if(day.getWeekScheduleId() != null) {
					errors.rejectValue("weekScheduleId", "com.ancientdawn.ppublica.validator.CreateWeekScheduleValidator.week.day.weekScheduleId");
				}
				createDayValidator.validate(day, errors);
				index++;
				errors.popNestedPath();

			}

		}
	
	}
	
}
