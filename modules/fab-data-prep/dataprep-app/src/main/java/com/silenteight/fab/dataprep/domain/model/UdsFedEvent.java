package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class UdsFedEvent {

  String batchId;
  String alertId;
  AlertErrorDescription errorDescription;
  Status feedingStatus;
  List<FedMatch> fedMatches;

  @Value
  public static class FedMatch {

    String matchId;
  }

  public enum Status {
    SUCCESS, FAILURE
  }
}
