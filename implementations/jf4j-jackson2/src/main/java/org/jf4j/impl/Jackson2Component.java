package org.jf4j.impl;

import javax.inject.Singleton;

import org.jf4j.JsonFactory;

import dagger.Component;

@Singleton
@Component(modules = { Jackson2Module.class })
public interface Jackson2Component {
    JsonFactory getFactory();
}
