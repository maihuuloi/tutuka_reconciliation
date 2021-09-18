package com.tutuka.txmanagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Mas N
 */
@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    CommonsRequestLoggingFilter logFilter() {
        final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(200);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("DATA : ");
        return filter;
    }
}
