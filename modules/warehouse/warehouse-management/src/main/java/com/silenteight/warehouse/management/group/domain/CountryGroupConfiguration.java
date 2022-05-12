package com.silenteight.warehouse.management.group.domain;

import com.silenteight.warehouse.common.domain.DomainConfiguration;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(DomainConfiguration.class)
@EnableTransactionManagement
class CountryGroupConfiguration {

  @Bean
  CountryGroupService countryGroupService(CountryGroupRepository countryGroupRepository) {
    return new CountryGroupService(countryGroupRepository);
  }

  @Bean
  CountryGroupQuery countryGroupQuery(CountryGroupRepository countryGroupRepository) {
    return new CountryGroupQuery(countryGroupRepository);
  }
}
