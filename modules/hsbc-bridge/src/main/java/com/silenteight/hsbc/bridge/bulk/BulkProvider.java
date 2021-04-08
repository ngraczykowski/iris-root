package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BulkWithGivenIdAlreadyCreatedException;
import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository;
import com.silenteight.hsbc.bridge.bulk.rest.input.Alert;
import com.silenteight.hsbc.bridge.bulk.rest.input.HsbcRecommendationRequest;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class BulkProvider {

  private final BulkItemPayloadConverter bulkItemPayloadConverter;
  private final BulkRepository bulkRepository;

  @Transactional
  Bulk getBulk(HsbcRecommendationRequest request) {
    var bulkItems = processAlertsToBulkItems(request.getAlerts());

    return createBulk(request.getBulkId(), bulkItems);
  }

  private Bulk createBulk(String id, List<BulkItem> bulkItems) {
    if (bulkRepository.existsById(id)) {
      throw new BulkWithGivenIdAlreadyCreatedException(id);
    }

    Bulk bulk = new Bulk(id);
    bulkItems.forEach(bulk::addItem);
    return bulkRepository.save(bulk);
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
