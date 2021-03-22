package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class BulkProvider {

  private final BulkItemPayloadConverter bulkItemPayloadConverter;
  private final BulkWriteRepository writeRepository;

  @Transactional
  Bulk getBulk(HsbcRecommendationRequest request) {
    var bulkItems = processAlertsToBulkItems(request.getAlerts());

    return createBulk(bulkItems);
  }

  private Bulk createBulk(List<BulkItem> bulkItems) {
    Bulk bulk = new Bulk();
    bulkItems.forEach(bulk::addItem);
    return writeRepository.save(bulk);
  }

  private List<BulkItem> processAlertsToBulkItems(List<Alert> alerts) {
    return alerts.stream()
        .map(this::mapToBulkItem)
        .collect(Collectors.toList());
  }

  private BulkItem mapToBulkItem(Alert a) {
    var alertId = a.getSystemInformation().getCaseWithAlertURL().getId();
    var payload = bulkItemPayloadConverter.map(a);
    return new BulkItem(alertId, payload);
  }
}
