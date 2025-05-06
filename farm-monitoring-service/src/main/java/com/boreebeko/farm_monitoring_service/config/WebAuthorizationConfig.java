package com.boreebeko.farm_monitoring_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up web-authorization.
 *
 *  <p>
 *  This class defines the SecurityFilterChain bean
 *  user throughout the application for authorization purposes.
 *  </p>
 *
 *  Annotated with {@code @Configuration} to indicate it provides Spring Beans.
 *  Enables web security with {@code @EnableWebSecurity}
 *
 * @author Beknur Tumenov
 * @since  2025-05-06
 */
@Configuration
@EnableWebSecurity
public class WebAuthorizationConfig {

    /**
     * Configures the {@link HttpSecurity} for the application.
     *
     * <p>
     *     Permits unauthenticated access to Swagger documentation endpoints and
     *     requires authentication for all other requests.
     *     Configures OAuth2 resource server with JWT support.
     * </p>
     *
     * @param httpSecurity the {@link HttpSecurity} instance to configure
     * @return a configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(http -> http
                        .requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }
}
