package com.silenteight.warehouse.management.group.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface CountryGroupRepository extends Repository<CountryGroupEntity, Long> {

  Collection<CountryGroupEntity> findAll();

  Optional<CountryGroupEntity> findByCountryGroupId(UUID id);

  void save(CountryGroupEntity entity);
}
