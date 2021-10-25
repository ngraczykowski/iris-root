package com.silenteight.warehouse.indexer.production.v1;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.AlertDefinition;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ProductionAlertMappingService {

  @NonNull
  private final AlertMapper alertMapper;

  List<MapWithIndex> mapFields(List<AlertWithIndex> alerts) {
    return alerts.stream()
        .map(this::toMapWithIndex)
        .collect(toList());
  }

  private MapWithIndex toMapWithIndex(AlertWithIndex alert) {
    Map<String, Object> payload =
        alertMapper.convertAlertToAttributes(toAlertDefinition(alert.getAlert()));
    String discriminator = alert.getAlert().getDiscriminator();
    String indexName = alert.getIndexName();

    return new MapWithIndex(indexName, discriminator, payload);
  }

  private static AlertDefinition toAlertDefinition(Alert alert) {
    return AlertDefinition.builder()
        .discriminator(alert.getDiscriminator())
        .name(alert.getName())
        .payload(alert.getPayload())
        .build();
  }
}
