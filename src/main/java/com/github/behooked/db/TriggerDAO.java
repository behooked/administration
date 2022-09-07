package com.github.behooked.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.github.behooked.core.Trigger;

import io.dropwizard.hibernate.AbstractDAO;

public class TriggerDAO extends AbstractDAO<Trigger> {

	public TriggerDAO(final SessionFactory sessionFactory) {
		super(sessionFactory);

	}

	public Trigger create(final Trigger trigger) {
		return persist(trigger);
	}

	@SuppressWarnings("unchecked")
	public List<Trigger> findAll()
	{
		final Query<Trigger> namedQuery = (Query<Trigger>) namedQuery("com.github.behooked.core.Trigger.findAll");

		return list(namedQuery);  
	}

	@SuppressWarnings("unchecked")
	public Trigger findByName(final String trigger)
	{
		final Query<Trigger> namedQuery = (Query<Trigger>) namedQuery("com.github.behooked.core.Trigger.findByName"); // query() Returns the entity class managed by this DAO.

		return namedQuery.setParameter("triggerName", trigger).uniqueResult();

	}

	public Optional<Trigger> findById(Long id) {
		return Optional.ofNullable(get(id));
	}


	public void delete(Trigger trigger){
		currentSession().delete(trigger);
	}	

}
