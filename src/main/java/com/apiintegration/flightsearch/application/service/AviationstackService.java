package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import com.apiintegration.flightsearch.infrastructure.provider.aviationstack.AviationstackClient;
import com.apiintegration.flightsearch.infrastructure.provider.aviationstack.AviationstackResponseMapper;
import com.apiintegration.flightsearch.infrastructure.provider.aviationstack.dto.AviationstackFlightSearchRequest;
import com.apiintegration.flightsearch.infrastructure.provider.aviationstack.dto.AviationstackResponseWrapper;
import com.apiintegration.flightsearch.infrastructure.provider.aviationstack.exception.AviationstackMalformedResponseException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class AviationstackService implements FlightSearchProvider {

    private final AviationstackClient aviationstackClient;
    private final AviationstackResponseMapper responseMapper;
    private final ObjectMapper objectMapper;

    public AviationstackService(
            AviationstackClient aviationstackClient,
            AviationstackResponseMapper responseMapper,
            ObjectMapper objectMapper) {

        this.aviationstackClient = aviationstackClient;
        this.responseMapper = responseMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getName() {
        return "Aviationstack";
    }

    @Override
    public Mono<List<FlightOffer>> searchFlights(FlightSearchRequest request) {

        AviationstackFlightSearchRequest providerRequest =
                new AviationstackFlightSearchRequest(
                        request.origin(),
                        request.destination(),
                        request.departureDate().toString(),
                        "scheduled",
                        100
                );

        return aviationstackClient
                .searchFlights(providerRequest)
                .flatMap(rawJson -> {
                    try {
                        AviationstackResponseWrapper response =
                                objectMapper.readValue(
                                        rawJson,
                                        AviationstackResponseWrapper.class
                                );

                        return responseMapper.mapToDomain(response);

                    } catch (Exception exception) {
                        return Mono.error(
                                new AviationstackMalformedResponseException(
                                        "Invalid or malformed response from Aviationstack",
                                        exception
                                )
                        );
                    }
                });
    }
}
