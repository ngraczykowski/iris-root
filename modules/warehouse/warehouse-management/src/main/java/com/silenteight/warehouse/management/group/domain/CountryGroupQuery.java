package com.silenteight.warehouse.management.group.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;
import com.silenteight.warehouse.management.group.get.GetCountryGroupQuery;
import com.silenteight.warehouse.management.group.list.ListCountryGroupQuery;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class CountryGroupQuery implements ListCountryGroupQuery, GetCountryGroupQuery {

  @NonNull
  private final CountryGroupRepository countryGroupRepository;

  public List<CountryGroupDto> listAll() {
    return countryGroupRepository.findAll()
        .stream()
        .map(CountryGroupQuery::toDto)
        .collect(Collectors.toList());
  }

  public CountryGroupDto get(UUID id) {
    return countryGroupRepository
        .findByCountryGroupId(id)
        .map(CountryGroupQuery::toDto)
        .orElseThrow(() -> new CountryGroupDoesNotExistException(id));
  }

  private static CountryGroupDto toDto(CountryGroupEntity entity) {
    return CountryGroupDto.builder()
        .id(entity.getCountryGroupId())
        .name(entity.getName()).build();
  }
}
