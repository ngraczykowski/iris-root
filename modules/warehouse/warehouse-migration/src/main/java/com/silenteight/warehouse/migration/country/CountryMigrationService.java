package com.silenteight.warehouse.migration.country;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.country.CountryEntity;
import com.silenteight.warehouse.common.domain.country.CountryRepository;
import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;
import com.silenteight.warehouse.common.opendistro.roles.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class CountryMigrationService {

  @NonNull
  private final CountryRepository countryRepository;
  @NonNull
  private final CountryGroupRepository countryGroupRepository;
  @NonNull
  private final RoleService roleService;

  Collection<CountryGroupEntity> findMigrationCandidates() {
    return countryGroupRepository.findAllByMigratedIsFalse();
  }

  List<CountryEntity> getCountriesToMigrate(Collection<CountryGroupEntity> countryGroups) {
    return countryGroups.stream()
        .map(CountryGroupEntity::getCountryGroupId)
        .map(cg -> toCountryEntities(cg, roleService.getCountries(cg)))
        .flatMap(Collection::stream)
        .collect(toList());
  }

  void saveCountries(List<CountryEntity> countriesToMigrate) {
    countryRepository.saveAll(countriesToMigrate);
  }

  void markCountryGroupsAsMigrated(Collection<CountryGroupEntity> countryGroups) {
    List<CountryGroupEntity> markedMigrated = countryGroups.stream()
        .peek(CountryGroupEntity::markMigrated)
        .collect(toList());

    countryGroupRepository.saveAll(markedMigrated);
  }

  private static List<CountryEntity> toCountryEntities(UUID countryGroup, List<String> countries) {
    return countries
        .stream()
        .map(country -> toCountryEntity(countryGroup, country))
        .collect(toList());
  }

  private static CountryEntity toCountryEntity(UUID countryGroup, String country) {
    return CountryEntity.builder()
        .countryGroupId(countryGroup)
        .country(country)
        .build();
  }
}
