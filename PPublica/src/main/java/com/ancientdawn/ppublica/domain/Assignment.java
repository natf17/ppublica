package com.ancientdawn.ppublica.domain;

public class Assignment {
	private Long id;
	private Long timeSlotId;
	private Long publisherId;
	
	public Assignment() {
		id = null;
		timeSlotId = null;
		publisherId = null;
	}
	
	
	public Assignment(Long timeSlotId, Long publisherId) {
		this(null, timeSlotId, publisherId);
	}
	
	public Assignment(Long id, Long timeSlotId, Long publisherId) {
		this.id = id;
		this.timeSlotId = timeSlotId;
		this.publisherId = publisherId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTimeSlotId() {
		return timeSlotId;
	}
	
	public void setTimeSlotId(Long timeSlotId) {
		this.timeSlotId = timeSlotId;
	}
	
	public Long getPublisherId() {
		return publisherId;
	}
	
	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}
	
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		Assignment other = (Assignment)obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		}
		
		else if (!id.equals(other.id)){
			return false;
		}
		
		if (timeSlotId == null) {
			if (other.getTimeSlotId() != null)
				return false;
		}
		
		else if (!timeSlotId.equals(other.timeSlotId)){
			return false;
		}
		
		if (publisherId == null) {
			if (other.getPublisherId() != null)
				return false;
		}
		
		else if (!publisherId.equals(other.publisherId)){
			return false;
		}

		
		return true;
		
	}

	
	@Override 
	public int hashCode() {
		final int prime = 13;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((timeSlotId == null) ? 0 : timeSlotId.hashCode());
		result = prime * result + ((publisherId == null) ? 0 : publisherId.hashCode());
		
		return result;
	}
}
