package org.jf4j.impl;

import javax.inject.Singleton;

import org.jf4j.JsonFactory;

import dagger.Component;

@Singleton
@Component(modules = { NoOpModule.class })
public interface NoOpComponent {
    JsonFactory getFactory();
}
