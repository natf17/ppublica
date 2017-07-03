package com.ancientdawn.ppublica.util;

import java.time.LocalTime;

public class ModifiedAssignment {
	
	public enum Request {
		DELETE,
		ADD
	}
	
	// set if slot already exists
	private Long timeSlotId;
	
	// set if slot will be created
	private LocalTime startTime;
	private Long dayId;
	
	private final Request requestType;
	private final Long publisherId;
	
	
	public ModifiedAssignment(Request request, Long publisherId) {
		this.requestType = request;
		this.publisherId = publisherId;
	}
	
	public Long getPublisherId() {
		return publisherId;
	}
	
	public Request getRequestType() {
		return requestType;
	}
	
	public Long getId() {
		return timeSlotId;
	}
	
	public void setId(Long timeSlotId) {
		this.timeSlotId = timeSlotId;
		System.out.println(timeSlotId);

	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		System.out.println(startTime);

	}
	
	public Long getDayId() {
		return dayId;
	}
	
	public void setDayId(Long dayId) {
		this.dayId = dayId;
		System.out.println(dayId);

	}
}
