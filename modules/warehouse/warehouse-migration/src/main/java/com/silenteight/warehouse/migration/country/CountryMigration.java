package com.silenteight.warehouse.migration.country;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.domain.country.CountryEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
class CountryMigration {

  @NonNull
  private final CountryMigrationService migrationService;

  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void migration() {
    Collection<CountryGroupEntity> toMigrate = migrationService.findMigrationCandidates();

    if (isNotEmpty(toMigrate)) {
      log.debug(
          "Starting warehouse_country_group migration, amount of country groups to migrate: {}",
          toMigrate.size());

      process(toMigrate);
      log.debug("Migrated {} country groups.", toMigrate.size());
    }
  }

  private void process(Collection<CountryGroupEntity> countryGroups) {
    List<CountryEntity> countriesToMigrate = migrationService.getCountriesToMigrate(countryGroups);
    migrationService.saveCountries(countriesToMigrate);
    migrationService.markCountryGroupsAsMigrated(countryGroups);
  }
}
