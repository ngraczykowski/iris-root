package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Builder
@Value
public class WarehouseEvent {

  @NonNull
  String requestId;
  @NonNull
  List<Alert> alerts;

  @Builder
  @Value
  public static class Alert {
    @NonNull
    String alertName;
    @NonNull
    String discriminator;
    @NonNull
    String accessPermissionTag;
    @Builder.Default
    List<Match> matches = emptyList();
    @NonNull
    Map<String, String> payload;
  }

  @Builder
  @Value
  public static class Match {

    String matchName;
    String discriminator;
    Map<String, String> payload;
  }
}
