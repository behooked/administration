package com.github.behooked.api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.behooked.core.Trigger;

import io.dropwizard.jackson.Jackson;

public class TriggerJsonTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	private Trigger trigger;


	@Test
	public void testTriggerJSON(){
		final TriggerJSON triggerJSON = new TriggerJSON(1,"triggerName");
		
		assertThat(triggerJSON.getName()).isEqualTo("triggerName");
		assertThat(triggerJSON.getId()).isEqualTo(1);
	}

	@Test
	public void convertSuccessfully() {
		trigger = new Trigger("dummyTrigger");
		assertTrue(TriggerJSON.from(trigger) instanceof TriggerJSON);

	}


	@Test
	void serializesToJSON() throws Exception {
		final TriggerJSON triggerJSON = new TriggerJSON(1,"trigger");

		final String expected = MAPPER.writeValueAsString(
				MAPPER.readValue(getClass().getResource("/trigger.json"), TriggerJSON.class));

		assertThat(MAPPER.writeValueAsString(triggerJSON)).isEqualTo(expected);
	}


	@Test
	public void deserializesFromJSON() throws Exception {
		final TriggerJSON trigger = new TriggerJSON(1,"trigger");

		TriggerJSON deserialized = MAPPER.readValue(getClass().getResource("/trigger.json"), TriggerJSON.class);
		assertThat(deserialized.getName()).isEqualTo(trigger.getName());
		assertThat(deserialized.getId()).isEqualTo(trigger.getId());

	} 

}
