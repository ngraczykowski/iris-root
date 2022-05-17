package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.port.outgoing.DataRetentionJobAlertRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcDataRetentionJobAlertRepository implements DataRetentionJobAlertRepository {

  private final CrudDataRetentionJobAlertRepository repository;

  @Override
  public void saveAll(long jobId, List<Long> alertPrimaryIds) {
    var entities = alertPrimaryIds.stream()
        .map(alertPrimaryId -> DataRetentionJobAlertEntity.builder()
            .jobId(jobId)
            .alertId(alertPrimaryId)
            .build())
        .toList();
    repository.saveAll(entities);
  }
}
