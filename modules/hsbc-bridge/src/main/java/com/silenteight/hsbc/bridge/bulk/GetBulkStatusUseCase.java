package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatusResponse;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetBulkStatusUseCase {

  private final BulkQueryRepository bulkQueryRepository;

  public BulkStatusResponse getStatus(UUID id) {
    var result = bulkQueryRepository.findById(id);

    var response = new BulkStatusResponse();
    response.setBulkId(id);
    response.setBulkStatus(BulkStatus.fromValue(result.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(result.getItems()));

    return response;
  }

  private List<BulkAlertItem> getRequestedAlerts(Collection<BulkItem> bulkItems) {
    return bulkItems.stream().map(r -> {
      var bulkItem = new BulkAlertItem();
      bulkItem.setId(r.getAlertCaseId());
      bulkItem.setStatus(BulkStatus.fromValue(r.getStatus().name()));
      return bulkItem;
    }).collect(Collectors.toList());
  }
}
