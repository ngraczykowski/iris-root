package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Builder
@Value
public class WarehouseEvent {

  String requestId;
  List<Alert> alerts;

  @Builder
  @Value
  public static class Alert {

    String alertName;
    String discriminator;
    String accessPermissionTag;
    @Builder.Default
    List<Match> matches = emptyList();
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
