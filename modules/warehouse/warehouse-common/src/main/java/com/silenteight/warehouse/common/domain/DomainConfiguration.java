package com.silenteight.warehouse.common.domain;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.domain.country.CountryRepository;
import com.silenteight.warehouse.common.properties.AnalystDecisionProperties;
import com.silenteight.warehouse.common.properties.RecommendationProperties;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableConfigurationProperties({ RecommendationProperties.class, AnalystDecisionProperties.class })
public class DomainConfiguration {

  @Bean
  CountryPermissionService countryPermissionService(CountryRepository countryRepository) {
    return new CountryPermissionService(countryRepository);
  }
}
