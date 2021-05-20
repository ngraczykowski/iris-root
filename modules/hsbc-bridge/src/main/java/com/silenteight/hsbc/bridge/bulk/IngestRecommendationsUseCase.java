package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert;
import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.WarehouseFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class IngestRecommendationsUseCase {

  private final WarehouseFacade warehouseFacade;

  void ingest(@NonNull List<SolvedAlert> solvedAlerts) {
    var alerts = prepareAlerts(solvedAlerts);

    warehouseFacade.sendAlerts(alerts);
  }

  private Collection<Alert> prepareAlerts(List<SolvedAlert> solvedAlerts) {
    return solvedAlerts.stream().map(s -> new Alert() {
      @Override
      public String getName() {
        return "";// TODO mmrowka s.getMetadata().getTrackingId();
      }

      @Override
      public Map<String, String> getMetadata() {
        return new HashMap<>();
      }

      @Override
      public Collection<Match> getMatches() {
        return List.of();
      }
    }).collect(toList());
  }
}
