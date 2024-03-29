package com.github.behooked;

import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
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
	
}
