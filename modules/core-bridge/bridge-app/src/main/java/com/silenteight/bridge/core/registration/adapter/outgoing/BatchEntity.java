package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("batches")
record BatchEntity(@Id long id, String batchId, String analysisName, Long alertsCount,
                   Status status) {

  @Builder
  BatchEntity {}

  enum Status {
    STORED, ERROR, PROCESSING, COMPLETED, DELIVERED
  }
}
