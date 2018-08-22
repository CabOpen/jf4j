package org.jf4j.impl;

import org.jf4j.JsonFactory;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class Jackson2ServiceProviderModule {

    @Provides
    @IntoSet
    static JsonFactory jackson2ServiceHolder() {
        return DaggerJackson2Component.builder().build().getFactory();
    }

}
