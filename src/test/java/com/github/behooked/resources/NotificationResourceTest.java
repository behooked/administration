package com.github.behooked.resources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;
import com.github.behooked.db.WebhookDAO;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class NotificationResourceTest {


	private static final WebhookDAO WEBHOOK_DAO = mock(WebhookDAO.class);

	private static final ResourceExtension EXT = ResourceExtension.builder() //
			.addResource(new NotificationResource(WEBHOOK_DAO)) //
			.build();

	private Webhook webhook;
	private Trigger trigger;
	private final List<Webhook> listWebhooks = new ArrayList<Webhook>();
	private ArrayNode expectedClientData;

	@BeforeEach
	void setUp()
	{
		webhook = new Webhook();
		trigger = new Trigger("trigger");
		webhook.setId(1L);
		webhook.setUrl("https://example.org");
		webhook.setTrigger(trigger);
		webhook.setSecret("testSecret");
		listWebhooks.add(webhook);

		// create dummyClientData
		final ObjectMapper mapper = new ObjectMapper();
		expectedClientData = mapper.createArrayNode();
		ObjectNode dataSetEntry= mapper.createObjectNode();

		dataSetEntry.put("url", "https://example.org");
		dataSetEntry.put("secret", "testSecret");
		expectedClientData.add(dataSetEntry);

	}
	@AfterEach
	void tearDown()
	{
		reset(WEBHOOK_DAO);
	}

	@Test
	void testSelectClientData() {

		final ObjectMapper mapper = new ObjectMapper();
		final ArrayNode testClientData= mapper.createArrayNode();

		NotificationResource.selectClientData(mapper,testClientData,listWebhooks);

		assertThat(testClientData).isEqualTo(expectedClientData);

	}


	@Test
	void testReceiveNotification() {

		String eventName = "trigger";

		when(WEBHOOK_DAO.findByTriggerName("trigger")).thenReturn(listWebhooks);

		Response response = EXT.target("/notifications").request(MediaType.APPLICATION_JSON).post(Entity.json(eventName));

		ArrayNode receivedClientdata = response.readEntity(ArrayNode.class);
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);	
		assertThat(receivedClientdata).isEqualTo(expectedClientData); 
	} 
}