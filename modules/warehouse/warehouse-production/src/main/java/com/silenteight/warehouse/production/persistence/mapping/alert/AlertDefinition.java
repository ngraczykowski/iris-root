package com.silenteight.warehouse.production.persistence.mapping.alert;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.warehouse.production.persistence.mapping.match.MatchDefinition;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.util.List.of;

@Data
@Builder
public class AlertDefinition {

  @NonNull
  private String discriminator;
  @Nullable
  private String name;
  @NonNull
  private String payload;
  @Nullable
  private OffsetDateTime recommendationDate;
  @NonNull
  private Map<String, String> labels;
  @NonNull
  @Default
  private List<MatchDefinition> matchDefinitions = of();

}
