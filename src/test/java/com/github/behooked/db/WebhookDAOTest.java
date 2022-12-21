package com.github.behooked.db;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class WebhookDAOTest {

// DAOTestExtension setups a Hibernate SessionFactory
    public DAOTestExtension database = DAOTestExtension.newBuilder().addEntityClass(Webhook.class).addEntityClass(Trigger.class).build();

    private WebhookDAO webhookDAO;
    private TriggerDAO triggerDAO;
    private Trigger trigger;
    
    @BeforeEach
    public void setUp() {
        webhookDAO = new WebhookDAO(database.getSessionFactory());
        triggerDAO = new TriggerDAO(database.getSessionFactory());
    }


 

    @Test
    void createWebhook(){
        final Webhook webhook = database.inTransaction(() -> {
        this.trigger = triggerDAO.create(new Trigger("triggerName"));
            return webhookDAO.create(new Webhook("https://example.org", trigger, "mySecret"));
        });
       
		assertEquals("triggerName",trigger.getName());
        assertThat(webhook.getUrl()).isEqualTo("https://example.org");
        assertThat(webhook.getTrigger().getName()).isEqualTo("triggerName");
        assertThat(webhook.getSecret()).isEqualTo("mySecret");
        assertThat(webhookDAO.findById(webhook.getId())).isEqualTo(Optional.of(webhook));
    }
    

    @Test
    void handlesNullURL() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() ->
                database.inTransaction(() -> webhookDAO.create(new Webhook(null, trigger, "mySecret"))));
    }
	
}
