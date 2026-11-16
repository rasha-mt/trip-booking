package com.apiintegration.flightsearch.infrastructure.provider.liteapi.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class LiteApiResponse {

    private List<LiteApiData> data;

    @Getter
    @Setter
    public static class LiteApiData {

        private List<LiteApiJourney> journeys;

        private LiteApiSortMetadata sortMetadata;
    }

    @Getter
    @Setter
    public static class LiteApiJourney {

        private String journeyKey;

        private boolean isCheapest;

        private String timestamp;

        private LiteApiDuration totalDuration;

        private List<LiteApiLegDuration> legDurations;

        private LiteApiParameters parameters;

        private List<LiteApiSegment> segments;

        private List<LiteApiOffer> offers;
    }

    @Getter
    @Setter
    public static class LiteApiDuration {

        private String iso8601;

        private Integer minutes;
    }

    @Getter
    @Setter
    public static class LiteApiLegDuration {

        private String direction;

        private LiteApiDuration duration;

        private Integer dayChange;

        private boolean overnightFlight;
    }

    @Getter
    @Setter
    public static class LiteApiParameters {

        private Integer adults;

        private Integer children;

        private Integer infants;
    }

    @Getter
    @Setter
    public static class LiteApiSegment {

        private String segmentKey;

        private Integer stopCount;

        private String originCode;

        private String originName;

        private String destinationCode;

        private String destinationName;

        private LocalDateTime departureTime;

        private LocalDateTime arrivalTime;

        private String direction;

        private LiteApiDuration duration;


        private LiteApiFlight flight;

        private LiteApiCarrier carrier;
    }

    @Getter
    @Setter
    public static class LiteApiFlight {

        private String marketingNumber;

        private String operatingNumber;
    }

    @Getter
    @Setter
    public static class LiteApiCarrier {

        private String marketingCode;

        private String marketingLogo;

        private String marketingName;

        private String operatingCode;

        private String operatingLogo;

        private String operatingName;
    }

    @Getter
    @Setter
    public static class LiteApiOffer {

        private String offerId;

        private String expiration;

        private LiteApiPricing pricing;

        private LiteApiProvider provider;

        private LiteApiFare fare;

        private LiteApiBaggage baggage;

        private LiteApiTerms terms;

        private List<LiteApiSegmentFare> segmentFares;

        private List<LiteApiSegmentAmenity> segmentAmenities;
    }

    @Getter
    @Setter
    public static class LiteApiPricing {

        private LiteApiDisplay display;

        private boolean converted;
    }

    @Getter
    @Setter
    public static class LiteApiDisplay {

        private BigDecimal total;

        private String currency;

        private BigDecimal base;

        private BigDecimal fees;

        private BigDecimal taxes;

        private LiteApiPerPassenger perPassenger;
    }

    @Getter
    @Setter
    public static class LiteApiPerPassenger {

        private LiteApiPassengerPrice adult;

        private LiteApiPassengerPrice child;

        private LiteApiPassengerPrice infant;
    }

    @Getter
    @Setter
    public static class LiteApiPassengerPrice {

        private BigDecimal base;

        private String currency;

        private BigDecimal fees;

        private BigDecimal taxes;

        private BigDecimal total;
    }

    @Getter
    @Setter
    public static class LiteApiProvider {

        private String code;

        private String logo;
    }

    @Getter
    @Setter
    public static class LiteApiFare {

        private String family;

        private boolean mixedCabin;

        private boolean mixedFareFamily;

        private Integer seatsRemaining;
    }

    @Getter
    @Setter
    public static class LiteApiBaggage {

        private boolean hasCarryOnBag;

        private boolean hasCheckedBag;

        private List<LiteApiBaggageItem> included;
    }

    @Getter
    @Setter
    public static class LiteApiBaggageItem {

        private String bagType;

        private String description;

        private String passengerType;

        private Integer pieces;

        private LiteApiBaggagePricing pricing;

        private String unit;

        private Integer weightKg;
    }

    @Getter
    @Setter
    public static class LiteApiBaggagePricing {

        private LiteApiAmount display;

        private boolean converted;
    }

    @Getter
    @Setter
    public static class LiteApiAmount {

        private BigDecimal amount;

        private String currency;
    }

    @Getter
    @Setter
    public static class LiteApiTerms {

        private boolean changeable;

        private boolean refundable;

        private List<LiteApiSummary> summary;

        private LiteApiChangeFee changeFee;

        private LiteApiRefundFee refundFee;
    }

    @Getter
    @Setter
    public static class LiteApiSummary {

        private String level;

        private String message;
    }

    @Getter
    @Setter
    public static class LiteApiChangeFee {

        private LiteApiFeePricing pricing;

        private BigDecimal percent;

        private String applicability;

        private String label;
    }

    @Getter
    @Setter
    public static class LiteApiRefundFee {

        private LiteApiFeePricing pricing;
    }

    @Getter
    @Setter
    public static class LiteApiFeePricing {

        private LiteApiAmount display;

        private boolean converted;
    }

    @Getter
    @Setter
    public static class LiteApiSegmentFare {

        private String segmentKey;

        private String bookingCode;

        private String cabin;

        private String fareBasisCode;

        private String fareFamily;

        private Integer seatsRemaining;
    }

    @Getter
    @Setter
    public static class LiteApiSegmentAmenity {

        private String segmentKey;

        private String aircraftType;

        private List<LiteApiAmenity> amenities;
    }

    @Getter
    @Setter
    public static class LiteApiAmenity {

        private boolean available;

        private String category;

        private boolean chargeable;

        private String name;

        private String details;
    }

    @Getter
    @Setter
    public static class LiteApiSortMetadata {

        private LiteApiSortOption best;

        private LiteApiSortOption price;

        private LiteApiSortOption duration;

        private LiteApiSortOption stops;
    }

    @Getter
    @Setter
    public static class LiteApiSortOption {

        private String journeyKey;

        private String offerId;

        private BigDecimal price;

        private String currency;
    }
}
