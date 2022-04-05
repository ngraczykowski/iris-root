package com.silenteight.connector.ftcc.request.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@Configuration
@Import(RequestDomainConfiguration.class)
class RequestDomainTestConfiguration {

  @Bean
  ObjectMapper objectMapper() {
    return INSTANCE.objectMapper();
  }
}
