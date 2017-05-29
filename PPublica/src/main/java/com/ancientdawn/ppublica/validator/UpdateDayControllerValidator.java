package com.ancientdawn.ppublica.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.service.WeekScheduleService;

@Component
public class UpdateDayControllerValidator implements Validator {

	@Autowired
	private UpdateDayValidator updateDayValidator;
	
	@Autowired
	private WeekScheduleService weekScheduleService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Day.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Day day = (Day)target;
		WeekSchedule weekSchedule = null;
		
		updateDayValidator.validate(day, errors);
		
		if(errors.hasErrors()) {
			return;
		}
		
		// check to see that day exists
		if(weekScheduleService.existsWeek(day.getWeekScheduleId())) {
			// load WeekSchedule object
			weekSchedule = weekScheduleService.readWeekSchedule(day.getWeekScheduleId());
		} else {
			errors.rejectValue("weekScheduleId", "com.ancientdawn.ppublica.validator.CreateDayValidator.weekScheduleId.mustExist"); 
			return;
		}
		
		if(weekSchedule.getWeek().size() >= 7) {
			errors.rejectValue("weekScheduleId", "com.ancientdawn.ppublica.validator.CreateDayValidator.weekScheduleId.weekSizeInvalid");
		}
	}

}
