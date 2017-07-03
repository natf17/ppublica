package com.ancientdawn.ppublica.service.impl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.domain.repository.WeekScheduleRepository;
import com.ancientdawn.ppublica.service.DayService;
import com.ancientdawn.ppublica.service.TimeSlotService;
import com.ancientdawn.ppublica.service.WeekScheduleService;
import com.ancientdawn.ppublica.util.ModifiedAssignment;

@Service
public class WeekScheduleServiceImpl implements WeekScheduleService {
	private WeekScheduleRepository weekScheduleRepository;
	@Autowired
	DayService dayService;
	
	@Autowired
	TimeSlotService timeSlotService;
	
	@Autowired
	public WeekScheduleServiceImpl(WeekScheduleRepository repository) {
		this.weekScheduleRepository = repository;
		
	}
	
	@Override
	public WeekSchedule createWeekSchedule(WeekSchedule weekSchedule) {
		// save to repository
		return weekScheduleRepository.createWeekSchedule(weekSchedule);
	}

	@Override
	public WeekSchedule updateWeekSchedule(WeekSchedule weekSchedule) {
		/*
		 * WeekSchedule...
		 * * MUST HAVE id
		 * * MUST HAVE at least one day
		 */
		System.out.println("Update service method");
		weekScheduleRepository.updateWeekSchedule(weekSchedule);
		
		
		return weekSchedule;
	}

	@Override
	public WeekSchedule readWeekSchedule(Long weekScheduleId) {
		if(!existsWeek(weekScheduleId)) {
			return null;
		}
		return weekScheduleRepository.readWeekSchedule(weekScheduleId);
	}


	@Override
	public void deleteWeekSchedule(Long weekScheduleId) {
		weekScheduleRepository.deleteWeekSchedule(weekScheduleId);
		
	}

	@Override
	public Set<WeekSchedule> getAllSchedules() {
		
		return weekScheduleRepository.getAllSchedules();
	}

	@Override
	public boolean existsWeek(long id) {
		return weekScheduleRepository.existsWeek(id);
	}

	@Override
	public Set<WeekSchedule> getWeeksForPublisher(Long id) {
		
		Set<WeekSchedule> weeksForPublisher = new HashSet<WeekSchedule>();
		
		
		
		
		
		Set<TimeSlot> timeSlots = timeSlotService.getAllTimeSlotsForPublisher(id);
		
		Set<Long> timeSlotIds = new HashSet<Long>();
		Set<Long> dayIds = new HashSet<Long>();
		
		// grab all day ids from timeSlot
		for(TimeSlot slot : timeSlots) {
			dayIds.add(slot.getDayId());
			timeSlotIds.add(slot.getId());
		}
		
		// grab all weekSchedule ids
		
		Set<Long> weekIds = new HashSet<Long>();
		Day day;
		for(Long dayId : dayIds) {
			day = dayService.readDay(dayId);
			weekIds.add(day.getWeekScheduleId());
		}
		
		
		for(Long weekId : weekIds) {
			weeksForPublisher.add(this.getWeekForPublisher(weekId, id, dayIds, timeSlotIds));
		}
		
		return weeksForPublisher;
		
		
		
		
	}

	@Override
	public WeekSchedule getWeekForPublisher(Long weekId, Long publisherId, Set<Long> dayIds, Set<Long> timeSlotIds) {
		// grab full weekSchedule
		WeekSchedule weekSchedule;
		WeekSchedule censoredWeek;
		Day censoredDay;
		SortedSet<TimeSlot> censoredSlots;
		Set<Day> censoredDays;
		
		weekSchedule = this.readWeekSchedule(weekId);
					
		censoredDays = new TreeSet<Day>();
		
		for(Day dayInWeek : weekSchedule.getWeek()) {
		
			if(dayIds.contains(dayInWeek.getId())) {
		
				// if day is contained in dayIds for publisher...
				// iterate through timeSlots
				censoredSlots = new TreeSet<TimeSlot>();
				for(TimeSlot slotInDay : dayInWeek.getTimeSlots()) {
					if(timeSlotIds.contains(slotInDay.getId())) {
						// if timeSlot is contained in timeSlotIds...
						// add timeSlot to censoredSlots
						censoredSlots.add(slotInDay);
					}
					else {
						// if timeSlot is not contained in timeSlotIds for publisher, don't add to censoredSlots
						continue;
					}
				}
							
				// create a new Day object to hold censored slots
				censoredDay = new Day();
				censoredDay.setId(dayInWeek.getId());
				censoredDay.setDefaultMaxPublishers(dayInWeek.getDefaultMaxPublishers());
				censoredDay.setDuration(dayInWeek.getDuration());
				censoredDay.setMaxTime(dayInWeek.getMaxTime());
				censoredDay.setMinTime(dayInWeek.getMinTime());
				censoredDay.setTimeSlots(censoredSlots);
				censoredDay.setWeekday(dayInWeek.getWeekday());
				censoredDay.setWeekScheduleId(dayInWeek.getWeekScheduleId());
							
				censoredDays.add(censoredDay);
							
			}
						
						
			else {
				// if day is not contained in dayIds for publisher, don't add to censoredWeek
				continue;
			}
			
		}
					
		// create a new WeekSchedule Object
					
		censoredWeek = new WeekSchedule();
		censoredWeek.setCartId(weekSchedule.getCartId());
		censoredWeek.setId(weekSchedule.getId());
		censoredWeek.setInfo(weekSchedule.getInfo());
		censoredWeek.setLocation(weekSchedule.getLocation());
		censoredWeek.setWeek(censoredDays);
					
					
				
				
		
		return censoredWeek;
		
	}
	
	@Override
	public WeekSchedule getWeekForPublisher(Long weekId, Long publisherId) {
		Set<TimeSlot> timeSlots = timeSlotService.getAllTimeSlotsForPublisher(publisherId);
		
		Set<Long> timeSlotIds = new HashSet<Long>();
		Set<Long> dayIds = new HashSet<Long>();
		
		// grab all day ids from timeSlot
		for(TimeSlot slot : timeSlots) {
			dayIds.add(slot.getDayId());
			timeSlotIds.add(slot.getId());
		}
		
		// grab all weekSchedule ids
		
		Set<Long> weekIds = new HashSet<Long>();
		Day day;
		for(Long dayId : dayIds) {
			day = dayService.readDay(dayId);
			weekIds.add(day.getWeekScheduleId());
		}
		
		
		
		
		WeekSchedule censoredWeekSchedule = this.getWeekForPublisher(weekId, publisherId, dayIds, timeSlotIds);
		
		return censoredWeekSchedule;
	}

	@Override
	public WeekSchedule updateWeekForPublisher(Long publisherId, WeekSchedule weekSchedule) {
		
		// get week in DB
		WeekSchedule weekRepo = this.getWeekForPublisher(weekSchedule.getId(), publisherId);
		
		// extract all timeSlots from provided weekSchedule object
		Set<TimeSlot> timeSlotsProv = new HashSet<TimeSlot>();
		
		for(Day days : weekRepo.getWeek()) {
			for(TimeSlot ts : days.getTimeSlots()){
				timeSlotsProv.add(ts);
			}
		}
		
		// extract all timeSlots from repo weekSchedule object
		Set<TimeSlot> timeSlotsRepo = new HashSet<TimeSlot>();
		
		for(Day days : weekSchedule.getWeek()) {
			for(TimeSlot ts : days.getTimeSlots()){
				timeSlotsRepo.add(ts);
			}
		}
		
		// Begin storing ModifiedAssignments
		List<ModifiedAssignment> ma = new ArrayList<ModifiedAssignment>();
		
		LocalTime startTimeP = null;
		Long dayIdP = null;
		Long timeSlotIdP = null;
		ModifiedAssignment assignment = null;
		Iterator<TimeSlot> it = null;
		
		for(TimeSlot tsP : timeSlotsProv) {
			// all provided TimeSlots are ADD
			assignment = new ModifiedAssignment(ModifiedAssignment.Request.ADD, publisherId);
			timeSlotIdP = tsP.getId();
			
			// NEW TIMESLOT
			if(timeSlotIdP == null) {
				
				startTimeP = tsP.getStartTime();
				dayIdP = tsP.getDayId();
				
				assignment.setStartTime(startTimeP);
				assignment.setDayId(dayIdP);
				
				ma.add(assignment);
				
				continue;

			}
			
			// UPDATING AN ALREADY EXISTING TIMESLOT
			timeSlotIdP = tsP.getId();
			assignment.setId(timeSlotIdP);
			
			ma.add(assignment);
			// remove from repo set of timeSlots
			it = timeSlotsRepo.iterator();
			while(it.hasNext()) {
				if(it.next().getId() == timeSlotIdP) {
					it.remove();
					break;
				}
			}
			
		}
		
		// at this point, timeSlotsRepo contains slots that were not sent back (NEED TO BE DELETED)
		for(TimeSlot tsD : timeSlotsRepo) {
			assignment = new ModifiedAssignment(ModifiedAssignment.Request.DELETE, publisherId);
			assignment.setId(tsD.getId());
			ma.add(assignment);
		}
		
		
		// send to repository for updating repository
		weekScheduleRepository.updateAssignments(ma);
		
		
		return null;
		
	}
	

	

}
