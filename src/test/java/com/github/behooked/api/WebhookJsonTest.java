package com.github.behooked.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;

import io.dropwizard.jackson.Jackson;

public class WebhookJsonTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	private Webhook webhook;


	@Test
	public void testWebhookJSON() {

		final WebhookJSON webhookJSON = new WebhookJSON(1, "www.test.com", "trigger", "superSecret");
		assertThat(webhookJSON.getId()).isEqualTo(1);
		assertThat(webhookJSON.getUrl()).isEqualTo("www.test.com");
		assertThat(webhookJSON.getTrigger()).isEqualTo("trigger");
		assertThat(webhookJSON.getSecret()).isEqualTo("superSecret");
	}


	@Test
	public void convertSuccessfully() {
		Trigger trigger= new Trigger("name");

		webhook = new Webhook("www.example.com", trigger, "mySecret");
		assertTrue(WebhookJSON.from(webhook) instanceof WebhookJSON);

	}



	@Test
	void serializesToJSON() throws Exception {
		final WebhookJSON webhookJSON = new WebhookJSON(1, "www.test.com", "trigger", "mySecret");

		final String expected = MAPPER.writeValueAsString(
				MAPPER.readValue(getClass().getResource("/webhook.json"), WebhookJSON.class));

		assertThat(MAPPER.writeValueAsString(webhookJSON)).isEqualTo(expected);
	}


	@Test
	public void deserializesFromJSON() throws Exception {
		final WebhookJSON webhookJSON = new WebhookJSON(1, "www.test.com", "trigger", "mySecret");

		WebhookJSON deserialized = MAPPER.readValue(getClass().getResource("/webhook.json"), WebhookJSON.class);
		assertThat(deserialized.getId()).isEqualTo(webhookJSON.getId());
		assertThat(deserialized.getUrl()).isEqualTo(webhookJSON.getUrl());
		assertThat(deserialized.getTrigger()).isEqualTo(webhookJSON.getTrigger());
		assertThat(deserialized.getSecret()).isEqualTo(webhookJSON.getSecret());

	} 

}
