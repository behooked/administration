package com.github.behooked.resources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.behooked.client.NotificationSender;
import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;
import com.github.behooked.db.WebhookDAO;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

@ExtendWith(DropwizardExtensionsSupport.class)
public class NotificationResourceTest {

	
	private static final WebhookDAO WEBHOOK_DAO = mock(WebhookDAO.class);
	private static final NotificationSender NOTIFICATION_SENDER= mock(NotificationSender.class);

	
	private static final ResourceExtension EXT = ResourceExtension.builder() //
			.addResource(new NotificationResource(WEBHOOK_DAO, NOTIFICATION_SENDER)) //
			.build();
	
	private Webhook webhook;
	private Trigger trigger;
	private final List<Webhook> listWebhooks = new ArrayList<Webhook>();
	
	@BeforeEach
	void setUp()
	{
	webhook = new Webhook();
	trigger = new Trigger("trigger");
	webhook.setId(1L);
	webhook.setUrl("https://example.org");
	webhook.setTrigger(trigger);
	webhook.setSecret("superSecret");
	
	listWebhooks.add(webhook);
	
	}
	@AfterEach
	void tearDown()
	{
		reset(WEBHOOK_DAO);
	}
	
	
	@Test
	void testSelectClientData() {

		final ObjectMapper mapper = new ObjectMapper();

		// testData
		final ArrayNode testClientData = mapper.createArrayNode();
		ObjectNode dataSetEntry= mapper.createObjectNode();

		dataSetEntry.put("url", "https://example.io");
		dataSetEntry.put("secret", "seeecret");
		testClientData.add(dataSetEntry);


		// create parameter
		final ArrayNode clientData= mapper.createArrayNode();
		Trigger trigger = new Trigger("name");
		Webhook w = new Webhook("https://example.io", trigger, "seeecret");
		final List<Webhook> list = new ArrayList<Webhook>();
		list.add(w);

		NotificationResource.selectClientData(mapper,clientData,list);

		assertThat(clientData).isEqualTo(testClientData);
	}
	
	@Test
	void testReceiveNotificationCallsNotificationSender() {
		
		String eventName = "trigger";
	
		when(WEBHOOK_DAO.findByTriggerName("trigger")).thenReturn(listWebhooks);
		
		EXT.target("/notifications").request(MediaType.APPLICATION_JSON).header("Behooked-Dispatcher-Notification-EventId",2).post(Entity.json(eventName));
		
		verify(NOTIFICATION_SENDER).sendNotification(any(ArrayNode.class), anyLong());
	
	} 


}