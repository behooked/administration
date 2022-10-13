package com.github.behooked.client;


import com.fasterxml.jackson.databind.node.ArrayNode;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class NotificationSender {

	private final Client client;
	private static final String  DISPATCHER_URL = "http://localhost:8084/api/dispatcher";

	public NotificationSender(final Client client)
	{
		this.client = client;
	}


	public void sendNotification(final ArrayNode listClientData, final Long eventId)
	{

		client.target(DISPATCHER_URL).request(MediaType.APPLICATION_JSON).header("Behooked-Administration-EventId", eventId)
		.post(Entity.json(listClientData));
		// eventId is send via Header to identify respective payload
	}

}
