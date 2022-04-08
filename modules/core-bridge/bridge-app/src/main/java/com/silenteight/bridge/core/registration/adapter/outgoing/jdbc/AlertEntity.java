package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import lombok.Builder;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

@Table("core_bridge_alerts")
record AlertEntity(@Id long id,
                   String name,
                   Status status,
                   String alertId,
                   String batchId,
                   String metadata,
                   String errorDescription,
                   @CreatedDate Instant createdAt,
                   @LastModifiedDate Instant updatedAt,
                   @MappedCollection(idColumn = "alert_id") Set<MatchEntity> matches) {

  @Builder
  AlertEntity {}

  enum Status {
    REGISTERED, PROCESSING, RECOMMENDED, ERROR, DELIVERED
  }
}
