package com.ancientdawn.ppublica.validator.authorization;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.service.WeekScheduleService;
import com.ancientdawn.ppublica.validator.util.WeekPublisherAuth;

@Component
public class UpdateWeekScheduleForPublisher implements Validator {
	
	@Autowired
	private WeekScheduleService weekScheduleService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WeekPublisherAuth.class.isAssignableFrom(clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 * target object cannot have null fields
	 */

	@Override
	public void validate(Object target, Errors errors) {
		
		WeekPublisherAuth weekAuth = (WeekPublisherAuth)target;
		
		WeekSchedule weekPublisher = weekAuth.getWeekSchedule();
		Long publisherId = weekAuth.getPublisherId();
		
		boolean canContinueValidation = true;
		
		// weekId
		Long weekId = weekPublisher.getId();
		if(weekId == null) {
			errors.rejectValue("id", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.id.missing");
			canContinueValidation = false;
		}
		
		// all other values - are either null or exactly as they are in the Repository
		WeekSchedule weekRepo = weekScheduleService.readWeekSchedule(weekId);
		if(canContinueValidation && weekRepo == null) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.weekDoesntExist");
			canContinueValidation = false;
			
		}
		
		String infoRepo = null;
		String infoProv = weekPublisher.getInfo();
		
		String locationRepo = null;
		String locationProv = weekPublisher.getLocation();
		
		Short cartIdRepo = null;
		Short cartIdProv = weekPublisher.getCartId();

		if(infoProv != null) {
			infoRepo = weekRepo.getInfo();
			if(infoRepo == null || !(infoRepo.equals(infoProv))) {
				errors.rejectValue("info", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
			}
		}
		
		if(locationProv != null) {
			locationRepo = weekRepo.getLocation();
			if(locationRepo == null || !(locationRepo.equals(locationProv))) {
				errors.rejectValue("location", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
			}
		}
		
		
		if(cartIdProv != null) {
			cartIdRepo = weekRepo.getCartId();
			if(cartIdRepo == null || !(cartIdRepo.equals(cartIdProv))) {
				errors.rejectValue("cartId", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
			}
		}
		
		if(!canContinueValidation) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.cantContinueValidation"); 

			return;
		}
		
		Set<Day> weekDays = weekPublisher.getWeek();
		Set<Day> weekDaysRepo = weekRepo.getWeek();
		// the existing week will be merged with new data...
		// only pertinent fields are validated here
		// the entire object will be validated later.
		
		// make sure week has at least one day
		if(weekDays == null || weekDays.size() == 0) {
			errors.rejectValue("week", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleValidator.week.atLeastOne");
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.cantContinueValidation"); 
			return;
		}
		
		// the days in provided week must match those in existing week
		if(weekDays.size() != weekDaysRepo.size()) {
			errors.rejectValue("week", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.weekSameSize");
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.cantContinueValidation"); 
			return;
		}
		
		
		// weekDays are the same size - make sure they match
		Map<Long, DayOfWeek> dayIdsToDayOfWeek = new HashMap<Long, DayOfWeek>();
		for(Day dayRepo : weekDaysRepo) {
			dayIdsToDayOfWeek.put(dayRepo.getId(), dayRepo.getWeekday());
		}
		
		// we already know they are the same size, so assume one-to-one and onto relationship:
		boolean dayMatch = false;
		Long dayId;
		DayOfWeek dayOfWeek;
		int index = 0;
		for(Day dayProv : weekDays) {
			errors.pushNestedPath("week[" + index + "]");


			dayId = dayProv.getId();
			dayOfWeek = dayProv.getWeekday();
			
			
			dayMatch = findWeekDayByIdOrDayOfWeek(dayId, dayOfWeek, dayIdsToDayOfWeek);

			if(!dayMatch) {
				errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.dayDoesNotMatch");
				errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.cantContinueValidation"); 
			} else {
				// day matches - validate rest of fields using dayOfWeek
				if(dayOfWeek == null) {
					dayOfWeek = dayIdsToDayOfWeek.get(dayId);
				}
				dayFieldsEqual(dayProv, dayOfWeek, weekDaysRepo, publisherId, errors);
				

			}
			

			index++;
			errors.popNestedPath();
		}

		
		
		
		
		
		
		

		
		
		

		
	}
	// will search for dayId in provided map
	// if also provided with dayOfWeek, it will check that it matches value in map
	private boolean findWeekDayByIdOrDayOfWeek(Long dayId, DayOfWeek dayOfWeek, Map<Long,DayOfWeek> daysRepo) {
		// dayId Provided
		boolean performMatchIdProv = dayId == null ? false : true;
		
		boolean performMatchDayProv = dayOfWeek == null ? false : true;
		
		// if id provided
		if(performMatchIdProv) {
			if(daysRepo.containsKey(dayId) ) {
				
				// if weekDay provided, check match
				if(performMatchDayProv) {
					if(dayOfWeek.equals(daysRepo.get(dayId))) {
						return true;
					}
					// this dayId exists but its dayOfWeek doesn't match one provided
					return false;
				}
				
				// dayOfWeek not provided, so match is successful
				return true;
			}
			// dayId not found, match unsuccessful
			return false;
		}
		// if dayProvided, no id
		else if(performMatchDayProv) {
			if(daysRepo.containsValue(dayOfWeek)) {
				return true;
			}
			
			return false;
		}
		
		// not enough data
		return false;
	}
	
	
	
	
	private void dayFieldsEqual(Day dayProv, DayOfWeek dayOfWeek, Set<Day> weekDaysRepo, Long publisherId, Errors errors) {
		
		Day dayRepo = null;
		boolean canContinueValidation = true;
		
		// find corresponding day
		for(Day day : weekDaysRepo) {
			if((day.getWeekday()).equals(dayOfWeek)) {
				dayRepo = day;
			}
		}
		
		if(dayRepo == null) {
			throw new RuntimeException("Day Not Found!");
		}
		
		
		// make sure all fields match if they are specified
		
		Long weekScheduleIdRepo = null;
		Long weekScheduleIdProv = dayProv.getWeekScheduleId();
		
		LocalTime minTimeRepo = null;
		LocalTime minTimeProv = dayProv.getMinTime();
		
		LocalTime maxTimeRepo = null;
		LocalTime maxTimeProv = dayProv.getMaxTime();

		Duration durationRepo = null;
		Duration durationProv = dayProv.getDuration();
		
		
		Short defaultMaxPublishersRepo = null;
		Short defaultMaxPublishersProv = dayProv.getDefaultMaxPublishers();

		

		
		
		if(weekScheduleIdProv != null) {
			weekScheduleIdRepo = dayRepo.getWeekScheduleId();
			if(weekScheduleIdRepo == null || !(weekScheduleIdRepo.equals(weekScheduleIdProv))) {
				errors.rejectValue("weekScheduleId", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
				canContinueValidation = false;
			}
		}
		
		
		if(minTimeProv != null) {
			minTimeRepo = dayRepo.getMinTime();
			if(minTimeRepo == null || !(minTimeRepo.equals(minTimeProv))) {
				errors.rejectValue("minTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
				canContinueValidation = false;
			}
		}
		
		if(maxTimeProv != null) {
			maxTimeRepo = dayRepo.getMaxTime();
			if(maxTimeRepo == null || !(maxTimeRepo.equals(maxTimeProv))) {
				errors.rejectValue("maxTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
				canContinueValidation = false;
			}
		}
		
		
		if(durationProv != null) {
			durationRepo = dayRepo.getDuration();
			if(durationRepo == null || !(durationRepo.equals(durationProv))) {
				errors.rejectValue("duration", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
				canContinueValidation = false;
			}
		}
		
		if(defaultMaxPublishersProv != null) {
			defaultMaxPublishersRepo = dayRepo.getDefaultMaxPublishers();
			if(defaultMaxPublishersRepo == null || !(defaultMaxPublishersRepo.equals(defaultMaxPublishersProv))) {
				errors.rejectValue("defaultMaxPublishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth");
				canContinueValidation = false;
			}
		}
		
		if(!canContinueValidation) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.cantContinueValidation"); 
			return;
		}
		
		// validate timeSlots
		
		Set<TimeSlot> timeSlotsProv = dayProv.getTimeSlots();
		
		if(timeSlotsProv == null || timeSlotsProv.size() == 0) {
			// no timeSlots provided for this publisher
			return;
		}
		
		// there are timeSlots...
		
		// make sure there are no repeat startTimes OR ids
		noRepeatIdsOrStartTimes(timeSlotsProv, errors);

		// add all timeSlots that can be validated as NEW - they don't exist, they have a valid startTime, ...
		Set<TimeSlot> newSlots = new HashSet<TimeSlot>();
		int index = 0;
		for(TimeSlot timeSlotProv : dayProv.getTimeSlots()) {
			errors.pushNestedPath("timeSlots[" + index + "]");
			
			if(timeSlotsValidator(timeSlotProv, dayRepo, publisherId, errors)) {
				newSlots.add(timeSlotProv);
			} else {
				// this is an update - times will not be validated
			}
			
			
			
			index++;
			errors.popNestedPath();

		}
		
		// make sure they all fit into day...
		fitTimeSlotsIntoDay(newSlots, dayRepo, errors);
		

		
		
		
		
		
		

	}
	
	private void noRepeatIdsOrStartTimes(Set<TimeSlot> timeSlotsProv, Errors errors) {
		// to store all provided Ids
		Set<Long> providedIds = new HashSet<Long>();
		
		// to store all taken startTimes
		Set<LocalTime> providedStartTimes = new HashSet<LocalTime>();
		
		Long idProv = null;
		LocalTime startTimeProv = null;
		int index = 0;
		for(TimeSlot ts : timeSlotsProv) {
			errors.pushNestedPath("timeSlots[" + index + "]");
			idProv = ts.getId();
			
			if(idProv != null) {
				if(providedIds.contains(idProv)) {
					// this id is repeated in provided timeSlots
					errors.rejectValue("id", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.noRepeatIdsOrStartTimes.id");

				}
				else {
					providedIds.add(idProv);
				}
			}
			
			startTimeProv = ts.getStartTime();
			
			if(startTimeProv != null) {
				if(providedStartTimes.contains(startTimeProv)) {
					// this startTime is repeated in provided timeSlots
					errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.noRepeatIdsOrStartTimes.startTime");

				} else {
					
					providedStartTimes.add(startTimeProv);

				}
			}
			
			errors.popNestedPath();
		}
		
		
	}
	
	private void fitTimeSlotsIntoDay(Set<TimeSlot> timeSlotsToAdd, Day dayRepo, Errors errors) {
		 /*  NEW objects must have a startTime
		 *  use startTime and day's default Duration to calculate 
		 * 
		 */
		
		int index = 0;
		
		Duration defaultDuration = dayRepo.getDuration();
		
		// Possible startTimes for the day
		Set<LocalTime> possibleTimes = new HashSet<LocalTime>();
		LocalTime dayMin = dayRepo.getMinTime();
		LocalTime dayMax = dayRepo.getMaxTime();
		LocalTime timeCounter = dayMin;
		
		while(timeCounter.isBefore(dayMax)) {
			possibleTimes.add(timeCounter);
			
			timeCounter = timeCounter.plus(defaultDuration);
		}
		

		
		LocalTime providedStartTime = null;
		for(TimeSlot timeSlotProv : timeSlotsToAdd) {
			// push nested path
			errors.pushNestedPath("timeSlots[" + index + "]");
			
			// ignore optionally provided endTime...
			providedStartTime = timeSlotProv.getStartTime();
			
			// startTime must be VALID
			if(possibleTimes.contains(providedStartTime)) {
				errors.rejectValue("startTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.fitIntoDay.startTimeNotValid", new Object[]{providedStartTime}, null);
			}
			
			// pop nested path
			index++;
			errors.popNestedPath();

		}
		
		
	}
	
	
	private boolean timeSlotsValidator(TimeSlot timeSlotProv, Day dayRepo, Long publisherId, Errors errors) {
		
		
		Long timeSlotIdProv = timeSlotProv.getId();
		LocalTime timeSlotStartTimeProv = timeSlotProv.getStartTime();
		TimeSlot timeSlotRepo = null;
		boolean isNew = false;
		try {
			timeSlotRepo = findTimeSlotByIdOrStartTime(timeSlotIdProv, timeSlotStartTimeProv, dayRepo.getTimeSlots());
		} catch(IdentityMismatch e) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.identityMismatch"); 

			return isNew;
		} catch(NullArgs e) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.nullArgs"); 

			return  isNew;
		}
		
		/*
		 * If timeSlot has no id - it is being created.
		 * The publisher should be the only person in this slot.
		 */		
		if(timeSlotRepo == null) {
			if(!(timeSlotProv.getId() == null)) {
				errors.reject("id", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.id.NotFound"); 
				return isNew;
			}
			// THIS IS A NEW SLOT
			// make sure its pertinent fields use day's defaults
			checkNewTimeSlotFieldsRestricted(timeSlotProv, dayRepo, publisherId, errors);
			
			isNew = true;
			return isNew;
			
		} 
		
		/* if publisher is being added or removed from the timeSlot
		 * this timeSlot must provide either a startTime or an id
		 * If both are provided, they must match
		 */
		
		else {
			// THIS IS AN UPDATE
			
			// timeSlotRepo found - id and/or startTime match
			checkUpdatedTimeSlotFieldsRestricted(timeSlotProv, timeSlotRepo, publisherId, errors);
			return isNew;
		}
		
		
		
	}
	
	private void checkNewTimeSlotFieldsRestricted(TimeSlot timeSlotProv, Day dayRepo, Long publisherId, Errors errors) {
		// assumes only startTime has been provided - don't check
		
		// all other fields, if provided, must match day's defaults
		
		// dayId
		Long timeSlotDayIdProv = timeSlotProv.getDayId();
		Long dayRepoId = null;
		
		if(timeSlotDayIdProv != null) {
			dayRepoId = dayRepo.getId();
			if(dayRepoId == null || !(timeSlotDayIdProv.equals(dayRepoId))) {
				errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.dayId.MustMatch");
			}
		}
		// endTime (using day's duration)
		LocalTime timeSlotEndTimeProv = timeSlotProv.getEndTime();
		Duration defaultDuration = null;
		LocalTime timeSlotEndTimeCalc = null;
		
		if(timeSlotEndTimeProv != null) {
			defaultDuration = dayRepo.getDuration();
			timeSlotEndTimeCalc = timeSlotProv.getStartTime().plus(defaultDuration);
			
			if(timeSlotEndTimeCalc == null || !(timeSlotEndTimeProv.equals(timeSlotEndTimeCalc))) {
				errors.rejectValue("endTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.endTime.MustMatchCalc", new Object[]{timeSlotEndTimeProv, timeSlotEndTimeCalc}, null);
			}
		}
		
		
		// publishers
		restrictedPublishersField(timeSlotProv, publisherId, errors);
		
		
		// maxPublishers
		Short timeSlotMaxPublishersProv = timeSlotProv.getMaxPublishers();
		Short defaultMaxPublishers = null;
		
		if(timeSlotMaxPublishersProv != null) {
			defaultMaxPublishers = dayRepo.getDefaultMaxPublishers();
			if(defaultMaxPublishers == null || !(timeSlotMaxPublishersProv.equals(defaultMaxPublishers))) {
				errors.rejectValue("maxPublishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.maxPublishers.MustMatchDefault");
			}
		}
		
		
	}
	
	
	private void checkUpdatedTimeSlotFieldsRestricted(TimeSlot timeSlotProv, TimeSlot timeSlotRepo, Long publisherId, Errors errors) {
		// assumes no id provided, and startTime has been provided and they match - don't check
		
		// all other fields, if provided, must match day's defaults
		
		// dayId
		Long timeSlotDayIdProv = timeSlotProv.getDayId();
		Long timeSlotDayIdRepo = null;
		
		if(timeSlotDayIdProv != null) {
			timeSlotDayIdRepo = timeSlotRepo.getDayId();
			if(timeSlotDayIdRepo == null || !(timeSlotDayIdProv.equals(timeSlotDayIdRepo))) {
				errors.rejectValue("dayId", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.dayId.MustMatchRepo");
			}
		}
		// endTime
		LocalTime timeSlotEndTimeProv = timeSlotProv.getEndTime();
		LocalTime timeSlotEndTimeRepo = null;
		
		if(timeSlotEndTimeProv != null) {
			timeSlotEndTimeRepo = timeSlotRepo.getEndTime();
			
			if(timeSlotEndTimeRepo == null || !(timeSlotEndTimeProv.equals(timeSlotEndTimeRepo))) {
				errors.rejectValue("endTime", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.endTime.MustMatchRepo");
			}
		}
		
		
		// publishers
		publishersFieldUpdateTimeSlot(timeSlotProv, timeSlotRepo, publisherId, errors);
		
		
		// maxPublishers
		Short timeSlotMaxPublishersProv = timeSlotProv.getMaxPublishers();
		Short timeSlotMaxPublishersRepo = null;
		
		if(timeSlotMaxPublishersProv != null) {
			timeSlotMaxPublishersRepo = timeSlotRepo.getMaxPublishers();
			if(timeSlotMaxPublishersRepo == null || !(timeSlotMaxPublishersProv.equals(timeSlotMaxPublishersRepo))) {
				errors.rejectValue("maxPublishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.maxPublishers.MustMatchRepo");
			}
		}

		
		
	}
	
	private void publishersFieldUpdateTimeSlot(TimeSlot timeSlotProv, TimeSlot timeSlotRepo, Long publisherId, Errors errors) {
		// an updated timeSlot - updated timeSlot's publishers field will differ with original in 1 respect: it will have a new publisher - the authenticated publisher
		
		// must contain new publisher
		Set<Publisher> timeSlotPublishersProv = timeSlotProv.getPublishers();
		Set<Publisher> timeSlotPublishersRepo = timeSlotRepo.getPublishers();
		
		Set<Long> publisherIdsProv = null;
		Set<Long> publisherIdsRepo = null;
		
		if(timeSlotPublishersProv == null) {
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.publishers.CantBeNull");

			return;
		} 
		
		publisherIdsProv = new HashSet<Long>();
		for(Publisher pub : timeSlotPublishersProv) {
			publisherIdsProv.add(pub.getId());
		}
		
		publisherIdsRepo = new HashSet<Long>();
		for(Publisher pub : timeSlotPublishersRepo) {
			publisherIdsRepo.add(pub.getId());
		}
		
		
		if(!publisherIdsProv.contains(publisherId)) {
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.publishers.MustContainPublisher");
			
		} else {
			// extract publisher from updated timeSlot's Set<Publisher> to prepare for next step in validation
			publisherIdsProv.remove(publisherId);
			
			// if this publisher is in this timeSlot (HE HAS TO BE)
			// and if he's in repo timeSlot, he already existed
			// if he's not - he's a new addition
			if(publisherIdsRepo.contains(publisherId)) {
				publisherIdsRepo.remove(publisherId);
			}
			
		}
		
		
		// updated and original sets must be equal
		if(!publisherIdsProv.equals(publisherIdsRepo)) {
			System.out.println("PUB IDS");
			System.out.println(publisherIdsProv);
			System.out.println(publisherIdsRepo);
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.timeSlot.publishers.CannotModifyOtherPublishers");
		}
		
		// make sure only publisher id has been provided
		int index = 0;
		for(Publisher publisher : timeSlotPublishersProv) {
			errors.pushNestedPath("publishers[" + index + "]");

			noOtherPublisherFieldsProvided(publisher, errors);
			
			errors.popNestedPath();
		}
		
		
		
	}
	
	private void restrictedPublishersField(TimeSlot timeSlotProv, Long publisherId, Errors errors) {
		// creating a new timeSlot
		Set<Publisher> timeSlotPublishersProv = timeSlotProv.getPublishers();
		
		Publisher publisherProv = null;
		Iterator<Publisher> it = null;
		
		
		
		if(timeSlotPublishersProv == null || timeSlotPublishersProv.size() != 1) {
			errors.rejectValue("publishers", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.New.Publishers.Size"); 

		} else {
			// there is one publisher - make sure it is only THIS publisher
			// will only accept publisherId
			it = timeSlotPublishersProv.iterator();
			if(it.hasNext()) {
				publisherProv = it.next();
				
				errors.pushNestedPath("publishers[0]");
				
				// make sure only this publisher is in the timeSlot
				publisherOnlyAuthIdProvided(publisherProv, publisherId, errors);					
				
				errors.popNestedPath();
				

			} else {
				throw new RuntimeException("Unexpected iterator error");
			}
			
		}
		
	}
	
	private boolean publisherOnlyAuthIdProvided(Publisher publisher, Long authId, Errors errors) {
		
		Long id = publisher.getId();
		String username = publisher.getUsername();
		String firstName = publisher.getFirstName();
		String lastName = publisher.getLastName();
		
		
		if(username != null || firstName != null || lastName != null) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.publisher.onlyAuthIdProvided");
			return false;
		}
		
		if(!id.equals(authId)) {
			errors.reject("com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.publisher.onlyThisPublisherInNewSlot");
			return false;
		}
		
		return true;
	}
	
	private void noOtherPublisherFieldsProvided(Publisher publisher, Errors errors) {
		String usernameProv = publisher.getUsername();
		String firstNameProv = publisher.getFirstName();
		String lastNameProv = publisher.getLastName();
		
		if(usernameProv != null) {
			errors.rejectValue("username", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.New.Publishers.username.Denied"); 

		}
		
		if(firstNameProv != null) {
			errors.rejectValue("firstName", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.New.Publishers.firstName.Denied"); 

		}
		
		if(lastNameProv != null) {
			errors.rejectValue("lastName", "com.ancientdawn.ppublica.validator.UpdateWeekScheduleForPublisherValidator.auth.timeSlot.New.Publishers.lastName.Denied"); 

		}
	}
	
	
	
	
	// will search for dayId in provided map
	// if also provided with dayOfWeek, it will check that it matches value in map
	private TimeSlot findTimeSlotByIdOrStartTime(Long timeSlotIdProv, LocalTime startTimeProv, Set<TimeSlot> slotsRepo) throws IdentityMismatch, NullArgs {
		// timeSlotId provided
		boolean performMatchIdProv = timeSlotIdProv == null ? false : true;
		
		boolean performMatchStartTimeProv = startTimeProv == null ? false : true;
		
		TimeSlot timeSlotRepo = null;
		
		// if id provided
		if(performMatchIdProv) {
			for(TimeSlot slotRepo : slotsRepo) {
				if((slotRepo.getId()).equals(timeSlotIdProv)) {
					// id found
					timeSlotRepo = slotRepo;

					// if startTime provided, check match
					if(performMatchStartTimeProv) {
						if((slotRepo.getStartTime()).equals(startTimeProv)) {
							return timeSlotRepo;
						}
						// id found but wrong startTime
						// throw exception
						throw new IdentityMismatch();
					}
					
					// startTime not provided, match successful
					return timeSlotRepo;
					
				}

			}
			
			// searched through all timeSlots - no id match
			return null;
			
		}
		// startTime provided - no id provided
		else if(performMatchStartTimeProv) {
			for(TimeSlot slotRepo : slotsRepo) {
				if((slotRepo.getStartTime()).equals(startTimeProv)) {
					// startTime found
					timeSlotRepo = slotRepo;

					return timeSlotRepo;
				}
			}
			
			// startTime provided - no match
			return null;
		}
		
		
		throw new NullArgs("TIMESLOT ID AND STARTIME CANNOT BE NULL");
		

	}
	
	protected class IdentityMismatch extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5458280677916873312L;
		
	}
	
	protected class NullArgs extends Exception {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2616996022619890082L;

		public NullArgs() {
			super();
		}
		
		public NullArgs(String msg) {
			super(msg);
		}
	}
	
	
	
	
}
