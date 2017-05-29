package com.ancientdawn.ppublica.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.repository.DayRepository;
import com.ancientdawn.ppublica.service.TimeSlotService;

@Service
public class DayServiceImpl implements com.ancientdawn.ppublica.service.DayService {
	private DayRepository dayRepository;
	@Autowired
	private TimeSlotService timeSlotService;
	
	@Autowired
	public DayServiceImpl(DayRepository dayRepository) {
		this.dayRepository = dayRepository;
		
	}
	
	@Override
	public Day createDay(Day day) {
		return dayRepository.createDay(day);
	}

	@Override
	public Day updateDay(Day day) {
		/*
		 * Day MUST HAVE id
		 */
		
		return dayRepository.updateDay(day);
		
	}

	@Override
	public Day readDay(Long dayId) {
		return dayRepository.readDay(dayId);
	}


	@Override
	public void deleteDay(Long dayId) {
		dayRepository.deleteDay(dayId);

	}


	@Override
	public Set<Day> getDaysInWeek(Long weekScheduleId) {
		return dayRepository.getDaysInWeek(weekScheduleId);
	}

	@Override
	public boolean existsDay(long dayId) {
		return dayRepository.existsDay(dayId);
	}

	@Override
	public Set<Day> getAllDays() {
		return dayRepository.getAllDays();
	}



}
