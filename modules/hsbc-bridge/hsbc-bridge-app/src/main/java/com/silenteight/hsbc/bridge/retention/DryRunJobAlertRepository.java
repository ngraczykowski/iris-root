package com.silenteight.hsbc.bridge.retention;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.stream.Collectors;

interface DryRunJobAlertRepository extends CrudRepository<DryRunJobAlertEntity, Long> {

  default void saveAll(long jobId, Set<String> alertNames) {
    var entities = alertNames.stream().map(alertName -> DryRunJobAlertEntity.builder()
            .jobId(jobId)
            .alertName(alertName)
            .build())
        .collect(Collectors.toList());
    saveAll(entities);
  }
}
