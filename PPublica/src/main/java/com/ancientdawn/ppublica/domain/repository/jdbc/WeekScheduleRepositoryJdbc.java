package com.ancientdawn.ppublica.domain.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
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

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.domain.repository.DayRepository;
import com.ancientdawn.ppublica.domain.repository.PublisherRepository;
import com.ancientdawn.ppublica.domain.repository.WeekScheduleRepository;
import com.ancientdawn.ppublica.util.ModifiedAssignment;

import static com.ancientdawn.ppublica.util.ModifiedAssignment.Request;

@Repository
public class WeekScheduleRepositoryJdbc implements WeekScheduleRepository {
	
	@Autowired
	private DayRepository dayRepository;
	@Autowired PublisherRepository publisherRepository;
	@Autowired
	private JdbcOperations jdbcOperations;
	private static final String SQL_READ_WEEKSCHEDULE_ID = "SELECT * FROM weekSchedule WHERE id=?";
	private static final String SQL_INSERT_WEEK = "INSERT INTO weekSchedule (location, info, cartId) VALUES (?, ?, ?)";
	private static final String SQL_EXISTS_WEEK_ID = "SELECT id FROM weekSchedule WHERE id=?";
	private static final String SQL_UPDATE_WEEK_ID = "UPDATE weekSchedule SET location=?, info=?, cartId=? WHERE id=?";
	private static final String SQL_DELETE_ID = "DELETE FROM weekSchedule WHERE id=?";
	private static final String SQL_GET_ALL_WEEKS = "SELECT * FROM weekSchedule";


	
	@Autowired
	public WeekScheduleRepositoryJdbc(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	public WeekSchedule createWeekSchedule(WeekSchedule weekSchedule) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Long weekScheduleId = null;
		
		// insert into weekSchedule table
		jdbcOperations.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(SQL_INSERT_WEEK, new String[]{"id"});
						ps.setString(1, weekSchedule.getLocation());
						ps.setString(2, weekSchedule.getInfo());
						if(weekSchedule.getCartId() == null) {
							ps.setNull(3, Types.BIGINT);
						} else {
							ps.setLong(3, weekSchedule.getCartId());
						}
						
						return ps;
					}
				}, keyHolder
				
		);
		
		weekScheduleId = (Long)keyHolder.getKey().longValue();
		
		weekSchedule.setId(weekScheduleId);
		
		// save days
		for(Day day : weekSchedule.getWeek()) {
			day.setWeekScheduleId(weekScheduleId);
			dayRepository.createDay(day);
		}
		
		return weekSchedule;
	}

	@Override
	public WeekSchedule updateWeekSchedule(WeekSchedule weekSchedule) {
		System.out.println("UPDATING DATABASE");
		
		// first update days with same weekDay
		Set<Day> daysRepo = dayRepository.getDaysInWeek(weekSchedule.getId());
		Set<Day> daysNew = weekSchedule.getWeek();

		Set<DayOfWeek> daysInWeekRepo = new HashSet<DayOfWeek>();
		Set<DayOfWeek> daysInWeekNew = new HashSet<DayOfWeek>();

		Set<DayOfWeek> daysToBeUpdated = new HashSet<DayOfWeek>();
				
		for(Day d : daysRepo) {
			daysInWeekRepo.add(d.getWeekday());
		}
				
		for(Day d : daysNew) {
			daysInWeekNew.add(d.getWeekday());
		}
		
		for(DayOfWeek newD : daysInWeekNew) {
			if(daysInWeekRepo.contains(newD)) {
				// update day in repository
				// add to removal list.. they have already been handled
				daysToBeUpdated.add(newD);
			}
		}
		daysInWeekRepo.removeAll(daysToBeUpdated);
		daysInWeekNew.removeAll(daysToBeUpdated);
				
		Day newDay = null;
		Day oldDay = null;
		Long dayId = null;
				
		for(DayOfWeek dN : daysToBeUpdated) {
			// for each day of week to be updated find corresponding day's id
			for(Day d : daysRepo) {
				if(d.getWeekday().equals(dN)) {
					oldDay = d;
					dayId = d.getId(); 
					break;
				}
			}
			daysRepo.remove(oldDay);
					
			// extract new day
			for(Day d : daysNew) {
				if(d.getWeekday().equals(dN)) {
					newDay = d;
					break;
				}
			}
			daysNew.remove(newDay);
					
			// set id, and update it
			newDay.setId(dayId);
			dayRepository.updateDay(newDay);
					
		}
				
				
		Iterator<Day> itN = daysNew.iterator();
		Iterator<Day> itR = daysRepo.iterator();
				
		if(daysInWeekNew.size() <= daysInWeekRepo.size()) {
			// perform 1:1 update
			// get the first from both
			while(itN.hasNext()) {
				newDay = itN.next();
				oldDay = itR.next();
				newDay.setId(oldDay.getId());
				dayRepository.updateDay(newDay);
				System.out.println("updating day");
						
			}
			// out of while loop - no more new days
			// the rest of days in repository will be deleted
			while(itR.hasNext()) {
				oldDay = itR.next();
				dayRepository.deleteDay(oldDay.getId());
			}
					
					
					
		} else {
			// more days in new set than in repository
			while(itR.hasNext()) {
				newDay = itN.next();
				oldDay = itR.next();
				newDay.setId(oldDay.getId());
				dayRepository.updateDay(newDay);
						
			}
			// out of while loop - no more days in repository
			// the rest of new days will be created
			while(itN.hasNext()) {
				newDay = itN.next();
				dayRepository.createDay(newDay);
			}
		}
		
		// update other fields in weekSchedule
		jdbcOperations.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_WEEK_ID);
					ps.setString(1, weekSchedule.getLocation());
					ps.setString(2, weekSchedule.getInfo());
					if(weekSchedule.getCartId() == null) {
						ps.setNull(3, Types.BIGINT);
					} else {
						ps.setLong(3, weekSchedule.getCartId());
					}
				
					ps.setLong(4, weekSchedule.getId());
				
					return ps;
				}
			}
		 );		
		
		return weekSchedule;
	}

	@Override
	public WeekSchedule readWeekSchedule(Long weekScheduleId) {
		// build weekSchedule object from weekSchedule table
		WeekSchedule weekSchedule = jdbcOperations.queryForObject(SQL_READ_WEEKSCHEDULE_ID, new WeekScheduleRowMapper(), weekScheduleId);

		// get all days in this week
		weekSchedule.setWeek(dayRepository.getDaysInWeek(weekScheduleId));
		
		return weekSchedule;
	}


	@Override
	public void deleteWeekSchedule(Long weekScheduleId) {
		// delete a week
		// corresponding days, timeSlots and corresponding assignments are deleted via SQL ON DELETE CASCADE
		jdbcOperations.update(SQL_DELETE_ID, weekScheduleId);
	}

	@Override
	public Set<WeekSchedule> getAllSchedules() {
		List<WeekSchedule> allSchedules = jdbcOperations.query(SQL_GET_ALL_WEEKS, new WeekScheduleRowMapper());
		Set<WeekSchedule> schedules = new HashSet<WeekSchedule>(allSchedules);
		
		for(WeekSchedule weekSchedule : schedules) {
			weekSchedule.setWeek(dayRepository.getDaysInWeek(weekSchedule.getId()));
		}
		
		return schedules;
	}
	
	private static final class WeekScheduleRowMapper implements RowMapper<WeekSchedule> {

		@Override
		public WeekSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new WeekSchedule(rs.getLong("id"),
					rs.getString("location"),
					rs.getString("info"),
					rs.getShort("cartId")
					);
		}
		
	}

	@Override
	public boolean existsWeek(long id) {
		try {
			jdbcOperations.queryForObject(SQL_EXISTS_WEEK_ID, Long.class, id);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}

	}

	@Override
	public List<ModifiedAssignment> updateAssignments(List<ModifiedAssignment> assignments) {
		
		ModifiedAssignment.Request action = null;
		Long timeSlotId = null;
		Long publisherId = null;
		
		for(ModifiedAssignment ma : assignments) {
			action = ma.getRequestType();
			
			timeSlotId = ma.getId();
			publisherId = ma.getPublisherId();
			
			if(action == Request.ADD) {
				// are we creating a new TimeSlot ?
				if(timeSlotId == null) {
					publisherRepository.createAssignment(ma.getDayId(), ma.getStartTime(), publisherId);
				}
				// adding to a slot - UPDATE
				else {
					publisherRepository.createAssignment(timeSlotId, publisherId);
				}
				
				continue;
				
			}
			
			if(action == Request.DELETE) {
				publisherRepository.deleteAssignment(timeSlotId, publisherId);
			}
		}
		
		
		return assignments;
	}


}
