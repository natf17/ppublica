package com.ancientdawn.ppublica.validator;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.service.DayService;

@Component
public class UpdateDayValidator implements Validator {
	@Autowired
	private CreateTimeSlotValidator createTimeSlotValidator;
	
	@Autowired
	private UpdateTimeSlotValidator updateTimeSlotValidator;
	
	@Autowired
	private DayService dayService;

	@Override
	public boolean supports(Class<?> clazz) {
		return UpdateDayValidator.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Day day = (Day)target;

		Long id = day.getId();
		DayOfWeek weekday = day.getWeekday();
		Short defaultMaxPublishers = day.getDefaultMaxPublishers();
		LocalTime dayStart = day.getMinTime();
		LocalTime dayEnd = day.getMaxTime();
		int dayStartMinutes = 0;
		int dayEndMinutes = 0;
		int durationOfDayMinutes = 0;
		List<LocalTime> possibleTimes = new ArrayList<LocalTime>();
		boolean canContinueValidateSlots = true;

		// make sure day has assigned id
		if(id == null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.UpdateDayValidator.id.new"); 
		} else {
			if(!dayService.existsDay(id)) {
				// make sure day exists
				errors.rejectValue("id", "com.ancientdawn.ppublica.validator.UpdateDayValidator.id.exists"); 
			}
		}
				
		// must have weekDay
		if(weekday == null) {
			errors.rejectValue("weekDay", "com.ancientdawn.ppublica.validator.UpdateDayValidator.weekday.mustExist"); 
		}
		
		// must have defaultMaxPublishers
		if(defaultMaxPublishers == null) {
			errors.rejectValue("weekday", "com.ancientdawn.ppublica.validator.UpdateDayValidator.defaultMaxPublishers.mustExist"); 
		}
		
		// must have a minTime
		if(dayStart == null) {
			errors.rejectValue("minTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.minTime.mustExist");
			canContinueValidateSlots = false;
		}
		
		// must have a maxTime
		if(dayEnd == null) {
			errors.rejectValue("maxTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.maxTime.mustExist"); 
			canContinueValidateSlots = false;
		}
		
		// minTime must be before endTime
		if(canContinueValidateSlots) {
			if(!dayStart.isBefore(dayEnd)) {
				errors.rejectValue("maxTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.dayStart.beforeEnd");
				canContinueValidateSlots = false;
			}
		}
		
		
		// must have a duration
		if(day.getDuration() == null) {
			errors.rejectValue("duration", "com.ancientdawn.ppublica.validator.UpdateDayValidator.duration.mustExist");
			canContinueValidateSlots = false;
		}
		
		if(canContinueValidateSlots) {
			// duration must fit evenly between start and end times
			dayStartMinutes = (dayStart.toSecondOfDay()) / 60; 
			dayEndMinutes = (dayEnd.toSecondOfDay()) / 60;
			durationOfDayMinutes = (int)(day.getDuration().toMinutes());
			if((dayEndMinutes - dayStartMinutes) % durationOfDayMinutes != 0) {
				errors.rejectValue("maxTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.maxTime.unevenIntervals");
				canContinueValidateSlots = false;
			}
			
		} 
		// doesn't need to have timeSlots.....
		if(day.getTimeSlots() == null) {
				// startTime or endTime or duration failed... set error on this object
				if(!canContinueValidateSlots) {
					errors.reject("com.ancientdawn.ppublica.validator.UpdateDayValidator.cantValidateTimeSlots");
				}
			return;
		}
		
		
		if(canContinueValidateSlots) {
			// generate possible startTimes

			for(int i = 0, minutesToAdd = 0; i < (dayEndMinutes - dayStartMinutes) / durationOfDayMinutes; i++) {
				minutesToAdd = i * durationOfDayMinutes;
				System.out.println("minutesToAdd: " + minutesToAdd);

				possibleTimes.add(dayStart.plusMinutes(minutesToAdd));
			}
			
			// validate all of the timeSlots
			int index = 0;
			LocalTime slotEndTime = null;
			LocalTime validEndTime = null;
			Long timeSlotId = null;
			for(TimeSlot timeSlot : day.getTimeSlots()) {
				errors.pushNestedPath("timeSlots[" + index + "]");

				if (timeSlot.getDayId() == null) {
					// set timeSlot dayId
					timeSlot.setDayId(id);
				} else {
					if(!timeSlot.getDayId().equals(id)) {
						// timeSlot's dayId does not match parent day's id
						errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.UpdateDayValidator.timeSlots.dayId.invalid"); 
					}
				}
				
				timeSlotId = timeSlot.getId();
				
				if(timeSlotId == null) {
					createTimeSlotValidator.validate(timeSlot, errors);
				} else {
					updateTimeSlotValidator.validate(timeSlot, errors);
				}
												
				// make sure slots fit evenly, can't overlap
				// slots must start at defined times
				// since the duration is determined by the day, only start time must be validated
				if(timeSlot.getStartTime() != null) {
					if(possibleTimes.contains(timeSlot.getStartTime())) {
						possibleTimes.remove(timeSlot.getStartTime());
						// set the endTime based on legal startTime and day Duration
						slotEndTime = timeSlot.getEndTime();
						validEndTime = timeSlot.getStartTime().plus(day.getDuration());
						if(slotEndTime != null) {
							// first check that provided endTime is valid
							if(!slotEndTime.equals(validEndTime)) {
								errors.rejectValue("endTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.timeSlots.endTime.invalid"); 
							}
						} else {
							timeSlot.setEndTime(validEndTime);
						}
					} else {
						errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.UpdateDayValidator.timeSlots.startTime.invalid"); 
					}
				} else {
					// if startTime null, timeSlotValidator will send error message
				}
				index++;
				errors.popNestedPath();
			}

		} else {
			// if timeSlots provided BUT... startTime and endTime and duration not all present, set error on object
			errors.reject("com.ancientdawn.ppublica.validator.UpdateDayValidator.cantValidateTimeSlots"); 
		}
	}

}
