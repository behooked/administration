package com.github.behooked.resources;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.behooked.core.Webhook;
import com.github.behooked.db.WebhookDAO;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;



@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

	private WebhookDAO webhookDao;

	public NotificationResource(WebhookDAO webhookDao) {
		this.webhookDao = webhookDao; 
	}   

	@POST
	@UnitOfWork
	public ArrayNode receiveNotification (@Valid final String eventName) {


		// Access WebhookDao to get registered webhooks
		final List<Webhook> listWebhooks = webhookDao.findByTriggerName(eventName);
		
		final ObjectMapper mapper = new ObjectMapper();
		final ArrayNode clientData = mapper.createArrayNode();
		
		if(listWebhooks.isEmpty())
		{
			LOGGER.info(String.format("No registered Webhooks for this event. EventName was: %s", eventName));
		}
		else

		{
			LOGGER.info(String.format("------------Received a notification from Dispatcher. Dispatcher requests client-data for the following event: EventName was: %s -------------------", eventName));

			selectClientData(mapper,clientData,listWebhooks);

			LOGGER.info(String.format("Requested client-data has been selected. Number of entries found: : %s", clientData.size()));

		}
		return clientData;
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
