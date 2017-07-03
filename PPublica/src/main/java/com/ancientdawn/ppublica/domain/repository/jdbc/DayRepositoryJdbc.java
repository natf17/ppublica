package com.ancientdawn.ppublica.domain.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ancientdawn.ppublica.domain.Day;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.repository.DayRepository;
import com.ancientdawn.ppublica.domain.repository.TimeSlotRepository;

@Repository
public class DayRepositoryJdbc implements DayRepository {

	@Autowired
	private TimeSlotRepository timeSlotRepository;
	
	private JdbcOperations jdbcOperations;
	private static final String SQL_READ_ALL_DAYS_WEEKSCHEDULE_ID = "SELECT * FROM day WHERE weekScheduleId=?";
	private static final String SQL_READ_DAY_ID = "SELECT * FROM day WHERE id=?";
	private static final String SQL_INSERT_DAY = "INSERT INTO day (weekScheduleId, weekDay, minTime, maxTime, duration, defaultMaxPublishers) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_ID = "DELETE FROM day WHERE id=?";
	private static final String SQL_UPDATE_DAY_ID = "UPDATE day SET weekDay=?, minTime=?, maxTime=?, duration=?, defaultMaxPublishers=? WHERE id=?";
	private static final String SQL_EXISTS_DAY_ID = "SELECT id FROM day WHERE id=?";
	private static final String SQL_GET_ALL_DAYS = "SELECT * FROM day";

	@Autowired
	public DayRepositoryJdbc(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	
	@Override
	public Day createDay(Day day) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Long dayId = null;
		
		// insert into weekSchedule table
		jdbcOperations.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(SQL_INSERT_DAY, new String[]{"id"});
						ps.setLong(1, day.getWeekScheduleId());
						ps.setString(2, day.getWeekday().toString());
						ps.setInt(3, day.getMinTime().toSecondOfDay());
						ps.setInt(4, day.getMaxTime().toSecondOfDay());
						ps.setInt(5, (int)day.getDuration().toMinutes());
						ps.setInt(6, day.getDefaultMaxPublishers());
						return ps;
					}
				}, keyHolder
				
		);
		
		dayId = (Long)keyHolder.getKey().longValue();
		
		day.setId(dayId);
				
		// save timeSlots
		for(TimeSlot timeSlot : day.getTimeSlots()) {
			timeSlot.setDayId(dayId);
			timeSlotRepository.createTimeSlot(timeSlot);
		}
		
		return day;
	}

	@Override
	public Day updateDay(Day day) {
		// first update timeSlots with same startTime
		Set<TimeSlot> timeSlotRepo = timeSlotRepository.getTimeSlotsInDay(day.getId());
		Set<TimeSlot> timeSlotNew = day.getTimeSlots();
				
		// if both new Day and repository Day have no slots... skip
		if((timeSlotRepo == null || timeSlotRepo.size() == 0) && (timeSlotNew == null || timeSlotNew.size() == 0)) {
			// go to return statement
		} 
		// if repository Day has no slots... add all from new Day
		else if(timeSlotRepo == null || timeSlotRepo.size() == 0) {
			for(TimeSlot t : timeSlotNew) {
				timeSlotRepository.createTimeSlot(t);
			}
		}
		// if new Day has no slots... delete all from repository
		else if(timeSlotNew == null || timeSlotNew.size() == 0) {
			for(TimeSlot t : timeSlotRepo) {
				timeSlotRepository.deleteTimeSlot(t.getId());
			}
		} 
		// iterate through the slots
		else {
			List<LocalTime> startTimesRepo = new ArrayList<LocalTime>();
			List<LocalTime> startTimesNew = new ArrayList<LocalTime>();

			List<LocalTime> slotsToBeUpdated = new ArrayList<LocalTime>();
							
			for(TimeSlot t : timeSlotRepo) {
				startTimesRepo.add(t.getStartTime());
			}
			
			System.out.println("START TIMESREPO " + startTimesRepo.size());
							
			for(TimeSlot t : timeSlotNew) {
				startTimesNew.add(t.getStartTime());
			}
			System.out.println("START TIMESNEW " + startTimesNew.size());

							
			for(LocalTime newT : startTimesNew) {
				if(startTimesRepo.contains(newT)) {
					// update timeSlot in repository
					// add to removal list.. they have already been handled
					slotsToBeUpdated.add(newT);
				}
			}
			startTimesRepo.removeAll(slotsToBeUpdated);
			startTimesNew.removeAll(slotsToBeUpdated);
							
			TimeSlot newSlot = null;
			TimeSlot oldSlot = null;
			Long timeSlotId = null;
							
			for(LocalTime tN : slotsToBeUpdated) {
				// for each slot of day to be updated find corresponding slot's id
				for(TimeSlot t : timeSlotRepo) {
					if(t.getStartTime().equals(tN)) {
						oldSlot = t;
						timeSlotId = t.getId(); 
						break;
					}
				}
								
				timeSlotRepo.remove(oldSlot);
								
				// extract new slot
				for(TimeSlot t : timeSlotNew) {
					if(t.getStartTime().equals(tN)) {
						newSlot = t;
						break;
					}
				}
				timeSlotNew.remove(newSlot);
								
				// set id, and update it
				newSlot.setId(timeSlotId);
				System.out.println("UPDATING timeSlot");
				timeSlotRepository.updateTimeSlot(newSlot);
								
			}
							
							
			Iterator<TimeSlot> itN = timeSlotNew.iterator();
			Iterator<TimeSlot> itR = timeSlotRepo.iterator();
							
			if(startTimesNew.size() <= startTimesRepo.size()) {
				System.out.println("HEY " + startTimesNew.size() + " " + startTimesRepo.size());
				// perform 1:1 update
				// get the first from both
				while(itN.hasNext()) {
					newSlot = itN.next();
					oldSlot = itR.next();
					newSlot.setId(oldSlot.getId());
					System.out.println("UPDATING timeSlot - optimizing created ones 1");
					timeSlotRepository.updateTimeSlot(newSlot);
								
				}
				// out of while loop - no more new days
				// the rest of days in repository will be deleted
				while(itR.hasNext()) {
					oldSlot = itR.next();
					timeSlotRepository.deleteTimeSlot(oldSlot.getId());
				}
								
								
								
			} else {
				System.out.println("HEYNOOO");

				// more timeSlots in new set than in repository
				while(itR.hasNext()) {
					newSlot = itN.next();
					oldSlot = itR.next();
					newSlot.setId(oldSlot.getId());
					System.out.println("UPDATING timeSlot - optimizing created ones 2");
					timeSlotRepository.updateTimeSlot(newSlot);
									
				}
				// out of while loop - no more days in repository
				// the rest of new days will be created
				while(itN.hasNext()) {
					newSlot = itN.next();
					System.out.println("CREATING NEW timeSlot");
					timeSlotRepository.createTimeSlot(newSlot);
				}
						
			}
					
		}		
		
		// dayId will never be updated...
		// use dayOfWeek and weekScheduleId to determine which one to update
		jdbcOperations.update(SQL_UPDATE_DAY_ID,
							  day.getWeekday().toString(),
							  day.getMinTime().toSecondOfDay(),
							  day.getMaxTime().toSecondOfDay(),
							  (int)day.getDuration().toMinutes(),
							  day.getDefaultMaxPublishers(),
							  day.getId()
							  );
		
		return day;
	}

	@Override
	public Day readDay(Long dayId) {
		// build day object from day table
		Day day = jdbcOperations.queryForObject(SQL_READ_DAY_ID, new DayRowMapper(), dayId);

		// get all timeSlots in week
		day.setTimeSlots(timeSlotRepository.getTimeSlotsInDay(dayId));
		
		return day;
	}


	@Override
	public void deleteDay(Long dayId) {
		// delete a day
		// corresponding timeSlots and corresponding assignments are deleted via SQL ON DELETE CASCADE
		jdbcOperations.update(SQL_DELETE_ID, dayId);
			

	}

	@Override
	public Set<Day> getDaysInWeek(Long weekScheduleId) {
		// build list of Days from day table
		List<Day> queryResults = jdbcOperations.query(SQL_READ_ALL_DAYS_WEEKSCHEDULE_ID, new DayRowMapper(), weekScheduleId);
		
		// populate with corresponding timeSlots
		SortedSet<TimeSlot> timeSlots = null;
		for(Day day : queryResults) {
			timeSlots = timeSlotRepository.getTimeSlotsInDay(day.getId());
			day.setTimeSlots(timeSlots);
		}
		
		return new TreeSet<Day>(queryResults);
	}
	
	private static final class DayRowMapper implements RowMapper<Day> {

		@Override
		public Day mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new Day(rs.getLong("id"),
					rs.getLong("weekScheduleId"),
					DayOfWeek.valueOf(rs.getString("weekDay")),
					LocalTime.ofSecondOfDay(rs.getInt("minTime")),
					LocalTime.ofSecondOfDay(rs.getInt("maxTime")),
					Duration.ofMinutes(rs.getInt("duration")),
					rs.getShort("defaultMaxPublishers")
					);
		}
		
	}

	@Override
	public boolean existsDay(long dayId) {
		try {
			jdbcOperations.queryForObject(SQL_EXISTS_DAY_ID, Long.class, dayId);
			return true;
		} catch(EmptyResultDataAccessException e) {
			return false;
		}	}


	@Override
	public Set<Day> getAllDays() {

		List<Day> allDays = jdbcOperations.query(SQL_GET_ALL_DAYS, new DayRowMapper());
		Set<Day> schedules = new HashSet<Day>(allDays);
		
		for(Day day : allDays) {
			day.setTimeSlots(timeSlotRepository.getTimeSlotsInDay(day.getId()));
		}
		
		return schedules;
	}


	@Override
	public Day readDayProps(Long dayId) {
		// build day object from day table
		Day day = jdbcOperations.queryForObject(SQL_READ_DAY_ID, new DayRowMapper(), dayId);
		
		return day;
	}



}
