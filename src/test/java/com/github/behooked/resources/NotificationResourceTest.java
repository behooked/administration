package com.github.behooked.resources;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;

public class NotificationResourceTest {


	@Test
	void testSelectClientData() {

		final ObjectMapper mapper = new ObjectMapper();

		// testData
		final ArrayNode testClientData = mapper.createArrayNode();
		ObjectNode dataSetEntry= mapper.createObjectNode();

		dataSetEntry.put("url", "https://example.io");
		dataSetEntry.put("secret", "seeecret");
		testClientData.add(dataSetEntry);

		// create parameter
		final ArrayNode clientData= mapper.createArrayNode();
		Trigger trigger = new Trigger("name");
		Webhook w = new Webhook("https://example.io", trigger, "seeecret");
		final List<Webhook> list = new ArrayList<Webhook>();
		list.add(w);

		NotificationResource.selectClientData(mapper,clientData,list);

		assertThat(clientData).isEqualTo(testClientData);
	}






}