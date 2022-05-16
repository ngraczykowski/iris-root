package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobRepository;

import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
class JdbcDataRetentionJobRepository implements DataRetentionJobRepository {

  private final CrudDataRetentionJobRepository repository;

  @Override
  public long save(Instant alertsExpirationDate, DataRetentionType type) {
    var entity = DataRetentionJobEntity.builder()
        .alertsExpirationDate(alertsExpirationDate)
        .type(type.name())
        .build();
    return repository.save(entity).id();
  }
}
