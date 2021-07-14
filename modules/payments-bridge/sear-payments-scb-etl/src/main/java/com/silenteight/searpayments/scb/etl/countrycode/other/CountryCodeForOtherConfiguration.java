package com.silenteight.searpayments.scb.etl.countrycode.other;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CountryCodeForOtherConfiguration {

  @Bean
  CountryCodeForOther countryCodeForOther() {
    return new CountryCodeForOtherImpl();
  }
}
