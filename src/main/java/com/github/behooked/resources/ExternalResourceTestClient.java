package com.github.behooked.resources;

import com.fasterxml.jackson.databind.node.ArrayNode;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.MediaType;

//This resource is used for testing purpose only. ExternalResourceTestClient functions as client when data is send to the dispatcher service in test runs.
@Produces(MediaType.APPLICATION_JSON)
@Path("/testClient")
public class ExternalResourceTestClient {

	private Long eventId;
	
	

	@Path("{dummyDispatcherService}")
	@UnitOfWork
	public void testReceiveNotificationFromAdminInformant(  final ArrayNode clientdata, @HeaderParam("Behooked-Administration-EventId") final Long eventId) 
	{

			this.eventId= eventId;
	}
}
