package com.github.behooked.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.github.behooked.api.TriggerJSON;

public class TriggerTest {
	
	private TriggerJSON triggerJson = new TriggerJSON(1, "dummyTrigger");
	
	@Test
	public void convertSuccessfully() {
		
		assertTrue(Trigger.convertToTrigger(triggerJson) instanceof Trigger);
	
	}

}
