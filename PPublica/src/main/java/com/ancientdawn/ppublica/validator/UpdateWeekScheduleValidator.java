package com.ancientdawn.ppublica.validator;

import java.util.HashSet;
import java.util.Set;

import org.springframework.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.service.WeekScheduleService;

@Component
public class UpdateWeekScheduleValidator implements Validator {
	
	@Autowired
	private CreateDayValidator createDayValidator;
	
	@Autowired
	private UpdateDayValidator updateDayValidator;
	
	@Autowired
	private WeekScheduleService weekScheduleService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WeekSchedule.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		WeekSchedule weekSchedule = (WeekSchedule)target;
		Long receivedWeekId = weekSchedule.getId();
		
		// week must exist - must specify id
		if(receivedWeekId == null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.id.missing"); 
		} else {
			// check that week exists
			if (!weekScheduleService.existsWeek(receivedWeekId)) {
				errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.weekDoesntExist");
				errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.cantValidateDaysInWeek"); 

			} else {
				Set<Day> week = weekSchedule.getWeek();
				
				// make sure week has at least one day
				if(week == null || week.size() == 0) {
					errors.rejectValue("week", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.week.atLeastOne"); 
				} else {
					// week cannot have more than 7 days
					if(week.size() > 7) {
						errors.rejectValue("week", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.week.tooMany"); 
					}
					Long dayId = null;
					int index = 0;
					for(Day day : weekSchedule.getWeek()) {
						errors.pushNestedPath("week[" + index + "]");
						// if day object has weekScheduleId, ignore it, replace it with this week's id
						day.setWeekScheduleId(receivedWeekId);
						
						// if day has id, use updateDayValidator
						dayId = day.getId();
						if(dayId != null) {
							updateDayValidator.validate(day,errors);
						} else {
							createDayValidator.validate(day, errors);
						}

						index++;
						errors.popNestedPath();

					}

				}
			
			}
	
		}
	
	}
}
