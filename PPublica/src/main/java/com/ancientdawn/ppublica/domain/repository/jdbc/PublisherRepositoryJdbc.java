package com.ancientdawn.ppublica.domain.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ancientdawn.ppublica.domain.Assignment;
import com.ancientdawn.ppublica.domain.AssignmentsPublisher;
import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.ProfilePublisher;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.repository.DayRepository;
import com.ancientdawn.ppublica.domain.repository.PublisherRepository;
import com.ancientdawn.ppublica.domain.repository.TimeSlotRepository;
import com.ancientdawn.ppublica.util.ModifiedAssignment;

@Repository
public class PublisherRepositoryJdbc implements PublisherRepository {
	@Autowired
	private TimeSlotRepository timeSlotRepository;
	
	@Autowired
	private DayRepository dayRepository;
	
	private JdbcOperations jdbcOperations;
	private static final String SQL_INSERT_PUBLISHER = "INSERT INTO publisher (firstName, lastName, username) VALUES (?, ?, ?)";
	private static final String SQL_INSERT_PROFILE_PUBLISHER = "INSERT INTO publisher (firstName, lastName, username, password) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE_PROFILE_PUBLISHER = "UPDATE publisher SET firstName=?,lastName=?,username=?,password=? WHERE id=?";
	private static final String SQL_UPDATE_PROFILE_NO_PSWD_PUBLISHER = "UPDATE publisher SET firstName=?,lastName=?,username=? WHERE id=?";
	private static final String SQL_READ_PUBLISHER_ID = "SELECT * FROM publisher WHERE id=?";
	private static final String SQL_READ_PUBLISHER_USERNAME = "SELECT * FROM publisher WHERE username=?";
	private static final String SQL_GET_ALL_PUBLISHERS = "SELECT * FROM publisher";
	private static final String SQL_DELETE_PUBLISHER_ID = "DELETE FROM publisher WHERE id=?";
	private static final String SQL_DELETE_PUBLISHER_USERNAME = "DELETE FROM publisher WHERE username=?";
	private static final String SQL_EXISTS_PUBLISHER_USERNAME = "SELECT username FROM publisher WHERE username=?";
	private static final String SQL_EXISTS_PUBLISHER_ID = "SELECT id FROM publisher WHERE id=?";
	private static final String SQL_READ_ALL_PUBLISHERS_ID_TIMESLOT_ID = "SELECT publisherId FROM assignments WHERE timeSlotId=?";
	private static final String SQL_INSERT_ASSIGNMENT = "INSERT INTO assignments (timeSlotId, publisherId) VALUES (?, ?)"; 
	private static final String SQL_UPDATE_ASSIGNMENT = "UPDATE assignments SET timeSlotId=?, publisherId=? WHERE timeSlotId=? AND publisherId=?";
	private static final String SQL_DELETE_ASSIGNMENT = "DELETE FROM assignments WHERE timeSlotId=? AND publisherId=?";
	private static final String SQL_READ_ASSIGNMENT_TIMESLOTID_PUBLISHERID = "SELECT * FROM assignments WHERE timeSlotId = ? AND publisherId=?";
	private static final String SQL_EXISTS_ASSIGNMENT = "SELECT id FROM assignments WHERE timeSlotId=? AND publisherId=?";
	
	
	@Autowired
	public PublisherRepositoryJdbc(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	public Publisher createPublisher(Publisher publisher) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Long publisherId = null;
		if(publisher instanceof ProfilePublisher) {
			jdbcOperations.update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(SQL_INSERT_PROFILE_PUBLISHER, new String[]{"id"});
							ps.setString(1, publisher.getFirstName());
							ps.setString(2, publisher.getLastName());
							ps.setString(3, publisher.getUsername());
							ps.setString(4, ((ProfilePublisher)publisher).getPassword());
							return ps;
						}
					}, keyHolder
			);
		} else {
			jdbcOperations.update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(SQL_INSERT_PUBLISHER, new String[]{"id"});
							ps.setString(1, publisher.getFirstName());
							ps.setString(2, publisher.getLastName());
							ps.setString(3, publisher.getUsername());
							return ps;
						}
					}, keyHolder
			);
		}
		
		publisherId = (Long)keyHolder.getKey().longValue();

		publisher.setId(publisherId);
		
		return publisher;
	}
	
	private Publisher updateProfileInfoPassword(ProfilePublisher publisher) {
		// no timeSlots
		jdbcOperations.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_PROFILE_PUBLISHER);
						ps.setString(1, publisher.getFirstName());
						ps.setString(2, publisher.getLastName());
						ps.setString(3, publisher.getUsername());
						ps.setString(4, publisher.getPassword());
						
						return ps;
					}
				});
		
		return publisher;

	}
	
	private Publisher updatePublisherProfileInfo(Publisher publisher) {
		// no timeSlots
				jdbcOperations.update(
						new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
								PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_PROFILE_NO_PSWD_PUBLISHER);
								ps.setString(1, publisher.getFirstName());
								ps.setString(2, publisher.getLastName());
								ps.setString(3, publisher.getUsername());
								
								return ps;
							}
						});
				
				return publisher;
	}
	

	@Override
	public Publisher updatePublisher(Publisher publisherU) {
		
		if(publisherU instanceof ProfilePublisher) {
			return this.updateProfileInfoPassword((ProfilePublisher)publisherU);
		} else if(!(publisherU instanceof AssignmentsPublisher)) {
			return this.updatePublisherProfileInfo(publisherU);
		}
		// instance: AssignmentsPublisher
		
		AssignmentsPublisher publisher = (AssignmentsPublisher)publisherU;
		// check to see if assignments must be modified...
		// publisher has timeSlot IDs for existing timeSlots
		// new timeSlots don't have IDs
		
		Set<TimeSlot> timeSlotsReceived = publisher.getMySlots();
		Set<TimeSlot> timeSlotsRepo = timeSlotRepository.findSlotsForPublisher(publisher.getId());
						
		List<Long> timeSlotIdsReceived = new ArrayList<Long>();
		List<Long> timeSlotIdsRepo = new ArrayList<Long>();
						
		List<Long> toBeDeleted = new ArrayList<Long>();
		
		Set<TimeSlot> timeSlotsToBeCreated = new HashSet<TimeSlot>();
						
		// populate lists with all ids -> from data received and from DB
		Long id = null;
		for(TimeSlot ts : timeSlotsReceived) {
			id = ts.getId();
			// not just new assignment
			// new timeSlot needs to be created
			if(id == null) {
				timeSlotsToBeCreated.add(ts);
			} else {
				timeSlotIdsReceived.add(ts.getId());
			}
		}
						
		for(TimeSlot tsr : timeSlotsRepo) {
			timeSlotIdsRepo.add(tsr.getId());
		}
						
						
		for(Long newT : timeSlotIdsReceived) {
			if(timeSlotIdsRepo.contains(newT)) {
				// this assignment already exists
				// add to removal list.. they will not be modified
				toBeDeleted.add(newT);
			}
		}
						
		// delete common assignments from both receivedIds and repoIds list
		timeSlotIdsReceived.removeAll(toBeDeleted);
		timeSlotIdsRepo.removeAll(toBeDeleted);

		
		// create new timeSlots -> should only have 1 publisher...
		HashSet<Publisher> newTimeSlotPublishers = new HashSet<Publisher>();
		newTimeSlotPublishers.add(publisher);
		for (TimeSlot toBeCreated : timeSlotsToBeCreated) {
			toBeCreated.setPublishers(null);
			timeSlotIdsReceived.add(timeSlotRepository.createTimeSlot(toBeCreated).getId());
		}		
								
		toBeDeleted.clear();
						
		if(timeSlotIdsReceived.size() <= timeSlotIdsRepo.size()) {
			// remaining ids in timeSlotIdsRepo are those that will be updated (1:1) with new timeSlots
			int i = 0;
			Long tR = null;
			for(Long tN : timeSlotIdsReceived) {
				tR = timeSlotIdsRepo.get(i);
				this.modifyAssignment(tR, publisher.getId(), tN, publisher.getId());
				timeSlotIdsRepo.remove(tR);
				toBeDeleted.add(tN); // to delete from new publisher ids list
				i++;
			}	
							
			timeSlotIdsReceived.removeAll(toBeDeleted);
							
			// if new set of ids is same size as what already existed, both lists are empty
							
			if(timeSlotIdsReceived.size() < timeSlotIdsRepo.size()) {
				// delete remaining publisher ids (assignments)
				for(Long repoId : timeSlotIdsRepo) {
					this.deleteAssignment(repoId, publisher.getId());
				}
			}

		} else {
			// new > repo
			int i = 0;
			Long tN = null;
			for(Long tR : timeSlotIdsRepo) {
				tN = timeSlotIdsReceived.get(i);
				this.modifyAssignment(tR, publisher.getId(), tN, publisher.getId());
				timeSlotIdsReceived.remove(tN);
				i++;
			}	
													
			// create remaining publisher ids (assignments)
			for(Long newId : timeSlotIdsReceived) {
				this.createAssignment(newId, publisher.getId());
			}
							
		}
		
		this.updatePublisherProfileInfo(publisher);
		
		return publisher;
	}

	@Override
	public Publisher readPublisher(Long publisherId) {
		Publisher publisher = jdbcOperations.queryForObject(SQL_READ_PUBLISHER_ID, new PublisherRowMapper(), publisherId);
		return publisher;
	}
	
	@Override
	public Publisher readPublisher(String username) {
		Publisher publisher = jdbcOperations.queryForObject(SQL_READ_PUBLISHER_USERNAME, new PublisherRowMapper(), username);
		return publisher;
	}
	
	@Override
	public AssignmentsPublisher readAssigmentPublisher(Long publisherId) {
		Publisher publisher = jdbcOperations.queryForObject(SQL_READ_PUBLISHER_ID, new PublisherRowMapper(), publisherId);
		AssignmentsPublisher assignPub = new AssignmentsPublisher(
																publisher.getId(),
																publisher.getFirstName(),
																publisher.getLastName(),
																publisher.getUsername());
		
		assignPub.setMySlots(timeSlotRepository.findSlotsForPublisher(publisher.getId()));
		
		return assignPub;

	}
	
	@Override
	public ProfilePublisher readFullProfilePublisher(Long publisherId) {
		ProfilePublisher publisher = jdbcOperations.queryForObject(SQL_READ_PUBLISHER_ID, new ProfilePublisherRowMapper(), publisherId);
		
		return publisher;
	}
	
	@Override
	public Set<Publisher> getAllPublishers() {
		List<Publisher> allPublishers = jdbcOperations.query(SQL_GET_ALL_PUBLISHERS, new PublisherRowMapper());
		Set<Publisher> publishers = new HashSet<Publisher>(allPublishers);
		
		return publishers;
		
		
	}

	@Override
	public void deletePublisher(String username) {
		// deleting publisher -> database automatically deletes (cascade) all its assignments
		jdbcOperations.update(SQL_DELETE_PUBLISHER_USERNAME, username);
		
		Long id = (this.readPublisher(username).getId());
		
		this.deletePublisher(id);
	}

	@Override
	public void deletePublisher(Long publisherId) {
		AssignmentsPublisher publisher = this.readAssigmentPublisher(publisherId);

		// deleting publisher -> database automatically deletes (cascade) all its assignments
		jdbcOperations.update(SQL_DELETE_PUBLISHER_ID, publisherId);
								
		// make sure any empty timeslots are deleted
		timeSlotRepository.deleteEmptySlots(publisher.getMySlots());
		
	}
	

	@Override
	public boolean existsUsername(String username) {
		String us = null;
		try {
			us = jdbcOperations.queryForObject(SQL_EXISTS_PUBLISHER_USERNAME, String.class, username);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	@Override
	public boolean existsWithId(Long id) {
		Long idDb = null;
		try {
			idDb = jdbcOperations.queryForObject(SQL_EXISTS_PUBLISHER_ID, Long.class, id);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	private static final class PublisherRowMapper implements RowMapper<Publisher> {

		@Override
		public Publisher mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new Publisher(rs.getLong("id"),
					rs.getString("firstName"),
					rs.getString("lastName"),
					rs.getString("username")
					);
		}
		
	}

	
	private static final class ProfilePublisherRowMapper implements RowMapper<ProfilePublisher> {

		@Override
		public ProfilePublisher mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new ProfilePublisher(rs.getLong("id"),
					rs.getString("firstName"),
					rs.getString("lastName"),
					rs.getString("username"),
					rs.getString("password")
					);
		}
		
	}
	
	private static final class AssignmentRowMapper implements RowMapper<Assignment> {

		@Override
		public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new Assignment(rs.getLong("id"),
					rs.getLong("timeSlotId"),
					rs.getLong("publisherId")
					);
		}
		
	}
	
	
	
	public void setTimeSlotRepository(TimeSlotRepository timeSlotRepository) {
		this.timeSlotRepository = timeSlotRepository;
	}

	@Override
	public Set<Publisher> getPublishersInTimeSlot(Long timeSlotId) {
		// build list of publisher ids
		List<Long> publisherIds = jdbcOperations.queryForList(SQL_READ_ALL_PUBLISHERS_ID_TIMESLOT_ID, Long.class, timeSlotId);
		
		// for each id, create a publisher object
		Set<Publisher> publishers = new HashSet<Publisher>();

		for(Long id : publisherIds) {
			publishers.add(this.readPublisher(id));
		}

		return publishers;
	}


	@Override
	public void modifyAssignment(Long oldTimeSlotId, Long oldPublisherId, Long timeSlotId, Long publisherId) {

		jdbcOperations.update(SQL_UPDATE_ASSIGNMENT,
				  timeSlotId,
				  publisherId,
				  oldTimeSlotId,
				  oldPublisherId
				  );
	}

	@Override
	public void deleteAssignment(Long timeSlotId, Long publisherId) {
		jdbcOperations.update(SQL_DELETE_ASSIGNMENT, timeSlotId, publisherId);
		
		TimeSlot ts = timeSlotRepository.readTimeSlot(timeSlotId);
		
		if(ts != null) {
			if(ts.getPublishers() == null || ts.getPublishers().size() == 0) {
				timeSlotRepository.deleteTimeSlot(timeSlotId);
			}
		}
		
	}

	@Override
	public void createAssignment(Long timeSlotId, Long publisherId) {
		jdbcOperations.update(SQL_INSERT_ASSIGNMENT, timeSlotId, publisherId);
		
	}
	
	// used when a timeSlot must be created
	@Override
	public void createAssignment(Long dayId, LocalTime startTime, Long publisherId) {
		// Build a timeSlot object
		
		Day day = dayRepository.readDayProps(dayId);
		TimeSlot tsNew = new TimeSlot();
		Publisher publisher = new Publisher();
		publisher.setId(publisherId);
		LocalTime endTime = startTime.plus(day.getDuration());
		
		
		tsNew.setDayId(dayId);
		tsNew.setMaxPublishers(day.getDefaultMaxPublishers());
		tsNew.setPublishers(new HashSet<Publisher>(Arrays.asList(publisher)));
		tsNew.setStartTime(startTime);
		tsNew.setEndTime(endTime);
		
		timeSlotRepository.createTimeSlot(tsNew);
		
	}

	@Override
	public Assignment getAssignment(Long timeSlotId, Long publisherId) {
		Assignment assignment = null;
		if(existsAssignment(timeSlotId, publisherId)) {
			assignment = jdbcOperations.queryForObject(SQL_READ_ASSIGNMENT_TIMESLOTID_PUBLISHERID, new AssignmentRowMapper(), timeSlotId, publisherId);
		}

		return assignment;
	}
	
	@Override
	public boolean existsAssignment(Long timeSlotId, Long publisherId) {
		Long idDb = null;
		try {
			idDb = jdbcOperations.queryForObject(SQL_EXISTS_ASSIGNMENT, Long.class, timeSlotId, publisherId);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}
	}



}
