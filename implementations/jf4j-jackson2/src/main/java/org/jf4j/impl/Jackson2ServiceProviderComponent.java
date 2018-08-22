package org.jf4j.impl;

import org.jf4j.spi.JsonServiceProviderHolder;
import org.jf4j.spi.JsonServiceProviderModule;

import dagger.Component;

@Component(modules = { JsonServiceProviderModule.class, Jackson2ServiceProviderModule.class })
public interface Jackson2ServiceProviderComponent {
    JsonServiceProviderHolder holder();

}
