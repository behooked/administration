package com.github.behooked.resources;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.behooked.client.NotificationSender;
import com.github.behooked.core.Webhook;
import com.github.behooked.db.WebhookDAO;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;

import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;



@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

	private WebhookDAO webhookDao;
	private NotificationSender notificationSender;
	private String dispatcherUrl;
	
	//private static final String  DISPATCHER_URL = "http://localhost:8082/events/dispatch";

	public NotificationResource(WebhookDAO webhookDao, NotificationSender notificationSender, String dispatcherUrl) {
		this.webhookDao = webhookDao; 
		this.notificationSender = notificationSender;
		this.dispatcherUrl = dispatcherUrl;
	}   



	// sends client-data to dispatcher when notification from kafka-connector is received
	@POST
	@UnitOfWork
	public void receiveNotification (@Valid final String eventName, @HeaderParam("Behooked-Dispatcher-Notification-EventId") final Long eventId) {


		// Access WebhookDao to get registered webhooks
		final List<Webhook> listWebhooks = webhookDao.findByTriggerName(eventName);


		final ObjectMapper mapper = new ObjectMapper();
		final ArrayNode arrayClientData = mapper.createArrayNode();



		if(listWebhooks.isEmpty())
		{
			LOGGER.info(String.format("No registered Webhooks for this event. EventId was: %s", eventId));
		}
		else

		{

			LOGGER.info(String.format("------------Received a notification from Dispatcher. Dispatcher requests client-data for the following event: EventId was: %s -------------------", eventId));

			selectClientData(mapper,arrayClientData,listWebhooks);

			int length = arrayClientData.size();
			LOGGER.info(String.format("Requested client-data has been selected. Number of entries found: : %s", length));


		}

		// send clientData + eventId to dispatcher 
		notificationSender.sendNotification(dispatcherUrl,arrayClientData, eventId);
		LOGGER.info(String.format("Client-Data has been send to Dispatcher. EventId was: %s", eventId));

		arrayClientData.removeAll();

	}



	public static void selectClientData (ObjectMapper mapper, ArrayNode arrayClientData, List<Webhook> listWebhooks) {


		for (Webhook w : listWebhooks) {

			ObjectNode clientDataJSON= mapper.createObjectNode();

			clientDataJSON.put("url", w.getUrl());

			clientDataJSON.put("secret", w.getSecret());

			arrayClientData.add(clientDataJSON);

		}
	}
}
