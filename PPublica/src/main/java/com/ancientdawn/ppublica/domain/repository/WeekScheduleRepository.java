package com.ancientdawn.ppublica.domain.repository;

import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.util.ModifiedAssignment;

import java.util.List;
import java.util.Set;

public interface WeekScheduleRepository {
	WeekSchedule createWeekSchedule(WeekSchedule weekSchedule);
	WeekSchedule updateWeekSchedule(WeekSchedule weekSchedule);
	WeekSchedule readWeekSchedule(Long weekScheduleId);
	void deleteWeekSchedule(Long weekScheduleId);
	boolean existsWeek(long id);
	Set<WeekSchedule> getAllSchedules();
	List<ModifiedAssignment> updateAssignments(List<ModifiedAssignment> assignments);
}
