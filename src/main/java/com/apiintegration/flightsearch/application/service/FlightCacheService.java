package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.domain.model.FlightOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor

public class FlightCacheService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Mono<List<FlightOffer>> get(String key) {

        return redisTemplate
                .opsForValue()
                .get(key)
                .flatMap(this::deserialize);
    }

    public Mono<Boolean> put(
            String key,
            List<FlightOffer> offers,
            Duration ttl) {

        return serialize(offers)
                .flatMap(json ->
                        redisTemplate
                                .opsForValue()
                                .set(key, json, ttl)
                );
    }

    private Mono<List<FlightOffer>> deserialize(String json) {
        try {
            List<FlightOffer> offers = objectMapper.readValue(
                    json,
                    objectMapper.getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    FlightOffer.class
                            )
            );

            return Mono.just(offers);

        } catch (Exception e) {
            return Mono.error(
                    new IllegalStateException(
                            "Failed to deserialize cached flight offers",
                            e
                    )
            );
        }
    }

    private Mono<String> serialize(List<FlightOffer> offers) {
        try {
            return Mono.just(
                    objectMapper.writeValueAsString(offers)
            );
        } catch (Exception e) {
            return Mono.error(
                    new IllegalStateException(
                            "Failed to serialize flight offers",
                            e
                    )
            );
        }
    }
}

