package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.domain.model.SearchOwner;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlightCacheKeyService {

    public String generateCriteriaHash(
            FlightSearchRequest request) {

        String criteria = String.join(
                "|",
                normalize(request.origin()),
                normalize(request.destination()),
                String.valueOf(request.departureDate()),
                String.valueOf(request.returnDate()),
                request.cabinClass().name(),
                normalizePassengers(request)
        );

        return sha256(criteria);
    }

    private String normalizePassengers(
            FlightSearchRequest request) {

        return request.passengers()
                .stream()
                .filter(Objects::nonNull)
                .map(passenger ->
                        passenger.type().name()
                )
                .sorted()
                .collect(Collectors.joining(","));
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim().toUpperCase();
    }

    private String sha256(String value) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat
                    .of()
                    .formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable",
                    e
            );
        }
    }

    public String generateKey(
            SearchOwner owner,
            FlightSearchRequest request) {

        String criteriaHash =
                generateCriteriaHash(request);

        return String.format(
                "flight-search:%s:%s:%s",
                owner.type(),
                owner.id(),
                criteriaHash
        );
    }
}