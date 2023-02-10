package com.github.behooked.core;

import java.util.Objects;
import java.util.Set;

import com.github.behooked.api.TriggerJSON;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Trigger")

@NamedQuery(
		name = "com.github.behooked.core.Trigger.findAll",
		query = "SELECT t FROM Trigger t")
@NamedQuery(
		name = "com.github.behooked.core.Trigger.findByName",
		query = "SELECT t FROM Trigger t WHERE t.name = :triggerName")


public class Trigger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // generate primary key value
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "trigger")
	private Set<Webhook> webhooks;

	public Trigger(){

	}

	public Trigger(final String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}


	public void setId(final long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Trigger)) {
			return false;
		}

		final Trigger trigger= (Trigger) o;

		return id == trigger.id &&
				name == trigger.name &&
				Objects.equals(name, trigger.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	public static Trigger convertToTrigger(final TriggerJSON triggerJSON)
	{
		return new Trigger( triggerJSON.getName());
	}

}
