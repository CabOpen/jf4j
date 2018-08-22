package org.jf4j.spi;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.jf4j.JsonFactory;
import org.jf4j.Logger;
import org.jf4j.impl.NoOpFactory;

public class JsonServiceProviderHolder {
    static {
        System.out.println("JsonServiceProviderHolder");
        DaggerJsonServiceProviderComponent.create().holder().set();
    }
    private Set<JsonFactory> holders;
    private Logger logger;
    private static volatile JsonFactory instance;
    private static volatile boolean hasLoadedPlugins;

    @Inject
    public JsonServiceProviderHolder(Set<JsonFactory> holders) {
        super();
        this.holders = holders;
        logger = Logger.getLogger();
    }

    /**
     * @return the holders
     */
    public Set<JsonFactory> getHolders() {
        return holders;
    }

    public synchronized void set() {
        System.out.println("CALLED SET");
        if (instance != null && !NoOpFactory.isNoOp(instance)) {
            return;
        }
        instance = fetchFactory();
    }

    private JsonFactory fetchFactory() {
        Iterator<JsonFactory> factoryIt = holders.iterator();
        System.out.println(holders);
        JsonFactory defaultFactory = null;
        while (factoryIt.hasNext()) {
            defaultFactory = factoryIt.next();
            if (!isDefaultImplementation(defaultFactory)) {
                return defaultFactory;
            }
        }
        logger.logWarn("No JSON providers were found.");
        logger.logInfo("Defaulting to " + defaultImplementationDescription());
        return defaultFactory;
    }

    private boolean isDefaultImplementation(JsonFactory defaultFactory) {
        return NoOpFactory.isNoOp(defaultFactory);
    }

    private String defaultImplementationDescription() {
        return NoOpFactory.FACTORY_DESCRITION;
    }

    public static JsonFactory get() {
        loadPlugins();
        return instance;
    }

    private static void loadPlugins() {
        if (hasLoadedPlugins) {
            return;
        }

        Consumer<JsonServiceProvider> action = new Consumer<JsonServiceProvider>() {

            @Override
            public void accept(JsonServiceProvider plugin) {
                if (plugin != null) {
                    plugin.load();
                }

            }
        };
        ServiceLoader<JsonServiceProvider> plugins = ServiceLoader.load(JsonServiceProvider.class);
        plugins.forEach(action);
        plugins = ServiceLoader.loadInstalled(JsonServiceProvider.class);
        plugins.forEach(action);
        hasLoadedPlugins = true;
    }

}
