package com.silenteight.searpayments.scb.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class HitConfiguration {

  @Bean
  CreateHitFactory createHitFactory() {
    return new CreateHitFactory(
        createNameAddressCrossmatchAgentRequestFactory(), createHitAddressFactory(),
        createCompareFreeTextRequestFactory(), createCompareNamesRequestFactory(),
        createCompareLocationsRequestFactory(), createOneLinerAgentRequestFactory(),
        createDelimiterInNameLineAgentRequestFactory(), createMatchFirstTokenOfAddrReqFactory());
  }

  @Bean
  CreateNameAddressCrossmatchAgentRequestFactory createNameAddressCrossmatchAgentRequestFactory() {
    return new CreateNameAddressCrossmatchAgentRequestFactory(createAlertPartyEntitiesFactory());
  }

  @Bean
  CreateAlertPartyEntitiesFactory createAlertPartyEntitiesFactory() {
    return new CreateAlertPartyEntitiesFactory();
  }

  @Bean
  CreateHitAddressFactory createHitAddressFactory() {
    return new CreateHitAddressFactory();
  }

  @Bean
  CreateCompareFreeTextRequestFactory createCompareFreeTextRequestFactory() {
    return new CreateCompareFreeTextRequestFactory();
  }

  @Bean
  CreateCompareNamesRequestFactory createCompareNamesRequestFactory() {
    return new CreateCompareNamesRequestFactory();
  }

  @Bean
  CreateCompareLocationsRequestFactory createCompareLocationsRequestFactory() {
    return new CreateCompareLocationsRequestFactory();
  }

  @Bean
  CreateOneLinerAgentRequestFactory createOneLinerAgentRequestFactory() {
    return new CreateOneLinerAgentRequestFactory();
  }

  @Bean
  CreateDelimiterInNameLineAgentRequestFactory createDelimiterInNameLineAgentRequestFactory() {
    return new CreateDelimiterInNameLineAgentRequestFactory();
  }

  @Bean
  CreateMatchtextFirstTokenOfAddressAgentRequestFactory createMatchFirstTokenOfAddrReqFactory() {
    return new CreateMatchtextFirstTokenOfAddressAgentRequestFactory();
  }
}
