package com.silenteight.warehouse.management.country;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CountriesConfiguration {

  @Bean
  CountryService countryService() {
    return new CountryService();
  }
}
