package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.dto.BulkItem;
import com.silenteight.hsbc.bridge.bulk.repository.BulkWriteRepository;
import com.silenteight.hsbc.bridge.rest.model.input.Alerts;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAcceptedResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateBulkUseCase {

  private final BulkWriteRepository writeRepository;

  public BulkAcceptedResponse recommend(Alerts alerts) {
    if (alerts.isEmpty()) {
      throw new IllegalArgumentException("No alerts have been provided.");
    }

    var result = writeRepository.createBulk(alerts);

    var response = new BulkAcceptedResponse();
    response.setBulkId(result.getBulkId());
    response.setRequestedAlerts(getRequestedAlerts(result.getBulkItems()));

    return response;
  }

  private List<BulkAlertItem> getRequestedAlerts(List<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }
}
