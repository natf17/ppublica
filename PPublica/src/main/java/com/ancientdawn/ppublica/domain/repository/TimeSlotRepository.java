package com.ancientdawn.ppublica.domain.repository;

import com.ancientdawn.ppublica.domain.TimeSlot;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface TimeSlotRepository {
	TimeSlot createTimeSlot(TimeSlot timeSlot);
	TimeSlot updateTimeSlot(TimeSlot timeSlot);
	TimeSlot readTimeSlot(Long timeSlotId);
	void deleteTimeSlot(Long timeSlotId);
	Set<TimeSlot>findSlotsForPublisher(long publisherId);
	void deleteEmptySlots(Set<TimeSlot> timeSlots);
	SortedSet<TimeSlot> getTimeSlotsInDay(Long dayId);
	TimeSlot readBasicTimeSlot(Long timeSlotId);
	Set<TimeSlot> getAllTimeSlots();

}
