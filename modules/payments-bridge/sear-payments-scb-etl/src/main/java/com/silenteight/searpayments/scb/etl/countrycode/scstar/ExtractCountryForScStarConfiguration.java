package com.silenteight.searpayments.scb.etl.countrycode.scstar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ExtractCountryForScStarConfiguration {

  @Bean
  ExtractCountryFromScstarBic extractCountryForScStar() {
    return ExtractCountryFromScstarBic.defaultExtract();
  }
}
