package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.repository.BulkQueryRepository;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkAlertItem;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkProcessingStatusResponse;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.rest.output.BulkStatusResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PROCESSING;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.STORED;

@RequiredArgsConstructor
public class GetBulkStatusUseCase {

  private final BulkQueryRepository bulkQueryRepository;

  public BulkStatusResponse getStatus(String id) {
    var result = bulkQueryRepository.findById(id);

    var response = new BulkStatusResponse();
    response.setBulkId(id);
    response.setBulkStatus(BulkStatus.fromValue(result.getStatus().name()));
    response.setRequestedAlerts(getRequestedAlerts(result.getItems()));

    return response;
  }

  public BulkProcessingStatusResponse isProcessing() {
    var result = bulkQueryRepository.existsByStatusIn(List.of(STORED, PROCESSING));

    var response = new BulkProcessingStatusResponse();
    response.setIsAdjudicationEngineProcessing(result);

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
