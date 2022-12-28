package com.github.behooked.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.behooked.core.Webhook;

import jakarta.validation.constraints.NotEmpty;

public class WebhookJSON {

	private long id;
	@NotEmpty
	private String url;
	@NotEmpty
	private String trigger;
	@NotEmpty
	private String secret;

	@JsonCreator
	public WebhookJSON(@JsonProperty("id") long id, @JsonProperty("url") String url, @JsonProperty("trigger") String trigger,
			@JsonProperty("secret") String secret) {
		this.id = id;
		this.url = url;
		this.trigger = trigger;
		this.secret = secret;
	}

	@JsonProperty
	public long getId()
	{
		return id;
	}

	@JsonProperty
	public void setId(long id)
	{
		this.id = id;
	}

	@JsonProperty
	public String getUrl() {
		return url;
	}

	@JsonProperty
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty
	public String getTrigger() {
		return trigger;
	}

	@JsonProperty
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	@JsonProperty
	public String getSecret() {
		return secret;
	}

	@JsonProperty
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public static WebhookJSON from(final Webhook webhook)
    {
        return new WebhookJSON(webhook.getId(), webhook.getUrl(), webhook.getTrigger().getName(), webhook.getSecret());
    }
}
