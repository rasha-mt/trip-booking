package com.apiintegration.flightsearch.infrastructure.provider.liteapi.mapper;

import com.apiintegration.flightsearch.domain.model.*;
import com.apiintegration.flightsearch.infrastructure.provider.liteapi.dto.LiteApiResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LiteApiResponseMapper {

    public List<FlightOffer> mapToDomain(
            LiteApiResponse response) {

        return response.getData()
                .stream()
                .flatMap(data -> data.getJourneys().stream())
                .flatMap(journey -> journey.getOffers().stream()
                        .map(offer -> mapOffer(journey, offer)))
                .toList();
    }

    private FlightOffer mapOffer(
            LiteApiResponse.LiteApiJourney journey,
            LiteApiResponse.LiteApiOffer offer) {

        FlightOffer flightOffer = new FlightOffer();

        flightOffer.setId(offer.getOfferId());

        flightOffer.setPrice(mapPrice(offer));

        flightOffer.setAirline(mapAirline(journey));

        flightOffer.setSegments(
                journey.getSegments()
                        .stream()
                        .map(this::mapSegment)
                        .toList()
        );

        return flightOffer;
    }

    private Price mapPrice(
            LiteApiResponse.LiteApiOffer offer) {

        var display = offer.getPricing()
                .getDisplay();

        Price price = new Price();

        price.setAmount(display.getTotal());
        price.setCurrency(display.getCurrency());

        return price;
    }

    private Airline mapAirline(
            LiteApiResponse.LiteApiJourney journey) {

        if (journey.getSegments() == null ||
                journey.getSegments().isEmpty()) {
            return null;
        }

        var carrier = journey.getSegments()
                .get(0)
                .getCarrier();

        Airline airline = new Airline();

        airline.setCode(carrier.getMarketingCode());
        airline.setName(carrier.getMarketingName());

        return airline;
    }

    private FlightSegment mapSegment(
            LiteApiResponse.LiteApiSegment segment) {

        FlightSegment flightSegment = new FlightSegment();

        flightSegment.setOrigin(segment.getOriginCode());
        flightSegment.setOrigin(segment.getOriginName());

        flightSegment.setDestination(segment.getDestinationCode());
        flightSegment.setDestination(segment.getDestinationName());

        flightSegment.setDepartureTime(segment.getDepartureTime());
        flightSegment.setArrivalTime(segment.getArrivalTime());

        flightSegment.setFlightNumber(
                segment.getFlight().getMarketingNumber()
        );

        return flightSegment;
    }
}
