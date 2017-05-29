package com.ancientdawn.ppublica.service.impl;

import java.time.DayOfWeek;
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
		
		
		// grab full weekSchedules
		Set<WeekSchedule> censoredWeeks = new HashSet<WeekSchedule>();
		WeekSchedule weekSchedule;
		WeekSchedule censoredWeek;
		Day censoredDay;
		SortedSet<TimeSlot> censoredSlots;
		Set<Day> censoredDays;
		for(Long weekId : weekIds) {
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
							// if day is not contained in dayIds for publisher, don't add to censoredWeek
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
			
			censoredWeeks.add(censoredWeek);
			
		}
		
		
		return censoredWeeks;
	}

}
