package com.ancientdawn.ppublica.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ancientdawn.ppublica.domain.Assignment;
import com.ancientdawn.ppublica.domain.AssignmentsPublisher;
import com.ancientdawn.ppublica.domain.ProfilePublisher;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.repository.PublisherRepository;
import com.ancientdawn.ppublica.service.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {

	PublisherRepository publisherRepository;
	
	@Autowired
	public PublisherServiceImpl(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}
	
	@Override
	public Publisher createPublisher(Publisher publisher) {
		return publisherRepository.createPublisher(publisher);
	}

	@Override
	public Publisher updatePublisher(Publisher publisher) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publisher readPublisher(Long publisherId) {
		if(!existsWithId(publisherId)) {
			System.out.println("noyt foumnd");
			return null;
		}
		return publisherRepository.readPublisher(publisherId);
	}
	
	@Override
	public Publisher readPublisher(String username) {
		if(!existsUsername(username)) {
			return null;
		}
		return publisherRepository.readPublisher(username);
	}

	@Override
	public void deletePublisher(Publisher publisher) {
		publisherRepository.deletePublisher(publisher.getUsername());
	}

	@Override
	public void deletePublisher(Long publisherId) {
		publisherRepository.deletePublisher(publisherId);
	}

	@Override
	public boolean existsUsername(String username) {
		return publisherRepository.existsUsername(username);
	}

	@Override
	public boolean existsWithId(Long id) {
		return publisherRepository.existsWithId(id);
	}

	@Override
	public void modifyAssignment(Long oldTimeSlotId, Long oldPublisherId, Long timeSlotId, Long publisherId) {
		publisherRepository.modifyAssignment(oldTimeSlotId, oldPublisherId, timeSlotId, publisherId);
	}

	@Override
	public void deleteAssignment(Long timeSlotId, Long publisherId) {
		publisherRepository.deleteAssignment(timeSlotId, publisherId);
		
	}

	@Override
	public void createAssignment(Long timeSlotId, Long publisherId) {
		publisherRepository.createAssignment(timeSlotId, publisherId);
		
	}

	@Override
	public Set<Publisher> getPublishersInTimeSlot(Long timeSlotId) {
		return publisherRepository.getPublishersInTimeSlot(timeSlotId);
	}

	@Override
	public Set<Publisher> getAllPublishers() {
		return publisherRepository.getAllPublishers();
	}

	@Override
	public AssignmentsPublisher readAssigmentPublisher(Long publisherId) {
		return publisherRepository.readAssigmentPublisher(publisherId);
	}

	@Override
	public ProfilePublisher readFullProfilePublisher(Long publisherId) {
		return publisherRepository.readFullProfilePublisher(publisherId);
	}

	@Override
	public Assignment getAssignment(Long timeSlotId, Long publisherId) {
		return publisherRepository.getAssignment(timeSlotId, publisherId);
	}

	@Override
	public boolean existsAssignment(Long timeSlotId, Long publisherId) {
		return publisherRepository.existsAssignment(timeSlotId, publisherId);
	}

}
