package com.apiintegration.flightsearch.application.decorator;

import com.apiintegration.flightsearch.application.port.FlightSearchProvider;

public abstract class FlightProviderDecorator
        implements FlightSearchProvider {

    protected final FlightSearchProvider delegate;

    protected FlightProviderDecorator(FlightSearchProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
