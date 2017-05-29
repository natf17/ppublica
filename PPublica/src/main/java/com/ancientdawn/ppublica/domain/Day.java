package com.ancientdawn.ppublica.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.Duration;


public class Day implements Comparable<Day>{
	private Long id;
	private Long weekScheduleId;
	private DayOfWeek weekday;
	private LocalTime minTime;   //start time of first slot
	private LocalTime maxTime;   //end time of last slot
	private Duration duration; //the default duration of all time slots during the day
	private Short defaultMaxPublishers;
	private Set<TimeSlot> timeSlots;
	
	// default
	public Day(DayOfWeek dayOfTheWeek) {
		this(dayOfTheWeek, LocalTime.of(6, 0), LocalTime.of(20, 0), Duration.ofMinutes(30), (short)2);
		
	}
	
	public Day() {
		this(null);
	}
	
	public Day(DayOfWeek dayOfWeek, LocalTime minTime, LocalTime maxTime, Duration duration, Short defaultMaxPublishers) {
		this(null, null, dayOfWeek, minTime, maxTime, duration, defaultMaxPublishers);

	}
	
	public Day(Long id, Long weekScheduleId, DayOfWeek dayOfWeek, LocalTime minTime, LocalTime maxTime, Duration duration, Short defaultMaxPublishers) {
		this.id = id;
		this.weekScheduleId = weekScheduleId;
		this.weekday = dayOfWeek;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.duration = duration;
		this.defaultMaxPublishers = defaultMaxPublishers;
		this.timeSlots = new HashSet<TimeSlot>();

	}
		
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setWeekScheduleId(Long weekScheduleId) {
		this.weekScheduleId = weekScheduleId;
	}
	
	public Long getWeekScheduleId() {
		return weekScheduleId;
	}
	
	public void setWeekday(DayOfWeek day) {
		this.weekday = day;
	}
	
	public DayOfWeek getWeekday() {
		return weekday;
	}
	
	public void setMinTime(LocalTime minTime) {
		this.minTime = minTime;
	}
	
	public LocalTime getMinTime() {
		return minTime;
	}
	
	public void setMaxTime(LocalTime maxTime) {
		this.maxTime = maxTime;
	}
	
	public LocalTime getMaxTime() {
		return maxTime;
	}
	
	public void setDefaultMaxPublishers(Short defaultMaxPublishers) {
		this.defaultMaxPublishers = defaultMaxPublishers;
	}
	
	public Short getDefaultMaxPublishers() {
		return defaultMaxPublishers;
	}
	
	
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	public Duration getDuration() {
		return duration;
	}
	
	public void setTimeSlots(SortedSet<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
		System.out.println("setting TimeSlots: " + weekday + ": " + timeSlots.size());
		System.out.println(timeSlots);
	}
	
	public Set<TimeSlot> getTimeSlots() {
		return timeSlots;
	}
	
	public void addTimeSlot(TimeSlot slot) {
		if(slot.getStartTime().isAfter(minTime) || slot.getStartTime().equals(minTime)) {
			if(slot.getEndTime().isBefore(maxTime) || slot.getEndTime().equals(maxTime)) {
				timeSlots.add(slot);
				return;
			}
		}
			
		throw new RuntimeException("NOT WITHIN TIME RANGE");
	}
	
	public void removeTimeSlot(TimeSlot slot) {
		timeSlots.remove(slot);
		
	}
	
	public void clearTimeSlots() {
		timeSlots.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		Day other = (Day)obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		}
		
		else if (!id.equals(other.id)){
			return false;
		}
		
		if (weekScheduleId == null) {
			if (other.getWeekScheduleId() != null)
				return false;
		}
		
		else if (!weekScheduleId.equals(other.weekScheduleId)){
			return false;
		}
		
		if (weekday == null) {
			if (other.getWeekday() != null)
				return false;
		}
		
		else if (!weekday.equals(other.weekday)){
			return false;
		}
		
		return true;
		
	}

	
	@Override 
	public int hashCode() {
		final int prime = 73;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((weekScheduleId == null) ? 0 : weekScheduleId.hashCode());
		result = prime * result + ((weekday == null) ? 0 : weekday.hashCode());
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id + "\n");
		sb.append("weekScheduleId: " + weekScheduleId + "\n");
		sb.append("weekDay: " + weekday + "\n");
		sb.append("minTime: " + minTime + "\n");
		sb.append("maxTime: " + maxTime + "\n");
		sb.append("duration: " + duration + "\n");
		sb.append("timeSlots: " + timeSlots + "\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Day other) {
		int otherWeekDayValue = other.getWeekday().getValue();
		int thisWeekDayValue = this.getWeekday().getValue();
		
		
		if(thisWeekDayValue < otherWeekDayValue) {
			return -1;
		}
		if(thisWeekDayValue > otherWeekDayValue) {
			return 1;
		}
		return 0;
	}
}
