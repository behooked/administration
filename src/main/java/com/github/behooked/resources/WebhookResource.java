package com.github.behooked.resources;

import java.util.List;
import java.util.stream.Collectors;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.github.behooked.db.TriggerDAO;
import com.github.behooked.db.WebhookDAO;
import com.github.behooked.api.WebhookJSON;
import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;

import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/webhooks-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})

public class WebhookResource {

	private final WebhookDAO webhookDAO;

	private final TriggerDAO triggerDAO;

	public WebhookResource(final WebhookDAO webhookDAO,final TriggerDAO triggerDAO) {
		this.webhookDAO = webhookDAO;
		this.triggerDAO = triggerDAO;
	}

	@GET
	@Timed(name = "WebhookResource-get-requests-timed")
	@Metered(name = "WebhookResources-get-requests-metered")
	@UnitOfWork
	public List<WebhookJSON> listWebhooks()
	{

		return webhookDAO.findAll().stream()
				.map(w -> WebhookJSON.from(w))
				.collect(Collectors.toList());  
	}


	@GET
	@Path("{id}")
	@UnitOfWork public WebhookJSON getWebhook(@PathParam("id") final long id) {
		final Webhook webhook = findSafely(id);

		return WebhookJSON.from(webhook);  }


	private Webhook findSafely(final long webhookId) {
		return webhookDAO.findById(webhookId).orElseThrow(() -> new NotFoundException("No such webhook")); }


	@POST
	@UnitOfWork
	public WebhookJSON createWebhook(@Valid final WebhookJSON webhookJson)
	{
		final Trigger trigger = triggerDAO.findByName(webhookJson.getTrigger());
		
		if (trigger == null)
		{
			throw new ClientErrorException("No such trigger. Please choose a valid trigger. ", 409);
		}
		final Webhook webhook = Webhook.convertToWebhook(webhookJson, trigger);
		final Webhook createdWebhook = webhookDAO.create(webhook);
		return WebhookJSON.from(createdWebhook);
	}

	
	
	@PUT
	@Path("{id}")
	@UnitOfWork
	public void updateWebhook(@PathParam("id") final long id, @Valid final WebhookJSON newWebhook)
	{
		final Webhook webhookToUpdate= findSafely(id);

		if (newWebhook.getId() != id)
		{
			throw new ClientErrorException("ID found in path param does not match ID found in JSON body", 409);
		}

		final Trigger trigger = triggerDAO.findByName(newWebhook.getTrigger());
		
		webhookToUpdate.setTrigger(trigger);
		webhookToUpdate.setSecret(newWebhook.getSecret());
		webhookToUpdate.setUrl(newWebhook.getUrl());

		webhookDAO.update(webhookToUpdate);
	}

	@DELETE
	@Path("{id}")
	@UnitOfWork
	public void deleteWebhookByID(@PathParam("id") final long id){
		final Webhook webhook= findSafely(id);
		webhookDAO.delete(webhook);
	}

}
