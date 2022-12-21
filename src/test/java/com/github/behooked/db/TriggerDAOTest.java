package com.github.behooked.db;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TriggerDAOTest {

    // DAOTestExtension setups a Hibernate SessionFactory	
	public DAOTestExtension database = DAOTestExtension.newBuilder().addEntityClass(Trigger.class).addEntityClass(Webhook.class).build();


    private TriggerDAO triggerDAO;
    private WebhookDAO webhookDAO;

    
    @BeforeEach
    public void setUp() {
  
        triggerDAO = new TriggerDAO(database.getSessionFactory());
        webhookDAO = new WebhookDAO(database.getSessionFactory());
        
    }
    
    @Test
    void createTrigger(){
        final Trigger trigger = database.inTransaction(() -> {
            return triggerDAO.create(new Trigger("triggerName"));
        });
       
        assertEquals("triggerName",trigger.getName());
        assertThat(triggerDAO.findById(trigger.getId())).isEqualTo(Optional.of(trigger));
    }
	
	@Test
	void findAll() {
		database.inTransaction(() -> {
			triggerDAO.create(new Trigger("triggerA"));
			triggerDAO.create(new Trigger("triggerB"));
			triggerDAO.create(new Trigger("triggerC"));
		});

		final List<Trigger> triggers = triggerDAO.findAll();
		assertThat(triggers).extracting("name").containsOnly("triggerA", "triggerB", "triggerC");

	}
}
