/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

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
