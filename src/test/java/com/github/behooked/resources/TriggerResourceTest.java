package com.github.behooked.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import com.github.behooked.api.TriggerJSON;
import com.github.behooked.core.Trigger;
import com.github.behooked.db.TriggerDAO;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TriggerResourceTest {

	private static final TriggerDAO TRIGGER_DAO = mock(TriggerDAO.class);

	private static final ResourceExtension EXT = ResourceExtension.builder() //
			.addResource(new TriggerResource(TRIGGER_DAO)) //
			.build();


	private Trigger trigger;
	
	
	@BeforeEach
	void setUp()
	{
		trigger = new Trigger("dummyTrigger");
		trigger.setId(1L);
	}

	@AfterEach
	void tearDown()
	{
		reset(TRIGGER_DAO);
	}
	
		@Test
		void getTriggerByNameSuccessful() {
			when(TRIGGER_DAO.findByName("dummyTrigger")).thenReturn((trigger));

			TriggerJSON response = EXT.target("/triggers/dummyTrigger").request().get(TriggerJSON.class);
			assertThat(response.getName(), is(trigger.getName()));
			
		}
		
		@Test 
		void GETcorrectStatusCode() {
			when(TRIGGER_DAO.findByName("dummyTrigger")).thenReturn((trigger));
			
			final Response response = EXT.target("/triggers/dummyTrigger").request().get();
			assertThat(response.getStatusInfo(), is(Response.Status.OK)); 
			}

		
		@Test
		void getAllTriggers() {
			
			Trigger trigger1 = new Trigger("dummyTrigger1");

			List<Trigger> list = new ArrayList<Trigger>(); 
			list.add(trigger); list.add(trigger1);
			when(TRIGGER_DAO.findAll()).thenReturn(list);
			final Response response = EXT.target("/triggers").request() .get();

			verify(TRIGGER_DAO,Mockito.times(1)).findAll(); 
			assertThat(response.getStatusInfo(),is(Response.Status.OK));
	}
		
		@Test
		void createTrigger() {

			when(TRIGGER_DAO.create(any(Trigger.class))).thenReturn(trigger);

			final Response response = EXT.target("/triggers")  
					.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.json(new TriggerJSON(1L, "dummyTrigger")));

			TriggerJSON postedTrigger = response.readEntity(TriggerJSON.class);
		

			assertThat(response.getStatusInfo(), is(Response.Status.OK)); 
			assertThat(postedTrigger.getName(), is("dummyTrigger")); 
			assertThat(postedTrigger.getId(), is(1L)); 
			
		}
		
		@Test
		void deleteTrigger()
		{
			when(TRIGGER_DAO.findById(1L)).thenReturn(Optional.of(trigger)); 

			final Response response = EXT.target("/triggers/1").request().delete();
			assertThat(response.getStatusInfo(), is(Response.Status.NO_CONTENT)); 

		}
	
}
