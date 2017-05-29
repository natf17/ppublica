package com.ancientdawn.ppublica.service;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.ancientdawn.ppublica.domain.TimeSlot;

public interface TimeSlotService {
	TimeSlot createTimeSlot(TimeSlot timeSlot);
	TimeSlot updateTimeSlot(TimeSlot timeSlot);
	TimeSlot readTimeSlot(Long timeSlotId);
	void deleteTimeSlot(Long timeSlotId);
	List<TimeSlot> getAllTimeSlotsForWeek(Long weekScheduleId);
	Set<TimeSlot> getAllTimeSlots();
	Set<TimeSlot> getAllTimeSlotsForPublisher(Long publisherId);
	SortedSet<TimeSlot> getTimeSlotsInDay(Long id);
}
