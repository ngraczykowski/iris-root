package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = {
    GnsRtResponseMapperConfigurationProperties.class,
})
@Configuration
class GnsRtMapperConfiguration {

  @Bean
  GnsRtRequestToAlertMapper gnsRtRequestToAlertMapper() {
    return new GnsRtRequestToAlertMapper(new HitDetailsParser(), new GenderDetector());
  }

  @Bean
  GnsRtResponseMapper gnsRtResponseMapper(
      GnsRtResponseMapperConfigurationProperties gnsRtResponseMapperConfigurationProperties) {
    return new GnsRtResponseMapper(gnsRtResponseMapperConfigurationProperties);
  }
}
