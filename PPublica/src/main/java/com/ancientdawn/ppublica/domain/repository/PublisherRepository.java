package com.ancientdawn.ppublica.domain.repository;

import com.ancientdawn.ppublica.domain.Assignment;
import com.ancientdawn.ppublica.domain.AssignmentsPublisher;
import com.ancientdawn.ppublica.domain.ProfilePublisher;
import com.ancientdawn.ppublica.domain.Publisher;
import java.util.Set;

public interface PublisherRepository {
	Publisher createPublisher(Publisher publisher);
	Publisher updatePublisher(Publisher publisher);
	Publisher readPublisher(Long publisherId);
	Publisher readPublisher(String username);
	AssignmentsPublisher readAssigmentPublisher(Long publisherId);
	ProfilePublisher readFullProfilePublisher(Long publisherId);
	Set<Publisher> getAllPublishers();
	void deletePublisher(String username);
	void deletePublisher(Long publisherId);
	boolean existsUsername(String username);
	boolean existsWithId(Long id);
	Set<Publisher> getPublishersInTimeSlot(Long timeSlotId);
	void modifyAssignment(Long oldTimeSlotId, Long oldPublisherId, Long timeSlotId, Long publisherId);
	void deleteAssignment(Long timeSlotId, Long publisherId);
	void createAssignment(Long timeSlotId, Long publisherId);
	Assignment getAssignment(Long timeSlotId, Long publisherId);
	boolean existsAssignment(Long timeSlotId, Long publisherId);
}
