package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.dto.BulkItem;
import com.silenteight.hsbc.bridge.bulk.query.GetStatusQuery;
import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository;
import com.silenteight.hsbc.bridge.rest.model.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatus;
import com.silenteight.hsbc.bridge.rest.model.output.BulkStatusResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetBulkStatusUseCase {

  private final BulkQueryRepository bulkQueryRepository;

  public BulkStatusResponse getStatus(UUID id) {
    var result = bulkQueryRepository.getStatus(new GetStatusQuery(id));

    var response = new BulkStatusResponse();
    response.setBulkId(id);
    response.setBulkStatus(BulkStatus.fromValue(result.getBulkStatus().name()));
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
