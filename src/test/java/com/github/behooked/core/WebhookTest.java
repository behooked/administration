package com.github.behooked.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.github.behooked.api.WebhookJSON;

public class WebhookTest {

	private Trigger trigger= new Trigger("name");

	@Test
	public void testWebhook() {

		final Webhook webhook = new Webhook("www.test.com", trigger, "superSecret");
		assertThat(webhook.getUrl()).isEqualTo("www.test.com");
		assertThat(webhook.getTrigger()).isEqualTo(trigger);
		assertThat(webhook.getSecret()).isEqualTo("superSecret");
	}


	private WebhookJSON webhookJson = new WebhookJSON(2,"https://example.com",trigger.getName(),"mySecret");

	@Test
	public void convertSuccessfully() {

		assertTrue(Webhook.convertToWebhook(webhookJson, trigger) instanceof Webhook);

	}

}
