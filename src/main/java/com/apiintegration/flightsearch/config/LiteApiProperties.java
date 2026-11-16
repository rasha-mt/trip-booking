package com.apiintegration.flightsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "flight.providers.liteapi")
@Getter
@Setter
public class LiteApiProperties {
    private String baseUrl;
    private String apiKey;
    private int timeout;
    private int maxResponseSize;
}
