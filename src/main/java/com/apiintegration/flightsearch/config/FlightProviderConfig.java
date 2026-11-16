package com.apiintegration.flightsearch.config;

import com.apiintegration.flightsearch.application.decorator.LoggingFlightProviderDecorator;
import com.apiintegration.flightsearch.application.decorator.RetryFlightProviderDecorator;
import com.apiintegration.flightsearch.application.port.FlightSearchProvider;
import com.apiintegration.flightsearch.application.service.AviationstackService;
import com.apiintegration.flightsearch.application.service.DuffelService;
import com.apiintegration.flightsearch.application.service.LiteApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightProviderConfig {

    @Bean("duffelProvider")
    public FlightSearchProvider duffelProvider(
            @Qualifier("duffelService")
            DuffelService provider) {

        return new RetryFlightProviderDecorator(
                new LoggingFlightProviderDecorator(
                        provider
                )
        );
    }

    @Bean("liteApiProvider")
    public FlightSearchProvider liteApiProvider(
            @Qualifier("liteApiService")
            LiteApiService provider) {

        return new RetryFlightProviderDecorator(
                new LoggingFlightProviderDecorator(
                        provider
                )
        );
    }

    @Bean("aviationstackProvider")
    public FlightSearchProvider aviationstackProvider(

            AviationstackService provider) {

        return new RetryFlightProviderDecorator(
                new LoggingFlightProviderDecorator(
                        provider
                )
        );
    }
}