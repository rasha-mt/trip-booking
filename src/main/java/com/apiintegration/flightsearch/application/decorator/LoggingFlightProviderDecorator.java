package com.apiintegration.flightsearch.application.decorator;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
public class LoggingFlightProviderDecorator
        extends FlightProviderDecorator {

    public LoggingFlightProviderDecorator(
            FlightSearchProvider delegate) {
        super(delegate);
    }

    @Override
    public Mono<List<FlightOffer>> searchFlights(
            FlightSearchRequest request) {

        return delegate.searchFlights(request)
                .doOnSubscribe(subscription ->
                        log.info(
                                "Searching flights using {}",
                                delegate.getName()
                        )
                )
                .doOnSuccess(result ->
                        log.info(
                                "{} returned {} offers",
                                delegate.getName(),
                                result.size()
                        )
                )
                .doOnError(error ->
                        log.error(
                                "{} failed",
                                delegate.getName(),
                                error
                        )
                );
    }
}
