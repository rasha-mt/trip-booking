package com.apiintegration.flightsearch.application.port;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FlightSearchProvider {
    String getName();
    Mono<List<FlightOffer>> searchFlights(FlightSearchRequest request);

}
