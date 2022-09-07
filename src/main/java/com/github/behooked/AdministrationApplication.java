package com.github.behooked;



import org.glassfish.jersey.servlet.ServletContainer;

import com.github.behooked.core.Trigger;
import com.github.behooked.core.Webhook;
import com.github.behooked.db.TriggerDAO;
import com.github.behooked.db.WebhookDAO;
import com.github.behooked.resources.NotificationResource;
import com.github.behooked.resources.TriggerResource;
import com.github.behooked.resources.WebhookResource;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import jakarta.ws.rs.client.Client;

public class AdministrationApplication extends Application<AdministrationConfiguration> {

	public static void main(final String[] args) throws Exception {
		new AdministrationApplication().run(args);
	}

	private final HibernateBundle<AdministrationConfiguration> hibernate = new HibernateBundle<AdministrationConfiguration>(Webhook.class, Trigger.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(AdministrationConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "Administration";
	}

	@Override
	public void initialize(final Bootstrap<AdministrationConfiguration> bootstrap) {
		bootstrap.addBundle(hibernate);
	}

	@Override
	public void run(final AdministrationConfiguration configuration,
			final Environment environment) {
		final TriggerDAO triggerDao = new TriggerDAO(hibernate.getSessionFactory());
		final WebhookDAO dao = new WebhookDAO(hibernate.getSessionFactory());
		environment.jersey().register(new WebhookResource(dao, triggerDao));

		// Client

		final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration()).build(getName());

		// create new jersey servlet for admin port
		DropwizardResourceConfig jerseyConfig = new DropwizardResourceConfig(environment.metrics());
		JerseyContainerHolder servletContainerHolder = new JerseyContainerHolder(new ServletContainer(jerseyConfig));

		// add jersey servlet to admin port (adminEnvironment) and map servlet to /admin/* 
		environment.admin().addServlet("admin resources", servletContainerHolder.getContainer()).addMapping("/api/*");

		// UnitOfWorkAwareProxyFactory - A factory for creating proxies for components that use Hibernate data access objects outside Jersey resources. 

		//create + register proxy for TriggerResource 

		TriggerResource proxyTriggerResource = new UnitOfWorkAwareProxyFactory(hibernate) .create(TriggerResource.class, TriggerDAO.class, triggerDao);

		jerseyConfig.register(proxyTriggerResource);





		environment.jersey().register(new NotificationResource(dao,client));
		//enable Jackson
		jerseyConfig.register(new JacksonMessageBodyProvider(Jackson.newObjectMapper())); 
		// JacksonMessageBodyProvider() : enables using Jackson to parse request entities into objects and generate response entities from objects. 
	}


}
