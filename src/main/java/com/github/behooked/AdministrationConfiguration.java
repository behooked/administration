package com.github.behooked;

import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class AdministrationConfiguration extends Configuration {
    
	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.database = dataSourceFactory;
	}
	
	// Jersey-Client

	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

	@JsonProperty("jerseyClient")
	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return jerseyClient;
	}

	@JsonProperty("jerseyClient")
	public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
		this.jerseyClient = jerseyClient;
	}
	
	// isTestRun
	
		private boolean testRun = false;
		
		@JsonProperty
		public boolean isTestRun() {
			return testRun;
		}
		
		@JsonProperty
		public void setTestRun(boolean testRun) {
			this.testRun = testRun;
		}
	
}
