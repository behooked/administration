package com.github.behooked.db;


import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.github.behooked.core.Webhook;

import io.dropwizard.hibernate.AbstractDAO;

public class WebhookDAO extends AbstractDAO<Webhook> {

	public WebhookDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	
	}

	public Optional<Webhook> findById(Long id) {
		return Optional.ofNullable(get(id));
	}

	public Webhook create(Webhook webhook) {
		return persist(webhook);
	}

	@SuppressWarnings("unchecked") 
	public List<Webhook> findAll()  
	{
		final Query<Webhook> namedQuery = (Query<Webhook>) namedQuery("com.github.behooked.core.Webhook.findAll");

		return list(namedQuery);                                          //convert to list
	}

	@SuppressWarnings("unchecked")
	public List<Webhook> findByTriggerName(String triggerName) { 
		final Query<Webhook> namedQuery = (Query<Webhook>)namedQuery("com.github.behooked.core.Webhook.findByTriggerName");

		return list(namedQuery.setParameter("name", triggerName));
		}
	

	public void update(Webhook webhook)
	{
		currentSession().persist(webhook);

	}

	public void delete(Webhook webhook){
		currentSession().delete(webhook);
	}
}
