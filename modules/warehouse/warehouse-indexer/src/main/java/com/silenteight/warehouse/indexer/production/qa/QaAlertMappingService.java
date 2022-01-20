package com.silenteight.warehouse.indexer.production.qa;

import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.QaAlertMapper;
import com.silenteight.warehouse.indexer.production.qa.indextracking.QaAlertWithIndex;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class QaAlertMappingService {

  List<MapWithIndex> mapFields(List<QaAlertWithIndex> alerts) {
    return alerts.stream()
        .map(this::toMapWithIndex)
        .collect(toList());
  }

  private MapWithIndex toMapWithIndex(QaAlertWithIndex alert) {
    Map<String, Object> payload = QaAlertMapper.convertAlertToAttributes(alert.getAlert());
    String discriminator = alert.getDiscriminator();
    String indexName = alert.getIndexName();

    return new MapWithIndex(indexName, discriminator, payload);
  }
}
