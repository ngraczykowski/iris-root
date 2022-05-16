package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.Builder;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("core_bridge_data_retention_job")
record DataRetentionJobEntity(
    @Id long id,
    @CreatedDate Instant createdAt,
    Instant alertsExpirationDate,
    String type) {

  @Builder
  public DataRetentionJobEntity {}
}
