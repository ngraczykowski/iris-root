package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.Alert;

@Value
@Builder
public class AlertOut {

  String id;
  String metadata;
  AlertStatus status;
  String errorMessage;
  String name;

  public enum AlertStatus {
    UNKNOWN, FAILURE, SUCCESS
  }

  static AlertOut createFrom(Alert alert) {
    return AlertOut.builder()
        .id(alert.getId())
        .metadata(alert.getMetadata())
        .status(AlertStatus.valueOf(alert.getStatus().name()))
        .errorMessage(alert.getErrorMessage())
        .name(alert.getName())
        .build();
  }
}
