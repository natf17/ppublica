package com.ancientdawn.ppublica.service;

import java.util.Set;

import com.ancientdawn.ppublica.domain.Day;

public interface DayService {
	Day createDay(Day day);
	Day updateDay(Day day);
	Day readDay(Long dayId);
	void deleteDay(Long dayId);
	Set<Day> getDaysInWeek(Long weekScheduleId);
	Set<Day> getAllDays();
	boolean existsDay(long dayId);

	
}
