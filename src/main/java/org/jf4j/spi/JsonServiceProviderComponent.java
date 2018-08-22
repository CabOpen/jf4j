package org.jf4j.spi;

import dagger.Component;

@Component(modules = JsonServiceProviderModule.class)
public interface JsonServiceProviderComponent {
    JsonServiceProviderHolder holder();
}
