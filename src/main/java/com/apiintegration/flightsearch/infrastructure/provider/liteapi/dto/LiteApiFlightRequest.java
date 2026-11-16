package com.apiintegration.flightsearch.infrastructure.provider.liteapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LiteApiFlightRequest {

    private List<Leg> legs;
    private Integer adults;
    private List<Passenger> passengers;

    @Getter
    @Builder
    public static class Leg {
        private String origin;
        private String destination;
        private String date;
    }

    @Getter
    @Builder
    public static class Passenger {
        private String type;
    }
}