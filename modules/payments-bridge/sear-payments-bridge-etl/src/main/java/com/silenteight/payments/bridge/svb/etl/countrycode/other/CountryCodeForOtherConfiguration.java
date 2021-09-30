package com.silenteight.payments.bridge.svb.etl.countrycode.other;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CountryCodeForOtherConfiguration {

  @Bean
  CountryCodeForOther countryCodeForOther() {
    return new CountryCodeForOtherImpl();
  }
}
