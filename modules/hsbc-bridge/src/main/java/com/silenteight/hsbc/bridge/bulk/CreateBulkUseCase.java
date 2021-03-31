package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAcceptedResponse;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateBulkUseCase {

  private final BulkProvider bulkProvider;
  private final ApplicationEventPublisher eventPublisher;

  public BulkAcceptedResponse createBulk(HsbcRecommendationRequest request) {
    Bulk bulk = bulkProvider.getBulk(request);

    eventPublisher.publishEvent(new BulkStoredEvent(bulk.getId()));

    var response = new BulkAcceptedResponse();
    response.setBulkId(bulk.getId());
    response.setRequestedAlerts(getRequestedAlerts(bulk.getItems()));

    return response;
  }

  private List<BulkAlertItem> getRequestedAlerts(Collection<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getAlertExternalId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }

}
