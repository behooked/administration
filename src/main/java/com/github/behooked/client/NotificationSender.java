package com.github.behooked.client;


import com.fasterxml.jackson.databind.node.ArrayNode;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class NotificationSender {

	private final Client client;

	public NotificationSender(final Client client)
	{
		this.client = client;
	}


	public Response sendNotification(final String url, final ArrayNode listClientData, final Long eventId)
	{

		Response response = client.target(url).request(MediaType.APPLICATION_JSON).header("Behooked-Administration-EventId", eventId)
		.post(Entity.json(listClientData));
		// eventId is send via Header to identify respective payload
		return response;
	}

}
