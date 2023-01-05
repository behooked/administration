package com.github.behooked.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;


@ExtendWith(DropwizardExtensionsSupport.class)
public class NotificationSenderTest {

	@Path("/receiveNotifications")
	public static class NotificationReceivingResource
	{
		@POST
		public String receiveNotification (final ArrayNode arrayClientData, @HeaderParam("Behooked-Administration-EventId")final Long eventId) {
			
			return "Notification received.";
		}
	}
	
	private static final DropwizardClientExtension EXT = new DropwizardClientExtension(
			new NotificationReceivingResource());


	@Test
	void testSendNotification() throws IOException {

		final Long dummyEventId = 1L;
		
		// create dummyArrayNode as parameter
		final ObjectMapper mapper = new ObjectMapper();
		final ArrayNode testClientData = mapper.createArrayNode();
		ObjectNode dataSetEntry= mapper.createObjectNode();

		dataSetEntry.put("url", "https://example.io");
		dataSetEntry.put("secret", "seeecret");
		testClientData.add(dataSetEntry);

		final Client client = new JerseyClientBuilder(EXT.getEnvironment()).build("test");

		final NotificationSender notificationSender = new NotificationSender(client);
		
		Response response = notificationSender.sendNotification(
				EXT.baseUri() + "/receiveNotifications", testClientData,dummyEventId);
		
		 assertEquals("Notification received.", response.readEntity(String.class));


	}
}
