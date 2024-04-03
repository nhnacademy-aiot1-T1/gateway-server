package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.filter.CustomGlobalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FilterConfig {

    @Bean
    public GlobalFilter customFilter(){return new CustomGlobalFilter();
    }
}
