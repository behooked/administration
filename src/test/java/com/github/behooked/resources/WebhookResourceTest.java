package com.github.behooked.resources;

import org.junit.jupiter.api.extension.ExtendWith;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;


import com.github.behooked.db.WebhookDAO;
import com.github.behooked.db.TriggerDAO;
import com.github.behooked.core.Webhook;
import com.github.behooked.AdministrationApplication;
import com.github.behooked.AdministrationConfiguration;
import com.github.behooked.api.WebhookJSON;
import com.github.behooked.core.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class WebhookResourceTest {

	private static final WebhookDAO DAO = mock(WebhookDAO.class);
	private static final TriggerDAO TRIGGER_DAO = mock(TriggerDAO.class);
	private static final ResourceExtension EXT = ResourceExtension.builder() //
			.addResource(new WebhookResource(DAO, TRIGGER_DAO)) //
			.build();

	private Webhook webhook;
	private Trigger trigger;
	private WebhookJSON webhookJSON;

	private static DropwizardAppExtension<AdministrationConfiguration> APP= new DropwizardAppExtension<>(
			AdministrationApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

	@BeforeEach
	void setUp()
	{
		webhook = new Webhook();
		trigger = new Trigger("trigger");
		webhook.setId(1L);
		webhook.setUrl("https://example.org");
		webhook.setTrigger(trigger);
		webhook.setSecret("superSecret");
        
		webhookJSON = new WebhookJSON(1,"https://example.org", "trigger","superSecret");
		
	}

	@AfterEach
	void tearDown()
	{
		reset(DAO);
	}

	@Test
	void idMismatchReturnsConflict()
	{
		when(DAO.findById(1L)).thenReturn(Optional.of(webhook));
		Trigger trigger= new Trigger("name");
		final Response response = EXT.target("/webhooks-service/1")//
				.request() //
				.put(Entity.json(new WebhookJSON(0, "url", trigger.getName(), "secret")));

		assertThat(response.getStatusInfo(), is(Response.Status.CONFLICT));

	}

	@Test void listWebhooks() { 
      
		List<Webhook> list = new ArrayList<Webhook>();

		list.add(webhook);
		when(DAO.findAll()).thenReturn(list);

		final Response response = EXT.target("/webhooks-service").request() .get();
	
		verify(DAO,Mockito.times(1)).findAll(); 
		assertThat(response.getStatusInfo(),is(Response.Status.OK));
	}
	
	@Test
	void getWebhookByIdSuccess() {
		when(DAO.findById(1L)).thenReturn(Optional.of(webhook));

		WebhookJSON found = EXT.target("/webhooks-service/1").request().get(WebhookJSON.class);
		assertThat(found.getId(), is(webhook.getId()));
		verify(DAO).findById(1L);  
	}

	@Test
	void getWebhookByIdNotFound() {
		when(DAO.findById(2L)).thenReturn(Optional.empty()); // returns an empty Optional instance

		final Response response = EXT.target("/webhooks-service/2").request().get();
		assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode())); 
		verify(DAO).findById(2L); 
	}



	@Test 
	void getByIdReturnsOK() {
		when(DAO.findById(1L)).thenReturn(Optional.of(webhook));
		final Response response = EXT.target("/webhooks-service/1").request().get();
		assertThat(response.getStatusInfo(), is(Response.Status.OK)); }


	@Test
	void createWebhook() {

		when(DAO.create(any(Webhook.class))).thenReturn(webhook);  // any() performs typecheck
		when(TRIGGER_DAO.findByName(any())).thenReturn(trigger);

		final Response response = EXT.target("/webhooks-service")  
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(webhookJSON));

		WebhookJSON postedWebhook = response.readEntity(WebhookJSON.class);
		assertThat(response.getStatusInfo(), is(Response.Status.OK)); 
		assertThat(postedWebhook.getUrl(), is("https://example.org")); 
		assertThat(postedWebhook.getTrigger(), is("trigger")); 
		assertThat(postedWebhook.getSecret(), is("superSecret")); 

	}


	@Test
	void updateWebhook() {

		Trigger newTrigger = new Trigger("newTrigger");
		when(TRIGGER_DAO.findByName(any())).thenReturn(newTrigger);
		when(DAO.findById(1L)).thenReturn(Optional.of(webhook)); 

		final Response response = EXT.target("/webhooks-service/1") 
				.request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.json(webhookJSON));

		assertThat(response.getStatusInfo(),is(Response.Status.NO_CONTENT)); 
	}

	
}
