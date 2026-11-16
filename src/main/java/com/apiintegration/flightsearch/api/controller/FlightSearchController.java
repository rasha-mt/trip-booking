package com.apiintegration.flightsearch.api.controller;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.aggregator.FlightAggregator;

import com.apiintegration.flightsearch.domain.model.FlightOffer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@Slf4j
public class FlightSearchController {

    private final FlightAggregator flightAggregator;

    public FlightSearchController(FlightAggregator flightAggregator) {
       this.flightAggregator = flightAggregator;
    }


    @PostMapping("/search")
    public Mono<List<FlightOffer>> searchFlights(
            @Valid @RequestBody FlightSearchRequest request,
            HttpServletRequest httpRequest) {

        return flightAggregator.searchFlights(request, httpRequest);
    }
}
