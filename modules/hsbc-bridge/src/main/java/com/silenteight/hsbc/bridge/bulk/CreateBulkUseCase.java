package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAcceptedResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class CreateBulkUseCase {

  private final AlertFacade alertFacade;
  private final BulkWriteRepository writeRepository;

  @Transactional
  public BulkAcceptedResponse createBulk(HsbcRecommendationRequest request) {
    var bulkItems = getBulkItems(request.getAlerts());
    var bulk = createBulk(bulkItems);

    var response = new BulkAcceptedResponse();
    response.setBulkId(bulk.getId());
    response.setRequestedAlerts(getRequestedAlerts(bulk.getItems()));

    return response;
  }

  private Bulk createBulk(List<BulkItem> bulkItems) {
    Bulk bulk = new Bulk();
    bulkItems.forEach(bulk::addItem);
    return writeRepository.save(bulk);
  }

  private List<BulkAlertItem> getRequestedAlerts(Collection<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getAlertCaseId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }

  private List<BulkItem> getBulkItems(List<Alert> alerts) {
    return alerts.stream()
        .map(alertFacade::map)
        .map(BulkItem::new)
        .collect(Collectors.toList());
  }
}
