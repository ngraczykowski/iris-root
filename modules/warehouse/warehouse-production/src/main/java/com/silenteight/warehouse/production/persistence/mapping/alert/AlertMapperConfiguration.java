package com.silenteight.warehouse.production.persistence.mapping.alert;

import com.silenteight.warehouse.production.persistence.common.PayloadConverter;
import com.silenteight.warehouse.production.persistence.mapping.match.MatchMapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AlertProperties.class)
class AlertMapperConfiguration {

  @Bean
  AlertMapper persistenceAlertMapper(
      PayloadConverter payloadConverter,
      MatchMapper matchMapper,
      @Valid AlertProperties alertProperties) {

    return new AlertMapper(
        payloadConverter,
        matchMapper,
        alertProperties.getLabels(),
        alertProperties.getRecommendationDateField());
  }
}
