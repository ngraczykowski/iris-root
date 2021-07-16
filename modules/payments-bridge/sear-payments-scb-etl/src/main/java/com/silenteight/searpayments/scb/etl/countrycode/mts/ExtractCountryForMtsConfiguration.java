package com.silenteight.searpayments.scb.etl.countrycode.mts;

import com.silenteight.searpayments.scb.etl.countrycode.mts.CountryCodeForUnitMapImpl.UnitCountryCodeTuple;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
class ExtractCountryForMtsConfiguration {

  @Bean
  CountryCodeForUnitMap extractCountryCodeForMts() {
    return new CountryCodeForUnitMapImpl(asList(
        tuple("MTS-JPT", "JP"),
        tuple("MTS-EFF", "DE"),
        tuple("MTS-SCB", "US"),
        tuple("MTS-EUF", "UK")
    ));
  }

  private static CountryCodeForUnitMapImpl.UnitCountryCodeTuple tuple(
      String unit, String countryCode) {
    return new UnitCountryCodeTuple(unit, countryCode);
  }
}
