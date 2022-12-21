import org.junit.jupiter.api.Test;

import com.github.behooked.AdministrationApplication;
import com.github.behooked.AdministrationConfiguration;
import com.github.behooked.api.TriggerJSON;
import com.github.behooked.api.WebhookJSON;

import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class IntegrationsTest {

	private static DropwizardAppExtension<AdministrationConfiguration> APP= new DropwizardAppExtension<>(
			AdministrationApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));



	@Test
	void testPostTrigger() {
		final TriggerJSON trigger= new TriggerJSON(1,"testTrigger");
		final TriggerJSON postedTrigger = postTrigger(trigger);
		assertThat(postedTrigger.getName()).isEqualTo("testTrigger");
	}


	private TriggerJSON postTrigger(TriggerJSON trigger) {
		return APP.client().target("http://localhost:8081/api").path("triggers")
				.request()
				.post(Entity.entity(trigger, MediaType.APPLICATION_JSON_TYPE))
				.readEntity(TriggerJSON.class);
	}


	@Test
	void testPostWebhook() {

		final TriggerJSON trigger= new TriggerJSON(1,"testTrigger");
		final TriggerJSON postedTrigger = postTrigger(trigger);

		final WebhookJSON postedWebhook = APP.client().target("http://localhost:8085/webhooks-service")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(new WebhookJSON(1,"https://example.org", postedTrigger.getName(), "dummySecret")))
				.readEntity(WebhookJSON.class);

		assertThat(postedWebhook.getUrl()).isEqualTo("https://example.org");
		assertThat(postedWebhook.getTrigger()).isEqualTo(postedTrigger.getName());
		assertThat(postedWebhook.getSecret()).isEqualTo("dummySecret");

	}




}
