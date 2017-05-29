package com.ancientdawn.ppublica.validator;

import java.time.LocalTime;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.service.DayService;

@Component
public class UpdateTimeSlotControllerValidator implements Validator {
	
	@Autowired
	private UpdateTimeSlotValidator updateValidator;
	
	@Autowired
	private DayService dayService;

	@Override
	public boolean supports(Class<?> clazz) {
		return TimeSlot.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		TimeSlot timeSlot = (TimeSlot)target;
		Day day = null;
		
		// call CreateTimeSlotValidator
		updateValidator.validate(timeSlot, errors);
		
		if(errors.hasErrors()) {
			return;
		}
		
		// check to see that day exists
		if(dayService.existsDay(timeSlot.getDayId())) {
			// load Day object
			day = dayService.readDay(timeSlot.getDayId());
		} else {
			errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.CreateTimeSlotControllerValidator.dayId.mustExist"); 
			return;
		}
		
		// remove timeSlot with this id from set of timeSlots... will validate as if new slot
		
		Set<TimeSlot> slots = day.getTimeSlots();
		Predicate<TimeSlot> removeSlot = t -> (t.getId()).equals(timeSlot.getId());
		
		if(!slots.removeIf(removeSlot)) {
			errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.UpdateTimeSlotControllerValidator.dayId.noTimeSlotWithIdInDay");
			return;
		}
		
		
		LocalTime dayStart = day.getMinTime();
		LocalTime dayEnd = day.getMaxTime();
		int dayStartMinutes = (dayStart.toSecondOfDay()) / 60; ;
		int dayEndMinutes = (dayEnd.toSecondOfDay()) / 60; ;
		int durationOfDayMinutes = (int)(day.getDuration().toMinutes());
		LocalTime possibleTime = null;
		
		// generate possible startTimes

		for(int i = 0, minutesToAdd = 0; i < (dayEndMinutes - dayStartMinutes) / durationOfDayMinutes; i++) {
				minutesToAdd = i * durationOfDayMinutes;
				System.out.println("minutesToAdd: " + minutesToAdd);

				possibleTime = dayStart.plusMinutes(minutesToAdd);
				
				if(possibleTime.equals(timeSlot.getStartTime())) {
					break; // timeSlot has valid startTime
				}
				
		}
		
		LocalTime slotEndTime = null;
		LocalTime validEndTime = null;
		
		// end reached... invalid slot startTime
		if(possibleTime.equals(dayEnd)) {
			errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.CreateTimeSlotControllerValidator.dayId.slotStartTimeInvalidInDay"); 
		} else {
			// check to see if startTime is repeated...
			for(TimeSlot slot : day.getTimeSlots()) {
				if((slot.getStartTime()).equals(timeSlot.getStartTime())) {
					errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.CreateTimeSlotControllerValidator.dayId.slotStartTimeRepeatedInDay"); 
					return;
				}
			}
			// if end reached, slot is unique
			// check endTime...
			// set the endTime based on legal startTime and day Duration
			slotEndTime = timeSlot.getEndTime();
			validEndTime = timeSlot.getStartTime().plus(day.getDuration());
			if(slotEndTime != null) {
				// first check that provided endTime is valid
				if(!slotEndTime.equals(validEndTime)) {
					errors.rejectValue("endTime", "com.ancientdawn.ppublica.validator.CreateTimeSlotControllerValidator.timeSlots.endTime.invalid"); 
				}
			} else {
				timeSlot.setEndTime(validEndTime);
			}
			
		}
	
	}

}
