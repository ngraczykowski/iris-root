package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

import com.google.protobuf.Timestamp;

import java.util.List;

@Builder
@Value
public class AddAlertsToAnalysisOut {

  List<AddedAlert> addedAlerts;

  @Builder
  @Value
  public static class AddedAlert {

    String name;
    Timestamp createdAt;
  }
}
