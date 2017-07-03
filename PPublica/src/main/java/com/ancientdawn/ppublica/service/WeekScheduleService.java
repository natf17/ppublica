package com.ancientdawn.ppublica.service;

import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import java.util.List;
import java.util.Set;

/* 
 * This service will be primarily used by administrator who can make changes
 * to week schedule 
 */

public interface WeekScheduleService {
	WeekSchedule createWeekSchedule(WeekSchedule weekSchedule);
	WeekSchedule updateWeekSchedule(WeekSchedule weekSchedule);
	WeekSchedule readWeekSchedule(Long weekScheduleId);
	void deleteWeekSchedule(Long weekScheduleId);
	boolean existsWeek(long id);
	Set<WeekSchedule> getAllSchedules();
	Set<WeekSchedule> getWeeksForPublisher(Long id);
	WeekSchedule getWeekForPublisher(Long weekId, Long publisherId, Set<Long> dayIds, Set<Long> timeSlotIds);
	WeekSchedule getWeekForPublisher(Long weekId, Long publisherId);
	WeekSchedule updateWeekForPublisher(Long publisherId, WeekSchedule weekSchedule);

}
