package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.match.Match;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

@Value
class ProcessingResult {

  List<ProcessedAlert> processedAlerts;

  @Builder
  @Value
  static class ProcessedAlert {

    String externalId;
    List<Match> matches;
    @Nullable
    String errorMessage;

    Optional<String> getErrorMessage() {
      return Optional.ofNullable(errorMessage);
    }
  }
}

