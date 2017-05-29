package com.ancientdawn.ppublica.domain.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.repository.PublisherRepository;
import com.ancientdawn.ppublica.domain.repository.TimeSlotRepository;

@Repository
public class TimeSlotRepositoryJdbc implements TimeSlotRepository {
	
	@Autowired
	private PublisherRepository publisherRepository;
	private JdbcOperations jdbcOperations;
	private static final String SQL_READ_ALL_TIMESLOTS_DAY_ID = "SELECT * FROM timeSlot WHERE dayId=?";
	private static final String SQL_READ_ALL_TIMESLOTS_PUBLISHER_ID = "SELECT timeSlotId FROM assignments WHERE publisherId=?";
	private static final String SQL_READ_TIMESLOT_ID = "SELECT * FROM timeSlot WHERE id=?";
	private static final String SQL_INSERT_TIMESLOT = "INSERT INTO timeSlot (dayId, startTime, endTime, maxPublishers) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE_TIMESLOT_ID = "UPDATE timeSlot SET startTime=?, endTime=?, maxPublishers=? WHERE id=? AND dayId=?";
	private static final String SQL_DELETE_ID = "DELETE FROM timeSlot WHERE id=?";
	private static final String SQL_GET_ALL_TIMESLOTS = "SELECT * FROM timeSlot";

	
	@Autowired
	public TimeSlotRepositoryJdbc(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public TimeSlot createTimeSlot(TimeSlot timeSlot) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Long timeSlotId = null;
		
		// insert into weekSchedule table
		jdbcOperations.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TIMESLOT, new String[]{"id"});
						ps.setLong(1, timeSlot.getDayId());
						ps.setLong(2, timeSlot.getStartTime().toSecondOfDay());
						ps.setLong(3, timeSlot.getEndTime().toSecondOfDay());
						ps.setLong(4, timeSlot.getMaxPublishers());
						
						return ps;
					}
				}, keyHolder
				
		);
		
		timeSlotId = (Long)keyHolder.getKey().longValue();
		
		timeSlot.setId(timeSlotId);
		
		// create assignment for publishers in timeSlot...
		// when created, publisher id must be provided
		if(timeSlot.getPublishers() != null) {
			for(Publisher publisher : timeSlot.getPublishers()) {
				publisherRepository.createAssignment(timeSlot.getId(), publisher.getId());
			}
		}
		
		return timeSlot;
	}

	@Override
	public TimeSlot updateTimeSlot(TimeSlot timeSlot) {
		// check to see if assignments must be modified...
		// timeSlot has publisher IDs
		Set<Publisher> publishersNew = timeSlot.getPublishers();
		Set<Publisher> publishersRepo = publisherRepository.getPublishersInTimeSlot(timeSlot.getId());
				
		List<Long> publisherIdsNew = new ArrayList<Long>();
		List<Long> publisherIdsRepo = new ArrayList<Long>();
				
		List<Long> toBeDeleted = new ArrayList<Long>();
				
		// populate lists with all ids -> from data received and from DB
		for(Publisher p : publishersNew) {
			publisherIdsNew.add(p.getId());
		}
				
		for(Publisher pr : publishersRepo) {
			publisherIdsRepo.add(pr.getId());
		}
				
				
		for(Long newP : publisherIdsNew) {
			if(publisherIdsRepo.contains(newP)) {
				// this assignment already exists
				// add to removal list.. they will not be modified
				toBeDeleted.add(newP);
			}
		}
				
		// delete common assignments from both newIds and repoIds list
		publisherIdsNew.removeAll(toBeDeleted);
				
		publisherIdsRepo.removeAll(toBeDeleted);
				
		toBeDeleted.clear();
				
		if(publisherIdsNew.size() <= publisherIdsRepo.size()) {
			// remaining ids in publisherIdsRepo are those that will be updated (1:1) with new publishers
			int i = 0;
			Long pR = null;
			for(Long pN : publisherIdsNew) {
				pR = publisherIdsRepo.get(i);
				publisherRepository.modifyAssignment(timeSlot.getId(), pR,timeSlot.getId(), pN);
				publisherIdsRepo.remove(pR);
				toBeDeleted.add(pN); // to delete from new publisher ids list
				i++;
			}	
					
			publisherIdsNew.removeAll(toBeDeleted);
					
			// if new set of ids is same size as what already existed, both lists are empty
					
			if(publisherIdsNew.size() < publisherIdsRepo.size()) {
				// delete remaining publisher ids (assignments)
				for(Long repoId : publisherIdsRepo) {
					publisherRepository.deleteAssignment(timeSlot.getId(), repoId);
				}
			}

		} else {
			// new > repo
			int i = 0;
			Long pN = null;
			for(Long pR : publisherIdsRepo) {
				pN = publisherIdsNew.get(i);
				publisherRepository.modifyAssignment(timeSlot.getId(), pR, timeSlot.getId(), pN);
				publisherIdsNew.remove(pN);
				i++;
			}	
											
			// create remaining publisher ids (assignments)
			for(Long newId : publisherIdsNew) {
				publisherRepository.createAssignment(timeSlot.getId(), newId);
			}
					
		}
		
		
		
		
		
		
		
		// timeSlotId will never be updated...
		// use id and dayId to identify timeSlot
		int i = jdbcOperations.update(SQL_UPDATE_TIMESLOT_ID,
							  timeSlot.getStartTime().toSecondOfDay(),
							  timeSlot.getEndTime().toSecondOfDay(),
							  timeSlot.getMaxPublishers(),
							  timeSlot.getId(),
							  timeSlot.getDayId()
							  );
		System.out.println("ROWS AFFECTED: " + i);
		System.out.println("PRINT!!!!!!!: ");

		System.out.println("id/dayID: " + timeSlot.getId() + "/" + timeSlot.getDayId());

		return timeSlot;
	}

	@Override
	public TimeSlot readTimeSlot(Long timeSlotId) {
		// build TimeSlot object from TimeSlot table
		TimeSlot timeSlot = jdbcOperations.queryForObject(SQL_READ_TIMESLOT_ID, new TimeSlotRowMapper(), timeSlotId);

		// get all publishers (simplified) in week
		timeSlot.setPublishers(publisherRepository.getPublishersInTimeSlot(timeSlotId));
		
		return timeSlot;
	}

	@Override
	public void deleteTimeSlot(Long timeSlotId) {

		// delete a timeSlot
		// corresponding assignments are deleted via SQL ON DELETE CASCADE
		jdbcOperations.update(SQL_DELETE_ID, timeSlotId);
			
	}

	@Override
	public Set<TimeSlot> findSlotsForPublisher(long publisherId) {
		// build list of publisher ids
		List<Long> timeSlotIds = jdbcOperations.queryForList(SQL_READ_ALL_TIMESLOTS_PUBLISHER_ID, Long.class, publisherId);
		System.out.println(timeSlotIds);
		System.out.println("findslotsforpub REPOSITORY called : " + publisherId);
		// for each id, create a timeSlot object
		Set<TimeSlot> timeSlots = new HashSet<TimeSlot>();

		for(Long id : timeSlotIds) {
			timeSlots.add(this.readBasicTimeSlot(id));
		}
		
		return timeSlots;
	}

	@Override
	public SortedSet<TimeSlot> getTimeSlotsInDay(Long dayId) {
		// build list of timeSlots from timeSlots table
		List<TimeSlot> queryResults = jdbcOperations.query(SQL_READ_ALL_TIMESLOTS_DAY_ID, new TimeSlotRowMapper(), dayId);
			
		// populate with corresponding publishers
		Set<Publisher> publishers = null;
		for(TimeSlot timeSlot : queryResults) {
			publishers = publisherRepository.getPublishersInTimeSlot(timeSlot.getId());
			timeSlot.setPublishers(publishers);
		}
		return new TreeSet<TimeSlot>(queryResults);
	}
	
	private static final class TimeSlotRowMapper implements RowMapper<TimeSlot> {

		@Override
		public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new TimeSlot(rs.getLong("id"),
					rs.getLong("dayId"),
					LocalTime.ofSecondOfDay(rs.getInt("startTime")),
					LocalTime.ofSecondOfDay(rs.getInt("endTime")),
					rs.getShort("maxPublishers")
					);
		}
		
	}

	@Override
	public TimeSlot readBasicTimeSlot(Long timeSlotId) {
		TimeSlot timeSlot = jdbcOperations.queryForObject(SQL_READ_TIMESLOT_ID, new TimeSlotRowMapper(), timeSlotId);

		return timeSlot;
	}

	
	@Override
	public void deleteEmptySlots(Set<TimeSlot> timeSlots) {
		Set<Publisher> pubsInSlot = null;
		for(TimeSlot slot : timeSlots) {
			pubsInSlot = slot.getPublishers();
			if(pubsInSlot == null || pubsInSlot.size() == 0) {
				this.deleteTimeSlot(slot.getId());
			}
		}
		
	}

	@Override
	public Set<TimeSlot> getAllTimeSlots() {
		List<TimeSlot> allSlots = jdbcOperations.query(SQL_GET_ALL_TIMESLOTS, new TimeSlotRowMapper());
		Set<TimeSlot> slots = new HashSet<TimeSlot>(allSlots);
		
		for(TimeSlot slot : slots) {
			slot.setPublishers(publisherRepository.getPublishersInTimeSlot(slot.getId()));
		}
		
		return slots;
	}

	/*
	@Override
	public void modifyAssignment(Long oldTimeSlotId, Long oldPublisherId, Long timeSlotId, Long publisherId) {

		jdbcOperations.update(SQL_UPDATE_ASSIGNMENT,
				  oldTimeSlotId,
				  oldPublisherId,
				  timeSlotId,
				  publisherId
				  );
	
	}

	@Override
	public void deleteAssignment(Long timeSlotId, Long publisherId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAssignment(Long timeSlotId, Long publisherId) {
		// TODO Auto-generated method stub
		
	}
*/

}
