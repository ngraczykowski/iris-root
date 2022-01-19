package com.silenteight.warehouse.common.domain.country;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class CountryPermissionService {

  @NonNull
  private final CountryRepository countryRepository;

  public Set<String> getCountries(Set<String> countryGroups) {
    return countryGroups.stream().map(this::getCountries)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  private List<String> getCountries(String countryGroup) {
    try {
      return getCountriesForGroup(UUID.fromString(countryGroup));
    } catch (IllegalArgumentException argumentException) {
      return Collections.emptyList();
    }
  }

  private List<String> getCountriesForGroup(UUID name) {
    return countryRepository.getCountryEntitiesByCountryGroupId(name)
        .stream()
        .map(CountryEntity::getCountry)
        .collect(Collectors.toList());
  }
}
