package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.WarehouseApi;
import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata;
import com.silenteight.hsbc.bridge.bulk.rest.BatchSolvedAlerts;
import com.silenteight.hsbc.bridge.bulk.rest.ErrorAlert;
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert;
import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class IngestRecommendationsUseCase {

  private final WarehouseApi warehouseApi;

  void ingest(@NonNull BatchSolvedAlerts recommendations) {
    var alerts = Stream.concat(
        mapErrorAlerts(recommendations.getErrorAlerts()),
        mapRecommendedAlerts(recommendations.getAlerts())
    ).collect(Collectors.toList());

    warehouseApi.send(alerts);
  }

  private Stream<Alert> mapErrorAlerts(List<ErrorAlert> errorAlerts) {
    return errorAlerts.stream().map(this::mapToAlert);
  }

  private Stream<Alert> mapRecommendedAlerts(List<SolvedAlert> solvedAlerts) {
    return solvedAlerts.stream().map(this::mapToAlert);
  }

  private Alert mapToAlert(SolvedAlert solvedAlert) {
    return new Alert() {
      @Override
      public String getDiscriminator() {
        return IngestRecommendationsUseCase.getDiscriminator(solvedAlert.getAlertMetadata());
      }

      @Override
      public Map<String, String> getMetadata() {
        var map = new HashMap<String, String>();
        map.put("id", solvedAlert.getId());
        map.put("recommendation", solvedAlert.getRecommendation());
        map.put("comment", solvedAlert.getComment());
        map.put("fvSignature", solvedAlert.getFvSignature());
        map.put("policyId", solvedAlert.getPolicyId());
        map.put("stepId", solvedAlert.getStepId());
        map.put("status", "OK");
        map.putAll(solvedAlert.getAlertMetadata()
            .stream()
            .collect(Collectors.toMap(AlertMetadata::getKey, AlertMetadata::getValue)));
        return map;
      }

      @Override
      public Collection<Match> getMatches() {
        return List.of();
      }
    };
  }

  private Alert mapToAlert(ErrorAlert errorAlert) {
    return new Alert() {

      @Override
      public String getDiscriminator() {
        return IngestRecommendationsUseCase.getDiscriminator(errorAlert.getAlertMetadata());
      }

      @Override
      public Map<String, String> getMetadata() {
        var map = new HashMap<String, String>();
        map.put("id", errorAlert.getId());
        map.put("errorMessage", errorAlert.getErrorMessage());
        map.put("status", "ERROR");
        map.putAll(errorAlert.getAlertMetadata()
            .stream()
            .collect(Collectors.toMap(AlertMetadata::getKey, AlertMetadata::getValue)));
        return map;
      }

      @Override
      public Collection<Match> getMatches() {
        return List.of();
      }
    };
  }

  private static String getDiscriminator(List<AlertMetadata> alertMetadata) {
    return alertMetadata.stream()
        .filter(alert -> "discriminator".equals(alert.getKey()))
        .map(AlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }
}
