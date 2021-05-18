package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert;
import com.silenteight.hsbc.bridge.report.AlertWithRecommendationSender;
import com.silenteight.hsbc.bridge.report.AlertWithRecommendationSender.AlertWithRecommendation;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IngestRecommendationsUseCase {

  private final AlertWithRecommendationSender sender;

  void ingest(@NonNull List<SolvedAlert> alerts) {
    sender.sendAlertsWithRecommendations(alerts.stream()
        .map(this::toAlertWithRecommendation)
        .collect(toList())
    );
  }

  private AlertWithRecommendation toAlertWithRecommendation(SolvedAlert alert) {
    return new AlertWithRecommendation() {
      @Override
      public String getAlert() {
        return alert.getId();
      }

      @Override
      public String getRecommendation() {
        return alert.getRecommendation().name();
      }
    };
  }
}
