package com.silenteight.warehouse.common.domain.country;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class CountryPermissionService {

  @NonNull
  private final CountryRepository countryRepository;

  public Set<String> getCountries(Set<String> countryGroups) {
    Set<String> resolvedCountries = countryGroups.stream().map(this::getCountries)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    log.trace("Countries resolved, userGroups={}, resolvedCountries={}",
        countryGroups, resolvedCountries);

    return resolvedCountries;
  }

  private List<String> getCountries(String countryGroup) {
    try {
      return getCountriesForGroup(UUID.fromString(countryGroup));
    } catch (IllegalArgumentException argumentException) {
      return Collections.emptyList();
    }
  }

  public List<String> getCountriesForGroup(UUID name) {
    List<String> countries = countryRepository.getCountryEntitiesByCountryGroupId(name)
        .stream()
        .map(CountryEntity::getCountry)
        .collect(toList());

    if (countries.isEmpty()) {
      log.warn("No countries resolved for the provided group, groupName={}", name);
    }

    return countries;
  }

  public void setCountries(UUID groupName, Collection<String> countries) {
    countryRepository.deleteByCountryGroupId(groupName);
    List<CountryEntity> countryEntities = countries.stream()
        .map(country -> createCountryEntity(groupName, country))
        .collect(toList());
    countryRepository.saveAll(countryEntities);
  }

  private CountryEntity createCountryEntity(UUID groupName, String country) {
    return CountryEntity.builder()
        .country(country)
        .countryGroupId(groupName)
        .build();
  }
}
