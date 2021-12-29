package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Timestamp;

import java.util.List;

@Builder
@Value
public class AddAlertsToAnalysisIn {

  String analysisName;
  List<Alert> alerts;

  @Builder
  @Value
  public static class Alert {

    String name;
    Timestamp deadlineTime;
  }
}
