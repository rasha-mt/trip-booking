package com.apiintegration.flightsearch.application.service;

import com.apiintegration.flightsearch.domain.model.SearchOwner;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.UUID;

@Component
public class SearchOwnerResolver {

    private static final String COOKIE_NAME = "FLIGHT_SESSION_ID";

    public SearchOwner resolve(HttpServletRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            String userId = authentication.getName();

            return new SearchOwner(
                    "user",
                    userId
            );
        }

        Cookie cookie = WebUtils.getCookie(
                request,
                COOKIE_NAME
        );

        if (cookie != null && !cookie.getValue().isBlank()) {

            return new SearchOwner(
                    "guest",
                    cookie.getValue()
            );
        }

        return new SearchOwner(
                "guest",
                UUID.randomUUID().toString()
        );
    }
}