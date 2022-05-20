package com.silenteight.warehouse.common.domain.country;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;

public interface CountryRepository extends Repository<CountryEntity, Long> {

  Iterable<CountryEntity> saveAll(Iterable<CountryEntity> countryEntities);

  List<CountryEntity> getCountryEntitiesByCountryGroupId(UUID countryGroup);

  @Transactional
  void deleteByCountryGroupId(UUID countryGroup);

}
