package com.silenteight.warehouse.common.domain.group;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CountryGroupRepository extends Repository<CountryGroupEntity, Long> {

  Collection<CountryGroupEntity> findAll();

  Optional<CountryGroupEntity> findByCountryGroupId(UUID countryGroupId);

  void save(CountryGroupEntity entity);

  Iterable<CountryGroupEntity> saveAll(Iterable<CountryGroupEntity> countryGroupEntities);

  boolean existsByCountryGroupId(UUID countryGroupIds);

  void deleteByCountryGroupId(UUID countryGroupId);

  Collection<CountryGroupEntity> findAllByMigratedIsFalse();
}
