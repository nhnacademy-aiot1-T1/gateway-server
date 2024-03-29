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
                .route("auth-api", p->p.path("/api/auth/**")
                        //해당 필터는 account-api 서비스에만 적용됩니다.
                        .filters(f->f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config())))
                        .uri("http://localhost:8100")
                )
//                .route("shoppingmall-api", p->p.path("/api/shop/**")
//                        .and()
//                        .weight("shoppingmall-api",50)
//                        .uri("http://localhost:8200")
//                )
//                .route("shoppingmall-api", p->p.path("/api/shop/**").
//                        and()
//                        .weight("shoppingmall-api",50)
//                        .uri("http://localhost:8300"))
                .build();

    }
}
