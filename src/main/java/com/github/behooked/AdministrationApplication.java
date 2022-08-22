package com.github.behooked;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class AdministrationApplication extends Application<AdministrationConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AdministrationApplication().run(args);
    }

    @Override
    public String getName() {
        return "Administration";
    }

    @Override
    public void initialize(final Bootstrap<AdministrationConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AdministrationConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
