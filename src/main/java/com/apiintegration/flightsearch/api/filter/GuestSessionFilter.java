package com.apiintegration.flightsearch.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
public class GuestSessionFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME =
            "FLIGHT_SESSION_ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Cookie cookie =
                WebUtils.getCookie(
                        request,
                        COOKIE_NAME
                );

        if (cookie == null || cookie.getValue().isBlank()) {

            String sessionId =
                    UUID.randomUUID().toString();

            ResponseCookie responseCookie =
                    ResponseCookie.from(
                                    COOKIE_NAME,
                                    sessionId
                            )
                            .httpOnly(true)
                            .secure(true)
                            .sameSite("Lax")
                            .path("/")
                            .maxAge(Duration.ofHours(24))
                            .build();

            response.addHeader(
                    HttpHeaders.SET_COOKIE,
                    responseCookie.toString()
            );
        }

        filterChain.doFilter(request, response);
    }
}