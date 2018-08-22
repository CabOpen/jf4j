package org.jf4j.impl;

import javax.inject.Singleton;

import org.jf4j.JsonFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class NoOpModule {
    @Singleton
    @Provides
    JsonFactory createFactory() {
        return new NoOpFactory();
    }
}
