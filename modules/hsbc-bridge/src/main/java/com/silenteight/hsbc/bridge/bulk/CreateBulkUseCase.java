package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.event.BulkStoredEvent;
import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository;
import com.silenteight.hsbc.bridge.rest.model.input.Alert;
import com.silenteight.hsbc.bridge.rest.model.input.HsbcRecommendationRequest;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAcceptedResponse;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class CreateBulkUseCase {

  private final BulkItemPayloadConverter bulkItemPayloadConverter;
  private final BulkWriteRepository writeRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public BulkAcceptedResponse createBulk(HsbcRecommendationRequest request) {
    var bulkItems = processAlertsToBulkItems(request.getAlerts());
    var bulk = createBulk(bulkItems);

    eventPublisher.publishEvent(new BulkStoredEvent(bulk.getId()));

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
      bulkItem.setId(r.getAlertExternalId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
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
