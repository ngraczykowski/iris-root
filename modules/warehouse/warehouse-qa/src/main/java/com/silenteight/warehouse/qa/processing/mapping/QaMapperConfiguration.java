package com.silenteight.warehouse.qa.processing.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QaMapperConfiguration {

  @Bean
  QaAlertMapper qaAlertMapper() {
    return new QaAlertMapper(new ObjectMapper());
  }
}
