
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.behooked.AdministrationApplication;
import com.github.behooked.AdministrationConfiguration;
import com.github.behooked.api.TriggerJSON;
import com.github.behooked.api.WebhookJSON;

import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {

	private static DropwizardAppExtension<AdministrationConfiguration> APP= new DropwizardAppExtension<>(
			AdministrationApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

	@Test
	public void testPostWebhook() {

		final TriggerJSON trigger = new TriggerJSON(1,"dummyTrigger");

		// test post Trigger
		Response responseTrigger = APP.client().target("http://localhost:8081/api").path("triggers")
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(trigger, MediaType.APPLICATION_JSON_TYPE));

		TriggerJSON postedTrigger = responseTrigger.readEntity(TriggerJSON.class);

		assertThat(postedTrigger.getName()).isEqualTo("dummyTrigger");



		//test post Webhook

		final Response responseWebhook= APP.client().target("http://localhost:8080/webhooks-service")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(new WebhookJSON(1,"https://example.org", postedTrigger.getName(), "dummySecret")));


		WebhookJSON postedWebhook = responseWebhook.readEntity(WebhookJSON.class);

		assertThat(postedWebhook.getUrl()).isEqualTo("https://example.org");
		assertThat(postedWebhook.getTrigger()).isEqualTo(postedTrigger.getName());
		assertThat(postedWebhook.getSecret()).isEqualTo("dummySecret");   
/*
	       final Long eventId = 1l;
			final String eventName = "eventName";
			Response response = APP.client().target("http://localhost:8081/api").path("notifications")
					.request(MediaType.APPLICATION_JSON).header("Behooked-Dispatcher-Notification-EventId",eventId).post(Entity.json(eventName));

			assertThat(response).extracting(Response::getStatus).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
		
	}  
	
	
	

	@Test
	void testReceiveNotification() {
		
        final Long eventId = 1l;
		final String eventName = "eventName";
		Response response = APP.client().target("http://localhost:8081/api").path("notifications")
				.request(MediaType.APPLICATION_JSON).header("Behooked-Dispatcher-Notification-EventId",eventId).post(Entity.json(eventName));

		assertThat(response).extracting(Response::getStatus).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
	
	}
	*/
	}
}
