package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("core_bridge_data_retention_job_alerts")
record DataRetentionJobAlertEntity(
    @Id long id,
    long jobId,
    String alertName) {

  @Builder
  public DataRetentionJobAlertEntity {}
}
