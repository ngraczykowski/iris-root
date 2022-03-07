package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.Builder;

import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("core_bridge_matches")
record MatchEntity(
    String name,
    String matchId,
    Instant createdAt,
    Instant updatedAt
) {

  @Builder
  MatchEntity {}
}
