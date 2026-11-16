package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import com.apiintegration.flightsearch.infrastructure.provider.liteapi.dto.LiteApiFlightRequest;
import com.apiintegration.flightsearch.infrastructure.provider.liteapi.dto.LiteApiResponse;
import com.apiintegration.flightsearch.infrastructure.provider.liteapi.mapper.LiteApiResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class LiteApiService  implements FlightSearchProvider {
    private final WebClient liteapiWebClient;
    private final LiteApiResponseMapper liteApiResponseMapper;


    public LiteApiService(
            @Qualifier("liteapiWebClient") WebClient webClient,
            LiteApiResponseMapper liteApiResponseMapper) {
        this.liteapiWebClient = webClient;
        this.liteApiResponseMapper = liteApiResponseMapper;
    }

    @Override
    public String getName() {
        return "LiteApi";
    }
    @Override
    public Mono<List<FlightOffer>> searchFlights(FlightSearchRequest request) {

        LiteApiFlightRequest payload = LiteApiFlightRequest.builder()
                .legs(buildLegs(request))
                .passengers(buildPassengers(request))
                .build();
        log.info("LiteAPI request body: {}", payload);

        return   liteapiWebClient.post()
                .uri("/flights/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error(
                                            "LiteAPI error: status={}, body={}",
                                            response.statusCode(),
                                            body
                                    );

                                    return Mono.error(
                                            new RuntimeException(
                                                    "LiteAPI error: " + body
                                            )
                                    );
                                })
                )
                .bodyToMono(LiteApiResponse.class)
                .map(liteApiResponseMapper::mapToDomain)
                .doOnError(error ->
                        log.error(
                                "LiteAPI flight search failed",
                                error
                        )
                );


    }

    private List<LiteApiFlightRequest.Leg> buildLegs(
            FlightSearchRequest request) {

        List<LiteApiFlightRequest.Leg> legs = new ArrayList<>();

        legs.add(
                LiteApiFlightRequest.Leg.builder()
                        .origin(request.origin())
                        .destination(request.destination())
                        .date(request.departureDate().toString())
                        .build()
        );

        if (request.returnDate() != null) {
            legs.add(
                    LiteApiFlightRequest.Leg.builder()
                            .origin(request.destination())
                            .destination(request.origin())
                            .date(request.returnDate().toString())
                            .build()
            );
        }

        return legs;
    }

    private List<LiteApiFlightRequest.Passenger> buildPassengers(
            FlightSearchRequest request) {

        return request.passengers().stream()
                .map(passenger ->
                        LiteApiFlightRequest.Passenger.builder()
                                .type(passenger.type().name().toLowerCase())
                                .build()
                )
                .toList();
    }
}
