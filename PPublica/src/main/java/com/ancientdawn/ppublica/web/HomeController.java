package com.ancientdawn.ppublica.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.domain.TimeSlot;
import com.ancientdawn.ppublica.domain.WeekSchedule;
import com.ancientdawn.ppublica.security.AuthenticatedPublisher;
import com.ancientdawn.ppublica.service.PublisherService;
import com.ancientdawn.ppublica.service.TimeSlotService;
import com.ancientdawn.ppublica.service.WeekScheduleService;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Controller
public class HomeController {
	private WeekScheduleService weekScheduleService;

	@Autowired
	private TimeSlotService timeSlotService;
	
	@Autowired
	private PublisherService publisherService;

	@Autowired
	public HomeController(WeekScheduleService weekScheduleService) {
		this.weekScheduleService = weekScheduleService;
	}
	
	@RequestMapping(value="/", method = GET)
	public String home(Model model) {
		model.addAttribute("weekSchedules", weekScheduleService.getAllSchedules());
		System.out.println("--------CONTROLLER princpial: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		return "home";
	}
	
	@RequestMapping(value="/publishers", method=GET)
	public String allPublishers() {
		return "seePublishers";
	}
	
	@RequestMapping(value="/schedules", method=GET)
	public String schedules(Model model) {
		model.addAttribute("weeks", weekScheduleService.getAllSchedules());

		return "schedules";
	}
	
	@RequestMapping(value="/schedules/add", method=GET)
	public String schedulesAdd() {
		return "schedulesNew";
	}
	
	@RequestMapping(value="/schedules/{id}", method=GET)
	public String scheduleId(Model model, @PathVariable long id) {
		model.addAttribute("weekId", id);
		
		return "showWeek";
	}
	
	@RequestMapping(value="/69th/edit", method=GET)
	public String editWeek69(Model model) {
		model.addAttribute("weekId", 9);

		return "editWeek";
	}
	
	@RequestMapping(value="/schedules/{id}/edit", method=GET)
	public String scheduleIdEdit(Model model, @PathVariable long id) {
		model.addAttribute("weekId", id);
		
		return "editWeek";
	}
	
	@RequestMapping(value="/newPublisher", method=GET)
	public String newPublisher() {
		return "addPublisher";
	}
	
	@RequestMapping(value="/69th", method=GET)
	public String showWeek69() {
		return "showWeek";
	}
	
	
	@RequestMapping(value="/login", method=POST)
	public ResponseEntity<String> login(HttpServletResponse response) {
		return new ResponseEntity<String>("hehe", HttpStatus.OK);
	}
	
	@RequestMapping(value="/myslots", method=GET)
	public String mySlots(Model model, @AuthenticationPrincipal User currentUser) {
		
		AuthenticatedPublisher publisher =(AuthenticatedPublisher)currentUser;
		
		Set<WeekSchedule> weekSchedules = weekScheduleService.getWeeksForPublisher(publisher.getId());

		model.addAttribute("schedules", weekSchedules);
		Publisher user = publisherService.readPublisher(publisher.getId());
		model.addAttribute("user", user);
		// publisher assignments will be 
		
		/*
		ModelAndView mav = new ModelAndView();
		mav.setViewName("myslots");
		System.out.println(publisher);

		mav.addObject("userId", publisher.getId());
		System.out.println(publisher.getId());

		return mav;
		
		*/
		
		return "myslots";
		
	}
	
	@RequestMapping(value="/login", method=GET)
	public String loginApp() {
		// provides form that is parsed by frontend javascript
		// gateway to REST login
		
		return "login";
	}
	
	@RequestMapping(value="/loggingOut", method=GET)
	public String logoutApp() {
		// provides javascript that will clear cookie and delete from localcache
		System.out.println("reached");
		return "logout";
	}
	
	
	
}
