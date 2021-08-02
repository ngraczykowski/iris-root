package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.WarehouseApi;
import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata;
import com.silenteight.hsbc.bridge.bulk.rest.AlertRecommendation;
import com.silenteight.hsbc.bridge.bulk.rest.BatchSolvedAlerts;
import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class IngestRecommendationsUseCase {

  private final WarehouseApi warehouseApi;

  void ingest(@NonNull BatchSolvedAlerts recommendations) {
    var alerts = mapAlerts(recommendations.getAlerts())
        .collect(toList());

    warehouseApi.send(alerts);
  }

  private Stream<Alert> mapAlerts(
      List<AlertRecommendation> alerts) {
    return alerts.stream().map(this::mapToAlert);
  }

  private Alert mapToAlert(AlertRecommendation alert) {
    return new Alert() {

      final List<AlertMetadata> alertMetadata = alert.getAlertMetadata();

      @Override
      public String getName() {
        return IngestRecommendationsUseCase.getFromMetadata(alertMetadata, "name");
      }

      @Override
      public String getDiscriminator() {
        return IngestRecommendationsUseCase.getFromMetadata(alertMetadata, "discriminator");
      }

      @Override
      public Map<String, String> getMetadata() {
        var map = new HashMap<String, String>();
        map.put("id", alert.getId());
        map.put("errorMessage", alert.getErrorMessage());
        map.put("recommendation", alert.getRecommendation());
        map.put("comment", alert.getComment());
        map.put("fvSignature", alert.getFvSignature());
        map.put("policyId", alert.getPolicyId());
        map.put("stepId", alert.getStepId());
        map.put("status", alert.getStatus().getValue());
        map.putAll(alert.getAlertMetadata()
            .stream()
            .collect(toMap(AlertMetadata::getKey, AlertMetadata::getValue)));
        return map;
      }

      @Override
      public Collection<Match> getMatches() {
        return List.of();
      }
    };
  }

  private static String getFromMetadata(List<AlertMetadata> alertMetadata, String key) {
    return alertMetadata.stream()
        .filter(alert -> key.equals(alert.getKey()))
        .map(AlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }
}
