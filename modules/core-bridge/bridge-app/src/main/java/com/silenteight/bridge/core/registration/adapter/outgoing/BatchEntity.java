package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.Builder;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("batches")
record BatchEntity(@Id long id,
                   Status status,
                   String batchId,
                   Long alertsCount,
                   String analysisName,
                   String errorDescription,
                   String batchMetadata,
                   @CreatedDate Instant createdAt,
                   @LastModifiedDate Instant updatedAt) {

  @Builder
  BatchEntity {}

  enum Status {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
