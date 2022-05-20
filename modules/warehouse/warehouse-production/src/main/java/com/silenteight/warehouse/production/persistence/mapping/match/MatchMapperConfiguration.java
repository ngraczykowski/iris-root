package com.silenteight.warehouse.production.persistence.mapping.match;

import com.silenteight.warehouse.production.persistence.common.PayloadConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MatchMapperConfiguration {

  @Bean
  MatchMapper persistenceMatchMapper(PayloadConverter payloadConverter) {
    return new MatchMapper(payloadConverter);
  }
}
