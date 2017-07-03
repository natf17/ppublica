package com.ancientdawn.ppublica.validator.util;

import com.ancientdawn.ppublica.domain.WeekSchedule;

/*
 * Objects of this class will hold a reference to the WeekSchedule provided and the publisherId to be sent to a validator
 */
public class WeekPublisherAuth {
	private Long publisherId;
	private WeekSchedule weekSchedule;
	
	public WeekPublisherAuth(Long publisherId, WeekSchedule weekSchedule) {
		this.publisherId = publisherId;
		this.weekSchedule = weekSchedule;
	}
	
	public Long getPublisherId() {
		return publisherId;
	}
	
	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}
	
	public WeekSchedule getWeekSchedule() {
		return weekSchedule;
	}
	
	public void setWeekSchedule(WeekSchedule weekSchedule) {
		this.weekSchedule = weekSchedule;
	}
}
