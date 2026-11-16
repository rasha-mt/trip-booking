package com.apiintegration.flightsearch.application.aggregator;

import com.apiintegration.flightsearch.api.dto.request.FlightSearchRequest;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.application.service.FlightCacheKeyService;
import com.apiintegration.flightsearch.application.service.FlightCacheService;
import com.apiintegration.flightsearch.application.service.SearchOwnerResolver;
import com.apiintegration.flightsearch.domain.model.FlightOffer;
import com.apiintegration.flightsearch.domain.model.SearchOwner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j

public class FlightAggregator {

    private static final Duration CACHE_TTL = Duration.ofMinutes(15);
    private static final Duration PROVIDER_TIMEOUT = Duration.ofSeconds(120);
    private static final int MAX_OFFERS = 20;

    private final List<FlightSearchProvider> providers;
    private final FlightCacheService cacheService;
    private final FlightCacheKeyService cacheKeyService;
    private final SearchOwnerResolver searchOwnerResolver;


    public FlightAggregator(
            @Qualifier("duffelProvider")
            FlightSearchProvider duffel,

            @Qualifier("liteApiProvider")
            FlightSearchProvider liteApi,

            @Qualifier("aviationstackProvider")
            FlightSearchProvider aviationstack,

            FlightCacheService cacheService,
            FlightCacheKeyService cacheKeyService, SearchOwnerResolver searchOwnerResolver) {
        this.searchOwnerResolver = searchOwnerResolver;

        this.providers = List.of(
                duffel,
                liteApi,
                aviationstack
        );

        this.cacheService = cacheService;
        this.cacheKeyService = cacheKeyService;
    }


    public Mono<List<FlightOffer>> searchFlights(
            FlightSearchRequest request,
            HttpServletRequest httpRequest) {

        SearchOwner owner = searchOwnerResolver.resolve(httpRequest);

        String cacheKey = cacheKeyService.generateKey(owner, request);

        return cacheService.get(cacheKey)
                .doOnNext(offers ->
                        log.debug("Flight cache hit for owner type={}",
                                owner.type()))
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Flight cache miss for owner type={}",
                            owner.type());

                    return searchProviders(request)
                            .flatMap(offers ->
                                    cacheService.put(cacheKey, offers, CACHE_TTL)
                                            .onErrorResume(error -> {
                                                log.warn(
                                                        "Failed to cache flight offers: {}",
                                                        error.getMessage());
                                                return Mono.just(false);
                                            })
                                            .thenReturn(offers));
                }));
    }

    private Mono<List<FlightOffer>> searchProviders(
            FlightSearchRequest request) {

        return Flux.fromIterable(providers)
                .flatMap(provider ->
                                provider.searchFlights(request)
                                        .timeout(PROVIDER_TIMEOUT)
                                        .doOnError(error ->
                                                log.error(
                                                        "Flight provider {} failed: {}",
                                                        provider.getName(),
                                                        error.getMessage()))
                                        .onErrorResume(error -> Mono.empty()),
                        providers.size())
                .flatMap(Flux::fromIterable)
                .take(MAX_OFFERS)
                .collectList();
    }
}
