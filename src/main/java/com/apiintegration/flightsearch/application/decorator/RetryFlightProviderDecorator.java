package com.apiintegration.flightsearch.application.decorator;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

public class RetryFlightProviderDecorator
        extends FlightProviderDecorator {

    public RetryFlightProviderDecorator(
            FlightSearchProvider delegate) {
        super(delegate);
    }

    @Override
    public Mono<List<FlightOffer>> searchFlights(
            FlightSearchRequest request) {

        return delegate.searchFlights(request)
                .retryWhen(
                        Retry.backoff(
                                2,
                                Duration.ofSeconds(1)
                        )
                );
    }
}