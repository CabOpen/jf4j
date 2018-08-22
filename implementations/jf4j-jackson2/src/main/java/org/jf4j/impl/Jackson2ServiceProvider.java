/**
 *
 */
package org.jf4j.impl;

import org.jf4j.spi.JsonServiceProvider;

/**
 * @author adrcab01
 *
 */
public class Jackson2ServiceProvider implements JsonServiceProvider {

    /*
     * (non-Javadoc)
     *
     * @see org.jf4j.JsonServiceProvider#load()
     */
    @Override
    public void load() {
        System.out.println("Loading jackson");
        DaggerJackson2ServiceProviderComponent.create().holder().set();

    }

}
