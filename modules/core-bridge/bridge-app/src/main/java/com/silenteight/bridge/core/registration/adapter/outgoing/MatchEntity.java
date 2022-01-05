package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.Builder;

import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("matches")
record MatchEntity(String name,
                   String matchId,
                   Status status,
                   Instant createdAt,
                   Instant updatedAt) {

  @Builder
  MatchEntity {}

  public enum Status {
    REGISTERED, PROCESSING, ERROR
  }
}
