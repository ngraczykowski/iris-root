package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.WarehouseApi;
import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata;
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert;
import com.silenteight.hsbc.bridge.report.Alert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IngestRecommendationsUseCase {

  private final WarehouseApi warehouseApi;

  void ingest(@NonNull List<SolvedAlert> solvedAlerts) {
    var recommendedAlerts = prepareAlerts(solvedAlerts);

    warehouseApi.send(recommendedAlerts);
  }

  private Collection<Alert> prepareAlerts(List<SolvedAlert> solvedAlerts) {
    return solvedAlerts.stream()
        .map(this::mapToAlert)
        .collect(toList());
  }

  private Alert mapToAlert(SolvedAlert solvedAlert) {
    return new Alert() {
      @Override
      public String getDiscriminator() {
        var trackingId = solvedAlert.getAlertMetadata().stream()
            .filter(alert -> "trackingId".equals(alert.getKey()))
            .map(AlertMetadata::getValue)
            .findFirst()
            .orElse("");

        return solvedAlert.getId() + getDiscriminatorSeparator() + trackingId;
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
}
