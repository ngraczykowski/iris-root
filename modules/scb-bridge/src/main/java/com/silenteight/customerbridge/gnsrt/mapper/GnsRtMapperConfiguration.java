package com.silenteight.customerbridge.gnsrt.mapper;

import com.silenteight.customerbridge.common.gender.GenderDetector;
import com.silenteight.customerbridge.common.hitdetails.HitDetailsParser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GnsRtMapperConfiguration {

  @Bean
  GnsRtRequestToAlertMapper gnsRtRequestToAlertMapper() {
    return new GnsRtRequestToAlertMapper(new HitDetailsParser(), new GenderDetector());
  }

  @Bean
  GnsRtResponseMapper gnsRtResponseMapper() {
    return new GnsRtResponseMapper();
  }
}
