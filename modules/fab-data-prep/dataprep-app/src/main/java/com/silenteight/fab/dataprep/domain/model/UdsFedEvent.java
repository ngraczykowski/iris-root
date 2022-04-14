package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class UdsFedEvent {

  String batchName;
  String alertName;
  AlertErrorDescription errorDescription;
  Status feedingStatus;
  @Default
  List<FedMatch> fedMatches = emptyList();

  @Value
  public static class FedMatch {

    String matchName;
  }

  public enum Status {
    SUCCESS, FAILURE
  }
}
