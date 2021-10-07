package com.silenteight.warehouse.indexer.production.indextracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.indexer.alert.WriteIndexResolver;

import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.NotThreadSafe;

import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@NotThreadSafe
@RequiredArgsConstructor
class IndexByDiscriminatorResolver implements WriteIndexResolver {

  @NonNull
  private final ProductionAlertTrackingService productionAlertTrackingService;

  Map<String, String> indexNameByDiscriminator = emptyMap();

  @Override
  public void prepareIndexNames(List<Alert> alerts) {
    List<String> discriminators = alerts.stream()
        .map(Alert::getDiscriminator)
        .collect(toList());

    indexNameByDiscriminator =
        productionAlertTrackingService.getIndexNameByDiscriminator(discriminators);
  }

  @Override
  public String getIndexName(Alert alert) {
    String discriminator = alert.getDiscriminator();
    return ofNullable(indexNameByDiscriminator.get(discriminator))
        .orElseThrow(() -> new IllegalStateException(
            "Cannot determine index name for discriminator=" + discriminator));
  }
}
