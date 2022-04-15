package com.silenteight.serp.governance.common.web.config;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class WebConfigurer
    implements WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
    mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
    // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
    mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=utf-8");
    factory.setMimeMappings(mappings);
  }
}
