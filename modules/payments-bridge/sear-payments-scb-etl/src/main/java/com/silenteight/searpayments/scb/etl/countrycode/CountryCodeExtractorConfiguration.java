package com.silenteight.searpayments.scb.etl.countrycode;

import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.countrycode.mts.CountryCodeForUnitMap;
import com.silenteight.searpayments.scb.etl.countrycode.other.CountryCodeForOther;
import com.silenteight.searpayments.scb.etl.countrycode.scstar.ExtractCountryFromScstarBic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CountryCodeExtractorConfiguration {

  @Bean
  CountryCodeExtractor countryCodeExtractor(
      CountryCodeForUnitMap extractCountryCodeForMts,
      ExtractCountryFromScstarBic extractCountryForScStar,
      CountryCodeForOther countryCodeForOther
  ) {
    return new CountryCodeExtractor(
        extractCountryCodeForMts, extractCountryForScStar, countryCodeForOther);
  }
}
