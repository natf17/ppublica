package com.ancientdawn.ppublica.service.impl;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.repository.TimeSlotRepository;
import com.ancientdawn.ppublica.service.PublisherService;
import com.ancientdawn.ppublica.service.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
	private TimeSlotRepository timeSlotRepository;
	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
		this.timeSlotRepository = timeSlotRepository;
	}

	@Override
	public TimeSlot createTimeSlot(TimeSlot timeSlot) {
		return timeSlotRepository.createTimeSlot(timeSlot);
	}

	@Override
	public TimeSlot updateTimeSlot(TimeSlot timeSlot) {
		/*
		 * TimeSlot MUST HAVE id
		 * TimeSlot MUST HAVE publisher ids
		 */
		

		return timeSlotRepository.updateTimeSlot(timeSlot);
	}

	@Override
	public TimeSlot readTimeSlot(Long timeSlotId) {
		return timeSlotRepository.readTimeSlot(timeSlotId);
	}


	@Override
	public void deleteTimeSlot(Long timeSlotId) {
		timeSlotRepository.deleteTimeSlot(timeSlotId);
	}

	@Override
	public List<TimeSlot> getAllTimeSlotsForWeek(Long weekScheduleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TimeSlot> getAllTimeSlots() {
		return timeSlotRepository.getAllTimeSlots();
	}

	@Override
	public Set<TimeSlot> getAllTimeSlotsForPublisher(Long publisherId) {
		
		return timeSlotRepository.findSlotsForPublisher(publisherId);
	}



	@Override
	public SortedSet<TimeSlot> getTimeSlotsInDay(Long id) {
		return timeSlotRepository.getTimeSlotsInDay(id);
	}

}
