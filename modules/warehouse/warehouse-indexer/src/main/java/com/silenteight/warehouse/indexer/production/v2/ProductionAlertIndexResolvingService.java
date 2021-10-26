package com.silenteight.warehouse.indexer.production.v2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionMatchTrackingService;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ProductionAlertIndexResolvingService {

  @NonNull
  private final ProductionAlertTrackingService productionAlertTrackingService;

  @NonNull
  private final ProductionMatchTrackingService productionMatchTrackingService;

  public List<AlertWithIndex> getTargetIndices(List<Alert> alerts) {
    List<String> alertDiscriminators = alerts.stream()
        .map(Alert::getDiscriminator)
        .collect(toList());

    Map<String, String> alertIndexNameByDiscriminator =
        productionAlertTrackingService.getIndexNameByDiscriminator(alertDiscriminators);

    return alerts.stream()
        .map(alert -> getAlertWithIndex(
            alert,
            alertIndexNameByDiscriminator.get(alert.getDiscriminator())))
        .collect(toList());
  }

  private AlertWithIndex getAlertWithIndex(Alert alert, String alertIndexName) {
    List<String> matchDiscriminators = alert.getMatchesList().stream()
        .map(Match::getDiscriminator)
        .collect(toList());

    Map<String, String> matchIndexNameByDiscriminator =
        productionMatchTrackingService.getIndexNameByDiscriminator(
            alert.getDiscriminator(), matchDiscriminators);

    List<MatchWithIndex> matches = alert.getMatchesList().stream()
        .map(match -> getMatchWithIndex(match,
            matchIndexNameByDiscriminator.get(match.getDiscriminator())))
        .collect(toList());

    return new AlertWithIndex(alert, alertIndexName, matches);
  }

  private MatchWithIndex getMatchWithIndex(Match match, String matchIndexName) {
    return new MatchWithIndex(match, matchIndexName);
  }
}
