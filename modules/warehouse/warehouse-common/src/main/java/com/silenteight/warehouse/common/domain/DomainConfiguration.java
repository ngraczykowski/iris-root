package com.silenteight.warehouse.common.domain;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.domain.country.CountryRepository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
public class DomainConfiguration {

  @Bean
  CountryPermissionService countryPermissionService(CountryRepository countryRepository) {
    return new CountryPermissionService(countryRepository);
  }
}
