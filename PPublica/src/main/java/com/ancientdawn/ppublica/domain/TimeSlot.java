package com.ancientdawn.ppublica.domain;

import java.time.LocalTime;
import java.util.Set;
import java.util.HashSet;

public class TimeSlot implements Comparable<TimeSlot>{
	private Long id;
	private Long dayId;
	private LocalTime startTime;   //start time of first slot
	private LocalTime endTime;
	private Set<Publisher> publishers;
	private Short maxPublishers;
	
	public TimeSlot(LocalTime startTime) {
		this(null, null, startTime, null, null);
	}
	
	public TimeSlot() {
		this(null);
	}
	
	public TimeSlot(Long id, Long dayId, LocalTime startTime, LocalTime endTime, Short maxPublishers) {
		System.out.println("CONST: set");
		this.id = id;
		System.out.println(id);
		this.dayId = dayId;
		System.out.println(dayId);
		this.startTime = startTime;
		System.out.println(startTime);
		this.endTime = endTime;
		System.out.println(endTime);
		this.publishers = new HashSet<Publisher>();
		System.out.println(publishers);
		this.maxPublishers = maxPublishers;
		System.out.println(maxPublishers);

	}
	
	public Long getId() {
		return id;
	}
	
	// set automatically by database, not user
	public void setId(Long id) {
		this.id = id;
		System.out.println(id);

	}
	
	public Long getDayId() {
		return dayId;
	}
	
	public void setDayId(Long dayId) {
		this.dayId = dayId;
		System.out.println(dayId);

	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		System.out.println(startTime);

	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		System.out.println(endTime);

	}
	
	public Set<Publisher> getPublishers() {
		return publishers;
	}
	
	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
		System.out.println("SET PUBS: " + publishers.size());

	}
	
	public void addPublisher(Publisher publisher) {
		publishers.add(publisher);
	}
	
	public void setMaxPublishers(Short maxPublishers) {
		this.maxPublishers = maxPublishers;
		System.out.println(maxPublishers);

	}
	
	public Short getMaxPublishers() {
		return maxPublishers;
	}
	
	@Override
	public boolean equals(Object obj) {
		System.out.println("Equals called");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		TimeSlot other = (TimeSlot)obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		}
		
		else if (!id.equals(other.id)){
			return false;
		}
		
		if (dayId == null) {
			if (other.getDayId() != null)
				return false;
		}
		
		else if (!dayId.equals(other.dayId)){
			return false;
		}
		
		if (startTime == null) {
			if (other.getStartTime() != null)
				return false;
		}
		
		else if (!startTime.equals(other.startTime)){
			return false;
		}

		System.out.println("They are Equal!!!!!!!!!");
		return true;
		
	}
	
	@Override
	public int compareTo(TimeSlot other) {
		LocalTime otherStartTime = other.getStartTime();
		LocalTime thisStartTime = this.getStartTime();
		
		if(thisStartTime.isBefore(otherStartTime)) {
			System.out.println("comapred: " + -1);

			return -1;
		} 
		
		if(thisStartTime.isAfter(otherStartTime)) {
			System.out.println("comapred: " + 1);

			return 1;
		} 
		System.out.println("comapred: " + 0);

		return 0;
				
	}

	
	@Override 
	public int hashCode() {
		final int prime = 71;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((dayId == null) ? 0 : dayId.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());

		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id + "\n");
		sb.append("dayId: " + dayId + "\n");
		sb.append("startTime: " + startTime + "\n");
		sb.append("endTime: " + endTime + "\n");
		sb.append("maxPublishers: " + maxPublishers + "\n");
		sb.append("publishers: " + publishers + "\n");


		return sb.toString();
	}
}
