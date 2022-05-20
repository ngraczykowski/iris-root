package com.silenteight.warehouse.production.persistence.mapping.match;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class MatchDefinition {

  @NonNull
  private String matchId;
  @NonNull
  private String name;
  @NonNull
  private String payload;
}
