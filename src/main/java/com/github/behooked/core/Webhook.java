package com.github.behooked.core;

import java.util.Objects;

import org.hibernate.validator.constraints.URL;

import com.github.behooked.api.WebhookJSON;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "webhooks")

@NamedQuery(
		name = "com.github.behooked.core.Webhook.findAll",
		query = "SELECT w FROM Webhook w")

@NamedQuery( name =
"com.github.behooked.core.Webhook.findByTriggerName", query =
		"SELECT w FROM Webhook w, Trigger t WHERE w.trigger = t AND t.name = :name")


public class Webhook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // generate the primary key value
	private long id;

	@URL
	@Column(name = "url", nullable = false)
	private String url;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Trigger trigger;

	@Column(name = "secret", nullable = false)
	private String secret;


	public Webhook()
	{
	}

	public Webhook(final String url, final Trigger trigger, final String secret) {
		this.url = url;
		this.trigger=trigger;
		this.secret = secret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(final String secret) {
		this.secret = secret;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(final Trigger trigger) {
		this.trigger = trigger;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Webhook)) {
			return false;
		}

		final Webhook webhook= (Webhook) o;

		return id == webhook.id &&
				url == webhook.url &&
				trigger == webhook.trigger &&
				Objects.equals(url, webhook.url) &&
				Objects.equals(trigger, webhook.trigger) &&
				Objects.equals(secret, webhook.secret);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, url, trigger, secret);
	}

	public static Webhook convertToWebhook(final WebhookJSON webhookJSON, final Trigger trigger)
	{
		return new Webhook( webhookJSON.getUrl(), trigger, webhookJSON.getSecret());
	}

}
