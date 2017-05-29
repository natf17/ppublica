package com.ancientdawn.ppublica.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ancientdawn.ppublica.config.WebConfig;
import com.ancientdawn.ppublica.domain.Publisher;
import com.ancientdawn.ppublica.service.PublisherService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class PublisherControllerTest {

	private MockMvc mockMvc;
		
	private PublisherService publisherServiceMock;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		publisherServiceMock = mock(PublisherService.class);
		Mockito.reset(publisherServiceMock);

	}
	
	@Test
	public void findOnePublisher_ShouldReturnOnePublisherObject() throws Exception {
		Publisher nathanTest = new Publisher(new Long(2), "Nathan", "Farciert", "natf", "268711454");
		
		
		//test service method
		when(publisherServiceMock.readPublisher((long)2)).thenReturn(nathanTest);
		
		//test controller method
		/*
		mockMvc.perform(get("/publisher/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath())
				.andExpect(jsonPath())
				.andExpect(jsonPath())
		
		
		*/
		
		
		
		
	}
}
