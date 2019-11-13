package com.silenteight.sens.webapp.backend.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Andrzej Haczewski
 */
@Configuration
@RequiredArgsConstructor
public class WebConfigurer
    implements WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final WebApplicationProperties properties;

  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
    mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
    // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
    mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
    factory.setMimeMappings(mappings);
  }

  @Bean
  CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = properties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      log.debug("Registering CORS filter");
      source.registerCorsConfiguration(RestConstants.ROOT + "/**", config);
      source.registerCorsConfiguration(RestConstants.MANAGEMENT_PREFIX + "/**", config);
    }
    return new CorsFilter(source);
  }
}
