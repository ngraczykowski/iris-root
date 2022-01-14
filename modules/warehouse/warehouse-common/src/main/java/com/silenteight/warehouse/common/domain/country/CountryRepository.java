package com.silenteight.warehouse.common.domain.country;

import org.springframework.data.repository.Repository;


public interface CountryRepository extends Repository<CountryEntity, Long> {

  Iterable<CountryEntity> saveAll(Iterable<CountryEntity> countryEntities);
}
