package com.silenteight.hsbc.bridge.retention;

import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;

interface DryRunJobRepository extends CrudRepository<DryRunJobEntity, Long> {

  default long save(OffsetDateTime expirationDate) {
    var entity = DryRunJobEntity.builder()
        .alertsExpirationDate(expirationDate)
        .build();
    save(entity);
    return entity.getId();
  }
}
