package com.medviv.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class SpringOauth2Config extends ResourceServerConfigurerAdapter {
    @Value("${oauth_client_secret}")
    private String clientSecret;

    @Value("${oauth_client_id}")
    private String clientId;

    @Value("${oauth_server_url}")
    private String authUrl;

    private static String TOP_LEVEL_API = "/api/v1/**";
    private static String READ_SCOPE = "#oauth2.hasScope('read')";

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(TOP_LEVEL_API)
            .access(READ_SCOPE);
    }

    @Bean
    public RemoteTokenServices tokenServices() {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(authUrl);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);

        return tokenServices;
    }
}
