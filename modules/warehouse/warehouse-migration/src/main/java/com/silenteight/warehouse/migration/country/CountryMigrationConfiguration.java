package com.silenteight.warehouse.migration.country;

import com.silenteight.warehouse.common.domain.country.CountryRepository;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;
import com.silenteight.warehouse.common.opendistro.roles.RoleService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "warehouse.country.migration.enabled", havingValue = "true")
class CountryMigrationConfiguration {

  @Bean
  CountryMigration countryMigration(CountryMigrationService migrationService) {
    return new CountryMigration(migrationService);
  }

  @Bean
  CountryMigrationService countryMigrationService(
      CountryRepository countryRepository,
      CountryGroupRepository countryGroupRepository,
      RoleService roleService) {

    return new CountryMigrationService(countryRepository, countryGroupRepository, roleService);
  }
}
