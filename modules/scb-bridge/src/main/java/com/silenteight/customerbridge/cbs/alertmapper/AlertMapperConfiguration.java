package com.silenteight.customerbridge.cbs.alertmapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.batch.DateConverter;
import com.silenteight.customerbridge.common.hitdetails.HitDetailsParser;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertMapperProperties.class)
class AlertMapperConfiguration {

  private final AlertMapperProperties properties;

  @Bean
  AlertMapper alertMapper() {
    var dateConverter = new DateConverter(properties.getTimeZone());
    var matchCollector = new MatchCollector();
    var suspectsCollector = new SuspectsCollector(new HitDetailsParser());

    return new AlertMapper(dateConverter, matchCollector, suspectsCollector);
  }
}
