package com.nhnacademy.gateway.config;


import com.nhnacademy.gateway.filter.JwtAuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteLocatorConfig {

    private final JwtAuthorizationHeaderFilter jwtAuthorizationHeaderFilter;

    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder ) {

        return builder.routes()
                .route("auth-api", p->p.path("/auth")
                        .filters(f->f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config())))
                        .uri("lb://AUTH-SERVER")
                )
                .build();

    }
}
