package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import com.apiintegration.flightsearch.infrastructure.provider.duffel.DuffelClient;
import com.apiintegration.flightsearch.infrastructure.provider.duffel.DuffelResponseMapper;
import com.apiintegration.flightsearch.infrastructure.provider.duffel.dto.DuffelOfferRequestPayload;
import com.apiintegration.flightsearch.infrastructure.provider.duffel.dto.DuffelResponseWrapper;
import com.apiintegration.flightsearch.infrastructure.provider.duffel.exception.DuffelApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuffelService implements FlightSearchProvider {

    private final DuffelClient duffelClient;
    private final DuffelResponseMapper duffelResponseMapper;
    private final ObjectMapper objectMapper;

    @Override
    public String getName() {
        return "duffel";
    }

    @Override
    public Mono<List<FlightOffer>> searchFlights(
            FlightSearchRequest request) {

        DuffelOfferRequestPayload payload =
                DuffelOfferRequestPayload.builder()
                        .data(
                                DuffelOfferRequestPayload.DataWrapper.builder()
                                        .slices(List.of(
                                                DuffelOfferRequestPayload.Slice.builder()
                                                        .origin(request.origin())
                                                        .destination(request.destination())
                                                        .departureDate(
                                                                request.departureDate().toString()
                                                        )
                                                        .build()
                                        ))
                                        .passengers(
                                                toDuffelPassengers(request)
                                        )
                                        .cabinClass(
                                                request.cabinClass()
                                                        .name()
                                                        .toLowerCase()
                                        )
                                        .build()
                        )
                        .build();

        return duffelClient.createOfferRequest(payload)
                .flatMap(rawJsonResponse -> {

                    try {

                        DuffelResponseWrapper responseWrapper =
                                objectMapper.readValue(
                                        rawJsonResponse,
                                        DuffelResponseWrapper.class
                                );

                        List<FlightOffer> offers =
                                duffelResponseMapper.mapToDomain(
                                        responseWrapper
                                );

                        return Mono.just(offers);

                    } catch (JacksonException e) {

                        return Mono.error(
                                new DuffelApiException(
                                        "Malformed response from Duffel",
                                        e
                                )
                        );

                    } catch (RuntimeException e) {

                        return Mono.error(
                                new DuffelApiException(
                                        "Failed to map Duffel response",
                                        e
                                )
                        );
                    }
                });
    }

    private List<DuffelOfferRequestPayload.Passenger> toDuffelPassengers(
            FlightSearchRequest request) {

        if (request.passengers() == null ||
                request.passengers().isEmpty()) {

            return List.of(
                    DuffelOfferRequestPayload.Passenger.builder()
                            .type("adult")
                            .build()
            );
        }

        return request.passengers()
                .stream()
                .map(passenger ->
                        DuffelOfferRequestPayload.Passenger.builder()
                                .type(
                                        passenger.type()
                                                .name()
                                                .toLowerCase()
                                )
                                .build()
                )
                .toList();
    }
}