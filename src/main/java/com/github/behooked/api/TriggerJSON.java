package com.github.behooked.api;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.behooked.core.Trigger;

import jakarta.validation.constraints.NotEmpty;





public class TriggerJSON {
    
	private long id;
    
	private String name;


	@JsonCreator
	public TriggerJSON(@JsonProperty("id") long id, @JsonProperty("name") String name) {

		this.id = id;
		this.name = name;

	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	public static TriggerJSON from(final Trigger trigger)
	{
		return new TriggerJSON(trigger.getId(), trigger.getName());
	}
}
