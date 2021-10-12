package com.silenteight.warehouse.indexer.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.indexer.production.indextracking.AlertWithIndex;
import com.silenteight.warehouse.indexer.production.indextracking.ProductionAlertTrackingService;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ProductionAlertIndexResolvingService {

  @NonNull
  private final ProductionAlertTrackingService productionAlertTrackingService;

  public List<AlertWithIndex> getIndex(List<Alert> alerts) {
    List<String> discriminators = alerts.stream()
        .map(Alert::getDiscriminator)
        .collect(toList());

    Map<String, String> indexNameByDiscriminator =
        productionAlertTrackingService.getIndexNameByDiscriminator(discriminators);

    return alerts.stream()
        .map(alert -> new AlertWithIndex(
            alert,
            indexNameByDiscriminator.get(alert.getDiscriminator())))
        .collect(toList());
  }
}
