package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FeedUdsCommand {

  String batchId;
  Alert alert;
  List<Match> matches;

  @Value
  @Builder
  public static class Alert {

    String id;
    String name;
    AlertStatus status;
  }

  @Value
  @Builder
  public static class Match {

    String id;
    String name;
  }
}
