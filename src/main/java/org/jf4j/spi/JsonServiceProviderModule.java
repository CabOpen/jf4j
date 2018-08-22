package org.jf4j.spi;

import org.jf4j.JsonFactory;
import org.jf4j.impl.DaggerNoOpComponent;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class JsonServiceProviderModule {

    @Provides
    @IntoSet
    static JsonFactory defaultFactory() {
        return DaggerNoOpComponent.builder().build().getFactory();
    }

}
